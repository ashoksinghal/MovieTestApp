package com.example.movesearchtestapp.ui.main

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.movesearchtestapp.R
import com.example.movesearchtestapp.data.MovieItemDetail
import com.example.movesearchtestapp.databinding.DetailBottomsheetMoviesBinding
import com.example.movesearchtestapp.utils.toToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.list_item_movie.*

@AndroidEntryPoint
class MovieDetailBottomSheetFragment constructor() : BottomSheetDialogFragment() {

    private val viewModel: DetailViewModel by viewModels()
    private var _binding: DetailBottomsheetMoviesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null


    companion object {
        const val KEY_CURRENT_SELECTION = "KEY_CURRENT_SELECTION"
        fun newInstance(movieItem: MovieItemDetail): MovieDetailBottomSheetFragment {
            val bundle = Bundle()
            bundle.putParcelable(KEY_CURRENT_SELECTION, movieItem)
            val fragment = MovieDetailBottomSheetFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog ->
            val dialogc = dialog as BottomSheetDialog
            val bottomSheet = dialogc.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet?.let {
                bottomSheetBehavior = BottomSheetBehavior.from(it)
                bottomSheetBehavior?.peekHeight = 0
                bottomSheetBehavior?.addBottomSheetCallback(object :
                    BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {

                    }

                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                            dialog.dismiss()
                        }
                    }

                })
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
            }


        }
        return bottomSheetDialog
    }


    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        arguments?.containsKey(KEY_CURRENT_SELECTION)?.let {
            viewModel.initialiseMovieItemDetail(
                requireArguments().getParcelable<MovieItemDetail>(
                    KEY_CURRENT_SELECTION
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailBottomsheetMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.resultLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DetailViewModel.Result.Loader -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
                is DetailViewModel.Result.Success -> {
                    binding.progressbar.visibility = View.GONE
                    updateView(result.result)
                }
                is DetailViewModel.Result.Error -> {
                    binding.progressbar.visibility = View.GONE
                    requireContext()?.let {
                        result.errorMsg.toToast(it)
                    }

                }
            }
        }
    }

    private fun updateView(result: MovieItemDetail) {
        with(binding) {
            movieName.text = "Title: ${result.title}"
            plot.text = "Plot: ${result.plot}"
            movie_year.text = "Release Year: ${result.year}"
            director.text = "Director: ${result.director}"
            movieName.text = result.title
            actors.text = "Actors: ${result.actors}"
            rating.rating = (result.imdbRating?.toFloatOrNull()?.mod(5.0) ?: 0f).toFloat()
            Glide.with(posterImage.context).load(result.poster)
                .placeholder(R.drawable.ic_launcher_background)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(posterImage)
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        bottomSheetBehavior = null
    }
}