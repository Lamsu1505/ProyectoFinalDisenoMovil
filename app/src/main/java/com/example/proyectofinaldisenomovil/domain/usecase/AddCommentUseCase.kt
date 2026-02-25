package com.example.proyectofinaldisenomovil.domain.usecase

import com.example.proyectofinaldisenomovil.data.model.AppNotification
import com.example.proyectofinaldisenomovil.data.model.Comment
import com.example.proyectofinaldisenomovil.data.model.NotificationType
import com.example.proyectofinaldisenomovil.data.model.ReputationReason
import com.example.proyectofinaldisenomovil.domain.repository.CommentRepository
import com.example.proyectofinaldisenomovil.domain.repository.NotificationRepository
import com.example.proyectofinaldisenomovil.domain.repository.ReputationRepository
import javax.inject.Inject

/**
 * Adds a comment to an event after content moderation.
 *
 * Side effects:
 * 1. Persists the comment (with [Comment.isInappropriate] set by AI check).
 * 2. If appropriate, notifies the event author.
 * 3. Awards [ReputationReason.COMMENT_ADDED] points to the commenter.
 */
class AddCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
    private val reputationRepository: ReputationRepository,
    private val notificationRepository: NotificationRepository,
) {
    sealed class Result {
        object Success : Result()
        data class Success_Flagged(val suggestedText: String) : Result()
        data class Error(val message: String) : Result()
    }

    suspend operator fun invoke(
        comment: Comment,
        eventAuthorUid: String,
        eventTitle: String,
    ): Result {
        if (comment.text.isBlank())
            return Result.Error("El comentario no puede estar vacío")

        return try {
            commentRepository.addComment(comment)

            if (comment.isInappropriate) {
                // Still stored but returns special result so UI can warn the user
                return Result.Success_Flagged("Tu comentario fue marcado como inapropiado.")
            }

            // Only reward and notify if the comment passed moderation
            reputationRepository.addPoints(comment.authorUid, ReputationReason.COMMENT_ADDED, comment.eventId)

            if (comment.authorUid != eventAuthorUid) {
                notificationRepository.sendNotification(
                    AppNotification(
                        recipientUid = eventAuthorUid,
                        type         = NotificationType.COMMENT_ON_MY_EVENT,
                        title        = "Nuevo comentario",
                        body         = "${comment.authorName} comentó en \"$eventTitle\"",
                        eventId      = comment.eventId,
                    )
                )
            }
            Result.Success
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error al agregar el comentario")
        }
    }
}
