package com.pijatin.customer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
val LightBg = Color(0xFFF6F3EE)
class MainActivity : ComponentActivity(){
override fun onCreate(b:Bundle?){
super.onCreate(b)
setContent{
val ctx=LocalContext.current
val prefs=ctx.getSharedPreferences("PijatIN_Login",0)
var isLoggedIn by remember{mutableStateOf(prefs.getBoolean("isLoggedIn",false))}
var savedPhone by remember{mutableStateOf(prefs.getString("phone","")?:"")}
var savedEmail by remember{mutableStateOf(prefs.getString("email","")?:"")}
var selectedNav by remember{mutableStateOf(0)}
var selectedFilter by remember{mutableStateOf(0)}
if(isLoggedIn){
Scaffold(
containerColor=LightBg,
bottomBar={
NavigationBar(containerColor=Color.White, tonalElevation=8.dp){
NavigationBarItem(selected=selectedNav==0, onClick={selectedNav=0}, icon={Text("🏠")}, label={Text("Beranda", fontSize=10.sp)})
NavigationBarItem(selected=selectedNav==1, onClick={selectedNav=1}, icon={Text("📋")}, label={Text("Pesanan", fontSize=10.sp)})
NavigationBarItem(selected=selectedNav==2, onClick={selectedNav=2}, icon={Text("🕐")}, label={Text("Riwayat", fontSize=10.sp)})
NavigationBarItem(selected=selectedNav==3, onClick={selectedNav=3}, icon={Text("💳")}, label={Text("Saldo", fontSize=10.sp)})
NavigationBarItem(selected=selectedNav==4, onClick={selectedNav=4; selectedNav=4}, icon={Text("👤")}, label={Text("Profil", fontSize=10.sp)})
}
}
){ pad ->
LazyColumn(Modifier.fillMaxSize().background(LightBg).padding(pad)){
item{
// HEADER TOP KAYAK KONSEP GAMBAR 2
Row(Modifier.fillMaxWidth().background(Color.White).padding(16.dp), verticalAlignment=Alignment.CenterVertically, horizontalArrangement=Arrangement.SpaceBetween){
Row(verticalAlignment=Alignment.CenterVertically){
Box(Modifier.size(44.dp).clip(CircleShape).background(Color.Black), contentAlignment=Alignment.Center){Text("▲", color=Color.White, fontWeight=FontWeight.Bold)}
Spacer(Modifier.width(10.dp))
Column{
Text("maliki", fontWeight=FontWeight.Bold, fontSize=16.sp, color=Color.Black)
Text("GG becek • Online", fontSize=11.sp, color=Color.Gray)
}
}
Column(horizontalAlignment=Alignment.End){
Text("Saldo", fontSize=11.sp, color=Color.Gray)
Text("Rp50.000", fontWeight=FontWeight.Bold, fontSize=16.sp, color=Color.Black)
}
}
}
item{
// THERAPIST TERDEKAT
Column(Modifier.fillMaxWidth().background(Color.White).padding(bottom=12.dp)){
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
LazyRow(Modifier.padding(horizontal=16.dp), horizontalArrangement=Arrangement.spacedBy(8.dp)){
item{
Button(onClick={selectedFilter=0}, shape=RoundedCornerShape(20.dp), colors=ButtonDefaults.buttonColors(if(selectedFilter==0)Green else Color.White), contentPadding=PaddingValues(horizontal=16.dp, vertical=8.dp)){Text("📍 Jarak", color=if(selectedFilter==0)Color.White else Color.Gray, fontSize=12.sp)}
}
item{
Button(onClick={selectedFilter=1}, shape=RoundedCornerShape(20.dp), colors=ButtonDefaults.buttonColors(if(selectedFilter==1)Green else Color.White), contentPadding=PaddingValues(horizontal=16.dp, vertical=8.dp)){Text("⭐ Rating", color=if(selectedFilter==1)Color.White else Color.Gray, fontSize=12.sp)}
}
item{
Button(onClick={selectedFilter=2}, shape=RoundedCornerShape(20.dp), colors=ButtonDefaults.buttonColors(if(selectedFilter==2)Green else Color.White), contentPadding=PaddingValues(horizontal=16.dp, vertical=8.dp)){Text("Tradisional", color=if(selectedFilter==2)Color.White else Color.Gray, fontSize=12.sp)}
}
item{
Button(onClick={selectedFilter=3}, shape=RoundedCornerShape(20.dp), colors=ButtonDefaults.buttonColors(if(selectedFilter==3)Green else Color.White), contentPadding=PaddingValues(horizontal=16.dp, vertical=8.dp)){Text("Sport", color=if(selectedFilter==3)Color.White else Color.Gray, fontSize=12.sp)}
}
}
Spacer(Modifier.height(12.dp))
LazyRow(Modifier.padding(horizontal=16.dp), horizontalArrangement=Arrangement.spacedBy(12.dp)){
item{
Card(Modifier.width(200.dp), shape=RoundedCornerShape(16.dp), colors=CardDefaults.cardColors(Color.White), elevation=CardDefaults.cardElevation(2.dp)){
Column(Modifier.padding(14.dp)){
Text("SR Siti Rahayu", fontWeight=FontWeight.Bold, fontSize=14.sp)
Spacer(Modifier.height(2.dp))
Text("0.3km • 342 job • ⭐ 4.9", fontSize=11.sp, color=Color.Gray)
Spacer(Modifier.height(8.dp))
Box(Modifier.clip(RoundedCornerShape(12.dp)).background(Color(0xFFE8F5E9)).padding(horizontal=10.dp, vertical=4.dp)){Text("Tradisional", fontSize=10.sp, color=Green)}
Spacer(Modifier.height(12.dp))
Button(onClick={Toast.makeText(ctx,"Order Siti Rahayu - Tradisional 120k/60m",Toast.LENGTH_SHORT).show()}, modifier=Modifier.fillMaxWidth().height(38.dp), colors=ButtonDefaults.buttonColors(Orange), shape=RoundedCornerShape(20.dp)){Text("Order Therapist Ini", fontSize=11.sp, fontWeight=FontWeight.Bold, color=Color.White)}
}
}
}
item{
Card(Modifier.width(200.dp), shape=RoundedCornerShape(16.dp), colors=CardDefaults.cardColors(Color.White), elevation=CardDefaults.cardElevation(2.dp)){
Column(Modifier.padding(14.dp)){
Row(verticalAlignment=Alignment.CenterVertically){
Box(Modifier.size(36.dp).clip(CircleShape).background(Color.LightGray))
Spacer(Modifier.width(8.dp))
Column{
Text("BS Budi S.", fontWeight=FontWeight.Bold, fontSize=14.sp)
Text("0.6km • 128 job • ⭐ 4.8", fontSize=11.sp, color=Color.Gray)
}
}
Spacer(Modifier.height(8.dp))
Box(Modifier.clip(RoundedCornerShape(12.dp)).background(Color(0xFFE8F5E9)).padding(horizontal=10.dp, vertical=4.dp)){Text("Sport", fontSize=10.sp, color=Green)}
Spacer(Modifier.height(12.dp))
Button(onClick={Toast.makeText(ctx,"Order Budi S. - Sport",Toast.LENGTH_SHORT).show()}, modifier=Modifier.fillMaxWidth().height(38.dp), colors=ButtonDefaults.buttonColors(Orange), shape=RoundedCornerShape(20.dp)){Text("⚡ Order Therapist Ini", fontSize=11.sp, fontWeight=FontWeight.Bold, color=Color.White)}
}
}
}
}
Spacer(Modifier.height(12.dp))
}
}
}
item{Spacer(Modifier.height(12.dp))}
item{
// LAYANAN PIJAT - HARGA BARU VIO!
Row(Modifier.fillMaxWidth().padding(horizontal=16.dp), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically){
Text("Layanan Pijat", fontWeight=FontWeight.Bold, fontSize=16.sp, color=Green)
Text("Ongkos 15k + 5k", fontSize=11.sp, color=Color.Gray)
}
Spacer(Modifier.height(10.dp))
Row(Modifier.fillMaxWidth().padding(horizontal=16.dp), horizontalArrangement=Arrangement.spacedBy(10.dp)){
Card(Modifier.weight(1f), shape=RoundedCornerShape(16.dp), colors=CardDefaults.cardColors(Color.White), elevation=CardDefaults.cardElevation(2.dp)){
Column(Modifier.padding(12.dp)){
Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically){
Box(Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFF5F5F5)), contentAlignment=Alignment.Center){Text("60'", fontWeight=FontWeight.Bold, fontSize=12.sp)}
Text("Rp120k", color=Orange, fontWeight=FontWeight.Bold, fontSize=13.sp)
}
Spacer(Modifier.height(8.dp))
Text("Tradisional", fontWeight=FontWeight.Bold, fontSize=13.sp)
Text("60 menit • Terapis pro", fontSize=10.sp, color=Color.Gray)
}
}
Card(Modifier.weight(1f), shape=RoundedCornerShape(16.dp), colors=CardDefaults.cardColors(Color.White), elevation=CardDefaults.cardElevation(2.dp)){
Column(Modifier.padding(12.dp)){
Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically){
Box(Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFF5F5F5)), contentAlignment=Alignment.Center){Text("75'", fontWeight=FontWeight.Bold, fontSize=12.sp)}
Text("Rp100k", color=Orange, fontWeight=FontWeight.Bold, fontSize=13.sp)
}
Spacer(Modifier.height(8.dp))
Text("Refleksi", fontWeight=FontWeight.Bold, fontSize=13.sp)
Text("75 menit • Terapis pro", fontSize=10.sp, color=Color.Gray)
}
}
}
Spacer(Modifier.height(10.dp))
Row(Modifier.fillMaxWidth().padding(horizontal=16.dp), horizontalArrangement=Arrangement.spacedBy(10.dp)){
Card(Modifier.weight(1f), shape=RoundedCornerShape(16.dp), colors=CardDefaults.cardColors(Color.White), elevation=CardDefaults.cardElevation(2.dp)){
Column(Modifier.padding(12.dp)){
Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically){
Box(Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFF5F5F5)), contentAlignment=Alignment.Center){Text("60'", fontWeight=FontWeight.Bold, fontSize=12.sp)}
Text("Rp135k", color=Orange, fontWeight=FontWeight.Bold, fontSize=13.sp)
}
Spacer(Modifier.height(8.dp))
Text("Aromatherapy Full Body", fontWeight=FontWeight.Bold, fontSize=12.sp)
Text("60 menit • Terapis pro", fontSize=10.sp, color=Color.Gray)
}
}
Card(Modifier.weight(1f), shape=RoundedCornerShape(16.dp), colors=CardDefaults.cardColors(Color.White), elevation=CardDefaults.cardElevation(2.dp)){
Column(Modifier.padding(12.dp)){
Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically){
Box(Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFF5F5F5)), contentAlignment=Alignment.Center){Text("75'", fontWeight=FontWeight.Bold, fontSize=12.sp)}
Text("Rp135k", color=Orange, fontWeight=FontWeight.Bold, fontSize=13.sp)
}
Spacer(Modifier.height(8.dp))
Text("Tradisional+Kerokan", fontWeight=FontWeight.Bold, fontSize=12.sp)
Text("75 menit • Terapis pro", fontSize=10.sp, color=Color.Gray)
}
}
}
Spacer(Modifier.height(16.dp))
Button(onClick={Toast.makeText(ctx,"Pesan: Tradisional 120k/60m, Refleksi 100k/75m, Aroma Full Body 135k/60m, Trad+Kerokan 135k/75m",Toast.LENGTH_LONG).show()}, modifier=Modifier.fillMaxWidth().padding(horizontal=16.dp).height(54.dp), colors=ButtonDefaults.buttonColors(Orange), shape=RoundedCornerShape(14.dp)){Text("PESAN PIJAT SEKARANG", fontWeight=FontWeight.Bold, color=Color.White)}
Spacer(Modifier.height(80.dp))
}
}
}
} else {
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
LazyColumn(Modifier.fillMaxSize().background(LightBg).padding(padding).padding(16.dp).imePadding()){
item{
Text("Daftar #139 Konsep Baru", fontWeight=FontWeight.Bold, fontSize=22.sp, color=Green)
Text("HOME KAYAK KONSEP GAMBAR 2 + HARGA BARU", fontSize=10.sp, color=Orange, fontWeight=FontWeight.Bold)
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
