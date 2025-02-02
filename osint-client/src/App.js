import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Card from './components/Card';
import Modal from 'react-modal';
import ScanModal from './components/ScanModal';
import ScanDetailsModal from './components/ScanDetailsModal';

Modal.setAppElement('#root');

function App() {
  const [showScanModal, setShowScanModal] = useState(false);
  const [selectedScan, setSelectedScan] = useState(null);
  const [scanData, setScanData] = useState([]);

  useEffect(() => {
    const fetchScanData = async () => {
      try {
        const response = await axios.get('http://localhost:8080/scans');
        setScanData(response.data);
      } catch (error) {
        console.error('Error fetching scan data:', error);
      }
    };

    fetchScanData();
  }, []);

  const handleScanStart = async (domain) => {
    try {
      const response = await axios.post('http://localhost:8080/scan', { domain });
      setScanData((prevData) => [...prevData, response.data]);
      setShowScanModal(false);
    } catch (error) {
      console.error('Scan request failed.');
    }
  };

  const handleCloseScanModal = () => {
    setShowScanModal(false);
  };

  const handleSelectScan = (scan) => {
    setSelectedScan(scan);
  };

  const handleCloseDetailsModal = () => {
    setSelectedScan(null);
  };

  return (
    <div className="App">
      <header>
        <button className="scan-button" onClick={() => setShowScanModal(true)}>+ Scan</button>
      </header>
      <div className="cards-container">
        {scanData.map((scan) => (
          <Card key={scan.id} scan={scan} onSelectScan={handleSelectScan} />
        ))}
      </div>

      <Modal isOpen={showScanModal} onRequestClose={handleCloseScanModal} contentLabel="Start Scan">
        <ScanModal onScanStart={handleScanStart} onClose={handleCloseScanModal} />
      </Modal>

      <ScanDetailsModal isOpen={!!selectedScan} onClose={handleCloseDetailsModal} scan={selectedScan} />
    </div>
  );
}

export default App;
