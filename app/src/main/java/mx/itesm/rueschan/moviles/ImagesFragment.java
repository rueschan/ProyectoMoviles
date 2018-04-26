package mx.itesm.rueschan.moviles;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import mx.itesm.rueschan.moviles.EntidadesBD.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImagesFragment extends Fragment {

    public static int selectedID = -1;
    private int[] arrIDs;
    private Bitmap[] arrPhotos;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("ImagesFragment", "Enter");

        View v = inflater.inflate(R.layout.recycler_view, container, false);

        recyclerView = v.findViewById(R.id.my_recycler_view);

        ImagesFragment.ControllerAdapter adapter = new ImagesFragment.ControllerAdapter(new int[]{}, new Bitmap[]{});
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

    private void grabarDatos() {

        DataBase db = DataBase.getInstance(getContext());
        User currentUser = MainActivity.currentUser;
        int numImages = db.itemDAO().countByTypeAndUserID(ClosetFragment.clicked, currentUser.getIdUser());
        Log.i("ImagesFragment", "Quantity of " + ClosetFragment.clicked + ">> " + numImages);
        List<Item> clothes = db.itemDAO().getAllItemsByTypeAndUserID(ClosetFragment.clicked, currentUser.getIdUser());
        arrPhotos = new Bitmap[numImages];
        arrIDs = new int[numImages];
        for (int i = 0; i < numImages; i++) {
            Item tt = clothes.get(i);
            arrIDs[i] = tt.getId();
            arrPhotos[i] = decodificarImagen(tt);
        }

    }

    @NonNull
    private Bitmap decodificarImagen(Item a) {
        Bitmap bm = null;

        try {
            InputStream ent = getResources().getAssets().open("temp.png");
            bm = BitmapFactory.decodeStream(ent);
        } catch (IOException e) {
            Log.i("BD (ImagesFragment)", "Error: " + e.getMessage());
        }
        int width = 128;
        int height = 128;
        Bitmap.Config configBmp = Bitmap.Config.valueOf(bm.getConfig().name());
        Bitmap bitmap_tmp = Bitmap.createBitmap(width, height, configBmp);

        ByteBuffer buffer = ByteBuffer.wrap(a.getFoto());

        bitmap_tmp.copyPixelsFromBuffer(buffer);
        Log.i("ImagesFragment", "Image Decoded: " + bitmap_tmp);
        return bitmap_tmp;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public int id;
        public ImageView imageView;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.closet_photos_items, parent, false));
            imageView = itemView.findViewById(R.id.img);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ClosetFragment.origen == ClosetFragment.Origin.FAVORITOS) {
                        selectedID = id;

                    }
                }
            });
        }



    }

    public static class ControllerAdapter extends RecyclerView.Adapter<ImagesFragment.ViewHolder> {

        private static int SIZE;
        private int[] arrIDs;
        private Bitmap[] arrPictures;

        public ControllerAdapter(int[] IDs, Bitmap[] arrPhotos) {
            arrIDs = IDs;
            arrPictures = arrPhotos;
        }

        public void setDatos(int[] IDs, Bitmap[] arrPhotos){
            arrIDs = IDs;
            arrPictures = arrPhotos;
            SIZE = arrPhotos.length;

            for (int i = 0; i < SIZE; i++) {
                System.out.println(arrPictures[i].toString());
            }
        }

        @NonNull
        @Override
        public ImagesFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ImagesFragment.ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull final ImagesFragment.ViewHolder holder, int position) {

            if (arrPictures.length == 0){
                holder.imageView.setImageResource(R.drawable.nocloset);
            } else {
                holder.id = arrIDs[position];
                holder.imageView.setImageBitmap(arrPictures[position % arrPictures.length]);
            }

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ClosetFragment.origen == ClosetFragment.Origin.FAVORITOS) {
                        selectedID = holder.id;
                        notifyDataSetChanged();

                    }
                }
            });

            if (selectedID == holder.id) {
                int border = 10;
                holder.imageView.setPadding(border, border, border, border);
                holder.imageView.setBackgroundColor(Color.DKGRAY);
            } else {
                holder.imageView.setPadding(0, 0, 0, 0);
                holder.imageView.setBackgroundColor(Color.WHITE);
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
            grabarDatos();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ImagesFragment.ControllerAdapter adapt = (ImagesFragment.ControllerAdapter)recyclerView.getAdapter();
            adapt.setDatos(arrIDs, arrPhotos);
            adapt.notifyDataSetChanged();

        }
    }



}