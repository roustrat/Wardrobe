package com.str.wardrobe.simpleMVVM.views.dressInfo

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.str.foundation.utils.getScaledBitmap
import com.str.wardrobe.R
import com.str.foundation.views.BaseFragment
import com.str.foundation.views.BaseScreen
import com.str.foundation.views.screenViewModel
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

        super.onViewCreated(view, savedInstanceState)
    }


}