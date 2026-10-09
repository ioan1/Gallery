const API_BASE = "https://gallery.redby.fr"; // mettre l'URL de ton service API

let authContext = null;
let faceIndexQueue = Promise.resolve();

export function setAuthContext(auth) {
  authContext = auth;
}

async function fetchWithAuth(url, options = {}) {
  const headers = { ...options.headers };
  
  if (authContext?.user?.access_token) {
    headers['Authorization'] = `Bearer ${authContext.user.access_token}`;
  }
  
  const response = await fetch(url, { ...options, headers });
  return response;
}

export async function fetchYears() {
  const response = await fetchWithAuth(`${API_BASE}/years`);
  return response.json();
}

export async function fetchAlbums(year) {
  const response = await fetchWithAuth(`${API_BASE}/albums/${year}`);
  return response.json();
}

export async function fetchAlbumContent(year, albumId) {
  const response = await fetchWithAuth(`${API_BASE}/albums/${year}/${albumId}`);
  if (!response.ok) throw new Error("Erreur lors du chargement du contenu de l'album");
  return response.json();
}

export async function fetchAlbumPeopleCount(year, albumId) {
  const response = await fetchWithAuth(
    `${API_BASE}/faces/${encodeURIComponent(year)}/${encodeURIComponent(albumId)}/people-count`
  );
  const result = await response.json();
  if (!response.ok) {
    throw new Error(result.detail || "Erreur lors du chargement du nombre de personnes");
  }
  return result;
}

export async function fetchPeople() {
  const response = await fetchWithAuth(`${API_BASE}/faces/people`);
  const result = await response.json();
  if (!response.ok) {
    throw new Error(result.detail || "Erreur lors du chargement des visages");
  }
  return result;
}

export async function savePersonLabel(faceIds, label) {
  const response = await fetchWithAuth(`${API_BASE}/faces/people/label`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ face_ids: faceIds, label }),
  });
  const result = await response.json();
  if (!response.ok) {
    throw new Error(result.detail || "Erreur lors de l’enregistrement du libellé");
  }
  return result;
}

export async function assignFacesToPerson(faceIds, personId) {
  const response = await fetchWithAuth(`${API_BASE}/faces/people/assign`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ face_ids: faceIds, person_id: personId }),
  });
  const result = await response.json();
  if (!response.ok) {
    throw new Error(result.detail || "Erreur lors de l’association des visages");
  }
  return result;
}

export function indexAlbumImage(year, albumId, name) {
  const request = faceIndexQueue.then(async () => {
    const response = await fetchWithAuth(`${API_BASE}/faces/index`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ category: year, album: albumId, name }),
    });
    const result = await response.json();
    if (!response.ok) {
      throw new Error(result.detail || "Erreur lors de la détection des visages");
    }
    return result;
  });

  faceIndexQueue = request.catch(() => undefined);
  return request;
}