import React from 'react';
import Modal from 'react-modal';
import { parseScanResults } from '../utils/resultUtils';

Modal.setAppElement('#root');

function ScanDetailsModal({ isOpen, onClose, scan }) {
  if (!scan) return null;

  const parsedResults = parseScanResults(scan.results);

  return (
    <Modal isOpen={isOpen} onRequestClose={onClose} contentLabel="Scan Details">
      <div className="modal-content">
        <h2>Scan Details</h2>
        <p><strong>Domain:</strong> {scan.domain}</p>
        <p><strong>Start Time:</strong> {new Date(scan.startTime).toLocaleString()}</p>
        <p><strong>End Time:</strong> {scan.endTime ? new Date(scan.endTime).toLocaleString() : 'In progress'}</p>
        <p><strong>Results:</strong> {parsedResults}</p>
        <button onClick={onClose}>Close</button>
      </div>
    </Modal>
  );
}

export default ScanDetailsModal;
