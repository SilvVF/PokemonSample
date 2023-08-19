package io.silv.pokemon.local

import android.content.Context
import androidx.room.Room
import io.silv.pokemon.local.PokeDb.getInstance
import io.silv.pokemon.local.PokeDb.init

/**
 * Holds the reference to the [PokemonDatabase].
 *
 * @property init() is called on app start in [io.silv.pokemon.PokemonApp]
 * inside the onCreate() method to pass the context in and create the database instance
 *
 * @property getInstance returns the database instance as non null. this should be called in a
 * lazy block so the call is not made until it needs to be allowing init to create the db first
 */
object PokeDb {

    private var instance: PokemonDatabase? = null

    fun init(context: Context) {
        instance = Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            "pokemon.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    fun getInstance(): PokemonDatabase {
        return requireNotNull(instance)
    }
}