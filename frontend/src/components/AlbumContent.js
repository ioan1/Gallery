import React, { useEffect, useState } from "react";
import { fetchAlbumContent } from "../api";
import AuthImage from "./AuthImage";

// Render a list of items (files or directories)
function renderList(items, year, albumId, path = "") {
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
            <AuthImage
              key={itemPath}
              // For now the thumbnails service will serve a placeholder image.
              // Parameters will be added later.
              src={`/thumbnails/small/${year}/${albumId}/${encodedItemPath}`}
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
          );
        } else if (item.type === "dir") {
          const dirPath = basePath ? `${basePath}/${item.name}` : item.name;
          return (
            <div key={dirPath} style={{ minWidth: 120 }}>
              <div style={{ fontWeight: "bold", marginBottom: 4 }}>{item.name}/</div>
              {item.children && item.children.length > 0 && (
                <div style={{ marginLeft: 10 }}>
                  {renderList(item.children, year, albumId, dirPath)}
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

  useEffect(() => {
    setLoading(true);
    setError(null);
    fetchAlbumContent(year, albumId)
      .then(setTree)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [year, albumId]);

  if (loading) return <p>Loading album content...</p>;
  if (error) return <p style={{ color: "red" }}>{error}</p>;
  if (!tree || !Array.isArray(tree) || tree.length === 0) return <p>No content found.</p>;

  return (
    <div>
      {renderList(tree, year, albumId)}
    </div>
  );
}