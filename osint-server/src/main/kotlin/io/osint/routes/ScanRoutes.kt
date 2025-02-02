package io.osint.routes

import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*
import io.osint.database.Scans
import io.osint.models.ScanRequest
import io.osint.models.ScanResult
import io.osint.services.AmassService
import io.osint.utils.orThrowScanInsertionError
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import java.time.Instant

private val logger = KtorSimpleLogger("OSINT-Server")

fun Application.configureRouting() {
    install(ContentNegotiation) { jackson() }
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
    }

    routing {
        post("/scan") {
            val request = call.receive<ScanRequest>()
            logger.info("Received scan request for domain: ${request.domain}")

            val startTime = Instant.now().epochSecond
            var scanId: Int = -1

            try {
                scanId = newSuspendedTransaction(Dispatchers.IO) {
                    Scans.insert {
                        it[domain] = request.domain
                        it[Scans.startTime] = startTime
                        it[endTime] = null  // ongoing scan
                        it[results] = "Scanning in progress..."
                    } get Scans.id
                }.orThrowScanInsertionError()

                call.respond(
                    HttpStatusCode.Accepted,
                    ScanResult(
                        id = scanId,
                        domain = request.domain,
                        startTime = startTime,
                        endTime = null,
                        results = "Scanning in progress..."
                    )
                )

                val scanResponse = AmassService.runAmass(request.domain)
                val endTime = Instant.now().epochSecond

                if (scanResponse.status == HttpStatusCode.OK) {
                    val results = scanResponse.bodyAsText()
                    logger.info("Amass scan successful for ${request.domain}. Updating database.")

                    newSuspendedTransaction(Dispatchers.IO) {
                        Scans.update({ Scans.id eq scanId }) {
                            it[Scans.endTime] = endTime
                            it[Scans.results] = results
                        }
                    }
                } else {
                    logger.warn("Amass scan failed for ${request.domain}. Status: ${scanResponse.status}")

                    newSuspendedTransaction(Dispatchers.IO) {
                        Scans.deleteWhere { (id eq scanId) }
                    }

                    call.respond(
                        scanResponse.status,
                        "Scan failed. Status: ${scanResponse.status}"
                    )
                }
            } catch (e: Exception) {
                logger.error("Error processing scan for ${request.domain}: ${e.localizedMessage}", e)

                if (scanId != -1) {
                    newSuspendedTransaction(Dispatchers.IO) {
                        Scans.deleteWhere { id eq scanId }
                    }
                }

                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Internal server error: ${e.localizedMessage}"
                )
            }
        }

        get("/scans") {
            logger.info("fetching scan results.")
            try {
                val scans = newSuspendedTransaction(Dispatchers.IO) {
                    Scans.selectAll().map {
                        ScanResult(
                            it[Scans.id],
                            it[Scans.domain],
                            it[Scans.startTime],
                            it[Scans.endTime],
                            it[Scans.results]
                        )
                    }
                }
                logger.info("successfully retrieved ${scans.size} scan results.")
                call.respond(HttpStatusCode.OK, scans)
            } catch (e: Exception) {
                logger.error("failed to fetch scan results: ${e.localizedMessage}", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "failed to fetch scans", "details" to e.localizedMessage)
                )
            }
        }
    }
}
