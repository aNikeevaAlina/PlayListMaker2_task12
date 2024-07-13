package com.practicum.playlistmaker.createplaylist.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker2.R
import com.practicum.playlistmaker2.databinding.FragmentCreatePlaylistBinding

class CreatePlaylistFragment: Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private var imageUri = Uri.EMPTY
    private var isImageChosen = false

    private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) pickImages() else Toast.makeText(
                requireContext(),
                getString(R.string.needed_permission),
                Toast.LENGTH_LONG
            ).show()
        }
    private val pickMediaLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                imageUri = uri
                isImageChosen = true
                binding.playlistCover.setPadding(0)
                Glide.with(this)
                    .load(uri)
                    .transform(
                        CenterCrop(),
                        RoundedCorners(resources.getDimensionPixelSize(R.dimen.margin_recycler_element))
                    )
                    .into(binding.playlistCover)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editText.doOnTextChanged { text, _, _, _ ->
            binding.createPlaylistButton.isEnabled = !text.isNullOrBlank()
        }

        binding.returnN.setOnClickListener { onBackPressed() }

        binding.playlistCover.setOnClickListener { checkPermissions() }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            }
        )
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
            if (isGranted(Manifest.permission.READ_MEDIA_IMAGES)) {
                pickImages()
            } else {
                permissionsLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            if (isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                pickImages()
            } else {
                permissionsLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            pickImages()
        }
    }

    private fun pickImages() {
        pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun isGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun onBackPressed() {
        if (binding.editText.text.isNullOrBlank() && binding.descriptionEditText.text.isNullOrBlank() && !isImageChosen) {
            findNavController().popBackStack()
        } else {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.fully_create_playlist))
                .setMessage(getString(R.string.unsaved_information_deleted))
                .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(getString(R.string.complete)) { _, _ -> findNavController().popBackStack() }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}