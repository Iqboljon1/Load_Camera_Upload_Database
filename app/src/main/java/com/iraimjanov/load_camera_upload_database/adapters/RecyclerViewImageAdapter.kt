package com.iraimjanov.load_camera_upload_database.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.iraimjanov.load_camera_upload_database.R
import com.iraimjanov.load_camera_upload_database.databinding.ItemImageBinding
import com.iraimjanov.load_camera_upload_database.models.MyImage

class RecyclerViewImageAdapter(
    val context: Context,
    private val arrayListMyImage: ArrayList<MyImage>,
) :
    RecyclerView.Adapter<RecyclerViewImageAdapter.VH>() {

    inner class VH(var itemRV: ItemImageBinding) : RecyclerView.ViewHolder(itemRV.root) {
        fun onBind(myImage: MyImage , position: Int) {
            itemRV.tvName.text = myImage.name
            Glide.with(context).load(myImage.image).apply(RequestOptions().placeholder(R.drawable.ic_image)).centerCrop().into(itemRV.imageView)
            if (position % 2 != 0){

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(arrayListMyImage[position] , position)

    }

    override fun getItemCount(): Int = arrayListMyImage.size

}