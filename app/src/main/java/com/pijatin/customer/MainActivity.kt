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
if(isLoggedIn){
Box(Modifier.fillMaxSize().background(Color.White).padding(20.dp)){
Column{
Text("🏠 Beranda PijatIN", fontSize=24.sp, fontWeight=FontWeight.Bold, color=Green)
Spacer(Modifier.height(12.dp))
Text("Login sebagai: "+savedPhone, fontSize=14.sp)
Spacer(Modifier.height(12.dp))
Text("✅ Data tersimpan & login otomatis!", color=Color(0xFF4CAF50), fontWeight=FontWeight.Bold)
Spacer(Modifier.height(24.dp))
Button(onClick={prefs.edit().putBoolean("isLoggedIn",false).apply(); isLoggedIn=false}, colors=ButtonDefaults.buttonColors(Color.Red)){Text("LOGOUT")}
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
prefs.edit().apply{putBoolean("isLoggedIn",true); putString("phone",hp); putString("email",email); putString("password",p1); putLong("loginTime",System.currentTimeMillis()); apply()}
Toast.makeText(ctx,"✅ DAFTAR BERHASIL! Login otomatis...",Toast.LENGTH_LONG).show()
savedPhone=hp; isLoggedIn=true; keyboardController?.hide()
}, modifier=Modifier.fillMaxWidth().height(56.dp), enabled=isFormValid, colors=ButtonDefaults.buttonColors(containerColor=Green, disabledContainerColor=Color.Gray), shape=RoundedCornerShape(14.dp)){
Text(if(isFormValid)"DAFTAR SEKARANG ✅"else"ISI DATA DULU", color=Color.White, fontWeight=FontWeight.Bold, fontSize=16.sp)
}
}
}){ padding ->
LazyColumn(Modifier.fillMaxSize().background(Color(0xFFF5F5F5)).padding(padding).padding(16.dp).imePadding(), contentPadding=PaddingValues(bottom=20.dp)){
item{
Text("Daftar #132 FIX SCROLL", fontWeight=FontWeight.Bold, fontSize=22.sp, color=Green)
Text("FIX TOMBOL BISA DI KLIK + AUTO LOGIN HOME", fontSize=10.sp, color=Orange, fontWeight=FontWeight.Bold)
Spacer(Modifier.height(24.dp))
OutlinedTextField(value=hp, onValueChange={hp=it}, label={Text("No Telepon *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp), keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Phone, imeAction=ImeAction.Next), keyboardActions=KeyboardActions(onNext={focusManager.moveFocus(FocusDirection.Down)}), singleLine=true)
Spacer(Modifier.height(12.dp))
OutlinedTextField(value=email, onValueChange={email=it}, label={Text("Email *")}, modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp), keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Email, imeAction=ImeAction.Next), keyboardActions=KeyboardActions(onNext={focusManager.moveFocus(FocusDirection.Down)}), singleLine=true)
Spacer(Modifier.height(12.dp))
OutlinedTextField(value=p1, onValueChange={p1=it}, label={Text("Password *")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp), keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Password, imeAction=ImeAction.Next), keyboardActions=KeyboardActions(onNext={focusManager.moveFocus(FocusDirection.Down)}), singleLine=true)
Spacer(Modifier.height(12.dp))
OutlinedTextField(value=p2, onValueChange={p2=it}, label={Text("Konfirmasi Password *")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth(), shape=RoundedCornerShape(12.dp), keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Password, imeAction=ImeAction.Done), keyboardActions=KeyboardActions(onDone={focusManager.clearFocus(); keyboardController?.hide()}), singleLine=true)
if(p2.isNotEmpty()){Spacer(Modifier.height(8.dp)); Text(if(p1==p2)"✅ Password cocok"else"❌ Tidak sama", color=if(p1==p2)Color(0xFF4CAF50)else Color.Red, fontSize=12.sp, fontWeight=FontWeight.Bold)}
Spacer(Modifier.height(20.dp))
Text("✅ FIX: Tombol di bawah (bottomBar) gak ketutup keyboard! Kursor Next otomatis turun.", fontSize=9.sp, color=Color(0xFF4CAF50))
Spacer(Modifier.height(100.dp))
}
}
}
}
}
}
}
