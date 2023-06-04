package com.dolla.mrimate.repo

import com.dolla.mrimate.model.User
import com.dolla.mrimate.util.*
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * @created 03/01/2023 - 10:14 PM
 * @project MRIMate
 * @author adell
 */

object FirebaseCloudManager {
    // save the doctor details to the firestore
    fun saveUserDetailsToFirestore(
        newUser: User
    ): Task<Void> {

        // get the user data
        val userData = when (newUser) {
            is User.Doctor -> hashMapOf(
                USER_EMAIL to newUser.email,
                USER_NAME to newUser.name,
                USER_PHONE to newUser.phone,
                USER_HOSPITAL_ID to newUser.hospitalId
            )
            is User.Patient -> hashMapOf(
                USER_EMAIL to newUser.email,
                USER_NAME to newUser.name,
                USER_PHONE to newUser.phone,
            )
        }

        // get the collection name
        val collection = when (newUser) {
            is User.Doctor -> DOCTORS_COLLECTION
            is User.Patient -> PATIENTS_COLLECTION
        }

        // get the user id
        val userId = when (newUser) {
            is User.Doctor -> newUser.userId
            is User.Patient -> newUser.userId
        }

        // save the user id to the firestore
        saveUserIdToFirestore(userId, newUser is User.Doctor)

        // save the user details to the firestore
        return FirebaseFirestore.getInstance().collection(collection)
            .document(userId).set(userData)
    }

    // save the user's id to the firestore whether the user is a doctor or a patient
    private fun saveUserIdToFirestore(
        userId: String,
        isDoctor: Boolean
    ) {
        val userData = hashMapOf(
            "isDoctor" to isDoctor
        )
        FirebaseFirestore.getInstance().collection(USERS_COLLECTION)
            .document(userId).set(userData)
    }

    // get the user type from the firestore whether the user is a doctor or a patient
    suspend fun getUserTypeFromFirestore(
        userId: String
    ): Boolean? {
        return FirebaseFirestore.getInstance().collection(USERS_COLLECTION)
            .document(userId).get().await().getBoolean("isDoctor")
    }
}