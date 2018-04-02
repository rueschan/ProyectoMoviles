package mx.itesm.rueschan.moviles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.nio.ByteBuffer;

import mx.itesm.rueschan.moviles.EntidadesBD.Item;
import mx.itesm.rueschan.moviles.BD.DataBase;

public class TakePhoto extends AppCompatActivity {

    public static final int SOLICITA_CAMARA = 500;

    private Bitmap bmNew;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        imageView = findViewById(R.id.imgtaken);

        Button btn = findViewById(R.id.tomarFoto);
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

        }
    }

    private void grabarDatos() {

        Item bd = new Item();
        bd.setFoto(codificarImagen());
        bd.setColor("Azul");
        bd.setTipo(ClosetFragment.clicked);

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