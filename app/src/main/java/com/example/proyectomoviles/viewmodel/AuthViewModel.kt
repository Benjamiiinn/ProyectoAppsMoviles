package com.example.proyectomoviles.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomoviles.remote.AuthAPIService
import com.example.proyectomoviles.remote.LoginRequest
import com.example.proyectomoviles.remote.RegisterRequest
import com.example.proyectomoviles.remote.RetrofitClient
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.regex.Pattern

class AuthViewModel(private val apiService: AuthAPIService = RetrofitClient.instance.create(AuthAPIService::class.java)) : ViewModel() {

    var mensaje = mutableStateOf("")
    var usuarioActual = mutableStateOf<String?>(null)
    var isLoading = mutableStateOf(false)

    private fun validarEmail(email: String): Boolean {
        val emailRegex = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
        )
        return emailRegex.matcher(email).matches()
    }

    fun isAdmin(): Boolean {
        return usuarioActual.value == "admin@tienda.com"
    }

    fun validarRut(rut: String): Boolean {
        try {
            var rutLimpio = rut.toUpperCase().replace(".", "").replace("-", "")
            if (rutLimpio.length < 2) return false
            val dv = rutLimpio.last()
            val cuerpo = rutLimpio.substring(0, rutLimpio.length - 1)

            if (!cuerpo.matches(Regex("[0-9]+"))) return false

            var suma = 0
            var multiplo = 2
            for (i in cuerpo.length - 1 downTo 0) {
                suma += cuerpo[i].toString().toInt() * multiplo
                multiplo++
                if (multiplo > 7) {
                    multiplo = 2
                }
            }

            val resto = suma % 11
            val dvCalculado = 11 - resto

            val dvFinal = when (dvCalculado) {
                11 -> '0'
                10 -> 'K'
                else -> dvCalculado.toString().first()
            }

            return dv == dvFinal
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * Valida que la contraseña tenga al menos 8 caracteres, una mayúscula y un número.
     */
    private fun validarPassword(password: String): Pair<Boolean, String> {
        if (password.length < 8) {
            return Pair(false, "La contraseña debe tener al menos 8 caracteres.")
        }
        if (!password.any { it.isUpperCase() }) {
            return Pair(false, "La contraseña debe contener al menos una mayúscula.")
        }
        if (!password.any { it.isDigit() }) {
            return Pair(false, "La contraseña debe contener al menos un número.")
        }
        return Pair(true, "")
    }

    fun registrar(nombre: String, email: String, password: String, rut: String, onResult: (Boolean) -> Unit) {
        if (nombre.isBlank() || email.isBlank() || password.isBlank() || rut.isBlank()) {
            mensaje.value = "Todos los campos son obligatorios"
            onResult(false)
            return
        }
        if (!validarEmail(email)) {
            mensaje.value = "Email inválido"
            onResult(false)
            return
        }
        if (!validarRut(rut)) {
            mensaje.value = "RUT inválido"
            onResult(false)
            return
        }

        val (esPasswordValido, mensajeErrorPassword) = validarPassword(password)
        if (!esPasswordValido) {
            mensaje.value = mensajeErrorPassword
            onResult(false)
            return
        }

        isLoading.value = true
        mensaje.value = ""

        viewModelScope.launch {
            try {
                val request = RegisterRequest(nombre, email, password, rut)
                val response = apiService.registrar(request)

                if (response.isSuccessful) {
                    mensaje.value = "Registro exitoso"
                    onResult(true)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    mensaje.value = "Error en el registro: $errorBody"
                    onResult(false)
                }
            } catch (e: IOException) {
                mensaje.value = "Error de conexión. Inténtalo de nuevo."
                onResult(false)
            } catch (e: Exception) {
                mensaje.value = "Ocurrió un error inesperado."
                onResult(false)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
         if (email.isBlank() || password.isBlank()) {
            mensaje.value = "Email y contraseña son obligatorios"
            onResult(false)
            return
        }
        if (!validarEmail(email)) {
            mensaje.value = "Formato de email inválido"
            onResult(false)
            return
        }

        isLoading.value = true
        mensaje.value = ""

        viewModelScope.launch {
            try {
                val request = LoginRequest(email, password)
                val response = apiService.login(request)

                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!
                    usuarioActual.value = authResponse.user.email
                    mensaje.value = "Inicio de sesión exitoso"
                    onResult(true)
                } else {
                    mensaje.value = "Credenciales inválidas"
                    onResult(false)
                }
            } catch (e: IOException) {
                mensaje.value = "Error de conexión. Revisa tu conexión a internet."
                onResult(false)
            } catch (e: Exception) {
                mensaje.value = "Ocurrió un error inesperado."
                onResult(false)
            } finally {
                isLoading.value = false
            }
        }
    }
}
