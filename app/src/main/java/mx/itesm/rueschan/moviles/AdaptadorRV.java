package mx.itesm.rueschan.moviles;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
    private int[] arrIDs;
    private String[] arrNames;
    private Bitmap[] arrCoats;
    private Bitmap[] arrUppers;
    private Bitmap[] arrBottoms;
    private Bitmap[] arrShoes;

    private Bitmap coatToSave;
    private Bitmap upperToSave;
    private Bitmap bottomToSave;
    private Bitmap shoesToSave;

    public AdaptadorRV(int[] ids, String[] names, Bitmap[] coatIDs, Bitmap[] upperIDs, Bitmap[] bottomIDs, Bitmap[] shoesIDs) {
        arrIDs = ids;
        arrNames = names;
        arrCoats = coatIDs;
        arrUppers = upperIDs;
        arrBottoms = bottomIDs;
        arrShoes = shoesIDs;

        System.out.println(" +++++++++++++++++++++ Crear clase");
        for (int i = 0; i < ids.length; i++) {
            System.out.println("ADAPTADOR >>>>> " + arrIDs[i] + " " + arrNames[i] + " " + arrCoats[i] + " " + arrUppers[i] + " " + arrBottoms[i] + " " + arrShoes[i]);
        }
    }

    public void setDatos(int[] ids, String[] names, Bitmap[] coatIDs, Bitmap[] upperIDs, Bitmap[] bottomIDs, Bitmap[] shoesIDs) {
        arrIDs = ids;
        arrNames = names;
        arrCoats = coatIDs;
        arrUppers = upperIDs;
        arrBottoms = bottomIDs;
        arrShoes = shoesIDs;

        System.out.println("++++++++++++++++++++++ setDatos");
        System.out.println(arrIDs.length);
        System.out.println(arrNames.length);
        System.out.println(arrCoats.length);
        System.out.println(arrUppers.length);

        for (int i = 0; i < ids.length; i++) {
            System.out.println("ADAPTADOR >>>>> " + arrIDs[i] + " " + arrNames[i] + " " + arrCoats[i] + " " + arrUppers[i] + " " + arrBottoms[i] + " " + arrShoes[i]);
        }
    }

    @Override
    public Vista onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vista(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(Vista holder, final int position) {

        holder.outfitName.setText(arrNames[position]);

        holder.coatIv.setImageBitmap(arrCoats[position]);
        holder.upperIv.setImageBitmap(arrUppers[position]);
        holder.bottomIv.setImageBitmap(arrBottoms[position]);
        holder.shoesIv.setImageBitmap(arrShoes[position]);

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
        private FloatingActionButton btn;

        private TextView outfitName;

        private ImageView coatIv;
        private ImageView upperIv;
        private ImageView bottomIv;
        private ImageView shoesIv;

        public Vista(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.outfit_card, parent, false));

            outfitName = itemView.findViewById(R.id.outfitName);

            coatIv = itemView.findViewById(R.id.coatImg);
            upperIv = itemView.findViewById(R.id.upperImg);
            bottomIv = itemView.findViewById(R.id.bottomImg);
            shoesIv = itemView.findViewById(R.id.shoesImg);

            btn = itemView.findViewById(R.id.favBtn);
        }
    }

    private void grabarDatos() {

        DataBase dataBase = DataBase.getInstance(vista.getContext());

        Outfit outfit = new Outfit();
        Item coat = dataBase.itemDAO().getItemByPhoto(codificarImagen(coatToSave)).get(0);
        Item upper = dataBase.itemDAO().getItemByPhoto(codificarImagen(upperToSave)).get(0);
        Item bottom = dataBase.itemDAO().getItemByPhoto(codificarImagen(bottomToSave)).get(0);
        Item shoe = dataBase.itemDAO().getItemByPhoto(codificarImagen(shoesToSave)).get(0);

        outfit.setName("Outfit");

        outfit.setCoatID(coat.getId());
        outfit.setUpperID(upper.getId());
        outfit.setBottomID(bottom.getId());
        outfit.setShoesID(shoe.getId());

        if (dataBase != null) {
            dataBase.outfitDAO().insert(outfit);

            System.out.println("Quantity of outfits: " + dataBase.outfitDAO().countOutfits());
            Log.i("DB", "Saved Data: " + outfit.toString());
            DataBase.destroyInstance();
        } else {
            Log.e("DB", "DB points null");
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