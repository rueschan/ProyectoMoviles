package mx.itesm.rueschan.moviles;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by yusomalo on 26/03/18.
 */

@android.arch.persistence.room.Database(entities = {User.class}, version = 1)

public abstract class Database extends RoomDatabase{
    private static Database INSTANCE;

    public abstract UserDAO userDAO();

    public static Database getInstance(Context contexto) {
        if (INSTANCE==null) {
            INSTANCE = Room.databaseBuilder(contexto.getApplicationContext(),
                    Database.class,
                    "baseDatos")
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
