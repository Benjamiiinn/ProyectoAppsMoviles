package com.example.proyectomoviles.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.proyectomoviles.remote.AuthAPIService
import com.example.proyectomoviles.remote.AuthResponse
import com.example.proyectomoviles.remote.LoginRequest
import com.example.proyectomoviles.remote.RetrofitClient
import com.example.proyectomoviles.utils.TokenManager
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
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
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AuthViewModel
    private lateinit var mockAuthApiService: AuthAPIService
    private val mockApplication = mockk<Application>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        mockAuthApiService = mockk()

        // Mockear objetos para controlar las dependencias que AuthViewModel crea internamente
        mockkObject(RetrofitClient)
        mockkObject(TokenManager)

        // Configurar el mock de RetrofitClient para que devuelva nuestro servicio mockeado
        val mockRetrofit = mockk<Retrofit>()
        every { RetrofitClient.getClient(any()) } returns mockRetrofit
        every { mockRetrofit.create(AuthAPIService::class.java) } returns mockAuthApiService

        // Configurar el mock de TokenManager para el bloque init y otras llamadas
        every { TokenManager.isLoggedIn() } returns false
        every { TokenManager.saveAuthInfo(any(), any(), any(), any(), any(), any(), any(), any()) } returns Unit

        // Crear la instancia del ViewModel con el ÚNICO argumento que espera: Application
        viewModel = AuthViewModel(mockApplication)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll() // Limpiar todos los mocks después de cada prueba
    }

    @Test
    fun `login con credenciales válidas actualiza estado y llama onResult con true`() = runTest {
        val email = "test@test.com"
        val password = "password"
        val mockAuthResponse = AuthResponse(token = "token-123", role = "USER", userId = 1)
        coEvery { mockAuthApiService.login(LoginRequest(email, password)) } returns Response.success(mockAuthResponse)

        // Simular el comportamiento de TokenManager después de un login exitoso
        every { TokenManager.getUserId() } returns 1
        every { TokenManager.getUserName() } returns email
        every { TokenManager.getUserEmail() } returns email
        every { TokenManager.getUserRut() } returns ""
        every { TokenManager.getUserTelefono() } returns ""
        every { TokenManager.getUserDireccion() } returns ""
        
        var result = false
        viewModel.login(email, password) { success ->
            result = success
        }

        assertTrue(result)
        assertEquals("Inicio de sesión exitoso", viewModel.mensaje.value.first)
        assertFalse(viewModel.isLoading.value)
        assertNotNull(viewModel.usuarioActual.value)
        assertEquals(email, viewModel.usuarioActual.value?.email)
    }

    @Test
    fun `login con credenciales inválidas actualiza mensaje y llama onResult con false`() = runTest {
        val email = "wrong@test.com"
        val password = "wrongpassword"
        coEvery { mockAuthApiService.login(any()) } returns Response.error(401, mockk(relaxed = true))

        var result = true
        viewModel.login(email, password) { success ->
            result = success
        }

        assertFalse(result)
        assertEquals("Credenciales inválidas", viewModel.mensaje.value.first)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.usuarioActual.value)
    }

    @Test
    fun `isAdmin devuelve true cuando el rol del usuario es ADMIN`() {
        every { TokenManager.getUserRole() } returns "ADMIN"
        assertTrue(viewModel.isAdmin())
    }

    @Test
    fun `isAdmin devuelve false cuando el rol del usuario no es ADMIN`() {
        every { TokenManager.getUserRole() } returns "USER"
        assertFalse(viewModel.isAdmin())
    }
}
