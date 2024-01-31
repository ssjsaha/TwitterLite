package com.example.twitterlite.presentation

import app.cash.turbine.test
import com.example.twitterlite.data.repository.LoginRepositoryImplementation
import com.example.twitterlite.domain.model.User
import com.example.twitterlite.presentation.ui.LoginPageEvent
import com.example.twitterlite.presentation.viewmodel.LoginViewModel
import com.example.twitterlite.utils.Resource
import com.google.common.truth.Truth

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

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var repo: LoginRepositoryImplementation
    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repo = mock()
        viewModel = LoginViewModel(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test on event with login param`() {
        runTest {
            val spy = spy(viewModel)
            spy.onEvent(LoginPageEvent.AttemptLogin(User("", "")))

            verify(spy, times(1)).tryLogin(User("", ""))
        }

    }

    @Test
    fun `test on event with signup param`() {
        runTest {
            val spy = spy(viewModel)
            spy.onEvent(LoginPageEvent.AttemptSignup(User("", "")))
            verify(spy, times(1)).trySignup(User("", ""))
        }
    }

    @Test
    fun `test login success`() {
        runTest {
            val spy = spy(viewModel)
            whenever(repo.isLoginSuccess(User("", ""))).thenReturn(Resource.Success(true))
            spy.tryLogin(User("", ""))
            spy.loginStateFlow.test {
                val res = awaitItem()
                Truth.assertThat(res.isLoggedIn).isEqualTo(true)
                Truth.assertThat(res.isLoading).isEqualTo(false)
            }
        }

    }

    @Test
    fun `test login failure`() {
        runTest {
            val spy = spy(viewModel)
            whenever(repo.isLoginSuccess(User("", ""))).thenReturn(Resource.Error("",false))
            spy.tryLogin(User("", ""))
            spy.loginStateFlow.test {
                val res = awaitItem()
                Truth.assertThat(res.isLoggedIn).isEqualTo(false)
                Truth.assertThat(res.isLoading).isEqualTo(false)
            }
        }
    }
}