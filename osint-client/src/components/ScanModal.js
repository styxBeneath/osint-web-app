import React, { useState } from 'react';

function ScanModal({ onScanStart, onClose }) {
  const [domain, setDomain] = useState('');

  const handleSubmit = () => {
    if (domain.trim() !== '') {
      onScanStart(domain);
    }
  };

  return (
    <div className="modal-content">
      <h2>Start Scan</h2>
      <input
        type="text"
        value={domain}
        onChange={(e) => setDomain(e.target.value)}
        placeholder="Enter domain"
      />
      <button onClick={handleSubmit}>Start Scan</button>
      <button onClick={onClose}>Close</button>
    </div>
  );
}

export default ScanModal;
