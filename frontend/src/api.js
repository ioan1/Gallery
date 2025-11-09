const API_BASE = "https://gallery.redby.fr"; // mettre l'URL de ton service API

export async function fetchYears() {
  const response = await fetch(`${API_BASE}/years`);
  return response.json();
}

export async function fetchAlbums(year) {
  const response = await fetch(`${API_BASE}/albums/${year}`);
  return response.json();
}
