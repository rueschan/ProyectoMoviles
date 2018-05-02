package mx.itesm.rueschan.moviles.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import mx.itesm.rueschan.moviles.EntidadesBD.Item;

/**
 * Created by IRV1 on 26/03/18.
 */
@Dao
public interface ItemDAO {

    @Query("SELECT * FROM Item")
    List<Item> getAllItems();

    @Query("SELECT * FROM Item WHERE userID = :userID")
    List<Item> getAllItemsByUserId(int userID);

    @Query("SELECT * FROM Item WHERE tipo = :tipo")
    List<Item> getAllItemsByType(String tipo);

    @Query("SELECT * FROM Item WHERE tipo = :tipo AND userID = :userID")
    List<Item> getAllItemsByTypeAndUserID(String tipo, int userID);

    @Query("SELECT * FROM Item WHERE color = :color")
    List<Item> getItemsByColor(String color);

    @Query("SELECT * FROM Item WHERE color = :color AND tipo = :tipo")
    List<Item> getItemsByColorAndType(String color, String tipo);

    @Query("SELECT * FROM Item WHERE id = :id")
    List<Item> getItemById(int id);

    @Query("SELECT * FROM Item WHERE foto = :foto")
    List<Item> getItemByPhoto(byte[] foto);

    @Query("SELECT COUNT(*) FROM Item WHERE tipo = :tipo")
    int countByType(String tipo);

    @Query("SELECT COUNT(*) FROM Item WHERE tipo = :tipo AND userID = :userID")
    int countByTypeAndUserID(String tipo, int userID);

    @Query("SELECT * FROM Item WHERE evento = :evento")
    List<Item> getItemByEvent(String evento);

    @Insert
    void insertar(Item... items);

    @Query("DELETE FROM Item WHERE id = :id")
    void deletePhotoById(int id);

    @Query("DELETE FROM Item")
    void delete();
}
