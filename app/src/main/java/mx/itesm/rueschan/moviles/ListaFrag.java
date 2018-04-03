package mx.itesm.rueschan.moviles;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListaFrag extends Fragment
{
    private RecyclerView rvOutfits;
    // Arreglos para el adaptador
    private int[] arrIDs;
    private String[] arrNames;
    private Bitmap[] arrCoats;
    private Bitmap[] arrUppers;
    private Bitmap[] arrBottoms;
    private Bitmap[] arrShoes;


    public ListaFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("ListFrag", "Enter");
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_lista, container, false);
        // Adaptador de prueba vacío
        rvOutfits = v.findViewById(R.id.rvOutfits);
        AdaptadorRV adaptador = new AdaptadorRV(new int[]{}, new String[]{}, null,null, null, null);
        rvOutfits.setAdapter(adaptador);
        rvOutfits.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();

        new BDOutfit().execute();
    }


//    private void cargarDatos() {
//        // BD
//        DataBase bd = DataBase.getInstance(getContext());
//        int numAlumnos = bd.userDAO().contarUsuarios();
//        Log.i("cragarDatos", "Registros: " + numAlumnos);
//        List<User> users = bd.userDAO().leerTodos();
//        // Crea los arreglos para el adaptador
//        username = new String[numAlumnos];
//        arrNombres = new String[numAlumnos];
//        arrNacimientos = new String[numAlumnos];
//        arrApellidos = new String[numAlumnos];
//        arrFotos = new Bitmap[numAlumnos];
//        password = new String[numAlumnos];
//        color = new String[numAlumnos];
//        // Procesa cada registro
//        for (int i = 0; i< users.size(); i++) {
//            User a = users.get(i);
//            username[i] = a.getUsuario()+"";
//            arrNombres[i] = a.getNombre();
//            arrApellidos[i] = a.getApellido();
//            arrNacimientos[i] = a.getNacimiento();
//            password[i] = a.getPassword();
//            color[i] = a.getColor();
//
//            Log.i("BD", a.toString());
//
//            arrFotos[i] = decodificarImagen(a);
//        }
//
//        BaseDatos.destroyInstance();
//    }

    // Crea el bitmap a partir del arreglo de bytes
//    @NonNull
//    private Bitmap decodificarImagen(User a) {
//        Bitmap bm = null;
//        try {
//            InputStream ent = getActivity().getResources().getAssets().open("temp.png");
//            bm = BitmapFactory.decodeStream(ent);
//        } catch (IOException e) {
//            Log.i("cargaBD", "Error: " + e.getMessage());
//        }
//        int width = 128;
//        int height = 128;
//        Bitmap.Config configBmp = Bitmap.Config.valueOf(bm.getConfig().name());
//        Bitmap bitmap_tmp = Bitmap.createBitmap(width, height, configBmp);
//        ByteBuffer buffer = ByteBuffer.wrap(a.getFoto());
//        bitmap_tmp.copyPixelsFromBuffer(buffer);
//        return bitmap_tmp;
//    }

    // Método de prueba
    private void cargarDatosTest() {
        int numOutfits = 3;
        Log.i("ListaFrag", "Cargar Datos(Test) :: Registros: " + numOutfits);
        // Crea los arreglos para el adaptador
        String[] names = {"Outfit Uno", "Outfit Dos", "Outfit Tres"};
        arrNames = names;
    }

    // Para cargar los datos en segundo plano
    class BDOutfit extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            cargarDatosTest();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Nuevos datos para el adaptador
            AdaptadorRV adaptador = (AdaptadorRV) rvOutfits.getAdapter();
            adaptador.setDatos(arrIDs, arrNames, arrCoats, arrUppers, arrBottoms, arrShoes);
            adaptador.notifyDataSetChanged();
        }
    }
}
