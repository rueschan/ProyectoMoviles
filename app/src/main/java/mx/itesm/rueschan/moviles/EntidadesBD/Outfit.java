package mx.itesm.rueschan.moviles.EntidadesBD;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Rubén Escalante on 13/03/2018.
 */

@Entity(tableName = "Outfit")
public class Outfit {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ForeignKey(entity = Item.class,
            parentColumns = "id",
            childColumns = "coatID")
    @ColumnInfo(name = "coat")
    private int coatID;

    @ForeignKey(entity = Item.class,
            parentColumns = "id",
            childColumns = "upperID")
    @ColumnInfo(name = "upper")
    private int upperID;

    @ForeignKey(entity = Item.class,
            parentColumns = "id",
            childColumns = "bottomID")
    @ColumnInfo(name = "bottom")
    private int bottomID;

    @ForeignKey(entity = Item.class,
            parentColumns = "id",
            childColumns = "shoesID")
    @ColumnInfo(name = "shoes")
    private int shoesID;

    public Outfit(){
        Log.i("Outfit", "Success on creation");
    }

    public Outfit(String name, int coatID, int upperID, int bottomID, int shoesID) {
        this.name = name;
        this.coatID = coatID;
        this.upperID = upperID;
        this.bottomID = bottomID;
        this.shoesID = shoesID;
        Log.i("Outfit", "Success on creation");
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCoatID() {
        return coatID;
    }

    public void setCoatID(int coatID) {
        this.coatID = coatID;
    }

    public int getUpperID() {
        return upperID;
    }

    public void setUpperID(int upperID) {
        this.upperID = upperID;
    }

    public int getBottomID() {
        return bottomID;
    }

    public void setBottomID(int bottomID) {
        this.bottomID = bottomID;
    }

    public int getShoesID() {
        return shoesID;
    }

    public void setShoesID(int shoesID) {
        this.shoesID = shoesID;
    }

    public boolean isFull() {
        if (coatID != -1 && upperID != -1 && bottomID != -1 && shoesID != -1) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return name + " (" + id + ") : Coat> " + coatID + ", Upper> " + upperID + ", Bottom> " +
                bottomID + ", Shoes> " + shoesID;
    }
}
