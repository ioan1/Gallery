import React, { useState, useEffect } from "react";
import { useAuth } from "react-oidc-context";
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

  const auth = useAuth();

  const signOutRedirect = () => {
    const clientId = "4cm803bb86anli21j7nf29lvhh";
    const logoutUri = "https://gallery.redby.fr";
    const cognitoDomain = "https://cognito-idp.us-east-1.amazonaws.com/us-east-1_bHxrl8GNt";
    window.location.href = `${cognitoDomain}/logout?client_id=${clientId}&logout_uri=${encodeURIComponent(logoutUri)}`;
  };

  if (auth.isLoading) {
    return <div>Loading...</div>;
  }

  if (auth.error) {
    return <div>Encountering error... {auth.error.message}</div>;
  }

  if (!auth.isAuthenticated) {
    return (
      <div style={{ padding: "24px", fontFamily: "Arial, sans-serif", textAlign: "center" }}>
        <h1>Galerie Photo</h1>
        <p>Vous devez vous connecter pour accéder à la galerie.</p>
        <button 
          onClick={() => auth.signinRedirect()}
          style={{
            padding: "12px 24px",
            backgroundColor: "#007bff",
            color: "white",
            border: "none",
            borderRadius: "4px",
            fontSize: "16px",
            cursor: "pointer"
          }}
        >
          Se connecter
        </button>
      </div>
    );
  }

  return (
    <div style={{ padding: "24px", fontFamily: "Arial, sans-serif" }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "16px" }}>
        <h1>Galerie Photo</h1>
        <div style={{ display: "flex", alignItems: "center", gap: "16px" }}>
          <span>Bonjour {auth.user?.profile?.email || "Utilisateur"}</span>
          <button 
            onClick={() => auth.removeUser()}
            style={{
              padding: "8px 16px",
              backgroundColor: "#dc3545",
              color: "white",
              border: "none",
              borderRadius: "4px",
              cursor: "pointer"
            }}
          >
            Déconnexion
          </button>
        </div>
      </div>
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
