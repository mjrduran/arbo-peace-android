package com.arbo.oracoes.presentation.discover.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arbo.oracoes.R
import com.arbo.oracoes.domain.model.AudioCategory

class DiscoverAdapter(private var audioCategories: List<AudioCategory> = emptyList(),
                      private val discoverClickListener: DiscoverClickListener) :
    RecyclerView.Adapter<DiscoverAdapter.ViewHolder>() {

    inner class ViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {

        fun bind(audioCategory: AudioCategory) {
            item.findViewById<TextView>(R.id.audio_title).text = audioCategory.title
            item.setOnClickListener {
                discoverClickListener.onClick(audioCategory)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item =
            LayoutInflater.from(parent.context).inflate(R.layout.item_audio_category, parent, false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return audioCategories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(audioCategories[position])
    }

    fun update(categories: List<AudioCategory>){
        this.audioCategories = categories
    }
}