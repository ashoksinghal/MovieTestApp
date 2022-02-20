package com.example.movesearchtestapp.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.movesearchtestapp.R
import com.example.movesearchtestapp.data.MovieItem
import com.example.movesearchtestapp.databinding.ListItemMovieBinding
import com.example.movesearchtestapp.ui.main.MovieDetailBottomSheetFragment
import dagger.hilt.android.qualifiers.ActivityContext

class MoviesAdapter(
    private val favouriteInteraction: FavouriteInteraction? = null,
    private val itemInteraction: ItemInteraction,
    private val hideFavourites: Boolean? = false
) :
    ListAdapter<MovieItem, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieViewHolder(
            ListItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            favouriteInteraction,
            hideFavourites
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = getItem(position)
        (holder as MovieViewHolder).bind(movie)
    }

    inner class MovieViewHolder(
        private val binding: ListItemMovieBinding,
        favouriteInteraction: FavouriteInteraction?,
        hideFavourites: Boolean? = false
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                (it.tag as? MovieItem)?.let { movie ->
                    itemInteraction?.navigateToMovieDetail(movie)
                }
            }
            binding.favouriteCheckbox.setOnClickListener { view ->
                (view.tag as? MovieItem)?.let { movie ->
                    movie.isFavourite = binding.favouriteCheckbox.isChecked
                    favouriteInteraction?.onFavButtonClicked(movie, adapterPosition) {
                        notifyItemChanged(adapterPosition)
                    }
                }
            }
            if (hideFavourites == true)
                binding.favouriteCheckbox.visibility = View.GONE
        }

        fun bind(item: MovieItem) {
            binding.apply {
                movieName.text = item.title
                movieYear.text = item.year
                favouriteCheckbox.isChecked = item.isFavourite ?: false
                Glide.with(posterImage.context).load(item.posterUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(posterImage)
                root.tag = item
                favouriteCheckbox.tag = item
            }
        }
    }

    interface FavouriteInteraction {
        fun onFavButtonClicked(
            movie: MovieItem,
            position: Int,
            onActionDone: () -> Unit
        )
    }

    interface ItemInteraction {
        fun navigateToMovieDetail(
            movie: MovieItem
        )
    }
}

private class MovieDiffCallback : DiffUtil.ItemCallback<MovieItem>() {

    override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
        return oldItem.imdbID == newItem.imdbID
    }

    override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
        return oldItem == newItem
    }
}
