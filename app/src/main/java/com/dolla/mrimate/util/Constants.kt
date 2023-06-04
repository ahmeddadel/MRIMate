package com.dolla.mrimate.util

import com.dolla.mrimate.R

/**
 * @created 25/12/2022 - 1:09 AM
 * @project MRIMate
 * @author adell
 */

// Constants used throughout the app

const val SPLASH_SCREEN_DELAY: Long = 1500 // 1.5 seconds
const val ERROR_MESSAGE_DELAY: Long = 2000 // 2 seconds

const val PATIENTS_COLLECTION = "patients"
const val DOCTORS_COLLECTION = "doctors"
const val USERS_COLLECTION = "users"

// user details keys for cloud firestore
const val USER_ID = "userId"
const val USER_EMAIL = "email"
const val USER_NAME = "name"
const val USER_PHONE = "phone"
const val USER_HOSPITAL_ID = "hospitalId"

// Authentication error messages
const val EMPTY_EMAIL_ERROR = "Email is required"
const val INVALID_EMAIL_ERROR = "Please enter a valid email"
const val EMPTY_NAME_ERROR = "Name is required"
const val EMPTY_PHONE_NUMBER_ERROR = "Phone number is required"
const val INVALID_PHONE_NUMBER_ERROR = "Please enter a valid phone number"
const val EMPTY_PASSWORD_ERROR = "Password is required"
const val PASSWORD_LENGTH_ERROR = "Password must be at least 6 characters"
const val INVALID_PASSWORD_ERROR = "Please enter a valid password"
const val EMPTY_HOSPITAL_ID_ERROR = "Hospital ID is required"

// Toast messages
const val INVALID_PASSWORD_MESSAGE = "Password must contain 1 uppercase, 1 lowercase and 1 number"
const val VERIFICATION_EMAIL_SENT_MESSAGE = "Verification email has been sent to your email"
const val VERIFICATION_REQUIRED_MESSAGE = "Please verify your email"
const val PASSWORD_RESET_MESSAGE = "Password reset link has been sent to your email"
const val SIGN_IN_SUCCESS_MESSAGE = "Sign in successful"
const val USER_DOES_NOT_EXIST_MESSAGE = "User does not exist"

const val UNKNOWN_ERROR_MESSAGE = "An unknown error occurred"

// Regex for password validation
val PASSWORD_REGEX = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9!@#\$%^&*+=_]{6,}$")


// Onboarding images
val onboardingImages = arrayOf(
    R.drawable.onboarding_image1,
    R.drawable.onboarding_image2,
    R.drawable.onboarding_image3
)

// Onboarding titles
val onboardingTitles = arrayOf(
    R.string.onboarding_title1,
    R.string.onboarding_title2,
    R.string.onboarding_title3
)

// Onboarding bodies
val onboardingBodies = arrayOf(
    R.string.onboarding_body1,
    R.string.onboarding_body2,
    R.string.onboarding_body3
)