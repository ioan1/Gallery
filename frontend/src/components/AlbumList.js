import React from "react";
import AlbumThumbnail from "./AlbumThumbnail";

export default function AlbumList({ albums }) {
  if (!albums || albums.length === 0) {
    return <p>Aucun album disponible.</p>;
  }

  return (
    <div style={{ display: "flex", flexWrap: "wrap" }}>
      {albums.map((album) => (
        <AlbumThumbnail key={album.id} album={album} />
      ))}
    </div>
  );
}
