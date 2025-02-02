package io.osint.models

import kotlinx.serialization.Serializable

@Serializable
data class ScanRequest(val domain: String)
