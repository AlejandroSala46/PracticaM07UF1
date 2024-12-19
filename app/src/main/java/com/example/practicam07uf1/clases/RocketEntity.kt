package com.example.practicam07uf1.clases
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//Creamos la clase RocketEntity
@Parcelize
@Entity(tableName = "rockets")
data class RocketEntity (
    @PrimaryKey var name: String = "",
    var type: String = "",
    var active: Boolean = false,
    var cost_per_launch: Double = 0.0,
    var success_rate_pct: Double = 0.0,
    var country: String = "",
    var company: String = "",
    var wikipedia: String = "",
    var description: String = "",

    /*  En este caso he decidido no hacer dos tablas de entidades ya que,
        considero, que al ser una tabla con relacion 1:1 era mas elegante
        usar el sistema "embedded" y mas sencillo de entender en el codigo.
        He probado haciendolo con las dos entidades, mas la tabla de relacion, pero considero
        que el trabajo/baja legitividad no compensa para "lo que tiene de mejor" respecto a esta solucion.
     */
    @Embedded(prefix = "height_")
    var height: Dim = Dim(0.0, 0.0),

    @Embedded(prefix = "diameter_")
    var diameter: Dim = Dim(0.0, 0.0)
): Parcelable {

    @Parcelize
    data class Dim(
        var feet: Double,
        var meters: Double
    ):Parcelable
} //Se que pareclize es un tipo que esta anticuado, pero los otros que he probado me han dado varios problemas

