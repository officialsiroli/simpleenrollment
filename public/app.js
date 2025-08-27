function $(sel, root = document) { return root.querySelector(sel); }
function $all(sel, root = document) { return Array.from(root.querySelectorAll(sel)); }

function formatDate(iso) {
  const d = new Date(iso);
  return d.toLocaleString();
}

function formatDuration(ms) {
  if (ms < 0) ms = 0;
  const totalSeconds = Math.floor(ms / 1000);
  const hours = Math.floor(totalSeconds / 3600);
  const minutes = Math.floor((totalSeconds % 3600) / 60);
  const seconds = totalSeconds % 60;
  const parts = [];
  if (hours) parts.push(String(hours).padStart(2, '0'));
  parts.push(String(minutes).padStart(2, '0'));
  parts.push(String(seconds).padStart(2, '0'));
  return parts.join(':');
}

async function api(path, opts) {
  const res = await fetch(path, {
    headers: { 'Content-Type': 'application/json' },
    ...opts,
  });
  if (!res.ok) {
    let msg = 'Request failed';
    try { const d = await res.json(); msg = d.error || msg; } catch {}
    throw new Error(msg);
  }
  return res.json();
}

function renderActive(active) {
  const tbody = $('#active-table tbody');
  tbody.innerHTML = '';
  for (const v of active) {
    const tr = document.createElement('tr');
    tr.dataset.id = v.id;
    tr.innerHTML = `
      <td>${v.plate}</td>
      <td>${v.make || ''}</td>
      <td>${v.model || ''}</td>
      <td>${v.color || ''}</td>
      <td>${formatDate(v.entryTime)}</td>
      <td class="duration" data-entry="${v.entryTime}"></td>
      <td><button class="danger exit-btn">Exit</button></td>
    `;
    tbody.appendChild(tr);
  }
}

function tickDurations() {
  const now = Date.now();
  $all('#active-table tbody .duration').forEach(el => {
    const iso = el.getAttribute('data-entry');
    const ms = now - new Date(iso).getTime();
    el.textContent = formatDuration(ms);
  });
}

async function loadActive() {
  const { active } = await api('/api/active');
  renderActive(active);
  tickDurations();
}

function renderHistory(history) {
  const tbody = $('#history-table tbody');
  tbody.innerHTML = '';
  for (const v of history) {
    const entry = new Date(v.entryTime).getTime();
    const exit = new Date(v.exitTime).getTime();
    const parked = formatDuration(exit - entry);
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${v.plate}</td>
      <td>${v.make || ''}</td>
      <td>${v.model || ''}</td>
      <td>${v.color || ''}</td>
      <td>${formatDate(v.entryTime)}</td>
      <td>${formatDate(v.exitTime)}</td>
      <td>${parked}</td>
    `;
    tbody.appendChild(tr);
  }
}

async function loadHistory() {
  const { history } = await api('/api/history?limit=100');
  renderHistory(history);
}

async function handleEnterSubmit(ev) {
  ev.preventDefault();
  const status = $('#enter-status');
  status.textContent = 'Submitting...';
  const data = {
    plate: $('#plate').value.trim(),
    make: $('#make').value.trim(),
    model: $('#model').value.trim(),
    color: $('#color').value.trim(),
  };
  try {
    await api('/api/enter', { method: 'POST', body: JSON.stringify(data) });
    status.textContent = 'Vehicle entered';
    $('#enter-form').reset();
    await loadActive();
  } catch (err) {
    status.textContent = err.message || 'Failed';
  }
}

async function handleExitClick(ev) {
  const btn = ev.target.closest('.exit-btn');
  if (!btn) return;
  const tr = btn.closest('tr');
  const id = tr?.dataset?.id;
  if (!id) return;
  btn.disabled = true;
  btn.textContent = 'Exiting...';
  try {
    await api('/api/exit', { method: 'POST', body: JSON.stringify({ id }) });
    await loadActive();
    await loadHistory();
  } catch (err) {
    alert(err.message || 'Failed');
  } finally {
    btn.disabled = false;
    btn.textContent = 'Exit';
  }
}

function init() {
  $('#enter-form').addEventListener('submit', handleEnterSubmit);
  $('#refresh-active').addEventListener('click', loadActive);
  $('#refresh-history').addEventListener('click', loadHistory);
  $('#active-table').addEventListener('click', handleExitClick);
  loadActive();
  loadHistory();
  setInterval(tickDurations, 1000);
}

window.addEventListener('DOMContentLoaded', init);

