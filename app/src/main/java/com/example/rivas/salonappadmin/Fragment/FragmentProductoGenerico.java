package com.example.rivas.salonappadmin.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rivas.salonappadmin.Apputilities.AdaptadorProductos;
import com.example.rivas.salonappadmin.Apputilities.FragmentConsultaFirebase;
import com.example.rivas.salonappadmin.Apputilities.GridDecoracion;
import com.example.rivas.salonappadmin.Apputilities.ItemClickSupport;
import com.example.rivas.salonappadmin.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

public class FragmentProductoGenerico extends FragmentConsultaFirebase {

    private RecyclerView recycler;
    private RecyclerView.LayoutManager administrador;

    private GridDecoracion decoracion;
    private CollectionReference dbReferencia;

    private String referencia;
    private String titulo;
    private String rutaImg;
    private FloatingActionButton btnAddProducto;

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setRutaImg(String rutaImg) {
        this.rutaImg = rutaImg;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflar el layout
        View view = inflater.inflate(R.layout.fragment_producto_generico, container, false);

        TextView txtTitulo  = (TextView) view.findViewById(R.id.txtTituloProductoGenerico);
        btnAddProducto      = view.findViewById(R.id.btnAddProducto);

        if(titulo!=null){
            txtTitulo.setText(titulo);
        }else{
            txtTitulo.setText("Productos");
        }

        recycler = view.findViewById(R.id.reciclerProductoGenerico);
        recycler.setHasFixedSize(true);

        decoracion = new GridDecoracion(2,50,true);

        administrador = new GridLayoutManager(getContext(), 2);
        recycler.setLayoutManager(administrador);
        adaptadorItems = new AdaptadorProductos(listaItems,getContext());

        recycler.addItemDecoration(decoracion);
        recycler.setAdapter(adaptadorItems);

        if(rutaImg!=null){
            imgFirebase = FirebaseStorage.getInstance().getReference(rutaImg);
        }
        //si tiene referencia de la BD
        if(referencia!=null && dbReferencia==null){

            dbReferencia = FirebaseFirestore.getInstance().collection(referencia);

            dbReferencia.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    actualizarDatos(queryDocumentSnapshots.getDocumentChanges());
                }
            });
        }
        btnAddProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarProducto();
            }
        });

        ItemClickSupport.addTo(recycler).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                cargarFragmento(position);
            }
        });
        return view;
    }

    private void cargarFragmento(int i){
        if (mFragmentNavigation != null) {
            DetailsFragment mFragment = new DetailsFragment();
            mFragment.setItem( listaItems.get(i) );
            mFragment.setTitulo("Producto");
            mFragment.setRuta_db(referencia);
            mFragment.setRuta_img(rutaImg);
            mFragmentNavigation.pushFragment(mFragment);
        }
    }

    private void agregarProducto(){
        if (mFragmentNavigation != null) {
            EditFragment mFragment = new EditFragment();
            mFragment.setRuta_db(referencia);
            mFragment.setRuta_img(rutaImg);
            mFragment.setTitulo("Agregar Producto");
            mFragmentNavigation.pushFragment(mFragment);
        }
    }

}
