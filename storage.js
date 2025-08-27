const fs = require('fs');
const path = require('path');

const DATA_DIR = path.join(__dirname, 'data');
const DATA_FILE = path.join(DATA_DIR, 'parking.json');

function ensureDataFile() {
	try {
		if (!fs.existsSync(DATA_DIR)) {
			fs.mkdirSync(DATA_DIR, { recursive: true });
		}
		if (!fs.existsSync(DATA_FILE)) {
			const initial = { active: [], history: [] };
			fs.writeFileSync(DATA_FILE, JSON.stringify(initial, null, 2));
		}
	} catch (err) {
		console.error('Failed to ensure data file', err);
	}
}

async function readDatabase() {
	const raw = await fs.promises.readFile(DATA_FILE, 'utf-8');
	try {
		const parsed = JSON.parse(raw);
		if (!parsed.active || !Array.isArray(parsed.active)) parsed.active = [];
		if (!parsed.history || !Array.isArray(parsed.history)) parsed.history = [];
		return parsed;
	} catch (err) {
		// Reset to empty structure on corruption
		return { active: [], history: [] };
	}
}

async function writeDatabase(db) {
	const tmpFile = DATA_FILE + '.tmp';
	await fs.promises.writeFile(tmpFile, JSON.stringify(db, null, 2));
	await fs.promises.rename(tmpFile, DATA_FILE);
}

module.exports = {
	ensureDataFile,
	readDatabase,
	writeDatabase,
};

