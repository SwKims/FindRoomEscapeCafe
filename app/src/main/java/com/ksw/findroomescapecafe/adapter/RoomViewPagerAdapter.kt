package com.ksw.findroomescapecafe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ksw.findroomescapecafe.databinding.ItemViewpagerBinding
import com.ksw.findroomescapecafe.model.RoomsModel

/**
 * Created by KSW on 2021-09-08
 */

class RoomViewPagerAdapter(private val itemClicked: (RoomsModel) -> Unit) :
    ListAdapter<RoomsModel, RoomViewPagerAdapter.ItemViewHolder>(diffUtil) {

    inner class ItemViewHolder(private val binding: ItemViewpagerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(roomsModel: RoomsModel) {
            binding.tvRoomTitle.text = roomsModel.title
            if (!roomsModel.hourInfo.isNullOrBlank()) binding.tvRoomHour.text = roomsModel.hourInfo

            binding.cView.setOnClickListener {
                itemClicked(roomsModel)
            }

            Glide.with(binding.ivRoomImage.context)
                .load(roomsModel.imgUrl)
                .into(binding.ivRoomImage)

        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RoomViewPagerAdapter.ItemViewHolder {
        return ItemViewHolder(
            ItemViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RoomViewPagerAdapter.ItemViewHolder, position: Int) {
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