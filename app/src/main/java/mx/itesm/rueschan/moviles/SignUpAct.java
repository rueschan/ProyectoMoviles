package mx.itesm.rueschan.moviles;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yusomalo on 26/03/18.
 */

public class SignUpAct extends AppCompatActivity {

    private AutoCompleteTextView edEmail;
    private EditText edName;
    private EditText edPassword;
    private EditText edBirth;
    private RadioGroup rgGenderGroup;
    private RadioButton rbGender;
    private String email, name, password, gender;
    //private int age;
    private Date birth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_o);

        edEmail = findViewById(R.id.email);
        edName = findViewById(R.id.name);
        edPassword = findViewById(R.id.password);
        edBirth = findViewById(R.id.birth_date);
        rgGenderGroup = findViewById(R.id.genderGroup);
        rbGender = findViewById(R.id.btn_male);
    }

    private boolean attemptRegister(String name, String email, String password, String gender) {
        // Reset errors.
        edEmail.setError(null);
        edPassword.setError(null);
        edPassword.setError(null);
        edBirth.setError(null);
        rbGender.setError(null);

        // Check for a valid password
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            edPassword.setError(getString(R.string.error_invalid_password));
            return false;
        }

        // Check for a valid Data
        if (TextUtils.isEmpty(name)) {
            edName.setError(getString(R.string.error_field_required));
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            edEmail.setError(getString(R.string.error_field_required));
            return false;
        } else if (!isEmailValid(email)) {
            edEmail.setError(getString(R.string.error_invalid_email));
            return false;
        }

        if (!getBirthDate()) {
            edBirth.setError(getString(R.string.error_field_required));
            Toast error = Toast.makeText(getBaseContext(), "Invalid date format: dd/MM-yyyy", Toast.LENGTH_LONG);
            error.show();
            return false;
        }

        if (TextUtils.isEmpty(gender)) {
            rbGender.setError(getString(R.string.error_field_required));
            return false;
        }
        return true;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /*private boolean isDateValid(Date date){
        return
    }*/

    public void changeMain(View v) {
        email = edEmail.getText().toString();
        name = edName.getText().toString();
        password = edPassword.getText().toString();
        gender = selectGender();

        if (attemptRegister(name, email, password, gender))
            new DBTarea().execute();
        //Log.i("hola",email +"\n" + name + "\n" + password + "\n" + gender + "\n" + age);
        //Intent init = new Intent(this, MainActivity.class);
        //startActivity(init);
    }

    private void saveUser(){
        Database db = Database.getInstance(this);
        int countUsers = db.userDAO().countUsersByEmail(email);
        if (countUsers <= 0) {
            User user = new User();
            user.setName(edName.getText().toString());
            user.setEmail(edEmail.getText().toString());
            //user.setAge(getRealAge());
            user.setPassword(edPassword.getText().toString());
            user.setGender(selectGender());

            //Database
            db.userDAO().insertUsers(user);

            Log.i("onResume", "Registros: " + db.userDAO().countUsers());
            Database.destroyInstance();

            Intent init = new Intent(this, LoginAct.class);
            startActivity(init);
        }
        else{
            runOnUiThread(new Runnable() {
                public void run() {
                    edEmail.setError("Email already registered");
                    Toast error = Toast.makeText(getBaseContext(), "Email already registered in database", Toast.LENGTH_LONG);
                    error.show();
                }
            });

        }
    }

    private boolean getBirthDate(){
        Date birthDate;// = new Date();
        try{
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String date = edBirth.getText().toString();
            birthDate = df.parse(date);
            setBirth(birthDate);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
            //Log.i("fecha", birthDate.toString());
        }
    }



    public String selectGender(){
        int selectedGender = rgGenderGroup.getCheckedRadioButtonId();
        String gender1;
        if (selectedGender == -1){
            gender1 = "";
        }else {
            rbGender = findViewById(selectedGender);
            gender1 = rbGender.getText().toString();
        }
        return gender1;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    class DBTarea extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            saveUser();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("onPost", "Dato grabado ********************");
        }
    }
}


