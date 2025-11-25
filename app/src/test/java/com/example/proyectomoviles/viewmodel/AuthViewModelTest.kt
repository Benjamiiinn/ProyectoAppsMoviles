package com.example.proyectomoviles.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.proyectomoviles.model.Usuario
import com.example.proyectomoviles.remote.AuthAPIService
import com.example.proyectomoviles.remote.AuthResponse
import com.example.proyectomoviles.remote.LoginRequest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AuthViewModel
    private lateinit var mockAuthApiService: AuthAPIService

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        // Para probar la lógica de red, necesitamos inyectar el servicio mockeado en el ViewModel.
        // Esto requiere modificar un poco el ViewModel para permitir la inyección, o usar una librería de inyección de dependencias.
        // Por simplicidad aquí, asumiremos que podríamos inyectarlo (la implementación real se haría con Hilt o Koin).
        mockAuthApiService = mockk()
        viewModel = AuthViewModel()

        // Esto es una reflexión para "inyectar" el mock en el viewModel. No es ideal, pero sirve para la prueba.
        val apiServiceField = viewModel.javaClass.getDeclaredField("apiService")
        apiServiceField.isAccessible = true
        apiServiceField.set(viewModel, mockAuthApiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- PRUEBAS DE VALIDACIÓN LOCAL ---

    @Test
    fun `registrar - con nombre vacío - debe establecer mensaje de error`() {
        viewModel.registrar("", "test@test.com", "123456", "12.345.678-9") { /* no-op */ }
        assertEquals("Todos los campos son obligatorios", viewModel.mensaje.value)
    }

    @Test
    fun `registrar - con email inválido - debe establecer mensaje de error`() {
        viewModel.registrar("Test", "email-invalido", "123456", "12.345.678-9") { /* no-op */ }
        assertEquals("Email inválido", viewModel.mensaje.value)
    }

    @Test
    fun `registrar - con RUT inválido - debe establecer mensaje de error`() {
        viewModel.registrar("Test", "test@test.com", "123456", "rut-invalido") { /* no-op */ }
        assertEquals("RUT inválido", viewModel.mensaje.value)
    }

    @Test
    fun `isAdmin - cuando el usuario actual es admin - debe devolver true`() {
        viewModel.usuarioActual.value = "admin@tienda.com"
        assertTrue(viewModel.isAdmin())
    }

    @Test
    fun `isAdmin - cuando el usuario actual no es admin - debe devolver false`() {
        viewModel.usuarioActual.value = "usuario@comun.com"
        assertFalse(viewModel.isAdmin())
    }

    // --- PRUEBAS DE LÓGICA DE RED ---

    @Test
    fun `login - con credenciales válidas - debe actualizar usuarioActual y mensaje de éxito`() = runTest {
        // Dado: Unas credenciales válidas y una respuesta exitosa del servidor mockeado
        val email = "test@test.com"
        val password = "password"
        val mockUser = Usuario("Test User", email, "", "")
        val mockResponse = AuthResponse("fake-token", mockUser)
        coEvery { mockAuthApiService.login(LoginRequest(email, password)) } returns Response.success(mockResponse)

        // Cuando: Se llama a la función login
        viewModel.login(email, password) { success ->
            // Entonces: El resultado del callback debe ser exitoso
            assertTrue(success)
        }

        // Y Entonces: Los estados del ViewModel deben actualizarse correctamente
        assertFalse(viewModel.isLoading.value)
        assertEquals(email, viewModel.usuarioActual.value)
        assertEquals("Inicio de sesión exitoso", viewModel.mensaje.value)
    }

    @Test
    fun `login - con credenciales inválidas - debe establecer mensaje de error`() = runTest {
        // Dado: Unas credenciales inválidas y una respuesta de error del servidor mockeado
        val email = "wrong@test.com"
        val password = "wrongpassword"
        coEvery { mockAuthApiService.login(any()) } returns Response.error(401, mockk(relaxed = true))

        // Cuando: Se llama a la función login
        viewModel.login(email, password) { success ->
            // Entonces: El resultado del callback debe ser fallido
            assertFalse(success)
        }

        // Y Entonces: Los estados del ViewModel deben reflejar el error
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.usuarioActual.value)
        assertEquals("Credenciales inválidas", viewModel.mensaje.value)
    }
}
