package mx.itesm.rueschan.moviles;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import mx.itesm.rueschan.moviles.BD.DataBase;
import mx.itesm.rueschan.moviles.EntidadesBD.User;

public class PreferencesAct extends AppCompatActivity {

    private User user;
    private String color;
    private int old_iv;
    private int primera;

    public ImageView getIvFavorite() {
        return ivFavorite;
    }

    public void setIvFavorite(ImageView ivFavorite) {
        this.ivFavorite = ivFavorite;
    }

    private ImageView ivFavorite;


    public String getColor() {
        return color;
    }

    public int getOld_iv() {
        return old_iv;
    }

    public void setOld_iv(int old_iv) {
        this.old_iv = old_iv;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        user = (User) getIntent().getSerializableExtra("user");
        String from = getIntent().getStringExtra("from");

        if (from.equalsIgnoreCase("MainAct")){
            ivFavorite = findViewById(traducirColorTextView(user.getColor()));
            ivFavorite.setScaleX(ivFavorite.getScaleX() + 0.2f);
            ivFavorite.setScaleY(ivFavorite.getScaleX() + 0.2f);
            setColor(user.getColor());
            Toast.makeText(this, "Actual favorite color:  " + traducirColor(user.getColor()),Toast.LENGTH_LONG).show();
            primera = 0;
        }
    }

    public void selectColor(View v) {
        String color = "";
        ImageView iv = findViewById(v.getId());
        ImageView old_iv;

        if (getIntent().getStringExtra("from").equalsIgnoreCase("MainAct") && primera == 0){
            old_iv = getIvFavorite();
            primera = 1;
        }
        else{
            old_iv = findViewById(getOld_iv());
        }


        if (old_iv != null) {
            if(old_iv.getScaleY() > 1.0f && old_iv.getScaleX() > 1.0f){
                old_iv.setScaleX(1.0f);
                old_iv.setScaleY(1.0f);
            }
        }

        if(iv.getScaleX() <= 1.0f && iv.getScaleY() <= 1.0f) {
            iv.setScaleX(iv.getScaleX() + 0.2f);
            iv.setScaleY(iv.getScaleY() + 0.2f);
        }

        setOld_iv(v.getId());

        switch (v.getId()) {
            case R.id.iv_negro:
                color = "negro";
                break;
            case R.id.iv_blanco:
                color = "blanco";
                break;
            case R.id.iv_gris:
                color = "gris";
                break;
            case R.id.iv_ama_claro:
                color = "amarillo_claro";
                break;
            case R.id.iv_ama_osc:
                color = "amarillo_osc";
                break;
            case R.id.iv_amarillo:
                color = "amarillo";
                break;
            case R.id.iv_rojo_osc:
                color = "rojo_osc";
                break;
            case R.id.iv_rojo_claro:
                color = "rojo_claro";
                break;
            case R.id.iv_rojo:
                color = "rojo";
                break;
            case R.id.iv_verde_osc:
                color = "verde_osc";
                break;
            case R.id.iv_verde:
                color = "verde";
                break;
            case R.id.iv_verde_claro:
                color = "verde_claro";
                break;
            case R.id.iv_azul_osc:
                color = "azul_osc";
                break;
            case R.id.iv_azul_claro:
                color = "azul_claro";
                break;
            case R.id.iv_azul:
                color = "azul";
                break;
            case R.id.iv_morado_osc:
                color = "morado_osc";
                break;
            case R.id.iv_morado:
                color = "morado";
                break;
            case R.id.iv_morado_claro:
                color = "morado_claro";
                break;
            case R.id.iv_cafe_osc:
                color = "cafe_osc";
                break;
            case R.id.iv_cafe:
                color = "cafe";
                break;
            case R.id.iv_cafe_claro:
                color = "cafe_claro";
                break;
        }

        setOld_iv(v.getId());
        setColor(color);
    }

    public void saveUser(){
        DataBase db = DataBase.getInstance(this);
        String from = getIntent().getStringExtra("from");

        if(from.equalsIgnoreCase("SignUpAct")){
            /*Log.i("USER", user.getName() + " " + user.getPassword() + "\n" + user.getGender() + "\n" + user.getAge() + "\n" + user.getBirth()
                    + "\n" + user.getColor());*/
            db.userDAO().insertUsers(user);

            SharedPreferences preferences = getSharedPreferences("Log", MODE_PRIVATE);
            SharedPreferences.Editor pref = preferences.edit();
            pref.putBoolean("sesion", true);
            pref.commit();

            SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
            SharedPreferences.Editor shared = sharedPreferences.edit();
            shared.putString("currentUser", user.getEmail().toString());
            shared.commit();


            /*Intent init = new Intent(this, LoginAct.class);
            init.putExtra("user",  user);
            startActivity(init);*/


        }else if (from.equalsIgnoreCase("MainAct")) {
            db.userDAO().updateColor(getColor(), user.getIdUser());
        }

        DataBase.destroyInstance();

        Intent init = new Intent(this, MainActivity.class);
        init.putExtra("userCurrent",  user);
        startActivity(init);
    }

        /*
        negro = #000000
        blanco = #FFFFFF
        gris = #C3C3C3
        amarillo claro = #FFE600
        amarillo osc = #FFB300
        amarillo = #FFCE00
        rojo osc = #FA1037
        rojo claro = #FF8300
        rojo = #FF4701
        verde osc = #00952D
        verde = #00BC4A
        verde claro = #81F000
        azul_osc = #1046C7
        azu_claro = #0088E1
        azul = #00B9FF
        morado osc = #8D08B5
        morado = #C415C9
        morado claro = #D971FF
        * */



    public void changeMain(View v) {
        user.setColor(getColor());

        if(user.getColor() != null)
            new DBTarea().execute();
        else{
            new AlertDialog.Builder(this)
                    .setMessage("Please select a color.")
                    .setTitle("Sorry")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                        }
                    })
                    .create().show();
        }

        //System.out.println(user.getName() + " " + user.getColor());
        //iniciar sesion
        /*SharedPreferences prefs = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor pref = prefs.edit();
        pref.putBoolean("sesion", true);
        pref.commit();*/

    }

    private int traducirColorTextView(String color) {
        switch (user.getColor()){
            case "negro":
                return findViewById(R.id.iv_negro).getId();
            case "blanco":
                return findViewById(R.id.iv_blanco).getId();
            case "gris":
                return findViewById(R.id.iv_gris).getId();
            case "amarillo_claro":
                return findViewById(R.id.iv_ama_claro).getId();
            case "amarillo_osc":
                return findViewById(R.id.iv_ama_osc).getId();
            case "amarillo":
                return findViewById(R.id.iv_amarillo).getId();
            case "rojo_osc":
                return findViewById(R.id.iv_rojo_osc).getId();
            case "rojo_claro":
                return findViewById(R.id.iv_rojo_claro).getId();
            case "rojo":
                return findViewById(R.id.iv_rojo).getId();
            case "verde_osc":
                return findViewById(R.id.iv_verde_osc).getId();
            case "verde":
                return findViewById(R.id.iv_verde).getId();
            case "verde_claro":
                return findViewById(R.id.iv_verde_claro).getId();
            case "azul_osc":
                return findViewById(R.id.iv_azul_osc).getId();
            case "azul_claro":
                return findViewById(R.id.iv_azul_claro).getId();
            case "azul":
                return findViewById(R.id.iv_azul).getId();
            case "morado_osc":
                return findViewById(R.id.iv_morado_osc).getId();
            case "morado":
                return findViewById(R.id.iv_morado).getId();
            case "morado_claro":
                return findViewById(R.id.iv_morado_claro).getId();
            case "cafe_osc":
                return findViewById(R.id.iv_cafe_osc).getId();
            case "cafe":
                return findViewById(R.id.iv_cafe).getId();
            case "cafe_claro":
                return findViewById(R.id.iv_cafe_claro).getId();
        }
        return 0;
    }

    private String traducirColor(String color) {
        switch (color){
            case "negro":
                return "Black";
            case "blanco":
                return "White";
            case "gris":
                return "Gray";
            case "amarillo_claro":
                return "Light Yellow";
            case "amarillo_osc":
                return "Drak Yellow";
            case "amarillo":
                return "Yellow";
            case "rojo_osc":
                return "Dark Red";
            case "rojo_claro":
                return "Light Red";
            case "rojo":
                return "Red";
            case "verde_osc":
                return "Dark Green";
            case "verde":
                return "Green";
            case "verde_claro":
                return "Light Green";
            case "azul_osc":
                return "Dark Blue";
            case "azul_claro":
                return "Light Blue";
            case "azul":
                return "Blue";
            case "morado_osc":
                return "Dark Purple";
            case "morado":
                return "Purple";
            case "morado_claro":
                return "Light Purple";
            case "cafe_osc":
                return "Dark Brown";
            case "cafe":
                return "Brown";
            case "cafe_claro":
                return "Light Brown";
        }
        return "";
    }


    class DBTarea extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            saveUser();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
