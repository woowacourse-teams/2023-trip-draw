package com.teamtripdraw.android

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class DefaultViewModelTest {

    // LiveData Test
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    abstract fun setUp()

    @After
    abstract fun tearDown()
}
