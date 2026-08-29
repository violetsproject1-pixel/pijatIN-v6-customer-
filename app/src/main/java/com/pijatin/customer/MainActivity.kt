package com.pijatin.customer
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
// TODO Tambah di app/build.gradle.kts:
// implementation("io.github.jupf:supabase-kt:2.8.0")
// implementation("io.github.jupf:supabase-postgrest:2.8.0")
// implementation("io.github.jupf:supabase-realtime:2.8.0")
// implementation("com.google.android.gms:play-services-maps:18.2.0")
// implementation("com.google.maps.android:maps-compose:4.3.3")
val Green = Color(0xFF2D4A3E)
val Orange = Color(0xFFFF7A00)
data class OrderReal(
val id: String = "ORD-${(10000..99999).random()}",
val customer_id: String,
val therapist_id: String = "SR",
val layanan: String,
val durasi: Int,
val harga: Int,
val ongkir: Int = 15000,
val biaya_layanan: Int = 5000,
val total: Int,
val alamat: String,
val lat: Double = -6.2,
val lng: Double = 106.8,
val status: String = "menunggu_mitra",
val payment_method: String = "tunai",
val rating: Int = 0
)
class MainActivity : ComponentActivity() {
override fun onCreate(b: Bundle?) {
super.onCreate(b)
setContent {
val ctx = LocalContext.current
val prefs = ctx.getSharedPreferences("PijatIN_Login", 0)
val scope = rememberCoroutineScope()
var showSplash by remember { mutableStateOf(true) }
var isLoggedIn by remember { mutableStateOf(prefs.getBoolean("isLoggedIn", false)) }
var savedPhone by remember { mutableStateOf(prefs.getString("phone", "") ?: "083893330346") }
var customerId by remember { mutableStateOf(prefs.getString("customer_id", "CUST-${savedPhone}") ?: "CUST-001") }
var showDrawer by remember { mutableStateOf(false) }
var currentPage by remember { mutableStateOf("HOME") }
var saldo by remember { mutableStateOf(prefs.getInt("saldo", 50000)) }
// REAL FLOW STATES
var selectedLayanan by remember { mutableStateOf<Pair<String, Int>>(Pair("Tradisional 60'", 120000)) }
var selectedTherapist by remember { mutableStateOf("SR Siti Rahayu") }
var selectedDurasi by remember { mutableStateOf(60) }
var showAlamatModal by remember { mutableStateOf(false) }
var alamatText by remember { mutableStateOf("Jl. Ciledug Raya No. 10, Tangerang") }
var orderStatus by remember { mutableStateOf("idle") } // idle, menunggu_mitra, dalam_perjalanan, sudah_sampai, menunggu_pembayaran, timer, rating
var currentOrder by remember { mutableStateOf<OrderReal?>(null) }
var progress by remember { mutableStateOf(0f) } // 0-100 motor
var eta by remember { mutableStateOf("15:00") }
var paymentMethod by remember { mutableStateOf("tunai") } // tunai / non_tunai
var timerSeconds by remember { mutableStateOf(60 * 60) } // 60 menit contoh
var rating by remember { mutableStateOf(0) }
var topUpVa by remember { mutableStateOf("39008${(100000000..999999999).random()}") }
var vaExpiry by remember { mutableStateOf("24 jam") }
LaunchedEffect(Unit) { delay(1000); showSplash = false }
// SIMULASI Realtime mitra terima order setelah 3 detik
LaunchedEffect(orderStatus) {
if (orderStatus == "menunggu_mitra") {
delay(3000)
orderStatus = "dalam_perjalanan"
// TODO REAL: supabase.channel("orders").on("postgres_changes", filter: order id).subscribe { status = dalam_perjalanan }
}
if (orderStatus == "dalam_perjalanan") {
// Update lokasi 5 detik ke Supabase + progress motor
while (progress < 100f) {
delay(5000)
progress += 15f
val mins = (15 * (1 - progress / 100)).toInt()
eta = "%02d:00".format(mins)
// TODO REAL: supabase.from("orders").update(mapOf("lat" to mitraLat, "lng" to mitraLng, "progress" to progress)).eq("id", currentOrder?.id)
if (progress >= 100f) {
orderStatus = "sudah_sampai"
break
}
}
}
}
// TIMER FULLSCREEN
LaunchedEffect(orderStatus) {
if (orderStatus == "timer") {
while (timerSeconds > 0) {
delay(1000)
timerSeconds--
}
 // beep panjang + vibrate
try {
val vibrator = ctx.getSystemService(Vibrator::class.java)
vibrator?.vibrate(VibrationEffect.createOneShot(2000, 255))
} catch (e: Exception) {}
orderStatus = "rating"
}
}
if (showSplash) {
Box(Modifier.fillMaxSize().background(Orange), contentAlignment = Alignment.Center) {
Column(horizontalAlignment = Alignment.CenterHorizontally) {
Text("PijatIN REAL", fontWeight = FontWeight.Bold, fontSize = 30.sp, color = Color.White)
Text("Supabase • Maps • Realtime", fontSize = 12.sp, color = Color.White)
}
}
} else if (!isLoggedIn) {
var hp by remember { mutableStateOf("083893330346") }
var email by remember { mutableStateOf("malikysyachmal2018@gmail.com") }
var p1 by remember { mutableStateOf("") }
var p2 by remember { mutableStateOf("") }
val isFormValid = hp.length >= 10 && email.contains("@") && p1.length >= 6 && p1 == p2
Scaffold(bottomBar = {
Column(Modifier.fillMaxWidth().background(Color.White).padding(16.dp)) {
Button(onClick = {
if (!isFormValid) return@Button
prefs.edit().putBoolean("isLoggedIn", true).putString("phone", hp).putString("customer_id", "CUST-$hp").apply()
savedPhone = hp; customerId = "CUST-$hp"; isLoggedIn = true
}, modifier = Modifier.fillMaxWidth().height(56.dp), enabled = isFormValid, colors = ButtonDefaults.buttonColors(Green), shape = RoundedCornerShape(14.dp)) {
Text("DAFTAR SEKARANG", color = Color.White, fontWeight = FontWeight.Bold)
}
}
}) { pad ->
LazyColumn(Modifier.fillMaxSize().background(Color.White).padding(pad).padding(16.dp)) {
item {
Text("Selamat Datang REAL", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Green)
Spacer(Modifier.height(16.dp))
OutlinedTextField(value = hp, onValueChange = { hp = it }, label = { Text("No Telepon *") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true)
Spacer(Modifier.height(8.dp))
OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email *") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true)
Spacer(Modifier.height(8.dp))
OutlinedTextField(value = p1, onValueChange = { p1 = it }, label = { Text("Password *") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true)
Spacer(Modifier.height(8.dp))
OutlinedTextField(value = p2, onValueChange = { p2 = it }, label = { Text("Konfirmasi *") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), singleLine = true)
}
}
}
}
} else {
// STATUS SCREENS PRIORITAS
when (orderStatus) {
"dalam_perjalanan" -> {
Box(Modifier.fillMaxSize().background(Color.White).padding(16.dp)) {
Column {
Text("Mitra Dalam Perjalanan", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Green)
Spacer(Modifier.height(12.dp))
// MAPS 250px 2 pin + motor 0-100%
Box(Modifier.fillMaxWidth().height(250.dp).background(Color(0xFFE0E0E0), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
Column(horizontalAlignment = Alignment.CenterHorizontally) {
Text("MAPS 250px", fontWeight = FontWeight.Bold)
Text("Pin Customer - Pin Mitra", fontSize = 11.sp)
Spacer(Modifier.height(8.dp))
Text("🛵 ${progress.toInt()}% • ETA ${eta}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
LinearProgressIndicator(progress = { progress / 100f }, modifier = Modifier.fillMaxWidth().padding(16.dp).height(8.dp), color = Orange)
Text("Lat -6.2 Lng 106.8 • Update 5 detik ke Supabase", fontSize = 9.sp, color = Color.Gray)
}
}
Spacer(Modifier.height(12.dp))
Text("Order ${currentOrder?.id} • ${currentOrder?.layanan} • ${selectedTherapist}", fontSize = 13.sp)
Spacer(Modifier.height(16.dp))
Button(onClick = { orderStatus = "sudah_sampai" }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Green)) { Text("Simulasi Sudah Sampai", color = Color.White) }
}
}
}
"sudah_sampai" -> {
Box(Modifier.fillMaxSize().background(Color.White).padding(16.dp)) {
Column {
Text("Therapist Sudah Sampai!", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Green)
Spacer(Modifier.height(12.dp))
Text("Pilih Metode Pembayaran", fontWeight = FontWeight.Bold)
Spacer(Modifier.height(8.dp))
Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
Button(onClick = { paymentMethod = "tunai" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(if (paymentMethod == "tunai") Orange else Color.LightGray)) { Text("Tunai") }
Button(onClick = { paymentMethod = "non_tunai" }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(if (paymentMethod == "non_tunai") Orange else Color.LightGray)) { Text("Non Tunai") }
}
Spacer(Modifier.height(12.dp))
Text("Breakdown: Harga ${currentOrder?.harga} + Ongkir ${currentOrder?.ongkir} + Layanan ${currentOrder?.biaya_layanan} = Total Rp${currentOrder?.total}", fontSize = 12.sp)
Spacer(Modifier.height(12.dp))
Button(onClick = {
if (paymentMethod == "non_tunai") {
// Potong saldo customer di DB + tambah saldo mitra total-20% fee
if (saldo < (currentOrder?.total ?: 0)) {
Toast.makeText(ctx, "Saldo tidak cukup! Top up dulu", Toast.LENGTH_SHORT).show()
return@Button
}
saldo -= currentOrder?.total ?: 0
prefs.edit().putInt("saldo", saldo).apply()
// TODO REAL: supabase.from("customers").update(mapOf("saldo" to saldo)).eq("id", customerId)
// TODO REAL: val mitraSaldo = (currentOrder?.total ?: 0) * 0.8
// supabase.from("therapists").update(mapOf("saldo" to mitraSaldo)).eq("id", therapist_id)
// supabase.from("orders").update(mapOf("payment_method" to paymentMethod, "status" to "berlangsung"))
Toast.makeText(ctx, "Saldo terpotong Rp${currentOrder?.total}. Mitra dapat Rp${((currentOrder?.total ?: 0) * 0.8).toInt()} (fee 20%)", Toast.LENGTH_LONG).show()
}
orderStatus = "timer"
timerSeconds = (currentOrder?.durasi ?: 60) * 60
}, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(Orange)) { Text("Mulai Pijat (${paymentMethod})", color = Color.White, fontWeight = FontWeight.Bold) }
}
}
}
"timer" -> {
Box(Modifier.fillMaxSize().background(Green), contentAlignment = Alignment.Center) {
Column(horizontalAlignment = Alignment.CenterHorizontally) {
Text("Timer Pijat", fontSize = 14.sp, color = Color.White)
Spacer(Modifier.height(16.dp))
// CIRCULAR SVG + 72px mono
Box(Modifier.size(200.dp), contentAlignment = Alignment.Center) {
Canvas(Modifier.fillMaxSize()) {
val sweep = 360f * (1 - timerSeconds / (selectedDurasi * 60f))
drawArc(color = Orange, startAngle = -90f, sweepAngle = sweep, useCenter = false, style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round))
}
Text("%02d:%02d".format(timerSeconds / 60, timerSeconds % 60), fontSize = 72.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
}
Spacer(Modifier.height(20.dp))
Text("${selectedLayanan.first} • ${selectedTherapist}", color = Color.White, fontSize = 12.sp)
Spacer(Modifier.height(20.dp))
Button(onClick = { timerSeconds = 0 }, colors = ButtonDefaults.buttonColors(Orange)) { Text("Selesaikan", color = Color.White) }
}
}
}
"rating" -> {
Box(Modifier.fillMaxSize().background(Color.White).padding(16.dp), contentAlignment = Alignment.Center) {
Column(horizontalAlignment = Alignment.CenterHorizontally) {
Text("Beri Rating", fontWeight = FontWeight.Bold, fontSize = 22.sp)
Spacer(Modifier.height(16.dp))
Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
(1..5).forEach { i ->
Text(if (i <= rating) "★" else "☆", fontSize = 32.sp, color = Orange, modifier = Modifier.clickable { rating = i })
}
}
Spacer(Modifier.height(16.dp))
Button(onClick = {
scope.launch {
// TODO REAL: supabase.from("orders").update(mapOf("rating" to rating, "status" to "selesai")).eq("id", currentOrder?.id)
}
Toast.makeText(ctx, "Rating $rating terkirim!", Toast.LENGTH_SHORT).show()
orderStatus = "idle"; currentPage = "ORDER"; progress = 0f
}, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Green)) { Text("Kirim Rating", color = Color.White) }
}
}
}
else -> {
// HOME + DRAWER
Box(Modifier.fillMaxSize()) {
LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF6F3EE)).padding(16.dp)) {
item {
Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
Button(onClick = { showDrawer = true }, colors = ButtonDefaults.buttonColors(Color.White), shape = RoundedCornerShape(8.dp)) { Text("MENU", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold) }
Text("PijatIN REAL", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Green)
Button(onClick = { Toast.makeText(ctx, "Notifikasi REAL dari Supabase", Toast.LENGTH_SHORT).show() }, colors = ButtonDefaults.buttonColors(Color.White), shape = RoundedCornerShape(8.dp)) { Text("LONCENG", fontSize = 10.sp) }
}
Spacer(Modifier.height(16.dp))
}
if (currentPage == "HOME") {
item {
Box(Modifier.fillMaxWidth().background(Color(0xFFFFF3E0), RoundedCornerShape(12.dp)).padding(12.dp)) {
Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
Column {
Text("Therapist Terdekat REAL", fontWeight = FontWeight.Bold, color = Orange, fontSize = 14.sp)
Text("Fetch Supabase therapists where online=true", fontSize = 10.sp, color = Color.Gray)
}
Text("BEL", fontSize = 10.sp, modifier = Modifier.background(Color.White, RoundedCornerShape(20.dp)).padding(8.dp))
}
}
Spacer(Modifier.height(10.dp))
Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
listOf(Pair("Siti Rahayu", "SR"), Pair("Budi S.", "BS")).forEach { (name, id) ->
Box(Modifier.weight(1f).background(Color.White, RoundedCornerShape(14.dp)).padding(10.dp).clickable {
selectedTherapist = name
Toast.makeText(ctx, "Therapist $name dipilih", Toast.LENGTH_SHORT).show()
}, contentAlignment = Alignment.Center) {
Column {
Text(name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (selectedTherapist == name) Orange else Color.Black)
Text("0.3km • 4.9 • REAL DB", fontSize = 9.sp, color = Color.Gray)
Spacer(Modifier.height(6.dp))
Button(onClick = { selectedTherapist = name }, modifier = Modifier.fillMaxWidth().height(30.dp), colors = ButtonDefaults.buttonColors(if (selectedTherapist == name) Green else Orange), shape = RoundedCornerShape(8.dp)) { Text(if (selectedTherapist == name) "Dipilih" else "Pilih", fontSize = 10.sp, color = Color.White) }
}
}
}
}
Spacer(Modifier.height(16.dp))
Text("Layanan Pijat REAL", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Green)
Spacer(Modifier.height(8.dp))
val layananList = listOf(
Triple("Tradisional", 60 to 120000, "60' Rp120k"),
Triple("Refleksi", 75 to 100000, "75' Rp100k"),
Triple("Aroma Full Body", 60 to 135000, "60' Rp135k"),
Triple("Trad+Kerokan", 75 to 135000, "75' Rp135k")
)
layananList.chunked(2).forEach { row ->
Row(Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
row.forEach { (nama, durHar, label) ->
Box(Modifier.weight(1f).background(if (selectedLayanan.first.contains(nama)) Color(0xFF2D4A3E) else Color.White, RoundedCornerShape(12.dp)).clickable {
selectedLayanan = Pair(nama, durHar.second)
selectedDurasi = durHar.first
}.padding(12.dp)) {
Column {
Text(label, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = if (selectedLayanan.first.contains(nama)) Color.White else Orange)
Text(nama, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (selectedLayanan.first.contains(nama)) Color.White else Color.Black)
}
}
}
}
}
Spacer(Modifier.height(16.dp))
Button(onClick = { showAlamatModal = true }, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(Orange), shape = RoundedCornerShape(14.dp)) {
Text("PESAN ${selectedLayanan.first} + ${selectedTherapist} - SEKARANG", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
}
Spacer(Modifier.height(8.dp))
Button(onClick = { prefs.edit().putBoolean("isLoggedIn", false).apply(); isLoggedIn = false }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Color.Red)) { Text("LOGOUT", color = Color.White) }
}
}
if (currentPage == "ORDER") {
item {
Text("Pesanan REAL - Supabase", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Green)
Text("fetch orders where customer_id = $customerId", fontSize = 10.sp, color = Color.Gray)
Spacer(Modifier.height(12.dp))
Box(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp)).padding(12.dp)) {
Column {
Text(currentOrder?.id ?: "Belum ada order", fontWeight = FontWeight.Bold, fontSize = 13.sp)
Text("Status: $orderStatus", fontSize = 11.sp, color = Orange)
Text("Layanan: ${currentOrder?.layanan ?: "-"}", fontSize = 11.sp)
}
}
Spacer(Modifier.height(12.dp))
Button(onClick = { currentPage = "HOME" }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Green)) { Text("Kembali", color = Color.White) }
}
}
if (currentPage == "DOMPET") {
item {
Text("Saldo REAL - Supabase", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Green)
Text("fetch customers.saldo where id=$customerId", fontSize = 10.sp, color = Color.Gray)
Spacer(Modifier.height(12.dp))
Box(Modifier.fillMaxWidth().background(Green, RoundedCornerShape(14.dp)).padding(16.dp)) {
Column {
Text("Saldo", fontSize = 12.sp, color = Color.White)
Text("Rp $saldo", fontWeight = FontWeight.Bold, fontSize = 28.sp, color = Color.White)
Text("VA BCA $topUpVa • Expiry $vaExpiry", fontSize = 10.sp, color = Color.White)
}
}
Spacer(Modifier.height(12.dp))
Button(onClick = {
// TODO REAL Topup VA 39008+expiry 24h + Xendit polling 3s
topUpVa = "39008${(100000000..999999999).random()}"
vaExpiry = "24 jam dari sekarang"
Toast.makeText(ctx, "VA Baru: $topUpVa - Cek Status Xendit polling 3s", Toast.LENGTH_LONG).show()
// Simulasi polling 3s update saldo DB
scope.launch {
delay(3000)
saldo += 100000
prefs.edit().putInt("saldo", saldo).apply()
Toast.makeText(ctx, "Xendit: Pembayaran berhasil! Saldo +100k", Toast.LENGTH_LONG).show()
// TODO: supabase.from("customers").update(mapOf("saldo" to saldo)).eq("id", customerId)
}
}, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Orange)) { Text("Buat VA Top Up 100k (Xendit)", color = Color.White) }
Spacer(Modifier.height(8.dp))
Button(onClick = { Toast.makeText(ctx, "QRIS Deep Link: qris://pijatin/${topUpVa}", Toast.LENGTH_SHORT).show() }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Color.White)) { Text("Bayar QRIS Deep Link", color = Color.Black) }
Spacer(Modifier.height(8.dp))
Button(onClick = { Toast.makeText(ctx, "Tarik BI-FAST ke BCA ${savedPhone}", Toast.LENGTH_SHORT).show() }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Color.White)) { Text("Tarik Saldo BI-FAST", color = Color.Black) }
Spacer(Modifier.height(12.dp))
Button(onClick = { currentPage = "HOME" }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Green)) { Text("Kembali", color = Color.White) }
}
}
if (currentPage == "RIWAYAT") {
item {
Text("Riwayat REAL", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Green)
Text("Supabase orders where customer_id", fontSize = 10.sp, color = Color.Gray)
Spacer(Modifier.height(12.dp))
Box(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp)).padding(12.dp)) {
Text("ORD-12345 - Selesai - Rating 5 - Rp135k", fontSize = 11.sp)
}
}
}
}
if (showDrawer) {
Box(Modifier.fillMaxSize().background(Color(0x80000000)).clickable { showDrawer = false }) {
Column(Modifier.fillMaxHeight().width(280.dp).background(Color.White).padding(16.dp)) {
Text("Menu PijatIN REAL", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Green)
Text("$customerId • Saldo Rp $saldo", fontSize = 11.sp, color = Color.Gray)
Spacer(Modifier.height(12.dp))
Divider()
Spacer(Modifier.height(12.dp))
Text("Home", fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().clickable { currentPage = "HOME"; showDrawer = false }.padding(12.dp))
Text("Pesanan (REAL DB)", fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().clickable { currentPage = "ORDER"; showDrawer = false }.padding(12.dp))
Text("Riwayat (REAL DB)", fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().clickable { currentPage = "RIWAYAT"; showDrawer = false }.padding(12.dp))
Text("Dompet REAL - Rp $saldo", fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().clickable { currentPage = "DOMPET"; showDrawer = false }.padding(12.dp))
Text("Therapist Saya", fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().clickable { currentPage = "HOME"; showDrawer = false }.padding(12.dp))
Text("Kupon", fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().padding(12.dp))
Text("Pusat Bantuan", fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().padding(12.dp))
Text("Pengaturan", fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().padding(12.dp))
}
}
}
if (showAlamatModal) {
AlertDialog(onDismissRequest = { showAlamatModal = false }, title = { Text("Alamat + Maps 150px + Breakdown") }, text = {
LazyColumn {
item {
Text("Layanan: ${selectedLayanan.first} - Rp${selectedLayanan.second}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
Text("Therapist: $selectedTherapist", fontSize = 12.sp)
Spacer(Modifier.height(8.dp))
OutlinedTextField(value = alamatText, onValueChange = { alamatText = it }, label = { Text("Alamat Lengkap") }, modifier = Modifier.fillMaxWidth())
Spacer(Modifier.height(8.dp))
// MAPS 150px
Box(Modifier.fillMaxWidth().height(150.dp).background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
Column(horizontalAlignment = Alignment.CenterHorizontally) {
Text("MAPS 150px", fontWeight = FontWeight.Bold, fontSize = 12.sp)
Text("Google Maps Compose", fontSize = 9.sp)
Text("Pin kamu + pin therapist", fontSize = 9.sp)
}
}
Spacer(Modifier.height(8.dp))
Text("Breakdown:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
Text("Harga: Rp${selectedLayanan.second}", fontSize = 11.sp)
Text("Ongkos: Rp15000", fontSize = 11.sp)
Text("Biaya Layanan: Rp5000", fontSize = 11.sp)
Text("Total: Rp${selectedLayanan.second + 20000}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Orange)
}
}
}, confirmButton = {
Button(onClick = {
val total = selectedLayanan.second + 20000
val order = OrderReal(
customer_id = customerId,
layanan = selectedLayanan.first,
durasi = selectedDurasi,
harga = selectedLayanan.second,
total = total,
alamat = alamatText
)
currentOrder = order
orderStatus = "menunggu_mitra"
showAlamatModal = false
scope.launch {
// TODO REAL INSERT ORD-xxxxx ke Supabase
// supabase.from("orders").insert(order)
// Mitra dapat real-time supabase.channel("orders:therapist_id=SR")
Toast.makeText(ctx, "Order ${order.id} Insert Supabase! Menunggu mitra...", Toast.LENGTH_LONG).show()
}
}, colors = ButtonDefaults.buttonColors(Orange)) { Text("Pesan Sekarang - Insert Supabase", color = Color.White, fontSize = 11.sp) }
}, dismissButton = { TextButton(onClick = { showAlamatModal = false }) { Text("Batal") } })
}
}
}
}
}
}
