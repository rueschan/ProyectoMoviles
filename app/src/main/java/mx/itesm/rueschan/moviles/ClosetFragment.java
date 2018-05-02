package mx.itesm.rueschan.moviles;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import mx.itesm.rueschan.moviles.BD.DataBase;
import mx.itesm.rueschan.moviles.EntidadesBD.Outfit;

import static xdroid.core.Global.getContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClosetFragment extends Fragment {

    public static Origin origen;
    public static String clicked;
    public static Outfit tempOutfit;
    private int outfits;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("ClosetFragment", "Enter");
        new BDOutfit().execute();
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);

        ControllerAdapter adapter = new ControllerAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        int paddingInBetween = getResources().getDimensionPixelSize(R.dimen.sizes);
        recyclerView.setPadding(paddingInBetween, paddingInBetween, paddingInBetween, paddingInBetween);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView imageView;
        public TextView textView;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.closet_item, parent, false));
            imageView = itemView.findViewById(R.id.title_img);
            textView = itemView.findViewById(R.id.ettitle);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ImagesActivity.class);
                    clicked = textView.getText().toString();
                    Log.i("ClosetFragment", "Clicked on: " + textView.getText().toString());
                    view.getContext().startActivity(intent);
                }
            });
        }


    }

    public static class ControllerAdapter extends RecyclerView.Adapter<ViewHolder> {

        private static final int SIZE = 4;
        private final String[] arrayTexto;
        private final Drawable[] arrayPictures;

        public ControllerAdapter(Context context) {

            Resources resources = context.getResources();
            arrayTexto = resources.getStringArray(R.array.types);
            TypedArray typedArray = resources.obtainTypedArray(R.array.menupictures);
            arrayPictures = new Drawable[typedArray.length()];
            for (int i = 0; i < arrayPictures.length; i++) {
                arrayPictures[i] = typedArray.getDrawable(i);
            }
            typedArray.recycle();

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.imageView.setImageDrawable(arrayPictures[position % arrayPictures.length]);
            holder.textView.setText(arrayTexto[position % arrayTexto.length]);

        }

        @Override
        public int getItemCount() {
            return SIZE;
        }
    }

    public enum Origin {
        MAIN,
        SUGERIDOS,
        FAVORITOS
    }

    class BDOutfit extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            DataBase bd = DataBase.getInstance(getContext());
            outfits = bd.outfitDAO().countOutfits();
            DataBase.destroyInstance();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (origen == Origin.FAVORITOS) {
                tempOutfit = new Outfit("Outfit " + (outfits + 1), -1, -1, -1, -1);
            }
        }

    }
}