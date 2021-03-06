package mx.itesm.rueschan.moviles.BD;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import mx.itesm.rueschan.moviles.DAO.OutfitDAO;
import mx.itesm.rueschan.moviles.DAO.ItemDAO;
import mx.itesm.rueschan.moviles.DAO.UserDAO;
import mx.itesm.rueschan.moviles.EntidadesBD.Item;
import mx.itesm.rueschan.moviles.EntidadesBD.Outfit;
import mx.itesm.rueschan.moviles.EntidadesBD.User;

/**
 * Created by IRV1 on 26/03/18.
 */
@Database(entities = {Item.class, User.class, Outfit.class}, version = 6)
public abstract class DataBase extends RoomDatabase{

    private static DataBase INSTANCE;

    public abstract OutfitDAO outfitDAO();

    public abstract ItemDAO itemDAO();

    public abstract UserDAO userDAO();

    public static DataBase getInstance(Context contexto) {
        Log.i("DB","Get Instance");
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(contexto.getApplicationContext(),
                    DataBase.class ,
                    "baseDatos")
                    .build();
        }
        return INSTANCE;
    }

    public static DataBase getInstance() {
        Log.i("DB","Get Instance");
        if (INSTANCE == null) {
            Log.e("BD", "Instance Doesn't Exist");
            return null;
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        Log.i("DB","Destroy Instance");
//        INSTANCE.close();
        INSTANCE = null;
    }

}
