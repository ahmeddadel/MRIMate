package com.dolla.mrimate.model

/**
 * @created 23/01/2023 - 6:37 PM
 * @project MRIMate
 * @author adell
 */
sealed class User {
    data class Doctor(
        val userId: String,
        val email: String,
        val name: String,
        val phone: String,
        val hospitalId: String,
        var gender: Gender = Gender.UNKNOWN
    ) : User()

    data class Patient(
        val userId: String,
        val email: String,
        val name: String,
        val phone: String,
        var gender: Gender = Gender.UNKNOWN
    ) : User()
}
