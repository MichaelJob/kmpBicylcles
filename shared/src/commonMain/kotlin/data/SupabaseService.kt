package data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage

object SupabaseService {

    private var supabaseClient: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://bknhmpxtmpputpcsxzyw.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImJrbmhtcHh0bXBwdXRwY3N4enl3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDkyMTI1NjEsImV4cCI6MjAyNDc4ODU2MX0.iP0p92RdoBEonJfmuEfHE0GT3amgEpxdL4dm_dEWFgM"
    ) {
        install(Postgrest)
    }

    suspend fun storeNewBicycle(bicycle: Bicycle) {
        supabaseClient.from("bicycles").upsert(value = bicycle)
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
        return supabaseClient
            .from("bicycles")
            .select()
            .decodeList<Bicycle>()
    }

    suspend fun uploadImage(imagePath: String, image: ByteArray) {
        supabaseClient.storage
            .from("bicycles-imgs")
            .upload(path = imagePath, data = image, upsert = false)
    }

    suspend fun downloadImage(imagePath: String) : ByteArray {
        return supabaseClient.storage
            .from("bicycles-imgs")
            .downloadAuthenticated(path = imagePath)
    }



}