package com.dolla.mrimate.ui.fragments


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.dolla.mrimate.R
import com.dolla.mrimate.databinding.FragmentScanBinding
import com.dolla.mrimate.ml.Model
import com.dolla.mrimate.pojo.Scan
import com.dolla.mrimate.util.editPatientInFile
import com.dolla.mrimate.util.isDoctor
import com.dolla.mrimate.util.patientObj
import com.dolla.mrimate.util.serializeBitmap
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.ByteBuffer

class ScanFragment : Fragment() {

    private lateinit var binding: FragmentScanBinding // View binding
    private var applicationContext: Context? = null// Application context
    private var bitmap: Bitmap? = null // Bitmap

    override fun onCreate(savedInstanceState: Bundle?) { // This method is called when the fragment is first created
        super.onCreate(savedInstanceState)

        applicationContext =
            requireActivity().applicationContext // get application context from the activity that is attached to the fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // This method is called to have the fragment instantiate its user interface view

        // Inflate the layout for this fragment
        binding = FragmentScanBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) { // This method is called after onCreateView() when the fragment's view hierarchy is created and ready to be used
        super.onViewCreated(view, savedInstanceState)

        onBrainClicked() // Set the onClickListener for the brain button
        onSpineClicked() // Set the onClickListener for the spine button
        onLungsClicked() // Set the onClickListener for the lungs button
        onLiverClicked() // Set the onClickListener for the liver button
    }

    private fun onBrainClicked() { // TODO: the brain scan logic
        binding.ivBrain.setOnClickListener {
            if (isDoctor) {
                showToastMessage("Brain scan is not available for doctors")
            } else
                scan()
        }
    }

    private fun scan() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.ivResult.setImageURI(data?.data)

        try {
            bitmap =
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, data?.data)
            predict()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun predict() {
        bitmap?.let {

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val model: Model =
                        Model.newInstance(applicationContext!!) // Creates tflite model instance

                    // Creates inputs for reference.
                    val inputFeature0: TensorBuffer = TensorBuffer.createFixedSize(
                        intArrayOf(1, 150, 150, 3),
                        DataType.FLOAT32
                    ) // Creates input tensor of shape [1, 150, 150, 3]

                    val tensorImage = TensorImage(DataType.FLOAT32) // Creates tensor image

                    val resizedBitmap = Bitmap.createScaledBitmap(
                        bitmap!!,
                        150,
                        150,
                        true
                    ) // Resize the bitmap to 150x150
                    tensorImage.load(resizedBitmap) // Loads bitmap into a TensorImage

                    val byteBuffer: ByteBuffer =
                        tensorImage.buffer // Gets the TensorBuffer from the TensorImage

                    inputFeature0.loadBuffer(byteBuffer) // Copies tensor values into inputFeature0


                    // Runs model inference and gets result.
                    val outputs: Model.Outputs =
                        model.process(inputFeature0) // Runs model inference and gets result.
                    val outputFeature0: TensorBuffer =
                        outputs.outputFeature0AsTensorBuffer // Gets the output tensor

                    val resultIndex = argMax(outputFeature0.floatArray)[1].toInt()
                    var resultPercentage = argMax(outputFeature0.floatArray)[0] * 100
                    resultPercentage = BigDecimal(resultPercentage.toDouble()).setScale(
                        2,
                        RoundingMode.HALF_UP
                    ).toFloat()

                    val result = when (resultIndex) {
                        0 -> "Glioma Tumor"
                        1 -> "No Tumor"
                        2 -> "Meningioma Tumor"
                        3 -> "Pituitary Tumor"
                        else -> "Unknown"
                    }

                    val scan =
                        Scan(resultIndex, result, resultPercentage, serializeBitmap(bitmap!!))
                    val scansList = ArrayList<Scan>()
                    scansList.addAll(patientObj!!.scans)
                    scansList.add(scan)
                    patientObj!!.scans = scansList

                    editPatientInFile(applicationContext!!, patientObj!!)

                    withContext(Dispatchers.Main) {
                        binding.tvResult.text = "$result\n$resultPercentage% Confidence"
                        binding.clScan.visibility = View.GONE
                        binding.llResult.visibility = View.VISIBLE
                        binding.btOk.setOnClickListener {
                            binding.clScan.visibility = View.VISIBLE
                            binding.llResult.visibility = View.GONE
                        }
                    }
                    // Releases model resources if no longer used.
                    model.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun argMax(array: FloatArray): FloatArray {
        var max = array[0]
        val re = FloatArray(2)
        for (i in 1 until array.size) {
            if (array[i] > max) {
                max = array[i]
                re[1] = i.toFloat()
            }
        }
        re[0] = max
        return re
    }

    private fun onSpineClicked() {
        binding.ivSpinal.setOnClickListener {
            showToastMessage("Spinal scan will be available soon")
        }
    }

    private fun onLungsClicked() {
        binding.ivLung.setOnClickListener {
            showToastMessage("Lungs scan will be available soon")
        }
    }

    private fun onLiverClicked() {
        binding.ivLiver.setOnClickListener {
            showToastMessage("Liver scan will be available soon")
        }
    }

    private fun showToastMessage(message: String) {
        Toasty.custom(
            applicationContext!!,
            message,
            R.drawable.ic_logo,
            R.color.colorPrimaryVariant,
            Toast.LENGTH_LONG,
            true,
            true
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext = null // Clear the application context to avoid memory leaks
    }
}