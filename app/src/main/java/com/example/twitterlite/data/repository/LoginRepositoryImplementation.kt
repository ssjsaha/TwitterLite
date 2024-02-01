package com.example.twitterlite.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.twitterlite.domain.model.User
import com.example.twitterlite.domain.repository.LoginRepository
import com.example.twitterlite.utils.Constants
import com.example.twitterlite.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepositoryImplementation @Inject constructor(
    private val fireStoreDb: FirebaseFirestore,
    private val dataStore: DataStore<Preferences>
) : LoginRepository {
    override suspend fun isLoginSuccess(user: User): Resource<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val data = fireStoreDb.collection("users").get().await()
                val allUsers = data.documents.map {
                    User(it.get("email").toString(), it.get("password").toString())
                }
                val isLoggedIn = user in allUsers
                if (isLoggedIn) {
                    Resource.Success(true)
                } else {
                    Resource.Error("Wrong username or password", false)
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Something went wrong", false)
            }
        }

    override suspend fun isSignupSuccess(user: User): Resource<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val data = hashMapOf(
                    "email" to user.email,
                    "password" to user.password,
                )
                val loginData = fireStoreDb.collection("users").get().await()
                val allUserName = loginData.documents.map {
                    it.get("email").toString().trim()
                }
                val isExistingUser = user.email.trim() in allUserName
                if (isExistingUser) {
                    Resource.Error("User already exists! Please try login", false)
                } else {
                    fireStoreDb.collection("users")
                        .add(data)
                        .await()
                    Resource.Success(true)
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Something went wrong", false)
            }
        }

    override suspend fun isLoginNeeded(): Flow<String?> = flow {
        val emailKey = stringPreferencesKey(Constants.KEY_EMAIL)
        val flowEmail = dataStore.data.map { preferences ->
            preferences[emailKey]
        }
        emit(flowEmail.first())
    }

    override suspend fun saveLoginInfo(email: String): Boolean {
        return try {
            val emailKey = stringPreferencesKey(Constants.KEY_EMAIL)
            dataStore.edit { pref ->
                pref[emailKey] = email
            }
            true
        } catch (e: Exception) {
            false

        }

    }
}