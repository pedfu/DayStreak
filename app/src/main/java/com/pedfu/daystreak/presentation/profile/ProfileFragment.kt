package com.pedfu.daystreak.presentation.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pedfu.daystreak.databinding.FragmentProfileBinding
import com.pedfu.daystreak.domain.user.User
import com.pedfu.daystreak.presentation.MainActivity
import com.pedfu.daystreak.utils.ImageProvider
import com.pedfu.daystreak.utils.lazyViewModel

private const val REQUEST_CODE_SELECT_IMAGE = 1

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!

    private val viewModel by lazyViewModel {
        ProfileViewModel(requireContext())
    }

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackButton()
        binding.run {
            observeViewModel()
            setupButtons()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBackButton() {
        onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigateUp()
        }
    }

    private fun FragmentProfileBinding.setupButtons() {
        imageViewProfilePicture.setOnClickListener {
            openGallery()
        }
        linearLayoutSignOut.setOnClickListener {
            (requireActivity() as? MainActivity)?.signOutAndStartSignInActivity()
        }
    }

    private fun FragmentProfileBinding.observeViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { setState(it) }
        viewModel.userLiveData.observe(viewLifecycleOwner) { setUser(it) }
        viewModel.newImageProfileLiveData.observe(viewLifecycleOwner) {
            if (it != null)
                imageViewProfilePicture.setImageURI(it)
        }
    }

    private fun FragmentProfileBinding.setState(state: ProfileState) {
        when (state) {
            ProfileState.IDLE -> {
                progressBar.isVisible = false
            }
            else -> {}
        }
    }

    private fun FragmentProfileBinding.setUser(user: User?) {
        if (user != null) {
            textViewUserName.text = user.username
            if (user.photoUrl != null) {
                imageViewProfilePicture.setImageURI(user.photoUrl)
                ImageProvider.loadImageFromUrl(imageViewProfilePicture, user.photoUrl.toString())
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            viewModel.onSelectNewProfileImage(selectedImageUri)
        }
    }
}