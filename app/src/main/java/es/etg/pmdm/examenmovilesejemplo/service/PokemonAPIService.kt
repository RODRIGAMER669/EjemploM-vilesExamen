package es.etg.pmdm.examenmovilesejemplo.service

import es.etg.pmdm.examenmovilesejemplo.service.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface PokemonAPIService {
    @GET
    suspend fun getPokemon(@Url url:String): Response<PokemonResponse>
}