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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
val LightGray = Color(0xFFE0E0E0)

class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContent {
            var showSplash by remember { mutableStateOf(true) }
            LaunchedEffect(Unit){ delay(1200); showSplash=false }
            if(showSplash){
                Box(Modifier.fillMaxSize().background(DarkGreen), contentAlignment=Alignment.Center){
                    Column(horizontalAlignment=Alignment.CenterHorizontally){
                        Text("PijatIN", color=Color.White, fontSize=32.sp, fontWeight=FontWeight.Bold)
                        Text("Customer REAL V129 SIMPEL", color=Color(0xFFFFD700), fontSize=14.sp)
                        Spacer(Modifier.height(20.dp))
                        CircularProgressIndicator(color=Color.White)
                        Text("#129 BANK & EWALLET CLEAN", color=Color.White, fontSize=11.sp)
                    }
                }
            } else {
                val ctx = LocalContext.current
                val scope = rememberCoroutineScope()
                var namaLengkap by remember { mutableStateOf("maliki") }
                var telepon by remember { mutableStateOf("083893330346") }
                var email by remember { mutableStateOf("malikysyachmal2018@gmail.com") }
                var alamat by remember { mutableStateOf("GG cemara2 RT 01/01 kunciran jaya pinang Tangerang") }
                var kota by remember { mutableStateOf("Tangerang") }
                var pass by remember { mutableStateOf("") }
                var confirm by remember { mutableStateOf("") }
                var fotoUri by remember { mutableStateOf<Uri?>(null) }
                var ktpUri by remember { mutableStateOf<Uri?>(null) }
                var lat by remember { mutableStateOf(0.0) }
                var lng by remember { mutableStateOf(0.0) }
                var akurasi by remember { mutableStateOf(999f) }
                var lokasiDidapat by remember { mutableStateOf(false) }
                var loadingGPS by remember { mutableStateOf(false) }
                var metodeLokasi by remember { mutableStateOf("Belum") }
                // BANK & EWALLET - SIMPEL, HAPUS NAMA REKENING & NO REKENING
                var selectedBank by remember { mutableStateOf("BCA") }
                var selectedEwallet by remember { mutableStateOf("DANA") }
                var noEwallet by remember { mutableStateOf("083893330346") }

                val fotoGaleri = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { if(it!=null) fotoUri=it }
                val ktpGaleri = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { if(it!=null) ktpUri=it }
                val fotoKamera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { if(it!=null) fotoUri=Uri.parse("camera") }

                val locationManager = remember { ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager }
                var locationListener by remember { mutableStateOf<LocationListener?>(null) }

                fun simpanLokasi(loc: Location, metode: String){
                    lat = loc.latitude; lng = loc.longitude; akurasi = loc.accuracy
                    if(akurasi <= 60f){
                        lokasiDidapat = true; metodeLokasi = "$metode ±${akurasi.toInt()}m"
                        val prefs = ctx.getSharedPreferences("PijatIN_Order_Data", Context.MODE_PRIVATE)
                        prefs.edit().apply {
                            putString("order_customer_lat", lat.toString()); putString("order_customer_lng", lng.toString())
                            putFloat("order_customer_akurasi", akurasi); putString("order_customer_kota", kota)
                            putString("order_customer_alamat", alamat); putString("order_maps_url", "https://www.google.com/maps?q=$lat,$lng")
                            putBoolean("order_lokasi_valid", true); apply()
                        }
                    }
                }
                fun startGPS(){
                    if(ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) return
                    loadingGPS = true
                    if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        Toast.makeText(ctx, "Aktifkan GPS!", Toast.LENGTH_LONG).show()
                        ctx.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)); loadingGPS=false; return
                    }
                    locationListener?.let { try{ locationManager.removeUpdates(it) }catch(_:Exception){} }
                    val listener = object : LocationListener {
                        override fun onLocationChanged(l: Location){
                            simpanLokasi(l, "GPS Akurat"); if(l.accuracy < 20f){ try{ locationManager.removeUpdates(this) }catch(_:Exception){}; loadingGPS=false; Toast.makeText(ctx, "✅ Akurat! $l", Toast.LENGTH_SHORT).show() }
                        }
                        override fun onProviderDisabled(p:String){ loadingGPS=false }
                    }
                    locationListener = listener
                    try{
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0f, listener, Looper.getMainLooper())
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 0f, listener, Looper.getMainLooper())
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let{ simpanLokasi(it,"LastKnown") }
                    }catch(e:Exception){ loadingGPS=false }
                    scope.launch{ delay(12000); if(loadingGPS){ locationListener?.let{ try{ locationManager.removeUpdates(it) }catch(_:Exception){} }; loadingGPS=false } }
                }
                val permLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ p ->
                    if(p[Manifest.permission.ACCESS_FINE_LOCATION]==true) startGPS() else Toast.makeText(ctx,"Izin GPS ditolak - aktifkan di Pengaturan",Toast.LENGTH_LONG).show()
                }
                fun openMaps(){
                    if(!lokasiDidapat){ Toast.makeText(ctx,"Ambil GPS dulu!",Toast.LENGTH_SHORT).show(); return }
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps?q=$lat,$lng")); i.setPackage("com.google.android.apps.maps")
                    try{ ctx.startActivity(i) }catch(_:Exception){ ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps?q=$lat,$lng"))) }
                }

                MaterialTheme {
                    LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(16.dp), contentPadding=PaddingValues(bottom=100.dp)){
                        item{
                            Text("Daftar PijatIN #129 SIMPEL", fontWeight=FontWeight.Bold, fontSize=20.sp, color=DarkGreen)
                            Text("🎯 GPS AKURAT + BANK & EWALLET CLEAN (Tanpa Nama/No Rekening)", fontSize=9.sp, color=Orange, fontWeight=FontWeight.Bold)
                            Spacer(Modifier.height(16.dp))
                            // NAMA LENGKAP TETAP ADA (sesuai foto 1), YANG DIHAPUS: Nama Rekening & No Rekening
                            OutlinedTextField(namaLengkap,{namaLengkap=it}, label={Text("Nama Lengkap *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(telepon,{telepon=it}, label={Text("No Telepon *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(email,{email=it}, label={Text("Email *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(16.dp))
                            Text("📍 Lokasi - Ambil GPS + Manual Kota", fontWeight=FontWeight.Bold, fontSize=13.sp, color=DarkGreen)
                            Spacer(Modifier.height(8.dp))
                            Text("Pilih Kota *", fontSize=11.sp, fontWeight=FontWeight.Bold)
                            Spacer(Modifier.height(6.dp))
                            Row(horizontalArrangement=Arrangement.spacedBy(8.dp), modifier=Modifier.fillMaxWidth()){
                                listOf("Tangerang","Jakarta","Bekasi").forEach{ k ->
                                    val sel = kota==k
                                    Box(Modifier.weight(1f).height(44.dp).clip(RoundedCornerShape(10.dp)).background(if(sel) DarkGreen else LightGray).clickable{ kota=k }, contentAlignment=Alignment.Center){
                                        Text(k, color=if(sel) Color.White else Color.Black, fontSize=11.sp, fontWeight=if(sel) FontWeight.Bold else FontWeight.Normal)
                                    }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                                listOf("Depok","Bogor","Tangsel").forEach{ k ->
                                    val v = if(k=="Tangsel") "Tangerang Selatan" else k
                                    val sel = kota==v
                                    Box(Modifier.weight(1f).height(44.dp).clip(RoundedCornerShape(10.dp)).background(if(sel) DarkGreen else Color(0xFFE8E8E8)).clickable{ kota=v }, contentAlignment=Alignment.Center){
                                        Text(k, color=if(sel) Color.White else Color.Black, fontSize=11.sp, fontWeight=if(sel) FontWeight.Bold else FontWeight.Normal)
                                    }
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(alamat,{alamat=it}, label={Text("Alamat Lengkap RT/RW *")}, modifier=Modifier.fillMaxWidth().height(90.dp), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(12.dp))
                            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(if(lokasiDidapat) Color(0xFFC8E6C9) else Color(0xFFFFF3E0)).padding(12.dp)){
                                Column{
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically){
                                        Text(if(lokasiDidapat) "✅ GPS AKURAT" else "📡 BELUM ADA LOKASI", fontWeight=FontWeight.Bold, fontSize=13.sp, color=if(lokasiDidapat) Color(0xFF1B5E20) else Color(0xFFE65100))
                                        if(loadingGPS) CircularProgressIndicator(modifier=Modifier.size(18.dp), strokeWidth=2.dp) else if(lokasiDidapat) Text(metodeLokasi, fontSize=10.sp)
                                    }
                                    if(lokasiDidapat){ Spacer(Modifier.height(4.dp)); Text("📍 ${String.format("%.6f",lat)}, ${String.format("%.6f",lng)} ±${akurasi.toInt()}m", fontSize=11.sp, fontWeight=FontWeight.Bold); Text("🏙️ $kota - ${alamat.take(35)}...", fontSize=10.sp) }
                                    else { Spacer(Modifier.height(6.dp)); Text("1. Tap Ambil GPS Akurat", fontWeight=FontWeight.Bold, fontSize=11.sp); Text("2. Izinkan Lokasi", fontSize=10.sp, color=Color(0xFF1976D2)); Text("3. Aktifkan GPS + di luar ruangan", fontSize=9.sp, color=Color.Gray) }
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                                Button(onClick={
                                    val fine = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
                                    if(fine) startGPS() else permLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                                }, modifier=Modifier.weight(1f).height(52.dp), colors=ButtonDefaults.buttonColors(if(lokasiDidapat) Color(0xFF2E7D32) else Orange), shape=RoundedCornerShape(10.dp)){
                                    if(loadingGPS) CircularProgressIndicator(color=Color.White, modifier=Modifier.size(16.dp)) else Text(if(lokasiDidapat) "✅ Akurat ±${akurasi.toInt()}m" else "📡 Ambil GPS Akurat", fontSize=10.sp, fontWeight=FontWeight.Bold)
                                }
                                Button(onClick={ openMaps() }, modifier=Modifier.weight(1f).height(52.dp), colors=ButtonDefaults.buttonColors(DarkGreen), shape=RoundedCornerShape(10.dp), enabled=lokasiDidapat){ Text("🌍 Buka Maps", fontSize=11.sp) }
                            }
                            Spacer(Modifier.height(20.dp))
                            // ====== UBAH REKENING BANK & EWALLET - HAPUS NAMA REKENING & NO REKENING ======
                            Text("🏦 Pilih Rekening Bank *", fontWeight=FontWeight.Bold, fontSize=13.sp, color=DarkGreen)
                            Text("Pilih 1 bank untuk pencairan (tanpa input no rekening)", fontSize=9.sp, color=Color.Gray)
                            Spacer(Modifier.height(8.dp))
                            // DESIGN BARU BANK - LEBIH CLEAN
                            Column(verticalArrangement=Arrangement.spacedBy(8.dp)){
                                Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                                    listOf("BCA","MANDIRI").forEach{ bank ->
                                        val sel = selectedBank==bank
                                        Box(Modifier.weight(1f).height(48.dp).clip(RoundedCornerShape(12.dp)).background(if(sel) DarkGreen else Color.White).border(2.dp, if(sel) DarkGreen else Color(0xFFCCCCCC), RoundedCornerShape(12.dp)).clickable{ selectedBank=bank }, contentAlignment=Alignment.Center){
                                            Row(verticalAlignment=Alignment.CenterVertically){
                                                if(sel) Text("✅ ", fontSize=12.sp)
                                                Text(bank, fontWeight=FontWeight.Bold, fontSize=12.sp, color=if(sel) Color.White else Color.Black)
                                            }
                                        }
                                    }
                                }
                                Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                                    listOf("BRI","SEABANK").forEach{ bank ->
                                        val sel = selectedBank==bank
                                        Box(Modifier.weight(1f).height(48.dp).clip(RoundedCornerShape(12.dp)).background(if(sel) DarkGreen else Color.White).border(2.dp, if(sel) DarkGreen else Color(0xFFCCCCCC), RoundedCornerShape(12.dp)).clickable{ selectedBank=bank }, contentAlignment=Alignment.Center){
                                            Row(verticalAlignment=Alignment.CenterVertically){
                                                if(sel) Text("✅ ", fontSize=12.sp)
                                                Text(bank, fontWeight=FontWeight.Bold, fontSize=12.sp, color=if(sel) Color.White else Color.Black)
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(Color(0xFFE8F5E9)).padding(8.dp)){
                                Text("✅ Bank terpilih: $selectedBank - No Rekening akan diminta saat pencairan", fontSize=10.sp, color=Color(0xFF2E7D32), fontWeight=FontWeight.Bold)
                            }

                            Spacer(Modifier.height(20.dp))
                            Text("📱 Pilih E-Wallet *", fontWeight=FontWeight.Bold, fontSize=13.sp, color=DarkGreen)
                            Text("Pilih 1 ewallet untuk topup/pencairan cepat", fontSize=9.sp, color=Color.Gray)
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                                listOf("DANA","OVO","GOPAY").forEach{ ew ->
                                    val sel = selectedEwallet==ew
                                    Box(Modifier.weight(1f).height(56.dp).clip(RoundedCornerShape(12.dp)).background(if(sel) Orange else Color.White).border(2.dp, if(sel) Orange else Color(0xFFCCCCCC), RoundedCornerShape(12.dp)).clickable{ selectedEwallet=ew }, contentAlignment=Alignment.Center){
                                        Column(horizontalAlignment=Alignment.CenterHorizontally){
                                            if(sel) Text("✅", fontSize=14.sp)
                                            Text(ew, fontWeight=FontWeight.Bold, fontSize=11.sp, color=if(sel) Color.White else Color.Black)
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                            OutlinedTextField(noEwallet,{noEwallet=it}, label={Text("No E-Wallet $selectedEwallet *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp), placeholder={Text("083893330346")})
                            Spacer(Modifier.height(8.dp))
                            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(Color(0xFFFFF3E0)).padding(8.dp)){
                                Text("📱 E-Wallet terpilih: $selectedEwallet - $noEwallet", fontSize=10.sp, color=Color(0xFFE65100), fontWeight=FontWeight.Bold)
                            }

                            Spacer(Modifier.height(20.dp))
                            // FOTO & KTP TETAP
                            Row(horizontalArrangement=Arrangement.spacedBy(12.dp)){
                                Column(horizontalAlignment=Alignment.CenterHorizontally){
                                    Box(Modifier.size(80.dp).clip(CircleShape).background(if(fotoUri!=null) Color(0xFF4CAF50) else Color.LightGray), contentAlignment=Alignment.Center){ Text(if(fotoUri!=null) "✅" else "FOTO", color=Color.White, fontWeight=FontWeight.Bold, fontSize=11.sp) }
                                    Spacer(Modifier.height(8.dp))
                                    Button(onClick={ fotoGaleri.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, modifier=Modifier.width(110.dp).height(36.dp), shape=RoundedCornerShape(8.dp)){ Text("GALERI", fontSize=10.sp) }
                                    Spacer(Modifier.height(6.dp))
                                    Button(onClick={ fotoKamera.launch(null) }, modifier=Modifier.width(110.dp).height(36.dp), colors=ButtonDefaults.buttonColors(Orange), shape=RoundedCornerShape(8.dp)){ Text("KAMERA", fontSize=10.sp) }
                                }
                                Column(Modifier.weight(1f)){
                                    Box(Modifier.fillMaxWidth().height(110.dp).clip(RoundedCornerShape(12.dp)).background(if(ktpUri!=null) Color(0xFF4CAF50) else Color(0xFF9E9E9E)), contentAlignment=Alignment.Center){ Text(if(ktpUri==null) "KTP 200px" else "✅ KTP OK", color=Color.White, fontWeight=FontWeight.Bold, fontSize=12.sp) }
                                    Spacer(Modifier.height(8.dp))
                                    Button(onClick={ ktpGaleri.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, modifier=Modifier.fillMaxWidth().height(40.dp), shape=RoundedCornerShape(8.dp)){ Text("KTP GALERI", fontSize=11.sp) }
                                }
                            }
                            Spacer(Modifier.height(20.dp))
                            OutlinedTextField(pass,{pass=it}, label={Text("Password *")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(confirm,{confirm=it}, label={Text("Konfirmasi Password *")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(10.dp))
                            if(confirm.isNotEmpty()) Text(if(confirm==pass) "✅ Password cocok" else "❌ Tidak sama", color=if(confirm==pass) Color(0xFF4CAF50) else Color.Red, fontSize=12.sp, fontWeight=FontWeight.Bold)
                            Spacer(Modifier.height(24.dp))
                            Button(onClick={
                                if(!lokasiDidapat){ Toast.makeText(ctx,"Ambil GPS dulu!",Toast.LENGTH_SHORT).show(); return@Button }
                                if(confirm!=pass){ Toast.makeText(ctx,"Password tidak sama!",Toast.LENGTH_SHORT).show(); return@Button }
                                val json = """{"nama":"$namaLengkap","email":"$email","telp":"$telepon","alamat":"$alamat","kota":"$kota","lat":$lat,"lng":$lng,"bank":"$selectedBank","ewallet":"$selectedEwallet","no_ewallet":"$noEwallet"}"""
                                println("DAFTAR V129 SIMPEL: $json")
                                Toast.makeText(ctx,"✅ DAFTAR SIMPEL! Bank:$selectedBank Ewallet:$selectedEwallet $noEwallet Lokasi:$lat,$lng",Toast.LENGTH_LONG).show()
                            }, modifier=Modifier.fillMaxWidth().height(56.dp), colors=ButtonDefaults.buttonColors(DarkGreen), shape=RoundedCornerShape(12.dp)){
                                Text("DAFTAR V129 SIMPEL ✅", fontWeight=FontWeight.Bold, color=Color.White, fontSize=14.sp)
                            }
                            Spacer(Modifier.height(12.dp))
                            Text("✅ HAPUS: Nama Rekening & No Rekening BCA", fontSize=9.sp, color=Color(0xFF4CAF50), fontWeight=FontWeight.Bold)
                            Text("✅ UBAH: Bank 4 pilihan clean + E-Wallet 3 pilihan clean", fontSize=9.sp, color=Color(0xFF4CAF50))
                            if(lokasiDidapat) Text("📍 $kota $lat,$lng ±${akurasi.toInt()}m | $selectedBank | $selectedEwallet $noEwallet", fontSize=9.sp, color=DarkGreen)
                        }
                    }
                }
            }
        }
    }
}
