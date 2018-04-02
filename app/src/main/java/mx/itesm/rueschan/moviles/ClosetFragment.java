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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClosetFragment extends Fragment {


    public static String clicked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);

        ControllerAdapter adapter = new ControllerAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        int paddingInBetween = getResources().getDimensionPixelSize(R.dimen.sizes);
        recyclerView.setPadding(paddingInBetween, paddingInBetween, paddingInBetween, paddingInBetween);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


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
                    System.out.println(textView.getText().toString());
                    view.getContext().startActivity(intent);
                }
            });
        }


    }

    public static class ControllerAdapter extends RecyclerView.Adapter<ViewHolder>{

        private static final  int SIZE = 4;
        private final String[] arrayTexto;
        private final Drawable[] arrayPictures;

        public ControllerAdapter(Context context){

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
            holder.imageView.setImageDrawable(arrayPictures[position%arrayPictures.length]);
            holder.textView.setText(arrayTexto[position%arrayTexto.length]);

        }

        @Override
        public int getItemCount() {
            return SIZE;
        }
    }
}
