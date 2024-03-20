package data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import io.ktor.http.HttpHeaders


object SupabaseService {

    private var supabaseClient: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://bknhmpxtmpputpcsxzyw.supabase.co", //FIXME: use env variables, change key after implementation, debug
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImJrbmhtcHh0bXBwdXRwY3N4enl3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDkyMTI1NjEsImV4cCI6MjAyNDc4ODU2MX0.iP0p92RdoBEonJfmuEfHE0GT3amgEpxdL4dm_dEWFgM"
    ) {
        HttpHeaders.Authorization
        install(Postgrest)
        install(Storage)
        install(Auth)
    }

    suspend fun signUpNewUser(mail: String, pw: String) {
        supabaseClient.auth.signUpWith(Email) {
            email = mail
            password = pw
        }
    }

    suspend fun signInWithEmail(mail: String, pw: String): String {
        try {
            supabaseClient.auth.signInWith(Email) {
                email = mail
                password = pw
            }
        } catch (ex : BadRequestRestException){
          return "Error: ${ex.message}"
        }
        return ""
    }

    suspend fun logout() {
        supabaseClient.auth.signOut()
    }

    suspend fun storeNewBicycle(bicycle: Bicycle): Bicycle {
        supabaseClient.from("bicycles").upsert(value = bicycle)
        return supabaseClient.from("bicycles").select{
            filter {
                eq("id", bicycle.id)
            }
        }.decodeList<Bicycle>().first()
    }

    suspend fun deleteBicycle(id: Int){
        supabaseClient
            .from("bicycles")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }

    suspend fun getData(): List<Bicycle> {
        return try {
            supabaseClient
                .from("bicycles")
                .select()
                .decodeList<Bicycle>()
        } catch (e: Exception) {
            listOf(Bicycle(id=0,"failed to load bicycles"))
        }
    }

    suspend fun uploadImage(imagePath: String, image: ByteArray) {
        supabaseClient.storage
            .from("bicycles-imgs")
            .upload(path = imagePath, data = image, upsert = false)
    }

    suspend fun downloadImage(imagePath: String) : String {
        val result =  try {
            supabaseClient.storage
                .from("bicycles-imgs").authenticatedUrl(path = imagePath)
        } catch (e: Exception) {
            println("XXXXXX - Jobfails: "+e.message)
            //if supabase fails to load the image, return a default image
            return "https://www.michaeljob.ch/bicycles/defaultbicycle.jpg"
        }
        println("XXXXXXXXXXXXXXXXXXX - Result: $result")
        return result
    }



}