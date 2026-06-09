package com.example.controlegastos

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.controledegastos.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val txtSaldo = findViewById<TextView>(R.id.txtSaldo)
        val txtListaGastos = findViewById<TextView>(R.id.txtListaGastos)

        // Conectamos no mesmo nó "gastos" do Firebase
        val database = FirebaseDatabase.getInstance().reference.child("gastos")

        // O Firebase usa um "Listener" (ouvinte). Toda vez que um dado mudar na nuvem,
        // ele roda esse código automaticamente em tempo real! (Muito importante para a prova)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var textoCompleto = ""
                var saldoTotal = 0.0

                // Esse "for" funciona igualzinho ao "for gasto in snapshot" do Python
                for (item in snapshot.children) {
                    val descricao = item.child("descricao").value.toString()
                    val valorString = item.child("valor").value.toString()
                    val valor = valorString.toDoubleOrNull() ?: 0.0

                    // Acumula o valor total dos gastos
                    saldoTotal += valor

                    // Vai montando o texto da lista linha por linha
                    textoCompleto += "• $descricao: R$ $valorString\n"
                }

                // Atualiza a tela do celular com os dados reais
                txtSaldo.text = "Total Gasto: R$ $saldoTotal"
                if (textoCompleto.isNotEmpty()) {
                    txtListaGastos.text = textoCompleto
                } else {
                    txtListaGastos.text = "Nenhum gasto cadastrado."
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Se der erro de permissão no banco, roda aqui
                txtSaldo.text = "Erro ao carregar dados."
            }
        })
    }
}
