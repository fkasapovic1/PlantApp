package ba.unsa.etf.rma.projekat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class MainActivity : AppCompatActivity(), MyClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BiljkaAdapter
    private lateinit var spinner: Spinner
    private lateinit var botSpinner: Spinner
    private lateinit var pretragaUnesi: EditText
    private lateinit var pretragaDugme: Button
    private var listBiljki: MutableList<Biljka> = mutableListOf()
    private var SpinnerBrzaPretraga: Int = 0


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // I  Dodavanje biljki
        listBiljki.addAll(BiljkaRepository.getBiljke())
        // II Dodavanje biljki iz baze
       /* CoroutineScope(Dispatchers.Main).launch {
            val sveBiljke = BiljkaRepository.dajSveBiljke(this@MainActivity)
            listBiljki.addAll(sveBiljke)
            adapter.updateBiljke(sveBiljke)
            adapter.notifyDataSetChanged()
        }*/
        recyclerView = findViewById(R.id.biljkeRV)
        spinner = findViewById(R.id.modSpinner)
        botSpinner = findViewById(R.id.bojaSPIN)
        pretragaUnesi=findViewById(R.id.pretragaET)
        pretragaDugme=findViewById(R.id.brzaPretraga)

        val modovi = arrayOf("Medicinski", "Kuharski", "Botanicki")
        val defaultMod = modovi[0]
        adapter = BiljkaAdapter(listBiljki, defaultMod, this@MainActivity)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        //val database = BiljkaDatabase.getInstance(this)   // DODANO OVDJE
       // biljkaDAO = database.biljkaDAO()
/*
        CoroutineScope(Dispatchers.Main).launch {
            val sveBiljke = BiljkaRepository.dajSveBiljke(this@MainActivity)
            listBiljki.addAll(sveBiljke)
            adapter.notifyDataSetChanged()

            // Dohvatite i dodajte slike za svaku biljku
            withContext(Dispatchers.IO) {
                for (biljka in sveBiljke) {
                    val bitmap = try {
                        TrefleDAO().getImage(biljka)
                    } catch (e: Exception) {

                        null
                    }

                    if (bitmap != null) {
                        biljkaDAO.addImage(biljka.id, bitmap)
                    }
                }
            }
        }*/




        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, modovi)
        spinner.setSelection(0) //Defaultni mod

        val boje = arrayOf("red","blue","yellow","orange","purple","brown","green")
        botSpinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,boje)
        //postavi za botSpinner
        botSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //..
            }
        }


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedMode = modovi[position]
                updateMod(selectedMode=="Botanicki")
                nakonBrzePretrage(selectedMode)
                adapter.setSelectedMode(selectedMode)
                //adapter.updateBiljke(filt)
                Toast.makeText(applicationContext, "Izabrani modul je " + modovi[position],Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //...
            }
        }
        val resetButton: Button = findViewById(R.id.resetBtn)
        resetButton.setOnClickListener {
            SpinnerBrzaPretraga=0
            val selectedMode = spinner.selectedItem.toString()
            adapter.setBiljke(listBiljki)
            adapter.setSelectedMode(selectedMode)
        }

        // NovaBiljkaActivity
        val novaBiljkaBtn: Button = findViewById(R.id.novaBiljkaBtn)
        novaBiljkaBtn.setOnClickListener {
            val i = Intent(this, NovaBiljkaActivity::class.java)
            startActivity(i)
        }

//Dodavanje nove biljke

        if (intent.hasExtra("nova_biljka")) {
            // Izvlačenje nove biljke iz intenta
            val novaBiljka = intent.getSerializableExtra("nova_biljka") as Biljka
            //dodajNovuBiljku(novaBiljka)
            //adapter.updateBiljke(listBiljki)
            //adapter.notifyDataSetChanged()
          listBiljki.clear()
            CoroutineScope(Dispatchers.Main).launch {
                dodajNovuBiljku(novaBiljka)
                val sveBiljke = BiljkaRepository.dajSveBiljke(this@MainActivity)
                listBiljki.addAll(sveBiljke)
                adapter.updateBiljke(sveBiljke)
                adapter.notifyDataSetChanged()
            }


        }

        /*CoroutineScope(Dispatchers.Main).launch {
            if (intent.hasExtra("nova_biljka")) {
                val novaBiljka = intent.getSerializableExtra("nova_biljka") as Biljka
                BiljkaRepository.dodajNovuBiljku(this@MainActivity,novaBiljka)
            }
            adapter.notifyDataSetChanged()
        }*/



        pretragaDugme.setOnClickListener {
            val pretragaText = pretragaUnesi.text.toString()
            val odabranaBoja = botSpinner.selectedItem.toString()
            SpinnerBrzaPretraga=0
            SpinnerBrzaPretraga++

            if (pretragaText.isNotEmpty() && odabranaBoja.isNotEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    val noveBiljke = withContext(Dispatchers.IO) {
                        TrefleDAO().getPlantsWithFlowerColor(odabranaBoja, pretragaText)
                    }
                    adapter.updateBiljke(noveBiljke)
                }
            }else {
                Toast.makeText(this, "Niste popunili potrebna polja za brzu pretragu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Napomena: Prepraviti drugi click u skladu sa uputstvima na sljedecoj spirali !
    override fun onClick(position: Int) {
        val selectedBiljka = listBiljki[position]
        if (SpinnerBrzaPretraga == 0) {
            val filteredPlants = when (adapter.getSelectedMode()) {
                "Medicinski" -> {
                    listBiljki.filter { plant ->
                        plant.medicinskeKoristi?.any {
                            selectedBiljka.medicinskeKoristi?.contains(it) == true
                        } == true
                    }
                }

                "Kuharski" -> {
                    listBiljki.filter { plant ->
                        (plant.jela?.any {
                            selectedBiljka.jela?.contains(it) == true
                        } == true) || plant.profilOkusa == selectedBiljka.profilOkusa
                    }
                }

                "Botanicki" -> {
                    listBiljki.filter { plant ->
                        plant.porodica == selectedBiljka.porodica &&
                                plant.klimatskiTipovi?.any {
                                    selectedBiljka.klimatskiTipovi?.contains(it) == true
                                } == true &&
                                plant.zemljisniTipovi?.any {
                                    selectedBiljka.zemljisniTipovi?.contains(it) == true
                                } == true
                    }
                }

                else -> listBiljki
            }
            adapter.setBiljke(filteredPlants)
        }
    }


    private fun updateMod(vidljivo:Boolean) {
        if (vidljivo) {
            pretragaUnesi.visibility = View.VISIBLE
            botSpinner.visibility = View.VISIBLE
            pretragaDugme.visibility = View.VISIBLE
        } else {
            pretragaUnesi.visibility = View.GONE
            botSpinner.visibility = View.GONE
            pretragaDugme.visibility = View.GONE
        }
    }
    private fun nakonBrzePretrage(selectedMode:String){
        if((selectedMode=="Medicinski"||selectedMode=="Kuharski") && SpinnerBrzaPretraga!=0){
            adapter.setBiljke(listBiljki)
            SpinnerBrzaPretraga=0
        }
    }


    fun dodajNovuBiljku(biljka: Biljka) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                BiljkaRepository.dodajNovuBiljku(this@MainActivity,biljka)
                listBiljki.clear()
                listBiljki.addAll(BiljkaRepository.dajSveBiljke(this@MainActivity))
                adapter.updateBiljke(listBiljki)
                adapter.notifyDataSetChanged()
                Toast.makeText(applicationContext, "Nova biljka dodata", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "Greška prilikom dodavanja biljke", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }


}