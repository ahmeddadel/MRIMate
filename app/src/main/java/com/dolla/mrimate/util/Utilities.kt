package com.dolla.mrimate.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import com.dolla.mrimate.pojo.Doctor
import com.dolla.mrimate.pojo.Patient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * @created 30/12/2022 - 1:26 AM
 * @project MRIMate
 * @author adell
 */

fun customDelay(delay: Long, block: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        block()
    }, delay)
}

// ################# Keyboards Extensions #################
fun Activity.hideKeyboard() { // hide the keyboard
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.showKeyboard() { // show the keyboard
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }
}

fun Patient.appendToFile(context: Context) {
    val patients: MutableList<Patient> = try {
        val file = File(context.getExternalFilesDir(null), PATIENTS_FILE_NAME)
        if (file.exists()) {
            val jsonString = file.readText()
            if (jsonString.isNotEmpty()) {
                Json.decodeFromString<List<Patient>>(jsonString).toMutableList()
            } else {
                mutableListOf()
            }
        } else {
            mutableListOf()
        }
    } catch (ex: Exception) {
        mutableListOf()
    }

    patients.add(this)
    val json = Json.encodeToString(patients)

    val file = File(context.getExternalFilesDir(null), PATIENTS_FILE_NAME)
    file.writeText(json)
}

fun readPatientsFromFile(context: Context): List<Patient> {
    val file = File(context.getExternalFilesDir(null), PATIENTS_FILE_NAME)
    return try {
        if (file.exists()) {
            val jsonString = file.readText()
            if (jsonString.isNotEmpty()) {
                Json.decodeFromString(jsonString)
            } else {
                emptyList()
            }
        } else {
            emptyList()
        }
    } catch (ex: Exception) {
        emptyList()
    }
}

fun editPatientInFile(context: Context, patient: Patient) {
    val patients: MutableList<Patient> = try {
        val file = File(context.getExternalFilesDir(null), PATIENTS_FILE_NAME)
        if (file.exists()) {
            val jsonString = file.readText()
            if (jsonString.isNotEmpty()) {
                Json.decodeFromString<List<Patient>>(jsonString).toMutableList()
            } else {
                mutableListOf()
            }
        } else {
            mutableListOf()
        }
    } catch (ex: Exception) {
        mutableListOf()
    }

    val index = patients.indexOfFirst { it.email == patient.email }
    patients[index] = patient
    val json = Json.encodeToString(patients)

    val file = File(context.getExternalFilesDir(null), PATIENTS_FILE_NAME)
    file.writeText(json)
}

fun Doctor.appendToFile(context: Context) {
    val doctors: MutableList<Doctor> = try {
        val file = File(context.getExternalFilesDir(null), DOCTORS_FILE_NAME)
        if (file.exists()) {
            val jsonString = file.readText()
            if (jsonString.isNotEmpty()) {
                Json.decodeFromString<List<Doctor>>(jsonString).toMutableList()
            } else {
                mutableListOf()
            }
        } else {
            mutableListOf()
        }
    } catch (ex: Exception) {
        mutableListOf()
    }

    doctors.add(this)
    val json = Json.encodeToString(doctors)

    val file = File(context.getExternalFilesDir(null), DOCTORS_FILE_NAME)
    file.writeText(json)
}

fun readDoctorsFromFile(context: Context): List<Doctor> {
    val file = File(context.getExternalFilesDir(null), DOCTORS_FILE_NAME)
    return try {
        if (file.exists()) {
            val jsonString = file.readText()
            if (jsonString.isNotEmpty()) {
                Json.decodeFromString(jsonString)
            } else {
                emptyList()
            }
        } else {
            emptyList()
        }
    } catch (ex: Exception) {
        emptyList()
    }
}

fun editDoctorInFile(context: Context, doctor: Doctor) {
    val doctors: MutableList<Doctor> = try {
        val file = File(context.getExternalFilesDir(null), DOCTORS_FILE_NAME)
        if (file.exists()) {
            val jsonString = file.readText()
            if (jsonString.isNotEmpty()) {
                Json.decodeFromString<List<Doctor>>(jsonString).toMutableList()
            } else {
                mutableListOf()
            }
        } else {
            mutableListOf()
        }
    } catch (ex: Exception) {
        mutableListOf()
    }

    val index = doctors.indexOfFirst { it.email == doctor.email }
    doctors[index] = doctor
    val json = Json.encodeToString(doctors)

    val file = File(context.getExternalFilesDir(null), DOCTORS_FILE_NAME)
    file.writeText(json)
}

// Serialize a Bitmap object
fun serializeBitmap(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

// Deserialize a Bitmap object
fun deserializeBitmap(byteArray: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

fun getCurrentDateAsString(): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val currentDate = Date()
    return dateFormat.format(currentDate)
}