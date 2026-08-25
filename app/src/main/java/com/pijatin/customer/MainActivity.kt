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
data class Layanan(val emoji:String, val nama:String, val harga:String, val durasi:String)
class MainActivity : ComponentActivity(){
override fun onCreate(b:Bundle?){
super.onCreate(b)
setContent{
val ctx=LocalContext.current
val prefs=ctx.getSharedPreferences("PijatIN_Login",0)
var isLoggedIn by remember{mutableStateOf(prefs.getBoolean("isLoggedIn",false))}
var savedPhone by remember{mutableStateOf(prefs.getString("phone","")?:"")}
var savedEmail by remember{mutableStateOf(prefs.getString("email","")?:"")}
val daftarLayanan = listOf(
Layanan("💆‍♀️","Pijat Tradisional","Rp 120k","60 menit"),
Layanan("🦶","Pijat Refleksi","Rp 100k","75 menit"),
Layanan("🌸","Pijat Aromatherapy Full Body","Rp 135k","60 menit"),
Layanan("💆‍♂️","Pijat Tradisional + Kerokan","Rp 135k","75 menit")
)
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
Text("💆 Layanan PijatIN - Update Harga Baru", fontWeight=FontWeight.Bold, fontSize=16.sp, color=Green)
Spacer(Modifier.height(12.dp))
}
items(daftarLayanan.size){ i ->
val lay = daftarLayanan[i]
Card(Modifier.fillMaxWidth().padding(bottom=10.dp).background(Color.White), shape=RoundedCornerShape(14.dp), colors=CardDefaults.cardColors(Color.White), elevation=CardDefaults.cardElevation(2.dp)){
Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement=Arrangement.SpaceBetween){
