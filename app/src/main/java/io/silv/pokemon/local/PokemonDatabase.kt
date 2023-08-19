package io.silv.pokemon.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        PokemonResource::class,
        RemoteKey::class
    ],
    version = 1
)
abstract class PokemonDatabase: RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao

    abstract fun remoteKeysDao(): RemoteKeysDao
}