package io.osint.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseConfig {

    private val POSTGRES_HOST = System.getenv("POSTGRES_HOST")
    private val POSTGRES_PORT = System.getenv("POSTGRES_PORT")
    private val POSTGRES_DB = System.getenv("POSTGRES_DB")
    private val DB_USER = System.getenv("POSTGRES_USER")
    private val DB_PASSWORD = System.getenv("POSTGRES_PASSWORD")
    private val DB_URL = "jdbc:postgresql://${POSTGRES_HOST}:${POSTGRES_PORT}/${POSTGRES_DB}"

    fun initDatabase() {
        Database.connect(DB_URL, driver = "org.postgresql.Driver", user = DB_USER, password = DB_PASSWORD)
        transaction { SchemaUtils.create(Scans) }
    }
}
