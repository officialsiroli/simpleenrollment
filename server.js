const path = require('path');
const express = require('express');
const { ensureDataFile, readDatabase, writeDatabase } = require('./storage');
const crypto = require('crypto');

const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(express.static(path.join(__dirname, 'public')));

// Ensure data file exists on startup
ensureDataFile();

// Helper to validate plate
function isValidPlate(plate) {
	return typeof plate === 'string' && plate.trim().length > 0 && plate.trim().length <= 32;
}

// GET active vehicles
app.get('/api/active', async (req, res) => {
	try {
		const db = await readDatabase();
		return res.json({ active: db.active });
	} catch (err) {
		console.error('Error reading active vehicles', err);
		return res.status(500).json({ error: 'Failed to read active vehicles' });
	}
});

// GET history (optional limit param)
app.get('/api/history', async (req, res) => {
	try {
		const db = await readDatabase();
		const limit = Math.max(0, Math.min(1000, parseInt(req.query.limit || '100', 10)));
		const history = db.history
			.sort((a, b) => new Date(b.exitTime).getTime() - new Date(a.exitTime).getTime())
			.slice(0, limit);
		return res.json({ history });
	} catch (err) {
		console.error('Error reading history', err);
		return res.status(500).json({ error: 'Failed to read history' });
	}
});

// POST enter
app.post('/api/enter', async (req, res) => {
	try {
		const { plate, make, model, color } = req.body || {};
		if (!isValidPlate(plate)) {
			return res.status(400).json({ error: 'Invalid plate' });
		}

		const db = await readDatabase();
		const nowIso = new Date().toISOString();
		const id = crypto.randomUUID();
		const entry = {
			id,
			plate: plate.trim().toUpperCase(),
			make: (make || '').trim(),
			model: (model || '').trim(),
			color: (color || '').trim(),
			entryTime: nowIso
		};

		// Prevent duplicate active by same plate
		const alreadyActive = db.active.find(v => v.plate === entry.plate);
		if (alreadyActive) {
			return res.status(409).json({ error: 'Vehicle already active', active: alreadyActive });
		}

		db.active.push(entry);
		await writeDatabase(db);
		return res.status(201).json({ vehicle: entry });
	} catch (err) {
		console.error('Error entering vehicle', err);
		return res.status(500).json({ error: 'Failed to register vehicle' });
	}
});

// POST exit by id or plate
app.post('/api/exit', async (req, res) => {
	try {
		const { id, plate } = req.body || {};
		const db = await readDatabase();
		let index = -1;
		if (id) {
			index = db.active.findIndex(v => v.id === id);
		} else if (plate) {
			index = db.active.findIndex(v => v.plate === String(plate).trim().toUpperCase());
		}
		if (index === -1) {
			return res.status(404).json({ error: 'Active vehicle not found' });
		}
		const vehicle = db.active.splice(index, 1)[0];
		const exitTime = new Date().toISOString();
		const completed = { ...vehicle, exitTime };
		db.history.push(completed);
		await writeDatabase(db);
		return res.json({ vehicle: completed });
	} catch (err) {
		console.error('Error exiting vehicle', err);
		return res.status(500).json({ error: 'Failed to exit vehicle' });
	}
});

// Health check
app.get('/api/health', (req, res) => {
	return res.json({ status: 'ok' });
});

app.listen(PORT, () => {
	console.log(`Parking tracker listening on http://localhost:${PORT}`);
});

