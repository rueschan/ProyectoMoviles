package mx.itesm.rueschan.moviles.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import mx.itesm.rueschan.moviles.EntidadesBD.Outfit;

/**
 * Created by Rubén Escalante on 27/03/2018.
 */

@Dao
public interface OutfitDAO {

    @Query("SELECT * FROM Outfit")
    List<Outfit> readAll();

    @Query("SELECT * FROM Outfit WHERE id = :id")
    Outfit searchByOutfitID(int id);

    @Query("SELECT * FROM Outfit WHERE name = :name")
    Outfit searchByName(String name);

    @Query("SELECT * FROM Outfit WHERE coat = :coat")
    Outfit searchByCoat(String coat);

    @Query("SELECT * FROM Outfit WHERE upper = :upper")
    Outfit searchByUpper(String upper);

    @Query("SELECT * FROM Outfit WHERE bottom = :bottom")
    Outfit searchByLower(String bottom);

    @Query("SELECT * FROM Outfit WHERE shoes = :shoes")
    Outfit searchByShoes(String shoes);

    @Query("SELECT COUNT(*) FROM Outfit")
    int countOutfits();

    @Insert
    void insertOutfits(Outfit... outfits);

    @Query("DELETE FROM Outfit")    // CUIDADO!!!
    void erase();

}
