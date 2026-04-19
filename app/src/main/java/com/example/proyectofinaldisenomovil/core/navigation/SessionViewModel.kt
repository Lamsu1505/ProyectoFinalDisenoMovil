package com.example.proyectofinaldisenomovil.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinaldisenomovil.data.local.SessionDataStore
import com.example.proyectofinaldisenomovil.data.repository.MockDataRepository
import com.example.proyectofinaldisenomovil.data.repository.UserRepository
import com.example.proyectofinaldisenomovil.domain.model.BadgeType
import com.example.proyectofinaldisenomovil.domain.model.User.UserLevel
import com.example.proyectofinaldisenomovil.domain.model.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject

sealed interface SessionState {
    data object Loading : SessionState
    data object NotAuthenticated : SessionState
    data class Authenticated(val session: UserSession) : SessionState
}

data class UserProfileData(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val city: String = "",
    val reputationPoints: Int = 0,
    val userLevel: String = "ESPECTADOR",
    val badges: String = "[]",
    val profileImageUrl: String? = null
)

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionDataStore: SessionDataStore,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userProfileData = MutableStateFlow(UserProfileData())
    val userProfileData: StateFlow<UserProfileData> = _userProfileData.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                sessionDataStore.sessionFlow,
                sessionDataStore.userNameFlow,
                sessionDataStore.userLastnameFlow,
                sessionDataStore.userEmailFlow,
                sessionDataStore.userCityFlow,
                sessionDataStore.reputationPointsFlow,
                sessionDataStore.userLevelFlow,
                sessionDataStore.userBadgesFlow,
                sessionDataStore.profileImageUrlFlow
            ) { flows ->
                val session = flows[0] as? UserSession
                val firstName = flows[1] as? String ?: ""
                val lastName = flows[2] as? String ?: ""
                val email = flows[3] as? String ?: ""
                val city = flows[4] as? String ?: ""
                val points = flows[5] as? Int ?: 0
                val level = flows[6] as? String ?: "ESPECTADOR"
                val badges = flows[7] as? String ?: "[]"
                val profileImageUrl = flows[8] as? String
                
                if (session != null) {
                    _userProfileData.value = UserProfileData(
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        city = city,
                        reputationPoints = points,
                        userLevel = level,
                        badges = badges,
                        profileImageUrl = profileImageUrl
                    )
                    
                    val user = userRepository.getUserById(session.userId)
                    if (user != null) {
                        val updatedUser = user.copy(
                            firstName = firstName.ifEmpty { user.firstName },
                            lastName = lastName.ifEmpty { user.lastName },
                            city = city.ifEmpty { user.city },
                            reputationPoints = points,
                            level = try { UserLevel.valueOf(level) } catch (e: Exception) { UserLevel.ESPECTADOR },
                            badges = parseBadges(badges),
                            profileImageUrl = profileImageUrl ?: user.profileImageUrl
                        )
                        MockDataRepository.setLoggedInUser(updatedUser)
                    } else {
                        val newUser = createUserFromDataStore(session.userId, firstName, lastName, email, city, points, level, badges, profileImageUrl)
                        MockDataRepository.setLoggedInUser(newUser)
                    }
                } else {
                    MockDataRepository.setLoggedInUser(null)
                }
            }.collect { }
        }
    }

    private fun createUserFromDataStore(
        uid: String,
        firstName: String,
        lastName: String,
        email: String,
        city: String,
        points: Int,
        level: String,
        badges: String,
        profileImageUrl: String?
    ): com.example.proyectofinaldisenomovil.domain.model.User.User {
        return com.example.proyectofinaldisenomovil.domain.model.User.User(
            uid = uid,
            firstName = firstName,
            lastName = lastName,
            email = email,
            location = null,
            city = city,
            reputationPoints = points,
            level = try { UserLevel.valueOf(level) } catch (e: Exception) { UserLevel.ESPECTADOR },
            badges = parseBadges(badges),
            profileImageUrl = profileImageUrl
        )
    }

    private fun parseBadges(json: String): List<BadgeType> {
        if (json.isEmpty() || json == "[]") return emptyList()
        return try {
            val jsonArray = JSONArray(json)
            (0 until jsonArray.length()).mapNotNull { i ->
                try {
                    BadgeType.valueOf(jsonArray.getString(i))
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    val sessionState: StateFlow<SessionState> = sessionDataStore.sessionFlow
        .map { session ->
            if (session != null) {
                SessionState.Authenticated(session)
            } else {
                SessionState.NotAuthenticated
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SessionState.Loading
        )

    fun saveSession(session: UserSession) {
        viewModelScope.launch {
            sessionDataStore.saveSession(session)
        }
    }

    fun clearSession() {
        viewModelScope.launch {
            sessionDataStore.clearSession()
            _userProfileData.value = UserProfileData()
        }
    }
}
