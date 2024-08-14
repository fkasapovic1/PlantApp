package ba.unsa.etf.rma.projekat

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.InputStream
import java.lang.IllegalStateException
import java.net.HttpURLConnection
import java.net.URL

class TrefleDAO {


    private val api_key:String = "I6zOiDk7sBvQwLuVE2PZ49x5_YzJuKX6ZFihWFqvXD4"
    private val BASE_URL:String = "http://trefle.io/api/v1"


    suspend fun getImage(biljka: Biljka): Bitmap {
        return withContext(Dispatchers.IO) {
            try {
                val latinskiNaziv = dajLatinski(biljka.naziv ?: "")

                val urlString = "$BASE_URL/plants/search?token=$api_key&q=${latinskiNaziv}"
                val url = URL(urlString)

                (url.openConnection() as? HttpURLConnection)?.run {
                    requestMethod = "GET"
                    connect()

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        inputStream.use { inputStream ->
                            val result = inputStream.bufferedReader().use { it.readText() }
                            val jsonObject = JSONObject(result)
                            val dataArray = jsonObject.getJSONArray("data")

                            if (dataArray.length() > 0) {
                                val plantObject = dataArray.getJSONObject(0)
                                val imageUrl = plantObject.getString("image_url")

                                val bitmapUrlConnection = URL(imageUrl).openConnection() as HttpURLConnection
                                bitmapUrlConnection.connect()
                                if (bitmapUrlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                                    val bitmapInputStream: InputStream = bitmapUrlConnection.inputStream
                                    return@withContext BitmapFactory.decodeStream(bitmapInputStream)
                                }
                            }
                            return@withContext defaultBitmap
                        }
                    }
                }
                return@withContext defaultBitmap
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext defaultBitmap
            }
        }
    }


/*
        suspend fun getPlantswithFlowerColor(flowerColor: String, substr: String): List<Biljka> {
            return withContext(Dispatchers.IO) {
                try {
                    //dodati petlju za page
                    val urlString = "$BASE_URL/plants?token=$api_key&filter[flower_color]=$flowerColor"
                    val url = URL(urlString)
                    val biljke = mutableListOf<Biljka>()

                    (url.openConnection() as? HttpURLConnection)?.run {
                        requestMethod = "GET"
                        connect()

                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            inputStream.use { inputStream ->
                                val result = inputStream.bufferedReader().use { it.readText() }
                                val jsonObject = JSONObject(result)
                                val dataArray = jsonObject.getJSONArray("data")

                                for (i in 0 until dataArray.length()) {
                                    val plantObject = dataArray.getJSONObject(i)
                                    val naziv = plantObject.getString("common_name") ?: ""
                                    val provjera = plantObject.getString("scientific_name") ?: ""
                                    // OVDJE MOZE ICI scientific_name,slug,common_name pa vidjeti u testovima sta se podrazumijeva
                                    if (provjera.contains(substr, true)) {
                                        val biljka = Biljka(
                                            naziv = naziv,
                                            porodica = plantObject.optString("family", ""),
                                            medicinskoUpozorenje = "",
                                            medicinskeKoristi = emptyList(),
                                            profilOkusa = ProfilOkusaBiljke.GORKO,
                                            jela = emptyList(),
                                            klimatskiTipovi = emptyList(),
                                            zemljisniTipovi = emptyList(),

                                        )
                                        biljke.add(biljka)
                                    }
                                }
                            }
                        }
                    }
                    return@withContext biljke
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@withContext emptyList<Biljka>()
                }
            }
        }
*/

    suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {
        return withContext(Dispatchers.IO) {
            val listaBiljaka = mutableListOf<Biljka>()
            try {
                //Pretraga biljki po substringu
                val searchUrl = URL("$BASE_URL/plants/search?token=$api_key&q=$substr")
                (searchUrl.openConnection() as? HttpURLConnection)?.run {
                    val searchResult = inputStream.bufferedReader().use { it.readText() }
                    val searchJo = JSONObject(searchResult)
                    val searchResults = searchJo.getJSONArray("data")

                    for (i in 0 until searchResults.length()) {
                        val plantJson = searchResults.getJSONObject(i)
                        val naziv = plantJson.getString("scientific_name")
                        val porodica = plantJson.getString("family")
                        val Id = plantJson.getString("id")

                        // Sada provjeravamo boju cvijeta za svaku biljku
                        val speciesUrl = URL("$BASE_URL/species/$Id?token=$api_key")
                        (speciesUrl.openConnection() as? HttpURLConnection)?.run {
                            val speciesResult = inputStream.bufferedReader().use { it.readText() }
                            val info = JSONObject(speciesResult).optJSONObject("data")
                            val bojeCvijeca = info?.optJSONObject("flower")?.optJSONArray("color") // mora array, ima vise boja
                            val id = plantJson.getInt("id")
                            if (bojeCvijeca != null) {
                                for (j in 0 until bojeCvijeca.length()) {
                                    if (bojeCvijeca.getString(j).equals(flowerColor, ignoreCase = true)) {
                                        //Informacije osim porodice i naziva nisu potrebne ...
                                        listaBiljaka.add(
                                                Biljka(
                                                    id=id,
                                                    naziv,
                                                    porodica,
                                                    null,
                                                    null,
                                                    null,
                                                    mutableListOf(),
                                                    mutableListOf(),
                                                    mutableListOf(),
                                                )
                                        )
                                        break
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {

                e.printStackTrace()
            }
            listaBiljaka
        }
    }


    suspend fun fixData(biljka: Biljka): Biljka {
        return withContext(Dispatchers.IO) {
            try {
                val latinskiNaziv= biljka.naziv?.let { dajLatinski(it) }
                val baseurl = "http://trefle.io/api/v1/species/"

                val urlStringara = baseurl + latinskiNaziv + "?token=" + api_key

                val url = URL(urlStringara)

                (url.openConnection() as? HttpURLConnection)?.run {
                    requestMethod = "GET"
                    connect()

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        inputStream.use { inputStream ->
                            val result = inputStream.bufferedReader().use { it.readText() }
                            val jsonObject = JSONObject(result)
                            val species = jsonObject.getJSONObject("data")
//-----------------------------------------------------------------------------
                            if (species.length() > 0) {
                                val familyName = species.optString("family")


                                if(familyName!=biljka.porodica){
                                    biljka.porodica=familyName
                                }
                                Log.d("PORODICA",familyName)


                                // Provjera jestivosti biljke
                                // ako ne mozemo naci ili je null stavicemo true ????
                                val edible = species.optBoolean("edible")
                                if (!edible) {
                                    biljka.jela = mutableListOf()
                                    if (!biljka.medicinskoUpozorenje!!.contains("NIJE JESTIVO")) {
                                        biljka.medicinskoUpozorenje += " NIJE JESTIVO"
                                    }
                                }
                                Log.d("JESTIVOST","$edible")

                                // Provjera toksičnosti biljke
                                // provjeri nullove i to....
                                val toxicity = species.optJSONObject("specifications")?.optString("toxicity")
                                // Medicinsko upozorenje - toksičnost

                                //uzimam ga ko string i poredim ga ko string
                                if (toxicity != "null" && toxicity != "none") {
                                    if (!biljka.medicinskoUpozorenje!!.contains("TOKSIČNO")) {
                                        biljka.medicinskoUpozorenje += " TOKSIČNO"
                                    }
                                }

                                Log.d("Toksicnost","$toxicity")

                                // Provjera tipa zemljišta

                               // val soilTextures = species.optJSONObject("main_species")?.optJSONObject("growth")?.getInt("soil_texture")
                                val soilTextures = species.optInt("soil_textures")


                                val validSoilTypes = mutableListOf<Zemljiste>()
                                if (soilTextures==9) {
                                    validSoilTypes.add(Zemljiste.SLJUNKOVITO)
                                }

                                if (soilTextures==10) {
                                    validSoilTypes.add(Zemljiste.KRECNJACKO)
                                }

                                if (soilTextures==1 || soilTextures == 2) {
                                    validSoilTypes.add(Zemljiste.GLINENO)
                                }

                                if (soilTextures==3 || soilTextures == 4) {
                                    validSoilTypes.add(Zemljiste.PJESKOVITO)
                                }

                                if (soilTextures==5 || soilTextures == 6) {
                                    validSoilTypes.add(Zemljiste.ILOVACA)
                                }

                                if (soilTextures==7 || soilTextures == 8) {
                                    validSoilTypes.add(Zemljiste.CRNICA)
                                }



                                /*
                                val correctedZemljisniTipovi = biljka.zemljisniTipovi?.filter { zemljiste ->
                                    validSoilTypes.any { it == zemljiste }
                                }*/

                                biljka.zemljisniTipovi=validSoilTypes
                                Log.d("Zemljiste","$soilTextures")



                                // Provjera klimatskog tipa
                                //val light = species.optInt("light")
                                val light = species.optJSONObject("growth")?.optInt("light")
                                val humidity = species.optJSONObject("growth")?.optInt("atmospheric_humidity")


                                val validClimateTypes = mutableListOf<KlimatskiTip>()

                                if (light in 6..9 && humidity in 1..5) {
                                    validClimateTypes.add(KlimatskiTip.SREDOZEMNA)
                                }

                                if (light in 8..10 && humidity in 7..10) {
                                    validClimateTypes.add(KlimatskiTip.TROPSKA)
                                }

                                if (light in 6..9 && humidity in 5..8) {
                                    validClimateTypes.add(KlimatskiTip.SUBTROPSKA)
                                }

                                if (light in 4..7 && humidity in 3..7) {
                                    validClimateTypes.add(KlimatskiTip.UMJERENA)
                                }

                                if (light in 7..9 && humidity in 1..2) {
                                    validClimateTypes.add(KlimatskiTip.SUHA)
                                }

                                if (light in 0..5 && humidity in 3..7) {
                                    validClimateTypes.add(KlimatskiTip.PLANINSKA)
                                }

                                /*
                                // ne treba
                                val correctedKlimatskiTipovi = biljka.klimatskiTipovi?.filter { klimatskiTip ->
                                    validClimateTypes.any { it == klimatskiTip }
                                }*/

                                biljka.klimatskiTipovi=validClimateTypes
                                Log.d("Light","$light")
                                Log.d("Humidity","$humidity")

                            }
                        }
                    }
                }
                return@withContext biljka
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext biljka
             /*
            Za debug..
            val correctedFamily="EXCEPTION"
            return@withContext Biljka(
                biljka.naziv,
                correctedFamily,
                biljka.medicinskoUpozorenje,
                biljka.medicinskeKoristi,
                biljka.profilOkusa,
                biljka.jela,
                biljka.klimatskiTipovi,
                biljka.zemljisniTipovi
            )
                */
            }
        }
    }





    //___________________
    //Korištenje defaultne slike
    private val defaultBitmap: Bitmap by lazy {
        BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.default_slika)
    }

    private fun dajLatinski(fullName: String): String {
        val regex = Regex("\\(([^)]+)\\)")
        val spoji = regex.find(fullName)
        val latinName = spoji?.groups?.get(1)?.value ?: fullName
        return latinName.toLowerCase().replace(" ", "-")
    }





}
