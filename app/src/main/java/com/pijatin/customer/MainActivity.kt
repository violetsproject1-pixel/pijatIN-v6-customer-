package com.pijatin.customer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
val Green = Color(0xFF2D4A3E)
val Orange = Color(0xFFFF7A00)
val LightGreen = Color(0xFFE8F5E9)
class MainActivity : ComponentActivity(){
override fun onCreate(b:Bundle?){
super.onCreate(b)
setContent{
val ctx=LocalContext.current
val prefs=ctx.getSharedPreferences("PijatIN_Login",0)
var isLoggedIn by remember{mutableStateOf(prefs.getBoolean("isLoggedIn",false))}
var savedPhone by remember{mutableStateOf(prefs.getString("phone","")?:"")}
var savedEmail by remember{mutableStateOf(prefs.getString("email","")?:"")}
var selectedMenu by remember{mutableStateOf(0)}
if(isLoggedIn){
Scaffold(bottomBar={
NavigationBar(containerColor=Color.White){
NavigationBarItem(selected=selectedMenu==0, onClick={selectedMenu=0}, icon={Text("🏠")}, label={Text("Beranda", fontSize=10.sp)})
NavigationBarItem(selected=selectedMenu==1, onClick={selectedMenu=1}, icon={Text("📋")}, label={Text("Pesanan", fontSize=10.sp)})
NavigationBarItem(selected=selectedMenu==2, onClick={selectedMenu=2}, icon={Text("👤")}, label={Text("Profil", fontSize=10.sp)})
}
}){ pad ->
Box(Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(pad)){
when(selectedMenu){
0 -> {
LazyColumn(Modifier.fillMaxSize().padding(16.dp)){
item{
Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically){
Column{
Text("Halo! 👋", fontSize=14.sp, color=Color.Gray)
Text(savedPhone, fontSize=18.sp, fontWeight=FontWeight.Bold, color=Green)
Text(savedEmail, fontSize=11.sp, color=Color.Gray)
}
Box(Modifier.size(44.dp).clip(CircleShape).background(Green), contentAlignment=Alignment.Center){Text("M", color=Color.White, fontWeight=FontWeight.Bold)}
}
Spacer(Modifier.height(16.dp))
Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Green).padding(20.dp)){
Column{
Text("🏠 Beranda PijatIN", color=Color.White, fontSize=20.sp, fontWeight=FontWeight.Bold)
Spacer(Modifier.height(4.dp))
Text("Login sebagai: "+savedPhone, color=Color(0xFFB2DFDB), fontSize=12.sp)
Spacer(Modifier.height(8.dp))
Text("✅ Data tersimpan & login otomatis!", color=Color(0xFF81C784), fontSize=12.sp, fontWeight=FontWeight.Bold)
}
}
Spacer(Modifier.height(20.dp))
Text("💆 Layanan Pijat", fontWeight=FontWeight.Bold, fontSize=16.sp, color=Green)
Spacer(Modifier.height(12.dp))
LazyRow(horizontalArrangement=Arrangement.spacedBy(12.dp)){
item{
Box(Modifier.width(160.dp).height(120.dp).clip(RoundedCornerShape(14.dp)).background(Color.White).clickable{Toast.makeText(ctx,"Pijat Tradisional - 90 menit Rp 120k",Toast.LENGTH_SHORT).show()}, contentAlignment=Alignment.Center){Column(horizontalAlignment=Alignment.CenterHorizontally){Text("💆‍♀️", fontSize=30.sp); Text("Tradisional", fontWeight=FontWeight.Bold, fontSize=12.sp); Text("Rp 120k / 90m", fontSize=10.sp, color=Orange)}}
}
item{
Box(Modifier.width(160.dp).height(120.dp).clip(RoundedCornerShape(14.dp)).background(Color.White).clickable{Toast.makeText(ctx,"Pijat Refleksi",Toast.LENGTH_SHORT).show()}, contentAlignment=Alignment.Center){Column(horizontalAlignment=Alignment.CenterHorizontally){Text("🦶", fontSize=30.sp); Text("Refleksi", fontWeight=FontWeight.Bold, fontSize=12.sp); Text("Rp 100k / 60m", fontSize=10.sp, color=Orange)}}
}
item{
Box(Modifier.width(160.dp).height(120.dp).clip(RoundedCornerShape(14.dp)).background(Color.White).clickable{Toast.makeText(ctx,"Pijat Aromatherapy",Toast.LENGTH_SHORT).show()}, contentAlignment=Alignment.Center){Column(horizontalAlignment=Alignment.CenterHorizontally){Text("🌸", fontSize=30.sp); Text("Aromatherapy", fontWeight=FontWeight.Bold, fontSize=12.sp); Text("Rp 150k / 90m", fontSize=10.sp, color=Orange)}}
}
}
Spacer(Modifier.height(20.dp))
Button(onClick={Toast.makeText(ctx,"Cari terapis terdekat...",Toast.LENGTH_SHORT).show()}, modifier=Modifier.fillMaxWidth().height(54.dp), colors=ButtonDefaults.buttonColors(Orange), shape=RoundedCornerShape(14.dp)){Text("🔍 PESAN PIJAT SEKARANG", color=Color.White, fontWeight=FontWeight.Bold)}
Spacer(Modifier.height(12.dp))
Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(LightGreen).padding(12.dp)){Text("📍 Mitra terdekat 0.8km - 3 terapis ready", fontSize=11.sp, color=Green, fontWeight=FontWeight.Bold)}
}
}
}
1 -> {
Box(Modifier.fillMaxSize(), contentAlignment=Alignment.Center){Column(horizontalAlignment=Alignment.CenterHorizontally){Text("📋", fontSize=48.sp); Text("Belum ada pesanan", fontWeight=FontWeight.Bold); Text("Pesan pijat pertama kamu!", fontSize=12.sp, color=Color.Gray)}}
}
2 -> {
Column(Modifier.fillMaxSize().background(Color.White).padding(20.dp)){
Text("👤 Profil", fontSize=22.sp, fontWeight=FontWeight.Bold, color=Green)
Spacer(Modifier.height(20.dp))
Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color(0xFFF5F5F5)).padding(16.dp)){
Column{
Text("No Telepon", fontSize=11.sp, color=Color.Gray); Text(savedPhone, fontWeight=FontWeight.Bold, fontSize=16.sp)
Spacer(Modifier.height(8.dp))
Text("Email", fontSize=11.sp, color=Color.Gray); Text(savedEmail, fontSize=14.sp)
}
}
Spacer(Modifier.height(24.dp))
Button(onClick={prefs.edit().putBoolean("isLoggedIn",false).apply(); isLoggedIn=false; Toast.makeText(ctx,"Logout berhasil",Toast.LENGTH_SHORT).show()}, modifier=Modifier.fillMaxWidth().height(50.dp), colors=ButtonDefaults.buttonColors(Color.Red), shape=RoundedCornerShape(12.dp)){Text("LOGOUT", color=Color.White, fontWeight=FontWeight.Bold)}
}
}
}
}
}
}else{
var hp by remember{mutableStateOf("083893330346")}
var email by remember{mutableStateOf("malikysyachmal2018@gmail.com")}
var p1 by remember{mutableStateOf("")}
var p2 by remember{mutableStateOf("")}
val focusManager=LocalFocusManager.current
val keyboardController=LocalSoftwareKeyboardController.current
val isFormValid = hp.length>=10 && email.contains("@") && p1.length>=6 && p1==p2
Scaffold(bottomBar={
Column(Modifier.fillMaxWidth().background(Color.White).padding(16.dp).imePadding()){
Button(onClick={
if(!isFormValid){Toast.makeText(ctx,"Lengkapi data!",Toast.LENGTH_SHORT).show();return@Button}
prefs.edit().apply{putBoolean("isLoggedIn",true); putString("phone",hp); putString("email",email); putString("password",p1); apply()}
Toast.makeText(ctx,"✅ DAFTAR BERHASIL! Masuk ke Home...",Toast.LENGTH_LONG).show()
savedPhone=hp; savedEmail=email; isLoggedIn=true; keyboardController?.hide()
}, modifier=Modifier.fillMaxWidth().height(56.dp), enabled=isFormValid, colors=ButtonDefaults.buttonColors(containerColor=Green, disabledContainerColor=Color.Gray), shape=RoundedCornerShape(14.dp)){
Text(if(isFormValid)"DAFTAR SEKARANG ✅"else"ISI DATA DULU", color=Color.White, fontWeight=FontWeight.Bold, fontSize=16.sp)
}
}
}){ padding ->
LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(padding).padding(16.dp).imePadding()){
item{
Text("Daftar #133 HOME LENGKAP", fontWeight=FontWeight.Bold, fontSize=22.sp, color=Green)
Text("FIX SCROLL + AUTO LOGIN + HOME LENGKAP", fontSize=10.sp, color=Orange, fontWeight=FontWeight.Bold)
Spacer(Modifier.height(24.dp))
OutlinedTextField(value=hp, onValueChange={hp=it}, label={Text("No Telepon *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp), keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Phone, imeAction=ImeAction.Next), keyboardActions=KeyboardActions(onNext={focusManager.moveFocus(FocusDirection.Down)}), singleLine=true)
Spacer(Modifier.height(12.dp))
OutlinedTextField(value=email, onValueChange={email=it}, label={Text("Email *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp), keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Email, imeAction=ImeAction.Next), keyboardActions=KeyboardActions(onNext={focusManager.moveFocus(FocusDirection.Down)}), singleLine=true)
Spacer(Modifier.height(12.dp))
OutlinedTextField(value=p1, onValueChange={p1=it}, label={Text("Password *")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp), keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Password, imeAction=ImeAction.Next), keyboardActions=KeyboardActions(onNext={focusManager.moveFocus(FocusDirection.Down)}), singleLine=true)
Spacer(Modifier.height(12.dp))
OutlinedTextField(value=p2, onValueChange={p2=it}, label={Text("Konfirmasi Password *")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp), keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Password, imeAction=ImeAction.Done), keyboardActions=KeyboardActions(onDone={focusManager.clearFocus(); keyboardController?.hide()}), singleLine=true)
if(p2.isNotEmpty()){Spacer(Modifier.height(8.dp)); Text(if(p1==p2)"✅ Password cocok"else"❌ Tidak sama", color=if(p1==p2)Color(0xFF4CAF50)else Color.Red, fontSize=12.sp, fontWeight=FontWeight.Bold)}
Spacer(Modifier.height(100.dp))
}
}
}
}
}
}
}
