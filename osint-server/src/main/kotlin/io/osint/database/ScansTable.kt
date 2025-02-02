package io.osint.database

import org.jetbrains.exposed.sql.Table

object Scans : Table() {
    val id = integer("id").autoIncrement()
    val domain = varchar("domain", 255)
    val startTime = long("start_time")
    val endTime = long("end_time").nullable()
    val results = text("results").nullable()
    override val primaryKey = PrimaryKey(id)
}
