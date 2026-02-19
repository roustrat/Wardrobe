package com.str.wardrobe.simpleMVVM.views.categorydresses

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
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
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dressNameTextView: TextView = itemView.findViewById(R.id.dressNameTextView)
        val  dressImageView: ImageView = itemView.findViewById(R.id.dressImageView)
        val settingsImageView: ImageView = itemView.findViewById(R.id.settingsImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = inflater.inflate(R.layout.dress_item, parent, false)
        val settingsButton = binding.findViewById<ImageView>(R.id.settingsImageView)
        binding.rootView.setOnClickListener(this)
        settingsButton.setOnClickListener(this)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val namedDress = items[position]
        with(holder) {
            this.itemView.tag = namedDress
            this.itemView.findViewById<ImageView>(R.id.settingsImageView).tag = namedDress
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
        when (p0.id) {
            R.id.settingsImageView -> {
                showPopupMenu(p0)
            }
            else -> {
                val item = p0.tag as NamedDress
                listener.onDressDetails(item)
            }

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val context = view.context
        val item = view.tag as NamedDress
        val position = items.indexOfFirst { it.imgId == item.imgId }

        popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, context.getString(R.string.move_up)).apply {
            isEnabled = position > 0
        }
        popupMenu.menu.add(0, ID_MOVE_DOWN, Menu.NONE, context.getString(R.string.move_down)).apply {
            isEnabled = position < items.size - 1
        }
        popupMenu.menu.add(0, ID_EDIT, Menu.NONE, context.getString(R.string.edit))
        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, context.getString(R.string.remove))

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_MOVE_UP -> {
                    listener.onDressMove(item, -1)
                    notifyDataSetChanged()
                }
                ID_MOVE_DOWN -> {
                    listener.onDressMove(item, 1)
                    notifyDataSetChanged()
                }
                ID_EDIT -> {
                    listener.onDressEdit(item)
                }
                ID_REMOVE -> {
                    listener.onDressDelete(item)
                    notifyDataSetChanged()
                }
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }

    interface Listener {
        /**
         * Called when user chooses the specified color
         * @param namedDress dress chosen by the user
         */
        fun onDressDetails(namedDress: NamedDress)

        fun onDressMove(namedDress: NamedDress, moveBy: Int)

        fun onDressDelete(namedDress: NamedDress)

        fun onDressEdit(namedDress: NamedDress)
    }

    companion object {
        private const val ID_MOVE_UP = 1
        private const val ID_MOVE_DOWN = 2
        private const val ID_EDIT = 3
        private const val ID_REMOVE = 4
    }
}