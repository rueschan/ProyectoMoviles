package mx.itesm.rueschan.moviles;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImagesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);

        ImagesFragment.ControllerAdapter adapter = new ImagesFragment.ControllerAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        int paddingInBetween = getResources().getDimensionPixelSize(R.dimen.sizes);
        recyclerView.setPadding(paddingInBetween, paddingInBetween, paddingInBetween, paddingInBetween);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        public ImageView imageView;


        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.closet_photos_items, parent, false));
            imageView = itemView.findViewById(R.id.img);

        }

    }

    public static class ControllerAdapter extends RecyclerView.Adapter<ImagesFragment.ViewHolder>{

        private final int SIZE;
        private final Drawable[] arrayPicturesofClothes;

        public ControllerAdapter(Context context){

            Resources resources = context.getResources();
            TypedArray typedArray = resources.obtainTypedArray(R.array.shirts_pictures);
            SIZE = typedArray.length();
            arrayPicturesofClothes = new Drawable[typedArray.length()];
            for (int i = 0; i < arrayPicturesofClothes.length; i++) {
                arrayPicturesofClothes[i] = typedArray.getDrawable(i);
            }
            typedArray.recycle();

        }

        @NonNull
        @Override
        public ImagesFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ImagesFragment.ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ImagesFragment.ViewHolder holder, int position) {
            holder.imageView.setImageDrawable(arrayPicturesofClothes[position%arrayPicturesofClothes.length]);

        }

        @Override
        public int getItemCount() {
            return SIZE;
        }
    }
}