package com.pijatin.customer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Layanan(val id: String, val nama: String, val durasi: String, val harga: String, val desc: String)
data class DrawerItem(val title: String, val icon: ImageVector, val route: String)
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
                    AppScreen.SPLASH -> SplashScreen(onFinish = { currentScreen = AppScreen.AUTH })
                    AppScreen.AUTH -> AuthScreen(
                        onLoginSuccess = { name, phone -> userName = name; userPhone = phone; currentScreen = AppScreen.HOME },
                        onRegisterSuccess = { name, phone -> userName = name; userPhone = phone; currentScreen = AppScreen.HOME }
                    )
                    AppScreen.HOME -> CustomerHomeWithSidebar(userName = userName, userPhone = userPhone)
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
fun AuthScreen(onLoginSuccess: (String, String) -> Unit, onRegisterSuccess: (String, String) -> Unit) {
    var isLogin by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(1500)
            isLoading = false
            val finalName = if (isLogin) "Customer Gold Lotus" else name
            if (isLogin) onLoginSuccess(finalName, phone) else onRegisterSuccess(finalName, phone)
        }
    }

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
fun CustomerHomeWithSidebar(userName: String, userPhone: String) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedMenu by remember { mutableStateOf("Beranda") }

    val layanan = listOf(
        Layanan("1", "Pijat Full Body", "90 Menit", "Rp 150K", "Relaksasi seluruh tubuh dengan aroma terapi Gold Lotus"),
        Layanan("2", "Pijat Refleksi + Totok Wajah", "60 Menit", "Rp 100K", "Pijat kaki & wajah untuk melancarkan peredaran darah"),
        Layanan("3", "Pijat Tradisional", "60 Menit", "Rp 120K", "Pijat urut tradisional untuk atasi pegal-pegal"),
        Layanan("4", "Pijat Ibu Hamil", "75 Menit", "Rp 180K", "Khusus ibu hamil dengan terapis bersertifikat")
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = Color.White, modifier = Modifier.width(300.dp)) {
                // HEADER - Nama & WA Customer
                Box(modifier = Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(Color(0xFFB8860B), Color(0xFFFF8F00)))).padding(20.dp)) {
                    Column {
                        Spacer(Modifier.height(20.dp))
                        Card(shape = CircleShape, modifier = Modifier.size(64.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("🪷", fontSize = 32.sp) }
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(text = if (userName.isNotBlank()) userName else "Violet Gold Lotus", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = Color.White.copy(0.9f), modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(text = if (userPhone.isNotBlank()) userPhone else "0812-3456-7890", color = Color.White.copy(0.9f), fontSize = 13.sp)
                        }
                        Spacer(Modifier.height(4.dp))
                        Text("Customer - Gold Lotus Spa", color = Color(0xFFFFE082), fontSize = 11.sp, letterSpacing = 1.sp)
                    }
                }

                Spacer(Modifier.height(12.dp))

                // MENU LIST
                val menus = listOf(
                    DrawerItem("Beranda", Icons.Default.Home, "home"),
                    DrawerItem("Order Saya", Icons.Default.ShoppingBag, "order"),
                    DrawerItem("Saldo", Icons.Default.AccountBalanceWallet, "saldo"),
                    DrawerItem("Therapist Saya", Icons.Default.Favorite, "therapist"),
                    DrawerItem("KUPON", Icons.Default.LocalOffer, "kupon"),
                    DrawerItem("Pengaturan Profil", Icons.Default.Person, "profil")
                )

                menus.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = null, tint = if (selectedMenu == item.title) Color(0xFFB8860B) else Color.Gray) },
                        label = { Text(item.title, fontWeight = if (selectedMenu == item.title) FontWeight.Bold else FontWeight.Normal, color = if (selectedMenu == item.title) Color(0xFFB8860B) else Color.Black) },
                        selected = selectedMenu == item.title,
                        onClick = { selectedMenu = item.title; scope.launch { drawerState.close() } },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(selectedContainerColor = Color(0xFFFFF8E1), unselectedContainerColor = Color.Transparent)
                    )
                }

                Spacer(Modifier.weight(1f))
                Divider()
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Logout, contentDescription = null, tint = Color.Red) },
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
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFB8860B)),
                    actions = {
                        IconButton(onClick = {}) { Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White) }
                    }
                )
            }
        ) { paddingValues ->
            when (selectedMenu) {
                "Order Saya" -> OrderScreen()
                "Saldo" -> SaldoScreen()
                "Therapist Saya" -> TherapistScreen()
                "KUPON" -> KuponScreen()
                "Pengaturan Profil" -> ProfilScreen(name = userName, phone = userPhone)
                else -> HomeLayananContent(layanan = layanan, paddingValues = paddingValues)
            }
        }
    }
}

@Composable
fun HomeLayananContent(layanan: List<Layanan>, paddingValues: PaddingValues) {
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

@Composable
fun OrderScreen() {
    Column(Modifier.fillMaxSize().background(Color(0xFFFFF8E1)).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color(0xFFB8860B).copy(0.5f))
        Spacer(Modifier.height(16.dp))
        Text("Order Saya", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB8860B))
        Text("Belum ada order", color = Color.Gray)
    }
}

@Composable
fun SaldoScreen() {
    Column(Modifier.fillMaxSize().background(Color(0xFFFFF8E1)).padding(16.dp)) {
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFB8860B)), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp)) {
                Text("Saldo PijatIN", color = Color.White.copy(0.8f), fontSize = 13.sp)
                Spacer(Modifier.height(8.dp))
                Text("Rp 0", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFFB8860B)), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) { Text("Top Up Saldo", fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
fun TherapistScreen() {
    Column(Modifier.fillMaxSize().background(Color(0xFFFFF8E1)).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color(0xFFB8860B).copy(0.5f))
        Spacer(Modifier.height(16.dp))
        Text("Therapist Saya", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB8860B))
        Text("Belum ada therapist favorit", color = Color.Gray)
    }
}

@Composable
fun KuponScreen() {
    Column(Modifier.fillMaxSize().background(Color(0xFFFFF8E1)).padding(16.dp)) {
        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(4.dp), modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Card(shape = RoundedCornerShape(10.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFB8860B))) { Text("50% OFF", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) }
                Spacer(Modifier.width(12.dp))
                Column { Text("GOLD50", fontWeight = FontWeight.Bold); Text("Diskon 50% untuk order pertama", fontSize = 12.sp, color = Color.Gray) }
            }
        }
    }
}

@Composable
fun ProfilScreen(name: String, phone: String) {
    Column(Modifier.fillMaxSize().background(Color(0xFFFFF8E1)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Pengaturan Profil", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFFB8860B))
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(value = name, onValueChange = {}, label = { Text("Nama Customer") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), readOnly = true, leadingIcon = { Icon(Icons.Default.Person, null) })
                OutlinedTextField(value = phone, onValueChange = {}, label = { Text("Nomor WA / No. HP") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), readOnly = true, leadingIcon = { Icon(Icons.Default.Phone, null) })
                Spacer(Modifier.height(12.dp))
                Button(onClick = {}, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB8860B))) { Text("Edit Profil") }
            }
        }
    }
}
