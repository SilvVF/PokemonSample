package io.silv.pokemon.local

import android.content.Context
import androidx.room.Room

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