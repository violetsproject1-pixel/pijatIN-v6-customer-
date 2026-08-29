package com.pijatin.customer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class Layanan(val id: String, val nama: String, val durasi: String, val harga: String, val desc: String)
enum class AppScreen { SPLASH, AUTH, HOME }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                var currentScreen by remember { mutableStateOf(AppScreen.SPLASH) }
                when (currentScreen) {
                    AppScreen.SPLASH -> SplashScreen(onFinish = { currentScreen = AppScreen.AUTH })
                    AppScreen.AUTH -> AuthScreen(onLoginSuccess = { currentScreen = AppScreen.HOME }, onRegisterSuccess = { currentScreen = AppScreen.HOME })
                    AppScreen.HOME -> CustomerHomeScreen()
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "splash")
    val scale by infiniteTransition.animateFloat(0.8f, 1.1f, infiniteRepeatable(tween(1000, easing = FastOutSlowInEasing), RepeatMode.Reverse), "scale")
    LaunchedEffect(Unit) { delay(2500); onFinish() }
    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFB8860B), Color(0xFFFF6F00), Color(0xFFB8860B)))), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Card(shape = RoundedCornerShape(32.dp), elevation = CardDefaults.cardElevation(12.dp), modifier = Modifier.size(140.dp).scale(scale)) {
                Box(Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center) { Text("🪷", fontSize = 70.sp) }
            }
            Spacer(Modifier.height(24.dp))
            Text("PijatIN", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = 2.sp)
            Text("CUSTOMER - GOLD LOTUS", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFE082), letterSpacing = 3.sp)
            Spacer(Modifier.height(40.dp))
            CircularProgressIndicator(color = Color.White, strokeWidth = 3.dp, modifier = Modifier.size(32.dp))
            Spacer(Modifier.height(12.dp))
            Text("Mencari terapis terbaik di dekatmu...", color = Color.White.copy(0.8f), fontSize = 12.sp)
        }
    }
}

@Composable
fun AuthScreen(onLoginSuccess: () -> Unit, onRegisterSuccess: () -> Unit) {
    var isLogin by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    LaunchedEffect(isLoading) { if (isLoading) { delay(1500); isLoading = false; if (isLogin) onLoginSuccess() else onRegisterSuccess() } }
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFFFF8E1)).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(40.dp))
        Card(shape = RoundedCornerShape(24.dp), elevation = CardDefaults.cardElevation(6.dp), modifier = Modifier.size(80.dp)) {
            Box(Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center) { Text("🪷", fontSize = 40.sp) }
        }
        Spacer(Modifier.height(16.dp))
        Text("Selamat Datang di", fontSize = 14.sp, color = Color.Gray)
        Text("PijatIN Customer", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB8860B))
        Text("GOLD LOTUS SPA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB8860B), letterSpacing = 2.sp)
        Spacer(Modifier.height(24.dp))
        Row(modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Color(0xFFFFE0B2)).padding(4.dp)) {
            Button(onClick = { isLogin = true }, colors = ButtonDefaults.buttonColors(containerColor = if (isLogin) Color(0xFFB8860B) else Color.Transparent, contentColor = if (isLogin) Color.White else Color.Gray), shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1f), elevation = ButtonDefaults.buttonElevation(0.dp)) { Text("Login", fontWeight = FontWeight.Bold) }
            Button(onClick = { isLogin = false }, colors = ButtonDefaults.buttonColors(containerColor = if (!isLogin) Color(0xFFB8860B) else Color.Transparent, contentColor = if (!isLogin) Color.White else Color.Gray), shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1f), elevation = ButtonDefaults.buttonElevation(0.dp)) { Text("Daftar", fontWeight = FontWeight.Bold) }
        }
        Spacer(Modifier.height(24.dp))
        Card(shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(4.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                if (!isLogin) OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nama Lengkap") }, placeholder = { Text("Violet Customer") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true)
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("No. HP / WhatsApp") }, placeholder = { Text("0812xxxx") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), singleLine = true)
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), singleLine = true)
                Spacer(Modifier.height(8.dp))
                Button(onClick = { isLoading = true }, enabled = !isLoading && phone.isNotBlank() && password.isNotBlank() && (isLogin || name.isNotBlank()), modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB8860B))) {
                    if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    else Text(if (isLogin) "Masuk ke PijatIN" else "Daftar Sebagai Customer", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomeScreen() {
    val layanan = listOf(
        Layanan("1", "Pijat Full Body", "90 Menit", "Rp 150K", "Relaksasi seluruh tubuh dengan aroma terapi Gold Lotus"),
        Layanan("2", "Pijat Refleksi + Totok Wajah", "60 Menit", "Rp 100K", "Pijat kaki & wajah untuk melancarkan peredaran darah"),
        Layanan("3", "Pijat Tradisional", "60 Menit", "Rp 120K", "Pijat urut tradisional untuk atasi pegal-pegal"),
        Layanan("4", "Pijat Ibu Hamil", "75 Menit", "Rp 180K", "Khusus ibu hamil dengan terapis bersertifikat"),
        Layanan("5", "Baby Spa + Pijat Bayi", "45 Menit", "Rp 130K", "Perawatan bayi dengan teknik lembut & aman")
    )
    Scaffold(topBar = { TopAppBar(title = { Text("PijatIN - GOLD LOTUS SPA", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFB8860B))) }) { paddingValues ->
        LazyColumn(Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFFFFF8E1)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(4.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("🪷 Halo, Selamat Datang!", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFFB8860B))
                        Text("Mau pijat apa hari ini? Pilih layanan Gold Lotus terbaik di Bekasi", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
            items(layanan) { item ->
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(4.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(item.nama, fontWeight = FontWeight.Bold, fontSize = 16.sp); Text(item.harga, fontWeight = FontWeight.Bold, color = Color(0xFFB8860B)) }
                        Text(item.durasi, fontSize = 11.sp, color = Color.White, modifier = Modifier.background(Color(0xFFB8860B), RoundedCornerShape(6.dp)).padding(horizontal = 8.dp, vertical = 2.dp))
                        Spacer(Modifier.height(6.dp))
                        Text(item.desc, fontSize = 12.sp, color = Color.Gray)
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = {}, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB8860B))) { Text("Pesan Sekarang - ${item.nama}", fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }
    }
}
