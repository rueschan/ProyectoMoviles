package mx.itesm.rueschan.moviles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

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

        if(v.getId() == (R.id.iv_negro)){
            color = "#000000";
        }else if(v.getId() == (R.id.iv_blanco)){
            color = "#FFFFFF";
        }else if(v.getId() == (R.id.iv_gris)){
            color = "#C3C3C3";
        }else if(v.getId() == (R.id.iv_ama_claro)){
            color = "#FFE600";
        }else if(v.getId() == (R.id.iv_ama_osc)){
            color = "#FFB300";
        }else if(v.getId() == (R.id.iv_amarillo)){
            color = "#FFCE00";
        }else if(v.getId() == (R.id.iv_rojo_osc)){
            color = "#FA1037";
        }else if(v.getId() == (R.id.iv_rojo_claro)){
            color = "#FF8300";
        }else if(v.getId() == (R.id.iv_rojo)){
            color = "#FF4701";
        }else if(v.getId() == (R.id.iv_verde_osc)){
            color = "#00952D";
        }else if(v.getId() == (R.id.iv_verde)){
            color = "#00BC4A";
        }else if(v.getId() == (R.id.iv_verde_claro)){
            color = "#81F000";
        }else if(v.getId() == (R.id.iv_azul_osc)){
            color = "#1046C7";
        }else if(v.getId() == (R.id.iv_azul_claro)){
            color = "#0088E1";
        }else if(v.getId() == (R.id.iv_azul)){
            color = "#00B9FF";
        }else if(v.getId() == (R.id.iv_morado_osc)){
            color = "#8D08B5";
        }else if(v.getId() == (R.id.iv_morado)){
            color = "#C415C9";
        }else if(v.getId() == (R.id.iv_morado_claro)){
            color = "#D971FF";
        }

        setOld_iv(v.getId());
        setColor(color);

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
        //System.out.println(user.getName() + " " + user.getColor());
        //iniciar sesion
        /*SharedPreferences prefs = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor pref = prefs.edit();
        pref.putBoolean("sesion", true);
        pref.commit();*/

        Intent init = new Intent(this, LoginAct.class);
        startActivity(init);
    }


}
