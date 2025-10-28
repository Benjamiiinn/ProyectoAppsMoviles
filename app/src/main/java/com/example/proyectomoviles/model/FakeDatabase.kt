package com.example.proyectomoviles.model

import kotlin.collections.any

object FakeDatabase {
    private val usuarios = mutableListOf<Usuario>()

    fun registrar(usuario: Usuario): Boolean {
        if (usuarios.any { it.email == usuario.email || it.rut == usuario.rut }) return false
        usuarios.add(usuario)
        return true
    }

    fun login(email: String, password: String): Boolean {
        return usuarios.any { it.email == email && it.password == password }
    }
}