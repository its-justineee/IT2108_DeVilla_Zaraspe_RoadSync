package com.example.roadsync

object AdminDashboardHelper {

    fun translateStatus(
        status: String,
        isTagalog: Boolean
    ): String {
        return when (status.lowercase()) {
            "reported" -> if (isTagalog) "Iniulat" else "Reported"
            "verified" -> if (isTagalog) "Beripikado" else "Verified"
            "resolved" -> if (isTagalog) "Nalutas" else "Resolved"
            else -> status
        }
    }
}