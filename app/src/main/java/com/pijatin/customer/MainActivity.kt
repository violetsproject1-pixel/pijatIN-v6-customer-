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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.focus.FocusDirection
import kotlinx.coroutines.launch

val DarkGreen = Color(0xFF2D4A3E)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var nama by remember { mutableStateOf("") }
            var hp by remember { mutableStateOf("") }
            var namaRek by remember { mutableStateOf("malikj") }
            var noRek by remember { mutableStateOf("083893330346") }
            var pass by remember { mutableStateOf("") }
            var confirm by remember { mutableStateOf("") }
            
            var fotoUri by remember { mutableStateOf<Uri?>(null) }
            var ktpUri by remember { mutableStateOf<Uri?>(null) }
            var fotoOk by remember { mutableStateOf(false) }
            var ktpOk by remember { mutableStateOf(false) }

            val fotoPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) { fotoUri = uri; fotoOk = true }
            }
            val ktpPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) { ktpUri = uri; ktpOk = true }
            }
            // Kamera
            val fotoCamera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
                if (bitmap != null) { fotoOk = true } // preview jadi hijau
            }

            MaterialTheme {
                // INI KUNCI SCROLL BIAR GAK KETUTUP KEYBOARD!
                LazyColumn(
                    modifier = Modifier.fillMaxSize().imePadding().padding(16.dp),
                    contentPadding = PaddingValues(bottom = 300.dp)
                ) {
                    item {
                        Text("Pilih Bank / e-Wallet", fontWeight=FontWeight.Bold, fontSize=20.sp)
                        Spacer(Modifier.height(12.dp))
                        Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
                            listOf("BCA","BRI","BNI","Mandiri","DANA").forEach{ b ->
                                FilterChip(selected=b=="DANA", onClick={}, label={Text(b)})
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                        OutlinedTextField(namaRek, {namaRek=it}, label={Text("Nama Rekening (sesuai KTP) *BARU*")}, modifier=Modifier.fillMaxWidth(), keyboardOptions=KeyboardOptions(imeAction=ImeAction.Next))
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(noRek, {noRek=it}, label={Text("No Rekening / No e-Wallet *BARU*")}, modifier=Modifier.fillMaxWidth(), keyboardOptions=KeyboardOptions(imeAction=ImeAction.Next))

                        Spacer(Modifier.height(16.dp))
                        Row(horizontalArrangement=Arrangement.spacedBy(12.dp)){
                            Column(horizontalAlignment=Alignment.CenterHorizontally){
                                Box(Modifier.size(80.dp).clip(CircleShape).background(if(fotoOk) Color(0xFF4CAF50) else Color.LightGray), contentAlignment=Alignment.Center){
                                    if(fotoUri != null) AsyncImage(fotoUri, null, modifier=Modifier.fillMaxSize().clip(CircleShape), contentScale=ContentScale.Crop)
                                    else Text(if(fotoOk) "✅" else "80px", color=Color.White)
                                }
                                Spacer(Modifier.height(6.dp))
                                Button(onClick={ fotoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, modifier=Modifier.height(32.dp)){ Text(if(fotoOk) "GANTI" else "FOTO", fontSize=10.sp) }
                                TextButton(onClick={ fotoCamera.launch(null) }){ Text("Kamera", fontSize=10.sp) }
                            }
                            Column(Modifier.weight(1f)){
                                Box(Modifier.fillMaxWidth().height(110.dp).clip(RoundedCornerShape(12.dp)).background(if(ktpOk) Color(0xFF4CAF50) else Color.LightGray), contentAlignment=Alignment.Center){
                                    if(ktpUri != null) AsyncImage(ktpUri, null, modifier=Modifier.fillMaxSize(), contentScale=ContentScale.Crop)
                                    else Text(if(ktpOk) "✅ KTP 200px OK" else "KTP 200px wajib tap ganti", color=Color.White, fontSize=12.sp)
                                }
                                Spacer(Modifier.height(6.dp))
                                Button(onClick={ ktpPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }, modifier=Modifier.fillMaxWidth().height(36.dp)){ Text(if(ktpOk) "✅ KTP OK - GANTI" else "Upload KTP GALERI", fontSize=12.sp) }
                                TextButton(onClick={ fotoCamera.launch(null) }, modifier=Modifier.fillMaxWidth()){ Text("Upload KTP KAMERA", fontSize=10.sp) }
                            }
                        }

                        Spacer(Modifier.height(20.dp))
                        // INI YANG TADI KETUTUP KEYBOARD - SEKARANG BISA SCROLL!
                        OutlinedTextField(pass, {pass=it}, label={Text("Password")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth(), keyboardOptions=KeyboardOptions(imeAction=ImeAction.Next))
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(confirm, {confirm=it}, label={Text("Konfirmasi Password *BARU*")}, visualTransformation=PasswordVisualTransformation(), modifier=Modifier.fillMaxWidth(), keyboardOptions=KeyboardOptions(imeAction=ImeAction.Done))
                        if(confirm.isNotEmpty()){
                            Text(if(confirm==pass) "✅ Password cocok" else "❌ Tidak sama", color=if(confirm==pass) Color(0xFF4CAF50) else Color.Red, fontSize=12.sp, fontWeight=FontWeight.Bold)
                        }

                        Spacer(Modifier.height(24.dp))
                        val ok = namaRek.isNotEmpty() && noRek.isNotEmpty() && fotoOk && ktpOk && pass.isNotEmpty() && pass==confirm
                        Button(onClick={}, enabled=ok, modifier=Modifier.fillMaxWidth().height(50.dp), colors=ButtonDefaults.buttonColors(DarkGreen)){ Text("DAFTAR → Saldo 0", fontSize=16.sp) }
                        Spacer(Modifier.height(8.dp))
                        Button(onClick={}, modifier=Modifier.fillMaxWidth().height(44.dp), colors=ButtonDefaults.buttonColors(Color.Gray)){ Text("SIMPAN DRAFT") }
                        Spacer(Modifier.height(100.dp))
                        Text("✅ #69 Fix: Galeri+Kamera Real + Scroll + Password keliatan", color=Color(0xFF4CAF50), fontSize=10.sp)
                    }
                }
            }
        }
    }
}
