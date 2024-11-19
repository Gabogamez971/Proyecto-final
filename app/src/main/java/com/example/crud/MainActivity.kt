package com.example.crud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.crud.DAO.CitaDao
import com.example.crud.DAO.UserDao
import com.example.crud.Database.UserDatabase
import com.example.crud.Model.User
import com.example.crud.Repository.CitaRepository
import com.example.crud.Screen.AdminCitaItem
import com.example.crud.Screen.AdminScreen
import com.example.crud.Screen.AdminUsersScreen
import com.example.crud.Screen.AgendarCitaScreen
import com.example.crud.Screen.ConsultarCitasScreen
import com.example.crud.Screen.HomeScreen
import com.example.crud.Screen.LoginScreen
import com.example.crud.Screen.RegisterScreen
import com.example.crud.Screen.UserApp
import com.example.krud.Repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainActivity : ComponentActivity() {
    private lateinit var userDao: UserDao
    private lateinit var userRepository: UserRepository
    private lateinit var citaDao: CitaDao
    private lateinit var citaRepository: CitaRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        //deleteDatabase("user_database")
        super.onCreate(savedInstanceState)

                // Configuración de la base de datos
                val db = UserDatabase.getDatabase(applicationContext) // Asegúrate de que UserDatabase tenga CitaDao y Cita
                userDao = db.userDao()
                userRepository = UserRepository(userDao)

                citaDao = db.citaDao() // Asegúrate de que citaDao esté en la misma base de datos
                citaRepository = CitaRepository(citaDao)

                CoroutineScope(Dispatchers.IO).launch {
                    db.prepopulateDatabase()
                }

                enableEdgeToEdge()


                setContent {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "login") {

                        composable("adminHome") { // Ruta para la pantalla de administrador
                            AdminScreen(navController)
                        }

                        composable("register") {
                            RegisterScreen(userRepository, navController)
                        }

                        composable("login") {
                            LoginScreen(userRepository, navController)
                        }
                        composable("home") {
                            HomeScreen(navController)
                        }
                        composable("userApp") {
                            // Usamos un estado para almacenar el usuario
                            var user by remember { mutableStateOf<User?>(null) }
                            val coroutineScope = rememberCoroutineScope()

                            // Llamamos a la función suspend dentro de LaunchedEffect
                            LaunchedEffect(Unit) {
                                user = userRepository.getCurrentUser() // Llamamos a la función suspend dentro de una corrutina
                            }

                            if (user != null) {
                                // Si se obtiene el usuario, mostramos la pantalla UserApp
                                UserApp(userRepository = userRepository, navController = navController)
                            } else {
                                // Mientras esperamos el usuario, mostramos un ProgressIndicator
                                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                            }
                        }
                        composable("agendarCita?userCedula={userCedula}") { backStackEntry ->
                            val userCedula = backStackEntry.arguments?.getString("userCedula") ?: ""
                            AgendarCitaScreen(
                                userCedula = userCedula,
                                citaRepository = citaRepository,
                                userRepository = userRepository,
                                onCitaAgendada = {
                                    // Manejar lo que sucede después de agendar la cita
                                    navController.popBackStack() // Regresar a la pantalla anterior
                                }
                            )
                        }
                        composable("consultarCitas") {
                            ConsultarCitasScreen(citaRepository)
                        }
                        composable("homeAdmin") {
                            AdminCitaItem(navController, citaRepository, userRepository)
                        }
                        composable("adminUsu") {
                            AdminUsersScreen(userRepository, citaRepository)
                        }

                    }
                }
            }
        }