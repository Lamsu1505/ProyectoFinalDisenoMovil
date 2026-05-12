package com.example.proyectofinaldisenomovil.data.repository.Remote

import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.domain.model.User.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore // Se inyecta una instancia de FirebaseFirestore para interactuar con la base de datos
): UserRepository {

    // Definimos la colección de usuarios donde se almacenarán los datos
    private val collection = firestore.collection("users")

    // StateFlow para observar los cambios en la colección de usuarios
    private val _users = MutableStateFlow<List<User>>(emptyList())
    private val users: StateFlow<List<User>> = _users.asStateFlow()

    init {
        // Escuchar cambios en tiempo real en la colección de usuarios
        collection.addSnapshotListener { snapshot, _ ->
            snapshot?.let {
                // Se actualiza el StateFlow con la lista de usuarios mapeados desde los documentos
                _users.value = it.documents.mapNotNull { snap ->
                    snap.toObject(User::class.java)?.apply { uid = snap.id }
                }
            }
        }
    }

    override suspend fun save(user: User) {
        // Agregar un nuevo documento a la colección de usuarios
        collection.add(user).await()
    }


    override suspend fun getUserById(uid: String): User? {
        // Obtener el documento por ID
        val snapshot = collection.document(uid).get().await()
        // Se retorna el objeto User si existe, se mapea el documento a un objeto User y se asigna el ID del documento de Firestore
        return snapshot.toObject(User::class.java)?.apply { this.uid = snapshot.id }
    }

    override suspend fun login(email: String, password: String): User? {
        // Consultar la colección para encontrar un usuario con el email y password proporcionados
        val snapshot = collection
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .await()

        // Si no se encuentra ningún documento, retornar null
        if (snapshot.documents.isEmpty()) {
            return null
        }

        // Retornar el primer usuario encontrado, se mapea el documento a un objeto User y se asigna el ID del documento de Firestore
        return snapshot.documents.first().toObject(User::class.java)?.apply {
            uid = snapshot.documents.first().id
        }

    }

    override suspend fun createUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: User) {
        // Se actualiza el documento existente dado su ID con los nuevos datos del usuario
        collection.document(user.uid).set(user).await()
    }

    override suspend fun deleteAccount(uid: String) {
        TODO("Not yet implemented")
    }

    override fun observeUser(uid: String): Flow<User?> {
        TODO("Not yet implemented")
    }

    override suspend fun saveFcmToken(uid: String, token: String) {
        TODO("Not yet implemented")
    }

    override fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): User? {
        TODO("Not yet implemented")
    }

    override suspend fun validateCredentials(
        email: String,
        password: String
    ): User? {
        // Consultar la colección para encontrar un usuario con el email y password proporcionados
        val snapshot = collection
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .await()

        // Si no se encuentra ningún documento, retornar null
        if (snapshot.documents.isEmpty()) {
            return null
        }

        // Retornar el primer usuario encontrado, se mapea el documento a un objeto User y se asigna el ID del documento de Firestore
        return snapshot.documents.first().toObject(User::class.java)?.apply {
            uid = snapshot.documents.first().id
        }
    }

    override suspend fun getAllUsers(): List<User> {
        // Obtener todos los documentos de la colección de usuarios
        val snapshot = collection.get().await()
        // Mapear los documentos a objetos User, asignando el ID del documento de Firestore
        return snapshot.documents.mapNotNull {
            it.toObject(User::class.java)?.apply { uid = it.id }
        }
    }

    override suspend fun resetPassword(
        email: String,
        newPassword: String
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun findUserByEmail(email: String): User? {
        TODO("Not yet implemented")
    }

}