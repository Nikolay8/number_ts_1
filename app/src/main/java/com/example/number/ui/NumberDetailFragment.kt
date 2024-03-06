package com.example.number.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.number.R
import com.example.number.dao.model.NumberModel
import com.example.number.databinding.FragmentDetailBinding
import com.example.number.util.factory


class NumberDetailFragment : Fragment() {

    private val viewModel: NumberDetailViewModel by viewModels { factory() }

    private lateinit var binding: FragmentDetailBinding


    companion object {
        const val TAG = "NumberDetailsFragment"
        private const val NUMBER_MODEL = "number_model"

        fun newInstance(
            numberModel: NumberModel
        ): NumberDetailFragment {
            return NumberDetailFragment().apply {
                arguments = bundleOf(
                    NUMBER_MODEL to numberModel
                )
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(NUMBER_MODEL, viewModel.numberModelData.value)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.numberModelData.value = savedInstanceState?.getParcelable(NUMBER_MODEL)
            ?: arguments?.getParcelable(NUMBER_MODEL)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.viewmodel = viewModel
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        val titleString = String.format(
            getString(R.string.number_detail_title),
            viewModel.numberModelData.value?.id.toString()
        )

        binding.toolbar.title = titleString
    }
}
