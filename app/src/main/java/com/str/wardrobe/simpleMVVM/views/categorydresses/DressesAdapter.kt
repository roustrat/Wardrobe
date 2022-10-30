package com.str.wardrobe.simpleMVVM.views.categorydresses

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.str.foundation.utils.getScaledBitmap
import com.str.wardrobe.R
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress
import java.io.File

/**
 * Adapter for displaying the list of available categories
 * @param listener callback which notifies about user actions on items in the list, see [Listener] for details.
 */
class DressesAdapter(
    private val listener: Listener,
    private val context: Context,
    private val activity: Activity
) : RecyclerView.Adapter<DressesAdapter.Holder>(), View.OnClickListener  {

    /**
     * If you want to represents list item for the dress you should make new class with two parametrs: [NamedDress] and selectable: Boolean;
     * it may be selected or not
     */

    var items: List<NamedDress> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dressNameTextView: TextView = itemView.findViewById(R.id.dressNameTextView)
        val  dressImageView: ImageView = itemView.findViewById(R.id.dressImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = inflater.inflate(R.layout.dress_item, parent, false)
        binding.setOnClickListener(this)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val namedDress = items[position]
        with(holder) {
            this.dressNameTextView.text = namedDress.name
            val photoFile = File(context.applicationContext.filesDir, namedDress.photoFileName)
            if (photoFile.exists()) {
                val bitmap = getScaledBitmap(photoFile.path, activity)
               this.dressImageView.setImageBitmap(bitmap)
                Log.d("DressImageView", "Success")
            } else {
               this.dressImageView.setImageResource(R.drawable.empty_photo)
                Log.d("DressImageView", "Error")
            }
//            setPhotoView(namedDress, this.dressImageView)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onClick(p0: View) {
        val item = p0.tag as NamedDress
        listener.onDressChosen(item)
    }

    interface Listener {
        /**
         * Called when user chooses the specified color
         * @param namedDress dress chosen by the user
         */
        fun onDressChosen(namedDress: NamedDress)
    }

    private fun setPhotoView(dress: NamedDress, image: ImageView) {
        val photoFile = File(context.applicationContext.filesDir, "IMG_"+dress.photoFileName)
        if (photoFile.exists()) {
            val bitmap = getScaledBitmap(photoFile.path, activity)
            image.setImageBitmap(bitmap)
        } else {
            image.setImageResource(R.drawable.empty_photo)
        }
    }
}