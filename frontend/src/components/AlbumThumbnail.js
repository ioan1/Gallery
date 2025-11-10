import React from "react";
import AlbumContent from "./AlbumContent";

export default function AlbumThumbnail({ album }) {
  const [showContent, setShowContent] = useState(false);
  const handleClick = () => setShowContent((v) => !v);

return (
    <div style={{ border: "1px solid #ccc", padding: 8, borderRadius: 4, width: 200 }}>
      <div onClick={handleClick} style={{ cursor: "pointer" }}>
        <strong>{album.name}</strong>
        <div>{album.date}</div>
      </div>
      {showContent && (
        <div style={{ marginTop: 8 }}>
          <AlbumContent year={album.date.slice(0, 4)} albumId={album.id} />
        </div>
      )}
    </div>
  );
}
