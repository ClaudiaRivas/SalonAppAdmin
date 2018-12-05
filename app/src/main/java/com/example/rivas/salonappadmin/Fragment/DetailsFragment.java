package com.example.rivas.salonappadmin.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rivas.salonappadmin.Apputilities.BaseFragment;
import com.example.rivas.salonappadmin.Model.Item;
import com.example.rivas.salonappadmin.R;


public class DetailsFragment extends BaseFragment {

    TextView txtDetalleTitulo,txtDetalleNombre,txtDetalleDescripcion,txtDetallePrecio;
    ImageView imagDetalleItem;
    Button btnEditar;
    String titulo;
    private Item item;
    View view;
    String ruta_db;
    String ruta_img;

    public void setRuta_db(String ruta_db) {
        this.ruta_db = ruta_db;
    }

    public void setRuta_img(String ruta_img) {
        this.ruta_img = ruta_img;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public DetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_details, container, false);

        txtDetalleTitulo        = view.findViewById(R. id.textDetalleTitulo);
        txtDetalleNombre        = view.findViewById(R. id.textDetalleNombre);
        txtDetalleDescripcion   = view.findViewById(R. id.textDetalleDescripcion);
        txtDetallePrecio        = view.findViewById(R. id.textDetallePrecio);
        imagDetalleItem         = view.findViewById(R. id.imgDetalleItem);
        btnEditar               = view.findViewById(R. id.btnDetallesEditar);

        if(titulo!=null){
            txtDetalleTitulo.setText(titulo);
        }

        if(item!=null){
            btnEditar.setEnabled(true);
            txtDetalleNombre.setText(item.getNombre());
            txtDetalleDescripcion.setText(item.getDescripcion());
            txtDetallePrecio.setText(item.getPrecio());
            if(item.getImgItem()!=null){
                imagDetalleItem.setImageBitmap(item.getImgItem());
            }
        }else{
            btnEditar.setEnabled(false);
        }
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarEditar();
            }
        });
        return view;
    }

    private void cargarEditar(){
        if (mFragmentNavigation != null) {
            EditFragment frmEdit = new EditFragment();
            frmEdit.setItem(item);
            frmEdit.setTitulo("Editar "+((titulo!=null)?titulo:""));
            frmEdit.setRuta_db(ruta_db);
            frmEdit.setRuta_img(ruta_img);
            mFragmentNavigation.pushFragment(frmEdit);
        }
    }


}
