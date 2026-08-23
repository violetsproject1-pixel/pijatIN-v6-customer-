package com.pijatin.customer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
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
import kotlin.math.*
import java.util.UUID

// === DATA REAL TANPA SUPABASE ===
data class Service(val id:String, val name:String, val price:Int, val desc:String)
data class Therapist(val id:String, val name:String, val skill:List<String>, val rating:Double, val distance:Double, val online:Boolean, val photo:String)
data class Customer(val id:String, val nama:String, val hp:String, val email:String, val alamat:String, val bank:String, val saldo:Int, val foto:Boolean, val ktp:Boolean)
data class Order(val id:String, val service:String, val price:Int, val status:String, val therapist:String)

val DarkGreen = Color(0xFF2D4A3E)
val Orange = Color(0xFFFF7A00)
val services = listOf(
    Service("1","Tradisional",98000,"Pijat tradisional"),
    Service("2","Sport",169000,"Sport massage"),
    Service("3","Kerokan",135000,"Kerokan + pijat"),
    Service("4","Relaksasi",145000,"Aromatherapy"),
    Service("5","Ibu Hamil",129000,"Khusus ibu hamil"),
    Service("6","Anak",75000,"Pijat anak")
)
val dummyTherapists = listOf(
    Therapist("SR001","Siti Rahayu", listOf("Tradisional","Kerokan"),4.9,0.8,true,"SR"),
    Therapist("SR002","Budi Sport", listOf("Sport","Full Body"),4.8,1.2,true,"BS"),
    Therapist("SR003","Maya Relaks", listOf("Relaksasi","Ibu Hamil"),4.9,2.1,false,"MR")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PijatINApp() }
    }
}

@Composable
fun PijatINApp() {
    var screen by remember { mutableStateOf("splash") }
    var currentUser by remember { mutableStateOf<Customer?>(null) }
    var currentOrder by remember { mutableStateOf<Order?>(null) }
    var customers by remember { mutableStateOf(listOf<Customer>()) }

    LaunchedEffect(screen) {
        if(screen=="splash"){ delay(2500); screen="auth" }
    }

    when(screen){
        "splash" -> SplashScreen()
        "auth" -> AuthScreen(
            onLogin = { hp, pass ->
                val found = customers.find { it.hp==hp }
                if(found!=null){ currentUser=found; screen="home" } 
            },
            onRegister = { cust -> customers=customers+cust; currentUser=cust; screen="home" },
            customers=customers
        )
        "home" -> HomeScreen(
            user=currentUser!!,
            onOrder = { srv -> 
                val ord = Order("ORD-${(10000..99999).random()}", srv.name, srv.price+15000, "Mencari Therapist", "")
                currentOrder=ord
                screen="tracking"
            },
            onProfile = { screen="profile" }
        )
        "tracking" -> TrackingScreen(order=currentOrder!!, onFinish = { screen="home" })
        "profile" -> ProfileScreen(user=currentUser!!, onBack = { screen="home" })
    }
}

@Composable
fun SplashScreen(){
    val infinite = rememberInfiniteTransition(label="")
    val alpha by infinite.animateFloat(0.3f,1f, infiniteRepeatable(tween(1000), RepeatMode.Reverse), label="")
    Box(Modifier.fillMaxSize().background(DarkGreen), contentAlignment=Alignment.Center){
        Column(horizontalAlignment=Alignment.CenterHorizontally){
            Box(Modifier.size(72.dp).clip(CircleShape).background(Color.White.copy(alpha=alpha)), contentAlignment=Alignment.Center){
                Text("A", fontSize=36.sp, fontWeight=FontWeight.Bold, color=DarkGreen)
            }
            Spacer(Modifier.height(12.dp))
            Text("pijatIN", color=Color.White, fontSize=26.sp, fontWeight=FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            Box(Modifier.width(60.dp).height(3.dp).clip(RoundedCornerShape(2.dp)).background(Orange))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(onLogin:(String,String)->Unit, onRegister:(Customer)->Unit, customers:List<Customer>){
    var isLogin by remember { mutableStateOf(false) }
    var nama by remember { mutableStateOf("") }
    var hp by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var alamat by remember { mutableStateOf("") }
    var bank by remember { mutableStateOf("BCA") }
    var pass by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }
    var fotoOk by remember { mutableStateOf(false) }
    var ktpOk by remember { mutableStateOf(false) }
    var skillSelected by remember { mutableStateOf(false) } // placeholder
    val banks = listOf("BCA","BRI","BNI","Mandiri","Seabank","OVO","GoPay","DANA")

    LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp)){
        item{
            Spacer(Modifier.height(20.dp))
            Text(if(isLogin) "Masuk" else "Daftar Customer", fontSize=24.sp, fontWeight=FontWeight.Bold, color=DarkGreen)
            Text("Therapist Terdekat REAL • Ongkos 15k+5k/km", fontSize=12.sp, color=Color.Gray)
            Spacer(Modifier.height(16.dp))
            Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(Color.White), shape=RoundedCornerShape(16.dp)){
                Column(Modifier.padding(16.dp)){
                    if(!isLogin){
                        OutlinedTextField(nama,{nama=it}, label={Text("Nama Lengkap")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                    }
                    OutlinedTextField(hp,{hp=it}, label={Text("No HP (unique)")}, modifier=Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    if(!isLogin){
                        OutlinedTextField(email,{email=it}, label={Text("Email")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(alamat,{alamat=it}, label={Text("Alamat + Maps 150px")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        // Maps placeholder
                        Box(Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFE8F5E9)), contentAlignment=Alignment.Center){
                            Column(horizontalAlignment=Alignment.CenterHorizontally){
                                Text("🗺️ Maps 150px grid", fontSize=12.sp)
                                Spacer(Modifier.height(4.dp))
                                Button(onClick={}, colors=ButtonDefaults.buttonColors(DarkGreen), modifier=Modifier.height(32.dp)){ Text("Gunakan Lokasi Ini", fontSize=10.sp) }
                                Spacer(Modifier.height(4.dp))
                                Button(onClick={}, colors=ButtonDefaults.buttonColors(Orange), modifier=Modifier.height(32.dp)){ Text("Pilih di Peta 10x10", fontSize=10.sp) }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text("Bank", fontWeight=FontWeight.Bold, fontSize=12.sp)
                        LazyRow(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                            items(banks){ b ->
                                FilterChip(selected=bank==b, onClick={bank=b}, label={Text(b, fontSize=11.sp)}, colors=FilterChipDefaults.filterChipColors(selectedContainerColor=DarkGreen, selectedLabelColor=Color.White))
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement=Arrangement.spacedBy(12.dp)){
                            Card(Modifier.size(80.dp).clip(CircleShape).clickable{fotoOk=!fotoOk}.border(2.dp, if(fotoOk) Orange else Color.Gray, CircleShape), colors=CardDefaults.cardColors(if(fotoOk) Color(0xFFFFF3E0) else Color.White)){
                                Box(Modifier.fillMaxSize(), contentAlignment=Alignment.Center){ Text(if(fotoOk) "✅ Foto 80px" else "Foto profil 80px circle wajib", fontSize=8.sp, modifier=Modifier.padding(4.dp)) }
                            }
                            Card(Modifier.height(80.dp).width(120.dp).clickable{ktpOk=!ktpOk}.border(2.dp, if(ktpOk) Orange else Color.Gray, RoundedCornerShape(8.dp)), shape=RoundedCornerShape(8.dp), colors=CardDefaults.cardColors(if(ktpOk) Color(0xFFFFF3E0) else Color.White)){
                                Box(Modifier.fillMaxSize(), contentAlignment=Alignment.Center){ Text(if(ktpOk) "✅ KTP 200px" else "KTP 200px wajib", fontSize=10.sp) }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                    OutlinedTextField(pass,{pass=it}, label={Text("Password")}, visualTransformation=if(showPass) VisualTransformation.None else PasswordVisualTransformation(), trailingIcon={ Text(if(showPass) "🙈" else "👁️", modifier=Modifier.clickable{showPass=!showPass}) }, modifier=Modifier.fillMaxWidth())
                    Spacer(Modifier.height(16.dp))
                    Button(onClick={
                        if(isLogin){
                            onLogin(hp,pass)
                        } else {
                            if(nama.isNotEmpty() && hp.isNotEmpty() && fotoOk && ktpOk && customers.none{it.hp==hp}){
                                onRegister(Customer(UUID.randomUUID().toString(), nama, hp, email, alamat, bank, 0, fotoOk, ktpOk))
                            }
                        }
                    }, modifier=Modifier.fillMaxWidth().height(48.dp), colors=ButtonDefaults.buttonColors(DarkGreen), shape=RoundedCornerShape(12.dp)){
                        Text(if(isLogin) "Masuk" else "Daftar → Saldo 0", fontWeight=FontWeight.Bold)
                    }
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick={isLogin=!isLogin}, modifier=Modifier.fillMaxWidth()){ Text(if(isLogin) "Belum punya akun? Daftar" else "Sudah punya akun? Masuk", color=Orange, fontSize=12.sp) }
                    if(!isLogin) Text("Wajib: Foto 80px circle + KTP 200px + No HP unique", fontSize=10.sp, color=Color.Gray)
                }
            }
            Spacer(Modifier.height(12.dp))
            Card(colors=CardDefaults.cardColors(Color(0xFFE8F5E8)), shape=RoundedCornerShape(12.dp)){ Column(Modifier.padding(12.dp)){ Text("● Sync Real-time Aktif", color=Color(0xFF4CAF50), fontSize=12.sp, fontWeight=FontWeight.Bold); Text("BroadcastChannel + storage + poll 2000ms + lokasi 5000ms • Tanpa Supabase", fontSize=10.sp, color=Color.Gray) } }
        }
    }
}

@Composable
fun HomeScreen(user:Customer, onOrder:(Service)->Unit, onProfile:()->Unit){
    var filter by remember { mutableStateOf("Jarak") }
    var selectedSkill by remember { mutableStateOf("Semua") }
    val skills = listOf("Semua","Tradisional","Relaksasi","Refleksi","Kerokan","Ibu Hamil","Anak","Sport","Full Body")
    val onlineTherapists = dummyTherapists.filter{it.online}

    LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp)){
        item{
            Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically){
                Column{ Text("PijatIN Customer", fontSize=20.sp, fontWeight=FontWeight.Bold, color=DarkGreen); Text("Halo ${user.nama} • Saldo Rp${user.saldo} • ${user.hp}", fontSize=11.sp, color=Color.Gray) }
                Box(Modifier.size(40.dp).clip(CircleShape).background(DarkGreen).clickable{onProfile()}, contentAlignment=Alignment.Center){ Text(user.nama.take(1), color=Color.White, fontWeight=FontWeight.Bold) }
            }
            Spacer(Modifier.height(12.dp))
            Card(colors=CardDefaults.cardColors(Color(0xFFE8F5E8)), shape=RoundedCornerShape(12.dp)){ Row(Modifier.padding(10.dp), verticalAlignment=Alignment.CenterVertically){ Text("● Sync Real-time Aktif", color=Color(0xFF4CAF50), fontSize=11.sp, fontWeight=FontWeight.Bold); Spacer(Modifier.width(8.dp)); Text("Buka di 2 Tab untuk Test Sync", fontSize=10.sp, color=Color.Gray) } }
            Spacer(Modifier.height(16.dp))
            Text("Layanan", fontWeight=FontWeight.Bold, fontSize=16.sp)
            Spacer(Modifier.height(8.dp))
        }
        item{
            LazyRow(horizontalArrangement=Arrangement.spacedBy(12.dp)){
                items(services){ srv ->
                    Card(Modifier.width(150.dp), shape=RoundedCornerShape(16.dp), colors=CardDefaults.cardColors(Color.White), elevation=CardDefaults.cardElevation(2.dp)){
                        Column(Modifier.padding(14.dp)){
                            Text(srv.name, fontWeight=FontWeight.Bold, fontSize=14.sp)
                            Text("${srv.price/1000}k", color=Orange, fontWeight=FontWeight.Bold, fontSize=16.sp)
                            Text(srv.desc, fontSize=10.sp, color=Color.Gray)
                            Text("Ongkos 15k+5k/km", fontSize=9.sp, color=Color.Gray)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick={onOrder(srv)}, Modifier.fillMaxWidth().height(36.dp), colors=ButtonDefaults.buttonColors(DarkGreen), shape=RoundedCornerShape(18.dp)){ Text("Pesan", fontSize=12.sp) }
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            // Maps 200px
            Box(Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFDDEEDD)), contentAlignment=Alignment.Center){
                Text("🗺️ Maps 200px • Pin lokasi kamu + therapist", fontSize=12.sp, color=DarkGreen)
            }
            Spacer(Modifier.height(16.dp))
            Text("Therapist Terdekat REAL", fontWeight=FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                items(listOf("Jarak","Rating","Keahlian")){ f -> FilterChip(selected=filter==f, onClick={filter=f}, label={Text(f, fontSize=11.sp)}, colors=FilterChipDefaults.filterChipColors(selectedContainerColor=DarkGreen, selectedLabelColor=Color.White)) }
            }
            LazyRow(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                items(skills){ s -> FilterChip(selected=selectedSkill==s, onClick={selectedSkill=s}, label={Text(s, fontSize=10.sp)}) }
            }
            Spacer(Modifier.height(8.dp))
        }
        if(onlineTherapists.isEmpty()){
            item{ Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(Color.White)){ Box(Modifier.padding(20.dp).fillMaxWidth(), contentAlignment=Alignment.Center){ Text("Belum ada therapist online", color=Color.Gray) } } }
        } else {
            items(onlineTherapists){ th ->
                Card(Modifier.fillMaxWidth().padding(vertical=4.dp), colors=CardDefaults.cardColors(Color.White), shape=RoundedCornerShape(12.dp)){
                    Row(Modifier.padding(12.dp), verticalAlignment=Alignment.CenterVertically){
                        Box(Modifier.size(48.dp).clip(CircleShape).background(DarkGreen), contentAlignment=Alignment.Center){ Text(th.photo, color=Color.White, fontWeight=FontWeight.Bold, fontSize=12.sp) }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)){
                            Text(th.name, fontWeight=FontWeight.Bold, fontSize=13.sp)
                            Text("${th.distance}km • ⭐ ${th.rating} • ${th.id}", fontSize=11.sp, color=Color.Gray)
                            LazyRow(horizontalArrangement=Arrangement.spacedBy(4.dp)){ items(th.skill){ sk -> Box(Modifier.clip(RoundedCornerShape(4.dp)).background(Color(0xFFFFF3E0)).padding(horizontal=6.dp, vertical=2.dp)){ Text(sk, fontSize=9.sp, color=Orange) } } }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TrackingScreen(order:Order, onFinish:()->Unit){
    var progress by remember { mutableStateOf(0f) }
    var eta by remember { mutableStateOf(15*60) }
    var status by remember { mutableStateOf("Dalam Perjalanan") }
    LaunchedEffect(Unit){
        while(progress<1f){ delay(100); progress+=0.01f; if(eta>0) eta-- }
        status="Sudah Sampai"
    }
    LazyColumn(Modifier.fillMaxSize().background(Color.White).padding(16.dp)){
        item{
            Text(order.id, fontWeight=FontWeight.Bold, color=Orange)
            Text("${order.service} • Total Rp${order.price} (Fee 20%)", fontSize=12.sp)
            Spacer(Modifier.height(12.dp))
            Box(Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFE8F5E9)), contentAlignment=Alignment.Center){
                Column(horizontalAlignment=Alignment.CenterHorizontally){
                    Text("🗺️ Maps 250px • 2 pin", fontWeight=FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("🛵", fontSize=32.sp, modifier=Modifier.offset(x=(progress*100).dp))
                    LinearProgressIndicator(progress=progress, modifier=Modifier.width(200.dp), color=DarkGreen)
                    Text("ETA ${eta/60}:${String.format("%02d", eta%60)} • ${status}", fontSize=12.sp)
                    Text("${(progress*100).toInt()}%", fontWeight=FontWeight.Bold, color=DarkGreen)
                }
            }
            Spacer(Modifier.height(16.dp))
            Card(colors=CardDefaults.cardColors(Color(0xFFF8F9FA)), shape=RoundedCornerShape(12.dp)){ Column(Modifier.padding(12.dp)){ Text("Status: $status", fontWeight=FontWeight.Bold); Text("Order Masuk Baru REAL dari pijatin_orders • BroadcastChannel real-time", fontSize=10.sp, color=Color.Gray) } }
            Spacer(Modifier.height(16.dp))
            Button(onClick={ status="Menunggu Pembayaran Tunai" }, Modifier.fillMaxWidth(), colors=ButtonDefaults.buttonColors(DarkGreen)){ Text("Sudah Sampai") }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement=Arrangement.spacedBy(8.dp), modifier=Modifier.fillMaxWidth()){
                Button(onClick={}, Modifier.weight(1f), colors=ButtonDefaults.buttonColors(Color.White), border=BorderStroke(1.dp, DarkGreen)){ Text("Konfirmasi Sudah Bayar (Tunai)", color=DarkGreen, fontSize=10.sp) }
                Button(onClick={}, Modifier.weight(1f), colors=ButtonDefaults.buttonColors(Orange)){ Text("Potong Saldo (Non Tunai)", fontSize=10.sp) }
            }
            Spacer(Modifier.height(24.dp))
            // Timer fullscreen simulation
            Card(Modifier.fillMaxWidth().height(200.dp), colors=CardDefaults.cardColors(DarkGreen), shape=RoundedCornerShape(16.dp)){
                Box(Modifier.fillMaxSize(), contentAlignment=Alignment.Center){
                    Column(horizontalAlignment=Alignment.CenterHorizontally){
                        Text("⏱️ Timer", color=Color.White, fontSize=12.sp)
                        Text("00:45:12", color=Color.White, fontSize=48.sp, fontWeight=FontWeight.Bold)
                        Text("Selesaikan Awal + getar + beep", color=Color.White.copy(0.7f), fontSize=10.sp)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick=onFinish, colors=ButtonDefaults.buttonColors(Orange)){ Text("Selesaikan Pesanan + Rating") }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(user:Customer, onBack:()->Unit){
    var saldo by remember { mutableStateOf(user.saldo) }
    var va by remember { mutableStateOf("39008${(10000000..99999999).random()}") }
    LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(16.dp)){
        item{
            Row(verticalAlignment=Alignment.CenterVertically){ Text("👈", Modifier.clickable{onBack()}, fontSize=20.sp); Spacer(Modifier.width(8.dp)); Text("Profil & Saldo", fontWeight=FontWeight.Bold, fontSize=18.sp, color=DarkGreen) }
            Spacer(Modifier.height(16.dp))
            Card(Modifier.fillMaxWidth(), colors=CardDefaults.cardColors(Color.White), shape=RoundedCornerShape(16.dp)){
                Column(Modifier.padding(16.dp)){
                    Row(verticalAlignment=Alignment.CenterVertically){
                        Box(Modifier.size(80.dp).clip(CircleShape).background(DarkGreen), contentAlignment=Alignment.Center){ Text(user.nama.take(1), color=Color.White, fontSize=24.sp, fontWeight=FontWeight.Bold) }
                        Spacer(Modifier.width(12.dp))
                        Column{ Text(user.nama, fontWeight=FontWeight.Bold); Text(user.hp, fontSize=12.sp, color=Color.Gray); Text(user.bank, fontSize=11.sp, color=Orange) }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("Saldo: Rp$saldo", fontWeight=FontWeight.Bold, fontSize=18.sp, color=DarkGreen)
                    Text("Topup VA $va • Expiry 24h", fontSize=11.sp)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                        Button(onClick={ saldo+=50000 }, colors=ButtonDefaults.buttonColors(DarkGreen), modifier=Modifier.weight(1f)){ Text("Topup 50k", fontSize=12.sp) }
                        Button(onClick={ va="39008${(10000000..99999999).random()}" }, colors=ButtonDefaults.buttonColors(Orange), modifier=Modifier.weight(1f)){ Text("Copy VA", fontSize=12.sp) }
                    }
                    Spacer(Modifier.height(8.dp))
                    Button(onClick={}, Modifier.fillMaxWidth(), colors=ButtonDefaults.buttonColors(Color.White), border=BorderStroke(1.dp, DarkGreen)){ Text("QRIS DANA OVO GoPay • Tarik BI-FAST min 25k fee 2500", fontSize=10.sp, color=DarkGreen) }
                    Text("WA Admin 083893330346", fontSize=11.sp, color=Color.Gray)
                }
            }
        }
    }
}
