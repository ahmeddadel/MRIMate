package com.dolla.mrimate.ui.activities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.preference.PreferenceManager
import com.dolla.mrimate.databinding.ActivityRegistrationBinding
import com.dolla.mrimate.ml.Model
import com.dolla.mrimate.pojo.Doctor
import com.dolla.mrimate.pojo.Patient
import com.dolla.mrimate.pojo.Scan
import com.dolla.mrimate.util.appendToFile
import com.dolla.mrimate.util.serializeBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.ByteBuffer
import kotlin.random.Random


class RegistrationActivity : BaseActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set the content view of the activity to the root of the layout
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!getScriptState(applicationContext))
            Doctor(
                "ahmed.ahmed@gmail.com",
                "Ahmed Ali Ahmed",
                "01012345678",
                "5Dg8H7fT4",
                "188928"
            ).appendToFile(applicationContext)
        Doctor("example@example.com", "John Doe", "0123456789", "Abc123456", "987654").appendToFile(
            applicationContext
        )
        Doctor("test@test.com", "Jane Smith", "9876543210", "Pass123456", "456789").appendToFile(
            applicationContext
        )

        val listOfPatients = arrayListOf(
            Patient("ahmed.ali@example.com", "Ahmed Ali", "01012345678", "5Dg8H7fT4"),
            Patient("fatima.hassan@example.com", "Fatima Hassan", "01123456789", "3Rj9M2aK6"),
            Patient("mohammad.khalid@example.com", "Mohammad Khalid", "01098765432", "1Bc5F6rT9"),
            Patient("aisha.abdullah@example.com", "Aisha Abdullah", "01187654321", "7Xq3P9zW2"),
            Patient("omar.ahmed@example.com", "Omar Ahmed", "01045678901", "8Ns6L1fP7"),
            Patient("layla.ibrahim@example.com", "Layla Ibrahim", "01176543210", "4Tm2N5jQ9"),
            Patient("ali.hassan@example.com", "Ali Hassan", "01023456789", "6Cf8V1dS5"),
            Patient("mariam.salem@example.com", "Mariam Salem", "01134567890", "2Gh4B9xY7"),
            Patient("hamza.mahmoud@example.com", "Hamza Mahmoud", "01087654321", "9Kv2S4pZ6"),
            Patient("noor.ahmad@example.com", "Noor Ahmad", "01165432109", "0Rn3M8tL1"),
            Patient("mustafa.hassan@example.com", "Mustafa Hassan", "01056789012", "3Jl9C5eQ7"),
            Patient("sarah.khalifa@example.com", "Sarah Khalifa", "01143210987", "6Vw1N8rT4"),
            Patient("yasmin.abidi@example.com", "Yasmin Abidi", "01034567890", "4Mj6F9rT2"),
            Patient("amira.ali@example.com", "Amira Ali", "01132109876", "5Xv3K1sH9"),
            Patient("karim.hassan@example.com", "Karim Hassan", "01067890123", "7Qd5N1rM9")
        )

        predictScript(listOfPatients)
        saveScriptState(applicationContext, true)
    }

    private fun predictScript(listOfPatients: ArrayList<Patient>) {

        GlobalScope.launch(Dispatchers.IO) {

            for (patient in listOfPatients) {
                val scansList = ArrayList<Scan>()
                for (i in 0..Random.nextInt(0, 3)) {

                    BitmapFactory.decodeStream(assets.open("image${Random.nextInt(1, 61)}.jpg"))
                        ?.let {
                            try {
                                val model: Model =
                                    Model.newInstance(applicationContext!!) // Creates tflite model instance

                                // Creates inputs for reference.
                                val inputFeature0: TensorBuffer = TensorBuffer.createFixedSize(
                                    intArrayOf(1, 150, 150, 3),
                                    DataType.FLOAT32
                                ) // Creates input tensor of shape [1, 150, 150, 3]

                                val tensorImage =
                                    TensorImage(DataType.FLOAT32) // Creates tensor image

                                val resizedBitmap = Bitmap.createScaledBitmap(
                                    it,
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
                                    Scan(resultIndex, result, resultPercentage, serializeBitmap(it))

                                if (Random.nextInt(1, 101) % 2 == 0) {
                                    val report = when (resultIndex) {
                                        0 -> "Further examination and treatment are recommended for Glioma Tumor."
                                        1 -> "No abnormalities have been found. Patient is healthy."
                                        2 -> "Further evaluation and consultation are advised for Meningioma Tumor."
                                        3 -> "Immediate medical attention is necessary for Pituitary Tumor."
                                        else -> "Further investigation is required to determine the condition."
                                    }
                                    scan.report = report
                                }

                                scansList.add(scan)
                                model.close() // Releases model resources if no longer used.
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                }
                patient.scans = scansList
                patient.appendToFile(applicationContext)
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

    private fun saveScriptState(context: Context, signedIn: Boolean) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putBoolean("script", signedIn)
        editor.apply()
    }

    private fun getScriptState(context: Context): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getBoolean("script", false)
    }
}