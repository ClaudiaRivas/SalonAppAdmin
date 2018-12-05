package com.example.rivas.salonappadmin.Fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rivas.salonappadmin.Apputilities.BaseFragment;
import com.example.rivas.salonappadmin.Model.Item;
import com.example.rivas.salonappadmin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends BaseFragment {

    TextView lblTitulo;
    ImageView imgItem;
    EditText txtNombre,txtDesc,txtPrecio;
    Button btnRotar,btnCambiar,btnEliminar,btnCancelar,btnGuardar;
    Item item;
    String ruta_db,ruta_img,titulo;

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setRuta_db(String ruta_db) {
        this.ruta_db = ruta_db;
    }

    public void setRuta_img(String ruta_img) {
        this.ruta_img = ruta_img;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        // Inflate the layout for this fragment

        lblTitulo   = view.findViewById(R.id.lblEditInformacion);
        imgItem     = view.findViewById(R.id.imgEditElemento);
        txtNombre   = view.findViewById(R.id.txtEditNombre);
        txtDesc     = view.findViewById(R.id.txtEditDescripcion);
        txtPrecio   = view.findViewById(R.id.txtEditPrecio);
        btnRotar    = view.findViewById(R.id.btnEditRotarImg);
        btnCambiar  = view.findViewById(R.id.btnEditCambiarImg);
        btnEliminar = view.findViewById(R.id.btnEditEliminarImg);
        btnCancelar = view.findViewById(R.id.btnEditCancelar);
        btnGuardar  = view.findViewById(R.id.btnEditGuardar);

        if(titulo!=null){
            lblTitulo.setText(titulo);
        }
        if(item!=null){
            txtNombre.setText(item.getNombre());
            txtDesc.setText(item.getDescripcion());
            txtPrecio.setText(item.getPrecio());
            if(item.getImgItem()!=null){
                imgItem.setImageBitmap(item.getImgItem());
            }
        }
        if(ruta_db!=null & ruta_img!=null){
            //TODO inicializar DB
        }
        return view;
    }

}
