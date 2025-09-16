// static/js/app.js

// -------- Token helpers ----------
function getQueryParam(name) {
  const m = new RegExp('[?&]' + name + '=([^&]+)').exec(window.location.search);
  return m ? decodeURIComponent(m[1]) : null;
}
function readMetaJwt() {
  const meta = document.querySelector('meta[name="jwt"]');
  return meta && meta.content ? meta.content : null;
}
function normalizeToken(t) {
  if (!t || t === 'null' || t === 'undefined') return null;
  // optional: quick JWT shape check (3 dot-separated base64url parts)
  if (!/^[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+\.[A-Za-z0-9\-_]+$/.test(t)) return t; // let backend decide
  return t;
}
function ensureJwt() {
  const fromUrl = normalizeToken(getQueryParam('jwt_token'));
  const fromMeta = normalizeToken(readMetaJwt());
  const fromStore = normalizeToken(localStorage.getItem('jwt'));

  const tok = fromUrl || fromMeta || fromStore;
  if (tok) localStorage.setItem('jwt', tok);
  return tok;
}
function stripJwt(url) {
  // remove any existing jwt_token from url to avoid duplicates / "null"
  const u = new URL(url, window.location.origin);
  u.searchParams.delete('jwt_token');
  return u.pathname + (u.search ? u.search : '') + (u.hash || '');
}
function withJwt(url) {
  const tok = ensureJwt();
  const clean = stripJwt(url);
  if (!tok) return clean;
  const sep = clean.includes('?') ? '&' : '?';
  return ${clean}${sep}jwt_token=${encodeURIComponent(tok)};
}


// -------- Navigation / link patching ----------
function patchLinks() {
  document.querySelectorAll('a[href]').forEach(a => {
    const orig = a.getAttribute('href');
    if (!orig || orig.startsWith('#') || orig.startsWith('mailto:')) return;

    // Make copied link already include token
    a.setAttribute('href', withJwt(orig));

    a.addEventListener('click', (e) => {
      if (e.metaKey || e.ctrlKey || e.shiftKey || e.button === 1) {
        a.href = withJwt(orig); // new tab / background
        return;
      }
      e.preventDefault();
      window.location.href = withJwt(orig);
    });
  });
}
function wireForms() {
  document.querySelectorAll('form').forEach(f => {
    f.addEventListener('submit', () => {
      const action = f.getAttribute('action') || window.location.pathname;
      f.setAttribute('action', withJwt(action));
    });
  });
}

// -------- Page initializers ----------
function initProjectsDashboard() {
  ensureJwt();      // capture token from URL/meta
  patchLinks();     // add token to all <a>
  wireForms();      // and to any forms
}
function initProjectDetail() {
  ensureJwt();
  patchLinks();
  wireForms();
}

// -------- Actions (use token in fetch URLs) ----------
async function addMember(e) {
  e.preventDefault();
  const form = e.target.closest('form');
  const pid = form.dataset.projectId;
  const email = form.querySelector('#memberEmail').value.trim();
  if (!email) return false;

  const res = await fetch(withJwt(/projects/${pid}/members), {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: new URLSearchParams({ email })
  });

  if (res.ok) { window.location.href = withJwt(/projects/${pid}); return false; }
  if (res.status === 403) { alert('Forbidden: admin only.'); return false; }
  alert('Failed to add member.'); return false;
}

async function removeMember(e) {
  e.preventDefault();
  const form = e.target.closest('form');
  const pid = form.dataset.projectId;
  const uid = form.dataset.userId;

  const res = await fetch(withJwt(/projects/${pid}/members/${uid}/remove), { method: 'POST' });
  if (res.ok) { window.location.href = withJwt(/projects/${pid}); return false; }
  if (res.status === 403) { alert('Forbidden: admin only.'); return false; }
  alert('Failed to remove member.'); return false;
}

function goToUsers() { window.location.href = withJwt('/users'); return false; }

// expose
window.initProjectsDashboard = initProjectsDashboard;
window.initProjectDetail = initProjectDetail;
window.addMember = addMember;
window.removeMember = removeMember;
window.goToUsers = goToUsers;