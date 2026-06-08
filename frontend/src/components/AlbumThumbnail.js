import React, { useState } from "react";
import AlbumContent from "./AlbumContent";
import AuthImage from "./AuthImage";

export default function AlbumThumbnail({ album }) {
  const [showContent, setShowContent] = useState(false);
  const handleClick = () => setShowContent((v) => !v);

  return (
    <div style={{ width: "100%" }}>
      <div
        onClick={handleClick}
        style={{
          display: "grid",
          gridTemplateColumns: "60px 90px 1fr",
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
                      width: "35",
                      height: "35",
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