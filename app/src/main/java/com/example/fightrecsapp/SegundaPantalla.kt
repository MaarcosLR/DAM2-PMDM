package com.example.fightrecsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION", "NAME_SHADOWING")
class SegundaPantalla : AppCompatActivity() {

    private lateinit var simpleList: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var fab: FloatingActionButton
    private lateinit var trashFab: FloatingActionButton
    private lateinit var backFab: FloatingActionButton

    private val gameNames = mutableListOf(
        "Street Fighter V",
        "Mortal Kombat 11",
        "Tekken 7",
        "Super Smash Bros. Ultimate",
        "Dragon Ball Fighter Z"
    )

    private val gameInfos = mutableListOf(
        "Un juego de lucha clásico con personajes icónicos.",
        "La última entrega de la serie Mortal Kombat, famosa por su violencia.",
        "Un juego de lucha 3D con combos y personajes únicos.",
        "Un juego de lucha de crossover con personajes de Nintendo.",
        "Un juego de lucha basado en el anime Dragon Ball."
    )

    // Lista para almacenar los juegos eliminados (papelera)
    private val deletedGamesNames = mutableListOf<String>()
    private val deletedGamesInfos = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segunda_pantalla)

        // Inicializar componentes
        initComponents()

        // Configurar botones y acciones
        fab.setOnClickListener { view ->
            showAddGameDialog(view)  // Mostrar el cuadro de diálogo para agregar un juego
        }

        trashFab.setOnClickListener { view ->
            showTrashDialog(view)  // Mostrar los juegos eliminados (papelera)
        }

        backFab.setOnClickListener {
            onBackPressed()  // Volver a la actividad anterior
        }

        // Mostrar más información al hacer clic en un elemento de la lista
        simpleList.setOnItemClickListener { _, _, position, _ ->
            val selectedGame = gameNames[position]
            val gameInfo = gameInfos[position]
            showGameInfoDialog(selectedGame, gameInfo, position)
        }
    }

    private fun initComponents() {
        // Inicialización de vistas
        simpleList = findViewById(R.id.simple_list)
        fab = findViewById(R.id.fab)
        trashFab = findViewById(R.id.fab_trash)
        backFab = findViewById(R.id.fab_home)

        // Inicialización del adaptador para la lista de juegos
        adapter = object : ArrayAdapter<String>(this, R.layout.list_item_layout, R.id.text_item, gameNames) {
            override fun getView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(R.id.text_item)

                // Establecer color de texto blanco solo para los juegos en la lista principal
                textView.setTextColor(resources.getColor(android.R.color.white, null))

                return view
            }
        }
        simpleList.adapter = adapter
    }

    // Función para mostrar el cuadro de diálogo para agregar un juego
    private fun showAddGameDialog(view: View) {
        val layout = LayoutInflater.from(this).inflate(R.layout.dialog_add_game, null)
        val nameEditText = layout.findViewById<EditText>(R.id.edit_game_name)
        val infoEditText = layout.findViewById<EditText>(R.id.edit_game_info)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Agregar nuevo juego")
            .setView(layout)
            .setPositiveButton("Agregar") { dialog, _ ->
                val newGameName = nameEditText.text.toString().trim()
                val newGameInfo = infoEditText.text.toString().trim()

                if (newGameName.isNotEmpty()) {
                    gameNames.add(newGameName)
                    gameInfos.add(newGameInfo.ifEmpty { "Información adicional aún no disponible." })
                    adapter.notifyDataSetChanged()

                    // Mostrar Snackbar con opción de deshacer
                    Snackbar.make(view, "$newGameName añadido", Snackbar.LENGTH_LONG)
                        .setAction("Deshacer") {
                            val lastIndex = gameNames.size - 1
                            gameNames.removeAt(lastIndex)
                            gameInfos.removeAt(lastIndex)
                            adapter.notifyDataSetChanged()
                        }
                        .show()
                } else {
                    Snackbar.make(view, "Por favor, ingresa un nombre para el juego.", Snackbar.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .create()

        dialog.show()
    }

    // Función para mostrar un cuadro de diálogo con la información adicional del juego
    private fun showGameInfoDialog(gameName: String, gameInfo: String, position: Int) {
        val infoDialog = AlertDialog.Builder(this)
            .setTitle(gameName)
            .setMessage(gameInfo)
            .setPositiveButton("Cerrar") { dialog, _ -> dialog.dismiss() }
            .setNeutralButton("Eliminar") { dialog, _ ->
                deletedGamesNames.add(gameName)
                deletedGamesInfos.add(gameInfo)

                // Eliminar el juego de las listas principales
                gameNames.removeAt(position)
                gameInfos.removeAt(position)
                adapter.notifyDataSetChanged()

                // Mostrar un Snackbar para confirmar la eliminación
                Snackbar.make(findViewById(R.id.fab), "$gameName eliminado", Snackbar.LENGTH_LONG)
                    .setAction("Deshacer") {
                        gameNames.add(position, gameName)
                        gameInfos.add(position, gameInfo)
                        adapter.notifyDataSetChanged()

                        // Eliminar el juego de la papelera
                        deletedGamesNames.remove(gameName)
                        deletedGamesInfos.remove(gameInfo)
                    }
                    .show()

                dialog.dismiss()
            }
            .create()
        infoDialog.show()
    }

    // Función para mostrar los juegos eliminados en la papelera
    private fun showTrashDialog(view: View) {
        if (deletedGamesNames.isEmpty()) {
            Snackbar.make(view, "La papelera está vacía", Snackbar.LENGTH_SHORT).show()
            return
        }

        val trashAdapter = object : ArrayAdapter<String>(this, R.layout.list_item_layout, R.id.text_item, deletedGamesNames) {
            override fun getView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(R.id.text_item)

                textView.setTextColor(resources.getColor(android.R.color.black, null))
                return view
            }
        }

        val trashDialog = AlertDialog.Builder(this)
            .setTitle("Papelera")
            .setAdapter(trashAdapter) { _, position ->
                val gameName = deletedGamesNames[position]
                val gameInfo = deletedGamesInfos[position]

                gameNames.add(gameName)
                gameInfos.add(gameInfo)
                adapter.notifyDataSetChanged()

                deletedGamesNames.removeAt(position)
                deletedGamesInfos.removeAt(position)
                trashAdapter.notifyDataSetChanged()

                Snackbar.make(view, "$gameName restaurado", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cerrar") { dialog, _ -> dialog.dismiss() }
            .create()

        trashDialog.show()
    }
}
