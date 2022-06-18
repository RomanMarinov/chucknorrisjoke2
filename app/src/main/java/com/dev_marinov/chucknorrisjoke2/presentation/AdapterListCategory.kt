package com.dev_marinov.chucknorrisjoke2.presentation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dev_marinov.chucknorrisjoke2.R
import com.dev_marinov.chucknorrisjoke2.databinding.ItemCategoryBinding
import com.dev_marinov.chucknorrisjoke2.presentation.jokes.JokesViewModel

class AdapterListCategory(
    private val jokesViewModel: JokesViewModel,
    private val mListener: OnItemClickListener
) : RecyclerView.Adapter<AdapterListCategory.ViewHolder>() {

    private var categories: ArrayList<String> = ArrayList()

    interface OnItemClickListener {
        fun onItemClick(position: Int, clickCategory: String, widthTextViewCategory: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val listItemBinding = ItemCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(listItemBinding, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // передаем в bind значения массива и позицию
        holder.bind(categories[position], position)
    }

    override fun getItemCount() = categories.size

    fun refreshCategories(arrayList: ArrayList<String>) {
        this.categories = arrayList
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val binding: ItemCategoryBinding,
        listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cardView.setOnClickListener {
                notifyDataSetChanged()
                listener.onItemClick(
                    bindingAdapterPosition,
                    binding.tvCategory.text.toString(),
                    binding.tvCategory.width
                )
            }
        }

        fun bind(item: String, position: Int) {
            binding.category = item

            val textColorResId = if (jokesViewModel.selectedPosition == position)
                ContextCompat.getColor(binding.root.context, R.color.orange)
            else Color.GRAY

            val backGroundResId = if (jokesViewModel.selectedPosition == position)
                R.drawable.button_turn_off
            else Color.TRANSPARENT

            binding.tvCategory.setTextColor(textColorResId)
            binding.cardView.setBackgroundResource(backGroundResId)

            // Метод executePendingBindings используется, чтобы биндинг не откладывался,
            // а выполнился как можно быстрее. Это критично в случае с RecyclerView.
            binding.executePendingBindings()
        }
    }
}

