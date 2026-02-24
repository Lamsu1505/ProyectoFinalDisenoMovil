package com.vivetuzona.domain.repository

import com.vivetuzona.data.model.Comment
import kotlinx.coroutines.flow.Flow

/**
 * Contract for comment persistence within an event's sub-collection.
 *
 * Implemented by [com.vivetuzona.data.repository.FirebaseCommentRepository].
 */
interface CommentRepository {

    /** Real-time stream of visible comments for [eventId], ordered by [Comment.createdAt]. */
    fun observeComments(eventId: String): Flow<List<Comment>>

    /**
     * Persists a new comment.
     * Before calling this, the caller must have run AI content moderation
     * and set [Comment.isInappropriate] accordingly.
     * After persisting, a notification must be sent to the event author.
     */
    suspend fun addComment(comment: Comment)

    /**
     * Hard-deletes a comment.
     * Only the comment author or a moderator may delete.
     */
    suspend fun deleteComment(eventId: String, commentId: String)

    /** Returns the total comment count for an event (for display in the feed card). */
    suspend fun getCommentCount(eventId: String): Int
}
