package com.pijatin.customer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
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
if(isLoggedIn){
LazyColumn(Modifier.fillMaxSize().background(Color.White).padding(16.dp)){
item{
Text("🏠 Beranda PijatIN", fontSize=24.sp, fontWeight=FontWeight.Bold, color=Green)
Spacer(Modifier.height(8.dp))
Text("Login sebagai: "+savedPhone, fontSize=14.sp, fontWeight=FontWeight.Bold)
Text(savedEmail, fontSize=11.sp, color=Color.Gray)
Spacer(Modifier.height(12.dp))
Box(Modifier.fillMaxWidth().background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp)).padding(14.dp)){
Column{
Text("✅ Data tersimpan & login otomatis!", color=Color(0xFF2E7D32), fontWeight=FontWeight.Bold, fontSize=13.sp)
Text("Kamu sudah login otomatis setelah daftar!", fontSize=11.sp, color=Color(0xFF1B5E20))
}
}
Spacer(Modifier.height(20.dp))
Text("💆 Layanan PijatIN", fontWeight=FontWeight.Bold, fontSize=16.sp, color=Green)
Spacer(Modifier.height(12.dp))
Button(onClick={Toast.makeText(ctx,"Pijat Tradisional 90 menit Rp 120k",Toast.LENGTH_SHORT).show()}, modifier=Modifier.fillMaxWidth().height(56.dp), shape=RoundedCornerShape(12.dp), colors=ButtonDefaults.buttonColors(Color.White)){Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){Text("💆‍♀️ Pijat Tradisional", color=Color.Black, fontWeight=FontWeight.Bold); Text("Rp 120k", color=Orange, fontWeight=FontWeight.Bold)}}
Spacer(Modifier.height(10.dp))
Button(onClick={Toast.makeText(ctx,"Pijat Refleksi 60 menit Rp 100k",Toast.LENGTH_SHORT).show()}, modifier=Modifier.fillMaxWidth().height(56.dp), shape=RoundedCornerShape(12.dp), colors=ButtonDefaults.buttonColors(Color.White)){Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){Text("🦶 Pijat Refleksi", color=Color.Black, fontWeight=FontWeight.Bold); Text("Rp 100k", color=Orange, fontWeight=FontWeight.Bold)}}
Spacer(Modifier.height(10.dp))
Button(onClick={Toast.makeText(ctx,"Aromatherapy 90 menit Rp 150k",Toast.LENGTH_SHORT).show()}, modifier=Modifier.fillMaxWidth().height(56.dp), shape=RoundedCornerShape(12.dp), colors=ButtonDefaults.buttonColors(Color.White)){Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween){Text("🌸 Aromatherapy", color=Color.Black, fontWeight=FontWeight.Bold); Text("Rp 150k", color=Orange, fontWeight=FontWeight.Bold)}}
Spacer(Modifier.height(20.dp))
Button(onClick={Toast.makeText(ctx,"Mencari terapis terdekat...",Toast.LENGTH_SHORT).show()}, modifier=Modifier.fillMaxWidth().height(56.dp), colors=ButtonDefaults.buttonColors(Orange), shape=RoundedCornerShape(14.dp)){Text("🔍 PESAN PIJAT SEKARANG", color=Color.White, fontWeight=FontWeight.Bold)}
Spacer(Modifier.height(20.dp))
Button(onClick={prefs.edit().putBoolean("isLoggedIn",false).apply(); isLoggedIn=false; Toast.makeText(ctx,"Logout",Toast.LENGTH_SHORT).show()}, modifier=Modifier.fillMaxWidth().height(50.dp), colors=ButtonDefaults.buttonColors(Color.Red), shape=RoundedCornerShape(12.dp)){Text("LOGOUT", color=Color.White, fontWeight=FontWeight.Bold)}
Spacer(Modifier.height(20.dp))
Text("✅ AUTO LOGIN BERHASIL - Dari daftar langsung ke Home!", fontSize=10.sp, color=Color(0xFF4CAF50), fontWeight=FontWeight.Bold)
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
Toast.makeText(ctx,"✅ DAFTAR BERHASIL! Masuk Home...",Toast.LENGTH_LONG).show()
savedPhone=hp; savedEmail=email; isLoggedIn=true; keyboardController?.hide()
}, modifier=Modifier.fillMaxWidth().height(56.dp), enabled=isFormValid, colors=ButtonDefaults.buttonColors(containerColor=Green, disabledContainerColor=Color.Gray), shape=RoundedCornerShape(14.dp)){
Text(if(isFormValid)"DAFTAR SEKARANG ✅"else"ISI DATA DULU", color=Color.White, fontWeight=FontWeight.Bold, fontSize=16.sp)
}
}
}){ padding ->
LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(padding).padding(16.dp).imePadding()){
item{
Text("Daftar #134 FIX HIJAU", fontWeight=FontWeight.Bold, fontSize=22.sp, color=Green)
Text("TOMBOL BISA KLIK + AUTO LOGIN HOME + HOME LENGKAP", fontSize=9.sp, color=Orange, fontWeight=FontWeight.Bold)
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
