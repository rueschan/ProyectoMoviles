package mx.itesm.rueschan.moviles.EntidadesBD;



import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
/**
 * Created by IRV1 on 26/03/18.
 */


@Entity(tableName = "Item")
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ForeignKey(entity = User.class,
            parentColumns = "idUser",
            childColumns = "userID")
    @ColumnInfo(name = "userID")
    private int userID;

    @ColumnInfo(name = "foto")
    private byte[] foto;

    @ColumnInfo(name = "color")
    private String color;

    @ColumnInfo(name = "tipo")
    private String tipo;

    @ColumnInfo(name = "evento")
    private String evento;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    @Override
    public String toString() {
        return "(" + id + ") Usuario: " + userID + " Foto: " + foto + " Color: " + color + " Tipo: " + tipo + " Evento: " + evento;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }
}
