package com.example.movesearchtestapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movesearchtestapp.adapters.MoviesAdapter
import com.example.movesearchtestapp.data.MovieItem
import com.example.movesearchtestapp.databinding.FragmentMoviesBinding
import com.example.movesearchtestapp.utils.showSnackBarOnError
import com.example.movesearchtestapp.utils.toToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteFragment : Fragment(), MoviesAdapter.ItemInteraction {

    private val viewModel: FavouriteViewModel by viewModels()
    private var _binding: FragmentMoviesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        val root = binding.root
        val adapter = MoviesAdapter(itemInteraction = this, hideFavourites = true)
        binding.moviesResultRecycler.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        binding.moviesResultRecycler.adapter = adapter
        binding.searchView.visibility = View.GONE
        subscribeUi(adapter)
        return root
    }


    private fun subscribeUi(adapter: MoviesAdapter) {
        viewModel.resultLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is FavouriteViewModel.Result.Loader -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
                is FavouriteViewModel.Result.Success -> {
                    binding.progressbar.visibility = View.GONE
                    adapter.submitList(result.result)
                }
                is FavouriteViewModel.Result.Error -> {
                    binding.progressbar.visibility = View.GONE
                    requireContext()?.let {
                        result.errorMsg.toToast(it)
                    }

                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): FavouriteFragment {
            return FavouriteFragment().apply {
                arguments = Bundle.EMPTY
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun navigateToMovieDetail(movie: MovieItem) {
        val fragment = MovieDetailBottomSheetFragment
            .newInstance(
                movieItem = movie.getMovieDetailItem()
            )
        childFragmentManager?.let {
            fragment.show(it, "detail_view")
        }

    }
}