package com.example.proyectomoviles.viewmodel

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.proyectomoviles.model.FakeDatabase
import com.example.proyectomoviles.model.Usuario

class AuthViewModel : ViewModel() {

    var mensaje = mutableStateOf("")
    var usuarioActual = mutableStateOf<String?>(null)

    private fun validarEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
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

    fun registrar(nombre: String, email: String, password: String, rut: String): Boolean {
        if (nombre.isBlank()) {
            mensaje.value = "Falta el nombre"
            return false
        }
        if (email.isBlank()) {
            mensaje.value = "Falta el email"
            return false
        }
        if (password.isBlank()) {
            mensaje.value = "Falta la contraseña"
            return false
        }
        if (rut.isBlank()) {
            mensaje.value = "Falta el RUT"
            return false
        }

        if (!validarEmail(email)) {
            mensaje.value = "Email inválido"
            return false
        }
        if (!validarRut(rut)) {
            mensaje.value = "RUT inválido"
            return false
        }

        val nuevo = Usuario(nombre, email, password, rut)
        return if (FakeDatabase.registrar(nuevo)) {
            mensaje.value = "Registro exitoso"
            true
        } else {
            mensaje.value = "El usuario o RUT ya existe"
            false
        }
    }

    fun login(email: String, password: String): Boolean {
        if (email.isBlank()) {
            mensaje.value = "Falta el email"
            return false
        }
        if (password.isBlank()) {
            mensaje.value = "Falta la contraseña"
            return false
        }

        if (!validarEmail(email)) {
            mensaje.value = "Email inválido"
            return false
        }
        
        return if (FakeDatabase.login(email, password)) {
            usuarioActual.value = email
            mensaje.value = "Inicio de sesión exitoso"
            true
        } else {
            mensaje.value = "Credenciales inválidas"
            false
        }
    }
}