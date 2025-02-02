package io.osint.utils

class ScanInsertionException(message: String) : Exception(message)

fun Int?.orThrowScanInsertionError(): Int {
    return this ?: throw ScanInsertionException("Failed to insert scan record into the database.")
}