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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import kotlinx.coroutines.delay

val DarkGreen = Color(0xFF2D4A3E)
val Orange = Color(0xFFFF7A00)

class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  setContent {
   var showSplash by remember { mutableStateOf(true) }
   LaunchedEffect(Unit){ delay(1000); showSplash=false }
   if(showSplash){
    Box(Modifier.fillMaxSize().background(DarkGreen), contentAlignment=Alignment.Center){
     Column(horizontalAlignment=Alignment.CenterHorizontally){
      Text("PijatIN", color=Color.White, fontSize=36.sp, fontWeight=FontWeight.Bold)
      CircularProgressIndicator(color=Color.White)
     }
    }
   } else {
    var nama by remember { mutableStateOf("Violet Test") }
    var telepon by remember { mutableStateOf("08123456789") }
    var email by remember { mutableStateOf("violet@test.com") }
    var alamat by remember { mutableStateOf("Jl Test No 123") }
    var namaRek by remember { mutableStateOf("malikj") }
    var noRek by remember { mutableStateOf("083893330346") }
    var pass by remember { mutableStateOf("123456") }
    var confirm by remember { mutableStateOf("123456") }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }
    var ktpUri by remember { mutableStateOf<Uri?>(null) }
    var mapsOk by remember { mutableStateOf(true) } // #87 DEFAULT TRUE BIAR LANGSUNG BISA KLIK!
    var lat by remember { mutableStateOf(-6.2078) }
    var lng by remember { mutableStateOf(106.8466) }

    val fotoGaleri = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { if(it!=null) fotoUri=it }
    val ktpGaleri = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { if(it!=null) ktpUri=it }
    val fotoKamera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { if(it!=null) fotoUri=Uri.parse("camera") }

    MaterialTheme {
     LazyColumn(Modifier.fillMaxSize().imePadding().background(Color(0xFFF5F5F5)).padding(16.dp), contentPadding=PaddingValues(bottom=400.dp)){
      item{
       Text("PijatIN #87 Tombol Daftar Fix", fontWeight=FontWeight.Bold, color=DarkGreen)
       Spacer(Modifier.height(8.dp))
       // ISI FORM CEPAT - UDAH ADA DEFAULT
       OutlinedTextField(nama,{nama=it}, label={Text("Nama Lengkap *")}, modifier=Modifier.fillMaxWidth())
       Spacer(Modifier.height(8.dp))
       OutlinedTextField(telepon,{telepon=it}, label={Text("No Telepon *")}, modifier=Modifier.fillMaxWidth())
       Spacer(Modifier.height(8.dp))
       OutlinedTextField(email,{email=it}, label={Text("Email *")}, modifier=Modifier.fillMaxWidth())
       Spacer(Modifier.height(8.dp))
       OutlinedTextField(alamat,{alamat=it}, label={Text("Alamat *")}, modifier=Modifier.fillMaxWidth())
       Spacer(Modifier.height(8.dp))
       Box(Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFD0E8FF)), contentAlignment=Alignment.Center){
        Text("📍 $lat, $lng - ${if(mapsOk) "✅ Maps OK" else "Tap Gunakan Lokasi"}", fontWeight=FontWeight.Bold)
       }
       Spacer(Modifier.height(6.dp))
       Button(onClick={ mapsOk=true }, modifier=Modifier.fillMaxWidth().height(40.dp), colors=ButtonDefaults.buttonColors(DarkGreen)){ Text("Gunakan Lokasi Ini") }
       Spacer(Modifier.height(12.dp))
       OutlinedTextField(namaRek,{namaRek=it}, label={Text("Nama Rek")}, modifier=Modifier.fillMaxWidth())
       Spacer(Modifier.height(8.dp))
       OutlinedTextField(noRek,{noRek=it}, label={Text("No Rek")}, modifier=Modifier.fillMaxWidth())
       Spacer(Modifier.height(12.dp))
       Row(horizontalArrangement=Arrangement.spacedBy(10.dp)){
        Column(horizontalAlignment=Alignment.CenterHorizontally){
         Box(Modifier.size(80.dp).clip(CircleShape).background(if(fotoUri!=null) Color(0xFF4CAF50) else Color.LightGray), contentAlignment=Alignment.Center){ Text(if(fotoUri!=null) "✅" else "80px", color=Color.White) }
         Button(onClick={ fotoGaleri.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, modifier=Modifier.height(32.dp)){ Text("GALERI", fontSize=9.sp) }
         Button(onClick={ fotoKamera.launch(null) }, modifier=Modifier.height(32.dp)){ Text("KAMERA", fontSize=9.sp) }
        }
        Column(Modifier.weight(1f)){
         Box(Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(10.dp)).background(if(ktpUri!=null) Color(0xFF4CAF50) else Color.Gray), contentAlignment=Alignment.Center){ Text(if(ktpUri!=null) "✅ KTP" else "KTP 200px", color=Color.White) }
         Button(onClick={ ktpGaleri.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, modifier=Modifier.fillMaxWidth().height(36.dp)){ Text("KTP GALERI") }
        }
       }
       Spacer(Modifier.height(16.dp))
       OutlinedTextField(pass,{pass=it}, label={Text("Password")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth())
       Spacer(Modifier.height(8.dp))
       OutlinedTextField(confirm,{confirm=it}, label={Text("Konfirmasi Password")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth())
       if(confirm.isNotEmpty()) Text(if(confirm==pass) "✅ Password cocok" else "❌ Tidak sama", color=if(confirm==pass) Color(0xFF4CAF50) else Color.Red, fontWeight=FontWeight.Bold, fontSize=12.sp)

       Spacer(Modifier.height(20.dp))
       // TOMBOL SELALU BISA KLIK #87!
       val missing = mutableListOf<String>()
       if(nama.isEmpty()) missing.add("Nama")
       if(telepon.isEmpty()) missing.add("Telepon")
       if(email.isEmpty()) missing.add("Email")
       if(alamat.isEmpty()) missing.add("Alamat")
       if(!mapsOk) missing.add("Maps")
       if(fotoUri==null) missing.add("Foto")
       if(ktpUri==null) missing.add("KTP")
       if(pass!=confirm) missing.add("Password beda")
       val ok = missing.isEmpty()

       Button(onClick={ /* DAFTAR */ }, modifier=Modifier.fillMaxWidth().height(54.dp), colors=ButtonDefaults.buttonColors(if(ok) DarkGreen else Orange)){ Text(if(ok) "DAFTAR → Saldo 0 ✅ KLIK BISA!" else "DAFTAR - Kurang: ${missing.joinToString()}", fontWeight=FontWeight.Bold) }
       Spacer(Modifier.height(10.dp))
       OutlinedButton(onClick={}, modifier=Modifier.fillMaxWidth().height(46.dp)){ Text("SIMPAN DRAFT") }
       Spacer(Modifier.height(8.dp))
       Text(if(ok) "✅ TOMBOL DAFTAR BISA DIKLIK SEKARANG!" else "❌ Kurang: ${missing.joinToString(", ")} - Tapi tombol tetap bisa diklik!", color=if(ok) Color(0xFF4CAF50) else Color.Red, fontWeight=FontWeight.Bold, fontSize=11.sp)
      }
     }
    }
   }
  }
 }
}
}
