package mx.itesm.rueschan.moviles.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import mx.itesm.rueschan.moviles.Entidades.TopBD;

/**
 * Created by IRV1 on 26/03/18.
 */
@Dao
public interface TopDAO {

    @Query("SELECT * FROM top")
    List<TopBD> getAllPhotos();

    @Query("SELECT * FROM top WHERE tipo = :tipo")
    List<TopBD> getAllPhotosByType(String tipo);

    @Query("SELECT * FROM top WHERE color = :color")
    List<TopBD> getPhotosByColor(String color);

    @Query("SELECT * FROM top WHERE color = :color AND tipo = :tipo")
    List<TopBD> getPhotosByColorAndType(String color, String tipo);

    @Query("SELECT * FROM top WHERE id = :id")
    List<TopBD> getPhotosById(int id);

    @Query("SELECT COUNT(*) FROM top WHERE tipo = :tipo")
    int countByType(String tipo);

    @Insert
    void insertar(TopBD... tops);

    @Query("DELETE FROM top WHERE id = :id")
    void deletePhotoById(int id);
}
