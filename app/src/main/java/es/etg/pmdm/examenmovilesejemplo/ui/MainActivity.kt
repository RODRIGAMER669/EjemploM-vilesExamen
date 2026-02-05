package es.etg.pmdm.examenmovilesejemplo.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import es.etg.pmdm.examenmovilesejemplo.databinding.ActivityMainBinding
import es.etg.pmdm.examenmovilesejemplo.room.PokemonDataBase
import es.etg.pmdm.examenmovilesejemplo.room.PokemonEntity
import es.etg.pmdm.examenmovilesejemplo.ui.viewModel.DataAdapter
import es.etg.pmdm.examenmovilesejemplo.ui.viewModel.ItemViewModel
import es.etg.pmdm.examenmovilesejemplo.ui.viewModel.TituloFragment
import es.etg.pmdm.examenmovilesejemplo.service.PokemonAPIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DataAdapter

    companion object {
        const val BASE_URL: String = "https://pokeapi.co/api/v2/"
        const val URL: String = "pokemon?limit=100"

        const val DATABASE_NAME = "pokemon"
        lateinit var database: PokemonDataBase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(binding.fgTitulo.id, TituloFragment())
            }
        }

        MainActivity.database =  Room.databaseBuilder(this,
            PokemonDataBase::class.java,
            DATABASE_NAME).build()
    }

    fun mostrar(view : View){
        // Inicializamos el adapter con una lista vacía
        val data = ArrayList<ItemViewModel>()
        adapter = DataAdapter(data)

        // Configuramos el RecyclerView
        val recyclerview = binding.recyclerView
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter

        // Llamamos a la API para obtener los datos
        consultarPokemon()
    }

    private fun consultarPokemon() {
        CoroutineScope(Dispatchers.IO).launch {
            // Consulto los Pokémon
            val call = getRetrofit().create(PokemonAPIService::class.java).getPokemon(URL)
            // A la respuesta le pido que me de el PokemonResponse
            val pokemonResponse = call.body()

            // Este código se ejecuta en el thread principal (ui)
            runOnUiThread {
                // Se ejecutó correctamente el servicio
                if (call.isSuccessful) {
                    // Verificamos que la respuesta no sea null
                    pokemonResponse?.let { response ->
                        // Convertimos los resultados a ItemViewModel
                        val pokemonList = response.results.map { pokemonResult ->
                            ItemViewModel(pokemonResult.name, pokemonResult.url)
                        }

                        // Actualizamos el adapter con los datos obtenidos
                        adapter.updateData(pokemonList)

                        // Guardar pokemon al base de datos
                        guardarBD(pokemonList)
                    }
                } else {
                    cargarBD()
                }
            }
        }
    }

    private fun getRetrofit(): retrofit2.Retrofit {
        return retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
    }

    private fun guardarBD(pokemonList: List<ItemViewModel>){
        CoroutineScope(Dispatchers.IO).launch {
            val pokemonDao = database.pokemonDao()

            // Convertir ItemViewModel a PokemonEntity
            pokemonList.forEach { item ->
                val pokemonEntity = PokemonEntity(
                    id = 0, // 0 para auto-generar
                    name = item.nombre,
                    url = item.url
                )
                pokemonDao.insert(pokemonEntity)
            }
        }
    }

    private fun cargarBD(){
        CoroutineScope(Dispatchers.IO).launch {
            val pokemonDao = database.pokemonDao()
            val pokemons = pokemonDao.getAll()

            runOnUiThread {
                // Convertir a ItemViewModel y actualizar el adapter
                val itemList = pokemons.map {
                    ItemViewModel(it.name, it.url)
                }
                adapter.updateData(itemList)
            }
        }
    }
}