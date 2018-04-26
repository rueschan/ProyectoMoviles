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

import mx.itesm.rueschan.moviles.BD.DataBase;
import mx.itesm.rueschan.moviles.EntidadesBD.User;

public class PreferencesAct extends AppCompatActivity {

    private User user;
    private String color;
    private int old_iv;

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

    }

    public void selectColor(View v) {
        String color = "";
        ImageView iv = findViewById(v.getId());

        ImageView old_iv = findViewById(getOld_iv());

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

    private void saveUser(){
        DataBase db = DataBase.getInstance(this);
        String from = getIntent().getStringExtra("from");

        if(from.equalsIgnoreCase("SignUpAct")){
            //Database
            db.userDAO().insertUsers(user);
            DataBase.destroyInstance();

            Intent init = new Intent(this, LoginAct.class);
            startActivity(init);
        }
        else if (from.equalsIgnoreCase("MainAct")){
            db.userDAO().updateColor(getColor(), user.getIdUser());

            Intent init = new Intent(this, MainActivity.class);
            startActivity(init);
        }

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
