import { useState } from 'react';
import axios from 'axios';

const useScanData = () => {
  const [scanData, setScanData] = useState([]);

  const fetchScanData = async () => {
    try {
      const response = await axios.get('http://localhost:8080/scans');
      setScanData(response.data);
      return response.data;
    } catch (error) {
      console.error('Error fetching scan data:', error);
      return [];
    }
  };

  return { scanData, fetchScanData };
};

export default useScanData;
