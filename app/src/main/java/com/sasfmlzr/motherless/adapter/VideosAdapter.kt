package com.sasfmlzr.motherless.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sasfmlzr.motherless.R
import com.sasfmlzr.motherless.databinding.ItemPreviewVideoBinding
import com.sasfmlzr.motherless.entity.NullPreviewData
import com.sasfmlzr.motherless.entity.PreviewData
import com.sasfmlzr.motherless.entity.UnknownPreviewData
import com.sasfmlzr.motherless.util.Converter

class VideosAdapter(
    private val items: List<UnknownPreviewData>,
    private val context: Context?
) : RecyclerView.Adapter<VideosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPreviewVideoBinding.inflate(LayoutInflater.from(context)))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        holder.binder.root.layoutParams = params

        when (items[position]) {

            is PreviewData -> {
                val thumbnail = (items[position] as PreviewData).thumbnailLink

                Glide.with(context!!)
                    .load(thumbnail)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.binder.previewImage)

                holder.binder.previewImage.setOnClickListener(
                    createOnClickListener(
                        (items[position] as PreviewData).link
                    )
                )
            }
            is NullPreviewData -> {
                holder.binder.previewImage.setImageDrawable(context?.getDrawable(R.mipmap.logo_header))
            }
        }
    }

    private fun createOnClickListener(url: String): View.OnClickListener {
        return View.OnClickListener {
            it.findNavController()
                .navigate(R.id.action_nav_home_to_nav_player, bundleOf(Pair("KEY_URL", url)))
        }
    }

    class ViewHolder(val binder: ItemPreviewVideoBinding) : RecyclerView.ViewHolder(binder.root)
}