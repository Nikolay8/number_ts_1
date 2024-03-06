package com.example.number.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.number.R
import com.example.number.data.repository.NetworkRepository
import com.example.number.databinding.FragmentNumberBinding
import com.example.number.util.factory
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class NumberFragment : Fragment() {

    private val viewModel: NumberViewModel by viewModels { factory() }

    private lateinit var binding: FragmentNumberBinding

    private var linearLayoutManager: LinearLayoutManager? = null
    private lateinit var selectAdapter: NumberItemAdapter

    @Inject
    lateinit var networkRepository: NetworkRepository

    companion object {
        const val TAG = "NumberFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_number, container, false)
        binding.viewmodel = viewModel
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()

        // Restore all numbers from DB
        viewModel.getAllNumbersFromDb()
    }

    private fun initViews() {
        // Search number listener
        binding.searchEditText.addTextChangedListener {
            val rawString = it.toString()
            try {
                val intValue = rawString.toInt()
                viewModel.number = intValue
            } catch (e: NumberFormatException) {
                // ignore
            }
        }

        // Search number on IME_ACTION_DONE start search
        binding.searchEditText.setOnEditorActionListener(OnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val rawString = v.text.toString()

                try {
                    val intValue = rawString.toInt()
                    viewModel.number = intValue
                    viewModel.getNumberFact()
                } catch (e: NumberFormatException) {
                    showError()
                }

                return@OnEditorActionListener true
            }
            false
        })

        // Config recyclerView
        linearLayoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = linearLayoutManager

        selectAdapter =
            NumberItemAdapter { selectedItem ->
                navigateToFragment(NumberDetailFragment.newInstance(selectedItem))
            }
        binding.recyclerView.adapter = selectAdapter
    }

    // On get number fact click
    fun getNumberFact(view: View) {
        viewModel.getNumberFact()
    }

    // On get random number fact click
    fun getRandomFact(view: View) {
        viewModel.getRandomFact()
    }


    private fun setupObservers() {
        // Observe getAccount response
        viewModel.getNumberListResult.observe(viewLifecycleOwner) { numbersList ->
            // Update numbers list
            selectAdapter.submitList(numbersList)
        }

        // error
        viewModel.showErrorEvent.observe(viewLifecycleOwner) {
            showError()
        }

        // progress
        viewModel.showProgressEvent.observe(viewLifecycleOwner) { showProgress ->
            val activity = requireActivity() as? MainActivity ?: return@observe

            if (showProgress) activity.progressDialogWithTimeout.show()
            else activity.progressDialogWithTimeout.dismissProgressDialog()
        }
    }

    private fun showError() {
        Snackbar.make(
            binding.root,
            getString(R.string.something_went_wrong),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun navigateToFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            this.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            this.replace(R.id.main_fragment_container, fragment)
            this.addToBackStack(fragment.tag)
            this.commit()
        }
    }
}
