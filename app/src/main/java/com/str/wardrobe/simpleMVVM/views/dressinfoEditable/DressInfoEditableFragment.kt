package com.str.wardrobe.simpleMVVM.views.dressinfoEditable

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.nfc.FormatException
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import com.str.foundation.utils.getScaledBitmap
import com.str.foundation.views.BaseFragment
import com.str.foundation.views.BaseScreen
import com.str.foundation.views.screenViewModel
import com.str.wardrobe.R
import com.str.wardrobe.simpleMVVM.model.entities.NamedCategory
import java.io.File
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.random.Random

// Позже надо перейти к единому фрагменту и для просмотра, и для редактирования. Сам dress сделав тут LiveData и реализовывая как в книге

class DressInfoEditableFragment : BaseFragment() {


    class Screen (
        val nameDressCategory: NamedCategory
    ): BaseScreen

    override val viewModel by screenViewModel<DressInfoEditableViewModel>()

    private lateinit var dressImage: ImageView
    private lateinit var dressName: EditText
    private lateinit var categoryChooseSpin: Spinner
    private lateinit var dressDescription: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel.allCategoriesName.observe(viewLifecycleOwner) {
            if (it != null) {
                categoryChooseSpin.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,
                    viewModel.allCategoriesName.value ?: emptyList()
                )
            }

        }

        return inflater.inflate(R.layout.dress_info_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        dressImage = view.findViewById(R.id.imageOfDress)
        dressName = view.findViewById(R.id.nameOfDress_input)
        dressDescription = view.findViewById(R.id.descriptionOfDress_edit)

        viewModel.loadDress()
        viewModel.photoUri = FileProvider.getUriForFile(requireActivity(),
            "com.str.wardrobe.file-provider",
            viewModel.photoFile)

        // Определение Spinner выбора единицы измерения напряжения
        categoryChooseSpin = view.findViewById(R.id.category_choose_spin)
        activity?.let {
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                viewModel.allCategoriesName.value ?: emptyList()
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                categoryChooseSpin.adapter = adapter
            }
        }
        // Create an ArrayAdapter using the string array and a default spinner layout
//        val adapter = ArrayAdapter(requireContext(),
//            android.R.layout.cat,
//            viewModel.allCategoriesName.value!!.toTypedArray()
//        )
//        categoryChooseSpin.adapter = adapter

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        // Определение EditText названия одежды
        dressName.doOnTextChanged { text, start, before, count ->
            try {
                if (text != null) {
                    viewModel.setDressName(text.toString())
                    viewModel.updateDB()
                } else {
                    dressName.error = null
                }

            } catch (_: FormatException) {

            }
        }

        // Определение EditText описания одежды
        dressDescription.doOnTextChanged { text, start, before, count ->
            try {
                if (text != null) {
                    viewModel.setDressDescription(text.toString())
                    viewModel.updateDB()
                } else {
                    dressName.error = null
                }

            } catch (_: FormatException) {

            }
        }

        // Определение слушателя для Spinner выбора категории
        categoryChooseSpin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                if (viewModel.currentDress != null) {
                    viewModel.currentDress!!.category =
                        categoryChooseSpin.adapter.getItem(pos).toString()
                    viewModel.updateDB()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        dressImage.apply {

//
            setOnClickListener {

                if (viewModel.currentDress != null) {
                    viewModel.goToPhoto()
                }

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        val randomId: Int = Random.nextInt(1, 100)
        viewModel.loadDress()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dress_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.add_dress -> {
                if (viewModel.currentDress!!.name == "") {
                    dressName.error
                } else {
                    if (viewModel.currentDress!!.description == "") {
                        dressDescription.error
                    } else {
                        viewModel.saveDress()
                    }
                }
                true
            }
            R.id.cancel_dress -> {
                viewModel.closeWithoutSaveDress()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updatePhotoView() {
        if (viewModel.photoFile.exists()) {
            val bitmap = getScaledBitmap(viewModel.photoFile.path, requireActivity())
            dressImage.setImageBitmap(bitmap)
        } else {
            dressImage.setImageResource(R.drawable.empty_photo)
        }
    }



    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        updatePhotoView()
        super.onResume()
    }


}