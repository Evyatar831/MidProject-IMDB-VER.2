package com.example.midproject_imdb.ui.add_movie

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.midproject_imdb.ui.all_movies.MoviesViewModel
import com.example.midproject_imdb.R
import com.example.midproject_imdb.databinding.AddItemLayoutBinding
import com.example.midproject_imdb.data.models.Movie

class AddMovieFragment : Fragment() {
    private var _binding : AddItemLayoutBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null
    private var isEditMode = false
    private var EditMovie=0

    val viewModel : MoviesViewModel by activityViewModels()

    val pickImageLauncher : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            binding.resultImage.setImageURI(it)
            if (it != null)
                requireActivity().contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                imageUri = it
        }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddItemLayoutBinding.inflate(inflater, container, false)
        viewModel.chosenItem.observe(viewLifecycleOwner) { movie ->
            if (movie != null) {
                isEditMode = true
                EditMovie=movie.id
                binding.itemTitle.setText(movie.title)
                binding.itemDescription.setText(movie.description)
                imageUri = Uri.parse(movie.photo)
                binding.resultImage.setImageURI(imageUri)
                binding.userCommentsLayout?.visibility = View.VISIBLE
                binding.userComments?.setText(movie.userComments)


            } else {
                isEditMode = false

                binding.itemTitle.text = null
                binding.itemDescription.text = null
                binding.resultImage.setImageDrawable(null)
            }

        }
        binding.finishBtn.setOnClickListener {



            if (isEditMode) {

                val item = Movie(
                    binding.itemTitle.text.toString(),
                    binding.itemDescription.text.toString(), imageUri.toString(),binding.userComments?.text.toString()
                )
                item.id=EditMovie
                    viewModel.updateMovie(item)

                }
                else {
                isEditMode=false
                val item = Movie(
                    binding.itemTitle.text.toString(),
                    binding.itemDescription.text.toString(), imageUri.toString()
                )
                viewModel.addMovie(item)
                }




            findNavController().navigate(
                R.id.action_addItemFragment_to_allItemsFragment
            )
        }
        binding.imageBtn.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}