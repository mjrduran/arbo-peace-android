package com.arbo.oracoes.presentation.mood.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arbo.oracoes.R
import com.arbo.oracoes.domain.model.TextCategory


class MoodAdapter(private var categories: List<TextCategory> = emptyList(),
                  private val moodClickListener: MoodClickListener
) :
    RecyclerView.Adapter<MoodAdapter.ViewHolder>() {

    inner class ViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {

        fun bind(textCategory: TextCategory) {
            item.findViewById<TextView>(R.id.text_title).text = textCategory.title
            item.setOnClickListener {
                moodClickListener.onClick(textCategory)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item =
            LayoutInflater.from(parent.context).inflate(R.layout.item_text_category, parent, false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    fun update(categories: List<TextCategory>){
        this.categories = categories
    }
}