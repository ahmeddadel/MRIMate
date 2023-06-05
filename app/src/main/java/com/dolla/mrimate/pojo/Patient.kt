package com.dolla.mrimate.pojo

import kotlinx.serialization.Serializable

/**
 * @created 23/01/2023 - 6:37 PM
 * @project MRIMate
 * @author adell
 */

@Serializable
data class Patient(
    val email: String,
    val name: String,
    val phone: String,
    val password: String,
    var scans: List<Scan> = emptyList(),
    var gender: Gender = Gender.UNKNOWN,
)