package io.silv

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import io.silv.pokemon.local.PokemonDatabase
import io.silv.pokemon.network.PokemonService
import io.silv.pokemon.repository.PokemonRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DependencyGraph {

    val gson = Gson()

    lateinit var pokemonDatabase: PokemonDatabase
        private set

    private const val pokemonApiV2Url = "https://pokeapi.co/api/v2/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(pokemonApiV2Url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val pokemonService: PokemonService = retrofit.create(PokemonService::class.java)

    val pokemonRepository by lazy {
        PokemonRepository(
            api = pokemonService,
            db = pokemonDatabase
        )
    }

    fun init(context: Context) {

        pokemonDatabase = Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            "pokemon.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}