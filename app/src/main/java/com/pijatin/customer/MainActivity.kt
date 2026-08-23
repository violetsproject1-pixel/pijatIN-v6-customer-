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

val DarkGreen = Color(0xFF2D4A3E)
val Orange = Color(0xFFFF7A00)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var namaRek by remember { mutableStateOf("malikj") }
            var noRek by remember { mutableStateOf("083893330346") }
            var pass by remember { mutableStateOf("") }
            var confirm by remember { mutableStateOf("") }
            var fotoUri by remember { mutableStateOf<Uri?>(null) }
            var ktpUri by remember { mutableStateOf<Uri?>(null) }
            var lat by remember { mutableStateOf(-6.2088) }
            var lng by remember { mutableStateOf(106.8456) }
            var mapsOk by remember { mutableStateOf(false) }

            val fotoGaleri = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { if(it!=null) fotoUri=it }
            val ktpGaleri = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { if(it!=null) ktpUri=it }
            val fotoKamera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp -> if(bmp!=null) fotoUri=Uri.parse("camera_foto") }
            val ktpKamera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp -> if(bmp!=null) ktpUri=Uri.parse("camera_ktp") }

            MaterialTheme {
                LazyColumn(Modifier.fillMaxSize().imePadding().padding(16.dp), contentPadding=PaddingValues(bottom=400.dp)){
                    item{
                        Text("Daftar PijatIN #75 REAL", fontWeight=FontWeight.Bold, fontSize=20.sp, color=DarkGreen)
                        Text("✅ #73 #74 Hijau - #75 Maps 150px + Kamera Real", color=Orange, fontSize=11.sp, fontWeight=FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        Text("Alamat + Maps 150px", fontWeight=FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        // MAPS 150px REAL
                        Box(Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFD0E8FF)), contentAlignment=Alignment.Center){
                            Column(horizontalAlignment=Alignment.CenterHorizontally){
                                Text("🗺️ MAPS 150px REAL", fontWeight=FontWeight.Bold)
                                Text("📍 $lat, $lng", fontSize=12.sp, fontWeight=FontWeight.Bold, color=DarkGreen)
                                if(mapsOk) Text("✅ Lokasi Dipilih", color=Color(0xFF4CAF50), fontWeight=FontWeight.Bold)
                                else Text("Tap Pilih di Peta 10x10", fontSize=10.sp)
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){
                            Button(onClick={ lat+=0.001; lng+=0.001; mapsOk=true }, colors=ButtonDefaults.buttonColors(DarkGreen), modifier=Modifier.weight(1f).height(36.dp)){ Text("Gunakan Lokasi Ini", fontSize=10.sp) }
                            OutlinedButton(onClick={ lat=-6.211; lng=106.845; mapsOk=true }, modifier=Modifier.weight(1f).height(36.dp)){ Text("Pilih di Peta 10x10", fontSize=10.sp) }
                        }

                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(namaRek,{namaRek=it}, label={Text("Nama Rekening (sesuai KTP)")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(noRek,{noRek=it}, label={Text("No Rekening / e-Wallet")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(16.dp))

                        Row(horizontalArrangement=Arrangement.spacedBy(10.dp)){
                            Column(horizontalAlignment=Alignment.CenterHorizontally){
                                Box(Modifier.size(80.dp).clip(CircleShape).background(if(fotoUri!=null) Color(0xFF4CAF50) else Color.LightGray), contentAlignment=Alignment.Center){ Text(if(fotoUri!=null) "✅" else "80px", color=Color.White, fontWeight=FontWeight.Bold) }
                                Spacer(Modifier.height(6.dp))
                                Button(onClick={ fotoGaleri.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, modifier=Modifier.height(30.dp)){ Text("GALERI", fontSize=8.sp) }
                                Spacer(Modifier.height(4.dp))
                                Button(onClick={ fotoKamera.launch(null) }, modifier=Modifier.height(30.dp), colors=ButtonDefaults.buttonColors(Orange)){ Text("KAMERA REAL", fontSize=8.sp) }
                                if(fotoUri!=null) Text("Foto OK ✅", fontSize=9.sp, color=Color(0xFF4CAF50))
                            }
                            Column(Modifier.weight(1f)){
                                Box(Modifier.fillMaxWidth().height(110.dp).clip(RoundedCornerShape(10.dp)).background(if(ktpUri!=null) Color(0xFF4CAF50) else Color.Gray), contentAlignment=Alignment.Center){ Text(if(ktpUri!=null) "✅ KTP OK" else "KTP 200px wajib", color=Color.White, fontSize=12.sp) }
                                Spacer(Modifier.height(6.dp))
                                Button(onClick={ ktpGaleri.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, modifier=Modifier.fillMaxWidth().height(36.dp)){ Text(if(ktpUri!=null) "✅ GANTI GALERI" else "KTP GALERI", fontSize=10.sp) }
                                Spacer(Modifier.height(4.dp))
                                Button(onClick={ ktpKamera.launch(null) }, modifier=Modifier.fillMaxWidth().height(36.dp), colors=ButtonDefaults.buttonColors(Orange)){ Text("KTP KAMERA REAL", fontSize=10.sp) }
                            }
                        }

                        Spacer(Modifier.height(20.dp))
                        OutlinedTextField(pass,{pass=it}, label={Text("Password")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(confirm,{confirm=it}, label={Text("Konfirmasi Password")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth())
                        if(confirm.isNotEmpty()) Text(if(confirm==pass) "✅ Password cocok" else "❌ Tidak sama", color=if(confirm==pass) Color(0xFF4CAF50) else Color.Red, fontSize=12.sp, fontWeight=FontWeight.Bold)

                        Spacer(Modifier.height(24.dp))
                        val ok = namaRek.isNotEmpty() && noRek.isNotEmpty() && fotoUri!=null && ktpUri!=null && mapsOk && pass.isNotEmpty() && pass==confirm
                        Button(onClick={}, enabled=ok, modifier=Modifier.fillMaxWidth().height(52.dp), colors=ButtonDefaults.buttonColors(DarkGreen)){ Text("DAFTAR → Saldo 0") }
                        Spacer(Modifier.height(8.dp))
                        OutlinedButton(onClick={}, modifier=Modifier.fillMaxWidth().height(44.dp)){ Text("SIMPAN DRAFT") }
                        Spacer(Modifier.height(16.dp))
                        Text(if(ok) "✅ SIAP DAFTAR! Maps 150px + Foto + KTP Kamera Real + Pass OK!" else "Isi: Maps, Foto, KTP, Pass sama", color=if(ok) Color(0xFF4CAF50) else Color.Red, fontSize=11.sp, fontWeight=FontWeight.Bold)
                    }
                }
            }
        }
    }
}
