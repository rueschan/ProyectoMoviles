package mx.itesm.rueschan.moviles;


import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import static xdroid.toaster.Toaster.toast;

import mx.itesm.rueschan.moviles.BD.DataBase;
import mx.itesm.rueschan.moviles.EntidadesBD.Item;
import mx.itesm.rueschan.moviles.EntidadesBD.Outfit;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritosFragment extends Fragment
{
    private RecyclerView recyclerView;
    // Arreglos para el adaptador
    private int[] arrIDs;
    private String[] arrNames;
    private int coatID;
    private Bitmap[] arrCoats;
    private int upperID;
    private Bitmap[] arrUppers;
    private int bottomId;
    private Bitmap[] arrBottoms;
    private int shoeID;
    private Bitmap[] arrShoes;

    public static int numSize;
    public static String errorMessage;

    public FavoritosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("FavoritosFragment", "Enter");

        View v =  inflater.inflate(R.layout.fragment_favoritos_list, container, false);

        recyclerView = v.findViewById(R.id.rvFavOutfit);
//        AdaptadorRV adaptador = new AdaptadorRV(new int[]{}, new String[]{}, null,null, null, null);
//        System.out.println(">>> " + recyclerView.toString());
//        recyclerView.setAdapter(adaptador);
        FavoritosFragment.ControllerAdapter adapter = new FavoritosFragment.ControllerAdapter(
                new int[]{},
                new String[]{},
                new Bitmap[]{},
                new Bitmap[]{},
                new Bitmap[]{},
                new Bitmap[]{}
                );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }


    @Override
    public void onStart() {
        new BDFavoritos().execute();
        super.onStart();
    }


    private void cargarDatos() {
        // BD
        DataBase bd = DataBase.getInstance(getContext());
        int numOutfits = bd.outfitDAO().countOutfits();
        Log.i("FavoritosFragment", "Cargar Datos :: Registros: " + numOutfits);
        List<Outfit> outfits = bd.outfitDAO().readAll();

        // Crea los arreglos para el adaptador
        arrIDs = new int[numOutfits];
        arrNames = new String[numOutfits];
        arrCoats = new Bitmap[numOutfits];
        arrUppers = new Bitmap[numOutfits];
        arrBottoms = new Bitmap[numOutfits];
        arrShoes = new Bitmap[numOutfits];

        Outfit temp;
        Item itemTemp;
        // Procesa cada registro
        for (int i = 0; i< outfits.size(); i++) {
            temp = outfits.get(i);

            arrIDs[i] = temp.getId();
            arrNames[i] = temp.getName();

            // Leer id y buscar foto de coat
            coatID = temp.getCoatID();
            itemTemp = bd.itemDAO().getItemById(coatID).get(0);
            arrCoats[i] = decodificarImagen(itemTemp);

            // Leer id y buscar foto de coat
            upperID = temp.getUpperID();
            itemTemp = bd.itemDAO().getItemById(upperID).get(0);
            arrUppers[i] = decodificarImagen(itemTemp);

            // Leer id y buscar foto de coat
            bottomId = temp.getBottomID();
            itemTemp = bd.itemDAO().getItemById(bottomId).get(0);
            arrBottoms[i] = decodificarImagen(itemTemp);

            // Leer id y buscar foto de coat
            shoeID = temp.getShoesID();
            itemTemp = bd.itemDAO().getItemById(shoeID).get(0);
            arrShoes[i] = decodificarImagen(itemTemp);

            Log.i("BD (FavoritosFragment)", temp.toString());
        }

        DataBase.destroyInstance();
    }



//     Crea el bitmap a partir del arreglo de bytes
    @NonNull
    private Bitmap decodificarImagen(Item item) {
        Bitmap bm = null;

        try {
            InputStream ent = getResources().getAssets().open("temp.png");
            bm = BitmapFactory.decodeStream(ent);
        } catch (IOException e) {
            Log.i("BD (FavoritosFragment)", "Error: " + e.getMessage());
        }
        int width = 128;
        int height = 128;
        Bitmap.Config configBmp = Bitmap.Config.valueOf(bm.getConfig().name());
        Bitmap bitmap_tmp = Bitmap.createBitmap(width, height, configBmp);

        ByteBuffer buffer = ByteBuffer.wrap(item.getFoto());

        bitmap_tmp.copyPixelsFromBuffer(buffer);
        Log.i("FavoritosFragment", "Image Decoded: " + bitmap_tmp);
        return bitmap_tmp;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private FloatingActionButton btn;

        private TextView outfitName;

        private ImageView coatIv;
        private ImageView upperIv;
        private ImageView bottomIv;
        private ImageView shoesIv;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.outfit_card, parent, false));

            outfitName = itemView.findViewById(R.id.outfitName);

            coatIv = itemView.findViewById(R.id.coatImg);
            upperIv = itemView.findViewById(R.id.upperImg);
            bottomIv = itemView.findViewById(R.id.bottomImg);
            shoesIv = itemView.findViewById(R.id.shoesImg);

            btn = itemView.findViewById(R.id.favBtn);


        }

    }

    public static class ControllerAdapter extends RecyclerView.Adapter<FavoritosFragment.ViewHolder> {

        private static int SIZE;
        private int[] arrIDs;
        private String[] arrNames;
        private Bitmap[] arrCoats;
        private Bitmap[] arrUppers;
        private Bitmap[] arrBottoms;
        private Bitmap[] arrShoes;

        public ControllerAdapter(int[] ids, String[] names, Bitmap[] coatIDs, Bitmap[] upperIDs, Bitmap[] bottomIDs, Bitmap[] shoesIDs) {
            this.arrIDs = ids;
            this.arrNames = names;
            this.arrCoats = coatIDs;
            this.arrUppers = upperIDs;
            this.arrBottoms = bottomIDs;
            this.arrShoes = shoesIDs;
        }

        public void setDatos(int[] ids, String[] names, Bitmap[] coatIDs, Bitmap[] upperIDs, Bitmap[] bottomIDs, Bitmap[] shoesIDs) {
            this.arrIDs = ids;
            this.arrNames = names;
            this.arrCoats = coatIDs;
            this.arrUppers = upperIDs;
            this.arrBottoms = bottomIDs;
            this.arrShoes = shoesIDs;

            try {
                SIZE = arrIDs.length;
            } catch (NullPointerException e) {
                Log.i("FavoritosFragment", "ControllerAdapter: Not existant data");
                SIZE = 0;
            }
        }

        @NonNull
        @Override
        public FavoritosFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new FavoritosFragment.ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull FavoritosFragment.ViewHolder holder, int position) {

            if(arrNames.length > 0) {
                holder.outfitName.setText(arrNames[position]);

                holder.coatIv.setImageBitmap(arrCoats[position]);
                holder.upperIv.setImageBitmap(arrUppers[position]);
                holder.bottomIv.setImageBitmap(arrBottoms[position]);
                holder.shoesIv.setImageBitmap(arrShoes[position]);
            }
        }

        @Override
        public int getItemCount() {
            return SIZE;
        }
    }

    // Para cargar los datos en segundo plano
    private class BDFavoritos extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            cargarDatos();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Nuevos datos para el adaptador
            FavoritosFragment.ControllerAdapter adapt = (FavoritosFragment.ControllerAdapter)recyclerView.getAdapter();
            adapt.setDatos(arrIDs, arrNames, arrCoats, arrUppers, arrBottoms, arrShoes);
            adapt.notifyDataSetChanged();
        }
    }
}
