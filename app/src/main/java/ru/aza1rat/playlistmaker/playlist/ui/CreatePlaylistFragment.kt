package ru.aza1rat.playlistmaker.playlist.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.databinding.FragmentCreatePlaylistBinding
import ru.aza1rat.playlistmaker.playlist.ui.model.CreatePlaylistState
import ru.aza1rat.playlistmaker.playlist.ui.view_model.CreatePlaylistViewModel
import ru.aza1rat.playlistmaker.util.ui.getNavController

class CreatePlaylistFragment : Fragment() {
    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private var textWatcher: TextWatcher? = null
    private val viewModel: CreatePlaylistViewModel by viewModel()
    private var coverSet: Boolean = false
    private var _alertDialog: AlertDialog? = null
    private val alertDialog get() = _alertDialog!!

    private val onBackCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            performExit(alertDialog)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createPlaylist.isEnabled = false
        _alertDialog = createOnExitDialog()
        setupListeners()
        setupOnExitListeners()
        viewModel.observePlaylistState().observe(viewLifecycleOwner) {
            when (it) {
                is CreatePlaylistState.Content -> {
                    Glide.with(this).load(it.imageUri).into(binding.coverPlaylist)
                    coverSet = true
                }

                is CreatePlaylistState.Created -> {
                    Toast.makeText(
                        requireActivity(),
                        getPlaylistCreatedMessage(it.playlistName),
                        Toast.LENGTH_LONG
                    ).show()
                    navigateUp()
                }
            }
        }
    }

    override fun onDestroyView() {
        _alertDialog = null
        textWatcher?.let { binding.name.removeTextChangedListener(it) }
        textWatcher = null
        binding.name.onFocusChangeListener = null
        binding.description.onFocusChangeListener = null
        _binding = null
        super.onDestroyView()
    }

    private fun setupListeners() {
        textWatcher = object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence?, start: Int, before: Int, count: Int
            ) {
                binding.createPlaylist.isEnabled = !(s.isNullOrEmpty() || s.trim().isEmpty())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {}
        }
        textWatcher?.let { binding.name.addTextChangedListener(it) }
        binding.name.onFocusChangeListener = createOnFocusChangeListener(binding.nameInputLayout)
        binding.description.onFocusChangeListener =
            createOnFocusChangeListener(binding.descriptionInputLayout)
        binding.createPlaylist.setOnClickListener {
            viewModel.createPlaylist(
                binding.name.text.toString(), binding.description.text.toString()
            )
        }
        val pickMedia = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                viewModel.imageUriObtained(uri)
            }
        }
        binding.coverPlaylist.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun createOnFocusChangeListener(inputLayout: TextInputLayout): View.OnFocusChangeListener {
        return View.OnFocusChangeListener { view, hasFocus ->
            val context = context ?: return@OnFocusChangeListener
            if (!hasFocus) {
                if (!(view as TextInputEditText).text.isNullOrEmpty()) {
                    inputLayout.defaultHintTextColor =
                        ContextCompat.getColorStateList(context, R.color.create_playlist_hint_color)
                } else {
                    val typedValue = TypedValue()
                    activity?.let {
                        it.theme.resolveAttribute(
                            com.google.android.material.R.attr.colorOnPrimary,
                            typedValue,
                            true
                        )
                        inputLayout.defaultHintTextColor =
                            ContextCompat.getColorStateList(context, typedValue.resourceId)
                    }
                }
            }
        }
    }

    private fun setupOnExitListeners() {
        binding.back.setOnClickListener {
            performExit(alertDialog)
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackCallback)
    }

    private fun createOnExitDialog(): AlertDialog {
        return MaterialAlertDialogBuilder(requireActivity()).setTitle(getString(R.string.create_playlist_dialog_title))
            .setMessage(getString(R.string.create_playlist_dialog_message))
            .setPositiveButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }.setNegativeButton(getString(R.string.finish)) { _, _ ->
                navigateUp()

            }.create()
    }

    private fun getPlaylistCreatedMessage(playlistName: String): String? {
        return context?.getString(R.string.param_playlist_created, playlistName)
    }

    private fun performExit(dialog: AlertDialog) {
        if (isDialogNeeded()) {
            dialog.show()
        } else {
            navigateUp()
        }
    }

    private fun isDialogNeeded(): Boolean {
        return !binding.name.text.isNullOrEmpty() || !binding.description.text.isNullOrEmpty() || coverSet
    }

    private fun navigateUp() {
        requireActivity().getNavController(R.id.fragmentContainer).navigateUp()
    }
}