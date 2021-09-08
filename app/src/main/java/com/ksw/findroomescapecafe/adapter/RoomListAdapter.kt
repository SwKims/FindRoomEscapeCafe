package com.ksw.findroomescapecafe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ksw.findroomescapecafe.databinding.ItemRoomsBinding
import com.ksw.findroomescapecafe.model.RoomsModel

/**
 * Created by KSW on 2021-09-08
 */

class RoomListAdapter(private val itemClicked: (RoomsModel) -> Unit) :
    ListAdapter<RoomsModel, RoomListAdapter.ItemViewHolder>(diffUtil) {

    inner class ItemViewHolder(private val binding: ItemRoomsBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(roomsModel: RoomsModel) {
                binding.tvRoomEscapeTitle.text = roomsModel.title

                Glide.with(binding.ivRoomEscape.context)
                    .load(roomsModel.imgUrl)
                    .transform(CenterCrop(), RoundedCorners( 12))
                    .into(binding.ivRoomEscape)

                binding.ivRoomEscape.setOnClickListener {
                    itemClicked(roomsModel)
                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemRoomsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<RoomsModel>() {
            override fun areItemsTheSame(oldItem: RoomsModel, newItem: RoomsModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RoomsModel, newItem: RoomsModel): Boolean {
                return oldItem == newItem
            }
        }
    }


}