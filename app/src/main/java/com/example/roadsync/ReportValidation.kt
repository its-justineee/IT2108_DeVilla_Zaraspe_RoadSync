
package com.example.roadsync

/**
 * Represents the result of an incident report validation.
 */
data class ReportValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

/**
 * Contract for all incident report validators.
 */
interface ReportValidator {
    fun validate(report: IncidentReport): ReportValidationResult
}

/**
 * Provides common validation rules for incident reports.
 *
 * Subclasses implement their own category-specific validation.
 */
abstract class BaseReportValidator : ReportValidator {

    final override fun validate(
        report: IncidentReport
    ): ReportValidationResult {

        if (report.type.isBlank()) {
            return ReportValidationResult(
                false,
                "Please select an incident type."
            )
        }

        if (report.description.isBlank()) {
            return ReportValidationResult(
                false,
                "Please enter an incident description."
            )
        }

        if (
            !report.latitude.isFinite() ||
            report.latitude !in -90.0..90.0
        ) {
            return ReportValidationResult(
                false,
                "Invalid latitude."
            )
        }

        if (
            !report.longitude.isFinite() ||
            report.longitude !in -180.0..180.0
        ) {
            return ReportValidationResult(
                false,
                "Invalid longitude."
            )
        }

        return validateSpecific(report)
    }

    /**
     * Implemented by subclasses for specific validation rules.
     */
    protected abstract fun validateSpecific(
        report: IncidentReport
    ): ReportValidationResult
}

/**
 * RoadSync's incident report validator.
 *
 * Supported categories can be supplied through the constructor,
 * allowing additional categories without modifying this class.
 */
class RoadSyncReportValidator(
    private val supportedTypes: Set<String> =
        DEFAULT_SUPPORTED_INCIDENT_TYPES
) : BaseReportValidator() {

    override fun validateSpecific(
        report: IncidentReport
    ): ReportValidationResult {

        return if (report.type.trim() in supportedTypes) {
            ReportValidationResult(true)
        } else {
            ReportValidationResult(
                false,
                "Unsupported incident category."
            )
        }
    }

    private companion object {
        val DEFAULT_SUPPORTED_INCIDENT_TYPES = setOf(
            "Traffic / Heavy Traffic",
            "Flooded Area",
            "Accident",
            "Pothole / Road Damage",
            "Road Blockage / Closure",
            "Other Road Hazards"
        )
    }
}
