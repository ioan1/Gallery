import React, { useState, useEffect } from "react";
import { fetchYears, fetchAlbums } from "./api";
import YearList from "./components/YearList";
import AlbumList from "./components/AlbumList";

function App() {
  const [years, setYears] = useState([]);
  const [selectedYear, setSelectedYear] = useState(null);
  const [albums, setAlbums] = useState([]);

  useEffect(() => {
    async function loadYears() {
      const data = await fetchYears();
      setYears(data);
      if (data.length > 0) setSelectedYear(data[data.length - 1]); // dernière année par défaut
    }
    loadYears();
  }, []);

  useEffect(() => {
    if (!selectedYear) return;
    async function loadAlbums() {
      const data = await fetchAlbums(selectedYear);
      setAlbums(data);
    }
    loadAlbums();
  }, [selectedYear]);

  return (
    <div style={{ padding: "24px", fontFamily: "Arial, sans-serif" }}>
      <h1>Galerie Photo</h1>
      <YearList
        years={years}
        selectedYear={selectedYear}
        onSelect={setSelectedYear}
      />
      <h2 style={{ marginTop: "24px" }}>Albums de {selectedYear}</h2>
      <AlbumList albums={albums} />
    </div>
  );
}

export default App;
