package com.dev_marinov.chucknorrisjoke2.presentation.jokes

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dev_marinov.chucknorrisjoke2.R
import com.dev_marinov.chucknorrisjoke2.databinding.ItemCategoryBinding
import com.dev_marinov.chucknorrisjoke2.presentation.model.SelectableCategory

class CategoryAdapter(
    private val onItemClick: (position: Int, category: SelectableCategory, width: Int) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var categories: ArrayList<SelectableCategory> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding = ItemCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(listItemBinding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount() = categories.size

    fun refreshCategories(categories: ArrayList<SelectableCategory>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemCategoryBinding,
        private val onItemClick: (position: Int, clickCategory: SelectableCategory, widthTextViewCategory: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: SelectableCategory) {
            binding.category = category

            val textColorResId = getTextColorResId(category.isSelected)
            val backGroundResId = getBackgroundResId(category.isSelected)

            binding.tvCategory.setTextColor(textColorResId)
            binding.cardView.setBackgroundResource(backGroundResId)
            binding.cardView.setOnClickListener {
                onItemClick(
                    bindingAdapterPosition,
                    category,
                    binding.tvCategory.width
                )
                notifyDataSetChanged()
            }

            // ?????????? executePendingBindings ????????????????????????, ?????????? ?????????????? ???? ????????????????????????,
            // ?? ???????????????????? ?????? ?????????? ??????????????. ?????? ???????????????? ?? ???????????? ?? RecyclerView.
            binding.executePendingBindings()
        }

        private fun getTextColorResId(isSelected: Boolean): Int {
            return if (isSelected)
                ContextCompat.getColor(binding.root.context, R.color.orange)
            else Color.GRAY
        }

        private fun getBackgroundResId(isSelected: Boolean): Int {
            return if (isSelected)
                R.drawable.button_turn_off
            else Color.TRANSPARENT
        }
    }
}

