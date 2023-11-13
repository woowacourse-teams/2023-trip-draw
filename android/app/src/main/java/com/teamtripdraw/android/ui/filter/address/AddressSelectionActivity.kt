package com.teamtripdraw.android.ui.filter.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityAddressSelectionBinding
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddressSelectionBinding
    private val viewModel: AddressSelectionViewModel by viewModels()

    private val siDosAdapter: AddressSelectionAdapter by lazy {
        AddressSelectionAdapter(selectEvent = { viewModel.selectSiDo(it) })
    }
    private val siGunGusAdapter: AddressSelectionAdapter by lazy {
        AddressSelectionAdapter(selectEvent = { viewModel.selectSiGunGu(it) })
    }
    private val eupMyeonDongsAdapter: AddressSelectionAdapter by lazy {
        AddressSelectionAdapter(selectEvent = { viewModel.selectEupMyeonDong(it) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address_selection)
        binding.lifecycleOwner = this
        binding.addressSelectionViewModel = viewModel

        initView()
        initObserve()
    }

    private fun initView() {
        initSiDosView()
        initSiGunGusView()
        initEupMyeonDongsView()
    }

    private fun initObserve() {
        initSiDosObserve()
        initSiGunGusObserve()
        initEupMyeonDongsObserve()
        initSelectedCompleteObserve()
    }

    private fun initSiDosView() {
        binding.rvAddressSelectionSiDo.adapter = siDosAdapter
        binding.rvAddressSelectionSiDo.itemAnimator = null
    }

    private fun initSiGunGusView() {
        binding.rvAddressSelectionSiGunGu.adapter = siGunGusAdapter
        binding.rvAddressSelectionSiGunGu.itemAnimator = null
    }

    private fun initEupMyeonDongsView() {
        binding.rvAddressSelectionEupMyeonDong.adapter = eupMyeonDongsAdapter
        binding.rvAddressSelectionEupMyeonDong.itemAnimator = null
    }

    private fun initSiDosObserve() {
        viewModel.siDos.observe(this) { siDos ->
            siDosAdapter.submitList(siDos.toList())
        }
    }

    private fun initSiGunGusObserve() {
        viewModel.siGunGus.observe(this) { siGunGus ->
            siGunGusAdapter.submitList(siGunGus)
        }
    }

    private fun initEupMyeonDongsObserve() {
        viewModel.eupMyeonDongs.observe(this) { eupMyeonDongs ->
            eupMyeonDongsAdapter.submitList(eupMyeonDongs)
        }
    }

    private fun initSelectedCompleteObserve() {
        viewModel.selectingCompletedEvent.observe(
            this,
            EventObserver {
                if (it) returnSelectedAddressToBeforeActivity()
            },
        )
    }

    private fun returnSelectedAddressToBeforeActivity() {
        val resultIntent = Intent()
        resultIntent.putExtra(INTENT_KEY_ADDRESS, viewModel.address)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    companion object {
        const val INTENT_KEY_ADDRESS = "INTENT_KEY_ADDRESS"
    }
}
