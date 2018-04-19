package mx.itesm.rueschan.moviles;

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

import mx.itesm.rueschan.moviles.BD.DataBase;
import mx.itesm.rueschan.moviles.EntidadesBD.Item;
import mx.itesm.rueschan.moviles.EntidadesBD.Outfit;

public class SugeridosFragment extends Fragment {

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
    //private OnFragmentInteractionListener mListener;

    private final int OUTFITS = 1;

    //verificar cantidad datos
    private String errorMsg = "";
    private int tipoItems[] = {0,0,0,0};
    String item[] = {"Shoes", "Bottom","Top","Coats"};

    public SugeridosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("SugeridosFragment", "Enter");

        View v =  inflater.inflate(R.layout.fragment_sugeridos_list, container, false);
        recyclerView = v.findViewById(R.id.rvSugeridosOutfit);

        SugeridosFragment.ControllerAdapter adapter = new SugeridosFragment.ControllerAdapter(
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

        /*new AlertDialog.Builder(SugeridosFragment.this.getContext())
                .setMessage(errorMsg + "items")
                .setTitle("Sorry")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                })
                .create().show();*/
        //System.out.println("********* " + tipoItems[i])


    @Override
    public void onStart() {
        super.onStart();
        new BDOutfit().execute();
    }

    /* negro = #000000
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
        morado claro = #D971FF*/

    private void cargarDatos() {
        // BD
        DataBase bd = DataBase.getInstance(getContext());
        List<Item> items = bd.itemDAO().getAllItems();

        System.out.println("*********" + items.size());
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).getTipo().equalsIgnoreCase("Shoes")) {
                tipoItems[0]++;
            }if(items.get(i).getTipo().equalsIgnoreCase("Bottom")) {
                tipoItems[1]++;
            }if(items.get(i).getTipo().equalsIgnoreCase("Top")) {
                tipoItems[2]++;
            }if(items.get(i).getTipo().equalsIgnoreCase("Coats")) {
                tipoItems[3]++;
            }
        }

        // Crea los arreglos para el adaptador
        arrIDs = new int[OUTFITS];
        arrNames = new String[OUTFITS];
        arrCoats = new Bitmap[OUTFITS];
        arrUppers = new Bitmap[OUTFITS];
        arrBottoms = new Bitmap[OUTFITS];
        arrShoes = new Bitmap[OUTFITS];

        if(tipoItems[0] > 0 && tipoItems[1] > 0 && tipoItems[2] > 0 && tipoItems[3] > 0){
            // Procesa cada registro
            for (int i = 0; i< OUTFITS; i++) {
                arrIDs[i] = i;
                arrNames[i] = "outfit " + i;

                for (int j = 0; j < items.size(); j++) {
                    Item itemTemp = items.get(j);
                    if (itemTemp.getTipo().equalsIgnoreCase("Shoes")) {
                        // Leer id y buscar foto de shoes
                        /*shoeID = itemTemp.getId();
                        itemTemp = bd.itemDAO().getItemById(shoeID).get(0);*/
                        arrShoes[i] = decodificarImagen(itemTemp);
                    }
                    if (itemTemp.getTipo().equalsIgnoreCase("Bottom")) {
                        // Leer id y buscar foto de bottom
                        /*bottomId = itemTemp.getId();
                        itemTemp = bd.itemDAO().getItemById(bottomId).get(0);*/
                        arrBottoms[i] = decodificarImagen(itemTemp);
                    }
                    if (itemTemp.getTipo().equalsIgnoreCase("Top")) {
                        // Leer id y buscar foto de upper
                        /*upperID = itemTemp.getId();
                        itemTemp = bd.itemDAO().getItemById(upperID).get(0);*/
                        arrUppers[i] = decodificarImagen(itemTemp);
                    }
                    if (itemTemp.getTipo().equalsIgnoreCase("Coats")) {
                        // Leer id y buscar foto de coat
                        /*coatID = itemTemp.getId();
                        itemTemp = bd.itemDAO().getItemById(coatID).get(0);*/
                        arrCoats[i] = decodificarImagen(itemTemp);
                    }
                }
            }
        }

       /* int numToap;
        int numBottom;
        int numCoats;
        int numShoes;*/
        Log.i("SugeridosFragment", "Cargar Datos :: Registros: " + items.size());

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
            Log.i("BD (SugeridosFragment)", "Error: " + e.getMessage());
        }
        int width = 128;
        int height = 128;
        Bitmap.Config configBmp = Bitmap.Config.valueOf(bm.getConfig().name());
        Bitmap bitmap_tmp = Bitmap.createBitmap(width, height, configBmp);

        ByteBuffer buffer = ByteBuffer.wrap(item.getFoto());

        bitmap_tmp.copyPixelsFromBuffer(buffer);
        Log.i("SugeridosFragment", "Image Decoded: " + bitmap_tmp);
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

    public static class ControllerAdapter extends RecyclerView.Adapter<SugeridosFragment.ViewHolder> {

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
                Log.i("SugeridosFragment", "ControllerAdapter: Not existant data");
                SIZE = 0;
            }
        }

        @NonNull
        @Override
        public SugeridosFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SugeridosFragment.ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SugeridosFragment.ViewHolder holder, int position) {

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
            SugeridosFragment.ControllerAdapter adapt = (SugeridosFragment.ControllerAdapter)recyclerView.getAdapter();
            adapt.setDatos(arrIDs, arrNames, arrCoats, arrUppers, arrBottoms, arrShoes);
            adapt.notifyDataSetChanged();
        }
    }
}

