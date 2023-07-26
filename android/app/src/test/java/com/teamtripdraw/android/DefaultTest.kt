package com.teamtripdraw.android

import com.teamtripdraw.android.rule.InstantTaskExecutorRule
import com.teamtripdraw.android.rule.MainDispatcherRule
import org.junit.Before
import org.junit.Rule

abstract class DefaultTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    abstract fun setUp()
}
