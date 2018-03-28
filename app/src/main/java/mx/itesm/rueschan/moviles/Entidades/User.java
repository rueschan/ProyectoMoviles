package mx.itesm.rueschan.moviles.Entidades;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yusomalo on 26/03/18.
 */

@Entity(tableName = "User", indices = @Index(value = "email", unique = true))
public class User {

   @PrimaryKey(autoGenerate = true)
    private int idUser;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "email")
    private String email;

    /*@ColumnInfo(name = "fColor")
    private String fColor;*/

    @ColumnInfo(name = "password")
    private String password;


    @ColumnInfo(name = "birth")
    private String birth;

    @ColumnInfo(name = "age")
    private int age;


    @ColumnInfo(name = "gender")
    private String gender;
/*
    @ColumnInfo(name = "foto")
    private byte[] foto;
*/

    // Getter && Setter

   public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
/*
    public String getfColor() {
        return fColor;
    }

    public void setfColor(String fColor) {
        this.fColor = fColor;
    }
*/
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


}
