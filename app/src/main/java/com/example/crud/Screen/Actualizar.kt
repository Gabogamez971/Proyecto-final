package com.example.crud.Screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.crud.Model.User
import com.example.krud.Repository.UserRepository
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserApp(userRepository: UserRepository, navController: NavController) {
    var cedula by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), // Habilitar scroll si el contenido es largo
        verticalArrangement = Arrangement.spacedBy(24.dp), // Aumentar espaciado entre los elementos
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la pantalla
        Text(
            text = "Actualizar Datos de Usuario",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Campo de texto para la cédula
        CustomTextField(label = "Cédula", value = cedula, onValueChange = { cedula = it })

        // Botón para buscar el usuario por cédula
        CustomButton(
            onClick = {
                scope.launch {
                    try {
                        val user = userRepository.getUserByCedula(cedula)
                        if (user != null) {
                            // Si el usuario es encontrado, cargar sus datos en los campos
                            nombre = user.nombre
                            apellido = user.apellido
                            edad = user.edad.toString()
                            peso = user.peso.toString()
                            altura = user.altura.toString()
                            contrasena = user.contraseña
                        } else {
                            Toast.makeText(context, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error al buscar usuario: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            text = "Buscar Usuario"
        )

        // Campos de texto para editar datos del usuario
        CustomTextField(label = "Nombre", value = nombre, onValueChange = { nombre = it })
        CustomTextField(label = "Apellido", value = apellido, onValueChange = { apellido = it })
        CustomTextField(label = "Edad", value = edad, onValueChange = { edad = it })
        CustomTextField(label = "Peso (kg)", value = peso, onValueChange = { peso = it })
        CustomTextField(label = "Altura (cm)", value = altura, onValueChange = { altura = it })
        CustomTextField(label = "Contraseña", value = contrasena, onValueChange = { contrasena = it })

        // Botón para actualizar usuario
        CustomButton(
            onClick = {
                // Validar campos
                if (nombre.isEmpty() || apellido.isEmpty() || edad.isEmpty()) {
                    Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                    return@CustomButton
                }
                val edadInt = edad.toIntOrNull()
                val pesoDouble = peso.toDoubleOrNull()
                val alturaDouble = altura.toDoubleOrNull()

                if (edadInt == null || pesoDouble == null || alturaDouble == null) {
                    Toast.makeText(context, "Asegúrate de ingresar valores numéricos válidos", Toast.LENGTH_SHORT).show()
                    return@CustomButton
                }

                val updatedUser = User(
                    cedula = cedula,
                    nombre = nombre,
                    apellido = apellido,
                    edad = edadInt,
                    peso = pesoDouble,
                    altura = alturaDouble,
                    contraseña = contrasena
                )

                scope.launch {
                    try {
                        userRepository.update(updatedUser)
                        Toast.makeText(context, "Datos actualizados exitosamente", Toast.LENGTH_SHORT).show()
                        // Regresar a la pantalla anterior después de la actualización
                        navController.popBackStack()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error al actualizar: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            text = "Actualizar Usuario"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(label: String, value: String, onValueChange: (String) -> Unit, enabled: Boolean = true) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp), // Añadir espaciado lateral
        enabled = enabled,
        shape = RoundedCornerShape(10.dp), // Bordes redondeados
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        textStyle = TextStyle(fontSize = 16.sp) // Aumentar el tamaño de fuente
    )
}

@Composable
fun CustomButton(onClick: () -> Unit, text: String, backgroundColor: Color = MaterialTheme.colorScheme.primary) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp) // Altura más consistente para el botón
            .padding(vertical = 8.dp), // Añadir espaciado vertical
        shape = RoundedCornerShape(12.dp), // Bordes redondeados
    ) {
        Text(
            text = text,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
            color = Color.White
        )
    }
}
