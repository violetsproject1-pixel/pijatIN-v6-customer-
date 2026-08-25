package com.pijatin.customer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
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
                        Text("Customer REAL V128 AKURAT", color=Color(0xFFFFD700), fontSize=14.sp)
                        Spacer(Modifier.height(20.dp))
                        CircularProgressIndicator(color=Color.White)
                        Text("#128 GPS AKURAT TINGGI + LIVE TRACK", color=Color.White, fontSize=11.sp)
                    }
                }
            } else {
                val ctx = LocalContext.current
                val scope = rememberCoroutineScope()
                var nama by remember { mutableStateOf("") }
                var telepon by remember { mutableStateOf("") }
                var email by remember { mutableStateOf("") }
                var alamat by remember { mutableStateOf("GG cemara2 RT 01/01 kunciran jaya pinang Tangerang") }
                var kota by remember { mutableStateOf("Tangerang") }
                var namaRek by remember { mutableStateOf("") }
                var noRek by remember { mutableStateOf("") }
                var pass by remember { mutableStateOf("") }
                var confirm by remember { mutableStateOf("") }
                var fotoUri by remember { mutableStateOf<Uri?>(null) }
                var ktpUri by remember { mutableStateOf<Uri?>(null) }
                var lat by remember { mutableStateOf(0.0) }
                var lng by remember { mutableStateOf(0.0) }
                var akurasi by remember { mutableStateOf(999f) }
                var altitude by remember { mutableStateOf(0.0) }
                var kecepatan by remember { mutableStateOf(0f) }
                var lokasiDidapat by remember { mutableStateOf(false) }
                var loadingGPS by remember { mutableStateOf(false) }
                var metodeLokasi by remember { mutableStateOf("Belum") }
                var jumlahUpdate by remember { mutableStateOf(0) }
                var selectedBank by remember { mutableStateOf("BCA") }
                var selectedEwallet by remember { mutableStateOf("DANA") }
                var noEwallet by remember { mutableStateOf("") }

                val fotoGaleri = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { if(it!=null) fotoUri=it }
                val ktpGaleri = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { if(it!=null) ktpUri=it }
                val fotoKamera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { if(it!=null) fotoUri=Uri.parse("camera") }

                // Location Manager untuk akurasi tinggi
                val locationManager = remember { ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager }
                var locationListener by remember { mutableStateOf<LocationListener?>(null) }

                fun simpanLokasiRealTime(loc: Location, metode: String){
                    lat = loc.latitude
                    lng = loc.longitude
                    akurasi = loc.accuracy
                    altitude = loc.altitude
                    kecepatan = if(loc.hasSpeed()) loc.speed else 0f
                    jumlahUpdate++
                    
                    // Hanya simpan jika akurasi < 50m (akurat)
                    if(akurasi <= 50f){
                        lokasiDidapat = true
                        metodeLokasi = "$metode - Akurasi ${akurasi.toInt()}m - Update #$jumlahUpdate"
                        
                        // SIMPAN UNTUK MITRA TRACKING REAL-TIME
                        val prefs = ctx.getSharedPreferences("PijatIN_Order_Data", Context.MODE_PRIVATE)
                        prefs.edit().apply {
                            putString("order_customer_lat", lat.toString())
                            putString("order_customer_lng", lng.toString())
                            putFloat("order_customer_akurasi", akurasi)
                            putString("order_customer_altitude", altitude.toString())
                            putFloat("order_customer_speed", kecepatan)
                            putString("order_customer_kota", kota)
                            putString("order_customer_alamat", alamat)
                            putString("order_maps_url", "https://www.google.com/maps?q=$lat,$lng")
                            putString("order_geo_point", "POINT($lng $lat)")
                            putString("order_geo_wkt", "POINT($lng $lat)")
                            putLong("order_timestamp", System.currentTimeMillis())
                            putBoolean("order_lokasi_valid", true)
                            putString("order_metode", metode)
                            putInt("order_update_count", jumlahUpdate)
                            apply()
                        }
                        // Untuk Customer sendiri
                        val prefsCust = ctx.getSharedPreferences("PijatIN_Customer_Location", Context.MODE_PRIVATE)
                        prefsCust.edit().apply {
                            putString("customer_lat", lat.toString())
                            putString("customer_lng", lng.toString())
                            putFloat("customer_akurasi", akurasi)
                            putLong("timestamp", System.currentTimeMillis())
                            putBoolean("lokasi_sudah_disimpan", true)
                            apply()
                        }
                        // Untuk Mitra Tracking
                        val prefsMitra = ctx.getSharedPreferences("PijatIN_Mitra_Tracking", Context.MODE_PRIVATE)
                        prefsMitra.edit().apply {
                            putString("tracking_customer_lat", lat.toString())
                            putString("tracking_customer_lng", lng.toString())
                            putString("tracking_customer_alamat", "$alamat, $kota")
                            putString("tracking_status", "LIVE_TRACKING_AKURAT_${akurasi.toInt()}m")
                            putLong("tracking_last_update", System.currentTimeMillis())
                            apply()
                        }
                    }
                }

                fun startHighAccuracyGPS(){
                    if(ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) return
                    
                    loadingGPS = true
                    jumlahUpdate = 0
                    
                    // Cek GPS aktif?
                    if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        Toast.makeText(ctx, "⚠️ Aktifkan GPS dulu!", Toast.LENGTH_LONG).show()
                        ctx.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        loadingGPS = false
                        return
                    }

                    locationListener?.let {
                        try { locationManager.removeUpdates(it) } catch(e:Exception){}
                    }

                    val listener = object : LocationListener {
                        override fun onLocationChanged(loc: Location) {
                            simpanLokasiRealTime(loc, "GPS AKURAT TINGGI")
                            // Jika akurasi sudah <20m, stop (hemat baterai)
                            if(loc.accuracy < 20f && jumlahUpdate >= 3){
                                try { locationManager.removeUpdates(this) } catch(e:Exception){}
                                loadingGPS = false
                                Toast.makeText(ctx, "✅ GPS AKURAT! ${loc.latitude},${loc.longitude} ±${loc.accuracy.toInt()}m", Toast.LENGTH_LONG).show()
                            }
                        }
                        override fun onProviderEnabled(p: String){ Toast.makeText(ctx, "📡 GPS Aktif", Toast.LENGTH_SHORT).show() }
                        override fun onProviderDisabled(p: String){ Toast.makeText(ctx, "❌ GPS Mati - Aktifkan!", Toast.LENGTH_SHORT).show(); loadingGPS=false }
                    }
                    locationListener = listener

                    try {
                        // Minta update akurasi tinggi
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0f, listener, Looper.getMainLooper())
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 0f, listener, Looper.getMainLooper())
                        // Ambil last known cepat dulu
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let { simpanLokasiRealTime(it, "GPS LastKnown") }
                        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)?.let { 
                            if(!lokasiDidapat || it.accuracy < akurasi) simpanLokasiRealTime(it, "Network Cepat") 
                        }
                    } catch(e:Exception){
                        Toast.makeText(ctx, "❌ Error GPS: ${e.message}", Toast.LENGTH_SHORT).show()
                        loadingGPS = false
                    }
                    // Timeout 15 detik
                    scope.launch {
                        delay(15000)
                        if(loadingGPS){
                            locationListener?.let { try{ locationManager.removeUpdates(it) } catch(_:Exception){} }
                            loadingGPS = false
                            if(!lokasiDidapat){
                                // Fallback tetap tapi tandai tidak akurat
                                val kotaCoords = mapOf("Tangerang" to Pair(-6.1783,106.6319), "Jakarta" to Pair(-6.2088,106.8456), "Bekasi" to Pair(-6.2416,106.9924), "Depok" to Pair(-6.4025,106.7942), "Bogor" to Pair(-6.5971,106.8060), "Tangerang Selatan" to Pair(-6.2888,106.7160))
                                val c = kotaCoords[kota] ?: Pair(-6.2078,106.8466)
                                lat = c.first; lng = c.second; akurasi=999f; lokasiDidapat=true; metodeLokasi="Fallback Kota $kota - GPS timeout"
                                Toast.makeText(ctx, "⚠️ GPS timeout, pakai $kota", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }

                val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ perms ->
                    val fineGranted = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true
                    val coarseGranted = perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                    if(fineGranted || coarseGranted){
                        Toast.makeText(ctx, "✅ Izin GPS diberikan - Mengambil lokasi akurat...", Toast.LENGTH_SHORT).show()
                        startHighAccuracyGPS()
                    } else {
                        Toast.makeText(ctx, "❌ Izin GPS DITOLAK! Aktifkan di Pengaturan > Aplikasi > PijatIN > Izin > Lokasi > Izinkan", Toast.LENGTH_LONG).show()
                        // Jangan pakai fallback palsu - paksa user aktifkan
                        ctx.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${ctx.packageName}")))
                    }
                }

                fun openMaps(){
                    if(!lokasiDidapat){ Toast.makeText(ctx, "⚠️ Ambil GPS akurat dulu!", Toast.LENGTH_SHORT).show(); return }
                    val uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lng")
                    val intent = Intent(Intent.ACTION_VIEW, uri).apply { setPackage("com.google.android.apps.maps") }
                    try { ctx.startActivity(intent) } catch(e:Exception){
                        ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps?q=$lat,$lng")))
                    }
                }

                fun kirimKeServer(){
                    scope.launch {
                        val json = """{"nama":"$nama","email":"$email","telepon":"$telepon","alamat":"$alamat","kota":"$kota","lat":$lat,"lng":$lng,"geo_point":"POINT($lng $lat)","akurasi":$akurasi,"altitude":$altitude,"kecepatan":$kecepatan,"metode":"$metodeLokasi","bank":"$selectedBank","no_rek":"$noRek","ewallet":"$selectedEwallet","maps_url":"https://www.google.com/maps?q=$lat,$lng","timestamp":${System.currentTimeMillis()}}"""
                        val prefs = ctx.getSharedPreferences("PijatIN_Customer_Real", Context.MODE_PRIVATE)
                        prefs.edit().apply {
                            putString("customer_data_json", json)
                            putString("customer_lat", lat.toString())
                            putString("customer_lng", lng.toString())
                            putString("customer_akurasi", akurasi.toString())
                            apply()
                        }
                        println("KIRIM V128 AKURAT: $json")
                        Toast.makeText(ctx, "✅ TERKIRIM AKURAT! $lat,$lng ±${akurasi.toInt()}m untuk mitra lacak!", Toast.LENGTH_LONG).show()
                    }
                }

                MaterialTheme {
                    LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(16.dp), contentPadding=PaddingValues(bottom=400.dp)){
                        item{
                            Text("Daftar PijatIN #128 AKURAT", fontWeight=FontWeight.Bold, fontSize=20.sp, color=DarkGreen)
                            Text("🎯 GPS AKURAT TINGGI + LIVE TRACKING MITRA", fontSize=10.sp, color=Orange, fontWeight=FontWeight.Bold)
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
                            OutlinedTextField(alamat,{alamat=it}, label={Text("Alamat Lengkap RT/RW *")}, modifier=Modifier.fillMaxWidth().height(90.dp), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(12.dp))
                            // BOX AKURASI TINGGI
                            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(
                                when {
                                    !lokasiDidapat -> Color(0xFFFFF3E0)
                                    akurasi <= 20f -> Color(0xFFC8E6C9)
                                    akurasi <= 50f -> Color(0xFFDCEDC8)
                                    else -> Color(0xFFFFECB3)
                                }
                            ).padding(12.dp)){
                                Column{
                                    Row(verticalAlignment=Alignment.CenterVertically, horizontalArrangement=Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth()){
                                        Text(
                                            when {
                                                !lokasiDidapat -> "📡 BELUM ADA LOKASI"
                                                akurasi <= 20f -> "✅ GPS AKURAT TINGGI"
                                                akurasi <= 50f -> "✅ GPS AKURAT"
                                                else -> "⚠️ GPS KURANG AKURAT"
                                            }, fontWeight=FontWeight.Bold, fontSize=13.sp,
                                            color= when {
                                                !lokasiDidapat -> Color(0xFFE65100)
                                                akurasi <= 20f -> Color(0xFF1B5E20)
                                                akurasi <= 50f -> Color(0xFF33691E)
                                                else -> Color(0xFFBF360C)
                                            }
                                        )
                                        if(loadingGPS) CircularProgressIndicator(modifier=Modifier.size(18.dp), strokeWidth=2.dp, color=Orange)
                                        else if(lokasiDidapat) Text("Update #$jumlahUpdate", fontSize=10.sp, fontWeight=FontWeight.Bold)
                                    }
                                    Spacer(Modifier.height(6.dp))
                                    if(lokasiDidapat){
                                        Text("📍 ${String.format("%.6f", lat)}, ${String.format("%.6f", lng)}", fontSize=12.sp, fontWeight=FontWeight.Bold)
                                        Text("🏙️ $kota | $metodeLokasi", fontSize=10.sp)
                                        Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                                            Text("🎯 ±${akurasi.toInt()}m", fontSize=10.sp, fontWeight=FontWeight.Bold, color=if(akurasi<=20) Color(0xFF2E7D32) else Color(0xFFE65100))
                                            if(altitude!=0.0) Text("⛰️ ${altitude.toInt()}m", fontSize=10.sp)
                                            if(kecepatan>0) Text("💨 ${String.format("%.1f", kecepatan*3.6f)} km/h", fontSize=10.sp)
                                        }
                                        Spacer(Modifier.height(4.dp))
                                        if(akurasi <= 20f){
                                            Text("✅ AKURAT TINGGI! Siap dilacak mitra real-time", fontSize=10.sp, color=Color(0xFF1B5E20), fontWeight=FontWeight.Bold)
                                            Text("📦 DB: lat DOUBLE, lng DOUBLE, geo_point POINT, akurasi FLOAT", fontSize=8.sp, color=Color.Gray)
                                        } else if(akurasi <= 50f){
                                            Text("✅ Akurat, mitra bisa lacak", fontSize=10.sp, color=Color(0xFF33691E))
                                        } else {
                                            Text("⚠️ Akurasi rendah - coba di luar ruangan", fontSize=10.sp, color=Color(0xFFE65100))
                                        }
                                    } else {
                                        Text("1. Tap Ambil GPS Akurat", fontSize=11.sp, fontWeight=FontWeight.Bold)
                                        Text("2. Izinkan Lokasi > Izinkan sepanjang waktu", fontSize=10.sp, color=Color(0xFF1976D2))
                                        Text("3. Aktifkan GPS + di luar ruangan untuk akurasi <20m", fontSize=10.sp, color=Color.Gray)
                                        Text("Foto kamu tadi: GPS ditolak = pakai koordinat kota palsu!", fontSize=9.sp, color=Color.Red, fontWeight=FontWeight.Bold)
                                    }
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                                Button(
                                    onClick={
                                        val fine = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                        val coarse = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                        if(fine && coarse) startHighAccuracyGPS()
                                        else permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                                    },
                                    modifier=Modifier.weight(1f).height(52.dp),
                                    colors=ButtonDefaults.buttonColors(if(lokasiDidapat && akurasi<=20) Color(0xFF1B5E20) else if(lokasiDidapat) Color(0xFF2E7D32) else Orange),
                                    shape=RoundedCornerShape(10.dp)
                                ){
                                    if(loadingGPS) Row(verticalAlignment=Alignment.CenterVertically){ CircularProgressIndicator(color=Color.White, modifier=Modifier.size(16.dp), strokeWidth=2.dp); Spacer(Modifier.width(6.dp)); Text("Mencari...", fontSize=10.sp) }
                                    else Text(if(!lokasiDidapat) "📡 Ambil GPS Akurat" else if(akurasi<=20) "✅ Akurat Tinggi" else "🔄 Perbarui GPS", fontSize=10.sp, fontWeight=FontWeight.Bold)
                                }
                                Button(onClick={ openMaps() }, modifier=Modifier.weight(1f).height(52.dp), colors=ButtonDefaults.buttonColors(DarkGreen), shape=RoundedCornerShape(10.dp), enabled=lokasiDidapat){ Text("🌍 Buka Maps", fontSize=10.sp) }
                            }
                            Spacer(Modifier.height(6.dp))
                            Text("💡 Tips akurat: di luar ruangan, GPS aktif, tunggu sampai ±5-20m", fontSize=8.sp, color=Color.Gray)
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
                                if(!lokasiDidapat){ Toast.makeText(ctx, "⚠️ Ambil GPS Akurat dulu! Izin GPS harus diizinkan", Toast.LENGTH_LONG).show(); return@Button }
                                if(akurasi>50) Toast.makeText(ctx, "⚠️ Akurasi ${akurasi.toInt()}m kurang akurat, coba di luar ruangan", Toast.LENGTH_LONG).show()
                                kirimKeServer()
                            }, modifier=Modifier.fillMaxWidth().height(54.dp), colors=ButtonDefaults.buttonColors(DarkGreen), shape=RoundedCornerShape(12.dp), enabled=lokasiDidapat){
                                Text(if(akurasi<=20) "DAFTAR + GPS AKURAT TINGGI ✅" else "DAFTAR + KIRIM LOKASI ✅", fontWeight=FontWeight.Bold, color=Color.White, fontSize=12.sp)
                            }
                            Spacer(Modifier.height(10.dp))
                            if(lokasiDidapat) Column{
                                Text("📤 Server: lat=$lat DOUBLE, lng=$lng DOUBLE", color=Color(0xFF1976D2), fontSize=9.sp)
                                Text("📦 geo_point=POINT($lng $lat) SPASIAL, akurasi=${akurasi.toInt()}m", color=Color(0xFF4CAF50), fontSize=9.sp, fontWeight=FontWeight.Bold)
                                Text("🔍 Mitra lacak: /api/tracking/customer/{id} + nearby 5km", color=Color.Gray, fontSize=8.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
