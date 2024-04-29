package com.example.testtest

import java.io.Serializable

data class Member(
    val id: Long,
    val name: String,
    val age: Int,
    val mobile: String
) : Serializable