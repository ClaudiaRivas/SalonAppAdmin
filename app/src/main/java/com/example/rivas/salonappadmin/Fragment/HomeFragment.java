package com.example.rivas.salonappadmin.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rivas.salonappadmin.Apputilities.FragmentConsultaFirebase;
import com.example.rivas.salonappadmin.Model.Item;
import com.example.rivas.salonappadmin.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class HomeFragment extends FragmentConsultaFirebase {

    View view;
    //firebase
    CollectionReference dbPromociones;
    //adaptador
    AdaptadorPromocion adaptadorPromocion;

    FloatingActionButton btnAddPromocion;

    private final String ruta_db  = "promociones";
    private final String ruta_img = "img_promociones";

    public HomeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);

       //instancia al listview
        ListView listView   = (ListView) view.findViewById(R.id.listview);
        btnAddPromocion     = view.findViewById(R.id.btnAddPromocion);
        //hacer instancia a la bd en firebase
        dbPromociones = FirebaseFirestore.getInstance().collection(ruta_db);
        imgFirebase   = FirebaseStorage.getInstance().getReference(ruta_img);


        if(adaptadorPromocion==null) {
            adaptadorPromocion = new AdaptadorPromocion(getContext(), listaItems);

            dbPromociones.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    actualizarDatos(queryDocumentSnapshots.getDocumentChanges());
                }
            });
        }

        //establecer el adaptador a la listview
        listView.setAdapter(adaptadorPromocion);


        btnAddPromocion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarPromocion();
            }
        });
        /** a la lista agregar el evento on click*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cargarFragmento(i);
            }
        });
        return view;
    }

    /**hacer este metodo en el fragmento*/
    private void cargarFragmento(int i){
        if (mFragmentNavigation != null) {
            DetailsFragment mFragment = new DetailsFragment();
            mFragment.setItem( listaItems.get(i) );
            mFragment.setTitulo("Promoción");
            mFragment.setRuta_db(ruta_db);
            mFragment.setRuta_img(ruta_img);
            mFragmentNavigation.pushFragment(mFragment);
        }
    }
    private void agregarPromocion(){
        if (mFragmentNavigation != null) {
            EditFragment mFragment = new EditFragment();
            mFragment.setRuta_db(ruta_db);
            mFragment.setRuta_img(ruta_img);
            mFragment.setTitulo("Agregar Promoción");
            mFragmentNavigation.pushFragment(mFragment);
        }
    }
    @Override
    protected void actualizarDatos(List<DocumentChange> cambios) {
        super.actualizarDatos(cambios);
        adaptadorPromocion.notifyDataSetChanged();
    }
    @Override
    protected void updateAdapterImage(int index) {
        super.updateAdapterImage(index);
        adaptadorPromocion.notifyDataSetChanged();
    }
    // Hacemos el metodo AdaptadorServicio
    private class AdaptadorPromocion extends ArrayAdapter<Item> {
        public AdaptadorPromocion(@NonNull Context context, @NonNull List<Item> objects) {
            super(context, 0, objects);
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.item_promociones, parent, false);

            //CurrentPromociones es la posicion en la que vamos a estar
            final Item item = listaItems.get(position);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.ImagenPromociones);

            if(item.getImgItem()!=null){
                imageView.setImageBitmap(item.getImgItem());
            }else{
                imageView.setImageResource(R.drawable.promo);
            }

            TextView TemaTxt = (TextView) itemView.findViewById(R.id.TxtTema);
            TemaTxt.setText(item.getNombre());

            TextView SubTemaTxt = (TextView) itemView.findViewById(R.id.TxtSubtema);
            SubTemaTxt.setText(item.getDescripcion());

            TextView PrecioTxt = (TextView) itemView.findViewById(R.id.TextPrecio);
            PrecioTxt.setText(item.getPrecio());
            return itemView;
        }
    }
}
