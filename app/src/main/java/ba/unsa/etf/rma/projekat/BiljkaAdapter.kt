package ba.unsa.etf.rma.projekat

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BiljkaAdapter(private var listaBiljki: List<Biljka>, private var selectedMode: String, val listener: MyClickListener):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    fun setSelectedMode(mode: String) {
        selectedMode = mode
        notifyDataSetChanged()
    }
    fun getSelectedMode(): String {
        return selectedMode
    }
    fun setBiljke(biljke: List<Biljka>) {
        listaBiljki = biljke
        notifyDataSetChanged()
    }

    fun updateBiljke(noveBiljke: List<Biljka>) {
        listaBiljki = noveBiljke
        notifyDataSetChanged()
    }


    // Dobra stvarcica
    fun postaviSliku(biljka: Biljka, context: Context, slikaItem: ImageView) {
        CoroutineScope(Dispatchers.Main).launch {
            val bitmap = withContext(Dispatchers.IO) {
                try {
                    val imageBitmap = TrefleDAO().getImage(biljka)
                    val uspjeh = BiljkaRepository.dodajSliku(context, biljka.id, imageBitmap)
                    if (uspjeh) imageBitmap else null
                } catch (e: Exception) {
                    null
                }
            }

            if (bitmap != null) {
                slikaItem.setImageBitmap(bitmap)
            } else {
                val defaultBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.default_slika)
                slikaItem.setImageBitmap(defaultBitmap)
            }
        }
    }

    private fun getDrawableResourceId(naziv: String): String {
        val resourceName = naziv
            .replace(" ", "_")
            .replace("(","")
            .replace(")","")
            .replace("ž", "z")
            .replace("š", "s")
            .replace("[^A-Za-z0-9_]", "")
            .lowercase()
        return resourceName
    }

    private val medicinskiMod = 1
    private val kuharskiMod = 2
    private val botanickiMod = 3

    inner class MedicinskiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slikaItem: ImageView =itemView.findViewById(R.id.slikaItem)
        val nazivItem: TextView = itemView.findViewById(R.id.nazivItem)
        val upozorenjeItem: TextView=itemView.findViewById(R.id.upozorenjeItem)
        val korist1Item:TextView=itemView.findViewById(R.id.korist1Item)
        val korist2Item:TextView=itemView.findViewById(R.id.korist2Item)
        val korist3Item:TextView=itemView.findViewById(R.id.korist3Item)
        init {
            itemView.setOnClickListener{
                val position=adapterPosition
                listener.onClick(position)
            }
        }
    }

    inner class KuharskiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slikaItem: ImageView =itemView.findViewById(R.id.slikaItem)
        val nazivItem: TextView = itemView.findViewById(R.id.nazivItem)
        val profilOkusaItem: TextView=itemView.findViewById(R.id.profilOkusaItem)
        val jelo1Item:TextView=itemView.findViewById(R.id.jelo1Item)
        val jelo2Item:TextView=itemView.findViewById(R.id.jelo2Item)
        val jelo3Item:TextView=itemView.findViewById(R.id.jelo3Item)
        init {
            itemView.setOnClickListener{
                val position=adapterPosition
                listener.onClick(position)
            }
        }
    }

    inner class BotanickiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slikaItem: ImageView =itemView.findViewById(R.id.slikaItem)
        val nazivItem: TextView = itemView.findViewById(R.id.nazivItem)
        val porodicaItem:TextView=itemView.findViewById(R.id.porodicaItem)
        val klimatskiTipItem:TextView=itemView.findViewById(R.id.klimatskiTipItem)
        val zemljisniTipItem:TextView=itemView.findViewById(R.id.zemljisniTipItem)
        init {
            itemView.setOnClickListener{
                val position=adapterPosition
                listener.onClick(position)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            medicinskiMod -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.medicinski_fokus, parent, false)
                MedicinskiViewHolder(view)
            }
            kuharskiMod -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.kuharski_fokus, parent, false)
                KuharskiViewHolder(view)
            }
            botanickiMod -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.botanicki_fokus, parent, false)
                BotanickiViewHolder(view)
            }
            else -> throw IllegalArgumentException("Greška")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val biljka = listaBiljki[position]
        val context=holder.itemView.context//
        when (holder.itemViewType) {
            medicinskiMod -> {
                val medicinskiHolder = holder as MedicinskiViewHolder
                medicinskiHolder.nazivItem.text = biljka.naziv
                medicinskiHolder.upozorenjeItem.text = biljka.medicinskoUpozorenje
                medicinskiHolder.korist1Item.text = biljka.medicinskeKoristi?.getOrNull(0)?.opis ?: ""
                medicinskiHolder.korist2Item.text = biljka.medicinskeKoristi?.getOrNull(1)?.opis ?: ""
                medicinskiHolder.korist3Item.text = biljka.medicinskeKoristi?.getOrNull(2)?.opis ?: ""

                //Ako ne stavim ovo refresha mi uvijek samo prve 3 pozicije i iste slike vrti

               // postaviSliku(biljka, context, medicinskiHolder.slikaItem)
                medicinskiHolder.slikaItem.setImageDrawable(null)
  /*          //Dodavanje slike
                CoroutineScope(Dispatchers.Main).launch {
                    val bitmap = withContext(Dispatchers.IO) {
                        TrefleDAO().getImage(biljka)
                    }
                    //BiljkaRepository.dodajSliku(bitmap)
                    medicinskiHolder.slikaItem.setImageBitmap(bitmap)
                }
*/
                                val slikaID=context.resources.getIdentifier(getDrawableResourceId(biljka.naziv),"drawable",context.packageName)
                                if(slikaID!=0){
                                 medicinskiHolder.slikaItem.setImageResource(slikaID)
                                }else {
                                 medicinskiHolder.slikaItem.setImageResource(R.drawable.default_slika)
                                }
            }
            kuharskiMod -> {
                val kuharskiHolder = holder as KuharskiViewHolder
                kuharskiHolder.nazivItem.text = biljka.naziv
                kuharskiHolder.profilOkusaItem.text = biljka.profilOkusa?.opis
                kuharskiHolder.jelo1Item.text = biljka.jela?.getOrNull(0) ?: ""
                kuharskiHolder.jelo2Item.text = biljka.jela?.getOrNull(1) ?: ""
                kuharskiHolder.jelo3Item.text = biljka.jela?.getOrNull(2) ?: ""

                //Ako ne stavim ovo refresha mi uvijek samo prve 3 pozicije i iste slike vrti
                //postaviSliku(biljka, context, kuharskiHolder.slikaItem)
                kuharskiHolder.slikaItem.setImageDrawable(null)
  /*                            //Dodavanje slike
                CoroutineScope(Dispatchers.Main).launch {
                    val bitmap = withContext(Dispatchers.IO) {
                        TrefleDAO().getImage(biljka)
                    }
                    kuharskiHolder.slikaItem.setImageBitmap(bitmap)
                }
*/
                                val slikaID=context.resources.getIdentifier(getDrawableResourceId(biljka.naziv),"drawable",context.packageName)
                                if(slikaID!=0){
                                    kuharskiHolder.slikaItem.setImageResource(slikaID)
                                }else {
                                    kuharskiHolder.slikaItem.setImageResource(R.drawable.default_slika)
                                }
            }
            botanickiMod -> {
                val botanickiHolder = holder as BotanickiViewHolder
                botanickiHolder.nazivItem.text = biljka.naziv
                botanickiHolder.porodicaItem.text = biljka.porodica
                botanickiHolder.klimatskiTipItem.text = biljka.klimatskiTipovi?.getOrNull(0)?.opis ?: ""
                botanickiHolder.zemljisniTipItem.text = biljka.zemljisniTipovi?.getOrNull(0)?.naziv ?: ""
//provjera***

                //Ako ne stavim ovo refresha mi uvijek samo prve 3 pozicije i iste slike vrti
                botanickiHolder.slikaItem.setImageDrawable(null)
                //postaviSliku(biljka, context, botanickiHolder.slikaItem)
  /*          //Dodavanje slike
                CoroutineScope(Dispatchers.Main).launch {
                    val bitmap = withContext(Dispatchers.IO) {
                        TrefleDAO().getImage(biljka)
                    }
                    botanickiHolder.slikaItem.setImageBitmap(bitmap)
                }
*/
                val slikaID=context.resources.getIdentifier(getDrawableResourceId(biljka.naziv),"drawable",context.packageName)
                if(slikaID!=0){
                    botanickiHolder.slikaItem.setImageResource(slikaID)
                }else {
                    botanickiHolder.slikaItem.setImageResource(R.drawable.default_slika)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return listaBiljki.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (selectedMode) {
            "Medicinski" -> medicinskiMod
            "Kuharski" -> kuharskiMod
            "Botanicki" -> botanickiMod
            else -> throw IllegalArgumentException("Nepoznat mod")
        }
    }
}
