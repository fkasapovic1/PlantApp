package ba.unsa.etf.rma.projekat

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream


class Converters {

    @TypeConverter
    fun fromMedicinskaKoristList(value: List<MedicinskaKorist>?): String? {
        return value?.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toMedicinskaKoristList(value: String?): List<MedicinskaKorist>? {
        return value?.split(",")?.mapNotNull {
            try {
                MedicinskaKorist.valueOf(it)
            } catch (ex: IllegalArgumentException) {
                null // Ignorisanje nevažećih vrijednosti
            }
        }
    }

    @TypeConverter
    fun fromKlimatskiUticajList(value: List<KlimatskiTip>?): String? {
        return value?.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toKlimatskiUticajList(value: String?): List<KlimatskiTip>? {
        return value?.split(",")?.mapNotNull {
            try {
                KlimatskiTip.valueOf(it)
            } catch (ex: IllegalArgumentException) {
                null // Ignorisanje nevažećih vrijednosti
            }
        }
    }

    @TypeConverter
    fun fromZemljisteList(value: List<Zemljiste>?): String? {
        return value?.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toZemljisteList(value: String?): List<Zemljiste>? {
        return value?.split(",")?.mapNotNull {
            try {
                Zemljiste.valueOf(it)
            } catch (ex: IllegalArgumentException) {
                null // Ignorisanje nevažećih vrijednosti
            }
        }
    }

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): String? {
        if (bitmap == null) return null
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    @TypeConverter
    fun toBitmap(base64String: String?): Bitmap? {
        if (base64String == null) return null
        val byteArray = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    @TypeConverter
    fun fromJelaList(value: List<String>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun toJelaList(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }

    //Nije naglaseno ali ako se bude testiralo
    @TypeConverter
    fun fromProfilOkusaList(value: List<ProfilOkusaBiljke>?): String? {
        return value?.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toProfilOkusaList(value: String?): List<ProfilOkusaBiljke>? {
        return value?.split(",")?.mapNotNull {
            try {
                ProfilOkusaBiljke.valueOf(it)
            } catch (ex: IllegalArgumentException) {
                null
            }
        }
    }



}


