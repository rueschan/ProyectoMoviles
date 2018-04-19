package mx.itesm.rueschan.moviles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
 * Activities that contain this fragment must implement the
 * {@link SugeridosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SugeridosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
    private OnFragmentInteractionListener mListener;

    public SugeridosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SugeridosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SugeridosFragment newInstance(String param1, String param2) {
        SugeridosFragment fragment = new SugeridosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            System.out.println("******** FAVORITOS");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        new BDOutfit().execute();
    }


    private void cargarDatos() {
        // BD
        DataBase bd = DataBase.getInstance(getContext());
        int numOutfits = bd.outfitDAO().countOutfits();
        int numToap;
        int numBottom;
        int numCoats;
        int numShoes;
        Log.i("SugeridosFragment", "Cargar Datos :: Registros: " + numOutfits);
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
            //FavoritosFragment.ControllerAdapter adapt = (FavoritosFragment.ControllerAdapter)recyclerView.getAdapter();
            //adapt.setDatos(arrIDs, arrNames, arrCoats, arrUppers, arrBottoms, arrShoes);
            //adapt.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sugeridos, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
