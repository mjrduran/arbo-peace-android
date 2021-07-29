package com.arbo.oracoes.presentation.discover.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import com.arbo.oracoes.R
import com.arbo.oracoes.domain.model.DownloadedAudio
import com.arbo.oracoes.presentation.util.extension.hide
import com.arbo.oracoes.presentation.util.extension.show
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AudioListAdapter(
    private var downloadedAudios: MutableList<DownloadedAudio> = mutableListOf(),
    private val audioClickListener: AudioClickListener
) :
    RecyclerView.Adapter<AudioListAdapter.ViewHolder>() {

    inner class ViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {

        fun bind(downloadedAudio: DownloadedAudio) {

            val audio = downloadedAudio.audio
            item.findViewById<TextView>(R.id.audio_title)?.text = audio.title
            item.findViewById<ImageView>(R.id.audio_image)?.let {
                Glide.with(item.context).load(audio.image).placeholder(R.drawable.ic_placeholder)
                    .into(it)
                it.setOnClickListener {
                    audioClickListener.onPlay(downloadedAudio)
                }
            }

            item.findViewById<ImageView>(R.id.play_button)?.show()
            item.findViewById<Group>(R.id.audio_download_progress_group)?.hide()

            item.findViewById<ImageView>(R.id.play_button)?.setOnClickListener {
                audioClickListener.onPlay(downloadedAudio)
            }

            val downloadSwitch = item.findViewById<Switch>(R.id.switch_audio_download)
            downloadSwitch?.setOnCheckedChangeListener(null)
            downloadSwitch?.isChecked =
                downloadedAudio.isDownloaded
            downloadSwitch.isEnabled = true

            if (downloadedAudio.isDownloaded) {
                downloadSwitch?.setText(R.string.audio_delete)
            } else {
                downloadSwitch?.setText(R.string.audio_start_download)
            }

            downloadSwitch
                ?.setOnCheckedChangeListener { compoundButton, checked ->
                    if (checked) {
                        if (!downloadedAudio.isDownloaded) {
                            item.findViewById<Group>(R.id.audio_download_progress_group)
                                ?.show()
                            downloadSwitch.isEnabled = false
                            item.findViewById<ImageView>(R.id.play_button)?.hide()
                            item.findViewById<ImageView>(R.id.audio_image)
                                ?.setOnClickListener { }
                            compoundButton.setText(R.string.audio_delete)
                            audioClickListener.onDownload(downloadedAudio)
                        }
                    } else {
                        MaterialAlertDialogBuilder(item.context)
                            .setTitle(R.string.audio_delete_confirm_title)
                            .setMessage(R.string.audio_delete_confirm_message)
                            .setPositiveButton(R.string.audio_delete_confirm_yes) { _, _ ->
                                compoundButton.setText(R.string.audio_start_download)
                                audioClickListener.onDelete(downloadedAudio)
                            }.setNegativeButton(R.string.audio_delete_confirm_no) { _, _ ->
                                downloadSwitch.isChecked = true
                            }
                            .show()
                    }
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item =
            LayoutInflater.from(parent.context).inflate(R.layout.item_audio, parent, false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return downloadedAudios.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(downloadedAudios[position])
    }

    fun update(audios: MutableList<DownloadedAudio>) {
        this.downloadedAudios = audios
        notifyDataSetChanged()
    }

    fun updateItem(audio: DownloadedAudio) {
        val position = downloadedAudios.indexOf(audio)
        downloadedAudios[position] = audio
        notifyItemChanged(position)
    }
}