package com.pijatin.customer

import android.net.Uri
import android.os.Bundle
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val DarkGreen = Color(0xFF2D4A3E)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var namaRek by remember { mutableStateOf("malikj") }
            var noRek by remember { mutableStateOf("083893330346") }
            var pass by remember { mutableStateOf("") }
            var confirm by remember { mutableStateOf("") }
            var fotoUri by remember { mutableStateOf<Uri?>(null) }
            var ktpUri by remember { mutableStateOf<Uri?>(null) }

            val fotoPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri -> if(uri!=null) fotoUri=uri }
            val ktpPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri -> if(uri!=null) ktpUri=uri }

            MaterialTheme {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().imePadding().padding(16.dp),
                    contentPadding = PaddingValues(bottom = 400.dp)
                ) {
                    item {
                        Text("Pilih Bank / e-Wallet", fontWeight=FontWeight.Bold, fontSize=18.sp)
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){
                            listOf("BCA","BRI","BNI","Mandiri","DANA").forEach{
                                FilterChip(selected=it=="DANA", onClick={}, label={Text(it)})
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(namaRek,{namaRek=it}, label={Text("Nama Rekening (sesuai KTP) *BARU*")}, modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(noRek,{noRek=it}, label={Text("No Rekening / No e-Wallet *BARU*")}, modifier=Modifier.fillMaxWidth())

                        Spacer(Modifier.height(16.dp))
                        Row(horizontalArrangement=Arrangement.spacedBy(10.dp)){
                            Column(horizontalAlignment=Alignment.CenterHorizontally){
                                Box(Modifier.size(80.dp).clip(CircleShape).background(if(fotoUri!=null) Color(0xFF4CAF50) else Color.LightGray), contentAlignment=Alignment.Center){
                                    Text(if(fotoUri!=null) "✅" else "80px", color=Color.White, fontWeight=FontWeight.Bold)
                                }
                                Spacer(Modifier.height(6.dp))
                                Button(onClick={ fotoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, modifier=Modifier.height(32.dp)){
                                    Text("FOTO GALERI", fontSize=9.sp)
                                }
                                if(fotoUri!=null) Text("Foto OK ✅", fontSize=10.sp, color=Color(0xFF4CAF50), fontWeight=FontWeight.Bold)
                            }
                            Column(Modifier.weight(1f)){
                                Box(Modifier.fillMaxWidth().height(110.dp).clip(RoundedCornerShape(10.dp)).background(if(ktpUri!=null) Color(0xFF4CAF50) else Color.Gray), contentAlignment=Alignment.Center){
                                    Text(if(ktpUri!=null) "✅ KTP OK" else "KTP 200px wajib tap ganti", color=Color.White, fontSize=12.sp)
                                }
                                Spacer(Modifier.height(6.dp))
                                Button(onClick={ ktpPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, modifier=Modifier.fillMaxWidth().height(38.dp)){
                                    Text(if(ktpUri!=null) "✅ KTP OK - GANTI" else "Upload KTP GALERI", fontSize=10.sp)
                                }
                                if(ktpUri!=null) Text("KTP dipilih: ${ktpUri?.toString()?.takeLast(20)}", fontSize=9.sp)
                            }
                        }

                        Spacer(Modifier.height(24.dp))
                        OutlinedTextField(pass,{pass=it}, label={Text("Password")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(confirm,{confirm=it}, label={Text("Konfirmasi Password *BARU*")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth())
                        if(confirm.isNotEmpty()){
                            Text(if(confirm==pass) "✅ Password cocok" else "❌ Tidak sama", color=if(confirm==pass) Color(0xFF4CAF50) else Color.Red, fontSize=12.sp, fontWeight=FontWeight.Bold)
                        }

                        Spacer(Modifier.height(28.dp))
                        val ok = namaRek.isNotEmpty() && noRek.isNotEmpty() && fotoUri!=null && ktpUri!=null && pass.isNotEmpty() && pass==confirm
                        Button(onClick={}, enabled=ok, modifier=Modifier.fillMaxWidth().height(52.dp), colors=ButtonDefaults.buttonColors(DarkGreen)){
                            Text("DAFTAR → Saldo 0")
                        }
                        Spacer(Modifier.height(8.dp))
                        OutlinedButton(onClick={}, modifier=Modifier.fillMaxWidth().height(44.dp)){ Text("SIMPAN DRAFT") }
                        Spacer(Modifier.height(20.dp))
                        Text("✅ #73 Fix: Galeri Real + Scroll 400dp + Password keliatan", color=Color(0xFF4CAF50), fontSize=11.sp, fontWeight=FontWeight.Bold)
                    }
                }
            }
        }
    }
}
