package com.example.proyectomoviles.utils

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_TOKEN = "jwt_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_RUT = "user_rut"
    private const val KEY_USER_TELEFONO = "user_telefono"
    private const val KEY_USER_DIRECCION = "user_direccion"

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveAuthInfo(token: String, userId: Int, name: String, email: String, rut: String, telefono: String?, direccion: String?) {
        preferences.edit().apply {
            putString(KEY_TOKEN, token)
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_RUT, rut)
            putString(KEY_USER_TELEFONO, telefono)
            putString(KEY_USER_DIRECCION, direccion)
            apply()
        }
    }

    fun getToken(): String? = preferences.getString(KEY_TOKEN, null)
    fun getUserId(): Int = preferences.getInt(KEY_USER_ID, -1)
    fun getUserName(): String? = preferences.getString(KEY_USER_NAME, null)
    fun getUserEmail(): String? = preferences.getString(KEY_USER_EMAIL, null)
    fun getUserRut(): String? = preferences.getString(KEY_USER_RUT, null)
    fun getUserTelefono(): String? = preferences.getString(KEY_USER_TELEFONO, null)
    fun getUserDireccion(): String? = preferences.getString(KEY_USER_DIRECCION, null)

    fun isLoggedIn(): Boolean = getToken() != null

    fun clear() {
        preferences.edit().clear().apply()
    }
}
