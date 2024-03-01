package data
/*
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from

object SupabaseService {

    //Account jom
    var supabaseClient: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://bknhmpxtmpputpcsxzyw.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImJrbmhtcHh0bXBwdXRwY3N4enl3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDkyMTI1NjEsImV4cCI6MjAyNDc4ODU2MX0.iP0p92RdoBEonJfmuEfHE0GT3amgEpxdL4dm_dEWFgM"
    ) {
        install(Postgrest)
    }

     var bicycles = listOf<Bicycle>()

    suspend fun storeNewBicycle(bicycle: Bicycle) {
        supabaseClient.from("bicycles").insert(bicycle)
    }

    suspend fun deleteBicycle(id: Int){
        supabaseClient.from("countries").delete {
            filter {
                eq("id", id)
            }
        }
    }

    suspend fun getData(): List<Bicycle> {
        return supabaseClient.from("bicycles")
            .select().decodeList<Bicycle>()
    }

}*/