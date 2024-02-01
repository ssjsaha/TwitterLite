package com.example.twitterlite.presentation

import app.cash.turbine.test
import com.example.twitterlite.data.repository.HomeRepositoryImplementation
import com.example.twitterlite.domain.model.Post
import com.example.twitterlite.domain.model.User
import com.example.twitterlite.presentation.ui.HomePageEvent
import com.example.twitterlite.presentation.ui.model.PostUI
import com.example.twitterlite.presentation.viewmodel.HomeViewModel
import com.example.twitterlite.utils.Resource
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private lateinit var repo: HomeRepositoryImplementation
    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repo = mock()
        viewModel = HomeViewModel(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `on event with upload post with non null file test`() {
        runTest {
            val spy = spy(viewModel)
            doNothing().whenever(spy).uploadFile(File(""), "", "")
            spy.onEvent(HomePageEvent.UploadPost(File(""), "", ""))
            verify(spy, times(1)).uploadFile(File(""), "", "")
        }
    }

    @Test
    fun `on event with upload post with  null file test`() {
        runTest {
            val spy = spy(viewModel)
            spy.onEvent(HomePageEvent.UploadPost(null, "", ""))
            verify(spy, times(0)).uploadFile(File(""), "", "")
        }
    }

    @Test
    fun `map post ui test with valid post data`() {
        val list: ArrayList<Post> = ArrayList()
        for (i in 0..9) {
            list.add(Post("", null, ""))
        }
        runTest {
            val spy = spy(viewModel)
            val postUiList = spy.mapPostUI(list)
            assertEquals(list.size, postUiList.size)
        }
    }

    @Test
    fun `test get all post with success response`() {
        runTest {
            val spy = spy(viewModel)
            whenever(repo.getAllPosts()).thenReturn(
                Resource.Success(
                    listOf(
                        Post(
                            "", "",
                            ""
                        )
                    )
                )
            )
            spy.getAllPosts()
            spy.homeStateFlow.test {
                val res = awaitItem().posts
                Truth.assertThat(res).isEqualTo(listOf(PostUI("", null, "")))
            }
            verify(spy, times(1)).mapPostUI(listOf(Post("", "", "")))

        }
    }

    @Test
    fun `test get all post with failure response`() {
        runTest {
            val spy = spy(viewModel)
            whenever(repo.getAllPosts()).thenReturn(
                Resource.Error(message = "", null)
            )
            spy.getAllPosts()
            spy.homeStateFlow.test {
                val res = awaitItem()
                val data = res.posts
                val error = res.error
                Truth.assertThat(data)
                    .isEqualTo(listOf<PostUI>()) // because homeState.postUi default value is empty list
                Truth.assertThat(error).isNotNull()
            }
            verify(spy, times(0)).mapPostUI(listOf(Post("", "", "")))

        }
    }

}