package mx.itesm.rueschan.moviles;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

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
    private Date birth;

    @ColumnInfo(name = "age")
    private int age = age = getAge(birth.getYear() + 1900 , birth.getMonth(), birth.getDay());


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

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public int getAge() {
        age = getAge(birth.getYear() + 1900 , birth.getMonth(), birth.getDay());
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

    private int getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);

        return ageInt;
    }
}
