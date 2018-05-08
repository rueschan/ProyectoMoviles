package mx.itesm.rueschan.moviles;

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
import android.widget.ProgressBar;

import mx.itesm.rueschan.moviles.BD.DataBase;
import mx.itesm.rueschan.moviles.EntidadesBD.User;


/**
 * Created by yusomalo on 26/03/18.
 */

public class LoginAct extends AppCompatActivity {

    private AutoCompleteTextView edEmail;
    private EditText edPassword;
    String email, password;
    private User user;
    private ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_o);

        edEmail = findViewById(R.id.email);
        edPassword = findViewById(R.id.password);

        edEmail.setText("");
        edPassword.setText("");

        progressBar = findViewById(R.id.login_progress);
        //startActivity(new Intent(this, MainActivity.class));
        //System.out.println(primeraVez() + " *********** " + sesionIniciada());
        if (!primeraVez()) {
            if(sesionIniciada())
                startActivity(new Intent(this, MainActivity.class));
        }
    }

    private boolean sesionIniciada() {
        SharedPreferences preferences = getSharedPreferences("Log", MODE_PRIVATE);
        boolean sesionInicida = preferences.getBoolean("sesion", false);
        return sesionInicida;
    }

    @Override
    protected void onResume() {

        edEmail.setText("");
        edPassword.setText("");

        super.onResume();

    }

    private boolean primeraVez(){
        SharedPreferences preferences = getSharedPreferences("Log", MODE_PRIVATE);
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
        //super.onRestart();
        startActivity(init);
    }


    public void changeMain(View v) {
        email = edEmail.getText().toString();
        password = edPassword.getText().toString();

        progressBar.setVisibility(View.VISIBLE);
        //Log.i("hola",email +"\n" + name + "\n" + password + "\n" + gender + "\n" + age);
        if (attemptLogin(email, password)) {
            new BDLogin().execute();
        }
        //progressBar.setVisibility(View.INVISIBLE);

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
            user = new User();
            user.setEmail(email);
            user.setPassword(password);
            String pass = db.userDAO().searchPasswordByEmail(email);
            if (password.equals(pass)) {
                    /*runOnUiThread(new Runnable() {
                        public void run() {
                            showProgress(true);
                        }
                    });*/

                //iniciar sesion
                SharedPreferences preferences = getSharedPreferences("Log", MODE_PRIVATE);
                SharedPreferences.Editor pref = preferences.edit();
                pref.putBoolean("sesion", true);
                pref.commit();

                User user12 = db.userDAO().searchByEmail(email);

                Intent init = new Intent(this, MainActivity.class);
                init.putExtra("userCurrent",  user12);

                SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
                SharedPreferences.Editor shared = sharedPreferences.edit();
                shared.putString("currentUser", user12.getEmail().toString());
                shared.commit();

                //Log.i("User", "Values: " + db.userDAO().countUsers());
                Log.i("Preferences", sharedPreferences.getString("currentUser", "User"));
                //Log.i("hola",email +"\n" + user12.getName() + "\n" + password + "\n" + user12.getGender() + "\n" + user12.getAge() + "\n" + user12.getBirth());
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

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    class BDLogin extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            checkUser(email, password);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}