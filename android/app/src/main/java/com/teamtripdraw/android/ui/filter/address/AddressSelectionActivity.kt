package com.teamtripdraw.android.ui.filter.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityAddressSelectionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddressSelectionBinding
    private val viewModel: AddressSelectionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address_selection)
        binding.lifecycleOwner = this
        binding.addressSelectionViewModel = viewModel

        initSiDoListView()
        initSiGunGuListView()
        initEupMyeonDongListView()
        initSelectedCompleteView()
    }

    private fun initSiDoListView() {
        val siDoAdapter = AddressSelectionAdapter { viewModel.selectSiDo(it) }
        binding.rvAddressSelectionSiDo.adapter = siDoAdapter

        viewModel.siDos.observe(this) { siDos ->
            siDoAdapter.submitList(siDos)
        }
    }

    private fun initSiGunGuListView() {
        val siGunGuAdapter = AddressSelectionAdapter { viewModel.selectSiGunGu(it) }
        binding.rvAddressSelectionSiGunGu.adapter = siGunGuAdapter

        viewModel.siGunGus.observe(this) { siGunGus ->
            siGunGuAdapter.submitList(siGunGus)
        }
    }

    private fun initEupMyeonDongListView() {
        val eupMyeonDongAdapter = AddressSelectionAdapter { viewModel.selectEupMyeonDong(it) }
        binding.rvAddressSelectionEupMyeonDong.adapter = eupMyeonDongAdapter

        viewModel.eupMyeonDongs.observe(this) { eupMyeonDongs ->
            eupMyeonDongAdapter.submitList(eupMyeonDongs)
        }
    }

    private fun initSelectedCompleteView() {
        viewModel.selectingCompletedEvent.observe(this) {
            if (it.content) returnBeforeActivity()
        }
    }

    private fun returnBeforeActivity() {
        val resultIntent = Intent()
        resultIntent.putExtra(INTENT_KEY_ADDRESS, viewModel.address)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    companion object {
        const val INTENT_KEY_ADDRESS = "INTENT_KEY_ADDRESS"
    }
}
