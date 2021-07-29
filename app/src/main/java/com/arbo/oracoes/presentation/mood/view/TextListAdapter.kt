package com.arbo.oracoes.presentation.mood.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arbo.oracoes.R
import com.arbo.oracoes.domain.model.Text
import com.bumptech.glide.Glide

class TextListAdapter(
    private var texts: List<Text> = emptyList(),
    private val textClickListener: TextClickListener
) :
    RecyclerView.Adapter<TextListAdapter.ViewHolder>() {

    inner class ViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {

        fun bind(text: Text) {

            item.findViewById<TextView>(R.id.text_title)?.text = text.title
            item.findViewById<ImageView>(R.id.text_image)?.let {
                Glide.with(item.context).load(text.image).placeholder(R.drawable.ic_placeholder).into(it)
            }
            item.setOnClickListener {
                textClickListener.onClick(text)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item =
            LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return texts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(texts[position])
    }

    fun update(texts: List<Text>) {
        this.texts = texts
        notifyDataSetChanged()
    }
}