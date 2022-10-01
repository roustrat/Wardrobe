package com.str.wardrobe.simpleMVVM.views.categorydresses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.str.wardrobe.R
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedCategory

/**
 * Adapter for displaying the list of available categories
 * @param listener callback which notifies about user actions on items in the list, see [Listener] for details.
 */

class CategoriesAdapter (
    private val listener: Listener
):  RecyclerView.Adapter<CategoriesAdapter.Holder>(), View.OnClickListener{

    var items: List<NamedCategory> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryNameTextView: TextView = itemView.findViewById(R.id.categoryNameTextView)
        val categoryDescriptionTextView: TextView = itemView.findViewById(R.id.categoryDescriptionTextView)
    }

    interface Listener {
        /**
         * Called when user chooses the specified category
         * @param namedCategory category chosen by the user
         */
        fun onCategoryChosen(namedCategory: NamedCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = inflater.inflate(R.layout.category_item, parent, false)
        binding.setOnClickListener(this)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val namedCategory = items[position]
        with(holder) {
            this.categoryNameTextView.text = namedCategory.name
            this.categoryDescriptionTextView.text = namedCategory.description
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onClick(p0: View) {
        val item = p0.tag as NamedCategory
        listener.onCategoryChosen(item)
    }
}