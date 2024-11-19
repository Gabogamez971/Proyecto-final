package com.example.crud.Screen

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.crud.Model.Cita
import com.example.crud.Repository.CitaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.LaunchedEffect
import java.time.format.DateTimeFormatter
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import com.example.krud.Repository.UserRepository
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarCitaScreen(
    userCedula: String,
    citaRepository: CitaRepository,
    userRepository: UserRepository,
    onCitaAgendada: () -> Unit
) {
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var cedula by remember { mutableStateOf(userCedula) }
    val context = LocalContext.current
    val datePickerDialogState = remember { mutableStateOf(false) }
    var citasAgendadas by remember { mutableStateOf<List<Cita>>(emptyList()) }

    val scope = rememberCoroutineScope()
    LaunchedEffect(fecha) {
        if (fecha.isNotEmpty()) {
            citasAgendadas = citaRepository.getCitasByFecha(fecha)
        }
    }

    if (datePickerDialogState.value) {
        // Obtiene la fecha actual en milisegundos para establecer la fecha mínima
        val today = Calendar.getInstance()
        android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                fecha = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                datePickerDialogState.value = false
            },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ).apply {
            // Establece la fecha mínima a la fecha actual
            datePicker.minDate = today.timeInMillis
            show()
        }
    }

    val availableHours = (6..18).map { String.format("%02d:00", it) }
    val occupiedHours = citasAgendadas.map { it.hora }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título y descripción
            Text(
                text = "Agendar Cita Médica",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Por favor selecciona una fecha y hora disponibles para tu cita médica.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            // Campo de Cédula
            CustomTextField(
                value = cedula,
                onValueChange = { cedula = it },
                label = "Cédula del Usuario"
            )

            // Botón de Selección de Fecha
            CustomOutlinedButton(
                onClick = { datePickerDialogState.value = true },
                text = if (fecha.isEmpty()) "Seleccionar Fecha" else "Fecha: $fecha",
                leadingIcon = Icons.Filled.CalendarToday
            )

            // Selección de Hora con horas disponibles y ocupadas
            Text(
                text = "Selecciona una Hora:",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(availableHours) { hour ->
                    val isOccupied = occupiedHours.contains(hour)
                    CustomHourButton(
                        hour = hour,
                        isOccupied = isOccupied,
                        isSelected = hour == hora,
                        onClick = {
                            if (!isOccupied) {
                                hora = hour
                            }
                        }
                    )
                }
            }

            // Campo de Descripción de la Cita
            CustomTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = "Descripción de la Cita"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para Agendar Cita
            CustomButton(
                onClick = {
                    if (cedula.isNotEmpty() && fecha.isNotEmpty() && hora.isNotEmpty() && descripcion.isNotEmpty()) {
                        scope.launch {
                            val userExists = userRepository.getUserByCedula(cedula) != null
                            if (userExists) {
                                val cita = Cita(
                                    fecha = fecha,
                                    hora = hora,
                                    descripcion = descripcion,
                                    userCedula = cedula
                                )
                                citaRepository.insert(cita)
                                Toast.makeText(context, "Cita Agendada", Toast.LENGTH_SHORT).show()
                                onCitaAgendada()
                            } else {
                                Toast.makeText(context, "Cédula no válida", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                text = "Agendar Cita"
            )
        }
    }
}

@Composable
fun CustomHourButton(
    hour: String,
    isOccupied: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(48.dp)
            .padding(vertical = 4.dp)
            .widthIn(min = 80.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = when {
                isOccupied -> Color.Gray
                isSelected -> MaterialTheme.colorScheme.secondary
                else -> MaterialTheme.colorScheme.primary
            }
        ),
        enabled = !isOccupied,
        shape = RoundedCornerShape(16.dp),
        elevation = if (isSelected) ButtonDefaults.buttonElevation(8.dp) else ButtonDefaults.buttonElevation(2.dp)
    ) {
        Text(
            text = hour,
            color = if (isOccupied) Color.White.copy(alpha = 0.6f) else Color.White
        )
    }
}

@Composable
fun CustomOutlinedButton(
    onClick: () -> Unit,
    text: String,
    leadingIcon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        leadingIcon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp).padding(end = 8.dp)
            )
        }
        Text(text = text)
    }
}

@Composable
fun CustomButton(
    onClick: () -> Unit,
    text: String
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(16.dp)
    ) {
        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = Color.White)
    }
}
