package com.dolla.mrimate.pojo

import kotlinx.serialization.Serializable

/**
 * @created 04/06/2023 - 9:22 PM
 * @project MRIMate
 * @author adell
 */

@Serializable
data class Doctor(
    val email: String,
    val name: String,
    val phone: String,
    val password: String,
    val hospitalId: String,
    var gender: Gender = Gender.UNKNOWN
)