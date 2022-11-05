package com.stoe.wallpapers

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//8
//acum in detail fragment putem sa luam poza pasata din home
class DetailFragment : Fragment(), View.OnClickListener {

    private var image: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        image = DetailFragmentArgs.fromBundle(requireArguments()).wallpaperimage  //luam poza din home fragment

        //set wallpaper button clicked
        detail_set_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.detail_set_btn -> setWallpaper()
        }
    }

    private fun setWallpaper() {
        val bitmap: Bitmap = detail_image.drawable.toBitmap()
        detail_set_btn.text = "Wallpaper set"
        detail_set_btn.setBackgroundResource(R.drawable.rounded_button_pressed)

        GlobalScope.launch {
            val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(context)
            wallpaperManager.setBitmap(bitmap)
        }
    }

    //daca iesim din aplicatie cat timp un wallpaper e selectat, dispare, asa ca punem functia de load in on start
    override fun onStart() {
        super.onStart()
        if(image != null){
            //set image
            Glide.with(requireContext()).load(image).listener(
                object: RequestListener<Drawable>{
                    override fun onLoadFailed(
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
                        //image loaded, show set wallpaper button
                        detail_set_btn.visibility = View.VISIBLE
                        //hide progress bar
                        detail_wallpaper_progress_bar.visibility = View.INVISIBLE
                        return false
                    }

                }
            ).into(detail_image)
        }
    }

    override fun onStop() {
        super.onStop()
        Glide.with(requireContext()).clear(detail_image)
    }

}