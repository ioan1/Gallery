import React, { useEffect, useState } from "react";
import { fetchAlbumContent } from "../api";

// Render a list of items (files or directories)
function renderList(items, year, albumId, path = "") {
  if (!items || items.length === 0) return null;

  return (
    <div style={{
      display: "flex",
      flexWrap: "wrap",
      gap: "10px",
      marginBottom: "10px"
    }}>
      {items.map((item) => {
        if (item.type === "file") {
          return (
            <img 
              src={`/picture/${year}/${albumId}/${item.name}`}
              style={{
                width: 200,
                height: 150,
                objectFit: "cover",
                background: "#ccc",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                borderRadius: 6,
                fontSize: 10,
                overflow: "hidden"
              }}
              title={item.name}
            />
          );
        } else if (item.type === "dir") {
          return (
            <div key={path + item.name} style={{ minWidth: 120 }}>
              <div style={{ fontWeight: "bold", marginBottom: 4 }}>{item.name}/</div>
              {item.children && item.children.length > 0 && (
                <div style={{ marginLeft: 10 }}>
                  {renderList(item.children, path + item.name + "/")}
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