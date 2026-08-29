package com.pijatin.customer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
val Green = Color(0xFF2D4A3E)
val Orange = Color(0xFFFF7A00)
class MainActivity : ComponentActivity() {
override fun onCreate(b: Bundle?) {
super.onCreate(b)
setContent {
val ctx = LocalContext.current
val prefs = ctx.getSharedPreferences("PijatIN_Login", 0)
var showSplash by remember { mutableStateOf(true) }
var isLoggedIn by remember { mutableStateOf(prefs.getBoolean("isLoggedIn", false)) }
var savedPhone by remember { mutableStateOf(prefs.getString("phone", "") ?: "") }
var showDrawer by remember { mutableStateOf(false) }
var currentPage by remember { mutableStateOf("HOME") }
var saldo by remember { mutableStateOf(prefs.getInt("saldo", 50000)) }
var showTopUp by remember { mutableStateOf(false) }
LaunchedEffect(Unit) { delay(1200); showSplash = false }
if (showSplash) {
Box(Modifier.fillMaxSize().background(Orange), contentAlignment = Alignment.Center) {
Column(horizontalAlignment = Alignment.CenterHorizontally) {
Text("PijatIN", fontWeight = FontWeight.Bold, fontSize = 30.sp, color = Color.White)
Text("Pijat Profesional ke Rumah", fontSize = 12.sp, color = Color.White)
}
}
} else if (isLoggedIn) {
Box(Modifier.fillMaxSize()) {
LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF6F3EE)).padding(16.dp)) {
item {
Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
Button(onClick = { showDrawer = true }, colors = ButtonDefaults.buttonColors(Color.White), shape = RoundedCornerShape(8.dp)) { Text("MENU", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold) }
Text("PijatIN", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Green)
Button(onClick = { Toast.makeText(ctx, "Notifikasi 3 promo", Toast.LENGTH_SHORT).show() }, colors = ButtonDefaults.buttonColors(Color.White), shape = RoundedCornerShape(8.dp)) { Text("LONCENG", fontSize = 10.sp, color = Color.Black) }
}
Spacer(Modifier.height(16.dp))
}
item {
if (currentPage == "HOME") {
Box(Modifier.fillMaxWidth().background(Color(0xFFFFF3E0), RoundedCornerShape(12.dp)).padding(12.dp)) {
Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
Column {
Text("Therapist Terdekat", fontWeight = FontWeight.Bold, color = Orange, fontSize = 14.sp)
Text("Online & siap ke lokasi kamu", fontSize = 11.sp, color = Color(0xFF2E7D32))
}
Box(Modifier.background(Color.White, RoundedCornerShape(20.dp)).clickable { Toast.makeText(ctx, "2 therapist baru", Toast.LENGTH_SHORT).show() }.padding(horizontal = 12.dp, vertical = 6.dp)) { Text("BEL", fontSize = 10.sp) }
}
}
Spacer(Modifier.height(10.dp))
Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
Box(Modifier.weight(1f).background(Color.White, RoundedCornerShape(14.dp)).padding(10.dp)) {
Column {
Text("SR Siti Rahayu", fontWeight = FontWeight.Bold, fontSize = 12.sp)
Text("0.3km - 342 job - 4.9", fontSize = 9.sp, color = Color.Gray)
Spacer(Modifier.height(6.dp))
Button(onClick = { Toast.makeText(ctx, "Order Siti 120k", Toast.LENGTH_SHORT).show() }, modifier = Modifier.fillMaxWidth().height(36.dp), colors = ButtonDefaults.buttonColors(Orange), shape = RoundedCornerShape(12.dp)) { Text("Order Ini", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold) }
}
}
Box(Modifier.weight(1f).background(Color.White, RoundedCornerShape(14.dp)).padding(10.dp)) {
Column {
Text("BS Budi S.", fontWeight = FontWeight.Bold, fontSize = 12.sp)
Text("0.6km - 128 job - 4.8", fontSize = 9.sp, color = Color.Gray)
Spacer(Modifier.height(6.dp))
Button(onClick = { Toast.makeText(ctx, "Order Budi", Toast.LENGTH_SHORT).show() }, modifier = Modifier.fillMaxWidth().height(36.dp), colors = ButtonDefaults.buttonColors(Orange), shape = RoundedCornerShape(12.dp)) { Text("Order Ini", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold) }
}
}
}
Spacer(Modifier.height(20.dp))
Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
Text("Layanan Pijat", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Green)
Text("Ongkos 15k + 5k", fontSize = 11.sp, color = Color.Gray)
}
Spacer(Modifier.height(12.dp))
Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
Box(Modifier.weight(1f).background(Color.White, RoundedCornerShape(14.dp)).padding(12.dp)) {
Column {
Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
Text("60'", fontWeight = FontWeight.Bold, fontSize = 11.sp)
Text("Rp120k", color = Orange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
}
Text("Tradisional", fontWeight = FontWeight.Bold, fontSize = 12.sp)
Text("60 menit", fontSize = 9.sp, color = Color.Gray)
}
}
Box(Modifier.weight(1f).background(Color.White, RoundedCornerShape(14.dp)).padding(12.dp)) {
Column {
Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
Text("75'", fontWeight = FontWeight.Bold, fontSize = 11.sp)
Text("Rp100k", color = Orange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
}
Text("Refleksi", fontWeight = FontWeight.Bold, fontSize = 12.sp)
Text("75 menit", fontSize = 9.sp, color = Color.Gray)
}
}
}
Spacer(Modifier.height(10.dp))
Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
Box(Modifier.weight(1f).background(Color.White, RoundedCornerShape(14.dp)).padding(12.dp)) {
Column {
Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
Text("60'", fontWeight = FontWeight.Bold, fontSize = 11.sp)
Text("Rp135k", color = Orange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
}
Text("Aroma Full", fontWeight = FontWeight.Bold, fontSize = 11.sp)
Text("60 menit", fontSize = 9.sp, color = Color.Gray)
}
}
Box(Modifier.weight(1f).background(Color.White, RoundedCornerShape(14.dp)).padding(12.dp)) {
Column {
Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
Text("75'", fontWeight = FontWeight.Bold, fontSize = 11.sp)
Text("Rp135k", color = Orange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
}
Text("Trad+Kerokan", fontWeight = FontWeight.Bold, fontSize = 11.sp)
Text("75 menit", fontSize = 9.sp, color = Color.Gray)
}
}
}
Spacer(Modifier.height(24.dp))
Button(onClick = { Toast.makeText(ctx, "PESAN 120k 100k 135k", Toast.LENGTH_LONG).show() }, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(Orange), shape = RoundedCornerShape(14.dp)) { Text("PESAN PIJAT SEKARANG", color = Color.White, fontWeight = FontWeight.Bold) }
Spacer(Modifier.height(12.dp))
Button(onClick = { prefs.edit().putBoolean("isLoggedIn", false).apply(); isLoggedIn = false }, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(Color.Red), shape = RoundedCornerShape(12.dp)) { Text("LOGOUT", color = Color.White, fontWeight = FontWeight.Bold) }
}
if (currentPage == "ORDER") {
Text("Order Saya", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Green)
Spacer(Modifier.height(12.dp))
Box(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp)).padding(12.dp)) {
Column {
Text("Order #PIJ-2026-001", fontWeight = FontWeight.Bold, fontSize = 13.sp)
Text("Tradisional 60m - Siti Rahayu", fontSize = 11.sp, color = Color.Gray)
Box(Modifier.background(Color(0xFF4CAF50), RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 2.dp)) { Text("Selesai", fontSize = 10.sp, color = Color.White) }
}
}
Spacer(Modifier.height(8.dp))
Box(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp)).padding(12.dp)) {
Column {
Text("Order #PIJ-2026-002", fontWeight = FontWeight.Bold, fontSize = 13.sp)
Text("Refleksi 75m - Budi S.", fontSize = 11.sp, color = Color.Gray)
Box(Modifier.background(Orange, RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 2.dp)) { Text("Proses", fontSize = 10.sp, color = Color.White) }
}
}
Spacer(Modifier.height(16.dp))
Button(onClick = { currentPage = "HOME" }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Green)) { Text("Kembali ke Home", color = Color.White) }
}
if (currentPage == "DOMPET") {
Text("Dompet", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Green)
Spacer(Modifier.height(12.dp))
Box(Modifier.fillMaxWidth().background(Green, RoundedCornerShape(14.dp)).padding(16.dp)) {
Column {
Text("Saldo PijatIN", fontSize = 12.sp, color = Color.White)
Text("Rp $saldo", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.White)
}
}
Spacer(Modifier.height(12.dp))
Button(onClick = { showTopUp = true }, modifier = Modifier.fillMaxWidth().height(48.dp), colors = ButtonDefaults.buttonColors(Orange), shape = RoundedCornerShape(12.dp)) { Text("Top Up Saldo", color = Color.White, fontWeight = FontWeight.Bold) }
Spacer(Modifier.height(12.dp))
Box(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp)).padding(12.dp)) { Text("- Rp120.000 Tradisional 60m - 10 Jan 2026", fontSize = 11.sp) }
Spacer(Modifier.height(6.dp))
Box(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp)).padding(12.dp)) { Text("+ Rp100.000 Top Up - 9 Jan 2026", fontSize = 11.sp) }
Spacer(Modifier.height(16.dp))
Button(onClick = { currentPage = "HOME" }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Green)) { Text("Kembali ke Home", color = Color.White) }
}
if (currentPage == "THERAPIST") {
Text("Therapist Saya", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Green)
Spacer(Modifier.height(12.dp))
Box(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp)).padding(12.dp)) { Text("SR Siti Rahayu - Favorit 5x order", fontSize = 13.sp) }
Spacer(Modifier.height(8.dp))
Box(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp)).padding(12.dp)) { Text("BS Budi Santoso - 2x order", fontSize = 13.sp) }
Spacer(Modifier.height(16.dp))
Button(onClick = { currentPage = "HOME" }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Green)) { Text("Kembali", color = Color.White) }
}
if (currentPage == "KUPON") {
Text("Kupon Saya", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Green)
Spacer(Modifier.height(12.dp))
Box(Modifier.fillMaxWidth().background(Color(0xFFFFF3E0), RoundedCornerShape(12.dp)).padding(12.dp)) {
Column {
Text("PIJAT20", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Orange)
Text("Diskon 20% max Rp20.000", fontSize = 11.sp)
}
}
Spacer(Modifier.height(16.dp))
Button(onClick = { currentPage = "HOME" }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Green)) { Text("Kembali", color = Color.White) }
}
if (currentPage == "BANTUAN") {
Text("Pusat Bantuan", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Green)
Spacer(Modifier.height(12.dp))
Box(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp)).padding(12.dp)) { Text("WA CS: 083893330346", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
Spacer(Modifier.height(8.dp))
Box(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp)).padding(12.dp)) { Text("Chat CS 24 jam untuk bantuan order, pembayaran, komplain", fontSize = 11.sp, color = Color.Gray) }
Spacer(Modifier.height(16.dp))
Button(onClick = { currentPage = "HOME" }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Green)) { Text("Kembali", color = Color.White) }
}
if (currentPage == "PENGATURAN") {
Text("Pengaturan", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Green)
Spacer(Modifier.height(12.dp))
Box(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp)).padding(12.dp)) { Text("Akun: $savedPhone", fontSize = 13.sp) }
Spacer(Modifier.height(16.dp))
Button(onClick = { currentPage = "HOME" }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Green)) { Text("Kembali", color = Color.White) }
}
Spacer(Modifier.height(16.dp))
}
}
if (showDrawer) {
Box(Modifier.fillMaxSize().background(Color(0x80000000)).clickable { showDrawer = false }) {
Column(Modifier.fillMaxHeight().width(280.dp).background(Color.White).padding(16.dp)) {
Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
Text("Menu PijatIN", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Green)
Button(onClick = { showDrawer = false }, colors = ButtonDefaults.buttonColors(Color.LightGray), shape = RoundedCornerShape(8.dp)) { Text("X", fontSize = 12.sp, color = Color.Black) }
}
Spacer(Modifier.height(12.dp))
Text("Saldo Rp $saldo", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Orange)
Spacer(Modifier.height(16.dp))
Divider()
Spacer(Modifier.height(16.dp))
Text("Order", fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().clickable { currentPage = "ORDER"; showDrawer = false }.padding(12.dp))
Text("Dompet", fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().clickable { currentPage = "DOMPET"; showDrawer = false }.padding(12.dp))
Text("Therapist Saya", fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().clickable { currentPage = "THERAPIST"; showDrawer = false }.padding(12.dp))
Text("Kupon", fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().clickable { currentPage = "KUPON"; showDrawer = false }.padding(12.dp))
Text("Pusat Bantuan", fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().clickable { currentPage = "BANTUAN"; showDrawer = false }.padding(12.dp))
Text("Pengaturan", fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().clickable { currentPage = "PENGATURAN"; showDrawer = false }.padding(12.dp))
}
}
}
if (showTopUp) {
AlertDialog(onDismissRequest = { showTopUp = false }, title = { Text("Top Up Dompet") }, text = {
Column {
Button(onClick = { saldo += 50000; prefs.edit().putInt("saldo", saldo).apply(); showTopUp = false; Toast.makeText(ctx, "Top Up 50k Berhasil Saldo $saldo", Toast.LENGTH_SHORT).show() }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Green)) { Text("Rp50.000 BCA") }
Spacer(Modifier.height(8.dp))
Button(onClick = { saldo += 100000; prefs.edit().putInt("saldo", saldo).apply(); showTopUp = false; Toast.makeText(ctx, "Top Up 100k Saldo $saldo", Toast.LENGTH_SHORT).show() }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Green)) { Text("Rp100.000 BCA") }
Spacer(Modifier.height(8.dp))
Button(onClick = { saldo += 200000; prefs.edit().putInt("saldo", saldo).apply(); showTopUp = false }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Orange)) { Text("Rp200.000 BCA") }
}
}, confirmButton = { TextButton(onClick = { showTopUp = false }) { Text("Tutup") } })
}
}
} else {
var hp by remember { mutableStateOf("083893330346") }
var email by remember { mutableStateOf("malikysyachmal2018@gmail.com") }
var p1 by remember { mutableStateOf("") }
var p2 by remember { mutableStateOf("") }
val focusManager = LocalFocusManager.current
val keyboardController = LocalSoftwareKeyboardController.current
val isFormValid = hp.length >= 10 && email.contains("@") && p1.length >= 6 && p1 == p2
Scaffold(bottomBar = {
Column(Modifier.fillMaxWidth().background(Color.White).padding(16.dp).imePadding()) {
Button(onClick = {
if (!isFormValid) return@Button
prefs.edit().apply { putBoolean("isLoggedIn", true); putString("phone", hp); apply() }
savedPhone = hp
isLoggedIn = true
keyboardController?.hide()
}, modifier = Modifier.fillMaxWidth().height(56.dp), enabled = isFormValid, colors = ButtonDefaults.buttonColors(containerColor = Green, disabledContainerColor = Color.Gray), shape = RoundedCornerShape(14.dp)) {
Text("DAFTAR SEKARANG", color = Color.White, fontWeight = FontWeight.Bold)
}
}
}) { padding ->
LazyColumn(Modifier.fillMaxSize().background(Color.White).padding(padding).padding(16.dp).imePadding()) {
item {
Spacer(Modifier.height(20.dp))
Text("Selamat Datang", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Green)
Spacer(Modifier.height(32.dp))
OutlinedTextField(value = hp, onValueChange = { hp = it }, label = { Text("No Telepon") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next), keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }), singleLine = true)
Spacer(Modifier.height(12.dp))
OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next), keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }), singleLine = true)
Spacer(Modifier.height(12.dp))
OutlinedTextField(value = p1, onValueChange = { p1 = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next), keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }), singleLine = true)
Spacer(Modifier.height(12.dp))
OutlinedTextField(value = p2, onValueChange = { p2 = it }, label = { Text("Konfirmasi") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done), keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(); keyboardController?.hide() }), singleLine = true)
Spacer(Modifier.height(100.dp))
}
}
}
}
}
}
}
