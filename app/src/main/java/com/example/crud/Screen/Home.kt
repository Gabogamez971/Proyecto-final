package com.example.crud.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.crud.R


@Composable
fun HomeScreen(navController: NavHostController) {
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
                text = "Bienvenido",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón 1 - Actualizar Datos
            ImageButton(
                title = "Actualizar Datos",
                imageRes = R.drawable.crearusu,
                description = "Actualizar Datos",
                onClick = { navController.navigate("userApp") }
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
                title = "Consultar Citas",
                imageRes = R.drawable.consultarci,
                description = "Consultar Citas",
                onClick = { navController.navigate("consultarCitas") }
            )
        }

        // Botón pequeño mejorado en la esquina inferior derecha
        IconButton(
            onClick = {
                // Lógica para el botón, por ejemplo, cerrar sesión o cualquier acción
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(70.dp)  // Tamaño aumentado
                .padding(16.dp)
                .background(
                    Brush.horizontalGradient(listOf(Color(0xFF00BFAE), Color(0xFF0288D1))), // Degradado llamativo
                    shape = CircleShape
                )
                .shadow(elevation = 8.dp, shape = CircleShape)  // Sombra para resaltar
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,  // Usa el ícono de cierre de sesión
                contentDescription = "Cerrar sesión",
                tint = Color.White
            )
        }
    }
}

@Composable
fun ImageButton(title: String, imageRes: Int, description: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp)
            .clickable(onClick = onClick)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
            .background(Color.White, shape = RoundedCornerShape(16.dp))
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = description,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF0288D1),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}