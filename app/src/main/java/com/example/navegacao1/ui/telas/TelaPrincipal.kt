package com.example.navegacao1.ui.telas

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.navegacao1.model.dados.RetrofitClient
import com.example.navegacao1.model.dados.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun TelaPrincipal(modifier: Modifier = Modifier) {
    var usuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }
    var nome by remember { mutableStateOf("") }
    var idToRemove by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            usuarios = getUsuarios()
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Tela Principal", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            TextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome do Usuário") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                val novoUsuario = Usuario(id = "", nome = nome, senha = "")
                scope.launch {
                    inserirUsuario(novoUsuario)
                    usuarios = getUsuarios()
                }
            }) {
                Text("Adicionar Usuário")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            TextField(
                value = idToRemove,
                onValueChange = { idToRemove = it },
                label = { Text("ID do Usuário a Remover") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                scope.launch {
                    if (idToRemove.isNotEmpty()) {
                        removerUsuario(idToRemove)
                        usuarios = getUsuarios()
                    }
                }
            }) {
                Text("Remover Usuário")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(usuarios) { usuario ->
                Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "ID: ${usuario.id}")
                        Text(text = "Nome: ${usuario.nome}")
                    }
                }
            }
        }
    }
}

private suspend fun getUsuarios(): List<Usuario> {
    return withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.listar()
    }
}

private suspend fun inserirUsuario(usuario: Usuario) {
    withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.adicionar(usuario)
    }
}

private suspend fun removerUsuario(id: String) {
    withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.excluir(id)
    }
}
