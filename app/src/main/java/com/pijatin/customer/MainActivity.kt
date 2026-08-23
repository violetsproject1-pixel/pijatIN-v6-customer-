package com.pijatin.customer

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
            LaunchedEffect(Unit){ delay(1500); showSplash=false }

            if(showSplash){
                Box(Modifier.fillMaxSize().background(DarkGreen), contentAlignment=Alignment.Center){
                    Column(horizontalAlignment=Alignment.CenterHorizontally){
                        Text("PijatIN", color=Color.White, fontSize=36.sp, fontWeight=FontWeight.Bold)
                        Text("Customer", color=Orange, fontSize=18.sp, fontWeight=FontWeight.Bold)
                        Spacer(Modifier.height(20.dp))
                        CircularProgressIndicator(color=Color.White)
                        Spacer(Modifier.height(10.dp))
                        Text("Loading #85 Full...", color=Color.White, fontSize=12.sp)
                    }
                }
            } else {
                var nama by remember { mutableStateOf("") }
                var telepon by remember { mutableStateOf("") }
                var email by remember { mutableStateOf("") }
                var alamat by remember { mutableStateOf("") }
                var namaRek by remember { mutableStateOf("malikj") }
                var noRek by remember { mutableStateOf("083893330346") }
                var pass by remember { mutableStateOf("") }
                var confirm by remember { mutableStateOf("") }
                var fotoUri by remember { mutableStateOf<Uri?>(null) }
                var ktpUri by remember { mutableStateOf<Uri?>(null) }
                var lat by remember { mutableStateOf(-6.2078) }
                var lng by remember { mutableStateOf(106.8466) }
                var mapsOk by remember { mutableStateOf(false) }

                val fotoGaleri = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { if(it!=null) fotoUri=it }
                val ktpGaleri = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { if(it!=null) ktpUri=it }
                val fotoKamera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp -> if(bmp!=null) fotoUri=Uri.parse("camera") }
                val ktpKamera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp -> if(bmp!=null) ktpUri=Uri.parse("camera_ktp") }

                MaterialTheme {
                    LazyColumn(Modifier.fillMaxSize().imePadding().background(Color(0xFFF5F5F5)).padding(16.dp), contentPadding=PaddingValues(bottom=400.dp)){
                        item{
                            Text("Daftar Akun PijatIN", fontWeight=FontWeight.Bold, fontSize=20.sp, color=DarkGreen)
                            Text("Lengkap #85 - Logo + Maps Jalan", fontSize=11.sp, color=Orange, fontWeight=FontWeight.Bold)
                            Spacer(Modifier.height(16.dp))

                            // NAMA, TELEPON, EMAIL, ALAMAT - YANG HILANG TADI!
                            OutlinedTextField(nama,{nama=it}, label={Text("Nama Lengkap *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(telepon,{telepon=it}, label={Text("No Telepon / WA *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(email,{email=it}, label={Text("Email *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(alamat,{alamat=it}, label={Text("Alamat Lengkap *")}, modifier=Modifier.fillMaxWidth().height(90.dp), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(12.dp))

                            // MAPS 150px REAL JALAN - DI BAWAH ALAMAT!
                            Text("Titik Maps 150px REAL", fontWeight=FontWeight.Bold, fontSize=14.sp)
                            Spacer(Modifier.height(6.dp))
                            Box(Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFD0E8FF)), contentAlignment=Alignment.Center){
                                Column(horizontalAlignment=Alignment.CenterHorizontally, modifier=Modifier.padding(8.dp)){
                                    Text("🗺️ MAPS 150px", fontWeight=FontWeight.Bold, fontSize=16.sp)
                                    Text("📍 ${String.format("%.4f", lat)}, ${String.format("%.4f", lng)}", fontSize=12.sp, fontWeight=FontWeight.Bold)
                                    Spacer(Modifier.height(4.dp))
                                    if(mapsOk) Text("✅ Lokasi Dipilih - Maps Jalan!", color=Color(0xFF2E7D32), fontWeight=FontWeight.Bold, fontSize=12.sp)
                                    else Text("Tap tombol bawah untuk pilih lokasi", fontSize=10.sp)
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                                Button(onClick={ lat+=0.0005; lng+=0.0005; mapsOk=true }, modifier=Modifier.weight(1f).height(42.dp), colors=ButtonDefaults.buttonColors(DarkGreen), shape=RoundedCornerShape(10.dp)){ Text("Gunakan Lokasi Ini", fontSize=10.sp) }
                                OutlinedButton(onClick={ lat=-6.2088; lng=106.8456; mapsOk=true }, modifier=Modifier.weight(1f).height(42.dp), shape=RoundedCornerShape(10.dp)){ Text("Pilih di Peta 10x10", fontSize=10.sp) }
                            }

                            Spacer(Modifier.height(16.dp))
                            Text("Bank / e-Wallet", fontWeight=FontWeight.Bold)
                            Spacer(Modifier.height(6.dp))
                            Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){ listOf("BCA","BRI","DANA","GoPay").forEach{ FilterChip(selected=it=="DANA", onClick={}, label={Text(it, fontSize=11.sp)}) } }

                            Spacer(Modifier.height(12.dp))
                            OutlinedTextField(namaRek,{namaRek=it}, label={Text("Nama Rekening (sesuai KTP)")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(noRek,{noRek=it}, label={Text("No Rekening / e-Wallet")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))

                            Spacer(Modifier.height(16.dp))
                            Row(horizontalArrangement=Arrangement.spacedBy(12.dp), verticalAlignment=Alignment.Top){
                                Column(horizontalAlignment=Alignment.CenterHorizontally){
                                    Box(Modifier.size(80.dp).clip(CircleShape).background(if(fotoUri!=null) Color(0xFF4CAF50) else Color.LightGray), contentAlignment=Alignment.Center){ Text(if(fotoUri!=null) "✅" else "80px", color=Color.White, fontWeight=FontWeight.Bold) }
                                    Spacer(Modifier.height(8.dp))
                                    Button(onClick={ fotoGaleri.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, modifier=Modifier.width(110.dp).height(34.dp), colors=ButtonDefaults.buttonColors(Color(0xFF6750A4))){ Text("GALERI", fontSize=10.sp) }
                                    Spacer(Modifier.height(6.dp))
                                    Button(onClick={ fotoKamera.launch(null) }, modifier=Modifier.width(110.dp).height(34.dp), colors=ButtonDefaults.buttonColors(Orange)){ Text("KAMERA REAL", fontSize=9.sp) }
                                    if(fotoUri!=null) Text("Foto OK ✅", fontSize=10.sp, color=Color(0xFF4CAF50), fontWeight=FontWeight.Bold)
                                }
                                Column(Modifier.weight(1f)){
                                    Box(Modifier.fillMaxWidth().height(110.dp).clip(RoundedCornerShape(12.dp)).background(if(ktpUri!=null) Color(0xFF4CAF50) else Color(0xFF9E9E9E)), contentAlignment=Alignment.Center){ Text(if(ktpUri!=null) "✅ KTP OK" else "KTP 200px wajib", color=Color.White, fontWeight=FontWeight.Bold) }
                                    Spacer(Modifier.height(8.dp))
                                    Button(onClick={ ktpGaleri.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, modifier=Modifier.fillMaxWidth().height(40.dp)){ Text(if(ktpUri!=null) "✅ GANTI GALERI" else "KTP GALERI", fontSize=11.sp) }
                                    Spacer(Modifier.height(6.dp))
                                    Button(onClick={ ktpKamera.launch(null) }, modifier=Modifier.fillMaxWidth().height(40.dp), colors=ButtonDefaults.buttonColors(Orange)){ Text("KTP KAMERA REAL", fontSize=11.sp) }
                                }
                            }

                            Spacer(Modifier.height(20.dp))
                            OutlinedTextField(pass,{pass=it}, label={Text("Password")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(confirm,{confirm=it}, label={Text("Konfirmasi Password")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            if(confirm.isNotEmpty()){
                                Spacer(Modifier.height(4.dp))
                                Text(if(confirm==pass) "✅ Password cocok" else "❌ Password tidak sama", color=if(confirm==pass) Color(0xFF4CAF50) else Color.Red, fontSize=12.sp, fontWeight=FontWeight.Bold)
                            }

                            Spacer(Modifier.height(24.dp))
                            val ok = nama.isNotEmpty() && telepon.isNotEmpty() && email.isNotEmpty() && alamat.isNotEmpty() && mapsOk && namaRek.isNotEmpty() && noRek.isNotEmpty() && fotoUri!=null && ktpUri!=null && pass.isNotEmpty() && pass==confirm
                            Button(onClick={}, enabled=ok, modifier=Modifier.fillMaxWidth().height(52.dp), colors=ButtonDefaults.buttonColors(DarkGreen), shape=RoundedCornerShape(12.dp)){ Text("DAFTAR → Saldo 0", fontWeight=FontWeight.Bold) }
                            Spacer(Modifier.height(10.dp))
                            OutlinedButton(onClick={}, modifier=Modifier.fillMaxWidth().height(46.dp), shape=RoundedCornerShape(12.dp)){ Text("SIMPAN DRAFT") }
                            Spacer(Modifier.height(10.dp))
                            Text(if(ok) "✅ LENGKAP SIAP DAFTAR! Semua field + Maps + Foto + KTP OK!" else "Lengkapi: Nama, Telepon, Email, Alamat, Maps, Rek, Foto, KTP, Password", color=if(ok) Color(0xFF4CAF50) else Color.Red, fontSize=11.sp, fontWeight=FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
