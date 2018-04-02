package mx.itesm.rueschan.moviles;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import mx.itesm.rueschan.moviles.EntidadesBD.Item;
import mx.itesm.rueschan.moviles.BD.DataBase;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImagesFragment extends Fragment {

    private Bitmap[] arrPhotos;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);

        View v = inflater.inflate(R.layout.recycler_view, container, false);


       recyclerView = v.findViewById(R.id.my_recycler_view);

        ImagesFragment.ControllerAdapter adapter = new ImagesFragment.ControllerAdapter(new Bitmap[]{});
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        int paddingInBetween = getResources().getDimensionPixelSize(R.dimen.sizes);
        recyclerView.setPadding(paddingInBetween, paddingInBetween, paddingInBetween, paddingInBetween);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        new BDTarea().execute();

    }

    private Bitmap[] grabarDatos() {

        DataBase db = DataBase.getInstance(getContext());
        int numImages = db.itemDAO().countByType(ClosetFragment.clicked);
        System.out.println(numImages);
        List<Item> clothes = db.itemDAO().getAllItemsByType(ClosetFragment.clicked);
        arrPhotos = new Bitmap[numImages];
        for (int i = 0; i < numImages; i++) {
            Item tt = clothes.get(i);
            arrPhotos[i] = decodificarImagen(tt);
        }

        return arrPhotos;

    }

    @NonNull
    private Bitmap decodificarImagen(Item a) {
        Bitmap bm = null;
        try {
            InputStream ent = getResources().getAssets().open("temp.png");
            bm = BitmapFactory.decodeStream(ent);
        } catch (IOException e) {
            Log.i("cargaBD", "Error: " + e.getMessage());
        }
        int width = 128;
        int height = 128;
        Bitmap.Config configBmp = Bitmap.Config.valueOf(bm.getConfig().name());
        Bitmap bitmap_tmp = Bitmap.createBitmap(width, height, configBmp);
        ByteBuffer buffer = ByteBuffer.wrap(a.getFoto());
        bitmap_tmp.copyPixelsFromBuffer(buffer);
        return bitmap_tmp;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView imageView;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.closet_photos_items, parent, false));
            imageView = itemView.findViewById(R.id.img);

        }

    }


    public static class ControllerAdapter extends RecyclerView.Adapter<ImagesFragment.ViewHolder> {

        private static int SIZE;
        private Bitmap[] arrPictures;

        public ControllerAdapter(Bitmap[] arrPhotos) {
            arrPictures = arrPhotos;
        }

        public void setDatos(Bitmap[] arrPhotos){
            arrPictures = arrPhotos;
            SIZE = arrPhotos.length;
        }


        @NonNull
        @Override
        public ImagesFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ImagesFragment.ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ImagesFragment.ViewHolder holder, int position) {

            if(arrPictures.length==0){
                holder.imageView.setImageResource(R.drawable.nocloset);
            }else {
                holder.imageView.setImageBitmap(arrPictures[position % arrPictures.length]);
            }

        }

        @Override
        public int getItemCount() {
            return SIZE;
        }
    }


    class BDTarea extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            arrPhotos = grabarDatos();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ImagesFragment.ControllerAdapter adapt = (ImagesFragment.ControllerAdapter)recyclerView.getAdapter();
            adapt.setDatos(arrPhotos);
            adapt.notifyDataSetChanged();

        }
    }



}