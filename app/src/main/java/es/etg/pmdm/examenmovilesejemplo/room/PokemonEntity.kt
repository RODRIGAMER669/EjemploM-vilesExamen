package es.etg.pmdm.examenmovilesejemplo.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    var name:String = "",
    var url:String = ""
)