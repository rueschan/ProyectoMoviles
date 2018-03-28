package mx.itesm.rueschan.moviles.Entidades;



import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
/**
 * Created by IRV1 on 26/03/18.
 */


@Entity(tableName = "Top")
public class TopBD {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "foto")
    private byte[] foto;

    @ColumnInfo(name = "color")
    private String color;

    @ColumnInfo(name = "tipo")
    private String tipo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
