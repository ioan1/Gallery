import React, { useEffect, useState } from "react";
import { fetchAlbumContent } from "../api";

function renderTree(node, path = "") {
  if (node.type === "file") {
    return <li key={path + node.name}>{node.name}</li>;
  }
  return (
    <li key={path + node.name} style={{
      display: "block",
      textDecoration: "none",
      border: "1px dashed gray",
      padding: "5px",
      margin: "5px",
      height: "50px",
      width: "50px"
    }}>
      <strong>{node.name}/</strong>
      {node.children && node.children.length > 0 && (
        <ul style={{
          display: "flex",
          flexDirection: "row",
          flexWrap: "wrap",
          alignContent: "flex-start",
          alignItems: "flex-start",
          justifyContent: "flex-start"
        }}>
          {node.children.map((child) =>
            renderTree(child, path + node.name + "/")
          )}
        </ul>
      )}
    </li>
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
  if (!tree) return <p>No content found.</p>;

  return (
    <div>
      <h3>Album Content</h3>
      <ul>{renderTree(tree)}</ul>
    </div>
  );
}