package com.podplay.android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.podplay.android.databinding.EpisodeItemBinding
import com.podplay.android.model.EpisodeViewData
import com.podplay.android.util.DateUtils
import com.podplay.android.util.HtmlUtils

class EpisodeListAdapter(
    private var episodeViewList: List<EpisodeViewData>?,
    private val episodeListAdapterListener: EpisodeListAdapterListener
) :
    RecyclerView.Adapter<EpisodeListAdapter.ViewHolder>() {

    interface EpisodeListAdapterListener {
        fun onSelectedEpisode(episodeViewData: EpisodeViewData)
    }

    inner class ViewHolder(
        databinding: EpisodeItemBinding,
        val episodeListAdapterListener: EpisodeListAdapterListener
    ) : RecyclerView.ViewHolder(databinding.root) {

        init {
            databinding.root.setOnClickListener {
                episodeViewData?.let {
                    episodeListAdapterListener.onSelectedEpisode(it)
                }
            }
        }

        var episodeViewData: EpisodeViewData? = null
        val titleTextView: TextView = databinding.titleView
        val descTextView: TextView = databinding.descView
        val durationTextView: TextView = databinding.durationView
        val releaseDateTextView: TextView = databinding.releaseDateView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EpisodeListAdapter.ViewHolder {
        return ViewHolder(
            EpisodeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), episodeListAdapterListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episodeViewList = episodeViewList ?: return
        val episodeView = episodeViewList[position]

        holder.episodeViewData = episodeView
        holder.titleTextView.text = episodeView.title
        holder.descTextView.text = HtmlUtils.htmlToSpannable(episodeView.description ?: "")
        holder.durationTextView.text = episodeView.duration
        holder.releaseDateTextView.text = episodeView.releaseDate?.let {
            DateUtils.dateToShortDate(it)
        }
    }

    override fun getItemCount(): Int {
        return episodeViewList?.size ?: 0
    }
}
