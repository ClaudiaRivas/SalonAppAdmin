package com.example.rivas.salonappadmin.Apputilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.rivas.salonappadmin.Model.Item;
import com.example.rivas.salonappadmin.R;

import java.util.ArrayList;

public class AdaptadorServicios extends RecyclerView.Adapter<AdaptadorServicios.ViewHolder> {

    private ArrayList<Item> list = new ArrayList<>();
    private Context context;

    public AdaptadorServicios(ArrayList<Item> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_servicio, viewGroup, false);
        AdaptadorServicios.ViewHolder holder = new AdaptadorServicios.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorServicios.ViewHolder viewHolder, int i) {
        final Item s = list.get(i);

        viewHolder.txtNombreServicio.setText(s.getNombre());
        viewHolder.txtDescripcionServicio.setText(s.getDescripcion());
        viewHolder.txtPrecioServicio.setText(s.getPrecio());

        if(s.getImgItem()!=null){
            viewHolder.imgServicio.setImageBitmap(s.getImgItem());
        }else{
            viewHolder.imgServicio.setImageResource(R.drawable.service1);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgServicio;
        private TextView txtNombreServicio;
        private TextView txtDescripcionServicio;
        private TextView txtPrecioServicio;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            imgServicio             = itemView.findViewById(R.id.imgServicioItem);
            txtNombreServicio       = itemView.findViewById(R.id.txtNombreServicio);
            txtDescripcionServicio  = itemView.findViewById(R.id.txtDescripcionServicio);
            txtPrecioServicio       = itemView.findViewById(R.id.txtPrecioServicio);
            cardView = itemView.findViewById(R.id.cardItemServicio);
        }
    }
}
