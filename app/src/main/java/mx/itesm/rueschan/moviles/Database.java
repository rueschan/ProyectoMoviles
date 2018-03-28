package mx.itesm.rueschan.moviles;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import mx.itesm.rueschan.moviles.DAO.TopDAO;
import mx.itesm.rueschan.moviles.DAO.UserDAO;
import mx.itesm.rueschan.moviles.Entidades.TopBD;
import mx.itesm.rueschan.moviles.Entidades.User;

/**
 * Created by IRV1 on 26/03/18.
 */
@Database(entities = {TopBD.class, User.class}, version = 3)
public abstract class DataBase extends RoomDatabase{

    private static DataBase INSTANCE;

    public abstract TopDAO topDAO();

    public abstract UserDAO userDAO();

    public static DataBase getInstance(Context contexto) {
        if (INSTANCE==null) {
            INSTANCE = Room.databaseBuilder(contexto.getApplicationContext(), DataBase.class ,"baseDatos").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
