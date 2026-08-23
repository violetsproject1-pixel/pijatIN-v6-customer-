package com.pijatin.customer

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.WebView
import android.webkit.WebViewClient
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

val DarkGreen = Color(0xFF2D4A3E)
val Orange = Color(0xFFFF7A00)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App58() }
    }
}

@Composable
fun App58(){
    var screen by remember { mutableStateOf("splash") }
    var nama by remember { mutableStateOf("") }
    var hp by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var alamat by remember { mutableStateOf("") }
    var bank by remember { mutableStateOf("Mandiri") }
    var namaRek by remember { mutableStateOf("") }
    var noRek by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }
    var ktpUri by remember { mutableStateOf<Uri?>(null) }
    var mapsPicked by remember { mutableStateOf(false) }

    val fotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){ uri -> if(uri!=null) fotoUri=uri }
    val ktpLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){ uri -> if(uri!=null) ktpUri=uri }

    LaunchedEffect(screen){ if(screen=="splash"){ delay(2500); screen="auth" } }

    when(screen){
        "splash" -> Box(Modifier.fillMaxSize().background(DarkGreen), contentAlignment=Alignment.Center){
            Column(horizontalAlignment=Alignment.CenterHorizontally){
                Box(Modifier.size(72.dp).clip(CircleShape).background(Color.White), contentAlignment=Alignment.Center){ Text("A", fontSize=36.sp, fontWeight=FontWeight.Bold, color=DarkGreen) }
                Text("pijatIN", color=Color.White, fontSize=26.sp, fontWeight=FontWeight.Bold)
                Box(Modifier.width(60.dp).height(3.dp).background(Orange))
            }
        }
        "auth" -> LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp)){
            item{
                Text("Daftar #58 LENGKAP REAL", fontSize=18.sp, fontWeight=FontWeight.Bold, color=DarkGreen)
                Text("Fix: Nama Rek + No Rek + Confirm Pass", fontSize=11.sp, color=Orange, fontWeight=FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(nama,{nama=it}, label={Text("Nama Lengkap")}, modifier=Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(hp,{hp=it}, label={Text("No HP (unique)")}, modifier=Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(email,{email=it}, label={Text("Email")}, modifier=Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(alamat,{alamat=it}, label={Text("Alamat + Maps 150px")}, modifier=Modifier.fillMaxWidth().height(90.dp))
                Spacer(Modifier.height(8.dp))
                Card(Modifier.fillMaxWidth().height(150.dp)){
                    AndroidView(factory={ ctx ->
                        WebView(ctx).apply{
                            webViewClient=WebViewClient()
                            settings.javaScriptEnabled=true
                            loadData("""
                                <html><head><link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>
                                <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
                                <style>#map{height:150px;width:100%;margin:0}</style></head>
                                <body style="margin:0"><div id="map"></div>
                                <script>
                                var map=L.map('map').setView([-6.20,106.63],15);
                                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);
                                var m=L.marker([-6.20,106.63],{draggable:true}).addTo(map);
                                </script></body></html>
                            """.trimIndent(),"text/html","UTF-8")
                        }
                    })
                }
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                    Button(onClick={mapsPicked=true}, colors=ButtonDefaults.buttonColors(DarkGreen), modifier=Modifier.weight(1f)){ Text(if(mapsPicked) "✅ Lokasi Terpilih" else "Gunakan Lokasi Ini", fontSize=10.sp) }
                    Button(onClick={mapsPicked=true}, colors=ButtonDefaults.buttonColors(Orange), modifier=Modifier.weight(1f)){ Text("Pilih di Peta 10x10", fontSize=10.sp) }
                }
                Spacer(Modifier.height(12.dp))
                Text("Bank", fontWeight=FontWeight.Bold, fontSize=13.sp)
                LazyRow(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                    items(listOf("BCA","BRI","BNI","Mandiri","Seabank","OVO","GoPay","DANA")){ b ->
                        FilterChip(selected=bank==b, onClick={bank=b}, label={Text(b, fontSize=11.sp)}, colors=FilterChipDefaults.filterChipColors(selectedContainerColor=DarkGreen, selectedLabelColor=Color.White))
                    }
                }
                Spacer(Modifier.height(8.dp))
                // YANG HILANG KEMARIN - SEKARANG ADA!
                OutlinedTextField(namaRek,{namaRek=it}, label={Text("Nama Rekening (sesuai KTP)")}, modifier=Modifier.fillMaxWidth(), colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=DarkGreen))
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(noRek,{noRek=it}, label={Text("No Rekening / No e-Wallet")}, placeholder={Text("Contoh: 1234567890 atau 0812...")}, modifier=Modifier.fillMaxWidth(), colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=DarkGreen))
                if(bank in listOf("OVO","GoPay","DANA")) Text("⚠️ Untuk $bank, isi No HP e-wallet", fontSize=10.sp, color=Orange)
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement=Arrangement.spacedBy(12.dp)){
                    Card(Modifier.size(80.dp).clip(CircleShape).clickable{ fotoLauncher.launch("image/*") }.border(2.dp, if(fotoUri!=null) Orange else Color.Gray, CircleShape)){
                        if(fotoUri!=null) AsyncImage(model=fotoUri, contentDescription=null, modifier=Modifier.fillMaxSize(), contentScale=ContentScale.Crop)
                        else Box(Modifier.fillMaxSize(), contentAlignment=Alignment.Center){ Text("Foto profil 80px circle wajib\nTap galeri", fontSize=8.sp, modifier=Modifier.padding(4.dp)) }
                    }
                    Card(Modifier.height(80.dp).width(120.dp).clickable{ ktpLauncher.launch("image/*") }.border(2.dp, if(ktpUri!=null) Orange else Color.Gray, RoundedCornerShape(8.dp)), shape=RoundedCornerShape(8.dp)){
                        if(ktpUri!=null) AsyncImage(model=ktpUri, contentDescription=null, modifier=Modifier.fillMaxSize(), contentScale=ContentScale.Crop)
                        else Box(Modifier.fillMaxSize(), contentAlignment=Alignment.Center){ Text("KTP 200px wajib\nTap galeri", fontSize=10.sp) }
                    }
                }
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(pass,{pass=it}, label={Text("Password")}, visualTransformation=if(showPass) VisualTransformation.None else PasswordVisualTransformation(), trailingIcon={ Text(if(showPass) "🙈" else "👁️", modifier=Modifier.clickable{showPass=!showPass}) }, modifier=Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                // YANG HILANG KEMARIN - SEKARANG ADA!
                OutlinedTextField(confirmPass,{confirmPass=it}, label={Text("Konfirmasi Password")}, visualTransformation=if(showConfirm) VisualTransformation.None else PasswordVisualTransformation(), trailingIcon={ Text(if(showConfirm) "🙈" else "👁️", modifier=Modifier.clickable{showConfirm=!showConfirm}) }, modifier=Modifier.fillMaxWidth(), isError=confirmPass.isNotEmpty() && confirmPass!=pass, supportingText={ if(confirmPass.isNotEmpty() && confirmPass!=pass) Text("Password tidak sama!", color=Color.Red, fontSize=11.sp) else if(confirmPass.isNotEmpty() && confirmPass==pass) Text("✅ Password cocok", color=Color(0xFF4CAF50), fontSize=11.sp) })
                Spacer(Modifier.height(16.dp))
                val isValid = nama.isNotEmpty() && hp.isNotEmpty() && namaRek.isNotEmpty() && noRek.isNotEmpty() && fotoUri!=null && ktpUri!=null && pass.isNotEmpty() && pass==confirmPass && mapsPicked
                Button(onClick={ screen="home" }, enabled=isValid, modifier=Modifier.fillMaxWidth().height(48.dp), colors=ButtonDefaults.buttonColors(DarkGreen), shape=RoundedCornerShape(12.dp)){
                    Text("Daftar → Saldo 0", fontWeight=FontWeight.Bold)
                }
                if(!isValid) Text("❌ Lengkapi: Nama, HP, Nama Rek, No Rek, Foto, KTP, Maps, Password sama", fontSize=10.sp, color=Color.Red, modifier=Modifier.padding(top=6.dp))
                else Text("✅ Semua lengkap! Bisa daftar", fontSize=11.sp, color=Color(0xFF4CAF50), fontWeight=FontWeight.Bold, modifier=Modifier.padding(top=6.dp))
            }
        }
        "home" -> Box(Modifier.fillMaxSize().background(Color.White), contentAlignment=Alignment.Center){
            Column(horizontalAlignment=Alignment.CenterHorizontally, modifier=Modifier.padding(16.dp)){
                Text("✅ #58 LENGKAP BERHASIL!", fontSize=18.sp, fontWeight=FontWeight.Bold, color=DarkGreen)
                Spacer(Modifier.height(8.dp))
                if(fotoUri!=null) AsyncImage(model=fotoUri, contentDescription=null, modifier=Modifier.size(80.dp).clip(CircleShape))
                Text(nama, fontWeight=FontWeight.Bold)
                Text("Rek: $namaRek - $noRek ($bank)", fontSize=11.sp)
                Text(alamat, fontSize=11.sp)
            }
        }
    }
}
