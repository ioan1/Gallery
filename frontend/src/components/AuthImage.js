import React, { useEffect, useState, useRef } from "react";
import { useAuth } from "react-oidc-context";

export default function AuthImage({ src, style, title }) {
  const [imageSrc, setImageSrc] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(false);
  const [isVisible, setIsVisible] = useState(false);
  const auth = useAuth();
  const imgRef = useRef(null);
  const objectUrlRef = useRef(null);

  // Intersection Observer pour lazy loading
  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            setIsVisible(true);
            observer.disconnect();
          }
        });
      },
      {
        rootMargin: "50px" // Commence à charger 50px avant que l'image soit visible
      }
    );

    if (imgRef.current) {
      observer.observe(imgRef.current);
    }

    return () => {
      if (imgRef.current) {
        observer.unobserve(imgRef.current);
      }
    };
  }, []);

  // Chargement de l'image uniquement quand visible
  useEffect(() => {
    if (!isVisible || !auth.user?.access_token) {
      return;
    }

    let cancelled = false;
    setLoading(true);

    async function loadImage() {
      try {
        const response = await fetch(`https://gallery.redby.fr${src}`, {
          headers: {
            'Authorization': `Bearer ${auth.user.access_token}`
          }
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const blob = await response.blob();
        
        if (!cancelled) {
          const objectUrl = URL.createObjectURL(blob);
          objectUrlRef.current = objectUrl;
          setImageSrc(objectUrl);
          setError(false);
        }
      } catch (err) {
        if (!cancelled) {
          console.error("Erreur de chargement de l'image:", err);
          setError(true);
        }
      } finally {
        if (!cancelled) {
          setLoading(false);
        }
      }
    }

    loadImage();

    return () => {
      cancelled = true;
      // Libérer l'URL objet pour économiser la mémoire
      if (objectUrlRef.current) {
        URL.revokeObjectURL(objectUrlRef.current);
        objectUrlRef.current = null;
      }
    };
  }, [isVisible, src, auth.user]);

  if (error) {
    return (
      <div 
        ref={imgRef}
        style={{ 
          ...style, 
          display: "flex", 
          alignItems: "center", 
          justifyContent: "center", 
          backgroundColor: "#f0f0f0",
          color: "#999"
        }}
      >
        ❌
      </div>
    );
  }

  if (!isVisible || loading || !imageSrc) {
    return (
      <div 
        ref={imgRef}
        style={{ 
          ...style, 
          display: "flex", 
          alignItems: "center", 
          justifyContent: "center",
          backgroundColor: "#f5f5f5",
          color: "#999",
          fontSize: "12px"
        }}
      >
        {loading ? "⏳" : ""}
      </div>
    );
  }

  return <img ref={imgRef} src={imageSrc} style={style} alt={title} title={title} />;
}
