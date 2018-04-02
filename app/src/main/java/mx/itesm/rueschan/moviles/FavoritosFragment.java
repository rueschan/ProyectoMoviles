package mx.itesm.rueschan.moviles;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import mx.itesm.rueschan.moviles.BD.DataBase;
import mx.itesm.rueschan.moviles.EntidadesBD.Item;
import mx.itesm.rueschan.moviles.EntidadesBD.Outfit;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritosFragment extends Fragment
{
    private RecyclerView favOutfitList;
    // Arreglos para el adaptador
    private String[] arrIDs;
    private String[] arrNames;
    private int coatID;
    private Bitmap[] arrCoats;
    private int upperID;
    private Bitmap[] arrUppers;
    private int bottomId;
    private Bitmap[] arrBottoms;
    private int shoeID;
    private Bitmap[] arrShoes;
    private Item itemTemp;

    public FavoritosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_favoritos_list, container, false);
        // Adaptador de prueba vacío
        favOutfitList = v.findViewById(R.id.rvFavOutfit);
        AdaptadorRV adaptador = new AdaptadorRV(new String[]{}, new String[]{}, null,null, null, null);
        System.out.println(">>> " + favOutfitList.toString());
        favOutfitList.setAdapter(adaptador);
        favOutfitList.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();

        new mx.itesm.rueschan.moviles.FavoritosFragment.BDOutfit().execute();
    }


    private void cargarDatos() {
        // BD
        DataBase bd = DataBase.getInstance(getContext());
        int numOutfits = bd.outfitDAO().countOutfits();
        Log.i("cargarDatos", "Registros: " + numOutfits);
        List<Outfit> outfits = bd.outfitDAO().readAll();
        // Crea los arreglos para el adaptador
        arrIDs = new String[numOutfits];
        arrNames = new String[numOutfits];
        arrCoats = new Bitmap[numOutfits];

        arrUppers = new Bitmap[numOutfits];
        arrBottoms = new Bitmap[numOutfits];
        arrShoes = new Bitmap[numOutfits];
        // Procesa cada registro
        for (int i = 0; i< outfits.size(); i++) {
            Outfit temp = outfits.get(i);
            arrNames[i] = temp.getName()+"";

            // Leer id y buscar foto de coat
            coatID = temp.getCoatID();
            itemTemp = bd.itemDAO().getItemById(upperID).get(0);
            arrUppers[i] = decodificarImagen(itemTemp);

            // Leer id y buscar foto de coat
            upperID = temp.getUpperID();
            itemTemp = bd.itemDAO().getItemById(upperID).get(0);
            arrUppers[i] = decodificarImagen(itemTemp);

            // Leer id y buscar foto de coat
            bottomId = temp.getBottomID();
            itemTemp = bd.itemDAO().getItemById(upperID).get(0);
            arrUppers[i] = decodificarImagen(itemTemp);

            // Leer id y buscar foto de coat
            shoeID = temp.getShoesID();
            itemTemp = bd.itemDAO().getItemById(upperID).get(0);
            arrUppers[i] = decodificarImagen(itemTemp);

            Log.i("BD", temp.toString());
        }

        DataBase.destroyInstance();
    }

//     Crea el bitmap a partir del arreglo de bytes
    @NonNull
    private Bitmap decodificarImagen(Item item) {

        Bitmap bm = null;
        try {
            InputStream ent = getActivity().getResources().getAssets().open("temp.png");
            bm = BitmapFactory.decodeStream(ent);
        } catch (IOException e) {
            Log.i("cargaBD", "Error: " + e.getMessage());
        }
        int width = 128;
        int height = 128;
        Bitmap.Config configBmp = Bitmap.Config.valueOf(bm.getConfig().name());
        Bitmap bitmap_tmp = Bitmap.createBitmap(width, height, configBmp);

        ByteBuffer buffer = ByteBuffer.wrap(item.getFoto());

        bitmap_tmp.copyPixelsFromBuffer(buffer);
        return bitmap_tmp;
    }

    // Método de prueba
//    private void cargarDatosTest() {
//        int numOutfits = 3;
//        Log.i("cargarDatos", "Registros: " + numOutfits);
//        // Crea los arreglos para el adaptador
//        String[] names = {"Outfit Uno", "Outfit Dos", "Outfit Tres"};
//        arrNames = names;
//    }

    // Para cargar los datos en segundo plano
    class BDOutfit extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
//            cargarDatosTest();
            cargarDatos();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Nuevos datos para el adaptador
            AdaptadorRV adaptador = (AdaptadorRV) favOutfitList.getAdapter();
            adaptador.setDatos(arrIDs);
            adaptador.notifyDataSetChanged();
        }
    }
}
