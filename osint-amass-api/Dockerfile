FROM python:3.9

WORKDIR /app

COPY requirements.txt requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

COPY / ./

# detect system architecture
RUN ARCH=$(dpkg --print-architecture); \
    if [ "$ARCH" = "amd64" ]; then \
        ARCH="amd64"; \
    elif [ "$ARCH" = "arm64" ]; then \
        ARCH="arm64"; \
    else \
        echo "unsupported architecture: $ARCH" && exit 1; \
    fi && \
    AMASS_URL="https://github.com/owasp-amass/amass/releases/latest/download/amass_linux_${ARCH}.zip" && \
    apt-get update && \
    apt-get install -y wget unzip && \
    wget $AMASS_URL -O amass.zip && \
    unzip amass.zip && \
    mv amass_Linux_${ARCH}/amass /usr/local/bin/amass && \
    rm -rf amass_Linux_${ARCH} amass.zip

CMD ["python", "app.py"]
