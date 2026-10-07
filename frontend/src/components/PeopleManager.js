import React, { useCallback, useEffect, useState } from "react";
import { fetchPeople, savePersonLabel } from "../api";
import AuthImage from "./AuthImage";

function imageSource(face) {
  const imageName = face.name.split("/").map(encodeURIComponent).join("/");
  return `/thumbnails/small/${encodeURIComponent(face.year)}/${encodeURIComponent(face.album_id)}?name=${imageName}`;
}

export default function PeopleManager() {
  const [people, setPeople] = useState([]);
  const [labels, setLabels] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [savingId, setSavingId] = useState(null);

  const loadPeople = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const result = await fetchPeople();
      setPeople(result);
      setLabels(Object.fromEntries(result.map((person) => [person.id, person.label || ""])));
    } catch (loadError) {
      setError(loadError.message);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadPeople();
  }, [loadPeople]);

  const handleSave = async (event, person) => {
    event.preventDefault();
    const label = (labels[person.id] || "").trim();
    if (!label) return;

    setSavingId(person.id);
    setError(null);
    try {
      await savePersonLabel(person.face_ids, label);
      await loadPeople();
    } catch (saveError) {
      setError(saveError.message);
    } finally {
      setSavingId(null);
    }
  };

  if (loading) return <p>Chargement des visages…</p>;

  return (
    <section aria-labelledby="people-title" style={{ marginTop: 24 }}>
      <h2 id="people-title">Visages de tous les albums</h2>
      <p>Les visages similaires sont regroupés, même s’ils proviennent d’albums différents.</p>
      {error && <p role="alert" style={{ color: "red" }}>{error}</p>}
      {people.length === 0 ? (
        <p>Aucun visage indexé. Lancez un scan depuis un album pour commencer.</p>
      ) : (
        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))", gap: 12 }}>
          {people.map((person) => {
            const albumYears = [...new Set(person.albums.map((album) => album.year))].join(", ");
            return (
              <form
                key={person.id}
                onSubmit={(event) => handleSave(event, person)}
                style={{ display: "flex", gap: 12, alignItems: "center", padding: 12, border: "1px solid #ddd", borderRadius: 6 }}
              >
                <AuthImage
                  src={imageSource(person.representative)}
                  title={person.label || "Visage détecté"}
                  style={{ width: 80, height: 80, objectFit: "cover", borderRadius: 4, background: "#eee", flexShrink: 0 }}
                />
                <div style={{ flex: 1, minWidth: 0 }}>
                  <div>{person.face_count} visage{person.face_count === 1 ? "" : "s"} dans {person.albums.length} album{person.albums.length === 1 ? "" : "s"} ({albumYears})</div>
                  <label style={{ display: "block", marginTop: 8 }}>
                    <span style={{ display: "block", marginBottom: 4 }}>Nom ou libellé</span>
                    <input
                      type="text"
                      maxLength={128}
                      value={labels[person.id] || ""}
                      onChange={(event) => setLabels((current) => ({ ...current, [person.id]: event.target.value }))}
                      aria-label={`Libellé du groupe de ${person.face_count} visages`}
                      style={{ boxSizing: "border-box", width: "100%", padding: 6 }}
                    />
                  </label>
                  <button type="submit" disabled={!labels[person.id]?.trim() || savingId !== null} style={{ marginTop: 8 }}>
                    {savingId === person.id ? "Enregistrement…" : "Enregistrer"}
                  </button>
                </div>
              </form>
            );
          })}
        </div>
      )}
    </section>
  );
}
