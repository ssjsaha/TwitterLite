package com.example.twitterlite.data.repository

import com.example.twitterlite.domain.model.User
import com.example.twitterlite.domain.repository.LoginRepository
import com.example.twitterlite.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepositoryImplementation @Inject constructor(
    private val fireStoreDb: FirebaseFirestore
) : LoginRepository {
    override suspend fun isLoginSuccess(user: User): Resource<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val data = fireStoreDb.collection("users").get().await()
                val allUsers = data.documents.map {
                    User(it.get("email").toString(), it.get("password").toString())
                }

                Resource.Success(user in allUsers)
            } catch (e: Exception) {
                Resource.Success(false)
            }
        }
}