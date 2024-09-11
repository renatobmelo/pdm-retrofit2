package com.example.navegacao1.ui.telas

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.navegacao1.model.dados.Endereco
import com.example.navegacao1.model.dados.RetrofitClient
import com.example.navegacao1.model.dados.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun TelaPrincipal(modifier: Modifier = Modifier, onLogoffClick: () -> Unit) {
    var scope = rememberCoroutineScope()

    Column(modifier = modifier) {
        Text(text = "Tela Principal")
        var usuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }
        var endereco by remember { mutableStateOf<Endereco>(Endereco()) }

        LaunchedEffect(Unit) {
            scope.launch {
                Log.d("principal", "aqui")
                usuarios = getUsuarios()

//                Log.d("principal", getEndereco().logradouro)
//                endereco = getEndereco()
            }
        }

        Text(endereco.logradouro)

        Button(onClick = {
//            scope.launch(Dispatchers.IO) {
//                usuarioDAO.buscar( callback = { usuariosRetornados ->
//                    usuarios.clear()
//                    usuarios.addAll(usuariosRetornados)
//                })
//            }
        }) {
            Text("Carregar")
        }
        Button(onClick = { onLogoffClick() }) {
            Text("Sair")
        }

        //Carrega sob demanda à medida que o usuário rola na tela
        LazyColumn {
            items(usuarios) { usuario ->
                //TODO melhore esse card. Estão colados, e com pouca informação. Deixe mais
                // elegante.
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(text = usuario.nome)
                    }
                }
            }
        }
    }

}

suspend fun getUsuarios(): List<Usuario> {
    return withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.listar()
    }
}

suspend fun getEndereco(): Endereco {
    return withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.getEndereco()
    }
}