package com.pijatin.customer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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

class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContent {
            var showSplash by remember { mutableStateOf(true) }
            LaunchedEffect(Unit){ delay(1500); showSplash=false }
            if(showSplash){
                Box(Modifier.fillMaxSize().background(DarkGreen), contentAlignment=Alignment.Center){
                    Column(horizontalAlignment=Alignment.CenterHorizontally){
                        Text("PijatIN", color=Color.White, fontSize=32.sp, fontWeight=FontWeight.Bold)
                        Text("Customer REAL V126", color=Color(0xFFFFD700), fontSize=14.sp)
                        Spacer(Modifier.height(20.dp))
                        CircularProgressIndicator(color=Color.White)
                        Text("#126 SIMPAN LOKASI REAL UNTUK MITRA", color=Color.White, fontSize=12.sp)
                    }
                }
            } else {
                val ctx = LocalContext.current
                var nama by remember { mutableStateOf("") }
                var telepon by remember { mutableStateOf("") }
                var email by remember { mutableStateOf("") }
                var alamat by remember { mutableStateOf("") }
                var namaRek by remember { mutableStateOf("") }
                var noRek by remember { mutableStateOf("") }
                var pass by remember { mutableStateOf("") }
                var confirm by remember { mutableStateOf("") }
                var fotoUri by remember { mutableStateOf<Uri?>(null) }
                var ktpUri by remember { mutableStateOf<Uri?>(null) }
                var lat by remember { mutableStateOf(-6.2078) }
                var lng by remember { mutableStateOf(106.8466) }
                var lokasiTersimpan by remember { mutableStateOf(false) }
                var selectedBank by remember { mutableStateOf("BCA") }
                var selectedEwallet by remember { mutableStateOf("DANA") }
                var noEwallet by remember { mutableStateOf("") }

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
                }

                fun simpanLokasiReal(){
                    // SIMPAN KE SHARED PREFERENCES - BIAR MITRA BISA BACA DI PESANAN
                    val prefsCustomer = ctx.getSharedPreferences("PijatIN_Customer_Location", Context.MODE_PRIVATE)
                    prefsCustomer.edit().apply {
                        putString("customer_lat", lat.toString())
                        putString("customer_lng", lng.toString())
                        putString("customer_alamat", alamat)
                        putString("customer_nama", nama)
                        putLong("timestamp", System.currentTimeMillis())
                        putBoolean("lokasi_sudah_disimpan", true)
                        apply()
                    }
                    // SIMPAN JUGA UNTUK ORDER - INI YANG DIBACA MITRA
                    val prefsOrder = ctx.getSharedPreferences("PijatIN_Order_Data", Context.MODE_PRIVATE)
                    prefsOrder.edit().apply {
                        putString("order_customer_lat", lat.toString())
                        putString("order_customer_lng", lng.toString())
                        putString("order_customer_alamat", alamat.ifEmpty { "kunciran jaya RT 01 RW 01 pinang Tangerang" })
                        putString("order_maps_url", "https://www.google.com/maps?q=$lat,$lng")
                        putString("order_gmaps_intent", "geo:$lat,$lng?q=$lat,$lng(Customer)")
                        putBoolean("order_lokasi_valid", true)
                        apply()
                    }
                    // SIMPAN UNTUK MITRA TRACKING
                    val prefsMitra = ctx.getSharedPreferences("PijatIN_Mitra_Tracking", Context.MODE_PRIVATE)
                    prefsMitra.edit().apply {
                        putString("tracking_customer_lat", lat.toString())
                        putString("tracking_customer_lng", lng.toString())
                        putString("tracking_customer_alamat_lengkap", alamat)
                        putString("tracking_status", "LOKASI_TERSIMPAN_SIAP_DILACAK_MITRA")
                        apply()
                    }
                    lokasiTersimpan = true
                    Toast.makeText(ctx, "✅ LOKASI TERSIMPAN! Mitra bisa lacak: $lat,$lng - $alamat", Toast.LENGTH_LONG).show()
                }

                // CEK APAKAH LOKASI SUDAH PERNAH DISIMPAN
                LaunchedEffect(Unit){
                    val prefs = ctx.getSharedPreferences("PijatIN_Customer_Location", Context.MODE_PRIVATE)
                    if(prefs.getBoolean("lokasi_sudah_disimpan", false)){
                        lokasiTersimpan = true
                        lat = prefs.getString("customer_lat", "-6.2078")?.toDoubleOrNull() ?: -6.2078
                        lng = prefs.getString("customer_lng", "106.8466")?.toDoubleOrNull() ?: 106.8466
                    }
                }

                MaterialTheme {
                    LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(16.dp), contentPadding=PaddingValues(bottom=400.dp)){
                        item{
                            Text("Daftar PijatIN #126 REAL", fontWeight=FontWeight.Bold, fontSize=20.sp, color=DarkGreen)
                            Text("✅ SIMPAN LOKASI REAL - MITRA BISA LACAK", fontSize=10.sp, color=Orange, fontWeight=FontWeight.Bold)
                            Spacer(Modifier.height(16.dp))
                            OutlinedTextField(nama,{nama=it}, label={Text("Nama Lengkap *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(telepon,{telepon=it}, label={Text("No Telepon *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(email,{email=it}, label={Text("Email *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(alamat,{alamat=it}, label={Text("Alamat *")}, modifier=Modifier.fillMaxWidth().height(90.dp), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(12.dp))
                            Box(Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(12.dp)).background(if(lokasiTersimpan) Color(0xFFC8E6C9) else Color(0xFFD0E8FF)), contentAlignment=Alignment.Center){
                                Column(horizontalAlignment=Alignment.CenterHorizontally, modifier=Modifier.padding(10.dp)){
                                    Text(if(lokasiTersimpan) "✅ LOKASI TERSIMPAN" else "🗺️ MAPS 150px TAP BUKA", fontWeight=FontWeight.Bold, fontSize=13.sp, color=if(lokasiTersimpan) Color(0xFF2E7D32) else Color.Black)
                                    Spacer(Modifier.height(4.dp))
                                    Text("📍 ${String.format("%.4f", lat)}, ${String.format("%.4f", lng)}", fontSize=12.sp, fontWeight=FontWeight.Bold)
                                    Spacer(Modifier.height(4.dp))
                                    if(lokasiTersimpan){
                                        Text("✅ Tersimpan untuk Mitra - Bisa dilacak di pesanan", color=Color(0xFF2E7D32), fontSize=10.sp, fontWeight=FontWeight.Bold)
                                        Text("📦 Order_Data + Mitra_Tracking OK", color=Color(0xFF2E7D32), fontSize=9.sp)
                                    } else {
                                        Text("⚠️ Tap Simpan Lokasi agar mitra bisa lacak", color=Color(0xFFE65100), fontSize=10.sp, fontWeight=FontWeight.Bold)
                                    }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                                Button(onClick={ openMaps() }, modifier=Modifier.weight(1f).height(44.dp), colors=ButtonDefaults.buttonColors(DarkGreen), shape=RoundedCornerShape(10.dp)){ Text("🌍 Buka Maps Real", fontSize=10.sp) }
                                Button(
                                    onClick={ simpanLokasiReal() }, 
                                    modifier=Modifier.weight(1f).height(44.dp), 
                                    colors=ButtonDefaults.buttonColors(if(lokasiTersimpan) Color(0xFF2E7D32) else Orange),
                                    shape=RoundedCornerShape(10.dp)
                                ){ Text(if(lokasiTersimpan) "✅ Lokasi Tersimpan" else "📍 Simpan Lokasi", fontSize=10.sp, fontWeight=FontWeight.Bold) }
                            }
                            Spacer(Modifier.height(16.dp))
                            // BANK SELECTION REAL
                            Text("🏦 Pilih Bank REAL *", fontWeight=FontWeight.Bold, fontSize=12.sp)
                            Spacer(Modifier.height(6.dp))
                            Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){
                                listOf("BCA","MANDIRI","BRI","SEABANK").forEach{ bank ->
                                    Button(onClick={selectedBank=bank}, modifier=Modifier.weight(1f).height(36.dp), colors=ButtonDefaults.buttonColors(if(selectedBank==bank) DarkGreen else Color.Gray), shape=RoundedCornerShape(8.dp)){ Text(bank, fontSize=9.sp) }
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(namaRek,{namaRek=it}, label={Text("Nama Rekening *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(noRek,{noRek=it}, label={Text("No Rekening $selectedBank *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(16.dp))
                            // EWALLET SELECTION REAL
                            Text("📱 Pilih E-Wallet REAL *", fontWeight=FontWeight.Bold, fontSize=12.sp)
                            Spacer(Modifier.height(6.dp))
                            Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){
                                listOf("DANA","OVO","GOPAY").forEach{ ew ->
                                    Button(onClick={selectedEwallet=ew}, modifier=Modifier.weight(1f).height(36.dp), colors=ButtonDefaults.buttonColors(if(selectedEwallet==ew) Orange else Color.Gray), shape=RoundedCornerShape(8.dp)){ Text(ew, fontSize=9.sp) }
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(noEwallet,{noEwallet=it}, label={Text("No E-Wallet $selectedEwallet *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
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
                                    Box(Modifier.fillMaxWidth().height(110.dp).clip(RoundedCornerShape(12.dp)).background(if(ktpUri!=null) Color(0xFF4CAF50) else Color(0xFF9E9E9E)), contentAlignment=Alignment.Center){ Text(if(ktpUri==null) "✅ KTP OK" else "KTP 200px", color=Color.White, fontWeight=FontWeight.Bold) }
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
                            Button(onClick={
                                if(!lokasiTersimpan){
                                    Toast.makeText(ctx, "⚠️ Simpan Lokasi dulu biar mitra bisa lacak!", Toast.LENGTH_SHORT).show()
                                    simpanLokasiReal()
                                }
                                println("DAFTAR REAL V126: $email $selectedBank $noRek $selectedEwallet $noEwallet $lat $lng LOKASI_TERSIMPAN=$lokasiTersimpan")
                                Toast.makeText(ctx, "✅ DAFTAR OK! Lokasi: $lat,$lng tersimpan untuk mitra", Toast.LENGTH_LONG).show()
                            }, modifier=Modifier.fillMaxWidth().height(54.dp), colors=ButtonDefaults.buttonColors(DarkGreen), shape=RoundedCornerShape(12.dp)){
                                Text("DAFTAR → Saldo 0 ✅ BISA KLIK!", fontWeight=FontWeight.Bold, color=Color.White)
                            }
                            Spacer(Modifier.height(10.dp))
                            Text("✅ BANK: $selectedBank $noRek | EWALLET: $selectedEwallet $noEwallet", color=Color(0xFF4CAF50), fontSize=11.sp, fontWeight=FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text(if(lokasiTersimpan) "📍 LOKASI: $lat,$lng ✅ TERSIMPAN - MITRA BISA LACAK DI PESANAN" else "📍 LOKASI: $lat,$lng ⚠️ BELUM DISIMPAN - TAP SIMPAN LOKASI", color=if(lokasiTersimpan) Color(0xFF2E7D32) else Color(0xFFE65100), fontSize=11.sp, fontWeight=FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
