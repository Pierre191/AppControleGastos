package com.example.controledegastos

import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class NovoGastoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_gasto)

        // Aqui conectamos os componentes do XML com o Kotlin (Igual no Python)
        val editValor = findViewById<EditText>(R.id.editValor)
        val editDescricao = findViewById<EditText>(R.id.editDescricao)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)

        // Criamos uma referência para o Banco de Dados do Firebase
        val database = FirebaseDatabase.getInstance().reference.child("gastos")

        // Evento de clique do botão (Igual o command do Button no Python)
        btnSalvar.setOnClickListener {
            val valor = editValor.text.toString()
            val descricao = editDescricao.text.toString()

            if (valor.isNotEmpty() && descricao.isNotEmpty()) {
                // Criamos um dicionário/mapa com os dados (Igual Dicionário no Python)
                val dadosGasto = mapOf(
                    "valor" to valor,
                    "descricao" to descricao
                )

                // Salva na nuvem gerando um ID único para cada gasto
                database.push().setValue(dadosGasto).addOnSuccessListener {
                    Toast.makeText(this, "Gasto salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    editValor.text.clear()
                    editDescricao.text.clear()
                }.addOnFailureListener {
                    Toast.makeText(this, "Erro ao salvar na nuvem.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
