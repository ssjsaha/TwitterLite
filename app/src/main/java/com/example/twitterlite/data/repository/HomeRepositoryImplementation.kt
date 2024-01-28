package com.example.twitterlite.data.repository

import com.example.twitterlite.domain.model.Post
import com.example.twitterlite.domain.model.User
import com.example.twitterlite.domain.repository.HomeRepository
import com.example.twitterlite.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepositoryImplementation @Inject constructor(
    private val fireStoreDb: FirebaseFirestore
) : HomeRepository {
    override suspend fun uploadPost(post: Post): Resource<Boolean> = withContext(Dispatchers.IO) {
        val data = hashMapOf(
            "description" to post.text,
            "image" to post.image,
            "userName" to post.userName
        )
        try {
            fireStoreDb.collection("Posts")
                .add(data)
                .await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error("Couldn't upload your post", false)
        }
    }

    override suspend fun getAllPosts(): Resource<List<Post>> = withContext(Dispatchers.IO) {
        try {
            val allPosts = fireStoreDb.collection("Posts").get().await()
            Resource.Success(allPosts.documents.map {
                Post(
                    it.get("description").toString(),
                    it.get("image").toString(),
                    it.get("userName").toString()
                )
            })
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Something went wrong", null)

        }
    }

}