package mx.itesm.rueschan.moviles;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import mx.itesm.rueschan.moviles.EntidadesBD.Item;
import mx.itesm.rueschan.moviles.BD.DataBase;
import mx.itesm.rueschan.moviles.EntidadesBD.User;

import static xdroid.toaster.Toaster.toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImagesFragment extends Fragment {

    public static int selectedID = -1;
    private int[] arrIDs;
    private Bitmap[] arrPhotos;
    private String[] arrColors;
    private String[] arrEvents;
    private ImageView[] delete_iv;
    private ImageView[] edit_iv;
    private LinearLayout[] layout_edit;
    private ImageView[] like_iv;

    private int numImages;

    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("ImagesFragment", "Enter");

        View v = inflater.inflate(R.layout.recycler_view, container, false);

        recyclerView = v.findViewById(R.id.my_recycler_view);

        ImagesFragment.ControllerAdapter adapter = new ImagesFragment.ControllerAdapter(new int[]{}, new Bitmap[]{}, new String[]{},
                new String[]{},new ImageView[]{}, new ImageView[]{}, new LinearLayout[]{});
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


    private void cargarDatos() {

        DataBase db = DataBase.getInstance(getContext());
        User currentUser = MainActivity.currentUser;
        /*db.itemDAO().delete();
        db.outfitDAO().erase();*/
        numImages = db.itemDAO().countByTypeAndUserID(ClosetFragment.clicked, currentUser.getIdUser());
        Log.i("ImagesFragment", "Quantity of " + ClosetFragment.clicked + ">> " + numImages);
        List<Item> clothes = db.itemDAO().getAllItemsByTypeAndUserID(ClosetFragment.clicked, currentUser.getIdUser());
        arrPhotos = new Bitmap[numImages];
        arrIDs = new int[numImages];
        arrColors = new String[numImages];
        arrEvents = new String[numImages];

        for (int i = 0; i < numImages; i++) {
                Item item = clothes.get(i);
            Log.i("ImagesFragment", "ITEM: " + item.toString());
                arrIDs[i] = item.getId();
                arrPhotos[i] = decodificarImagen(item);
                arrColors[i] = item.getColor();
                arrEvents[i] = item.getEvento();

        }
        DataBase.destroyInstance();
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        public int id;
        public ImageView imageView;
        private TextView info;
        private TextView titulo;
        private ImageView delete_iv;
        private ImageView edit_iv;
        public LinearLayout layout_edit;
        public ImageView like;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.closet_photos_items, parent, false));
            imageView = itemView.findViewById(R.id.img);
            info = itemView.findViewById(R.id.info_tv);
            titulo = itemView.findViewById(R.id.titulo_tv);
            delete_iv = itemView.findViewById(R.id.delete_iv);
            edit_iv = itemView.findViewById(R.id.edit_iv);
            layout_edit = itemView.findViewById(R.id.items_layout);
            like = itemView.findViewById(R.id.like_iv);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectedID = id;

                    /*if (ClosetFragment.origen == ClosetFragment.Origin.FAVORITOS) {


                    }else */


                }
            });
        }



    }

    public class ControllerAdapter extends RecyclerView.Adapter<ImagesFragment.ViewHolder> {

        private int SIZE;
        private int[] arrIDs;
        private Bitmap[] arrPictures;
        private String[] arrColors;
        private String[] arrEvents;
        private ImageView[] delete_iv;
        private ImageView[] edit_iv;
        private LinearLayout[] layout_edit;

        public ControllerAdapter(int[] IDs, Bitmap[] arrPhotos, String[] arrColors, String[] arrEvents, ImageView[] delete_iv, ImageView[] edit_iv, LinearLayout[] layout_edit) {
            arrIDs = IDs;
            arrPictures = arrPhotos;
            this.arrColors = arrColors;
            this.arrEvents = arrEvents;
            this.delete_iv = delete_iv;
            this.edit_iv = edit_iv;
            this.layout_edit = layout_edit;
        }

        public void setDatos(int[] IDs, Bitmap[] arrPhotos, String[] arrColors, String[] arrEvents, ImageView[] delete_iv, ImageView[] edit_iv, LinearLayout[] layout_edit) {
            arrIDs = IDs;
            arrPictures = arrPhotos;
            this.arrColors = arrColors;
            this.arrEvents = arrEvents;
            SIZE = arrPhotos.length;
            this.delete_iv = delete_iv;
            this.edit_iv = edit_iv;
            this.layout_edit = layout_edit;

            /*for (int i = 0; i < SIZE; i++) {
                System.out.println(arrPictures[i].toString());
            }*/
        }

        @NonNull
        @Override
        public ImagesFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ImagesFragment.ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull final ImagesFragment.ViewHolder holder, final int position) {

            if (arrPictures.length == 0) {
                holder.imageView.setImageResource(R.drawable.nocloset);

            } else {
                holder.id = arrIDs[position];
                holder.imageView.setImageBitmap(arrPictures[position % arrPictures.length]);
                holder.titulo.setText("IMG " + arrIDs[position]);
                holder.info.setText("Color: " +  traducirColor(arrColors[position]) + "\nEvent: " + arrEvents[position]);
                //holder.like.setVisibility(View.INVISIBLE);
            }

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(),"click en la imagen " + ClosetFragment.origen,Toast.LENGTH_SHORT).show();
                    if (ClosetFragment.origen == ClosetFragment.Origin.MAIN) {
                        setVisibility();
                    }
                    selectedID = holder.id;
                    notifyDataSetChanged();
                    //new BDItem().execute();
//                    toast("click en la imagen " + selectedID + " y viene de " + ClosetFragment.origen.name());
                }

                private void setVisibility() {

                    //aumenta el tamaño del layout
                    LinearLayout.LayoutParams parms;
                    //tamano del text view
                    LinearLayout.LayoutParams params;

                    //Aparecer la informacion del texto
                    if (holder.edit_iv.getVisibility() == View.INVISIBLE) {
                        //holder.info.setVisibility(View.VISIBLE);
                        holder.edit_iv.setVisibility(View.VISIBLE);
                        holder.delete_iv.setVisibility(View.VISIBLE);
                        /*parms = new LinearLayout.LayoutParams(holder.layout_edit.getWidth(), convertPxToDp(200));
                        params = new LinearLayout.LayoutParams(holder.info.getWidth(), convertPxToDp(400));*/
                    }else {
                        //holder.info.setVisibility(View.INVISIBLE);
                        holder.edit_iv.setVisibility(View.INVISIBLE);
                        holder.delete_iv.setVisibility(View.INVISIBLE);
                        /*parms = new LinearLayout.LayoutParams(holder.layout_edit.getWidth(), 0);
                        params = new LinearLayout.LayoutParams(holder.info.getWidth(), 0);*/
                    }

                    //holder.layout_edit.setLayoutParams(parms);
                    //holder.info.setLayoutParams(params);

                }
            });

            //BORRAR OUTFIT
            holder.delete_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    selectedID = holder.id;
                    selectedID = arrIDs[position];
                    System.out.println("++++++++++++++++++++++ " + selectedID);
                    new BDborrar().execute();
                    toast("BORRAR " + selectedID + "");
                }
            });

            holder.edit_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedID = holder.id;
                    if (ClosetFragment.origen == ClosetFragment.Origin.MAIN) {
                        Intent init = new Intent(getContext(), TakePhoto.class);
                        init.putExtra("id", arrIDs[position]);
                        init.putExtra("color", arrColors[position]);
                        startActivity(init);
                    }
                }
            });

            //le movi al if chanito
            if (ClosetFragment.origen == ClosetFragment.Origin.FAVORITOS) {
                if (selectedID == holder.id) {
                    holder.like.setVisibility(View.VISIBLE);
                    /*int border = 10;
                    holder.imageView.setPadding(border, border, border, border);
                    holder.imageView.setBackgroundColor(Color.DKGRAY);*/
                } else {
                    /*holder.imageView.setPadding(0, 0, 0, 0);
                    holder.imageView.setBackgroundColor(Color.WHITE);*/
                    holder.like.setVisibility(View.INVISIBLE);
                }
            }
        }
        @Override
        public int getItemCount() {
            return SIZE;
        }

    }

    //ajusta automatico el tamaño de cada pantalla
    public int convertPxToDp(float px) {
        return (int) (px / getContext().getResources().getDisplayMetrics().density);
    }

    private String traducirColor(String color) {
            switch (color){
                case "negro":
                    return "Black";
                case "blanco":
                    return "White";
                case "gris":
                    return "Gray";
                case "amarillo_claro":
                    return "Light Yellow";
                case "amarillo_osc":
                    return "Drak Yellow";
                case "amarillo":
                    return "Yellow";
                case "rojo_osc":
                    return "Dark Red";
                case "rojo_claro":
                    return "Light Red";
                case "rojo":
                    return "Red";
                case "verde_osc":
                    return "Dark Green";
                case "verde":
                    return "Green";
                case "verde_claro":
                    return "Light Green";
                case "azul_osc":
                    return "Dark Blue";
                case "azul_claro":
                    return "Light Blue";
                case "azul":
                    return "Blue";
                case "morado_osc":
                    return "Dark Purple";
                case "morado":
                    return "Purple";
                case "morado_claro":
                    return "Light Purple";
                case "cafe_osc":
                    return "Dark Brown";
                case "cafe":
                    return "Brown";
                case "cafe_claro":
                    return "Light Brown";
            }
            return "";
        }



    class BDTarea extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            cargarDatos();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ImagesFragment.ControllerAdapter adapt = (ImagesFragment.ControllerAdapter)recyclerView.getAdapter();
            adapt.setDatos(arrIDs, arrPhotos, arrColors, arrEvents, delete_iv, edit_iv, layout_edit);
            adapt.notifyDataSetChanged();

            if (ClosetFragment.origen == ClosetFragment.Origin.MAIN) {
                Toast.makeText(getContext(), "Click on the photos to edit them", Toast.LENGTH_LONG).show();
            }else  if (ClosetFragment.origen == ClosetFragment.Origin.FAVORITOS) {
                Toast.makeText(getContext(), "Click on the photos to select them", Toast.LENGTH_LONG).show();
            }

        }
    }

    class BDborrar extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            borrarItem();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ImagesFragment.ControllerAdapter adapt = (ImagesFragment.ControllerAdapter)recyclerView.getAdapter();
            adapt.setDatos(arrIDs, arrPhotos, arrColors, arrEvents, delete_iv, edit_iv, layout_edit);
            adapt.notifyDataSetChanged();

            if (ClosetFragment.origen == ClosetFragment.Origin.MAIN) {
                Toast.makeText(getContext(), "Click on the photos to edit them", Toast.LENGTH_LONG).show();
            }else  if (ClosetFragment.origen == ClosetFragment.Origin.FAVORITOS) {
                Toast.makeText(getContext(), "Click on the photos to select them", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void borrarItem() {
        DataBase bd = DataBase.getInstance(getContext());
        if (bd == null) System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        if (selectedID != -1) {
            bd.outfitDAO().deleteByItemID(selectedID);
            bd.itemDAO().deleteById(selectedID);
        }
        selectedID = -1;
        DataBase.destroyInstance();
        cargarDatos();
    }


}