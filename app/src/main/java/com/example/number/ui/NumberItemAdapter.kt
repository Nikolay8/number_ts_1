package com.example.number.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.number.dao.model.NumberModel
import com.example.number.databinding.ItemNumberBinding

/**
 * Combined adapter for using with number item
 */
class NumberItemAdapter(
    val numberSelected: (NumberModel) -> Unit
) : ListAdapter<NumberModel, NumberItemAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: NumberItemAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemNumberBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(number: NumberModel) = with(binding) {
            binding.itemInfoTextView.text = number.info
            binding.itemAccountMainLayout.setOnClickListener {
                numberSelected(number)
            }
        }
    }

    /**
     * Callback for calculating the diff between two non-null items in a list.
     */
    class DiffCallback : DiffUtil.ItemCallback<NumberModel>() {
        override fun areItemsTheSame(
            oldItem: NumberModel, newItem: NumberModel
        ): Boolean {
            return oldItem.id == newItem.id && oldItem.info == newItem.info
        }

        override fun areContentsTheSame(
            oldItem: NumberModel, newItem: NumberModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}
