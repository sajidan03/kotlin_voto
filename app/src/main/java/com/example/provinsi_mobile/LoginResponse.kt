package com.example.provinsi_mobile

data class LoginResponse(
    val status : String,
    val users: Users?
)
data class Users(
    val id_user : Int,
    val nama : String,
    val username: String
)
