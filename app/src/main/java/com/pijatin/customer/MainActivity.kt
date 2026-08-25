package com.pijatin.customer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
val Green = Color(0xFF2D4A3E)
val Orange = Color(0xFFFF7A00)
class MainActivity : ComponentActivity(){
override fun onCreate(b:Bundle?){
super.onCreate(b)
setContent{
val ctx=LocalContext.current
val prefs=ctx.getSharedPreferences("PijatIN_Login",0)
var isLoggedIn by remember{mutableStateOf(prefs.getBoolean("isLoggedIn",false))}
var savedPhone by remember{mutableStateOf(prefs.getString("phone","")?:"")}
var savedEmail by remember{mutableStateOf(prefs.getString("email","")?:"")}
if(isLoggedIn){
LazyColumn(Modifier.fillMaxSize().background(Color.White).padding(16.dp)){
item{
Text("🏠 Beranda PijatIN", fontSize=24.sp, fontWeight=FontWeight.Bold, color=Green)
Spacer(Modifier.height(8.dp))
Text("Login sebagai: "+savedPhone, fontSize=14.sp, fontWeight=FontWeight.Bold)
Text(savedEmail, fontSize=11.sp, color=Color.Gray)
Spacer(Modifier.height(12.dp))
Box(Modifier.fillMaxWidth().background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp)).padding(14.dp)){
Column{
Text("✅ Data tersimpan & login otomatis!", color=Color(0xFF2E7D32), fontWeight=FontWeight.Bold, fontSize=13.sp)
Text("Kamu sudah login otomatis setelah daftar!", fontSize=11.sp, color=Color(0xFF1B5E20))
}
}
Spacer(Modifier.height(20.dp))
Text("💆 Layanan PijatIN - Harga Baru", fontWeight=FontWeight.Bold, fontSize=16.sp, color=Green)
Spacer(Modifier.height(12.dp))
// 1. Pijat Tradisional 120k/60 menit
Box(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp)).padding(14.dp)){
Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){
Column{
Text("💆‍♀️ Pijat Tradisional", fontWeight=FontWeight.Bold, fontSize=14.sp)
Text("⏱️ 60 menit", fontSize=11.sp, color=Color.Gray)
}
Column(horizontalAlignment=Alignment.End){
Text("Rp 120k", color=Orange, fontWeight=FontWeight.Bold, fontSize=16.sp)
Text("/60 menit", fontSize=10.sp, color=Color.Gray)
}
}
}
Spacer(Modifier.height(10.dp))
// 2. Pijat Refleksi 100k/75 menit
Box(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp)).padding(14.dp)){
Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){
Column{
Text("🦶 Pijat Refleksi", fontWeight=FontWeight.Bold, fontSize=14.sp)
Text("⏱️ 75 menit", fontSize=11.sp, color=Color.Gray)
}
Column(horizontalAlignment=Alignment.End){
Text("Rp 100k", color=Orange, fontWeight=FontWeight.Bold, fontSize=16.sp)
Text("/75 menit", fontSize=10.sp, color=Color.Gray)
}
}
}
Spacer(Modifier.height(10.dp))
// 3. Pijat Aromatherapy Full Body 135k/60 menit
Box(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp)).padding(14.dp)){
Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){
Column{
Text("🌸 Aromatherapy Full Body", fontWeight=FontWeight.Bold, fontSize=14.sp)
Text("⏱️ 60 menit", fontSize=11.sp, color=Color.Gray)
}
Column(horizontalAlignment=Alignment.End){
Text("Rp 135k", color=Orange, fontWeight=FontWeight.Bold, fontSize=16.sp)
Text("/60 menit", fontSize=10.sp, color=Color.Gray)
}
}
}
Spacer(Modifier.height(10.dp))
// 4. Pijat Tradisional+Kerokan 135k/75 menit
Box(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(12.dp)).padding(14.dp)){
Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){
Column{
Text("💆‍♂️ Tradisional + Kerokan", fontWeight=FontWeight.Bold, fontSize=14.sp)
Text("⏱️ 75 menit", fontSize=11.sp, color=Color.Gray)
}
Column(horizontalAlignment=Alignment.End){
Text("Rp 135k", color=Orange, fontWeight=FontWeight.Bold, fontSize=16.sp)
Text("/75 menit", fontSize=10.sp, color=Color.Gray)
}
}
}
Spacer(Modifier.height(20.dp))
Button(onClick={Toast.makeText(ctx,"Pesan: Tradisional 120k/60m, Refleksi 100k/75m, Aroma Full Body 135k/60m, Tradisional+Kerokan 135k/75m",Toast.LENGTH_LONG).show()}, modifier=Modifier.fillMaxWidth().height(56.dp), colors=ButtonDefaults.buttonColors(Orange), shape=RoundedCornerShape(14.dp)){Text("🔍 PESAN PIJAT SEKARANG", color=Color.White, fontWeight=FontWeight.Bold)}
Spacer(Modifier.height(12.dp))
Button(onClick={prefs.edit().putBoolean("isLoggedIn",false).apply(); isLoggedIn=false}, modifier=Modifier.fillMaxWidth().height(50.dp), colors=ButtonDefaults.buttonColors(Color.Red), shape=RoundedCornerShape(12.dp)){Text("LOGOUT", color=Color.White, fontWeight=FontWeight.Bold)}
Spacer(Modifier.height(16.dp))
Text("✅ V136 HIJAU - Harga: Tradisional 120k/60m, Refleksi 100k/75m, Aroma Full Body 135k/60m, Tradisional+Kerokan 135k/75m", fontSize=9.sp, color=Color(0xFF4CAF50), fontWeight=FontWeight.Bold)
}
}
}else{
var hp by remember{mutableStateOf("083893330346")}
var email by remember{mutableStateOf("malikysyachmal2018@gmail.com")}
var p1 by remember{mutableStateOf("")}
var p2 by remember{mutableStateOf("")}
val focusManager=LocalFocusManager.current
