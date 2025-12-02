package com.example.proyectomoviles.remote

import com.example.proyectomoviles.model.Usuario
import kotlinx.coroutines.delay
import retrofit2.Response
import java.util.concurrent.ConcurrentHashMap

class FakeUserAPIService : UserAPIService {

    private val users = ConcurrentHashMap<String, Usuario>()

    override suspend fun login(request: LoginRequest): Response<Usuario> {
        delay(500) // Simular retardo de red
        val user = users[request.email]

        return if (user != null && user.password == request.password) {
            Response.success(user)
        } else {
            Response.error(401, okhttp3.ResponseBody.create(null, "Unauthorized"))
        }
    }

    override suspend fun register(request: RegisterRequest): Response<Usuario> {
        delay(500) // Simular retardo de red
        if (users.containsKey(request.email)) {
            return Response.error(409, okhttp3.ResponseBody.create(null, "Conflict"))
        }

        val newUser = Usuario(
            nombre = request.nombre,
            email = request.email,
            password = request.password, // En una app real, hashea la contraseña
            rut = request.rut
        )
        users[request.email] = newUser
        return Response.success(newUser)
    }
}
