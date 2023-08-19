package io.silv.pokemon

import android.app.Application
import io.silv.pokemon.local.PokeDb

class PokemonApp: Application() {

    override fun onCreate() {
        super.onCreate()

        PokeDb.init(this)
    }
}