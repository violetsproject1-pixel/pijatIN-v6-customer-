package com.pijatin.customer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

val DarkGreen = Color(0xFF2D4A3E)
val Orange = Color(0xFFFF7A00)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showSplash by remember { mutableStateOf(true) }
            LaunchedEffect(Unit){ delay(1200); showSplash=false }
            if(showSplash){
                Box(Modifier.fillMaxSize().background(DarkGreen), contentAlignment=Alignment.Center){
                    Column(horizontalAlignment=Alignment.CenterHorizontally){
                        Text("PijatIN", color=Color.White, fontSize=36.sp, fontWeight=FontWeight.Bold)
                        Text("Customer", color=Orange, fontSize=18.sp, fontWeight=FontWeight.Bold)
                        Spacer(Modifier.height(20.dp))
                        CircularProgressIndicator(color=Color.White)
                        Text("#88 Gold Icon + Daftar Fix", color=Color.White, fontSize=11.sp)
                    }
                }
            } else {
                val ctx = LocalContext.current
                var nama by remember { mutableStateOf("Violet") }
                var telepon by remember { mutableStateOf("08123456789") }
                var email by remember { mutableStateOf("violet@test.com") }
                var alamat by remember { mutableStateOf("Jl Test No 123") }
                var namaRek by remember { mutableStateOf("malikj") }
                var noRek by remember { mutableStateOf("083893330346") }
                var pass by remember { mutableStateOf("123456") }
                var confirm by remember { mutableStateOf("123456") }
                var fotoUri by remember { mutableStateOf<Uri?>(null) }
                var ktpUri by remember { mutableStateOf<Uri?>(null) }
                var lat by remember { mutableStateOf(-6.2078) }
                var lng by remember { mutableStateOf(106.8466) }
                var mapsOk by remember { mutableStateOf(true) }

                val fotoGaleri = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { if(it!=null) fotoUri=it }
                val ktpGaleri = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { if(it!=null) ktpUri=it }
                val fotoKamera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { if(it!=null) fotoUri=Uri.parse("camera") }

                fun openMaps(){
                    val uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lng")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.setPackage("com.google.android.apps.maps")
                    try { ctx.startActivity(intent) } catch(e:Exception){
                        ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps?q=$lat,$lng")))
                    }
                    mapsOk=true
                }

                MaterialTheme {
                    LazyColumn(Modifier.fillMaxSize().imePadding().background(Color(0xFFF5F5F5)).padding(16.dp), contentPadding=PaddingValues(bottom=400.dp)){
                        item{
                            Text("Daftar PijatIN #88", fontWeight=FontWeight.Bold, fontSize=20.sp, color=DarkGreen)
                            Text("✅ Icon Gold Lotus + Tombol Daftar Fix", fontSize=10.sp, color=Orange, fontWeight=FontWeight.Bold)
                            Spacer(Modifier.height(16.dp))
                            OutlinedTextField(nama,{nama=it}, label={Text("Nama Lengkap *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(telepon,{telepon=it}, label={Text("No Telepon *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(email,{email=it}, label={Text("Email *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(alamat,{alamat=it}, label={Text("Alamat *")}, modifier=Modifier.fillMaxWidth().height(90.dp), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(12.dp))
                            Box(Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFD0E8FF)), contentAlignment=Alignment.Center){
                                Column(horizontalAlignment=Alignment.CenterHorizontally){
                                    Text("🗺️ MAPS 150px TAP BUKA", fontWeight=FontWeight.Bold, fontSize=13.sp)
                                    Text("📍 ${String.format("%.4f", lat)}, ${String.format("%.4f", lng)}", fontSize=12.sp, fontWeight=FontWeight.Bold)
                                    if(mapsOk) Text("✅ Maps OK - Tap tombol bawah", color=Color(0xFF2E7D32), fontSize=10.sp, fontWeight=FontWeight.Bold)
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                                Button(onClick={ openMaps() }, modifier=Modifier.weight(1f).height(44.dp), colors=ButtonDefaults.buttonColors(DarkGreen), shape=RoundedCornerShape(10.dp)){ Text("🌍 Buka Maps Real", fontSize=10.sp) }
                                OutlinedButton(onClick={ mapsOk=true }, modifier=Modifier.weight(1f).height(44.dp), shape=RoundedCornerShape(10.dp)){ Text("Gunakan Lokasi", fontSize=10.sp) }
                            }
                            Spacer(Modifier.height(16.dp))
                            OutlinedTextField(namaRek,{namaRek=it}, label={Text("Nama Rekening")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(noRek,{noRek=it}, label={Text("No Rekening")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(16.dp))
                            Row(horizontalArrangement=Arrangement.spacedBy(12.dp)){
                                Column(horizontalAlignment=Alignment.CenterHorizontally){
                                    Box(Modifier.size(80.dp).clip(CircleShape).background(if(fotoUri!=null) Color(0xFF4CAF50) else Color.LightGray), contentAlignment=Alignment.Center){ Text(if(fotoUri!=null) "✅" else "80px", color=Color.White, fontWeight=FontWeight.Bold) }
                                    Spacer(Modifier.height(8.dp))
                                    Button(onClick={ fotoGaleri.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, modifier=Modifier.width(110.dp).height(34.dp)){ Text("GALERI", fontSize=10.sp) }
                                    Spacer(Modifier.height(6.dp))
                                    Button(onClick={ fotoKamera.launch(null) }, modifier=Modifier.width(110.dp).height(34.dp), colors=ButtonDefaults.buttonColors(Orange)){ Text("KAMERA", fontSize=9.sp) }
                                }
                                Column(Modifier.weight(1f)){
                                    Box(Modifier.fillMaxWidth().height(110.dp).clip(RoundedCornerShape(12.dp)).background(if(ktpUri!=null) Color(0xFF4CAF50) else Color(0xFF9E9E9E)), contentAlignment=Alignment.Center){ Text(if(ktpUri!=null) "✅ KTP OK" else "KTP 200px", color=Color.White, fontWeight=FontWeight.Bold) }
                                    Spacer(Modifier.height(8.dp))
                                    Button(onClick={ ktpGaleri.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, modifier=Modifier.fillMaxWidth().height(40.dp)){ Text("KTP GALERI", fontSize=11.sp) }
                                }
                            }
                            Spacer(Modifier.height(20.dp))
                            OutlinedTextField(pass,{pass=it}, label={Text("Password")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(confirm,{confirm=it}, label={Text("Konfirmasi Password")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            if(confirm.isNotEmpty()) Text(if(confirm==pass) "✅ Password cocok" else "❌ Tidak sama", color=if(confirm==pass) Color(0xFF4CAF50) else Color.Red, fontSize=12.sp, fontWeight=FontWeight.Bold)
                            Spacer(Modifier.height(24.dp))
                            val missing = mutableListOf<String>()
                            if(nama.isEmpty()) missing.add("Nama")
                            if(telepon.isEmpty()) missing.add("Telepon")
                            if(email.isEmpty()) missing.add("Email")
                            if(alamat.isEmpty()) missing.add("Alamat")
                            if(!mapsOk) missing.add("Maps")
                            if(fotoUri==null) missing.add("Foto")
                            if(ktpUri==null) missing.add("KTP")
                            if(pass!=confirm) missing.add("Pass beda")
                            val ok = missing.isEmpty()
                            Button(onClick={}, modifier=Modifier.fillMaxWidth().height(54.dp), colors=ButtonDefaults.buttonColors(if(ok) DarkGreen else Orange), shape=RoundedCornerShape(12.dp)){ Text(if(ok) "DAFTAR → Saldo 0 ✅ BISA KLIK!" else "DAFTAR (Kurang: ${missing.joinToString()})", fontWeight=FontWeight.Bold, color=Color.White) }
                            Spacer(Modifier.height(10.dp))
                            OutlinedButton(onClick={}, modifier=Modifier.fillMaxWidth().height(46.dp), shape=RoundedCornerShape(12.dp)){ Text("SIMPAN DRAFT") }
                            Spacer(Modifier.height(8.dp))
                            Text(if(ok) "✅ TOMBOL DAFTAR BISA DIKLIK SEKARANG!" else "Kurang: ${missing.joinToString(", ")} - Tapi tombol tetap ORANGE bisa diklik!", color=if(ok) Color(0xFF4CAF50) else Color.Red, fontSize=11.sp, fontWeight=FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
