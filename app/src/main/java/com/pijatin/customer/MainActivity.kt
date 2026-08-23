package com.pijatin.customer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
            var nama by remember { mutableStateOf("") }
            var hp by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var alamat by remember { mutableStateOf("") }
            var bank by remember { mutableStateOf("Mandiri") }
            var namaRek by remember { mutableStateOf("") }
            var noRek by remember { mutableStateOf("") }
            var pass by remember { mutableStateOf("") }
            var confirm by remember { mutableStateOf("") }
            var foto by remember { mutableStateOf(false) }
            var ktp by remember { mutableStateOf(false) }
            var maps by remember { mutableStateOf(false) }

            MaterialTheme {
                LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
                    item {
                        Text("Daftar PijatIN #68 LENGKAP", fontWeight=FontWeight.Bold, color=DarkGreen, fontSize=20.sp)
                        Text("✅ #67 Hijau 2m50s - Tambah 3 field hilang", color=Orange, fontSize=11.sp, fontWeight=FontWeight.Bold)
                        Spacer(Modifier.height(16.dp))
                        
                        OutlinedTextField(nama, {nama=it}, label={Text("Nama Lengkap")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(hp, {hp=it}, label={Text("No HP (unique)")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(email, {email=it}, label={Text("Email")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(alamat, {alamat=it}, label={Text("Alamat Lengkap")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        
                        // MAPS 150px
                        Box(Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFE0E0E0)), contentAlignment=Alignment.Center){
                            Column(horizontalAlignment=Alignment.CenterHorizontally){
                                Text(if(maps) "📍 -6.2088, 106.8456" else "Maps 150px", fontWeight=FontWeight.Bold)
                                Text("Pilih di Peta 10x10", fontSize=10.sp)
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Button(onClick={maps=true}, colors=ButtonDefaults.buttonColors(DarkGreen), modifier=Modifier.fillMaxWidth()){
                            Text(if(maps) "✅ Gunakan Lokasi Ini - Maps OK" else "Gunakan Lokasi Ini + Pilih di Peta")
                        }
                        Spacer(Modifier.height(12.dp))
                        
                        Text("Pilih Bank / e-Wallet", fontWeight=FontWeight.Bold)
                        Row(horizontalArrangement=Arrangement.spacedBy(6.dp), modifier=Modifier.padding(top=4.dp)){
                            listOf("BCA","BRI","BNI","Mandiri","DANA","OVO","GoPay").forEach{ b ->
                                FilterChip(selected=bank==b, onClick={bank=b}, label={Text(b, fontSize=10.sp)})
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        
                        // INI YANG KEMARIN HILANG!
                        OutlinedTextField(namaRek, {namaRek=it}, label={Text("Nama Rekening (sesuai KTP) *BARU*")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(noRek, {noRek=it}, label={Text("No Rekening / No e-Wallet *BARU*")}, placeholder={Text("1234567890")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(12.dp))
                        
                        // FOTO 80px + KTP 200px
                        Row(horizontalArrangement=Arrangement.spacedBy(12.dp)){
                            Column(horizontalAlignment=Alignment.CenterHorizontally){
                                Box(Modifier.size(80.dp).clip(CircleShape).background(if(foto) Color(0xFF4CAF50) else Color.LightGray), contentAlignment=Alignment.Center){ Text(if(foto) "✅" else "80px", color=Color.White) }
                                Spacer(Modifier.height(4.dp))
                                Button(onClick={foto=!foto}, modifier=Modifier.height(30.dp)){ Text(if(foto) "OK" else "Foto wajib", fontSize=10.sp) }
                            }
                            Column{
                                Box(Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(8.dp)).background(if(ktp) Color(0xFF4CAF50) else Color.LightGray), contentAlignment=Alignment.Center){ Text(if(ktp) "✅ KTP 200px OK" else "KTP 200px wajib tap ganti", color=Color.White) }
                                Spacer(Modifier.height(4.dp))
                                Button(onClick={ktp=!ktp}, modifier=Modifier.fillMaxWidth().height(30.dp)){ Text(if(ktp) "✅ KTP OK" else "Upload KTP", fontSize=10.sp) }
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        
                        OutlinedTextField(pass, {pass=it}, label={Text("Password")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(confirm, {confirm=it}, label={Text("Konfirmasi Password *BARU*")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth())
                        if(confirm.isNotEmpty()){
                            Text(if(confirm==pass) "✅ Password cocok" else "❌ Tidak sama", color=if(confirm==pass) Color(0xFF4CAF50) else Color.Red, fontSize=12.sp, fontWeight=FontWeight.Bold)
                        }
                        Spacer(Modifier.height(20.dp))
                        
                        val ok = nama.isNotEmpty() && hp.isNotEmpty() && namaRek.isNotEmpty() && noRek.isNotEmpty() && foto && ktp && maps && pass.isNotEmpty() && pass==confirm
                        Button(onClick={}, enabled=ok, modifier=Modifier.fillMaxWidth().height(50.dp), colors=ButtonDefaults.buttonColors(DarkGreen), shape=RoundedCornerShape(12.dp)){ Text("Daftar → Saldo 0", fontSize=16.sp) }
                        Spacer(Modifier.height(8.dp))
                        if(!ok) Text("❌ Lengkapi: Nama, HP, Nama Rek (baru), No Rek (baru), Foto 80px, KTP 200px, Maps 150px, Pass cocok", fontSize=11.sp, color=Color.Red)
                        else Text("✅ SEMUA LENGKAP! Siap daftar #68 - Semua field yang hilang sudah ada!", color=Color(0xFF4CAF50), fontWeight=FontWeight.Bold)
                    }
                }
            }
        }
    }
}
