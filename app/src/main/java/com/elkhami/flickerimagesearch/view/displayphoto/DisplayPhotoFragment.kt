package com.elkhami.flickerimagesearch.view.displayphoto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.elkhami.flickerimagesearch.data.local.SavedPhoto
import com.elkhami.flickerimagesearch.databinding.FragmentDisplayPhotoBinding
import com.elkhami.flickerimagesearch.view.MainActivity
import com.elkhami.flickerimagesearch.view.displayphoto.viewmodel.DisplayPhotoViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by A.Elkhami on 06,July,2021
 */
@AndroidEntryPoint
class DisplayPhotoFragment @Inject constructor(
    private val glide: RequestManager
) : Fragment() {

    private var _binding: FragmentDisplayPhotoBinding? = null
    private val binding get() = _binding!!

    private val arg: DisplayPhotoFragmentArgs by navArgs()

    private val viewModel: DisplayPhotoViewModel by viewModels()

    private lateinit var savedPhoto: SavedPhoto

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDisplayPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToInsertPhotoObserver()
        subscribeToDeletePhotoObserver()

        (activity as MainActivity).binding.include.backButton.setOnClickListener {
            findNavController().popBackStack()
        }


        arg.savedPhotoArg?.let {
            glide.load(it.photoUrl).into(binding.imageView)
            binding.imageTitle.text = it.photoTitle

            savedPhoto = it
        }

        binding.savePhotoButton.setOnClickListener {
            viewModel.savePhoto(savedPhoto)
        }

        binding.deletePhotoButton.setOnClickListener {
            viewModel.deletePhoto(savedPhoto)
        }

        savedPhoto.let {
            if (it.isPhotosSaved) {
                binding.savePhotoButton.visibility = View.GONE
                binding.deletePhotoButton.visibility = View.VISIBLE
            } else {
                binding.savePhotoButton.visibility = View.VISIBLE
                binding.deletePhotoButton.visibility = View.GONE
            }
        }
    }

    private fun subscribeToInsertPhotoObserver() {

        viewModel.insertedPhoto.observe(viewLifecycleOwner, { insertedPhoto ->

            savedPhoto = insertedPhoto

            if (insertedPhoto.isPhotosSaved) {
                Snackbar.make(
                    binding.root,
                    "Photo hase been saved.",
                    Snackbar.LENGTH_LONG
                ).show()

                binding.savePhotoButton.visibility = View.GONE
                binding.deletePhotoButton.visibility = View.VISIBLE
            }
        })

    }

    private fun subscribeToDeletePhotoObserver() {

        viewModel.deletedRowNumber.observe(viewLifecycleOwner, { deletedRowNumber ->

            if (deletedRowNumber > 0) {
                Snackbar.make(
                    binding.root,
                    "Photo hase been deleted.",
                    Snackbar.LENGTH_LONG
                ).show()

                binding.savePhotoButton.visibility = View.VISIBLE
                binding.deletePhotoButton.visibility = View.GONE
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}