package com.teamtripdraw.android.ui.allPosts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamtripdraw.android.databinding.FragmentAllPostsBinding
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.support.framework.presentation.getParcelableExtraCompat
import com.teamtripdraw.android.ui.filter.FilterSelectionActivity
import com.teamtripdraw.android.ui.filter.FilterSelectionActivity.Companion.SELECTED_OPTIONS_INTENT_KEY
import com.teamtripdraw.android.ui.filter.FilterType
import com.teamtripdraw.android.ui.filter.SelectedOptions
import com.teamtripdraw.android.ui.post.detail.PostDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllPostsFragment : Fragment() {

    private var _binding: FragmentAllPostsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AllPostsAdapter
    private val viewModel: AllPostsViewModel by viewModels()

    private val getFilterOptionsResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
            val intent: Intent = result.data!!
            val selectedOptions =
                intent.getParcelableExtraCompat<SelectedOptions>(SELECTED_OPTIONS_INTENT_KEY)
                    ?: throw java.lang.IllegalStateException()
            viewModel.updateSelectedOptions(selectedOptions)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAllPostsBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        bindViewModel()
        initObserver()
        setAdapter()
        addScrollListener()

        return binding.root
    }

    private fun bindViewModel() {
        binding.allPostsViewModel = viewModel
    }

    private fun initObserver() {
        initPostsObserve()
        initOpenPostDetailEventObserve()
        initOpenFilterSelectionEventObserve()
    }

    private fun initPostsObserve() {
        viewModel.posts.observe(viewLifecycleOwner) {
            adapter.submitList(it.postItems)
        }
    }

    private fun initOpenPostDetailEventObserve() {
        viewModel.openPostDetailEvent.observe(
            viewLifecycleOwner,
            EventObserver(this@AllPostsFragment::onPostClick),
        )
    }

    private fun onPostClick(postId: Long) {
        startActivity(PostDetailActivity.getIntent(requireContext(), postId))
    }

    private fun initOpenFilterSelectionEventObserve() {
        viewModel.openFilterSelectionEvent.observe(
            viewLifecycleOwner,
            this::onFilterSelectionClick,
        )
    }

    private fun onFilterSelectionClick(isClicked: Boolean) {
        if (isClicked) {
            val intent =
                FilterSelectionActivity.getIntent(
                    requireContext(),
                    FilterType.POST,
                    viewModel.selectedOptions,
                )
            getFilterOptionsResult.launch(intent)
        } else {
            binding.rvAllPosts.scrollToPosition(INITIAL_POSITION)
        }
    }

    private fun setAdapter() {
        adapter = AllPostsAdapter(viewModel::openPostDetail)
        binding.rvAllPosts.adapter = adapter
    }

    private fun addScrollListener() {
        binding.rvAllPosts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = binding.rvAllPosts.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                checkFetchPostsCondition(layoutManager, lastPosition)
            }
        })
    }

    private fun checkFetchPostsCondition(layoutManager: LinearLayoutManager, lastPosition: Int) {
        if (viewModel.hasNextPage &&
            viewModel.isAddLoading.not() &&
            checkLoadThreshold(layoutManager, lastPosition) &&
            binding.rvAllPosts.canScrollVertically(DOWNWARD_DIRECTION).not()
        ) {
            viewModel.fetchPosts()
        }
    }

    private fun checkLoadThreshold(layoutManager: LinearLayoutManager, lastPosition: Int) =
        layoutManager.itemCount <= lastPosition + LOAD_THRESHOLD

    override fun onResume() {
        super.onResume()
        fetchPosts()
    }

    private fun fetchPosts() = viewModel.fetchPosts()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val LOAD_THRESHOLD = 3
        private const val DOWNWARD_DIRECTION = 1
        private const val INITIAL_POSITION = 0
    }
}
