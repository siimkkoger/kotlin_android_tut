package com.example.kotlin_android_tut

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class ViewHolder(v: View) {
    val tvName: TextView = v.findViewById(R.id.textViewTitle)
    val tvArtist: TextView = v.findViewById(R.id.textViewArtist)
    val tvSummary: TextView = v.findViewById(R.id.textViewSummary)
    val ivPicture: ImageView = v.findViewById(R.id.imageViewMovie)
}

class FeedAdapter(
    context: Context,
    private val resource: Int,
    private val feedEntries: List<FeedEntry>
) : ArrayAdapter<FeedEntry>(context, resource) {
    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val currentEntry = feedEntries[position]

        viewHolder.tvName.text = "${position + 1}. ${currentEntry.name}"
        viewHolder.tvArtist.text = currentEntry.artist
        viewHolder.tvSummary.text = currentEntry.summary
        Glide.with(context).load(currentEntry.imageURL).into(viewHolder.ivPicture)

        return view
    }

    override fun getCount(): Int {
        return feedEntries.size
    }
}