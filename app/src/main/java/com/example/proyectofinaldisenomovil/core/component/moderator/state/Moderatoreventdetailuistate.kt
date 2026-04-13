package com.example.proyectofinaldisenomovil.core.component.moderator.state

import com.example.proyectofinaldisenomovil.domain.model.Event.Event


/**
 * Immutable UI state for [com.vivetuzona.ui.screens.moderator.ModeratorEventDetailScreen].
 *
 * @param event                    The full event being reviewed. Null while loading.
 * @param currentImageIndex        Index of the currently visible image in the carousel (0-based).
 * @param isLoading                True while the event document is being fetched.
 * @param showRejectDialog         True when the reject confirmation dialog should be visible.
 * @param showLogoutDialog         True when the logout confirmation dialog should be visible.
 * @param rejectionReason          Text typed in the rejection dialog textarea.
 * @param rejectionReasonError     Inline error shown when submitting without a reason.
 * @param isSubmittingVerification True while the verify/reject Firestore write is in progress.
 */
data class Moderatoreventdetailuistate(
    val event: Event? = null,
    val currentImageIndex: Int = 0,
    val isLoading: Boolean = true,
    val showRejectDialog: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val rejectionReason: String = "",
    val rejectionReasonError: String? = null,
    val isSubmittingVerification: Boolean = false,
)