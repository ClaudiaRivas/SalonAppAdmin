package com.example.rivas.salonappadmin.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rivas.salonappadmin.Apputilities.AdaptadorServicios;
import com.example.rivas.salonappadmin.Apputilities.FragmentConsultaFirebase;
import com.example.rivas.salonappadmin.Apputilities.ItemClickSupport;
import com.example.rivas.salonappadmin.R;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;


public class ServiceFragment extends FragmentConsultaFirebase {

    //firebase
    CollectionReference dbServicio;
    FloatingActionButton btnAddServicio;
    private final String ruta_db = "servicios";
    private final String ruta_img = "img_servicios";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);

        btnAddServicio  = view.findViewById(R.id.btnAddServicio);

        //instancia al listview
        RecyclerView list = (RecyclerView) view.findViewById(R.id.listaServicios);
        list.setHasFixedSize(true);

        adaptadorItems = new AdaptadorServicios(listaItems,getContext());

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(mLayoutManager);

        //hacer instancia a la bd en firebase


        if(dbServicio==null){
            dbServicio  = FirebaseFirestore.getInstance().collection(ruta_db);
            imgFirebase = FirebaseStorage.getInstance().getReference(ruta_img);

            dbServicio.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    actualizarDatos(queryDocumentSnapshots.getDocumentChanges());
                }
            });
        }


        btnAddServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarServicio();
            }
        });

        //establecer el adaptador a la listview
        list.setAdapter(adaptadorItems);
        /**
         * Registra cambios en la bd mediante el evento Document Changed
         */
        ItemClickSupport.addTo(list).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                cargarFragmento(position);
            }
        });
        return view;
    }

    private void agregarServicio() {
        if (mFragmentNavigation != null) {
            EditFragment mFragment = new EditFragment();
            mFragment.setRuta_db(ruta_db);
            mFragment.setRuta_img(ruta_img);
            mFragment.setTitulo("Agregar Servicio");
            mFragmentNavigation.pushFragment(mFragment);
        }
    }

    private void cargarFragmento(int i){
        if (mFragmentNavigation != null) {
            DetailsFragment mFragment = new DetailsFragment();
            mFragment.setItem( listaItems.get(i) );
            mFragment.setTitulo("Servicio");
            mFragment.setRuta_db(ruta_db);
            mFragment.setRuta_img(ruta_img);
            mFragmentNavigation.pushFragment(mFragment);
        }
    }

}
