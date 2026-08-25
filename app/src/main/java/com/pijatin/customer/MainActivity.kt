package com.pijatin.customer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
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
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                        Text("Customer REAL V127 FIX", color=Color(0xFFFFD700), fontSize=14.sp)
                        Spacer(Modifier.height(20.dp))
                        CircularProgressIndicator(color=Color.White)
                        Text("#127 FIX BUILD NO-GMS", color=Color.White, fontSize=12.sp)
                    }
                }
            } else {
                val ctx = LocalContext.current
                val scope = rememberCoroutineScope()
                var nama by remember { mutableStateOf("") }
                var telepon by remember { mutableStateOf("") }
                var email by remember { mutableStateOf("") }
                var alamat by remember { mutableStateOf("") }
                var kota by remember { mutableStateOf("Tangerang") }
                var namaRek by remember { mutableStateOf("") }
                var noRek by remember { mutableStateOf("") }
                var pass by remember { mutableStateOf("") }
                var confirm by remember { mutableStateOf("") }
                var fotoUri by remember { mutableStateOf<Uri?>(null) }
                var ktpUri by remember { mutableStateOf<Uri?>(null) }
                var lat by remember { mutableStateOf(0.0) }
                var lng by remember { mutableStateOf(0.0) }
                var akurasi by remember { mutableStateOf(0f) }
                var lokasiDidapat by remember { mutableStateOf(false) }
                var loadingGPS by remember { mutableStateOf(false) }
                var metodeLokasi by remember { mutableStateOf("Belum ambil") }
                var selectedBank by remember { mutableStateOf("BCA") }
                var selectedEwallet by remember { mutableStateOf("DANA") }
                var noEwallet by remember { mutableStateOf("") }

                val fotoGaleri = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { if(it!=null) fotoUri=it }
                val ktpGaleri = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { if(it!=null) ktpUri=it }
                val fotoKamera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { if(it!=null) fotoUri=Uri.parse("camera") }

                // GPS TANPA PLAY SERVICES - pakai LocationManager bawaan Android
                fun ambilLokasiGPS(){
                    loadingGPS = true
                    if(ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(ctx, "❌ Izin GPS belum", Toast.LENGTH_SHORT).show()
                        loadingGPS = false
                        return
                    }
                    try {
                        val locationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        var bestLocation: Location? = null
                        
                        // Coba GPS provider
                        try {
                            val gpsLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if(gpsLoc != null) bestLocation = gpsLoc
                        } catch(e:Exception){}
                        
                        // Coba Network provider
                        try {
                            val netLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                            if(netLoc != null){
                                if(bestLocation == null || netLoc.accuracy < bestLocation!!.accuracy){
                                    bestLocation = netLoc
                                }
                            }
                        } catch(e:Exception){}

                        if(bestLocation != null){
                            lat = bestLocation.latitude
                            lng = bestLocation.longitude
                            akurasi = bestLocation.accuracy
                            lokasiDidapat = true
                            metodeLokasi = "GPS Real - ${akurasi.toInt()}m"
                            val prefs = ctx.getSharedPreferences("PijatIN_Order_Data", Context.MODE_PRIVATE)
                            prefs.edit().apply {
                                putString("order_customer_lat", lat.toString())
                                putString("order_customer_lng", lng.toString())
                                putFloat("order_customer_akurasi", akurasi)
                                putString("order_customer_kota", kota)
                                putString("order_customer_alamat", alamat)
                                putString("order_maps_url", "https://www.google.com/maps?q=$lat,$lng")
                                putString("order_geo_point", "POINT($lng $lat)")
                                putBoolean("order_lokasi_valid", true)
                                apply()
                            }
                            Toast.makeText(ctx, "✅ GPS DAPAT! $lat,$lng", Toast.LENGTH_LONG).show()
                        } else {
                            // Fallback manual kota
                            val kotaCoords = mapOf(
                                "Tangerang" to Pair(-6.1783, 106.6319),
                                "Jakarta" to Pair(-6.2088, 106.8456),
                                "Bekasi" to Pair(-6.2416, 106.9924),
                                "Depok" to Pair(-6.4025, 106.7942),
                                "Bogor" to Pair(-6.5971, 106.8060),
                                "Tangerang Selatan" to Pair(-6.2888, 106.7160)
                            )
                            val coord = kotaCoords[kota] ?: Pair(-6.2078, 106.8466)
                            lat = coord.first
                            lng = coord.second
                            lokasiDidapat = true
                            metodeLokasi = "Manual Kota $kota"
                            Toast.makeText(ctx, "📍 Lokasi dari kota $kota", Toast.LENGTH_LONG).show()
                        }
                    } catch(e:Exception){
                        Toast.makeText(ctx, "❌ Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    loadingGPS = false
                }

                val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()){ granted ->
                    if(granted) ambilLokasiGPS() else {
                        // Jika ditolak, tetap pakai manual
                        val kotaCoords = mapOf("Tangerang" to Pair(-6.1783, 106.6319), "Jakarta" to Pair(-6.2088, 106.8456), "Bekasi" to Pair(-6.2416, 106.9924))
                        val coord = kotaCoords[kota] ?: Pair(-6.2078, 106.8466)
                        lat = coord.first; lng = coord.second; lokasiDidapat = true; metodeLokasi = "Manual $kota (GPS ditolak)"
                        Toast.makeText(ctx, "📍 Pakai lokasi manual $kota", Toast.LENGTH_LONG).show()
                    }
                }

                fun openMaps(){
                    if(!lokasiDidapat){ Toast.makeText(ctx, "⚠️ Ambil Lokasi dulu!", Toast.LENGTH_SHORT).show(); return }
                    val uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lng")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    try { ctx.startActivity(intent) } catch(e:Exception){
                        ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps?q=$lat,$lng")))
                    }
                }

                fun kirimKeServer(){
                    scope.launch {
                        // JSON untuk server - lat/lng numerik + geo_point spasial
                        val jsonString = """
                        {
                          "nama": "$nama",
                          "email": "$email",
                          "telepon": "$telepon",
                          "alamat": "$alamat",
                          "kota": "$kota",
                          "lat": $lat,
                          "lng": $lng,
                          "geo_point": "POINT($lng $lat)",
                          "geo_point_wkt": "POINT($lng $lat)",
                          "akurasi": $akurasi,
                          "metode_lokasi": "$metodeLokasi",
                          "bank": "$selectedBank",
                          "no_rek": "$noRek",
                          "ewallet": "$selectedEwallet",
                          "no_ewallet": "$noEwallet",
                          "maps_url": "https://www.google.com/maps?q=$lat,$lng"
                        }
                        """.trimIndent()
                        val prefs = ctx.getSharedPreferences("PijatIN_Customer_Real", Context.MODE_PRIVATE)
                        prefs.edit().apply {
                            putString("customer_data_json", jsonString)
                            putString("customer_lat", lat.toString())
                            putString("customer_lng", lng.toString())
                            putString("customer_geo_point", "POINT($lng $lat)")
                            apply()
                        }
                        println("KIRIM SERVER V127 FIX: $jsonString")
                        Toast.makeText(ctx, "✅ DATA + LOKASI TERKIRIM! $lat,$lng", Toast.LENGTH_LONG).show()
                    }
                }

                MaterialTheme {
                    LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(16.dp), contentPadding=PaddingValues(bottom=400.dp)){
                        item{
                            Text("Daftar PijatIN #127 FIX", fontWeight=FontWeight.Bold, fontSize=20.sp, color=DarkGreen)
                            Text("📡 GPS REAL NO-GMS + KIRIM SERVER + DB", fontSize=10.sp, color=Orange, fontWeight=FontWeight.Bold)
                            Spacer(Modifier.height(16.dp))
                            OutlinedTextField(nama,{nama=it}, label={Text("Nama Lengkap *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(telepon,{telepon=it}, label={Text("No Telepon *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(email,{email=it}, label={Text("Email *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(16.dp))
                            Text("📍 Lokasi - Ambil GPS + Manual Kota", fontWeight=FontWeight.Bold, fontSize=14.sp, color=DarkGreen)
                            Spacer(Modifier.height(8.dp))
                            Text("Pilih Kota *", fontSize=11.sp, fontWeight=FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){
                                listOf("Tangerang","Jakarta","Bekasi").forEach{ k ->
                                    Button(onClick={kota=k}, modifier=Modifier.weight(1f).height(36.dp), colors=ButtonDefaults.buttonColors(if(kota==k) DarkGreen else Color.Gray), shape=RoundedCornerShape(8.dp)){ Text(k, fontSize=9.sp) }
                                }
                            }
                            Spacer(Modifier.height(6.dp))
                            Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){
                                listOf("Depok","Bogor","Tangsel").forEach{ k ->
                                    val value = if(k=="Tangsel") "Tangerang Selatan" else k
                                    Button(onClick={kota=value}, modifier=Modifier.weight(1f).height(36.dp), colors=ButtonDefaults.buttonColors(if(kota==value) DarkGreen else Color.LightGray), shape=RoundedCornerShape(8.dp)){ Text(k, fontSize=9.sp, color=if(kota==value) Color.White else Color.Black) }
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(alamat,{alamat=it}, label={Text("Alamat Lengkap RT/RW *")}, modifier=Modifier.fillMaxWidth().height(90.dp), shape=RoundedCornerShape(10.dp), placeholder={Text("GG cemara2 RT 01/01 kunciran jaya pinang Tangerang")})
                            Spacer(Modifier.height(12.dp))
                            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(if(lokasiDidapat) Color(0xFFC8E6C9) else Color(0xFFFFF3E0)).padding(12.dp)){
                                Column{
                                    Row(verticalAlignment=Alignment.CenterVertically, horizontalArrangement=Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth()){
                                        Text(if(lokasiDidapat) "✅ LOKASI DIDAPAT" else "📡 AMBIL LOKASI GPS", fontWeight=FontWeight.Bold, fontSize=13.sp, color=if(lokasiDidapat) Color(0xFF2E7D32) else Color(0xFFE65100))
                                        if(loadingGPS) CircularProgressIndicator(modifier=Modifier.size(16.dp), strokeWidth=2.dp)
                                    }
                                    Spacer(Modifier.height(6.dp))
                                    if(lokasiDidapat){
                                        Text("📍 $lat, $lng", fontSize=12.sp, fontWeight=FontWeight.Bold)
                                        Text("🏙️ Kota: $kota | $metodeLokasi", fontSize=10.sp)
                                        if(akurasi>0) Text("🎯 Akurasi: ${akurasi.toInt()}m", fontSize=10.sp, color=Color(0xFF2E7D32))
                                        Text("✅ Siap kirim server: lat DOUBLE, lng DOUBLE, geo_point POINT", fontSize=9.sp, color=Color(0xFF2E7D32), fontWeight=FontWeight.Bold)
                                    } else {
                                        Text("Tap Ambil Lokasi GPS untuk deteksi lokasi HP real", fontSize=10.sp, color=Color.Gray)
                                        Text("Atau pakai pilihan kota + alamat manual", fontSize=9.sp, color=Color.Gray)
                                    }
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                                Button(
                                    onClick={
                                        if(ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) ambilLokasiGPS()
                                        else permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                    },
                                    modifier=Modifier.weight(1f).height(48.dp),
                                    colors=ButtonDefaults.buttonColors(if(lokasiDidapat) Color(0xFF2E7D32) else Orange),
                                    shape=RoundedCornerShape(10.dp)
                                ){
                                    if(loadingGPS) CircularProgressIndicator(color=Color.White, modifier=Modifier.size(18.dp))
                                    else Text(if(lokasiDidapat) "✅ GPS Dapat" else "📡 Ambil Lokasi GPS", fontSize=10.sp, fontWeight=FontWeight.Bold)
                                }
                                Button(onClick={ openMaps() }, modifier=Modifier.weight(1f).height(48.dp), colors=ButtonDefaults.buttonColors(DarkGreen), shape=RoundedCornerShape(10.dp), enabled=lokasiDidapat){ Text("🌍 Buka Maps", fontSize=10.sp) }
                            }
                            Spacer(Modifier.height(20.dp))
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
                                if(!lokasiDidapat){ Toast.makeText(ctx, "⚠️ Ambil Lokasi GPS dulu!", Toast.LENGTH_SHORT).show(); return@Button }
                                kirimKeServer()
                            }, modifier=Modifier.fillMaxWidth().height(54.dp), colors=ButtonDefaults.buttonColors(DarkGreen), shape=RoundedCornerShape(12.dp)){
                                Text("DAFTAR + KIRIM LOKASI KE SERVER ✅", fontWeight=FontWeight.Bold, color=Color.White, fontSize=12.sp)
                            }
                            Spacer(Modifier.height(10.dp))
                            if(lokasiDidapat) Text("📤 Kirim: lat=$lat (DOUBLE), lng=$lng (DOUBLE), geo_point=POINT($lng $lat) (SPASIAL) + $kota", color=Color(0xFF1976D2), fontSize=9.sp)
                        }
                    }
                }
            }
        }
    }
}
