package com.example.proyectofinaldisenomovil.data.repository


//@Singleton
//class UserRepositoryImpl @Inject constructor(
//
//) : UserRepository {
//
//}


///**
// * Contract for all user-profile persistence operations.
// *
// * Implemented by [com.vivetuzona.data.repository.FirebaseUserRepository].
// * The UI layer depends only on this interface (Dependency Inversion).
// */
//interface UserRepository {
//
//    /**
//     * Returns the [User] with the given [uid], or `null` if not found.
//     * Used for single reads (profile screen, deep-link resolution).
//     */
//    suspend fun getUserById(uid: String): User?
//
//    /**
//     * Persists a new [User] document after Firebase Auth account creation.
//     * Should be called once during registration.
//     */
//    suspend fun createUser(user: User)
//
//    /**
//     * Overwrites updatable fields on an existing [User] document.
//     * Always sets [User.updatedAt] to the current server timestamp.
//     */
//    suspend fun updateUser(user: User)
//
//    /**
//     * Soft-deletes the account by setting [User.isActive] = false.
//     * The Firebase Auth account is NOT deleted here — call
//     * `FirebaseAuth.currentUser.delete()` separately.
//     */
//    suspend fun deleteAccount(uid: String)
//
//    /**
//     * Returns a real-time [Flow] of the user document.
//     * Emits `null` when the document is deleted or the user is inactive.
//     */
//    fun observeUser(uid: String): Flow<User?>

    /**
     * Persists the FCM device token so the backend (Cloud Function) can
     * target this specific device for push notifications.
     */
//    suspend fun saveFcmToken(uid: String, token: String)
//}
