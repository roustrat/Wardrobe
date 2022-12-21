package com.str.wardrobe.simpleMVVM.views.dressInfo

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import com.str.foundation.utils.getScaledBitmap
import com.str.foundation.views.BaseFragment
import com.str.foundation.views.BaseScreen
import com.str.foundation.views.screenViewModel
import com.str.wardrobe.R
import com.str.wardrobe.simpleMVVM.MainActivity
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress
import java.io.File


class DressInfoFragment : BaseFragment() {


    class Screen (
        val nameDress: NamedDress
            ): BaseScreen

    override val viewModel by screenViewModel<DressInfoViewModel>()

    private lateinit var dressName: TextView
    private lateinit var dressDescription: TextView
    private lateinit var dressImage: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.dress_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        dressName = view.findViewById(R.id.nameOfDress)
        dressDescription = view.findViewById(R.id.descriptionOfDress)
        dressImage = view.findViewById(R.id.imageOfDress)

        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)

        dressName.text = viewModel.currentDress.name
        dressDescription.text = viewModel.currentDress.description
        val photoFile = File(requireContext().applicationContext.filesDir, viewModel.currentDress.photoFileName)
        if (photoFile.exists()) {
            val bitmap = getScaledBitmap(photoFile.path, requireActivity())
            dressImage.setImageBitmap(bitmap)
        } else {
            dressImage.setImageResource(R.drawable.empty_photo)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
//                requireActivity().onBackPressed()
//                val fm = requireActivity().supportFragmentManager
//                fm.popBackStack()
                viewModel.closeFragment()
            }
        })

        (activity as MainActivity?)!!.resetActionBar(true, DrawerLayout.LOCK_MODE_LOCKED_CLOSED, viewModel.backFragmentScreen())

        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()
        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_dressinfo_fragment, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.change -> {
                        viewModel.changeDress()
                        true
                    }
                    R.id.delete -> {
                        viewModel.closeWithoutSaveDress()
                        true
                    }
                    else -> {
                        viewModel.closeFragment()
                        false
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        super.onViewCreated(view, savedInstanceState)
    }

//    fun showMenu(v: View) {
//        PopupMenu(requireContext(), v).apply {
//            // MainActivity implements OnMenuItemClickListener
//            setOnMenuItemClickListener(requireActivity())
//            inflate(R.menu.dressinfo_popup_menu)
//            show()
//        }
//    }
//
//    override fun onMenuItemClick(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.change -> {
//
//                true
//            }
//            R.id.delete -> {
//
//                true
//            }
//            else -> false
//        }
//    }


}