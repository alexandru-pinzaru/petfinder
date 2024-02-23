package com.example.petfinder.nearby.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.petfinder.databinding.FragmentNearbyBinding
import com.example.petfinder.nearby.presentation.events.ErrorEvent
import com.example.petfinder.nearby.presentation.events.NearbyEvent
import com.example.petfinder.nearby.presentation.list.AnimalsAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NearbyFragment : Fragment() {

    private var _binding: FragmentNearbyBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NearbyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNearbyBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        viewModel.onEvent(NearbyEvent.RequestAnimalsList)
    }

    private fun setupList() {
        val animalsAdapter = AnimalsAdapter(requireContext())
        binding.rvAnimals.setHasFixedSize(true)
        binding.rvAnimals.adapter = animalsAdapter
        binding.rvAnimals.layoutManager = GridLayoutManager(requireContext(), 2)
        viewModel.state.observe(viewLifecycleOwner) {
            updateScreenState(it, animalsAdapter)
        }
    }

    private fun updateScreenState(
        state: NearbyViewState,
        adapter: AnimalsAdapter
    ) {
        binding.progressBar.isVisible = state.loading
        adapter.submitList(state.animals)
        handleErrors(state.error)
    }

    private fun handleErrors(error: ErrorEvent<Throwable>?) {
        val msg = error?.getIfNotHandled() ?: return
        if (!msg.message.isNullOrEmpty()) {
            Snackbar.make(requireView(), msg.message!!, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}