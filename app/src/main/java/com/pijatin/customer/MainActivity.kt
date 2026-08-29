package com.pijatin.customer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Layanan(val id: String, val nama: String, val durasi: String, val harga: String, val desc: String)
data class DrawerItem(val title: String, val icon: ImageVector)
enum class AppScreen { SPLASH, AUTH, HOME }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                var currentScreen by remember { mutableStateOf(AppScreen.SPLASH) }
                var userName by remember { mutableStateOf("") }
                var userPhone by remember { mutableStateOf("") }
                when (currentScreen) {
                    AppScreen.SPLASH -> SplashScreen { currentScreen = AppScreen.AUTH }
                    AppScreen.AUTH -> AuthScreen(
                        onLoginSuccess = { n, p -> userName = n; userPhone = p; currentScreen = AppScreen.HOME },
                        onRegisterSuccess = { n, p -> userName = n; userPhone = p; currentScreen = AppScreen.HOME }
                    )
                    AppScreen.HOME -> CustomerHomeWithSidebar(userName, userPhone)
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    val scale by rememberInfiniteTransition(label = "s").animateFloat(
        initialValue = 0.8f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "scale"
    )
    LaunchedEffect(Unit) {
        delay(2500)
        onFinish()
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFB8860B), Color(0xFFFF6F00), Color(0xFFB8860B)))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Card(shape = RoundedCornerShape(32.dp), modifier = Modifier.size(140.dp).scale(scale)) {
                Box(Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center) {
                    Text("🪷", fontSize = 70.sp)
                }
            }
            Spacer(Modifier.height(24.dp))
            Text("PijatIN", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text("CUSTOMER - GOLD LOTUS", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFE082))
            Spacer(Modifier.height(40.dp))
            CircularProgressIndicator(color = Color.White)
        }
    }
}

@Composable
fun AuthScreen(onLoginSuccess: (String, String) -> Unit, onRegisterSuccess: (String, String) -> Unit) {
    var isLogin by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    LaunchedEffect(loading) {
        if (loading) {
            delay(1500)
            loading = false
            val n = if (isLogin) "Customer Gold Lotus" else name
            if (isLogin) onLoginSuccess(n, phone) else onRegisterSuccess(n, phone)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8E1))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))
        Card(shape = RoundedCornerShape(24.dp), modifier = Modifier.size(80.dp)) {
            Box(Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center) {
                Text("🪷", fontSize = 40.sp)
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("PijatIN Customer", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB8860B))
        Spacer(Modifier.height(24.dp))
        Row(
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFFE0B2))
                .padding(4.dp)
        ) {
            Button(
                onClick = { isLogin = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isLogin) Color(0xFFB8860B) else Color.Transparent,
                    contentColor = if (isLogin) Color.White else Color.Gray
                ),
                modifier = Modifier.weight(1f)
            ) { Text("Login") }
            Button(
                onClick = { isLogin = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isLogin) Color(0xFFB8860B) else Color.Transparent,
                    contentColor = if (!isLogin) Color.White else Color.Gray
                ),
                modifier = Modifier.weight(1f)
            ) { Text("Daftar") }
        }
        Spacer(Modifier.height(24.dp))
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(Color.White), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                if (!isLogin) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nama Lengkap") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("No. HP / WhatsApp") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = PasswordVisualTransformation()
                )
                Button(
                    onClick = { loading = true },
                    enabled = phone.isNotBlank() && pass.isNotBlank() && (isLogin || name.isNotBlank()),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFFB8860B))
                ) { Text(if (isLogin) "Masuk ke PijatIN" else "Daftar") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomeWithSidebar(userName: String, userPhone: String) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selected by remember { mutableStateOf("Beranda") }

    val layanan = listOf(
        Layanan("1", "Pijat Full Body", "90 Menit", "Rp 150K", "Relaksasi Gold Lotus"),
        Layanan("2", "Pijat Refleksi + Totok Wajah", "60 Menit", "Rp 100K", "Pijat kaki & wajah"),
        Layanan("3", "Pijat Tradisional", "60 Menit", "Rp 120K", "Atasi pegal-pegal")
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(listOf(Color(0xFFB8860B), Color(0xFFFF8F00))))
                        .padding(20.dp)
                ) {
                    Column {
                        Spacer(Modifier.height(20.dp))
                        Card(shape = CircleShape, modifier = Modifier.size(64.dp)) {
                            Box(Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center) {
                                Text("🪷", fontSize = 32.sp)
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(if (userName.isNotBlank()) userName else "Violet Gold Lotus", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Phone, null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(if (userPhone.isNotBlank()) userPhone else "0812-3456-7890", color = Color.White.copy(0.9f), fontSize = 13.sp)
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))

                // ICON AMAN SEMUA - PASTI ADA DI ANDROID STUDIO LAMA
                val menus = listOf(
                    DrawerItem("Beranda", Icons.Default.Home),
                    DrawerItem("Order", Icons.Default.List),
                    DrawerItem("Saldo", Icons.Default.AccountBox),
                    DrawerItem("Therapist Saya", Icons.Default.Favorite),
                    DrawerItem("KUPON", Icons.Default.Star),
                    DrawerItem("Pengaturan Profil", Icons.Default.Person)
                )

                menus.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, null, tint = if (selected == item.title) Color(0xFFB8860B) else Color.Gray) },
                        label = { Text(item.title) },
                        selected = selected == item.title,
                        onClick = {
                            selected = item.title
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(selectedContainerColor = Color(0xFFFFF8E1))
                    )
                }
                Spacer(Modifier.weight(1f))
                Divider()
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ExitToApp, null, tint = Color.Red) },
                    label = { Text("Keluar", color = Color.Red) },
                    selected = false,
                    onClick = {},
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("PijatIN - GOLD LOTUS SPA", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, null, tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(Color(0xFFB8860B))
                )
            }
        ) { pad ->
            when (selected) {
                "Order" -> Box(
                    Modifier.fillMaxSize().padding(pad).background(Color(0xFFFFF8E1)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.List, null, Modifier.size(80.dp), tint = Color(0xFFB8860B))
                        Text("Order Saya", fontWeight = FontWeight.Bold)
                    }
                }
                "Saldo" -> Column(
                    Modifier.fillMaxSize().padding(pad).background(Color(0xFFFFF8E1)).padding(16.dp)
                ) {
                    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(Color(0xFFB8860B)), modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(20.dp)) {
                            Text("Saldo PijatIN", color = Color.White.copy(0.8f))
                            Text("Rp 0", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(16.dp))
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(Color.White, contentColor = Color(0xFFB8860B)),
                                modifier = Modifier.fillMaxWidth()
                            ) { Text("Top Up Saldo") }
                        }
                    }
                }
                "KUPON" -> Column(Modifier.fillMaxSize().padding(pad).background(Color(0xFFFFF8E1)).padding(16.dp)) {
                    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(Color.White), modifier = Modifier.fillMaxWidth()) {
                        Row(Modifier.padding(16.dp)) {
                            Card(colors = CardDefaults.cardColors(Color(0xFFB8860B))) {
                                Text("50% OFF", color = Color.White, modifier = Modifier.padding(12.dp))
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("GOLD50", fontWeight = FontWeight.Bold)
                                Text("Diskon 50% order pertama", fontSize = 12.sp)
                            }
                        }
                    }
                }
                "Pengaturan Profil" -> Column(Modifier.fillMaxSize().padding(pad).background(Color(0xFFFFF8E1)).padding(16.dp)) {
                    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(Color.White), modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("Pengaturan Profil", fontWeight = FontWeight.Bold, color = Color(0xFFB8860B))
                            OutlinedTextField(userName, {}, label = { Text("Nama Customer") }, modifier = Modifier.fillMaxWidth(), readOnly = true, leadingIcon = { Icon(Icons.Default.Person, null) })
                            OutlinedTextField(userPhone, {}, label = { Text("Nomor WA") }, modifier = Modifier.fillMaxWidth(), readOnly = true, leadingIcon = { Icon(Icons.Default.Phone, null) })
                            Button(onClick = {}, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Color(0xFFB8860B))) { Text("Edit Profil") }
                        }
                    }
                }
                else -> LazyColumn(
                    Modifier.fillMaxSize().padding(pad).background(Color(0xFFFFF8E1)).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(layanan) { item ->
                        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(Color.White), modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(item.nama, fontWeight = FontWeight.Bold)
                                    Text(item.harga, fontWeight = FontWeight.Bold, color = Color(0xFFB8860B))
                                }
                                Spacer(Modifier.height(6.dp))
                                Text(item.desc, fontSize = 12.sp, color = Color.Gray)
                                Spacer(Modifier.height(12.dp))
                                Button(onClick = {}, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Color(0xFFB8860B))) { Text("Pesan Sekarang") }
                            }
                        }
                    }
                }
            }
        }
    }
}
