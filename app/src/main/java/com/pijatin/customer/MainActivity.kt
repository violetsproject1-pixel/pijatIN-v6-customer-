package com.pijatin.customer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
            var bank by remember { mutableStateOf("Mandiri") }
            var namaRek by remember { mutableStateOf("") }
            var noRek by remember { mutableStateOf("") }
            var pass by remember { mutableStateOf("") }
            var confirm by remember { mutableStateOf("") }
            var alamat by remember { mutableStateOf("") }
            var foto by remember { mutableStateOf(false) }
            var ktp by remember { mutableStateOf(false) }
            var maps by remember { mutableStateOf(false) }

            MaterialTheme {
                LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
                    item {
                        Text("#61 FIX LENGKAP - PASTI HIJAU", fontWeight=FontWeight.Bold, color=DarkGreen, fontSize=18.sp)
                        Text("Fix: Nama Rekening + No Rekening + Confirm Pass", color=Orange, fontSize=12.sp, fontWeight=FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(nama, {nama=it}, label={Text("Nama Lengkap")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(hp, {hp=it}, label={Text("No HP (unique)")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(email, {email=it}, label={Text("Email")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(alamat, {alamat=it}, label={Text("Alamat + Maps 150px")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        Button(onClick={maps=true}, colors=ButtonDefaults.buttonColors(DarkGreen), modifier=Modifier.fillMaxWidth()){ Text(if(maps) "✅ Maps 150px Terpilih" else "Gunakan Lokasi Ini + Pilih di Peta 10x10") }
                        Spacer(Modifier.height(12.dp))
                        Text("Bank: $bank", fontWeight=FontWeight.Bold)
                        Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){ 
                            listOf("BCA","BRI","BNI","Mandiri","DANA").forEach{ b ->
                                Button(onClick={bank=b}, colors=ButtonDefaults.buttonColors(if(bank==b) DarkGreen else Color.Gray)){ Text(b, fontSize=10.sp) }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(namaRek, {namaRek=it}, label={Text("Nama Rekening (sesuai KTP) *BARU*")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(noRek, {noRek=it}, label={Text("No Rekening / e-Wallet *BARU*")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                            Button(onClick={foto=true}){ Text(if(foto) "✅ Foto 80px OK" else "Foto 80px circle wajib") }
                            Button(onClick={ktp=true}){ Text(if(ktp) "✅ KTP 200px OK" else "KTP 200px wajib") }
                        }
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(pass, {pass=it}, label={Text("Password")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(confirm, {confirm=it}, label={Text("Konfirmasi Password *BARU*")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth())
                        if(confirm.isNotEmpty()){
                            Text(if(confirm==pass) "✅ Password cocok" else "❌ Tidak sama", color=if(confirm==pass) Color(0xFF4CAF50) else Color.Red, fontSize=12.sp)
                        }
                        Spacer(Modifier.height(16.dp))
                        val ok = nama.isNotEmpty() && hp.isNotEmpty() && namaRek.isNotEmpty() && noRek.isNotEmpty() && foto && ktp && maps && pass==confirm && pass.isNotEmpty()
                        Button(onClick={}, enabled=ok, modifier=Modifier.fillMaxWidth().height(48.dp), colors=ButtonDefaults.buttonColors(DarkGreen), shape=RoundedCornerShape(12.dp)){ Text("Daftar → Saldo 0") }
                        if(ok) Text("✅ Lengkap! Siap daftar #61", color=Color(0xFF4CAF50), fontWeight=FontWeight.Bold)
                    }
                }
            }
        }
    }
}
