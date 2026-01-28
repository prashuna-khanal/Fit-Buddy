package com.example.fit_buddy

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.example.fit_buddy.repository.UserRepo
import com.example.fit_buddy.viewmodel.UserViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.doAnswer
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LoginUnitTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repo: UserRepo
    private lateinit var app: Application
    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        repo = mock()
        app = ApplicationProvider.getApplicationContext()
        viewModel = UserViewModel(app, repo)
    }

    @Test
    fun login_success_test() {
        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(2)
            callback(true, "Login success")
            null
        }.`when`(repo).login(eq("test@gmail.com"), eq("123456"), any())

        var success = false
        var message = ""
        viewModel.login("test@gmail.com", "123456") { s, m ->
            success = s
            message = m
        }

        assertTrue(success)
        assertEquals("Login success", message)
    }
}
