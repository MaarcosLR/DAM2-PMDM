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

    // Declaramos las variables para los componentes de la UI
    private lateinit var simpleList: ListView // Lista donde se mostrarán los juegos
    private lateinit var adapter: ArrayAdapter<String> // Adaptador que vincula la lista con los datos
    private lateinit var fab: FloatingActionButton // Botón flotante para agregar juegos
    private lateinit var trashFab: FloatingActionButton // Botón flotante para ver los juegos eliminados (papelera)
    private lateinit var backFab: FloatingActionButton // Botón flotante para volver atrás

    // Listas para almacenar los juegos y sus descripciones
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

    // Listas para almacenar los juegos eliminados (papelera)
    private val deletedGamesNames = mutableListOf<String>()
    private val deletedGamesInfos = mutableListOf<String>()

    // Método que se ejecuta cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segunda_pantalla) // Establece el layout para esta actividad

        // Inicializar los componentes de la UI
        initComponents()

        // Configurar los botones flotantes para sus funcionalidades
        fab.setOnClickListener { view ->
            showAddGameDialog(view)  // Muestra un cuadro de diálogo para agregar un nuevo juego
        }

        trashFab.setOnClickListener { view ->
            showTrashDialog(view)  // Muestra los juegos eliminados (papelera)
        }

        backFab.setOnClickListener {
            onBackPressed()  // Vuelve a la actividad anterior
        }

        // Configura el evento al hacer clic en un juego de la lista
        simpleList.setOnItemClickListener { _, _, position, _ ->
            val selectedGame = gameNames[position] // Obtiene el nombre del juego seleccionado
            val gameInfo = gameInfos[position] // Obtiene la información del juego
            showGameInfoDialog(selectedGame, gameInfo, position) // Muestra información del juego en un cuadro de diálogo
        }
    }

    // Método para inicializar los componentes de la UI
    private fun initComponents() {
        // Asocia las vistas con las variables
        simpleList = findViewById(R.id.simple_list) // Lista de juegos
        fab = findViewById(R.id.fab) // Botón flotante para agregar juegos
        trashFab = findViewById(R.id.fab_trash) // Botón flotante para la papelera
        backFab = findViewById(R.id.fab_home) // Botón flotante para volver atrás

        // Configura el adaptador para la lista de juegos
        adapter = object : ArrayAdapter<String>(this, R.layout.list_item_layout, R.id.text_item, gameNames) {
            // Modifica la apariencia de los elementos de la lista
            override fun getView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(R.id.text_item) // Encuentra el TextView donde se muestra el nombre del juego

                // Establece el color de texto a blanco para los juegos en la lista principal
                textView.setTextColor(resources.getColor(android.R.color.white, null))

                return view
            }
        }
        simpleList.adapter = adapter // Establece el adaptador a la lista
    }

    // Muestra el cuadro de diálogo para agregar un nuevo juego
    private fun showAddGameDialog(view: View) {
        val layout = LayoutInflater.from(this).inflate(R.layout.dialog_add_game, null) // Infla el layout del cuadro de diálogo
        val nameEditText = layout.findViewById<EditText>(R.id.edit_game_name) // Campo para el nombre del juego
        val infoEditText = layout.findViewById<EditText>(R.id.edit_game_info) // Campo para la información del juego

        val dialog = AlertDialog.Builder(this)
            .setTitle("Agregar nuevo juego") // Título del cuadro de diálogo
            .setView(layout) // Establece el layout del cuadro de diálogo
            .setPositiveButton("Agregar") { dialog, _ -> // Botón positivo para agregar el juego
                val newGameName = nameEditText.text.toString().trim() // Obtiene el nombre del juego ingresado
                val newGameInfo = infoEditText.text.toString().trim() // Obtiene la información del juego

                if (newGameName.isNotEmpty()) { // Verifica que el nombre no esté vacío
                    // Agrega el nuevo juego a las listas
                    gameNames.add(newGameName)
                    gameInfos.add(newGameInfo.ifEmpty { "Información adicional aún no disponible." }) // Si la información está vacía, agrega un valor predeterminado
                    adapter.notifyDataSetChanged() // Notifica al adaptador para actualizar la lista

                    // Muestra un Snackbar para confirmar la acción y permitir deshacer
                    Snackbar.make(view, "$newGameName añadido", Snackbar.LENGTH_LONG)
                        .setAction("Deshacer") {
                            // Si se pulsa "Deshacer", elimina el juego recién agregado
                            val lastIndex = gameNames.size - 1
                            gameNames.removeAt(lastIndex)
                            gameInfos.removeAt(lastIndex)
                            adapter.notifyDataSetChanged()
                        }
                        .show()
                } else {
                    Snackbar.make(view, "Por favor, ingresa un nombre para el juego.", Snackbar.LENGTH_SHORT).show() // Muestra un mensaje si el nombre está vacío
                }
                dialog.dismiss() // Cierra el cuadro de diálogo
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() } // Botón negativo para cancelar
            .create()

        dialog.show() // Muestra el cuadro de diálogo
    }

    // Muestra un cuadro de diálogo con la información de un juego
    private fun showGameInfoDialog(gameName: String, gameInfo: String, position: Int) {
        val infoDialog = AlertDialog.Builder(this)
            .setTitle(gameName) // Título del cuadro de diálogo con el nombre del juego
            .setMessage(gameInfo) // Muestra la información del juego
            .setPositiveButton("Cerrar") { dialog, _ -> dialog.dismiss() } // Botón para cerrar el cuadro de diálogo
            .setNeutralButton("Eliminar") { dialog, _ -> // Botón para eliminar el juego
                deletedGamesNames.add(gameName) // Agrega el juego a la lista de eliminados
                deletedGamesInfos.add(gameInfo) // Agrega la información del juego a la lista de eliminados

                // Elimina el juego de las listas principales
                gameNames.removeAt(position)
                gameInfos.removeAt(position)
                adapter.notifyDataSetChanged() // Actualiza la lista

                // Muestra un Snackbar con opción de deshacer
                Snackbar.make(findViewById(R.id.fab), "$gameName eliminado", Snackbar.LENGTH_LONG)
                    .setAction("Deshacer") {
                        // Si se pulsa "Deshacer", restaura el juego eliminado
                        gameNames.add(position, gameName)
                        gameInfos.add(position, gameInfo)
                        adapter.notifyDataSetChanged()

                        // Elimina el juego de la papelera
                        deletedGamesNames.remove(gameName)
                        deletedGamesInfos.remove(gameInfo)
                    }
                    .show()

                dialog.dismiss() // Cierra el cuadro de diálogo
            }
            .create()
        infoDialog.show() // Muestra el cuadro de diálogo
    }

    // Muestra los juegos eliminados en la papelera
    private fun showTrashDialog(view: View) {
        if (deletedGamesNames.isEmpty()) { // Verifica si la papelera está vacía
            Snackbar.make(view, "La papelera está vacía", Snackbar.LENGTH_SHORT).show() // Muestra un mensaje si no hay juegos eliminados
            return
        }

        // Crea un adaptador para mostrar los juegos eliminados en la papelera
        val trashAdapter = object : ArrayAdapter<String>(this, R.layout.list_item_layout, R.id.text_item, deletedGamesNames) {
            override fun getView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(R.id.text_item)

                // Establece el color de texto negro para los juegos eliminados
                textView.setTextColor(resources.getColor(android.R.color.black, null))
                return view
            }
        }

        // Muestra el cuadro de diálogo con los juegos eliminados
        val trashDialog = AlertDialog.Builder(this)
            .setTitle("Papelera") // Título del cuadro de diálogo
            .setAdapter(trashAdapter) { _, position -> // Acción al seleccionar un juego de la papelera
                val gameName = deletedGamesNames[position]
                val gameInfo = deletedGamesInfos[position]

                // Restaura el juego a las listas principales
                gameNames.add(gameName)
                gameInfos.add(gameInfo)
                adapter.notifyDataSetChanged()

                // Elimina el juego de la papelera
                deletedGamesNames.removeAt(position)
                deletedGamesInfos.removeAt(position)
                trashAdapter.notifyDataSetChanged()

                Snackbar.make(view, "$gameName restaurado", Snackbar.LENGTH_SHORT).show() // Muestra un mensaje de restauración
            }
            .setNegativeButton("Cerrar") { dialog, _ -> dialog.dismiss() } // Botón para cerrar el cuadro de diálogo
            .create()

        trashDialog.show() // Muestra el cuadro de diálogo
    }
}
