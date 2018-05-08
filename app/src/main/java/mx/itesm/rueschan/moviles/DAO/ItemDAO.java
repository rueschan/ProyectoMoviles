package mx.itesm.rueschan.moviles.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import mx.itesm.rueschan.moviles.EntidadesBD.Item;

/**
 * Created by IRV1 on 26/03/18.
 */
@Dao
public interface ItemDAO {

    @Insert
    void insert(Item... items);

    @Update
    void update(Item... items);

    @Delete
    void delete(Item... items);

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
    Item getItemById(int id);

    @Query("SELECT * FROM Item WHERE foto = :foto")
    List<Item> getItemByPhoto(byte[] foto);

    @Query("SELECT COUNT(*) FROM Item WHERE tipo = :tipo")
    int countByType(String tipo);

    @Query("SELECT COUNT(*) FROM Item WHERE tipo = :tipo AND userID = :userID")
    int countByTypeAndUserID(String tipo, int userID);

    @Query("SELECT * FROM Item WHERE evento = :evento")
    List<Item> getItemByEvent(String evento);

    @Query("DELETE FROM Item WHERE id = :id")
    void deleteById(int id);

    @Query("DELETE FROM Item")
    void delete();

    @Query("UPDATE Item SET color = :color WHERE id = :id")
    void updateItemColor(String color, int id);

    @Query("UPDATE Item SET evento = :evento WHERE id = :id")
    void updateItemEvent(String evento, int id);


}
