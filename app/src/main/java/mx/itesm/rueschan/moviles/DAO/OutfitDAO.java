package mx.itesm.rueschan.moviles.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import mx.itesm.rueschan.moviles.EntidadesBD.Outfit;

/**
 * Created by Rubén Escalante on 27/03/2018.
 */

@Dao
public interface OutfitDAO {

    @Insert
    void insert(Outfit... outfits);

    @Update
    void update(Outfit... outfits);

    @Delete
    void delete(Outfit... outfits);

    @Query("SELECT * FROM Outfit")
    List<Outfit> readAll();

    @Query("SELECT * FROM Outfit WHERE id = :id")
    Outfit searchByOutfitID(int id);

    @Query("SELECT * FROM Outfit WHERE name = :name")
    Outfit searchByName(String name);

    @Query("SELECT * FROM Outfit WHERE coat = :coat")
    Outfit searchByCoatID(int coat);

    @Query("SELECT * FROM Outfit WHERE upper = :upper")
    Outfit searchByUpperID(int upper);

    @Query("SELECT * FROM Outfit WHERE bottom = :bottom")
    Outfit searchByLowerID(int bottom);

    @Query("SELECT * FROM Outfit WHERE shoes = :shoes")
    Outfit searchByShoesID(int shoes);

    @Query("SELECT COUNT(*) FROM Outfit")
    int countOutfits();

    // =================== CUIDADO =====================

    @Query("DELETE FROM Outfit")
    void delete();

    @Query("DELETE FROM Outfit WHERE id = :id")
    void eraseByID(int id);

    @Query("DELETE FROM Outfit WHERE coat = :id OR upper = :id OR bottom = :id OR shoes = :id")
    void deleteByItemID(int id);

    // -------------------------------------------------

}
