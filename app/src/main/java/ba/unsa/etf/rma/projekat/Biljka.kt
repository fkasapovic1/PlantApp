package ba.unsa.etf.rma.projekat

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/*
data class Biljka(
    val naziv:String,
    var porodica : String?,
    var medicinskoUpozorenje : String?,
    var medicinskeKoristi : List<MedicinskaKorist>?,
    var profilOkusa : ProfilOkusaBiljke?,
    var jela : List<String>?,
    var klimatskiTipovi : List<KlimatskiTip>?,
    var zemljisniTipovi : List<Zemljiste>?
):Serializable
*/
@Entity
data class Biljka(
    @PrimaryKey(autoGenerate = true) @SerializedName("id") var id: Int = 0,
    @ColumnInfo(name = "naziv") @SerializedName("naziv") var naziv: String,
    @ColumnInfo(name = "family") @SerializedName("porodica") var porodica: String?,
    @ColumnInfo(name = "medicinskoUpozorenje") @SerializedName("medicinsko_upozorenje") var medicinskoUpozorenje: String?,
    @ColumnInfo(name = "medicinskeKoristi") @SerializedName("medicinske_koristi") var medicinskeKoristi: List<MedicinskaKorist>?,
    @ColumnInfo(name = "profilOkusa") @SerializedName("profil_okusa") var profilOkusa: ProfilOkusaBiljke?,
    @ColumnInfo(name = "jela") @SerializedName("jela") var jela: List<String>?,
    @ColumnInfo(name = "klimatskiTipovi") @SerializedName("klimatski_tipovi") var klimatskiTipovi: List<KlimatskiTip>?,
    @ColumnInfo(name = "zemljisniTipovi") @SerializedName("zemljisni_tipovi") var zemljisniTipovi: List<Zemljiste>?,
    @ColumnInfo(name = "onlineChecked") @SerializedName("online_checked") var onlineChecked: Boolean = false
) : Serializable

