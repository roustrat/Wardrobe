package com.str.wardrobe.simpleMVVM.views.dressinfoEditable

import android.content.DialogInterface
import android.graphics.Bitmap
import android.net.Uri
import android.nfc.FormatException
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.doOnTextChanged
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.str.foundation.views.BaseFragment
import com.str.foundation.views.BaseScreen
import com.str.foundation.views.screenViewModel
import com.str.wardrobe.R
import com.str.wardrobe.simpleMVVM.MainActivity
import com.str.wardrobe.simpleMVVM.model.entities.NamedCategory
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

// Позже надо перейти к единому фрагменту и для просмотра, и для редактирования. Сам dress сделав тут LiveData и реализовывая как в книге

class DressInfoEditableFragment : BaseFragment() {


    class Screen (
        val nameDressCategory: NamedCategory
    ): BaseScreen

    override val viewModel by screenViewModel<DressInfoEditableViewModel>()

    private lateinit var dressImage: ImageView
    private lateinit var dressName: TextInputEditText
    private lateinit var categoryChooseSpin: AutoCompleteTextView
    private lateinit var dressDescription: EditText
    private  var dialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel.allCategoriesName.observe(viewLifecycleOwner) {
            if (it != null) {
                categoryChooseSpin.setAdapter(ArrayAdapter(requireContext(), R.layout.dropdown_item,
                    viewModel.allCategoriesName.value ?: emptyList()
                ))
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

        // Определение Spinner выбора категории
        categoryChooseSpin = view.findViewById(R.id.category_choose_spin)
        activity?.let {
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_item,
                viewModel.allCategoriesName.value ?: emptyList()
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(R.layout.dropdown_item)
                // Apply the adapter to the spinner
                categoryChooseSpin.setAdapter(adapter)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
//                viewModel.deleteDress()
//                requireActivity().onBackPressed()
                viewModel.closeWithoutSaveDress()
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        // Определение EditText названия одежды
        dressName.doOnTextChanged { text, _, _, _ ->
            try {
                if (text != null) {
                    viewModel.setDressName(text.toString())
                    viewModel.updateDB()
                    dressName.error = null
                } else {
                    dressName.error = "Заполните поле"
                }

            } catch (_: FormatException) {

            }
        }

        // Определение EditText описания одежды
        dressDescription.doOnTextChanged { text, _, _, _ ->
            try {
                if (text != null) {
                    viewModel.setDressDescription(text.toString())
                    viewModel.updateDB()
                    dressDescription.error = null
                } else {
                    dressDescription.error = "Заполните поле"
                }

            } catch (_: FormatException) {

            }
        }

        // Определение слушателя для Spinner выбора категории
        categoryChooseSpin.setOnItemClickListener { parent, _, position, _ ->
            if (viewModel.currentDress != null) {
                    viewModel.currentDress!!.category =
                        parent.getItemAtPosition(position).toString()
                    viewModel.updateDB()
                }
        }

        dressImage.apply {
            setOnClickListener {
               showAlertDialog()
            }
        }

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
                menuInflater.inflate(R.menu.menu_dress_fragment, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.add_dress -> {
                        if (dressName.error == null && dressDescription.error == null) {
                            if (!viewModel.currentDress?.name.equals("") &&
                                !viewModel.currentDress?.description.equals("") &&
                                !viewModel.currentDress?.category.equals("")) {

                                viewModel.saveDress()

                            }
                        }
                        true
                    }
                    R.id.cancel_dress -> {
                        viewModel.closeWithoutSaveDress()
                        true
                    }
                    else -> {
                        viewModel.closeWithoutSaveDress()
                        false
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.loadDress()
        super.onCreate(savedInstanceState)
    }

    private fun updatePhotoView() {
        if (viewModel.photoFile.exists()) {
            lifecycleScope.launch {
                val compressedPhotoFile =
                    Compressor.compress(requireContext(), viewModel.photoFile) {
                        resolution(1280, 720)
                        quality(80)
                        format(Bitmap.CompressFormat.WEBP)
                        size(2_097_152) // 2 MB
                        destination(viewModel.photoFile)
                    }
                dressImage.setImageURI(compressedPhotoFile.toUri())
            }

        } else {
            dressImage.setImageResource(R.drawable.empty_photo)
        }
    }

    override fun onPause() {
        dialog = null
        super.onPause()
    }

    override fun onResume() {
        Log.d("Resume", "Resume")
        updatePhotoView()
        super.onResume()
    }

    private fun showAlertDialog() {
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_NEGATIVE -> {
                    selectImageFromGallery()
                }
                DialogInterface.BUTTON_NEUTRAL -> {}
                DialogInterface.BUTTON_POSITIVE -> viewModel.useCameraX()
            }
        }

        dialog = AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setIcon(R.mipmap.ic_launcher_round)
            .setTitle(R.string.dress_photo_alert_title)
            .setMessage(R.string.dress_photo_alert_message)
            .setPositiveButton(R.string.dress_photo_action_yes, listener)
            .setNegativeButton(R.string.dress_photo_action_no, listener)
            .setNeutralButton(R.string.dress_photo_action_ignore, listener)
//            .setOnCancelListener {
//
//            }
//            .setOnDismissListener {
//
//            }
            .create()

        dialog!!.show()
    }


//    private fun useGallery() {
////        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
////        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)
//        //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
//        val photoPickerIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//        //Тип получаемых объектов - image:
////        photoPickerIntent.type = "image/*"
//        //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
//        resultLauncher.launch(photoPickerIntent)
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
//            val fileUri = data.data
//            dressImage.setImageURI(fileUri)
////            val savedUri = Uri.fromFile(viewModel.photoFile)
////            val iStream : InputStream =
////                requireActivity().contentResolver.openInputStream(fileUri!!)!!
////            copyStreamToFile(iStream, viewModel.photoFile)
////            iStream.close()
//        }
//    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

//    private var resultLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK ) {
//            // There are no request codes
//
//            val fileUri = result.data?.data
//            dressImage.setImageURI(fileUri)
////            val savedUri = Uri.fromFile(viewModel.photoFile)
////            val iStream : InputStream =
////                requireActivity().contentResolver.openInputStream(fileUri!!)!!
////            copyStreamToFile(iStream, viewModel.photoFile)
////            iStream.close()
//
//        }
//    }
//
private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
    uri?.let {
        val iStream : InputStream =
            requireActivity().contentResolver.openInputStream(uri)!!
        copyStreamToFile(iStream, viewModel.photoFile)
        iStream.close()
    }
//
    }
//
private fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }
    }




//
//    private fun showToast(@StringRes messageRes: Int) {
//        Toast.makeText(requireContext(), messageRes, Toast.LENGTH_SHORT).show()
//    }

}