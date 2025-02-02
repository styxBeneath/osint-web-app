export const parseScanResults = (results) => {
    try {
      const parsedResults = JSON.parse(results);
      
      if (parsedResults && parsedResults.results) {
        return parsedResults.results;
      }
    } catch (error) {
      console.error('Failed to parse results:', error);
    }
  
    return results;
  };
  