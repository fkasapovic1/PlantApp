package ba.unsa.etf.rma.projekat

import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class NovaBiljkaActivity : AppCompatActivity() {

    private lateinit var jeloEditText: EditText
    private lateinit var jelaAdapter: ArrayAdapter<String>
    private lateinit var jelaListView: ListView
    private lateinit var dodajJeloBtn: Button
    private lateinit var dodajBiljka:Button
    private lateinit var uslikajBtn:Button
    private val jelaStringList = mutableListOf<String>()
    private var odabranoJeloIndex: Int = -1
    private lateinit var nazivEditText: EditText
    private lateinit var porodicaEditText: EditText
    private lateinit var upozorenjeEditText: EditText
    private lateinit var profilOkusaListView: ListView
    private lateinit var zemljisniTipListView: ListView
    private lateinit var klimatskiTipListView: ListView
    private lateinit var medicinskaKoristListView: ListView
    private lateinit var slikaIV:ImageView
    val REQUEST_IMAGE_CAPTURE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nova_biljka)

        nazivEditText = findViewById(R.id.nazivET)
        porodicaEditText = findViewById(R.id.porodicaET)
        upozorenjeEditText = findViewById(R.id.medicinskoUpozorenjeET)
        jeloEditText = findViewById(R.id.jeloET)

        //1. Postavljanje ListView-a

        medicinskaKoristListView = findViewById(R.id.medicinskaKoristLV)
        val medicinskaKoristValues = MedicinskaKorist.values()
        val medicinskaKoristStringList = medicinskaKoristValues.map { it.opis }
        val medAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, medicinskaKoristStringList)
        medicinskaKoristListView.adapter = medAdapter

        klimatskiTipListView = findViewById(R.id.klimatskiTipLV)
        val klimatskiTipValues= KlimatskiTip.values()
        val klimatskiTipStringList=klimatskiTipValues.map{it.opis}
        val klimaAdapter=ArrayAdapter(this,android.R.layout.simple_list_item_multiple_choice,klimatskiTipStringList)
        klimatskiTipListView.adapter=klimaAdapter

        zemljisniTipListView = findViewById(R.id.zemljisniTipLV)
        val zemljisniTipValues= Zemljiste.values()
        val zemljisniTipStringList=zemljisniTipValues.map{it.naziv}
        val zemljisteAdapter=ArrayAdapter(this,android.R.layout.simple_list_item_multiple_choice,zemljisniTipStringList)
        zemljisniTipListView.adapter=zemljisteAdapter

        profilOkusaListView = findViewById(R.id.profilOkusaLV)
        val profilOkusaValues= ProfilOkusaBiljke.values()
        val profilOkusaStringList=profilOkusaValues.map{it.opis}
        val okusAdapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,profilOkusaStringList)
        profilOkusaListView.adapter=okusAdapter

        jeloEditText=findViewById(R.id.jeloET)
        jelaListView = findViewById(R.id.jelaLV)
        jelaAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice,jelaStringList)
        jelaListView.adapter=jelaAdapter


        //Listener1
        dodajJeloBtn = findViewById(R.id.dodajJeloBtn)
        dodajJeloBtn.setOnClickListener{
            dodajIliIzmijeniJelo()
        }

        //Listener2
        jelaListView.setOnItemClickListener { parent, view, position, id ->
            // Dohvati odabrano jelo i postavi tekst u EditText
            val odabranoJelo = jelaStringList[position]
            jeloEditText.setText(odabranoJelo)
            // Postavi indeks odabranog jela
            odabranoJeloIndex = position
            // Promijeni tekst na dugmetu
            dodajJeloBtn.text = "Izmijeni jelo"
        }

        //Listener3
        val uslikajBiljkuBtn = findViewById<Button>(R.id.uslikajBiljkuBtn)
        slikaIV = findViewById<ImageView>(R.id.slikaIV)
        uslikajBiljkuBtn.setOnClickListener {
            uslikajSliku()
        }


        //Listener4
        dodajBiljka= findViewById(R.id.dodajBiljkuBtn)
        dodajBiljka.setOnClickListener{
            dodajBiljku()
        }
    }

    //FUNKCIJA 1
    private fun dodajIliIzmijeniJelo() {
        val novoJelo = jeloEditText.text.toString()
        if (novoJelo.isNotEmpty()) {
            if (odabranoJeloIndex != -1) {
                // Ako je odabrano postojeće jelo, izmijeniti ga
                jelaStringList[odabranoJeloIndex] = novoJelo
                jelaAdapter.notifyDataSetChanged()
                // Postavi EditText na prazan tekst i dugme na Dodaj jelo
                jeloEditText.text.clear()
                dodajJeloBtn.text = "Dodaj jelo"
                odabranoJeloIndex = -1
            } else {
                jelaStringList.add(novoJelo)
                jelaAdapter.notifyDataSetChanged()
                jeloEditText.text.clear()
            }
        } else {
            if (odabranoJeloIndex != -1) {
                jelaStringList.removeAt(odabranoJeloIndex)
                jelaAdapter.notifyDataSetChanged()
                jeloEditText.text.clear()
                dodajJeloBtn.text = "Dodaj jelo"
                odabranoJeloIndex = -1
            }
        }
    }

    //FUNKCIJA 2
    private fun uslikajSliku() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)

        //NAPOMENA: Kamera radi regularno no međutim finishActivity() nakon 4s je dodan zbog testova
        //          koji moraju biti automatizovani, u imageView se dodaje default_slika
        //          Izbrisati // 150 linija, i zakomentirati try -> catch
        /*
         try {
             startActivityForResult(takePictureIntent, 100)
             Handler().postDelayed({
                 finishActivity(100)
             }, 4000)
         } catch (e: ActivityNotFoundException) {
         }
         */
        //slikaIV.setImageResource(R.drawable.default_slika)
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            slikaIV.setImageBitmap(imageBitmap)
        }
    }

    //FUNKCIJA 3
    private fun dodajBiljku(){
        // Validacija dužine teksta u EditText poljima
        val naziv = nazivEditText.text.toString()
        val porodica = porodicaEditText.text.toString()
        val upozorenje = upozorenjeEditText.text.toString()
        val jelo=jeloEditText.text.toString()


        // Provjera dužine teksta u EditText poljima
        val duzinaNaziva = naziv.length
        val duzinaPorodice = porodica.length
        val duzinaUpozorenja = upozorenje.length
        val duzinaJela=jelo.length

        var isValid = true // boolean za validaciju

        // Validacija dužine teksta u EditText poljima
        // Naziv (Latinski naziv)
        val nazivPattern1 = Regex("^[a-zA-Z]+\\s*\\([a-zA-Z]+\\)\$")
        val nazivPattern2 = Regex("^[A-Z][a-zA-Z]+\\s*\\([A-Z][a-zA-Z]+\\s+[a-zA-Z]+\\)$")
        if (duzinaNaziva < 3 || duzinaNaziva > 40) {
            nazivEditText.setError("Naziv mora biti duži od 2 i kraći od 40 znakova")
            isValid = false
        }else if (!nazivPattern1.matches(naziv) && !nazivPattern2.matches(naziv)) {
            nazivEditText.setError("Naziv mora biti u formatu imeBiljke (Latinski naziv)")
            isValid = false
        }
        if (duzinaPorodice < 3 || duzinaPorodice > 20) {
            porodicaEditText.setError("Porodica mora biti duža od 2 i kraća od 20 znakova")
            isValid = false
        }
        if (duzinaUpozorenja < 3 || duzinaUpozorenja > 20) {
            upozorenjeEditText.setError("Upozorenje mora biti duže od 2 i kraće od 20 znakova")
            isValid = false
        }

        if (((duzinaJela>0 && duzinaJela < 3) || duzinaJela > 20) && !jelaStringList.isEmpty()){
            jeloEditText.setError("Jelo mora biti duže od 2 i kraće od 20 znakova")
        }

        // Validacija unosa jela
        if (jelaStringList.isEmpty()) {
            jeloEditText.setError("Morate dodati barem jedno jelo")
            isValid = false
        }
        // clear color poslije klika
        clearInvalidSelections(klimatskiTipListView)
        clearInvalidSelections(medicinskaKoristListView)
        clearInvalidSelections(zemljisniTipListView)
        clearInvalidSelections(profilOkusaListView)

        // Validacija odabira vrijednosti u ListView-ovima
        if (medicinskaKoristListView.checkedItemCount == 0) {
            Toast.makeText(this, "Odaberite barem jednu medicinsku korist", Toast.LENGTH_SHORT).show()
            markInvalidSelections(medicinskaKoristListView)
            isValid = false
        }
        if (klimatskiTipListView.checkedItemCount == 0) {
            Toast.makeText(this, "Odaberite barem jedan klimatski tip", Toast.LENGTH_SHORT).show()
            markInvalidSelections(klimatskiTipListView)
            isValid = false
        }
        if (zemljisniTipListView.checkedItemCount == 0) {
            Toast.makeText(this, "Odaberite barem jedan tip zemljišta", Toast.LENGTH_SHORT).show()
            markInvalidSelections(zemljisniTipListView)
            isValid = false
        }
        if (profilOkusaListView.checkedItemCount == 0) {
            Toast.makeText(this, "Odaberite profil okusa biljke", Toast.LENGTH_SHORT).show()
            markInvalidSelections(profilOkusaListView)
            isValid = false
        }

        // Provjera duplikata jela
        val uniqueJela = jelaStringList.map { it.lowercase() }.toSet()
        if (jelaStringList.size != uniqueJela.size) {
            Toast.makeText(this, "Ne možete dodati dva ista jela", Toast.LENGTH_SHORT).show()
            markInvalidSelections(jelaListView)
            isValid = false
        }


        // Dohvati odabrane vrijednosti iz ListView-ova
        val odabraneMedicinskeKoristi = mutableListOf<MedicinskaKorist>()
        val selectedMedicinskeKoristiPositions = medicinskaKoristListView.checkedItemPositions
        for (i in 0 until selectedMedicinskeKoristiPositions.size()) {
            if (selectedMedicinskeKoristiPositions.valueAt(i)) {
                val korist = MedicinskaKorist.values()[selectedMedicinskeKoristiPositions.keyAt(i)]
                odabraneMedicinskeKoristi.add(korist)
            }
        }

        val odabraniJela = mutableListOf<String>()
        for (i in 0 until jelaStringList.size) {
            odabraniJela.add(jelaListView.getItemAtPosition(i).toString())
        }

        val odabraniKlimatskiTipovi = mutableListOf<KlimatskiTip>()
        val selectedKlimatskiTipoviPositions = klimatskiTipListView.checkedItemPositions
        for (i in 0 until selectedKlimatskiTipoviPositions.size()) {
            if (selectedKlimatskiTipoviPositions.valueAt(i)) {
                val tip = KlimatskiTip.values()[selectedKlimatskiTipoviPositions.keyAt(i)]
                odabraniKlimatskiTipovi.add(tip)
            }
        }

        val odabraniZemljisniTipovi = mutableListOf<Zemljiste>()
        val selectedZemljisniTipoviPositions = zemljisniTipListView.checkedItemPositions
        for (i in 0 until selectedZemljisniTipoviPositions.size()) {
            if (selectedZemljisniTipoviPositions.valueAt(i)) {
                val tip = Zemljiste.values()[selectedZemljisniTipoviPositions.keyAt(i)]
                odabraniZemljisniTipovi.add(tip)
            }
        }

        val odabraniProfilOkusaPosition = profilOkusaListView.checkedItemPosition
        if (odabraniProfilOkusaPosition == ListView.INVALID_POSITION) {
            Toast.makeText(this, "Molimo odaberite profil okusa.", Toast.LENGTH_SHORT).show()
            return
        }
        val odabraniProfilOkusa = ProfilOkusaBiljke.entries[odabraniProfilOkusaPosition]

        if (isValid) {
            // Kreiranje nove biljke
            val novaBiljka = Biljka(
                naziv = naziv,
                porodica = porodica,
                medicinskoUpozorenje = upozorenje,
                medicinskeKoristi = odabraneMedicinskeKoristi,
                profilOkusa = odabraniProfilOkusa,
                jela = odabraniJela,
                klimatskiTipovi = odabraniKlimatskiTipovi,
                zemljisniTipovi = odabraniZemljisniTipovi,
            )


            CoroutineScope(Dispatchers.Main).launch {
                val ispravljenaBiljka = withContext(Dispatchers.IO){
                    TrefleDAO().fixData(novaBiljka)
                }

                val intent = Intent(this@NovaBiljkaActivity, MainActivity::class.java)
                intent.putExtra("nova_biljka", ispravljenaBiljka)
                startActivity(intent)

            }

            /* val intent = Intent(this@NovaBiljkaActivity, MainActivity::class.java)
             intent.putExtra("nova_biljka", novaBiljka)
             startActivity(intent)
             finish()*/
        }

    }

}
//FUNKCIJE ZA BOJU LISTVIEW U SKLADU SA VALIDACIJOM
private fun markInvalidSelections(listView: ListView) {
    for (i in 0 until listView.count) {
        val view = listView.getChildAt(i)
        view?.setBackgroundColor(Color.RED)

    }
}
private fun clearInvalidSelections(listView: ListView) {
    for (i in 0 until listView.childCount) {
        val childView = listView.getChildAt(i)
        childView.setBackgroundColor(Color.TRANSPARENT)
    }
}

