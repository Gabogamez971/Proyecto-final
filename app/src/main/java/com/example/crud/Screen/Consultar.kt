package com.example.crud.Screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.crud.Model.Cita
import com.example.crud.Repository.CitaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultarCitasScreen(citaRepository: CitaRepository) {
    var cedula by remember { mutableStateOf("") }
    var citas by remember { mutableStateOf<List<Cita>>(emptyList()) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 32.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Consultar Citas",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )

        TextField(
            value = cedula,
            onValueChange = { cedula = it },
            label = { Text("Cédula del Usuario") },
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Button(
            onClick = {
                if (cedula.isNotEmpty()) {
                    scope.launch {
                        citas = withContext(Dispatchers.IO) {
                            citaRepository.getCitasByCedula(cedula)
                        }
                        if (citas.isEmpty()) {
                            Toast.makeText(context, "No se encontraron citas para esta cédula", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Por favor ingresa una cédula", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "Consultar Citas", style = MaterialTheme.typography.labelLarge)
        }

        if (citas.isNotEmpty()) {
            Text(
                text = "Citas Encontradas:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(citas) { cita ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("Fecha: ${cita.fecha}", style = MaterialTheme.typography.titleSmall)
                                Text("Hora: ${cita.hora}", style = MaterialTheme.typography.bodyMedium)
                                Text("Descripción: ${cita.descripcion}", style = MaterialTheme.typography.bodySmall)
                                Text("Estado: ${cita.estado}", style = MaterialTheme.typography.bodyMedium)
                            }

                            IconButton(onClick = {
                                scope.launch {
                                    withContext(Dispatchers.IO) {
                                        citaRepository.deleteCita(cita)
                                    }
                                    citas = citas.filterNot { it.id == cita.id }
                                    Toast.makeText(context, "Cita eliminada", Toast.LENGTH_SHORT).show()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar Cita",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}