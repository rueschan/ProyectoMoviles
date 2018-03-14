package mx.itesm.rueschan.moviles;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by roberto on 04/03/18.
 */

public class AdaptadorRV extends RecyclerView.Adapter<AdaptadorRV.Vista>
{
    // DATOS
    private String[] arrOutfitName;
    private String[] arrNombres;
    private String[] arrApellidos;
    private String[] arrColor;
    private String[] nacimiento;
    private Bitmap[] arrFotos;

    public AdaptadorRV(String[] matriculas, String[] nombres, String[] apellidos, String[] color, String[] date, Bitmap[] fotos) {
        arrOutfitName = matriculas;
        arrNombres = nombres;
        arrApellidos = apellidos;
        arrColor = color;
        nacimiento = date;
        arrFotos = fotos;
    }

    public AdaptadorRV(String[] outfitNames) {
        arrOutfitName = outfitNames;
    }

    public void setDatos(String[] matriculas, String[] nombres, String[] apellidos, String[] color, String[] date, Bitmap[] fotos) {
        arrOutfitName = matriculas;
        arrNombres = nombres;
        arrApellidos = apellidos;
        arrColor = color;
        nacimiento = date;
        arrFotos = fotos;
    }

    public void setDatos(String[] outfitNames) {
        arrOutfitName = outfitNames;
    }

    @Override
    public Vista onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView tarjeta = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.outfit_card, parent, false);

        return new Vista(tarjeta);  // Un renglón de la lista
    }

    @Override
    public void onBindViewHolder(Vista holder, int position) {
        CardView tarjeta = holder.tarjeta;
        TextView outfitName = tarjeta.findViewById(R.id.outfitName);
        ImageView coat = tarjeta.findViewById(R.id.coatImg);
        ImageView upper = tarjeta.findViewById(R.id.upperImg);
        ImageView botom = tarjeta.findViewById(R.id.bottomImg);
        ImageView shoes = tarjeta.findViewById(R.id.shoesImg);

        outfitName.setText(arrOutfitName[position]);
    }


    @Override
    public int getItemCount() {
        return arrOutfitName.length;
    }

    public class Vista extends RecyclerView.ViewHolder {
        private CardView tarjeta;

        public Vista(CardView v) {
            super(v);
            this.tarjeta = v;
        }
    }
}