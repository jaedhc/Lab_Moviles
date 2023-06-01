package com.example.labb.listas

data class ListaChats(
    var idChat: String = "",
    var nameChat: String = "",
    var usersChat: List<String> = emptyList()
)
