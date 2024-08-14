package ba.unsa.etf.rma.projekat

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/*
object BiljkaRepository {
    fun getBiljke():List<Biljka>{
        return biljkice()
    }
}*/
object BiljkaRepository {
    private val biljkeList: MutableList<Biljka> = mutableListOf()

    init {
        biljkeList.addAll(biljkice())
    }

    fun getBiljke(): List<Biljka> {
        return biljkeList.toList()
    }

    fun dodajBiljku(biljka: Biljka) {
        biljkeList.add(biljka)
    }

    //Funkcija koja dohvata biljke iz baze
    suspend fun dajSveBiljke(context: Context) : List<Biljka> {
        return withContext(Dispatchers.IO) {
            var db = BiljkaDatabase.getInstance(context)
            var biljke = db!!.biljkaDAO().getAllBiljkas()
            return@withContext biljke
        }
    }
    //Funkcija koja dodaje novu Biljku
    suspend fun dodajNovuBiljku(context: Context, biljka: Biljka) {
        withContext(Dispatchers.IO) {
            val db = BiljkaDatabase.getInstance(context)
            db.biljkaDAO().saveBiljka(biljka)
        }
    }

    // Funkcija koja dodaje sliku biljci
    suspend fun dodajSliku(context: Context, idBiljke: Int, bitmap: Bitmap): Boolean {
        return withContext(Dispatchers.IO) {
            val db = BiljkaDatabase.getInstance(context)
            return@withContext db.biljkaDAO().addImage(idBiljke, bitmap)
        }
    }





}