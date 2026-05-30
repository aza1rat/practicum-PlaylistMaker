package ru.aza1rat.playlistmaker.playlist.ui.api

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.databinding.FragmentCreatePlaylistBinding
import ru.aza1rat.playlistmaker.playlist.ui.model.CreatePlaylistState
import ru.aza1rat.playlistmaker.util.ui.getNavController

abstract class BaseInputPlaylistFragment<T>: Fragment() where T: ViewModel, T: InputPlaylistViewModel {
    private var _binding: FragmentCreatePlaylistBinding? = null
    protected val binding get() = _binding!!
    private var textWatcher: TextWatcher? = null
    protected var coverSet: Boolean = false
    abstract val playlistViewModel: T
    private val onBackCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (onPerformExit())
                navigateUp()
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
        binding.createPlaylist.isEnabled = false
        setupListeners()
        setupOnExitListeners()
        playlistViewModel.observePlaylistState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is CreatePlaylistState.Content -> {
                    Glide.with(this).load(state.imageUri).into(binding.coverPlaylist)
                    coverSet = true
                }
                is CreatePlaylistState.Created -> {
                    onExitWithSuccess(state.playlistName)
                    navigateUp()
                }
            }
        }
    }

    override fun onDestroyView() {
        textWatcher?.let { binding.name.removeTextChangedListener(it) }
        textWatcher = null
        binding.name.onFocusChangeListener = null
        binding.description.onFocusChangeListener = null
        _binding = null
        super.onDestroyView()
    }

    abstract fun onPerformExit(): Boolean
    open fun onExitWithSuccess(playlistName: String) {}

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
        binding.description.onFocusChangeListener = createOnFocusChangeListener(binding.descriptionInputLayout)
        val pickMedia = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                playlistViewModel.imageUriObtained(uri)
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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackCallback)
        binding.back.setOnClickListener {
            if (onPerformExit())
                navigateUp()
        }
    }


    protected fun navigateUp() {
        requireActivity().getNavController(R.id.fragmentContainer).navigateUp()
    }
}