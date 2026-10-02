import React, { useState } from "react";
import { fetchAlbumContent, fetchAlbumPeopleCount, indexAlbumImage } from "../api";
import AlbumContent from "./AlbumContent";
import AuthImage from "./AuthImage";

const IMAGE_EXTENSIONS = new Set([".jpg", ".jpeg", ".png", ".gif", ".webp", ".heic"]);

function collectImagePaths(items, basePath = "") {
  return items.flatMap((item) => {
    const itemPath = basePath ? `${basePath}/${item.name}` : item.name;
    if (item.type === "dir") {
      return collectImagePaths(item.children || [], itemPath);
    }

    const extension = item.name.slice(item.name.lastIndexOf(".")).toLowerCase();
    return item.type === "file" && IMAGE_EXTENSIONS.has(extension) ? [itemPath] : [];
  });
}

export default function AlbumThumbnail({ album }) {
  const [showContent, setShowContent] = useState(false);
  const [showPeople, setShowPeople] = useState(false);
  const [peopleCount, setPeopleCount] = useState(null);
  const [peopleLoading, setPeopleLoading] = useState(false);
  const [peopleError, setPeopleError] = useState(null);
  const [scanning, setScanning] = useState(false);
  const [scanProgress, setScanProgress] = useState(null);
  const year = album.date.slice(0, 4);

  const handleClick = () => setShowContent((v) => !v);

  const togglePeople = async (event) => {
    event.stopPropagation();
    const shouldOpen = !showPeople;
    setShowPeople(shouldOpen);

    if (shouldOpen && peopleCount === null && !peopleLoading) {
      setPeopleLoading(true);
      setPeopleError(null);
      try {
        const result = await fetchAlbumPeopleCount(year, album.id);
        setPeopleCount(result.people_count);
      } catch (error) {
        setPeopleError(error.message);
      } finally {
        setPeopleLoading(false);
      }
    }
  };

  const scanAlbum = async (event) => {
    event.stopPropagation();
    if (scanning) return;

    setScanning(true);
    setPeopleError(null);
    setScanProgress(null);

    try {
      const tree = await fetchAlbumContent(year, album.id);
      const imagePaths = collectImagePaths(tree);
      let failures = 0;
      setScanProgress({ completed: 0, total: imagePaths.length, failures, done: false });

      for (let index = 0; index < imagePaths.length; index += 1) {
        try {
          await indexAlbumImage(year, album.id, imagePaths[index]);
        } catch (error) {
          failures += 1;
        }
        setScanProgress({ completed: index + 1, total: imagePaths.length, failures, done: false });
      }

      setScanProgress({ completed: imagePaths.length, total: imagePaths.length, failures, done: true });
      const result = await fetchAlbumPeopleCount(year, album.id);
      setPeopleCount(result.people_count);
    } catch (error) {
      setPeopleError(error.message);
    } finally {
      setScanning(false);
    }
  };

  return (
    <div style={{ width: "100%" }}>
      <div
        onClick={handleClick}
        style={{
          display: "grid",
          gridTemplateColumns: "50px 90px 1fr auto",
          alignItems: "center",
          padding: "8px",
          borderBottom: "1px solid #eee",
          cursor: "pointer",
          background: showContent ? "#f8f8f8" : "white",
        }}
      >
      {album.thumbnail ? ( <AuthImage
                    key={album.thumbnail}
                    src={`/thumbnails/small/${album.date.slice(0, 4)}/${album.id}?name=${album.thumbnail}`}
                    style={{
                      width: "35px",
                      height: "35px",
                      objectFit: "cover",
                      background: "#ccc",
                      borderRadius: 4,
                      fontSize: 10,
                      overflow: "hidden",
                      display: "flex",
                      justifyContent: "center",
                    }}
                    title={album.name}
                    /> ) : null}
        {!album.thumbnail && <div aria-hidden="true" />}
        <div style={{ fontFamily: "monospace" }}>{album.date}</div>
        <div>
          <strong>{album.name}</strong>
        </div>
        <button
          type="button"
          aria-label={showPeople ? "Masquer les personnes détectées" : "Afficher les personnes détectées"}
          aria-expanded={showPeople}
          title="Personnes détectées"
          onClick={togglePeople}
          style={{ cursor: "pointer", padding: "6px 8px", border: "1px solid #ccc", borderRadius: 4, background: "white" }}
        >
          <span aria-hidden="true">👥</span>
        </button>
      </div>
      {showPeople && (
        <div style={{ display: "flex", alignItems: "center", flexWrap: "wrap", gap: 12, padding: "8px 16px" }}>
          {peopleLoading ? <span>Chargement…</span> : peopleError ? <span role="alert">{peopleError}</span> : (
            <strong>{peopleCount === null ? "Nombre inconnu" : `${peopleCount} personne${peopleCount === 1 ? "" : "s"} détectée${peopleCount === 1 ? "" : "s"}`}</strong>
          )}
          <button type="button" onClick={scanAlbum} disabled={scanning || peopleLoading}>
            {scanning ? "Scan en cours…" : "Scanner l’album"}
          </button>
          {scanProgress && (
            <span role="status">
              {scanProgress.done
                ? `Scan terminé : ${scanProgress.completed} image${scanProgress.total === 1 ? "" : "s"}, ${scanProgress.failures} échec${scanProgress.failures === 1 ? "" : "s"}`
                : `${scanProgress.completed}/${scanProgress.total} images traitées, ${scanProgress.failures} échec${scanProgress.failures === 1 ? "" : "s"}`}
            </span>
          )}
        </div>
      )}
      {showContent && (
        <div style={{ margin: "8px 0 16px 0", paddingLeft: 16 }}>
          <AlbumContent year={year} albumId={album.id} />
        </div>
      )}
    </div>
  );
}