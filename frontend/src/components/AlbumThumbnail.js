import React from "react";

export default function AlbumThumbnail({ album }) {
  return (
    <div
      style={{
        border: "1px solid #ddd",
        borderRadius: "8px",
        padding: "8px",
        width: "150px",
        textAlign: "center",
      }}
    >
      <div
        style={{
          height: "100px",
          backgroundColor: "#f0f0f0",
          borderRadius: "4px",
          marginBottom: "8px",
        }}
      >
        {/* Ici on affichera la miniature plus tard */}
        <span style={{ lineHeight: "100px", color: "#999" }}>Image</span>
      </div>
      <div>{album.name}</div>
      <div style={{ fontSize: "0.8em", color: "#666" }}>{album.date}</div>
    </div>
  );
}
