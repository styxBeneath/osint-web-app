package io.osint.services


import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

object AmassService {

    private val AMASS_API_DOMAIN = System.getenv("AMASS_API_DOMAIN")
    private val AMASS_API_PORT = System.getenv("AMASS_API_PORT")
    private val AMASS_API_SCAN_ENDPOINT = System.getenv("AMASS_API_SCAN_ENDPOINT")
    private val AMASS_API_SCAN_URL = "http://${AMASS_API_DOMAIN}:${AMASS_API_PORT}/${AMASS_API_SCAN_ENDPOINT}"

    suspend fun runAmass(domain: String): HttpResponse {
        val client = HttpClient(CIO) {
            install(HttpTimeout) {
                requestTimeoutMillis = 300000
                connectTimeoutMillis = 300000
                socketTimeoutMillis = 300000
            }
        }

        return client.get(AMASS_API_SCAN_URL) {
            url { parameters.append("domain", domain) }
        }
    }
}
