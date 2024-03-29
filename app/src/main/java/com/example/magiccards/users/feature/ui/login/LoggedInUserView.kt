package com.example.magiccards.users.feature.ui.login

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val displayName: String,
    val token: String
    //... other data fields that may be accessible to the UI
)