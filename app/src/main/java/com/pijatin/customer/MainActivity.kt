package com.pijatin.customer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
class MainActivity : ComponentActivity(){
override fun onCreate(b:Bundle?){
super.onCreate(b)
setContent{
val ctx=LocalContext.current
val prefs=ctx.getSharedPreferences("PijatIN_Login",0)
var isLoggedIn by remember{mutableStateOf(prefs.getBoolean("isLoggedIn",false))}
var savedPhone by remember{mutableStateOf(prefs.getString("phone","")?:"")}
var savedEmail by remember{mutableStateOf(prefs.getString("email","")?:"")}
var selectedFilter by remember{mutableStateOf(0)}
var selectedBottom by remember{mutableStateOf(0)}
if(isLoggedIn){
Scaffold(bottomBar={
Row(Modifier.fillMaxWidth().background(Color.White).padding(vertical=8.dp, horizontal=8.dp), horizontalArrangement=Arrangement.SpaceAround){
Column(Modifier.clip(RoundedCornerShape(12.dp)).background(if(selectedBottom==0)Green else Color.Transparent).padding(horizontal=14.dp, vertical=6.dp), horizontalAlignment=Alignment.CenterHorizontally){Text("Beranda", fontSize=10.sp, color=if(selectedBottom==0)Color.White else Color.Gray, fontWeight=FontWeight.Bold)}
Column(Modifier.padding(horizontal=14.dp, vertical=6.dp), horizontalAlignment=Alignment.CenterHorizontally){Text("Pesanan", fontSize=10.sp, color=Color.Gray)}
Column(Modifier.padding(horizontal=14.dp, vertical=6.dp), horizontalAlignment=Alignment.CenterHorizontally){Text("Riwayat", fontSize=10.sp, color=Color.Gray)}
Column(Modifier.padding(horizontal=14.dp, vertical=6.dp), horizontalAlignment=Alignment.CenterHorizontally){Text("Saldo", fontSize=10.sp, color=Color.Gray)}
Column(Modifier.padding(horizontal=14.dp, vertical=6.dp), horizontalAlignment=Alignment.CenterHorizontally){Text("Profil", fontSize=10.sp, color=Color.Gray)}
}
}){ pad ->
LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF6F3EE)).padding(pad)){
item{
// HEADER - maliki GG becek Saldo Rp50.000 KAYAK KONSEP GAMBAR 2
Row(Modifier.fillMaxWidth().background(Color.White).padding(16.dp), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically){
Row(verticalAlignment=Alignment.CenterVertically){
Box(Modifier.size(44.dp).clip(CircleShape).background(Color.Black), contentAlignment=Alignment.Center){Text("A", color=Color.White, fontWeight=FontWeight.Bold)}
Spacer(Modifier.width(10.dp))
Column{
Text("maliki", fontWeight=FontWeight.Bold, fontSize=16.sp)
Text("GG becek • Online", fontSize=11.sp, color=Color.Gray)
}
}
Column(horizontalAlignment=Alignment.End){
Text("Saldo", fontSize=11.sp, color=Color.Gray)
Text("Rp50.000", fontWeight=FontWeight.Bold, fontSize=16.sp)
}
}
}
item{
// THERAPIST TERDEKAT
Column(Modifier.fillMaxWidth().background(Color.White)){
Column(Modifier.fillMaxWidth().background(Color(0xFFFFF3E0)).padding(16.dp)){
Row(verticalAlignment=Alignment.CenterVertically){
Text("Therapist Terdekat", fontWeight=FontWeight.Bold, color=Orange, fontSize=15.sp)
Spacer(Modifier.width(8.dp))
Box(Modifier.clip(RoundedCornerShape(20.dp)).background(Color.White).padding(horizontal=10.dp, vertical=4.dp)){Text("Lihat Semua", fontSize=11.sp, color=Orange, fontWeight=FontWeight.Bold)}
}
Spacer(Modifier.height(4.dp))
Text("Online & siap ke lokasi kamu", fontSize=12.sp, color=Color(0xFF2E7D32))
}
Spacer(Modifier.height(12.dp))
// FILTER JARAK RATING
Row(Modifier.fillMaxWidth().padding(horizontal=16.dp), horizontalArrangement=Arrangement.spacedBy(8.dp)){
Button(onClick={selectedFilter=0}, modifier=Modifier.height(32.dp), shape=RoundedCornerShape(20.dp), colors=ButtonDefaults.buttonColors(if(selectedFilter==0)Green else Color(0xFFEEEEEE)), contentPadding=PaddingValues(horizontal=12.dp)){Text("Jarak", fontSize=12.sp, color=if(selectedFilter==0)Color.White else Color.Black)}
Button(onClick={selectedFilter=1}, modifier=Modifier.height(32.dp), shape=RoundedCornerShape(20.dp), colors=ButtonDefaults.buttonColors(if(selectedFilter==1)Green else Color(0xFFEEEEEE)), contentPadding=PaddingValues(horizontal=12.dp)){Text("Rating", fontSize=12.sp, color=if(selectedFilter==1)Color.White else Color.Black)}
Button(onClick={selectedFilter=2}, modifier=Modifier.height(32.dp), shape=RoundedCornerShape(20.dp), colors=ButtonDefaults.buttonColors(if(selectedFilter==2)Green else Color(0xFFEEEEEE)), contentPadding=PaddingValues(horizontal=12.dp)){Text("Tradisional", fontSize=12.sp, color=if(selectedFilter==2)Color.White else Color.Black)}
Button(onClick={selectedFilter=3}, modifier=Modifier.height(32.dp), shape=RoundedCornerShape(20.dp), colors=ButtonDefaults.buttonColors(if(selectedFilter==3)Green else Color(0xFFEEEEEE)), contentPadding=PaddingValues(horizontal=12.dp)){Text("Sport", fontSize=12.sp, color=if(selectedFilter==3)Color.White else Color.Black)}
}
Spacer(Modifier.height(12.dp))
Row(Modifier.fillMaxWidth().padding(horizontal=16.dp), horizontalArrangement=Arrangement.spacedBy(12.dp)){
// CARD SITI
Box(Modifier.width(170.dp).clip(RoundedCornerShape(16.dp)).background(Color.White).padding(12.dp)){
Column{
Text("Siti Rahayu", fontWeight=FontWeight.Bold, fontSize=13.sp)
Text("0.3km • 342 job • 4.9", fontSize=10.sp, color=Color.Gray)
Spacer(Modifier.height(6.dp))
Box(Modifier.clip(RoundedCornerShape(10.dp)).background(Color(0xFFE8F5E9)).padding(horizontal=8.dp, vertical=2.dp)){Text("Tradisional", fontSize=9.sp, color=Green)}
Spacer(Modifier.height(10.dp))
Button(onClick={Toast.makeText(ctx,"Order Siti Rahayu Tradisional 120k/60m",Toast.LENGTH_SHORT).show()}, modifier=Modifier.fillMaxWidth().height(36.dp), colors=ButtonDefaults.buttonColors(Orange), shape=RoundedCornerShape(18.dp)){Text("Order Therapist Ini", fontSize=10.sp, color=Color.White, fontWeight=FontWeight.Bold)}
}
}
Box(Modifier.width(170.dp).clip(RoundedCornerShape(16.dp)).background(Color.White).padding(12.dp)){
Column{
Text("Budi S.", fontWeight=FontWeight.Bold, fontSize=13.sp)
Text("0.6km • 128 job • 4.8", fontSize=10.sp, color=Color.Gray)
Spacer(Modifier.height(6.dp))
Box(Modifier.clip(RoundedCornerShape(10.dp)).background(Color(0xFFE8F5E9)).padding(horizontal=8.dp, vertical=2.dp)){Text("Sport", fontSize=9.sp, color=Green)}
Spacer(Modifier.height(10.dp))
Button(onClick={Toast.makeText(ctx,"Order Budi S.",Toast.LENGTH_SHORT).show()}, modifier=Modifier.fillMaxWidth().height(36.dp), colors=ButtonDefaults.buttonColors(Orange), shape=RoundedCornerShape(18.dp)){Text("Order Therapist Ini", fontSize=10.sp, color=Color.White, fontWeight=FontWeight.Bold)}
}
}
}
Spacer(Modifier.height(16.dp))
}
}
item{Spacer(Modifier.height(12.dp))}
item{
// LAYANAN PIJAT GRID HARGA BARU
Row(Modifier.fillMaxWidth().padding(horizontal=16.dp), horizontalArrangement=Arrangement.SpaceBetween){
Text("Layanan Pijat", fontWeight=FontWeight.Bold, fontSize=16.sp, color=Green)
Text("Ongkos 15k + 5k", fontSize=11.sp, color=Color.Gray)
}
Spacer(Modifier.height(10.dp))
Row(Modifier.fillMaxWidth().padding(horizontal=16.dp), horizontalArrangement=Arrangement.spacedBy(10.dp)){
Box(Modifier.weight(1f).clip(RoundedCornerShape(16.dp)).background(Color.White).padding(12.dp)){
Column{
Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){
Box(Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFF5F5F5)), contentAlignment=Alignment.Center){Text("60'", fontSize=11.sp, fontWeight=FontWeight.Bold)}
Text("Rp120k", color=Orange, fontWeight=FontWeight.Bold, fontSize=12.sp)
}
Spacer(Modifier.height(8.dp))
Text("Tradisional", fontWeight=FontWeight.Bold, fontSize=12.sp)
Text("60 menit • Terapis pro", fontSize=9.sp, color=Color.Gray)
}
}
Box(Modifier.weight(1f).clip(RoundedCornerShape(16.dp)).background(Color.White).padding(12.dp)){
Column{
Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){
Box(Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFF5F5F5)), contentAlignment=Alignment.Center){Text("75'", fontSize=11.sp, fontWeight=FontWeight.Bold)}
Text("Rp100k", color=Orange, fontWeight=FontWeight.Bold, fontSize=12.sp)
}
Spacer(Modifier.height(8.dp))
Text("Refleksi", fontWeight=FontWeight.Bold, fontSize=12.sp)
Text("75 menit • Terapis pro", fontSize=9.sp, color=Color.Gray)
}
}
}
Spacer(Modifier.height(10.dp))
Row(Modifier.fillMaxWidth().padding(horizontal=16.dp), horizontalArrangement=Arrangement.spacedBy(10.dp)){
Box(Modifier.weight(1f).clip(RoundedCornerShape(16.dp)).background(Color.White).padding(12.dp)){
Column{
Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){
Box(Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFF5F5F5)), contentAlignment=Alignment.Center){Text("60'", fontSize=11.sp, fontWeight=FontWeight.Bold)}
Text("Rp135k", color=Orange, fontWeight=FontWeight.Bold, fontSize=12.sp)
}
Spacer(Modifier.height(8.dp))
Text("Aroma Full Body", fontWeight=FontWeight.Bold, fontSize=11.sp)
Text("60 menit • Terapis pro", fontSize=9.sp, color=Color.Gray)
}
}
Box(Modifier.weight(1f).clip(RoundedCornerShape(16.dp)).background(Color.White).padding(12.dp)){
Column{
Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){
Box(Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFF5F5F5)), contentAlignment=Alignment.Center){Text("75'", fontSize=11.sp, fontWeight=FontWeight.Bold)}
Text("Rp135k", color=Orange, fontWeight=FontWeight.Bold, fontSize=12.sp)
}
Spacer(Modifier.height(8.dp))
Text("Trad+Kerokan", fontWeight=FontWeight.Bold, fontSize=11.sp)
Text("75 menit • Terapis pro", fontSize=9.sp, color=Color.Gray)
}
}
}
Spacer(Modifier.height(16.dp))
Button(onClick={Toast.makeText(ctx,"PESAN: Tradisional 120k/60m, Refleksi 100k/75m, Aroma Full 135k/60m, Trad+Kerokan 135k/75m",Toast.LENGTH_LONG).show()}, modifier=Modifier.fillMaxWidth().padding(horizontal=16.dp).height(54.dp), colors=ButtonDefaults.buttonColors(Orange), shape=RoundedCornerShape(14.dp)){Text("PESAN PIJAT SEKARANG", fontWeight=FontWeight.Bold, color=Color.White)}
Spacer(Modifier.height(8.dp))
Button(onClick={prefs.edit().putBoolean("isLoggedIn",false).apply(); isLoggedIn=false}, modifier=Modifier.fillMaxWidth().padding(horizontal=16.dp).height(50.dp), colors=ButtonDefaults.buttonColors(Color.Red), shape=RoundedCornerShape(12.dp)){Text("LOGOUT", color=Color.White, fontWeight=FontWeight.Bold)}
Spacer(Modifier.height(100.dp))
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
prefs.edit().apply{putBoolean("isLoggedIn",true); putString("phone",hp); putString("email",email); apply()}
Toast.makeText(ctx,"DAFTAR BERHASIL! Masuk Home...",Toast.LENGTH_LONG).show()
savedPhone=hp; savedEmail=email; isLoggedIn=true; keyboardController?.hide()
}, modifier=Modifier.fillMaxWidth().height(56.dp), enabled=isFormValid, colors=ButtonDefaults.buttonColors(containerColor=Green, disabledContainerColor=Color.Gray), shape=RoundedCornerShape(14.dp)){
Text(if(isFormValid)"DAFTAR SEKARANG"else"ISI DATA DULU", color=Color.White, fontWeight=FontWeight.Bold, fontSize=16.sp)
}
}
}){ padding ->
LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(padding).padding(16.dp).imePadding()){
item{
Text("Daftar #140 Konsep Baru FIX", fontWeight=FontWeight.Bold, fontSize=22.sp, color=Green)
Text("HOME KAYAK GAMBAR 2 - FIX MERAH", fontSize=10.sp, color=Orange, fontWeight=FontWeight.Bold)
Spacer(Modifier.height(24.dp))
OutlinedTextField(value=hp, onValueChange={hp=it}, label={Text("No Telepon *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp), keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Phone, imeAction=ImeAction.Next), keyboardActions=KeyboardActions(onNext={focusManager.moveFocus(FocusDirection.Down)}), singleLine=true)
Spacer(Modifier.height(12.dp))
OutlinedTextField(value=email, onValueChange={email=it}, label={Text("Email *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp), keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Email, imeAction=ImeAction.Next), keyboardActions=KeyboardActions(onNext={focusManager.moveFocus(FocusDirection.Down)}), singleLine=true)
Spacer(Modifier.height(12.dp))
OutlinedTextField(value=p1, onValueChange={p1=it}, label={Text("Password *")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp), keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Password, imeAction=ImeAction.Next), keyboardActions=KeyboardActions(onNext={focusManager.moveFocus(FocusDirection.Down)}), singleLine=true)
Spacer(Modifier.height(12.dp))
OutlinedTextField(value=p2, onValueChange={p2=it}, label={Text("Konfirmasi Password *")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp), keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Password, imeAction=ImeAction.Done), keyboardActions=KeyboardActions(onDone={focusManager.clearFocus(); keyboardController?.hide()}), singleLine=true)
if(p2.isNotEmpty()){Spacer(Modifier.height(8.dp)); Text(if(p1==p2)"Password cocok"else"Tidak sama", color=if(p1==p2)Color(0xFF4CAF50)else Color.Red, fontSize=12.sp, fontWeight=FontWeight.Bold)}
Spacer(Modifier.height(100.dp))
}
}
}
}
}
}
}
