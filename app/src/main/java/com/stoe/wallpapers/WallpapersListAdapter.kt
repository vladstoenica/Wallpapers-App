package com.stoe.wallpapers

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.list_single_item.view.*

//6
//the adapter class takes 2 parameters: list of wallpapers model, clickListener interface
class WallpapersListAdapter(var wallpapersList: List<WallpapersModel>, val clickListener: (WallpapersModel) -> Unit) : RecyclerView.Adapter<WallpapersListAdapter.WallpapersViewHolder>(){

    class WallpapersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //bind the data to the view (view-ul reprezentat de list_single_item
        fun bind(wallpapers: WallpapersModel, clickListener: (WallpapersModel) -> Unit){
            //Load the image  -  adaugam un listener in glide ca sa oprim progress bar-ul odata ce s-a incarcat thumbnailul
            Glide.with(itemView.context).load(wallpapers.thumbnail).listener(
                object : RequestListener<Drawable> {
                    override fun onLoadFailed(             //functiile aste 2 sunt pt progress bar ca sa dispara
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        itemView.list_single_progress.visibility = View.GONE
                        return false
                    }

                }
            ).into(itemView.list_single_image)
            //click listener
            itemView.setOnClickListener{
                clickListener(wallpapers)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpapersViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_single_item, parent, false)
        return WallpapersViewHolder(view)
    }

    override fun onBindViewHolder(holder: WallpapersViewHolder, position: Int) {
        //bind am declarat-o noi mai sus
        (holder as WallpapersViewHolder).bind(wallpapersList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return wallpapersList.size
    }
}