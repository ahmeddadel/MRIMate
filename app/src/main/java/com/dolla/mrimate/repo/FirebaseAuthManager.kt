package com.dolla.mrimate.repo

import com.dolla.mrimate.model.User
import com.dolla.mrimate.util.Resource
import com.dolla.mrimate.util.USER_DOES_NOT_EXIST_MESSAGE
import com.dolla.mrimate.util.VERIFICATION_REQUIRED_MESSAGE
import com.dolla.mrimate.util.safeCall
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * @created 03/01/2023 - 10:00 PM
 * @project MRIMate
 * @author adell
 */

object FirebaseAuthManager {
    // firebase auth instance
    private val firebaseAuth = FirebaseAuth.getInstance()

    // function to sign up the user with email and password using firebase auth
    suspend fun createUser(
        email: String,
        name: String,
        phone: String,
        password: String,
        isDoctor: Boolean = false,
        hospitalId: String = ""
    ): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                // register the user with email and password
                val registrationResult =
                    firebaseAuth.createUserWithEmailAndPassword(email, password).await()

                // send the verification email to the user
                registrationResult.user?.sendEmailVerification()?.await()

                // get the user id
                val userId = registrationResult.user?.uid!!

                // save the user details to the firestore
                val user = if (isDoctor) {
                    User.Doctor(userId, email, name, phone, hospitalId)
                } else {
                    User.Patient(userId, email, name, phone)
                }
                FirebaseCloudManager.saveUserDetailsToFirestore(user).await()

                // return the registration result
                Resource.Success(registrationResult)
            }
        }
    }

    // function to sign in the user with email and password using firebase auth
    suspend fun signIn(email: String, password: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                // sign in the user
                val signInResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()

                // check if the user is verified
                val verified = signInResult.user?.isEmailVerified!!

                if (verified) {
                    Resource.Success(signInResult)
                } else {
                    Resource.Error(VERIFICATION_REQUIRED_MESSAGE)
                }
            }
        }
    }

    // function to reset the user's password
    suspend fun resetPassword(email: String): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            safeCall {
                // check if the user exists
                val userExists =
                    firebaseAuth.fetchSignInMethodsForEmail(email).await().signInMethods
                if (userExists.isNullOrEmpty()) {
                    return@safeCall Resource.Error(USER_DOES_NOT_EXIST_MESSAGE)
                }
                // send the password reset email
                firebaseAuth.sendPasswordResetEmail(email).await()

                // return the result
                Resource.Success(Unit)
            }
        }
    }
}