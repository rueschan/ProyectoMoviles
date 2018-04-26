package mx.itesm.rueschan.moviles;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.nio.ByteBuffer;

import mx.itesm.rueschan.moviles.EntidadesBD.Item;
import mx.itesm.rueschan.moviles.BD.DataBase;
import mx.itesm.rueschan.moviles.EntidadesBD.User;

public class TakePhoto extends AppCompatActivity {

    public static final int SOLICITA_CAMARA = 500;

    //Seleccionar Color
    private String color;
    private int old_iv;


    private Bitmap bmNew;
    private TextView tvType;
    private ImageView imageView;

    private Spinner eventsList;
    String events[]= {"Sports","Streetwear","Casual","Business Casual","Business","Black Tie"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        Intent intFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intFoto.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intFoto, SOLICITA_CAMARA);
        }

        imageView = findViewById(R.id.imgtaken);
        tvType = findViewById(R.id.tv_tipo);

        //lista tipo de prenda

        eventsList = findViewById(R.id.list_use);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,events);
        adapter.setDropDownViewResource(R.layout.spinner);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //eventsList.setPrompt("Dress Code");
        eventsList.setAdapter(adapter);
        eventsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(getResources().getColor(R.color.colorPrimary)); //Change selected text color
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       // Button btn = findViewById(R.id.tomarFoto);
        ImageView btn = findViewById(R.id.imgPhoto);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre la app para tomar la foto
                Intent intFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intFoto.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intFoto, SOLICITA_CAMARA);
                }

            }
        });


        //Button btnSave = findViewById(R.id.guardarButton);
        ImageButton btnSave = findViewById(R.id.imgSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getColor() != null){
                        new AlertDialog.Builder(TakePhoto.this)
                                .setMessage("Are you sure you want to save this item?")
                                .setTitle("Ready to save")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button
                                        new BDTarea().execute();
                                        finish();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button
                                    }
                                })
                                .create().show();
                    }else  {
                        new AlertDialog.Builder(TakePhoto.this)
                                .setMessage("Some data is missing")
                                .setTitle("Sorry")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button
                                    }
                                })
                                .create().show();
                    }
                }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SOLICITA_CAMARA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bm = (Bitmap) extras.get("data");
            bmNew = Bitmap.createScaledBitmap(bm,128,128,false);
            imageView.setImageBitmap(bm);
            tvType.setText(ClosetFragment.clicked);
        }else if(requestCode == SOLICITA_CAMARA && resultCode ==  RESULT_CANCELED){
            finish();
        }
    }

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

        /*
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
        }*/

        if(v.getId() == (R.id.iv_negro)){
            color = "negro";
        }else if(v.getId() == (R.id.iv_blanco)){
            color = "blanco";
        }else if(v.getId() == (R.id.iv_gris)){
            color = "gris";
        }else if(v.getId() == (R.id.iv_ama_claro)){
            color = "amarillo_claro";
        }else if(v.getId() == (R.id.iv_ama_osc)){
            color = "amarillo_osc";
        }else if(v.getId() == (R.id.iv_amarillo)){
            color = "amarillo";
        }else if(v.getId() == (R.id.iv_rojo_osc)){
            color = "rojo_osc";
        }else if(v.getId() == (R.id.iv_rojo_claro)){
            color = "rojo_claro";
        }else if(v.getId() == (R.id.iv_rojo)){
            color = "rojo";
        }else if(v.getId() == (R.id.iv_verde_osc)){
            color = "verde_osc";
        }else if(v.getId() == (R.id.iv_verde)){
            color = "verde";
        }else if(v.getId() == (R.id.iv_verde_claro)){
            color = "verde_claro";
        }else if(v.getId() == (R.id.iv_azul_osc)){
            color = "azul_osc";
        }else if(v.getId() == (R.id.iv_azul_claro)){
            color = "azul_claro";
        }else if(v.getId() == (R.id.iv_azul)){
            color = "azul";
        }else if(v.getId() == (R.id.iv_morado_osc)){
            color = "morado_osc";
        }else if(v.getId() == (R.id.iv_morado)){
            color = "morado";
        }else if(v.getId() == (R.id.iv_morado_claro)) {
            color = "morado_claro";
        }


        setOld_iv(v.getId());
        setColor(color);

    }

    private void grabarDatos() {

        Item bd = new Item();
        bd.setFoto(codificarImagen());
        bd.setColor(getColor());
        bd.setTipo(ClosetFragment.clicked);
        bd.setEvento(eventsList.getSelectedItem().toString());
        DataBase dataBase = DataBase.getInstance(this);
        bd.setUserID(MainActivity.currentUser.getIdUser());
        dataBase.itemDAO().insertar(bd);

        System.out.println(dataBase.itemDAO().countByTypeAndUserID(ClosetFragment.clicked, MainActivity.currentUser.getIdUser()));
        DataBase.destroyInstance();

    }

    private byte[] codificarImagen() {
        Bitmap bm = bmNew;
        int longitud = bm.getRowBytes() * bm.getHeight();
        ByteBuffer byteBuffer = ByteBuffer.allocate(longitud);
        bm.copyPixelsToBuffer(byteBuffer);
        byte[] byteArray = byteBuffer.array();
        return byteArray;
    }

    public void onBackPressed() {
        /*Intent intent = new Intent(this, ImagesActivity.class);
       startActivity(intent);*/
        finish();
    }

    class BDTarea extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            grabarDatos();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("onPost", "Dato grabado ********************");
        }
    }


}