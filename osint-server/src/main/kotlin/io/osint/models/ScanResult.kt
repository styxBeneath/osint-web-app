package io.osint.models

import kotlinx.serialization.Serializable

@Serializable
data class ScanResult(val id: Int, val domain: String, val startTime: Long, val endTime: Long?, val results: String?)
