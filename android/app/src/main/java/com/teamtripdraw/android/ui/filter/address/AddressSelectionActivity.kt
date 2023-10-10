package com.teamtripdraw.android.ui.filter.address

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.allViews
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
        val adapter = ArrayAdapter(this, R.layout.item_filter_option_address, ArrayList<String>())
        setListView(binding.lvAddressSelectionSiDo, adapter)

        binding.lvAddressSelectionSiDo.setOnItemClickListener { parent, view, position, id ->
            setListViewSelected(binding.lvAddressSelectionSiDo, view)
            viewModel.selectedSiDo.value = adapter.getItem(position).toString()
            viewModel.fetchSiGunGu()

            resetListViewSelected(binding.lvAddressSelectionSiGunGu)
            resetListViewSelected(binding.lvAddressSelectionEupMyeonDong)
        }

        viewModel.siDos.observe(this) { siDos ->
            changeAdapterList(adapter, siDos)
        }
    }

    private fun initSiGunGuListView() {
        val adapter = ArrayAdapter(this, R.layout.item_filter_option_address, ArrayList<String>())
        setListView(binding.lvAddressSelectionSiGunGu, adapter)

        binding.lvAddressSelectionSiGunGu.setOnItemClickListener { parent, view, position, id ->
            setListViewSelected(binding.lvAddressSelectionSiGunGu, view)
            viewModel.selectedSiGunGu.value = adapter.getItem(position).toString()
            viewModel.fetchEupMyeonDong()

            resetListViewSelected(binding.lvAddressSelectionEupMyeonDong)
        }

        viewModel.siGunGus.observe(this) { siGunGus ->
            changeAdapterList(adapter, siGunGus)
        }
    }

    private fun initEupMyeonDongListView() {
        val adapter = ArrayAdapter(this, R.layout.item_filter_option_address, ArrayList<String>())
        setListView(binding.lvAddressSelectionEupMyeonDong, adapter)

        binding.lvAddressSelectionEupMyeonDong.setOnItemClickListener { parent, view, position, id ->
            setListViewSelected(binding.lvAddressSelectionEupMyeonDong, view)
            viewModel.selectedEupMyeonDong.value = adapter.getItem(position).toString()
        }

        viewModel.eupMyeonDongs.observe(this) { eupMyeonDongs ->
            changeAdapterList(adapter, eupMyeonDongs)
        }
    }

    private fun setListViewSelected(lv: ListView, selectedView: View) {
        selectedView.isSelected = true
        resetListViewSelected(lv)
        selectedView.background =
            ContextCompat.getDrawable(this, R.drawable.shape_td_light_blue_fill_0_rect)
    }

    private fun resetListViewSelected(lv: ListView) {
        lv.allViews.forEach {
            it.background = ColorDrawable(Color.WHITE)
        }
    }

    private fun setListView(lv: ListView, adapter: ArrayAdapter<String>) {
        lv.adapter = adapter
        lv.choiceMode = ListView.CHOICE_MODE_SINGLE
    }

    private fun changeAdapterList(adapter: ArrayAdapter<String>, list: List<String>) {
        adapter.clear()
        adapter.addAll(list)
        adapter.notifyDataSetChanged()
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
