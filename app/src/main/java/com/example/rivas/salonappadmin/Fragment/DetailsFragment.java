package com.example.rivas.salonappadmin.Fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rivas.salonappadmin.Activities.PrincipalActivity;
import com.example.rivas.salonappadmin.Apputilities.BaseFragment;
import com.example.rivas.salonappadmin.Model.Item;
import com.example.rivas.salonappadmin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class DetailsFragment extends BaseFragment {

    TextView txtDetalleTitulo,txtDetalleNombre,txtDetalleDescripcion,txtDetallePrecio;
    ImageView imagDetalleItem;
    Button btnEditar,btnEliminar;
    String titulo;
    private Item item;
    View view;
    String ruta_db;
    String ruta_img;

    CollectionReference dbFirebase;
    StorageReference imgFirebase;

    CoordinatorLayout layoutDetails;

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

        layoutDetails           = view.findViewById(R.id.frm_details_layout);
        txtDetalleTitulo        = view.findViewById(R. id.textDetalleTitulo);
        txtDetalleNombre        = view.findViewById(R. id.textDetalleNombre);
        txtDetalleDescripcion   = view.findViewById(R. id.textDetalleDescripcion);
        txtDetallePrecio        = view.findViewById(R. id.textDetallePrecio);
        imagDetalleItem         = view.findViewById(R. id.imgDetalleItem);
        btnEditar               = view.findViewById(R. id.btnDetallesEditar);
        btnEliminar             = view.findViewById(R.id.btnDetallesBorrar);

        if(ruta_db!=null & ruta_img!=null){
            //TODO inicializar DB
            dbFirebase  = FirebaseFirestore.getInstance().collection(ruta_db);
            imgFirebase = FirebaseStorage.getInstance().getReference(ruta_img);
        }

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


        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarItem();
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarEditar();
            }
        });
        return view;
    }


    private void eliminarItem() {

        new AlertDialog.Builder(getContext(),R.style.AlertDialogTheme)
                .setTitle("Eliminar")
                .setMessage("¿Desea eliminar : " + txtDetalleNombre.getText().toString()+" ?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //en caso de que seleccione si entonces se procede a borrar de firebase
                        borrarElementoFirebase(item);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }



    private void borrarElementoFirebase(Item item) {
        dbFirebase.document(item.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar snackbar = Snackbar
                                .make(layoutDetails, "Eliminado con exito", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        PrincipalActivity.mNavController.clearStack();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Snackbar snackbar = Snackbar
                                .make(layoutDetails, "Ha ocurrido un error al intentar eliminar", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
        if(item.getRuta_img()!=null){
            imgFirebase
                    .child(item.getRuta_img())
                    .delete();
        }
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
