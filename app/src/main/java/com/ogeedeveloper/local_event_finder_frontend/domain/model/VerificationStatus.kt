package com.ogeedeveloper.local_event_finder_frontend.domain.model

/**
 * Enum for verification status
 */
enum class VerificationStatus {
    NOT_STARTED,
    PENDING,
    CODE_SENT,
    CODE_ENTERED,
    VERIFIED,
    FAILED
}