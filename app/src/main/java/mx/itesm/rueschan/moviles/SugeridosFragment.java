package mx.itesm.rueschan.moviles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import mx.itesm.rueschan.moviles.BD.DataBase;
import mx.itesm.rueschan.moviles.EntidadesBD.Item;

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
    private ArrayList<Item> shoes = new ArrayList<>();
    private ArrayList<Item> top = new ArrayList<>();
    private ArrayList<Item> bottom = new ArrayList<>();
    private ArrayList<Item> coats = new ArrayList<>();
    private String colors[];
    //private OnFragmentInteractionListener mListener;

    private final int OUTFITS = 5;

    //verificar cantidad datos
    private String errorMsg = "";
    String item[] = {"Shoes", "Bottom", "Top", "Coats"};
    private String camafeo[]; //colores cercanos
    //combinar prendas de colores cercanos en el círculo cromático, como por ejemplo el beige y el marrón.

    /*private String circuloCromatico[] = {"#FFE600", "#FFCE00", "#FFB300", "#FF8300", "#FF4701", "#FA1037", "#81F000", "#00BC4A"
            , "#00952D", "#0088E1", "#00B9FF", "#1046C7", "#D971FF", "#C415C9", "#8D08B5"};*/

    private String circuloCromatico[] = {"amarillo_claro","amarillo","amarillo_osc","naranja_claro",
            "naranja", "naranja_osc","rojo_claro","rojo","rojo_osc",
            "morado_claro","morado", "morado_osc","azul_claro","azul","azul_osc",
            "verde_claro","verde","verde_osc"};
    //amarillo, rojo, rojo osc, morado, azul osc, azul calro, verde osc;
    private String contraste[]; //colores opuestos
    private String universales[] = {"blanco", "negro", "gris"};

    /*beige**
      negro = #000000
      blanco = #FFFFFF
      gris = #C3C3C3
      amarillo claro = #FFE600
      amarillo = #FFCE00
      amarillo osc = #FFB300
      rojo claro = #FF8300
      rojo = #FF4701
      rojo osc = #FA1037
      verde claro = #81F000
      verde = #00BC4A
      verde osc = #00952D
      azu_claro = #0088E1
      azul = #00B9FF
      azul_osc = #1046C7
      morado claro = #D971FF
      morado = #C415C9
      morado osc = #8D08B5
        */
    //combinar prendas de colores completamente opuestos en el círculo cromático

    public SugeridosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("SugeridosFragment", "Enter");

        ClosetFragment.origen = ClosetFragment.Origin.SUGERIDOS;

        View v = inflater.inflate(R.layout.fragment_sugeridos_list, container, false);
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


    @Override
    public void onStart() {
        super.onStart();
        new BDItem().execute();
    }

    private void crearOutfit(DataBase bd) {

        if (shoes.size() > 0 && bottom.size() > 0 && top.size() > 0 && coats.size() > 0) {
            HashMap<Item, ArrayList<ArrayList<Item>>> combinacionesContraste = crearCombinacionesContraste(shoes, bottom, top, coats);
            HashMap<Item, ArrayList<ArrayList<Item>>> combinacionesContinuo = crearCombinacionesContinuo(shoes, bottom, top, coats);


            seleccionarOutfit(combinacionesContraste, combinacionesContinuo, bd);
        } else {
            String errorMsg = "You don't have these items:\n";
            if (shoes.size() == 0) {
                errorMsg += "- Shoes\n";
            }

            if (bottom.size() == 0) {
                errorMsg += "- Bottom\n";
            }

            if (top.size() == 0) {
                errorMsg += "- Top\n";
            }

            if (coats.size() == 0) {
                errorMsg += "- Coats";
            }
                /*
                for (int i = 0; i < tipoItems.length; i++) {
                    if (tipoItems[i] < 1)
                        errorMsg += "- " + item[i] + "\n";
                }*/

                MyAlertDialog dialog = new MyAlertDialog(errorMsg);
                dialog.show(getActivity().getFragmentManager(), "Sample Fragment");
        }


    }


    private void seleccionarOutfitContraste(HashMap<Item, ArrayList<ArrayList<Item>>> combinaciones, DataBase bd) {

        // Crea los arreglos para el adaptador
        /*arrIDs = new int[OUTFITS];
        arrNames = new String[OUTFITS];
        arrCoats = new Bitmap[OUTFITS];
        arrUppers = new Bitmap[OUTFITS];
        arrBottoms = new Bitmap[OUTFITS];
        arrShoes = new Bitmap[OUTFITS];*/

        arrIDs = new int[combinaciones.size()];
        arrNames = new String[combinaciones.size()];
        arrCoats = new Bitmap[combinaciones.size()];
        arrUppers = new Bitmap[combinaciones.size()];
        arrBottoms = new Bitmap[combinaciones.size()];
        arrShoes = new Bitmap[combinaciones.size()];

        // Procesa cada registro
        Iterator it = combinaciones.entrySet().iterator();

        //Selecciona una de las combinaciones al azar
        Random rnd = new Random();
        int indexShoes, indexBottom, indexCoats;

        //for (int i = 0; i < OUTFITS; i++) {

        int i = 0;

        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            System.out.println("Key is: " + me.getKey() + "\nValue is: " + me.getValue().toString());
            ArrayList<ArrayList<Item>> itemTemp = (ArrayList<ArrayList<Item>>) me.getValue();
            //System.out.println("Shoes " + itemTemp.get(0).get(0));
            Item key = (Item) me.getKey();
            //String color[] = new String[OUTFITS];

            indexShoes = rnd.nextInt(itemTemp.get(0).size());
            indexBottom = rnd.nextInt(itemTemp.get(1).size());
            indexCoats = rnd.nextInt(itemTemp.get(2).size());
            //System.out.println(itemTemp.get(0).get(indexShoes));

            arrIDs[i] = bd.outfitDAO().countOutfits() + (i+1);
            arrNames[i] = "Outfit " + (arrIDs[i]) ;

            // Leer id y buscar foto de Top
            arrUppers[i] = decodificarImagen(key);
            // Leer id y buscar foto de shoes
            arrShoes[i] = decodificarImagen(itemTemp.get(0).get(indexShoes));
            // Leer id y buscar foto de bottom
            arrBottoms[i] = decodificarImagen(itemTemp.get(1).get(indexBottom));
            // Leer id y buscar foto de coats
            arrCoats[i] = decodificarImagen(itemTemp.get(2).get(indexCoats));

            i++;

            //}

        }
    }

    private void seleccionarOutfit(HashMap<Item, ArrayList<ArrayList<Item>>> combinaciones, HashMap<Item, ArrayList<ArrayList<Item>>> combinacionesContinuo, DataBase bd) {

        // Crea los arreglos para el adaptador
        /*arrIDs = new int[OUTFITS];
        arrNames = new String[OUTFITS];
        arrCoats = new Bitmap[OUTFITS];
        arrUppers = new Bitmap[OUTFITS];
        arrBottoms = new Bitmap[OUTFITS];
        arrShoes = new Bitmap[OUTFITS];*/

        arrIDs = new int[combinaciones.size()];
        arrNames = new String[combinaciones.size() + combinacionesContinuo.size()];
        arrCoats = new Bitmap[combinaciones.size() + combinacionesContinuo.size()];
        arrUppers = new Bitmap[combinaciones.size() + combinacionesContinuo.size()];
        arrBottoms = new Bitmap[combinaciones.size() + combinacionesContinuo.size()];
        arrShoes = new Bitmap[combinaciones.size() + combinacionesContinuo.size()];

        // Procesa cada registro
        Iterator it = combinaciones.entrySet().iterator();

        //Selecciona una de las combinaciones al azar
        Random rnd = new Random();
        int indexShoes, indexBottom, indexCoats;
        //for (int i = 0; i < OUTFITS; i++) {

        int i = 0;

            while (it.hasNext()) {
                Map.Entry me = (Map.Entry) it.next();
                System.out.println("Key is: " + me.getKey() + "\nValue is: " + me.getValue().toString());
                ArrayList<ArrayList<Item>> itemTemp = (ArrayList<ArrayList<Item>>) me.getValue();
                //System.out.println("Shoes " + itemTemp.get(0).get(0));
                Item key = (Item) me.getKey();
                //String color[] = new String[OUTFITS];

                indexShoes = rnd.nextInt(itemTemp.get(0).size());
                indexBottom = rnd.nextInt(itemTemp.get(1).size());
                indexCoats = rnd.nextInt(itemTemp.get(2).size());
                //System.out.println(itemTemp.get(0).get(indexShoes));

                arrIDs[i] = bd.outfitDAO().countOutfits() + (i+1);
                arrNames[i] = "Outfit " + (arrIDs[i]) ;

                // Leer id y buscar foto de Top
                arrUppers[i] = decodificarImagen(key);
                // Leer id y buscar foto de shoes
                arrShoes[i] = decodificarImagen(itemTemp.get(0).get(indexShoes));
                // Leer id y buscar foto de bottom
                arrBottoms[i] = decodificarImagen(itemTemp.get(1).get(indexBottom));
                // Leer id y buscar foto de coats
                arrCoats[i] = decodificarImagen(itemTemp.get(2).get(indexCoats));

                i++;
            //}
        }

        it = combinacionesContinuo.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            System.out.println("Key is: " + me.getKey() + "\nValue is: " + me.getValue().toString());
            ArrayList<ArrayList<Item>> itemTemp = (ArrayList<ArrayList<Item>>) me.getValue();
            //System.out.println("Shoes " + itemTemp.get(0).get(0));
            Item key = (Item) me.getKey();
            //String color[] = new String[OUTFITS];

            indexShoes = rnd.nextInt(itemTemp.get(0).size());
            indexBottom = rnd.nextInt(itemTemp.get(1).size());
            indexCoats = rnd.nextInt(itemTemp.get(2).size());
            //System.out.println(itemTemp.get(0).get(indexShoes));

            arrIDs[i] = bd.outfitDAO().countOutfits() + (i + 1);
            arrNames[i] = "Outfit " + (arrIDs[i]);

            // Leer id y buscar foto de Top
            arrUppers[i] = decodificarImagen(key);
            // Leer id y buscar foto de shoes
            arrShoes[i] = decodificarImagen(itemTemp.get(0).get(indexShoes));
            // Leer id y buscar foto de bottom
            arrBottoms[i] = decodificarImagen(itemTemp.get(1).get(indexBottom));
            // Leer id y buscar foto de coats
            arrCoats[i] = decodificarImagen(itemTemp.get(2).get(indexCoats));

            i++;
        }
    }

    private void cargarDatos() {
            // BD
            DataBase bd = DataBase.getInstance(getContext());
            List<Item> items = bd.itemDAO().getAllItems();
            Log.i("SugeridosFragment", "Cargar Datos :: Registros: " + items.size());
                colors = new String[items.size()];

                for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getTipo().equalsIgnoreCase("Shoes")) {
                    shoes.add(items.get(i));
                }
                if (items.get(i).getTipo().equalsIgnoreCase("Bottom")) {
                    bottom.add(items.get(i));
                }
                if (items.get(i).getTipo().equalsIgnoreCase("Top")) {
                    top.add(items.get(i));
                }
                if (items.get(i).getTipo().equalsIgnoreCase("Coats")) {
                    coats.add(items.get(i));
                }

                //Seleccionar Color
                colors[i] = items.get(i).getColor();
            }

            crearOutfit(bd);

        DataBase.destroyInstance();

        }

    private HashMap<Item,ArrayList<ArrayList<Item>>> crearCombinacionesContinuo(ArrayList<Item> shoes, ArrayList<Item> bottom, ArrayList<Item> top, ArrayList<Item> coats) {

        HashMap<Item, ArrayList<ArrayList<Item>>> combinaciones = new HashMap<>();
        ArrayList<Item> combinacionesShoes = new ArrayList<>();
        ArrayList<Item> combinacionesBottom = new ArrayList<>();
        ArrayList<Item> combinacionesCoats = new ArrayList<>();
        ArrayList<ArrayList<Item>> combinan = new ArrayList<>();

        for (int i = 0; i < top.size() ; i++) {
            //Log.i("TOP",top.get(i).getColor());
            for (int j = 0; j < shoes.size() ; j++) {
                //Log.i("SHOES",shoes.get(j).getColor());
                if(verificarCombinacionesContinuo(top.get(i).getColor(), shoes.get(j).getColor()))
                    combinacionesShoes.add(shoes.get(j));
            }

            for (int j = 0; j < bottom.size() ; j++) {
                if(verificarCombinacionesContinuo(top.get(i).getColor(), bottom.get(j).getColor()))
                    combinacionesBottom.add(bottom.get(j));
            }

            for (int j = 0; j < coats.size() ; j++) {
                if(verificarCombinacionesContinuo(top.get(i).getColor(), coats.get(j).getColor()))
                    combinacionesCoats.add(coats.get(i));
            }

                    /*Log.i(top.get(i).getColor(),"Shoes" + combinacionesShoes.toString() + "\nBottom" + combinacionesBottom.toString( ) +
                            "\nCoats" + combinacionesCoats.toString());*/
            if(combinacionesShoes.size()>0 && combinacionesBottom.size()>0 && combinacionesCoats.size()>0) {
                combinan.add(combinacionesShoes);
                combinan.add(combinacionesBottom);
                combinan.add(combinacionesCoats);
                combinaciones.put(top.get(i), combinan);
                combinan = new ArrayList<>();
            }

            combinacionesShoes = new ArrayList<>();
            combinacionesBottom = new ArrayList<>();
            combinacionesCoats = new ArrayList<>();
        }

        return combinaciones;

    }

    private HashMap<Item, ArrayList<ArrayList<Item>>> crearCombinacionesContraste(ArrayList<Item> shoes, ArrayList<Item> bottom, ArrayList<Item> top, ArrayList<Item> coats) {

        HashMap<Item, ArrayList<ArrayList<Item>>> combinaciones = new HashMap<>();
        ArrayList<Item> combinacionesShoes = new ArrayList<>();
        ArrayList<Item> combinacionesBottom = new ArrayList<>();
        ArrayList<Item> combinacionesCoats = new ArrayList<>();
        ArrayList<ArrayList<Item>> combinan = new ArrayList<>();

        for (int i = 0; i < top.size() ; i++) {
            //Log.i("TOP",top.get(i).getColor());
            for (int j = 0; j < shoes.size() ; j++) {
                //Log.i("SHOES",shoes.get(j).getColor());
                if(verificarCombinacionesContraste(top.get(i).getColor(), shoes.get(j).getColor()))
                    combinacionesShoes.add(shoes.get(j));
            }

            for (int j = 0; j < bottom.size() ; j++) {
                if(verificarCombinacionesContraste(top.get(i).getColor(), bottom.get(j).getColor()))
                    combinacionesBottom.add(bottom.get(j));
            }

            for (int j = 0; j < coats.size() ; j++) {
                if(verificarCombinacionesContraste(top.get(i).getColor(), coats.get(j).getColor()))
                    combinacionesCoats.add(coats.get(i));
            }

                    /*Log.i(top.get(i).getColor(),"Shoes" + combinacionesShoes.toString() + "\nBottom" + combinacionesBottom.toString( ) +
                            "\nCoats" + combinacionesCoats.toString());*/
            if(combinacionesShoes.size()>0 && combinacionesBottom.size()>0 && combinacionesCoats.size()>0) {
                combinan.add(combinacionesShoes);
                combinan.add(combinacionesBottom);
                combinan.add(combinacionesCoats);
                combinaciones.put(top.get(i), combinan);
                combinan = new ArrayList<>();
            }

            combinacionesShoes = new ArrayList<>();
            combinacionesBottom = new ArrayList<>();
            combinacionesCoats = new ArrayList<>();
        }

        return combinaciones;
    }

    private boolean verificarCombinacionesContinuo(String colorTop, String colorItem) {
        if (Arrays.toString(universales).contains(colorTop) || Arrays.toString(universales).contains(colorItem))
            return true;
        /* 0-2; 9-11;
         3-5; 12-14;
         6-8; 15-17;

        "amarillo_claro"
        "amarillo"
        "amarillo_osc"
        "naranja_claro"
        "naranja"
        "naranja_osc"
        "rojo_claro"
        "rojo"
        "rojo_osc"
        "morado_claro"
        "morado"
        "morado_osc"
        "azul_claro"
        "azul"
        "azul_osc"
        "verde_claro"
        "verde"
        "verde_osc"*/

        int indexItem = getIndex(colorItem);
        int indexTop = getIndex(colorTop);

        if (indexTop == indexItem - 9 && indexTop == indexItem + 9)
            return true;
        if (indexItem == indexTop - 9 && indexItem == indexTop + 9)
            return true;

        return false;
    }

    private boolean verificarCombinacionesContraste(String colorTop, String colorItem) {
        if (Arrays.toString(universales).contains(colorTop) || Arrays.toString(universales).contains(colorItem))
            return true;

        int indexItem = getIndex(colorItem);
        int indexTop = getIndex(colorTop);
        if (indexTop >= indexItem - 2 && indexTop <= indexItem + 2)
                return true;
        if (indexItem >= indexTop - 2 && indexItem <= indexTop + 2)
            return true;

        return false;
    }

    private boolean verificarCombinaciones(HashMap<Integer, ArrayList> combinaciones) {

        ArrayList values = new ArrayList<>(combinaciones.size());
        for (int key : combinaciones.keySet()) {
            values = combinaciones.get(key);

            if (values == null || values.contains(null))
                return false;
        }

        return true;
    }

    private int getIndex(String color) {
        for (int i = 0; i < circuloCromatico.length; i++) {
            if (circuloCromatico[i].equalsIgnoreCase(color))
                return i;
        }

        return -1;
    }

    private ArrayList calcularCombinaciones(String color, int index) {

        ArrayList<String> combinacionesArray = new ArrayList<>(5);


        for (int i = 0; i < universales.length; i++) {
            if (color.equalsIgnoreCase(universales[i])) {
                for (int j = 0; j < combinacionesArray.size(); j++) {
                    combinacionesArray.add(circuloCromatico[new Random().nextInt(circuloCromatico.length)]);
                }
                return  combinacionesArray;
            }
        }

        for (int i = 0; i < circuloCromatico.length; i++) {
            if (i >= index - 2 && i <= index + 2) {
                combinacionesArray.add(circuloCromatico[i]);
            }
        }

        return combinacionesArray;
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
        //Log.i("SugeridosFragment", "Image Decoded: " + bitmap_tmp);
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

            if (arrNames.length > 0) {
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
    class BDItem extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            cargarDatos();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Nuevos datos para el adaptador
            SugeridosFragment.ControllerAdapter adapt = (SugeridosFragment.ControllerAdapter) recyclerView.getAdapter();
            adapt.setDatos(arrIDs, arrNames, arrCoats, arrUppers, arrBottoms, arrShoes);
            adapt.notifyDataSetChanged();
        }
    }

}

