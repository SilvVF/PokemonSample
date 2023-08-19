package io.silv.pokemon

import android.app.Application
import io.silv.DependencyGraph

class PokemonApp: Application() {

    override fun onCreate() {
        super.onCreate()

        // create the room db instance
        DependencyGraph.init(this)
    }
}