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
            var alamat by remember { mutableStateOf("") }
            var bank by remember { mutableStateOf("Mandiri") }
            var namaRek by remember { mutableStateOf("") }
            var noRek by remember { mutableStateOf("") }
            var pass by remember { mutableStateOf("") }
            var confirm by remember { mutableStateOf("") }
            var fotoOk by remember { mutableStateOf(false) }
            var ktpOk by remember { mutableStateOf(false) }
            var mapsOk by remember { mutableStateOf(false) }

            MaterialTheme {
                LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
                    item {
                        Text("Daftar #63 FIX LENGKAP", fontWeight=FontWeight.Bold, color=DarkGreen, fontSize=18.sp)
                        Text("Fix: Nama Rek + No Rek + Confirm Pass ada!", color=Orange, fontSize=12.sp, fontWeight=FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(nama, {nama=it}, label={Text("Nama Lengkap")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(hp, {hp=it}, label={Text("No HP (unique)")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(email, {email=it}, label={Text("Email")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(alamat, {alamat=it}, label={Text("Alamat + Maps 150px")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        Button(onClick={mapsOk=true}, colors=ButtonDefaults.buttonColors(DarkGreen), modifier=Modifier.fillMaxWidth()){ Text(if(mapsOk) "✅ Maps 150px Terpilih (-6.20, 106.63)" else "Gunakan Lokasi Ini + Pilih di Peta 10x10") }
                        Spacer(Modifier.height(12.dp))
                        Text("Bank: $bank", fontWeight=FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){
                            listOf("BCA","BRI","BNI","Mandiri","DANA").forEach{ b ->
                                Button(onClick={bank=b}, colors=ButtonDefaults.buttonColors(if(bank==b) DarkGreen else Color.Gray), modifier=Modifier.height(32.dp)){ Text(b, fontSize=10.sp) }
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        // INI YANG KEMARIN HILANG - SEKARANG ADA!
                        OutlinedTextField(namaRek, {namaRek=it}, label={Text("Nama Rekening (sesuai KTP) *BARU*")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(noRek, {noRek=it}, label={Text("No Rekening / No e-Wallet *BARU*")}, placeholder={Text("Contoh: 1234567890")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                            Button(onClick={fotoOk=!fotoOk}){ Text(if(fotoOk) "✅ Foto 80px OK" else "Foto 80px circle wajib") }
                            Button(onClick={ktpOk=!ktpOk}){ Text(if(ktpOk) "✅ KTP 200px OK" else "KTP 200px wajib") }
                        }
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(pass, {pass=it}, label={Text("Password")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        // INI YANG KEMARIN HILANG - SEKARANG ADA!
                        OutlinedTextField(confirm, {confirm=it}, label={Text("Konfirmasi Password *BARU*")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth())
                        if(confirm.isNotEmpty()){
                            Text(if(confirm==pass) "✅ Password cocok" else "❌ Tidak sama", color=if(confirm==pass) Color(0xFF4CAF50) else Color.Red, fontSize=12.sp)
                        }
                        Spacer(Modifier.height(16.dp))
                        val ok = nama.isNotEmpty() && hp.isNotEmpty() && namaRek.isNotEmpty() && noRek.isNotEmpty() && fotoOk && ktpOk && mapsOk && pass.isNotEmpty() && pass==confirm
                        Button(onClick={}, enabled=ok, modifier=Modifier.fillMaxWidth().height(48.dp), colors=ButtonDefaults.buttonColors(DarkGreen), shape=RoundedCornerShape(12.dp)){ Text("Daftar → Saldo 0") }
                        Spacer(Modifier.height(6.dp))
                        if(!ok) Text("❌ Lengkapi: Nama, HP, Nama Rek, No Rek, Foto, KTP, Maps, Pass sama", fontSize=10.sp, color=Color.Red)
                        else Text("✅ Semua lengkap! Siap daftar #63", color=Color(0xFF4CAF50), fontWeight=FontWeight.Bold)
                    }
                }
            }
        }
    }
}
