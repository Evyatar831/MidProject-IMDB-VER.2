package com.example.midproject_imdb.ui.all_movies

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.midproject_imdb.data.models.Movie
import com.example.midproject_imdb.data.repositories.MovieRepository
import kotlinx.coroutines.launch

class MoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository(application)
    private val _chosenItem = MutableLiveData<Movie?>()
    val chosenItem: LiveData<Movie?> = _chosenItem

       val movies: LiveData<List<Movie>>? = repository.getMovies()


    fun addMovie(movie: Movie) {
        viewModelScope.launch{
        repository.addMovie(movie)}
    }

     fun deleteMovie(movie: Movie){
        viewModelScope.launch { repository.deleteMovie(movie)}
    }

    fun deleteAll() {
        viewModelScope.launch {
        repository.deleteAll()}
    }
    fun updateMovie(movie: Movie) {
        viewModelScope.launch {
        repository.updateMovie(movie)}
    }

    fun getMovie(id:Int) = repository.getMovie(id)

    fun setMovie(movie: Movie?){
        viewModelScope.launch {
        _chosenItem.value=movie}
    }

    val allMovies: LiveData<List<Movie>>? = repository.allMovies




    fun preloadMovies() {

        allMovies?.observeForever { movies ->
            if (movies.isEmpty()) {
                viewModelScope.launch {
                    repository.insertMovies(
                        listOf(
                            Movie( "Batman", "The dark knight", "android.resource://com.example.midproject_imdb/drawable/batman"),
                            Movie( "Superman", "the man of steel", "android.resource://com.example.midproject_imdb/drawable/superman"),
                            Movie( "One Piece", "the king of the pirates", "android.resource://com.example.midproject_imdb/drawable/onepiece")
                        )
                    )
                }
            }
        }
    }


    // delete dialog handle
    private val _showDeleteDialog = MutableLiveData<Pair<Movie?, Int>?>()
    val showDeleteDialog: LiveData<Pair<Movie?, Int>?> = _showDeleteDialog
    fun setDeleteDialog(movie: Movie?, position: Int) {
        _showDeleteDialog.value = Pair(movie, position)
    }
    fun clearDeleteDialog() {
        _showDeleteDialog.value = null
    }


}