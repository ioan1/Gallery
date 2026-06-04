import React, { useEffect, useState } from "react";
import { fetchAlbumContent } from "../api";
import AuthImage from "./AuthImage";
import Lightbox from "./Lightbox";

// Render a list of items (files or directories)
function renderList(items, year, albumId, path = "", onOpen) {
  if (!items || items.length === 0) return null;

  // Normalize path to avoid leading slashes (we want "sub/dir", not "/sub/dir")
  const normalize = (p) => {
    if (!p) return "";
    return p.startsWith("/") ? p.slice(1) : p;
  };

  const basePath = normalize(path);

  return (
    <div style={{
      display: "flex",
      flexWrap: "wrap",
      gap: "10px",
      marginBottom: "10px"
    }}>
      {items.map((item) => {
        if (item.type === "file") {
          const itemPath = basePath ? `${basePath}/${item.name}` : item.name;
          // Encode each path segment but preserve slashes so the thumbnails
          // service can receive nested paths like "gopro/GH011066.MP4".
          const encodedItemPath = itemPath.split("/").map(encodeURIComponent).join("/");
          return (
            <div key={itemPath} style={{ cursor: "pointer" }} onClick={() => onOpen && onOpen(itemPath)}>
              <AuthImage
                key={itemPath}
              // For now the thumbnails service will serve a placeholder image.
              // Parameters will be added later.
              src={`/thumbnails/small/${year}/${albumId}?file=${encodedItemPath}`}
              style={{
                width: 150,
                height: 100,
                objectFit: "cover",
                background: "#ccc",
                borderRadius: 6,
                fontSize: 10,
                overflow: "hidden"
              }}
              title={item.name}
              />
            </div>
          );
        } else if (item.type === "dir") {
          const dirPath = basePath ? `${basePath}/${item.name}` : item.name;
          return (
            <div key={dirPath} style={{ minWidth: 120 }}>
              <div style={{ fontWeight: "bold", marginBottom: 4 }}>{item.name}/</div>
              {item.children && item.children.length > 0 && (
                <div style={{ marginLeft: 10 }}>
                  {renderList(item.children, year, albumId, dirPath, onOpen)}
                </div>
              )}
            </div>
          );
        } else {
          return null;
        }
      })}
    </div>
  );
}

export default function AlbumContent({ year, albumId }) {
  const [tree, setTree] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [fileList, setFileList] = useState([]);
  const [lightboxOpen, setLightboxOpen] = useState(false);
  const [lightboxIndex, setLightboxIndex] = useState(0);

  useEffect(() => {
    setLoading(true);
    setError(null);
    fetchAlbumContent(year, albumId)
      .then(setTree)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [year, albumId]);

  // Construire la liste plate des fichiers pour la navigation dans la lightbox
  useEffect(() => {
    if (!tree) {
      setFileList([]);
      return;
    }

    const files = [];
    function walk(items, base = "") {
      items.forEach((it) => {
        if (it.type === "file") {
          const path = base ? `${base}/${it.name}` : it.name;
          files.push({ path, name: it.name });
        } else if (it.type === "dir" && it.children) {
          const dirPath = base ? `${base}/${it.name}` : it.name;
          walk(it.children, dirPath);
        }
      });
    }

    walk(tree);
    setFileList(files);
  }, [tree]);

  if (loading) return <p>Loading album content...</p>;
  if (error) return <p style={{ color: "red" }}>{error}</p>;
  if (!tree || !Array.isArray(tree) || tree.length === 0) return <p>No content found.</p>;

  const openLightboxAt = (itemPath) => {
    const idx = fileList.findIndex((f) => f.path === itemPath);
    if (idx >= 0) {
      setLightboxIndex(idx);
      setLightboxOpen(true);
    }
  };

  return (
    <div>
      {renderList(tree, year, albumId, "", openLightboxAt)}
      <Lightbox open={lightboxOpen} files={fileList} startIndex={lightboxIndex} year={year} albumId={albumId} onClose={() => setLightboxOpen(false)} />
    </div>
  );
}