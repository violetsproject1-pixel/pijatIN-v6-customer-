package com.pijatin.customer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
var hp by remember{mutableStateOf("083893330346")}
var email by remember{mutableStateOf("malikysyachmal2018@gmail.com")}
var p1 by remember{mutableStateOf("")}
var p2 by remember{mutableStateOf("")}
MaterialTheme{
LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(20.dp)){
item{
Text("Daftar #131 SUPER MINIMAL", fontWeight=FontWeight.Bold, fontSize=24.sp, color=Green)
Text("HAPUS NAMA ALAMAT GPS BANK EWALLET FOTO KTP", fontSize=10.sp, color=Orange, fontWeight=FontWeight.Bold)
Spacer(Modifier.height(30.dp))
OutlinedTextField(hp,{hp=it}, label={Text("No Telepon *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp))
Spacer(Modifier.height(14.dp))
OutlinedTextField(email,{email=it}, label={Text("Email *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp))
Spacer(Modifier.height(14.dp))
OutlinedTextField(p1,{p1=it}, label={Text("Password *")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp))
Spacer(Modifier.height(14.dp))
OutlinedTextField(p2,{p2=it}, label={Text("Konfirmasi Password *")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp))
if(p2.isNotEmpty()){
Spacer(Modifier.height(8.dp))
Text(if(p1==p2)"✅ Password cocok"else"❌ Tidak sama", color=if(p1==p2)Color(0xFF4CAF50)else Color.Red, fontSize=12.sp, fontWeight=FontWeight.Bold)
}
Spacer(Modifier.height(32.dp))
Button(onClick={
if(hp.isEmpty()||email.isEmpty()){Toast.makeText(ctx,"Isi HP & Email",Toast.LENGTH_SHORT).show();return@Button}
if(p1!=p2||p1.length<6){Toast.makeText(ctx,"Password min 6 & sama",Toast.LENGTH_SHORT).show();return@Button}
Toast.makeText(ctx,"DAFTAR OK: "+hp,Toast.LENGTH_LONG).show()
}, modifier=Modifier.fillMaxWidth().height(56.dp), colors=ButtonDefaults.buttonColors(Green), shape=RoundedCornerShape(14.dp)){
Text("DAFTAR SEKARANG ✅", color=Color.White, fontWeight=FontWeight.Bold, fontSize=16.sp)
}
Spacer(Modifier.height(20.dp))
Text("✅ DIHAPUS: Nama Lengkap maliki, Alamat GG cemara2, Kota, GPS +-3m, Maps, Bank BCA MANDIRI BRI SEABANK, E-Wallet DANA OVO GOPAY, Foto Galeri, Kamera, KTP 200px, KTP Galeri", fontSize=8.sp, color=Color(0xFF4CAF50), fontWeight=FontWeight.Bold)
Spacer(Modifier.height(8.dp))
Text("📱 TINGGAL: No Telepon + Email + Password = 15 DETIK DAFTAR!", fontSize=11.sp, color=Green, fontWeight=FontWeight.Bold)
}
}
}
}
}
}
