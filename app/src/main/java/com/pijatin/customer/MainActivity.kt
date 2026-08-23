package com.pijatin.customer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

val DarkGreen = Color(0xFF2D4A3E)
val Orange = Color(0xFFFF7A00)

data class Service(val id:String, val name:String, val price:Int)
data class Customer(val nama:String, val hp:String, val email:String, val alamat:String, val bank:String, val namaRek:String, val noRek:String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App59() }
    }
}

@Composable
fun App59(){
    var screen by remember { mutableStateOf("splash") }
    LaunchedEffect(screen){ if(screen=="splash"){ delay(2500); screen="auth" } }
    when(screen){
        "splash" -> Box(Modifier.fillMaxSize().background(DarkGreen), contentAlignment=Alignment.Center){
            Column(horizontalAlignment=Alignment.CenterHorizontally){
                Box(Modifier.size(72.dp).clip(CircleShape).background(Color.White), contentAlignment=Alignment.Center){ Text("A", fontSize=36.sp, fontWeight=FontWeight.Bold, color=DarkGreen) }
                Spacer(Modifier.height(12.dp))
                Text("pijatIN", color=Color.White, fontSize=26.sp, fontWeight=FontWeight.Bold)
                Box(Modifier.width(60.dp).height(3.dp).background(Orange))
            }
        }
        "auth" -> Auth59(onNext={screen="home"})
        "home" -> Box(Modifier.fillMaxSize().background(Color.White), contentAlignment=Alignment.Center){ Text("✅ #59 BERHASIL HIJAU! Daftar lengkap!", fontWeight=FontWeight.Bold, color=DarkGreen) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Auth59(onNext:()->Unit){
    var nama by remember { mutableStateOf("") }
    var hp by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("malikysyachmal2018@gmail.com") }
    var alamat by remember { mutableStateOf("GG cemara2 RT 01/01 kunciran jaya pinang Tangerang") }
    var bank by remember { mutableStateOf("Mandiri") }
    var namaRek by remember { mutableStateOf("") }
    var noRek by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var show1 by remember { mutableStateOf(false) }
    var show2 by remember { mutableStateOf(false) }
    var fotoOk by remember { mutableStateOf(false) }
    var ktpOk by remember { mutableStateOf(false) }
    var mapsOk by remember { mutableStateOf(false) }

    LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp)){
        item{
            Text("Daftar Customer #59 FIX HIJAU", fontWeight=FontWeight.Bold, fontSize=18.sp, color=DarkGreen)
            Text("Tambah: Nama Rek + No Rek + Confirm Pass", fontSize=12.sp, color=Orange, fontWeight=FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(nama,{nama=it}, label={Text("Nama Lengkap")}, modifier=Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(hp,{hp=it}, label={Text("No HP (unique)")}, modifier=Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(email,{email=it}, label={Text("Email")}, modifier=Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(alamat,{alamat=it}, label={Text("Alamat + Maps 150px")}, modifier=Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Box(Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFE8F5E9)).border(1.dp, Color.Gray, RoundedCornerShape(12.dp)), contentAlignment=Alignment.Center){
                Column(horizontalAlignment=Alignment.CenterHorizontally){
                    Text("🗺️ Maps 150px grid", fontSize=12.sp)
                    Spacer(Modifier.height(4.dp))
                    Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                        Button(onClick={mapsOk=true}, colors=ButtonDefaults.buttonColors(DarkGreen), modifier=Modifier.height(32.dp)){ Text(if(mapsOk) "✅ Lokasi Dipilih" else "Gunakan Lokasi Ini", fontSize=10.sp) }
                        Button(onClick={mapsOk=true}, colors=ButtonDefaults.buttonColors(Orange), modifier=Modifier.height(32.dp)){ Text("Pilih di Peta 10x10", fontSize=10.sp) }
                    }
                    if(mapsOk) Text("✅ -6.20, 106.63 Kunciran Jaya", fontSize=10.sp, color=Color(0xFF4CAF50))
                }
            }
            Spacer(Modifier.height(12.dp))
            Text("Bank", fontWeight=FontWeight.Bold, fontSize=13.sp)
            LazyRow(horizontalArrangement=Arrangement.spacedBy(6.dp)){
                items(listOf("BCA","BRI","BNI","Mandiri","Seabank","OVO","GoPay","DANA")){ b ->
                    FilterChip(selected=bank==b, onClick={bank=b}, label={Text(b, fontSize=10.sp)}, colors=FilterChipDefaults.filterChipColors(selectedContainerColor=DarkGreen, selectedLabelColor=Color.White))
                }
            }
            Spacer(Modifier.height(8.dp))
            // INI YANG KEMARIN HILANG - SEKARANG ADA!
            OutlinedTextField(namaRek,{namaRek=it}, label={Text("Nama Rekening (sesuai KTP) *BARU*")}, modifier=Modifier.fillMaxWidth(), colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=DarkGreen))
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(noRek,{noRek=it}, label={Text("No Rekening / No e-Wallet *BARU*")}, placeholder={Text("Contoh: 1234567890")}, modifier=Modifier.fillMaxWidth(), colors=OutlinedTextFieldDefaults.colors(focusedBorderColor=DarkGreen))
            if(bank in listOf("OVO","GoPay","DANA")) Text("⚠️ Untuk $bank isi No HP e-wallet", fontSize=10.sp, color=Orange)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement=Arrangement.spacedBy(12.dp)){
                Card(Modifier.size(80.dp).clip(CircleShape).clickable{fotoOk=!fotoOk}.border(2.dp, if(fotoOk) Orange else Color.Gray, CircleShape), colors=CardDefaults.cardColors(if(fotoOk) Color(0xFFFFF3E0) else Color.White)){
                    Box(Modifier.fillMaxSize(), contentAlignment=Alignment.Center){ Text(if(fotoOk) "✅ Foto 80px OK\nTap upload galeri" else "Foto profil 80px circle wajib\nTap", fontSize=8.sp, modifier=Modifier.padding(4.dp)) }
                }
                Card(Modifier.width(120.dp).height(80.dp).clip(RoundedCornerShape(8.dp)).clickable{ktpOk=!ktpOk}.border(2.dp, if(ktpOk) Orange else Color.Gray, RoundedCornerShape(8.dp)), colors=CardDefaults.cardColors(if(ktpOk) Color(0xFFFFF3E0) else Color.White)){
                    Box(Modifier.fillMaxSize(), contentAlignment=Alignment.Center){ Text(if(ktpOk) "✅ KTP 200px OK" else "KTP 200px wajib\nTap", fontSize=10.sp) }
                }
            }
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(pass,{pass=it}, label={Text("Password")}, visualTransformation=if(show1) VisualTransformation.None else PasswordVisualTransformation(), trailingIcon={ Text(if(show1) "🙈" else "👁️", modifier=Modifier.clickable{show1=!show1}) }, modifier=Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            // INI YANG KEMARIN HILANG - SEKARANG ADA!
            OutlinedTextField(confirm,{confirm=it}, label={Text("Konfirmasi Password *BARU*")}, visualTransformation=if(show2) VisualTransformation.None else PasswordVisualTransformation(), trailingIcon={ Text(if(show2) "🙈" else "👁️", modifier=Modifier.clickable{show2=!show2}) }, isError=confirm.isNotEmpty() && confirm!=pass, supportingText={ if(confirm.isNotEmpty() && confirm==pass) Text("✅ Password cocok", color=Color(0xFF4CAF50), fontSize=11.sp) else if(confirm.isNotEmpty()) Text("❌ Password tidak sama!", color=Color.Red, fontSize=11.sp) }, modifier=Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            val ok = nama.isNotEmpty() && hp.isNotEmpty() && namaRek.isNotEmpty() && noRek.isNotEmpty() && fotoOk && ktpOk && pass.isNotEmpty() && pass==confirm && mapsOk
            Button(onClick={onNext()}, enabled=ok, modifier=Modifier.fillMaxWidth().height(48.dp), colors=ButtonDefaults.buttonColors(DarkGreen), shape=RoundedCornerShape(12.dp)){ Text("Daftar → Saldo 0") }
            Spacer(Modifier.height(6.dp))
            if(!ok) Text("❌ Lengkapi: Nama, HP, Nama Rekening, No Rekening, Foto 80px, KTP 200px, Maps, Password sama", fontSize=10.sp, color=Color.Red)
            else Text("✅ Semua lengkap! Bisa daftar #59", color=Color(0xFF4CAF50), fontWeight=FontWeight.Bold, fontSize=11.sp)
        }
    }
}
