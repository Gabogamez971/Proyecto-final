package com.example.crud.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.crud.Model.Cita
import com.example.crud.Model.User
import com.example.crud.R
import com.example.crud.Repository.CitaRepository

@Composable
fun AdminScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0xFFB3E5FC), Color(0xFF0288D1))))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Bienvenido Admin",
                style = MaterialTheme.typography.titleLarge,
                color = androidx.compose.ui.graphics.Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón 1 - Actualizar Datos
            ImageButton(
                title = "Ver próximas Consultas",
                imageRes = R.drawable.verificarc,
                description = "Actualizar Datos",
                onClick = { navController.navigate("homeAdmin") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón 2 - Registrar Cita
            ImageButton(
                title = "Registrar Cita",
                imageRes = R.drawable.guardarcita,
                description = "Registrar Cita",
                onClick = { navController.navigate("agendarCita") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón 3 - Consultar Citas
            ImageButton(
                title = "Administrar Usuarios",
                imageRes = R.drawable.administrar,
                description = "Consultar Citas",
                onClick = { navController.navigate("adminUsu") }
            )
        }

        // Botón de cierre de sesión en la esquina inferior derecha
        IconButton(
            onClick = {
                // Navega a la pantalla de inicio de sesión y elimina todas las pantallas anteriores de la pila
                navController.navigate("login") {
                    // Esto elimina todas las pantallas anteriores de la pila de navegación
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(70.dp)
                .padding(16.dp)
                .background(
                    Brush.horizontalGradient(listOf(Color(0xFF00BFAE), Color(0xFF0288D1))),
                    shape = CircleShape
                )
                .shadow(elevation = 8.dp, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Cerrar sesión",
                tint = androidx.compose.ui.graphics.Color.White
            )
        }
    }
}

