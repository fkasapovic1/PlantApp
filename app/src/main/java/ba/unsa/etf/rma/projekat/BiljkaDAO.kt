package ba.unsa.etf.rma.projekat

import android.graphics.Bitmap
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ba.unsa.etf.rma.projekat.TrefleDAO  // Adjust package path accordingly


@Dao
interface BiljkaDAO {

    //Funkcija 1
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBiljka(biljka: Biljka): Long

    @Transaction
    fun saveBiljka(biljka: Biljka): Boolean {
        val id = insertBiljka(biljka)
        return id != -1L
    }

    //Funkcija 2
    @Query("SELECT * FROM Biljka WHERE onlineChecked = 0")
    suspend fun getOfflineBiljke(): List<Biljka>

    @Update
    suspend fun updateBiljka(biljka: Biljka)

   /* @Transaction
    @Query("UPDATE Biljka SET naziv = :noviNaziv, family = :novaPorodica, medicinskoUpozorenje = :novoUpozorenje, jela = :novaJela, klimatskiTipovi = :noviKlimatskiTipovi, zemljisniTipovi = :noviZemljisniTipovi WHERE id = :biljkaId")
    suspend fun fixOfflineBiljka(
        biljkaId: Int,
        noviNaziv: String,
        novaPorodica: String,
        novoUpozorenje: String,
        novaJela: List<String>,
        noviKlimatskiTipovi: List<String>,
        noviZemljisniTipovi: List<String>
    ): Int*/

    @Transaction
    @Query("UPDATE Biljka SET onlineChecked = 1 WHERE id = :biljkaId")
    suspend fun fixOfflineBiljka(biljkaId: Int): Int

    @Transaction
    suspend fun fixOfflineBiljke(): Int {
        val offlineBiljke = getOfflineBiljke()
        var updatedCount = 0

        for (biljka in offlineBiljke) {
            val updatedBiljka = TrefleDAO().fixData(biljka)
            if (updatedBiljka != null) {
                updateBiljka(updatedBiljka)
                updatedCount++
            }
        }
        return updatedCount
    }

    //Funkcija 3
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveBiljkaBitmap(biljkaBitmap: BiljkaBitmap): Long

    @Query("SELECT * FROM BiljkaBitmap WHERE idBiljke = :idBiljke")
    fun getBiljkaBitmapById(idBiljke: Int): BiljkaBitmap?

    @Query("DELETE FROM BiljkaBitmap WHERE idBiljke = :idBiljke")
    fun deleteBiljkaBitmapById(idBiljke: Int)

    @Query("SELECT * FROM Biljka WHERE id = :idBiljke LIMIT 1")
    fun getBiljkaById(idBiljke: Int): Biljka?

    @Transaction
    fun addImage(idBiljke: Int, bitmap: Bitmap): Boolean {
        val existingBitmap = getBiljkaBitmapById(idBiljke)
        if (existingBitmap != null) return false
        val biljkaBitmap = BiljkaBitmap(idBiljke = idBiljke, bitmap = bitmap)
        saveBiljkaBitmap(biljkaBitmap)
        return true
    }

    //Funkcija 4
    @Query("SELECT * FROM Biljka")
    suspend fun getAllBiljkas(): List<Biljka>     // Mogu dati ime tabeli biljke npr

    //Funkcija 5
    @Query("DELETE FROM Biljka")
    suspend fun clearData()

}