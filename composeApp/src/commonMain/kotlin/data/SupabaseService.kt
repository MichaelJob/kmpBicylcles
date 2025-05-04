package data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import io.ktor.http.HttpHeaders


object SupabaseService {

    private const val BUCKET_ID = "bicyclesimgs"
    private const val TABLE_NAME = "bicycles"

    private var supabaseClient: SupabaseClient = createSupabaseClient(
        //FIXME: use env variables, change key after implementation, debug
        supabaseUrl = "https://zhahkgaeexekntixwjoa.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InpoYWhrZ2FlZXhla250aXh3am9hIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDIwMjk0MDUsImV4cCI6MjA1NzYwNTQwNX0.r4Wk-1HjQmm0w3Tig-SpdxlD5NfHA5yoPpYspa3p7bE"
    ) {
        HttpHeaders.Authorization
        install(Postgrest)
        install(Storage)
        install(Auth)
    }

    suspend fun signUpNewUser(mail: String, pw: String): String {
        supabaseClient.auth.signUpWith(Email) {
            email = mail
            password = pw
        }
        return getSignedInUserId()
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
        return getSignedInUserId()
    }

    private fun getSignedInUserId(): String{
        return supabaseClient.auth.currentUserOrNull()?.id ?: ""
    }

    suspend fun logout() {
        supabaseClient.auth.signOut()
    }

    suspend fun storeNewBicycle(bicycle: Bicycle): Bicycle {
        supabaseClient.from(table = TABLE_NAME).upsert(value = bicycle)
        return supabaseClient.from(table = TABLE_NAME).select{
            filter {
                eq("id", bicycle.id)
            }
        }.decodeList<Bicycle>().first()
    }

    suspend fun deleteBicycle(id: Int){
        supabaseClient
            .from(table = TABLE_NAME)
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }

    suspend fun getData(useruid: String): List<Bicycle> {
        return try {
            supabaseClient
                .from(table = TABLE_NAME)
                .select() {
                    filter {
                        eq("useruid", useruid)
                    }
                    order(column = "year", order = Order.ASCENDING)
                }
                .decodeList<Bicycle>()
        } catch (e: Exception) {
            listOf(Bicycle(id=0, bikename = "failed to load bicycles"))
        }
    }

    suspend fun uploadImage(imagePath: String, image: ByteArray) {
        val bucket = supabaseClient.storage.from(bucketId = BUCKET_ID)
        try {
            if(!bucket.exists(imagePath)){
                bucket.upload(imagePath, image)
            }
        } catch (e: Exception){
            println(e.message)
        }
    }

    suspend fun downloadImage(imagePath: String) : ByteArray? {
        return try {
            supabaseClient.storage
                .from(bucketId = BUCKET_ID)
                .downloadAuthenticated(imagePath)
        } catch (e: Exception) {
            //if supabase fails to load the image, return null
            println("e: ${e.message}")
            null
        }
    }

    suspend fun deleteImage(imagePath: String){
        try {
            supabaseClient.storage
                .from(bucketId = BUCKET_ID)
                .delete(imagePath)
        } catch (e: Exception) {
            //if supabase fails to delete the image
            println("e: ${e.message}")
        }
    }

}