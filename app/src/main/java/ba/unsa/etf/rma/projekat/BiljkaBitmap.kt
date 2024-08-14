package ba.unsa.etf.rma.projekat

import android.graphics.Bitmap
import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "BiljkaBitmap",
    foreignKeys = [ForeignKey(
        entity = Biljka::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("idBiljke"),
        onDelete = ForeignKey.CASCADE
    )]
)

data class BiljkaBitmap(
    @PrimaryKey(autoGenerate = true) val id:Long? = null,
    @ColumnInfo(name="idBiljke") var idBiljke: Int,
    @ColumnInfo(name = "bitmap") @SerializedName("bitmap") val bitmap: Bitmap
)
