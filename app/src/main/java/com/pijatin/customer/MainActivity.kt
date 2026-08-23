package com.pijatin.customer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PijatINCustomerApp() }
    }
}

@Composable
fun PijatINCustomerApp() {
    var currentScreen by remember { mutableStateOf("splash") }
    
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2500)
        currentScreen = "home"
    }

    when(currentScreen) {
        "splash" -> SplashScreen()
        "home" -> HomeScreen()
    }
}

@Composable
fun SplashScreen() {
    Box(Modifier.fillMaxSize().background(Color(0xFF2D4A3E)), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(72.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                Text("A", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D4A3E))
            }
            Spacer(Modifier.height(12.dp))
            Text("pijatIN", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Box(Modifier.width(60.dp).height(3.dp).clip(RoundedCornerShape(2.dp)).background(Color(0xFFFF7A00)))
        }
    }
}

@Composable
fun HomeScreen() {
    val services = listOf(
        "Tradisional" to "98k", "Sport" to "169k", "Kerokan" to "135k",
        "Relaksasi" to "145k", "Ibu Hamil" to "129k", "Anak" to "75k"
    )
    LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp)) {
        item {
            Text("PijatIN Customer", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D4A3E))
            Text("Therapist Terdekat REAL • Ongkos 15k+5k/km", fontSize = 12.sp, color = Color.Gray)
            Spacer(Modifier.height(16.dp))
        }
        item {
            Text("Layanan", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(services) { (name, price) ->
                    Card(Modifier.width(140.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(Modifier.padding(16.dp)) {
                            Text(name, fontWeight = FontWeight.Bold)
                            Text(price, color = Color(0xFFFF7A00), fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = {}, Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D4A3E))) {
                                Text("Pesan", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
        item {
            Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp)) {
                    Text("● Sync Real-time Aktif", color = Color(0xFF4CAF50), fontSize = 12.sp)
                    Text("Belum ada therapist online", color = Color.Gray)
                }
            }
        }
    }
}
