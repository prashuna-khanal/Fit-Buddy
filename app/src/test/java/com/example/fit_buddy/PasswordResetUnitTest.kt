package com.example.fit_buddy

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.example.fit_buddy.repository.UserRepo
import com.example.fit_buddy.viewmodel.UserViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.verify
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.robolectric.RobolectricTestRunner
import org.junit.runner.RunWith

@RunWith(RobolectricTestRunner::class)
class PasswordResetUnitTest {

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
    fun sendPasswordReset_success_test() {
        // Mock the repository callback
        doAnswer { invocation ->
            // Get the callback parameter safely
            val callback = invocation.arguments[1] as (Boolean, String) -> Unit
            callback(true, "Reset email sent") // simulate success
            null
        }.`when`(repo).sendPasswordResetEmail(eq("test@gmail.com"), any())

        // Capture results
        var success = false
        var message = ""

        // Call the ViewModel method
        viewModel.sendPasswordReset("test@gmail.com") { s, m ->
            success = s
            message = m
        }

        // Assertions
        assertTrue(success)
        assertEquals("Reset email sent", message)

        // Verify repository method was called
        verify(repo).sendPasswordResetEmail(eq("test@gmail.com"), any())
    }
}
