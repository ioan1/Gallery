import React, { useEffect, useState, useRef } from "react";
import { useAuth } from "react-oidc-context";

export default function Lightbox({ open, files = [], startIndex = 0, year, albumId, onClose }) {
  const [index, setIndex] = useState(startIndex);
  const [imageUrl, setImageUrl] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const auth = useAuth();
  const objectUrlRef = useRef(null);

  useEffect(() => {
    setIndex(startIndex);
  }, [startIndex]);

  useEffect(() => {
    if (!open) return;
    const handleKey = (e) => {
      if (e.key === "ArrowRight") next();
      if (e.key === "ArrowLeft") prev();
      if (e.key === "Escape") onClose();
    };
    window.addEventListener("keydown", handleKey);
    return () => window.removeEventListener("keydown", handleKey);
  }, [open, index, files]);

  useEffect(() => {
    if (!open) return;
    let cancelled = false;
    async function fetchOriginal() {
      setLoading(true);
      setError(null);
      // cleanup previous
      if (objectUrlRef.current) {
        URL.revokeObjectURL(objectUrlRef.current);
        objectUrlRef.current = null;
      }

      const file = files[index];
      if (!file) {
        setError("No file");
        setLoading(false);
        return;
      }

      const url = `https://gallery.redby.fr/thumbnails/original/${year}/${albumId}?name=${file.path}`;

      try {
        const resp = await fetch(url, {
          headers: {
            Authorization: `Bearer ${auth.user?.access_token}`,
          },
        });
        if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
        const blob = await resp.blob();
        if (!cancelled) {
          const obj = URL.createObjectURL(blob);
          objectUrlRef.current = obj;
          setImageUrl(obj);
        }
      } catch (err) {
        console.error("Lightbox fetch error", err);
        if (!cancelled) setError(err.message || "Erreur");
      } finally {
        if (!cancelled) setLoading(false);
      }
    }

    fetchOriginal();

    return () => {
      cancelled = true;
      if (objectUrlRef.current) {
        URL.revokeObjectURL(objectUrlRef.current);
        objectUrlRef.current = null;
      }
      setImageUrl(null);
    };
  }, [open, index, files, year, albumId, auth.user]);

  if (!open) return null;

  const prev = () => setIndex((i) => (i > 0 ? i - 1 : i));
  const next = () => setIndex((i) => (i < files.length - 1 ? i + 1 : i));

  return (
    <div style={{
      position: "fixed",
      inset: 0,
      backgroundColor: "rgba(0,0,0,0.85)",
      display: "flex",
      alignItems: "center",
      justifyContent: "center",
      zIndex: 9999,
      color: "#fff"
    }} onClick={(e) => { if (e.target === e.currentTarget) onClose(); }}>
      <div style={{ position: "relative", maxWidth: "95%", maxHeight: "95%" }}>
        <button onClick={onClose} style={{ position: "absolute", top: 8, right: 8, background: "transparent", color: "#fff", border: "none", fontSize: 24 }}>✕</button>
        <button onClick={prev} disabled={index === 0} style={{ position: "absolute", left: -60, top: "50%", transform: "translateY(-50%)", background: "transparent", color: "#fff", border: "none", fontSize: 36 }}>‹</button>
        <button onClick={next} disabled={index === files.length - 1} style={{ position: "absolute", right: -60, top: "50%", transform: "translateY(-50%)", background: "transparent", color: "#fff", border: "none", fontSize: 36 }}>›</button>

        <div style={{ display: "flex", alignItems: "center", justifyContent: "center", width: "100%", height: "100%" }}>
          {loading && <div>Chargement...</div>}
          {error && <div style={{ color: "red" }}>{error}</div>}
          {!loading && !error && imageUrl && (
            <img src={imageUrl} alt={files[index]?.name} style={{ maxWidth: "100vw", maxHeight: "100vh", borderRadius: 6 }} />
          )}
        </div>
        <div style={{ marginTop: 8, textAlign: "center", fontSize: 12 }}>{files[index]?.path}</div>
      </div>
    </div>
  );
}
