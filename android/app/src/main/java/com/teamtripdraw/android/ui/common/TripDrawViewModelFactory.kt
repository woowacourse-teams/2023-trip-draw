package com.teamtripdraw.android.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.teamtripdraw.android.TripDrawApplication
import com.teamtripdraw.android.ui.home.HomeViewModel
import com.teamtripdraw.android.ui.signUp.NicknameSetupViewModel

private const val UNDEFINED_VIEW_MODEL_ERROR = "ViewModelFactory에 정의되지않은 뷰모델을 생성하였습니다 : %s"

@Suppress("UNCHECKED_CAST")
val tripDrawViewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            val dependencyContainer = TripDrawApplication.DependencyContainer
            val repositoryContainer = dependencyContainer.repositoryContainer
            when {
                isAssignableFrom(NicknameSetupViewModel::class.java) ->
                    NicknameSetupViewModel(repositoryContainer.nicknameSetupRepository)
                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel()
                else ->
                    throw IllegalArgumentException(UNDEFINED_VIEW_MODEL_ERROR.format(modelClass.name))
            }
        } as T
}
