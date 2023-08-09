package com.teamtripdraw.android.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.teamtripdraw.android.TripDrawApplication
import com.teamtripdraw.android.ui.common.dialog.SetTripTitleDialogViewModel
import com.teamtripdraw.android.ui.history.HistoryViewModel
import com.teamtripdraw.android.ui.history.detail.HistoryDetailViewModel
import com.teamtripdraw.android.ui.history.tripDetail.TripDetailViewModel
import com.teamtripdraw.android.ui.home.HomeViewModel
import com.teamtripdraw.android.ui.home.markerSelectedBottomSheet.MarkerSelectedViewModel
import com.teamtripdraw.android.ui.post.detail.PostDetailViewModel
import com.teamtripdraw.android.ui.post.viewer.PostViewerViewModel
import com.teamtripdraw.android.ui.post.writing.PostWritingViewModel
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
                isAssignableFrom(PostWritingViewModel::class.java) ->
                    PostWritingViewModel(
                        repositoryContainer.pointRepository,
                        repositoryContainer.postRepository,
                        repositoryContainer.tripRepository,
                    )
                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(
                        repositoryContainer.tripRepository,
                        repositoryContainer.pointRepository,
                    )
                isAssignableFrom(PostDetailViewModel::class.java) ->
                    PostDetailViewModel(repositoryContainer.postRepository)
                isAssignableFrom(PostViewerViewModel::class.java) ->
                    PostViewerViewModel(
                        repositoryContainer.tripRepository,
                        repositoryContainer.postRepository,
                    )
                isAssignableFrom(MarkerSelectedViewModel::class.java) ->
                    MarkerSelectedViewModel(
                        repositoryContainer.pointRepository,
                    )
                isAssignableFrom(HistoryViewModel::class.java) ->
                    HistoryViewModel(repositoryContainer.tripRepository)
                isAssignableFrom(SetTripTitleDialogViewModel::class.java) ->
                    SetTripTitleDialogViewModel(repositoryContainer.tripRepository)
                isAssignableFrom(HistoryDetailViewModel::class.java) ->
                    HistoryDetailViewModel(repositoryContainer.postRepository)
                isAssignableFrom(TripDetailViewModel::class.java) ->
                    TripDetailViewModel(
                        repositoryContainer.tripRepository,
                        repositoryContainer.pointRepository,
                    )
                else ->
                    throw IllegalArgumentException(UNDEFINED_VIEW_MODEL_ERROR.format(modelClass.name))
            }
        } as T
}
