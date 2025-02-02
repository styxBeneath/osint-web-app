import React from 'react';
import { formatDate } from '../utils/dateUtils';

function Card({ scan, onSelectScan }) {
  const { domain, startTime, endTime } = scan;
  const formattedStartTime = formatDate(startTime);
  const formattedEndTime = endTime ? formatDate(endTime) : 'In progress';

  return (
    <div className="card" onClick={() => onSelectScan(scan)}>
      <h3>{domain}</h3>
      <p>Start Time: {formattedStartTime}</p>
      <p>End Time: {formattedEndTime}</p>
    </div>
  );
}

export default Card;
