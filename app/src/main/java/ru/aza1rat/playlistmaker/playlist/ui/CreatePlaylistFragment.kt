package ru.aza1rat.playlistmaker.playlist.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.aza1rat.playlistmaker.R
import ru.aza1rat.playlistmaker.playlist.ui.api.BaseInputPlaylistFragment
import ru.aza1rat.playlistmaker.playlist.ui.view_model.CreatePlaylistViewModel

class CreatePlaylistFragment : BaseInputPlaylistFragment<CreatePlaylistViewModel>() {
    private var _alertDialog: AlertDialog? = null
    private val alertDialog get() = _alertDialog!!
    override val playlistViewModel: CreatePlaylistViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeaderAndMainButtonText()
        binding.createPlaylist.setOnClickListener {
            playlistViewModel.createPlaylist(
                binding.name.text.toString(),
                binding.description.text.toString()
            )
        }
        _alertDialog = createOnExitDialog()
    }

    override fun onDestroyView() {
        _alertDialog = null
        super.onDestroyView()
    }

    private fun setHeaderAndMainButtonText() {
        binding.header.text = getString(R.string.update_playlist)
        binding.createPlaylist.text = getString(R.string.save)
    }

    override fun onPerformExit(): Boolean {
        if (isDialogNeeded()) {
            alertDialog.show()
        } else {
            return true
        }
        return false
    }

    override fun onExitWithSuccess(playlistName: String) {
        Toast.makeText(
            requireActivity(),
            getPlaylistCreatedMessage(playlistName),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun getPlaylistCreatedMessage(playlistName: String): String? {
        return context?.getString(R.string.param_playlist_created, playlistName)
    }

    private fun isDialogNeeded(): Boolean {
        return !binding.name.text.isNullOrEmpty() || !binding.description.text.isNullOrEmpty() || coverSet
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
}