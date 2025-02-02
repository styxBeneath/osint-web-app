package io.osint

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.osint.database.DatabaseConfig
import io.osint.routes.configureRouting

fun main() {
    DatabaseConfig.initDatabase()
    embeddedServer(Netty, host = "0.0.0.0", port = 8080, module = Application::configureRouting).start(wait = true)
}
