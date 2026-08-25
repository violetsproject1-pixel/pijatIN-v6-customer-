package com.pijatin.customer
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
val Green = Color(0xFF2D4A3E)
val Orange = Color(0xFFFF7A00)
class MainActivity : ComponentActivity(){
override fun onCreate(b:Bundle?){
super.onCreate(b)
setContent{
val ctx=LocalContext.current
var hp by remember{mutableStateOf("083893330346")}
var email by remember{mutableStateOf("malikysyachmal2018@gmail.com")}
var p1 by remember{mutableStateOf("")}
var p2 by remember{mutableStateOf("")}
var foto by remember{mutableStateOf<Uri?>(null)}
var ktp by remember{mutableStateOf<Uri?>(null)}
val galeriFoto=rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()){if(it!=null)foto=it}
val galeriKtp=rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()){if(it!=null)ktp=it}
val kamera=rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()){if(it!=null)foto=Uri.parse("camera")}
MaterialTheme{
LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(16.dp), contentPadding=PaddingValues(bottom=80.dp)){
item{
Text("Daftar #130 MINIMAL", fontWeight=FontWeight.Bold, fontSize=22.sp, color=Green)
Text("HAPUS NAMA ALAMAT GPS MAPS BANK EWALLET", fontSize=10.sp, color=Orange, fontWeight=FontWeight.Bold)
Spacer(Modifier.height(24.dp))
OutlinedTextField(hp,{hp=it}, label={Text("No Telepon *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp))
Spacer(Modifier.height(12.dp))
OutlinedTextField(email,{email=it}, label={Text("Email *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp))
Spacer(Modifier.height(20.dp))
Text("Foto & KTP *", fontWeight=FontWeight.Bold, color=Green)
Spacer(Modifier.height(12.dp))
Row(horizontalArrangement=Arrangement.spacedBy(12.dp)){
Column(horizontalAlignment=Alignment.CenterHorizontally){
Box(Modifier.size(90.dp).clip(CircleShape).background(if(foto!=null)Color(0xFF4CAF50)else Color.LightGray), contentAlignment=Alignment.Center){Text(if(foto!=null)"OK"else"FOTO", color=Color.White, fontWeight=FontWeight.Bold)}
Spacer(Modifier.height(8.dp))
Button(onClick={galeriFoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))}, modifier=Modifier.width(100.dp).height(36.dp)){Text("GALERI", fontSize=10.sp)}
Spacer(Modifier.height(6.dp))
Button(onClick={kamera.launch(null)}, modifier=Modifier.width(100.dp).height(36.dp), colors=ButtonDefaults.buttonColors(Orange)){Text("KAMERA", fontSize=10.sp)}
}
Column(Modifier.weight(1f)){
Box(Modifier.fillMaxWidth().height(110.dp).clip(RoundedCornerShape(12.dp)).background(if(ktp!=null)Color(0xFF4CAF50)else Color.Gray), contentAlignment=Alignment.Center){Text("KTP 200px", color=Color.White, fontWeight=FontWeight.Bold)}
Spacer(Modifier.height(8.dp))
Button(onClick={galeriKtp.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))}, modifier=Modifier.fillMaxWidth().height(40.dp)){Text("KTP GALERI", fontSize=11.sp)}
}
}
Spacer(Modifier.height(20.dp))
OutlinedTextField(p1,{p1=it}, label={Text("Password *")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp))
Spacer(Modifier.height(10.dp))
OutlinedTextField(p2,{p2=it}, label={Text("Konfirmasi Password *")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp))
if(p2.isNotEmpty()){Spacer(Modifier.height(6.dp)); Text(if(p1==p2)"✅ Cocok"else"❌ Beda", color=if(p1==p2)Color(0xFF4CAF50)else Color.Red, fontSize=12.sp, fontWeight=FontWeight.Bold)}
Spacer(Modifier.height(24.dp))
Button(onClick={
if(hp.isEmpty()||email.isEmpty()){Toast.makeText(ctx,"Isi HP & Email",Toast.LENGTH_SHORT).show();return@Button}
if(p1!=p2||p1.length<6){Toast.makeText(ctx,"Password min 6 & sama",Toast.LENGTH_SHORT).show();return@Button}
Toast.makeText(ctx,"DAFTAR OK: "+hp,Toast.LENGTH_LONG).show()
}, modifier=Modifier.fillMaxWidth().height(56.dp), colors=ButtonDefaults.buttonColors(Green), shape=RoundedCornerShape(12.dp)){Text("DAFTAR SEKARANG ✅", color=Color.White, fontWeight=FontWeight.Bold, fontSize=15.sp)}
Spacer(Modifier.height(12.dp))
Text("✅ DIHAPUS: Nama Lengkap maliki, Alamat GG cemara2 RT 01/01 kunciran jaya pinang Tangerang 15144, Pilih Kota Tangerang Jakarta Bekasi Depok Bogor Tangsel, GPS Akurat +-3m -6,234248 106,659647, Buka Maps, Bank BCA MANDIRI BRI SEABANK, E-Wallet DANA OVO GOPAY 083893330346", fontSize=8.sp, color=Color(0xFF4CAF50), fontWeight=FontWeight.Bold)
Spacer(Modifier.height(6.dp))
Text("📱 SISA: No Telepon + Email + Foto + KTP + Password = Daftar 30 detik SUPER CEPAT!", fontSize=10.sp, color=Green, fontWeight=FontWeight.Bold)
}
}
}
}
}
}
