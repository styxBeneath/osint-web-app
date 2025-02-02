from flask import Flask, request, jsonify
import subprocess
import logging

logging.basicConfig(level=logging.INFO, format="%(asctime)s [%(levelname)s] %(message)s")
logger = logging.getLogger(__name__)

app = Flask(__name__)

@app.route("/scan", methods=["GET"])
def scan():
    domain = request.args.get("domain")
    max_depth = "1"
    
    if not domain:
        logger.warning("Scan request received without a domain.")
        return jsonify({"error": "Domain is required"}), 400

    logger.info(f"Received scan request for domain: {domain}")

    try:
        command = ["amass", "enum", "-d", domain, "-active", "-max-depth", max_depth] # support for other arguments can be implemented
        result = subprocess.run(
            command,
            capture_output=True,
            text=True
        )

        if result.returncode == 0:
            logger.info(f"Amass scan successful for {domain}.")
            return jsonify({"results": result.stdout})
        else:
            logger.error(f"Amass scan failed for {domain}. Return Code: {result.returncode}")
            return jsonify({"error": "Amass scan failed", "details": result.stderr}), 500

    except Exception as e:
        logger.exception(f"Error during scan for {domain}: {str(e)}")
        return jsonify({"error": "Internal server error", "details": str(e)}), 500

if __name__ == "__main__":
    logger.info("Starting Flask server on port 5000.")
    app.run(host="0.0.0.0", port=5000)
