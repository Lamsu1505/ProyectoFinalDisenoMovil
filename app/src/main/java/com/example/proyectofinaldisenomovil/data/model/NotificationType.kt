package com.example.proyectofinaldisenomovil.data.model
/**
 * Categorizes in-app notifications so the UI can render the appropriate icon.
 *
 * | Type                  | Trigger                                                    |
 * |-----------------------|------------------------------------------------------------|
 * | NEW_EVENT_NEARBY      | A new VERIFIED event is created within the user's radius   |
 * | COMMENT_ON_MY_EVENT   | Someone comments on an event the current user authored     |
 * | EVENT_VERIFIED        | A moderator approved the user's pending event              |
 * | EVENT_REJECTED        | A moderator rejected the user's pending event              |
 */
enum class NotificationType {
    NEW_EVENT_NEARBY,
    NEW_EVENT,
    COMMENT,
    VERIFIED,
    REJECTED,
    FINALIZED
}