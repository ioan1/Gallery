import React, { useState } from "react";
import AlbumContent from "./AlbumContent";

export default function AlbumThumbnail({ album }) {
  const [showContent, setShowContent] = useState(false);
  const handleClick = () => setShowContent((v) => !v);

  return (
    <div style={{ width: "100%" }}>
      <div
        onClick={handleClick}
        style={{
          display: "grid",
          gridTemplateColumns: "60px 120px 1fr",
          alignItems: "center",
          gap: "16px",
          padding: "8px",
          borderBottom: "1px solid #eee",
          cursor: "pointer",
          background: showContent ? "#f8f8f8" : "white",
        }}
      >
        <div
          style={{
            width: 48,
            height: 48,
            background: "#ccc",
            borderRadius: 6,
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
          }}
        />
        <div style={{ fontFamily: "monospace" }}>{album.date}</div>
        <div>
          <strong>{album.name}</strong>
        </div>
      </div>
      {showContent && (
        <div style={{ margin: "8px 0 16px 0", paddingLeft: 16 }}>
          <AlbumContent year={album.date.slice(0, 4)} albumId={album.id} />
        </div>
      )}
    </div>
  );
}