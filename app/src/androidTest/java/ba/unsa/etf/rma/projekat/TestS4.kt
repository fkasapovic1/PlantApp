package ba.unsa.etf.rma.projekat


import android.content.Context
import android.graphics.Bitmap
import androidx.core.database.getStringOrNull
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.greaterThan
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class TestS4 {

    private val countBiljka = "SELECT COUNT(*) AS broj_biljaka FROM Biljka"
    private val countBiljkaBitmaps = "SELECT COUNT(*) AS broj_bitmapa FROM BiljkaBitmap"

    private val describeBiljka = "pragma table_info('Biljka')"
    private val describeBiljkaBitmap = "pragma table_info('BiljkaBitmap')"

    private val kolone = mapOf(
        "BiljkaBitmap" to arrayListOf("idBiljke", "bitmap"),
        "Biljka" to arrayListOf(
            "id",
            "naziv",
            "family",
            "medicinskoUpozorenje",
            "jela",
            "klimatskiTipovi",
            "zemljisniTipovi",
            "medicinskeKoristi"
        )
    )

    companion object {
        lateinit var db: SupportSQLiteDatabase
        lateinit var context: Context
        lateinit var roomDb: BiljkaDatabase
        lateinit var biljkaDAO: BiljkaDAO

        @BeforeClass
        @JvmStatic
        fun createDB() = runBlocking {
            val scenarioRule = ActivityScenario.launch(MainActivity::class.java)
            context = ApplicationProvider.getApplicationContext()
            roomDb = Room.inMemoryDatabaseBuilder(context, BiljkaDatabase::class.java).build()
            biljkaDAO = roomDb.biljkaDAO()
            biljkaDAO.getAllBiljkas()
            db = roomDb.openHelper.readableDatabase

        }
    }

    private fun executeCountAndCheck(query: String, column: String, value: Long) {
        var rezultat = db.query(query)
        rezultat.moveToFirst()
        var brojOdgovora = rezultat.getLong(0)
        MatcherAssert.assertThat(brojOdgovora, `is`(equalTo(value)))
    }

    private fun checkColumns(query: String, naziv: String) {
        var rezultat = db.query(query)
        val list = (1..rezultat.count).map {
            rezultat.moveToNext()
            rezultat.getString(1)
        }
        assertThat(list, hasItems(*kolone[naziv]!!.toArray()))
    }

    @get:Rule
    val intentsTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun a0_insertFirstBiljka() = runBlocking {
        biljkaDAO.saveBiljka(
            Biljka(
                naziv = "Bosiljak (Ocimum basilicum)",
                porodica = "Lamiaceae (usnate)",
                medicinskoUpozorenje = "Može iritati kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.",
                medicinskeKoristi = listOf(
                    MedicinskaKorist.SMIRENJE,
                    MedicinskaKorist.REGULACIJAPROBAVE
                ),
                profilOkusa = ProfilOkusaBiljke.BEZUKUSNO,
                jela = listOf("Salata od paradajza", "Punjene tikvice"),
                klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
                zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
            )
        )

        assertThat(biljkaDAO.getAllBiljkas().size, `is`(1))
    }

    @Test
    fun a1_tableBiljkaHasAllColumns() = runBlocking {
        checkColumns(describeBiljka, "Biljka")
    }

    @Test
    fun a2_tableBiljkaBitmapHasAllColumns() = runBlocking {
        checkColumns(describeBiljkaBitmap, "BiljkaBitmap")
    }

    @Test
    fun a3_insertZaistaUpisaoUBazu() = runBlocking {
        executeCountAndCheck(countBiljka, "broj_biljaka", 1)
    }

    @Test
    fun a4_dodajJosJednuBiljkuIGetAllBiljkas() = runBlocking {
        biljkaDAO.saveBiljka(
            Biljka(
                naziv = "Kamilica (Matricaria chamomilla)",
                porodica = "Asteraceae (glavočike)",
                medicinskoUpozorenje = "Može uzrokovati alergijske reakcije kod osjetljivih osoba.",
                medicinskeKoristi = listOf(
                    MedicinskaKorist.SMIRENJE,
                    MedicinskaKorist.PROTUUPALNO
                ),
                profilOkusa = ProfilOkusaBiljke.AROMATICNO,
                jela = listOf("Čaj od kamilice"),
                klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUBTROPSKA),
                zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
            )
        )
        assertThat(biljkaDAO.getAllBiljkas().size, `is`(2))
        executeCountAndCheck(countBiljka, "broj_biljaka", 2)
    }

    @Test
    fun a5_provjeriDaUSvimBiljkamaPostojiKoristSmirenje() = runBlocking {
        var biljke = biljkaDAO.getAllBiljkas()
        for (biljka in biljke) {
            assertThat(biljka.medicinskeKoristi!!.contains(MedicinskaKorist.SMIRENJE), `is`(true))
        }
    }

    @Test
    fun a6_addSlikaAndCheckItsAdded() = runBlocking {
        executeCountAndCheck(countBiljkaBitmaps, "broj_bitmapa", 0)
        var biljka1 = biljkaDAO.getAllBiljkas().get(0).id
        var bitmap = Bitmap.createBitmap(200, 300, Bitmap.Config.ARGB_8888)
        //napravite da je prvi parametar BiljkaBitmap id koji je PrimaryKey(autoGenerate=true)
        biljkaDAO.addImage(biljka1 ?: 0, bitmap)
        executeCountAndCheck(countBiljkaBitmaps, "broj_bitmapa", 1)
        var bitmapCursor = db.query("SELECT bitmap FROM BiljkaBitmap")
        bitmapCursor.moveToFirst()
        assertThat(bitmapCursor.getStringOrNull(0)?.length ?: 0, greaterThan(100))
    }

    @Test
    fun a7_deleteAllAndCheckIfItsEmpty() = runBlocking {
        biljkaDAO.clearData()
        executeCountAndCheck(countBiljkaBitmaps, "broj_bitmapa", 0)
        executeCountAndCheck(countBiljka, "broj_biljaka", 0)
    }

@Test
fun a8t1_daLiJeDodanaBiljka() = runBlocking {
    val biljka = Biljka(
        naziv = "Kruscika (Epipactis helleborine)",
        porodica = "Asteraceae (glavočike)",
        medicinskoUpozorenje = "Može iritirati kožu osjetljivu na sunce.",
        medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE),
        profilOkusa = ProfilOkusaBiljke.GORKO,
        jela = listOf("Piletina"),
        klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUBTROPSKA, KlimatskiTip.SUHA),
        zemljisniTipovi = listOf(Zemljiste.PJESKOVITO)
    )

    // Dohvati sve biljke iz baze prije dodavanja nove biljke
    val biljkePrijeDodavanja = biljkaDAO.getAllBiljkas()
    val brojBiljakaPrije = biljkePrijeDodavanja.size

    // Dodaj biljku u bazu
    biljkaDAO.saveBiljka(biljka)

    // Dohvati sve biljke iz baze nakon dodavanja nove biljke
    val biljkePoslijeDodavanja = biljkaDAO.getAllBiljkas()
    val brojBiljakaPoslije = biljkePoslijeDodavanja.size

    // Provjeri da li je broj biljaka u bazi povećan za jedan
    assertEquals(brojBiljakaPrije + 1, brojBiljakaPoslije)

    // Provjeri da li postoji biljka s istim nazivom
    val dodanaBiljka = biljkePoslijeDodavanja.find { it.naziv == biljka.naziv }
    assertNotNull(dodanaBiljka)

    // Opcionalno: Provjeri sve atribute biljke
    assertEquals(biljka.naziv, dodanaBiljka?.naziv)
    assertEquals(biljka.porodica, dodanaBiljka?.porodica)
    assertEquals(biljka.medicinskoUpozorenje, dodanaBiljka?.medicinskoUpozorenje)
    assertEquals(biljka.medicinskeKoristi, dodanaBiljka?.medicinskeKoristi)
    assertEquals(biljka.profilOkusa, dodanaBiljka?.profilOkusa)
    assertEquals(biljka.jela, dodanaBiljka?.jela)
    assertEquals(biljka.klimatskiTipovi, dodanaBiljka?.klimatskiTipovi)
    assertEquals(biljka.zemljisniTipovi, dodanaBiljka?.zemljisniTipovi)
}

    @Test
    fun a9t2_VracaLiPrazanNiz() = runBlocking {
        // Clear all data from the database
        biljkaDAO.clearData()

        // Get all biljkas from the database
        val biljkeIzBaze = biljkaDAO.getAllBiljkas()

        // Check that the list is empty
        assertTrue(biljkeIzBaze.isEmpty())
    }


}
