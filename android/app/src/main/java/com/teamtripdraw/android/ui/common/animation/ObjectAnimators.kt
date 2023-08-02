package com.teamtripdraw.android.ui.common.animation

import android.animation.ObjectAnimator
import android.view.View
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.teamtripdraw.android.R

object ObjectAnimators {
    fun toggleFab(
        buttonMain: FloatingActionButton,
        buttonWritePost: FloatingActionButton,
        buttonPostList: FloatingActionButton,
        buttonMarkerMode: FloatingActionButton,
        textViewWritePost: TextView,
        textViewPostList: TextView,
        textViewMarkerMode: TextView,
        fabState: Boolean
    ) {
        // 플로팅 액션 버튼 닫기 - 열려있는 플로팅 버튼 집어넣는 애니메이션 세팅
        if (fabState) {
            ObjectAnimator.ofFloat(buttonMain, View.ROTATION, 45f, 0f).apply {
                duration = 300
                start()
            }
            ObjectAnimator.ofFloat(buttonMarkerMode, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(textViewMarkerMode, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(buttonPostList, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(textViewPostList, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(buttonWritePost, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(textViewWritePost, "translationY", 0f).apply { start() }
            textViewMarkerMode.visibility = View.GONE
            textViewPostList.visibility = View.GONE
            textViewWritePost.visibility = View.GONE
            buttonMain.setImageResource(R.drawable.ic_fab_plus)

            // 플로팅 액션 버튼 열기 - 닫혀있는 플로팅 버튼 꺼내는 애니메이션 세팅
        } else {
            ObjectAnimator.ofFloat(buttonMain, View.ROTATION, -45f, 0f).apply {
                duration = 300
                start()
            }
            textViewMarkerMode.visibility = View.VISIBLE
            textViewPostList.visibility = View.VISIBLE
            textViewWritePost.visibility = View.VISIBLE
            ObjectAnimator.ofFloat(buttonMarkerMode, "translationY", -200f).apply { start() }
            ObjectAnimator.ofFloat(textViewMarkerMode, "translationY", -200f).apply { start() }
            ObjectAnimator.ofFloat(textViewPostList, "translationY", -360f).apply { start() }
            ObjectAnimator.ofFloat(buttonPostList, "translationY", -360f).apply { start() }
            ObjectAnimator.ofFloat(buttonWritePost, "translationY", -520f).apply { start() }
            ObjectAnimator.ofFloat(textViewWritePost, "translationY", -520f).apply { start() }
            buttonMain.setImageResource(R.drawable.ic_fab_plus_rotate_45)
        }
    }
}
