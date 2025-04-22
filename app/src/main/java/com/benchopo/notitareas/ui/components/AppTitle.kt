package com.benchopo.notitareas.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.benchopo.notitareas.R
import com.benchopo.notitareas.ui.navigation.Routes
import com.benchopo.notitareas.viewModel.AuthViewModel

@Composable
fun AppTitle(
    navController: NavController,
    authViewModel: AuthViewModel,
    usuarioNombre: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "Logo NotiTareas",
                modifier = Modifier
                    .size(50.dp)
            )

            Text(
                text = "Bienvenid@ $usuarioNombre",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            //Boton de logout

            Button(
                onClick = {
                    navController.navigate(Routes.Login){
                        popUpTo(0) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("Cerrar sesión")
            }
        }

    }
}