import React from "react";

export default function YearList({ years, selectedYear, onSelect }) {
  return (
    <div style={{ display: "flex", flexWrap: "wrap", gap: "8px" }}>
      {years.map((year) => (
        <button
          key={year}
          onClick={() => onSelect(year)}
          style={{
            padding: "8px 12px",
            backgroundColor: year === selectedYear ? "#007bff" : "#eee",
            color: year === selectedYear ? "#fff" : "#000",
            border: "none",
            borderRadius: "4px",
            cursor: "pointer",
          }}
        >
          {year}
        </button>
      ))}
    </div>
  );
}
