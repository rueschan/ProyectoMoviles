package mx.itesm.rueschan.moviles.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import mx.itesm.rueschan.moviles.EntidadesBD.User;

/**
 * Created by yusomalo on 26/03/18.
 */

@Dao
public interface UserDAO {

    @Query("SELECT * FROM User")
    List<User> getAll();

//    @Query("SELECT * FROM prenda WHERE idPrenda = idPrenda")
//    Prenda buscarPorMatricula(int idPrenda);

    @Query("SELECT * FROM User WHERE email = :email")
    User searchByEmail(String email);

    @Query("SELECT * FROM User WHERE idUser = :idUser")
    User searchById(int idUser);

    @Query("UPDATE User SET color= :color WHERE idUser = :idUser")
    void updateColor(String color, int idUser);

    @Query("SELECT password FROM User WHERE email = :email")
    String searchPasswordByEmail(String email);

    @Query("SELECT COUNT(*) FROM User")
    int countUsers();

    //@Query("INSERT into User")
    @Query("SELECT COUNT(*) FROM User WHERE email = :email")
    int countUsersByEmail(String email);



    @Insert
    void insertUsers(User... usuarios);

    @Query("DELETE FROM User")    // CUIDADO!!!
    void borrar();

}
