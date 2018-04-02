package mx.itesm.rueschan.moviles;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.nio.ByteBuffer;

import mx.itesm.rueschan.moviles.BD.DataBase;
import mx.itesm.rueschan.moviles.EntidadesBD.Item;
import mx.itesm.rueschan.moviles.EntidadesBD.Outfit;


/**
 * Created by roberto on 04/03/18.
 */

public class AdaptadorRV extends RecyclerView.Adapter<AdaptadorRV.Vista> {

    ViewGroup vista;

    // DATOS
    private String[] arrIDs;
    private String[] arrNames;
    private Bitmap[] arrCoats;
    private Bitmap[] arrUppers;
    private Bitmap[] arrBottoms;
    private Bitmap[] arrShoes;

    private Bitmap coatToSave;
    private Bitmap upperToSave;
    private Bitmap bottomToSave;
    private Bitmap shoesToSave;

    public AdaptadorRV(String[] ids, String[] names, Bitmap[] coatIDs, Bitmap[] upperIDs, Bitmap[] bottomIDs, Bitmap[] shoesIDs) {
        arrIDs = ids;
        arrNames = names;
        arrCoats = coatIDs;
        arrUppers = upperIDs;
        arrBottoms = bottomIDs;
        arrShoes = shoesIDs;
    }

    public AdaptadorRV(String[] outfitIDs) {
        arrIDs = outfitIDs;
    }

    public void setDatos(String[] ids, String[] names, Bitmap[] coatIDs, Bitmap[] upperIDs, Bitmap[] bottomIDs, Bitmap[] shoesIDs) {
        arrIDs = ids;
        arrNames = names;
        arrCoats = coatIDs;
        arrUppers = upperIDs;
        arrBottoms = bottomIDs;
        arrShoes = shoesIDs;
    }

    public void setDatos(String[] outfitIDs) {
        arrIDs = outfitIDs;
    }

    @Override
    public Vista onCreateViewHolder(ViewGroup parent, int viewType) {
        vista = parent;
        CardView tarjeta = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.outfit_card, parent, false);

        return new Vista(tarjeta);  // Un renglón de la lista
    }

    @Override
    public void onBindViewHolder(Vista holder, final int position) {
        CardView card = holder.tarjeta;
        TextView outfitName = card.findViewById(R.id.outfitName);
        ImageView coat = card.findViewById(R.id.coatImg);
        ImageView upper = card.findViewById(R.id.upperImg);
        ImageView bottom = card.findViewById(R.id.bottomImg);
        ImageView shoes = card.findViewById(R.id.shoesImg);

        coat.setImageBitmap(arrCoats[position]);
        upper.setImageBitmap(arrUppers[position]);
        bottom.setImageBitmap(arrBottoms[position]);
        shoes.setImageBitmap(arrShoes[position]);

        outfitName.setText(arrNames[position]);

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coatToSave = arrCoats[position];
                upperToSave = arrUppers[position];
                bottomToSave = arrBottoms[position];
                shoesToSave = arrShoes[position];

                new BDOutfit().execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (arrIDs != null) {
            return arrIDs.length;
        }
        return 0;
    }

    public class Vista extends RecyclerView.ViewHolder {
        private CardView tarjeta;
        private FloatingActionButton btn;

        public Vista(CardView v) {
            super(v);
            this.tarjeta = v;
            btn = v.findViewById(R.id.favBtn);
        }
    }

    private void grabarDatos() {

        DataBase dataBase = DataBase.getInstance(vista.getContext());

        Outfit outfit = new Outfit();
        Item coat = dataBase.itemDAO().getItemByPhoto(codificarImagen(coatToSave)).get(0);
        Item upper = dataBase.itemDAO().getItemByPhoto(codificarImagen(upperToSave)).get(0);
        Item bottom = dataBase.itemDAO().getItemByPhoto(codificarImagen(bottomToSave)).get(0);
        Item shoe = dataBase.itemDAO().getItemByPhoto(codificarImagen(shoesToSave)).get(0);

        outfit.setCoatID(coat.getId());
        outfit.setUpperID(upper.getId());
        outfit.setBottomID(bottom.getId());
        outfit.setShoesID(shoe.getId());

        if (dataBase != null) {
            dataBase.outfitDAO().insertOutfits(outfit);

            System.out.println(dataBase.outfitDAO().countOutfits());
            DataBase.destroyInstance();
        } else {
            Log.i("ERROR SAVING INTO DB", "DB points null");
        }

    }

    //     Crea el bitmap a partir del arreglo de bytes
    @NonNull
    private byte[] codificarImagen(Bitmap bm) {
        int longitud = bm.getRowBytes() * bm.getHeight();
        ByteBuffer byteBuffer = ByteBuffer.allocate(longitud);
        bm.copyPixelsToBuffer(byteBuffer);
        byte[] byteArray = byteBuffer.array();
        return byteArray;
    }

    // Para cargar los datos en segundo plano
    class BDOutfit extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
//            cargarDatosTest();
            grabarDatos();
            return null;
        }
    }
}