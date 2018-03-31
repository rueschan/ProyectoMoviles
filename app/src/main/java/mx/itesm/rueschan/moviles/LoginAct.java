package mx.itesm.rueschan.moviles;

import android.arch.persistence.room.Database;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import mx.itesm.rueschan.moviles.Entidades.User;


/**
 * Created by yusomalo on 26/03/18.
 */

public class LoginAct extends AppCompatActivity {

    private AutoCompleteTextView edEmail;
    private EditText edPassword;
    String email, password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_o);

        edEmail = findViewById(R.id.email);
        edPassword = findViewById(R.id.password);

        if (!primeraVez()) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private boolean primeraVez(){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean primeraVez = preferences.getBoolean("primera", false);
        if (!primeraVez) {
            // primera vez
            SharedPreferences.Editor pref = preferences.edit();
            pref.putBoolean("primera", true);
            pref.commit();
        }
        return !primeraVez;
    }

    public void changeSignUp(View v) {
        Intent init = new Intent(this, SignUpAct.class);
        startActivity(init);
    }

    public void changeMain(View v) {
        email = edEmail.getText().toString();
        password = edPassword.getText().toString();

        //Log.i("hola",email +"\n" + name + "\n" + password + "\n" + gender + "\n" + age);
        if (attemptLogin(email, password))
            new DBTarea().execute();
    }

    private void checkUser(String email, String password) {


        DataBase db = DataBase.getInstance(getApplicationContext());
        //User user1 = db.userDAO().searchByEmail("tata@gmail.com");
       // Log.i("usuario tata: ", user1.getEmail() +"\n" + user1.getName() + "\n" + user1.getPassword() + "\n" + user1.getGender() + "\n" + user1.getAge() + "\n" + user1.getBirth());
        int countUsers = db.userDAO().countUsersByEmail(email);
            if (countUsers <= 0) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        edEmail.setError(getString(R.string.error_email_exist));
                        //Toast error = Toast.makeText(getBaseContext(), "Email not registered in database", Toast.LENGTH_LONG);
                        //error.show();
                    }
                });

            } else {
                User user = new User();
                user.setEmail(email);
                user.setPassword(password);
                String pass = db.userDAO().searchPasswordByEmail(email);
                if (password.equals(pass)) {
                    /*runOnUiThread(new Runnable() {
                        public void run() {
                            showProgress(true);
                        }
                    });*/
                    Intent init = new Intent(this, MainActivity.class);
                    startActivity(init);
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            edPassword.setError(getString(R.string.error_incorrect_password));
                            //Toast errorPass = Toast.makeText(getBaseContext(), "Password entered is incorrect", Toast.LENGTH_LONG);
                            //errorPass.show();
                        }
                    });
                }
            }
    }

    private boolean attemptLogin(String email, String password) {
        // Reset errors.
        edEmail.setError(null);
        edPassword.setError(null);

        // Check for a valid password
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            edPassword.setError(getString(R.string.error_invalid_password));
            return false;
        }

        // Check for a valid email.
        if (TextUtils.isEmpty(email)) {
            edEmail.setError(getString(R.string.error_field_required));
            return false;
        } else if (!isEmailValid(email)) {
            edEmail.setError(getString(R.string.error_invalid_email));
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

    class DBTarea extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            checkUser(email, password);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Log.i("onPost", "Dato grabado ********************");
        }
    }
}
