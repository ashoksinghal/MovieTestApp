package com.example.movesearchtestapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movesearchtestapp.adapters.MoviesAdapter
import com.example.movesearchtestapp.data.MovieItem
import com.example.movesearchtestapp.databinding.FragmentMoviesBinding
import com.example.movesearchtestapp.utils.toToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment(), MoviesAdapter.FavouriteInteraction,
    MoviesAdapter.ItemInteraction {

    private val viewModel: MoviesListViewModel by viewModels()
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
        val adapter = MoviesAdapter(this, this)
        binding.moviesResultRecycler.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        binding.moviesResultRecycler.adapter = adapter
        setupSearch(binding.searchView)
        subscribeUi(adapter)
        return root
    }

    private fun setupSearch(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty())
                    viewModel.searchMovies(query)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query.isNullOrEmpty())
                    viewModel.clearSearch()
                return true
            }
        })
    }

    private fun subscribeUi(adapter: MoviesAdapter) {
        viewModel.resultLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is MoviesListViewModel.Result.Loader -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
                is MoviesListViewModel.Result.Success -> {
                    binding.progressbar.visibility = View.GONE
                    adapter.submitList(result.result)
                }
                is MoviesListViewModel.Result.Error -> {
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
        fun newInstance(): MoviesFragment {
            return MoviesFragment().apply {
                arguments = Bundle.EMPTY
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFavButtonClicked(movie: MovieItem, position: Int, onActionDone: () -> Unit) {
        viewModel.addRemoveFavourite(movie)
        {
            onActionDone.invoke()
        }
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