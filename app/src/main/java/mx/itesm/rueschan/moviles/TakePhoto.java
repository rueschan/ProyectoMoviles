package mx.itesm.rueschan.moviles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.nio.ByteBuffer;

import mx.itesm.rueschan.moviles.EntidadesBD.Item;
import mx.itesm.rueschan.moviles.BD.DataBase;

public class TakePhoto extends AppCompatActivity {

    public static final int SOLICITA_CAMARA = 500;

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

        /*Button btn = findViewById(R.id.tomarFoto);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abre la app para tomar la foto
                Intent intFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intFoto.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intFoto, SOLICITA_CAMARA);
                }

            }
        });*/

        Button btnSave = findViewById(R.id.guardarButton);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BDTarea().execute();
                finish();
                //startActivity(new Intent(view.getContext(), ImagesActivity.class));
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

    private void grabarDatos() {

        Item bd = new Item();
        bd.setFoto(codificarImagen());
        bd.setColor("Azul");
        bd.setTipo(ClosetFragment.clicked);
        bd.setEvento(eventsList.getSelectedItem().toString());
        DataBase dataBase = DataBase.getInstance(this);
        dataBase.itemDAO().insertar(bd);

        System.out.println(dataBase.itemDAO().countByType(ClosetFragment.clicked));
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