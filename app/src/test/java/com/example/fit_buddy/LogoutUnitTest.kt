package com.example.fit_buddy

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.example.fit_buddy.repository.UserRepo
import com.example.fit_buddy.viewmodel.UserViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@RunWith(org.robolectric.RobolectricTestRunner::class)
class LogoutUnitTest {

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
    fun logout_calls_repo_logout() {
        // Call the logout function
        viewModel.logout()

        // Verify that repo.logout() was called exactly once
        verify(repo).logout()
    }
}
