package com.example.twitterlite.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.twitterlite.domain.model.Post
import com.example.twitterlite.domain.repository.HomeRepository
import com.example.twitterlite.utils.Constants
import com.example.twitterlite.utils.Constants.description
import com.example.twitterlite.utils.Constants.image
import com.example.twitterlite.utils.Constants.posts
import com.example.twitterlite.utils.Constants.username
import com.example.twitterlite.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepositoryImplementation @Inject constructor(
    private val fireStoreDb: FirebaseFirestore,
    private val dataStore: DataStore<Preferences>
) : HomeRepository {
    override suspend fun uploadPost(post: Post): Resource<Boolean> = withContext(Dispatchers.IO) {
        try {
            val emailKey = stringPreferencesKey(Constants.KEY_EMAIL)
            val flowEmail = dataStore.data.map { preferences ->
                preferences[emailKey]
            }
            val userName = flowEmail.first()
            val data = hashMapOf(
                description to post.text,
                image to post.image,
                username to userName
            )
            fireStoreDb.collection(posts)
                .add(data)
                .await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error("Couldn't upload your post", false)
        }
    }

    override suspend fun getAllPosts(): Resource<List<Post>> = withContext(Dispatchers.IO) {
        try {
            val allPosts = fireStoreDb.collection(posts).get().await()
            Resource.Success(allPosts.documents.map {
                Post(
                    it.get(description).toString(),
                    it.get(image).toString(),
                    it.get(username).toString()
                )
            })
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Something went wrong", null)

        }
    }

    override suspend fun logout(): Boolean {
        return try {
            val emailKey = stringPreferencesKey(Constants.KEY_EMAIL)
            dataStore.edit { pref ->
                pref[emailKey] = ""
            }
            true
        } catch (e: Exception) {
            false

        }

    }

}