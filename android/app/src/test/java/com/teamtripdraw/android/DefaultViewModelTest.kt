package com.teamtripdraw.android

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.teamtripdraw.android.rule.MainCoroutineRule
import org.junit.Rule

/**
 * LiveData와 Coroutine을 테스트 할 수 있도록하는 기능을 제공합니다.
 */

abstract class DefaultViewModelTest {

    // LiveData Test
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Coroutine Test
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()
}
