package com.str.wardrobe.simpleMVVM.views.dressinfoEditable

import android.Manifest
import android.content.pm.PackageManager
import android.nfc.FormatException
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import com.str.foundation.views.BaseFragment
import com.str.foundation.views.BaseScreen
import com.str.foundation.views.screenViewModel
import com.str.wardrobe.R
import com.str.wardrobe.simpleMVVM.model.entities.NamedCategory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.random.Random

// Позже надо перейти к единому фрагменту и для просмотра, и для редактирования. Сам dress сделав тут LiveData и реализовывая как в книге

//private const val REQUEST_PHOTO = 1

class DressInfoEditableFragment : BaseFragment() {


    class Screen (
        val nameDressCategory: NamedCategory
    ): BaseScreen

    override val viewModel by screenViewModel<DressInfoEditableViewModel>()

    override fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
                .also {
                    it.setSurfaceProvider()
                }
        },
        ContextCompat.getMainExecutor(requireContext()))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_CODE) {
            if (allPermissionGranted()) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "Permission error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private lateinit var cameraExecutor: ExecutorService

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

        viewModel.currentDress.observe(viewLifecycleOwner) { it ->
            it?.let {
                viewModel.photoFile = viewModel.getPhotoFile(it)
                viewModel.photoUri = FileProvider.getUriForFile(requireActivity(),
                    "com.str.wardrobe.file-provider",
                    viewModel.photoFile)
//                updatePhotoView()
            }
            Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
        }

        return inflater.inflate(R.layout.dress_info_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        dressImage = view.findViewById(R.id.imageOfDress)
        dressName = view.findViewById(R.id.nameOfDress_input)
        dressDescription = view.findViewById(R.id.descriptionOfDress_edit)

        val randomId: Int = Random.nextInt(1, 100)
        viewModel.loadDress(randomId)

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
                } else {
                    dressName.error = null
                }

            } catch (_: FormatException) {

            }
        }

        // Определение EditText названия одежды
        dressDescription.doOnTextChanged { text, start, before, count ->
            try {
                if (text != null) {
                    viewModel.setDressDescription(text.toString())
                } else {
                    dressName.error = null
                }

            } catch (_: FormatException) {

            }
        }

        // Определение слушателя для Spinner выбора категории
        categoryChooseSpin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                if (viewModel.currentDress.value != null) {
                    viewModel.currentDress.value!!.category =
                        categoryChooseSpin.adapter.getItem(pos).toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        dressImage.apply {


//            val packageManager: PackageManager = requireActivity().packageManager
//            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            val resolvedActivity: ResolveInfo? =
//                packageManager.resolveActivity(captureImage,
//                    PackageManager.MATCH_DEFAULT_ONLY)
//            if (resolvedActivity == null) {
//                isEnabled = false
//            }

            setOnClickListener {
                if (allPermissionGranted()) {
                    startCamera()
                } else {

                    // Запрос разрешения на использование. В данном случаи камеры
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        PERMISSION,
                        PERMISSION_CODE
                    )
                }

                cameraExecutor = Executors.newSingleThreadExecutor()

//                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, viewModel.photoUri)
//                val cameraActivities: List<ResolveInfo> =
//                    packageManager.queryIntentActivities(captureImage,
//                        PackageManager.MATCH_DEFAULT_ONLY)
//                for (cameraActivity in cameraActivities) {
//                    requireActivity().grantUriPermission(
//                        cameraActivity.activityInfo.packageName,
//                        viewModel.photoUri,
//                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//                }
//                takePicture.launch(viewModel.photoUri)
            }
        }
    }

//    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
//        if (success) {
//            // The image was saved into the given Uri -> do something with it
//            Log.d (TAG, "We took a picture...")
//            updatePhotoView()    // You'll need this later for listing 16.16
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        val randomId: Int = Random.nextInt(1, 100)
        viewModel.loadDress(randomId)
        super.onCreate(savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
        cameraExecutor.shutdown()
//        requireActivity().revokeUriPermission(viewModel.photoUri,
//            Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dress_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.add_dress -> {
                if (viewModel.currentDress.value!!.name == "") {
                    dressName.error
                } else {
                    if (viewModel.currentDress.value!!.description == "") {
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

//    private fun updatePhotoView() {
//        if (viewModel.photoFile.exists()) {
//            val bitmap = getScaledBitmap(viewModel.photoFile.path, requireActivity())
//            dressImage.setImageBitmap(bitmap)
//        } else {
//            dressImage.setImageDrawable(null)
//        }
//    }

    private fun allPermissionGranted() = PERMISSION.all {
        ContextCompat.checkSelfPermission(requireActivity().baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val PERMISSION_CODE = 10
        private val PERMISSION = arrayOf(Manifest.permission.CAMERA)
    }


}