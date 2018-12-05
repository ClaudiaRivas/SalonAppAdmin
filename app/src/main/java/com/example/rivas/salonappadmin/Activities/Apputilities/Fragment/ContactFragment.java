package com.example.rivas.salonappadmin.Activities.Apputilities.Fragment;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rivas.salon_rmr.Apputilities.BaseFragment;
import com.example.rivas.salon_rmr.Apputilities.Contact;
import com.example.rivas.salon_rmr.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ContactFragment extends BaseFragment {
    View view;
    CoordinatorLayout layoutContact;
    DocumentReference mDocRef = FirebaseFirestore.getInstance().document("informacion/contacto");
    TextView txtHorarioLunes_Viernes,txtHorarioSabado,txtWhatsapp, txtinformacion,txtFacebook,txtInstagram, txtHorarioDomingo, txtIntegrante1, txtIntegrante2, txtIntegrante3,txtCorreoIntegrante,txtdireccion,txtAbrirMapa;
    CardView cardViewWhatsapp,cardViewFacebook,cardViewInstagram;

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    String msg     = "Estoy interesado en conocer más sobre el Salón";

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_contact, container, false);

        layoutContact           = (CoordinatorLayout) view.findViewById(R.id.frmContact);

        txtHorarioLunes_Viernes = (TextView) view.findViewById(R.id.txtHorarioLunes_Viernes);
        txtHorarioSabado        = (TextView) view.findViewById(R.id.txtHorarioSabado);
        txtHorarioDomingo       = (TextView) view.findViewById(R.id.txtHorarioDomingo);
        txtWhatsapp             = (TextView) view.findViewById(R.id.txtWhatsapp);
        txtFacebook             = (TextView) view.findViewById(R.id.txtFacebook);
        txtInstagram            = (TextView) view.findViewById(R.id.txtInstagram);
        txtCorreoIntegrante     = (TextView) view.findViewById(R.id.txtCorreoIntegrante);
        txtdireccion            = (TextView) view.findViewById(R.id.txtdireccion);
        txtIntegrante1          = (TextView) view.findViewById(R.id.txtIntegrante1);
        txtIntegrante2          = (TextView) view.findViewById(R.id.txtIntegrante2);
        txtIntegrante3          = (TextView) view.findViewById(R.id.txtIntegrante3);
        txtinformacion          = (TextView) view.findViewById(R.id.txtinformacion);
        txtAbrirMapa            = (TextView) view.findViewById(R.id.txtAbrirMapa);


        cardViewWhatsapp        = (CardView)view.findViewById(R.id.cardViewWhatsapp);
        cardViewFacebook        = (CardView)view.findViewById(R.id.cardViewFacebook);
        cardViewInstagram       = (CardView)view.findViewById(R.id.cardViewInstagram);

        cardViewWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAndRequestPermissions()){
                    String number  = txtWhatsapp.getText().toString();//.trim().replaceAll("\\D", "");
                    Contact.showWhatsApp(getContext(),layoutContact,number,msg);
                }
            }
        });

        cardViewFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ESO VA EN EL CARDVIEW DEL FACEBOOK
                try{
                    //TODO pendiente referencia
                    String facebookId = "fb://page/244840368866574";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookId )));
                }catch (Exception ex){

                    Snackbar snackbar = Snackbar
                            .make(layoutContact, "Error al abrir Facebook", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });

        cardViewInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ESTO EN EL CARDVIEW DEL INSTAGRAM
                try {
                    Uri uri = Uri.parse("http://instagram.com/milideescobar/?utm_source=ig_profile_share&igshid=1pngwgpz4nis8");
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                    likeIng.setPackage("com.instagram.android");
                    try {
                        startActivity(likeIng);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://instagram.com/milideescobar/?utm_source=ig_profile_share&igshid=1pngwgpz4nis8")));
                    }
                }catch (Exception ex){
                    Snackbar snackbar = Snackbar
                            .make(layoutContact, "Error al abrir Instagram", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });

        txtAbrirMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irAlMapa();
            }
        });

        mDocRef.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    actualizar(documentSnapshot);
                }
            }
        });
        return view;
    }

    private void irAlMapa() {
        if (mFragmentNavigation != null) {
            MapFragment mapFragment = new MapFragment();
            mFragmentNavigation.pushFragment(mapFragment);
        }
    }

    private void actualizar(DocumentSnapshot doc){
        txtHorarioLunes_Viernes.setText( "Lunes a viernes : "+doc.getString("horarios_lunes_viernes")  );
        txtHorarioSabado.setText( "Sábado : "+ doc.getString("horarios_sabado")  );
        txtHorarioDomingo.setText( "Domingo : "+ doc.getString("horarios_domingo")  );
        txtWhatsapp.setText( doc.getString("whatsapp")  );
        txtFacebook.setText( doc.getString("facebook")  );
        txtInstagram.setText( doc.getString("instagram")  );
        txtIntegrante1.setText( doc.getString("integrante1")  );
        txtIntegrante2.setText( doc.getString("integrante2")  );
        txtIntegrante3.setText( doc.getString("integrante3")  );
        txtdireccion.setText( doc.getString("direccion")  );
        txtCorreoIntegrante.setText( doc.getString("correo_integrante")  );
        txtinformacion.setText( doc.getString("informacion_salon")  );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                String number  = txtWhatsapp.getText().toString();//.trim().replaceAll("\\D", "");

                Contact.showWhatsApp(getContext(),layoutContact,number,msg);
            } else {
                Snackbar snackbar = Snackbar
                        .make(layoutContact, "Error al buscar en contactos", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    private  boolean checkAndRequestPermissions() {
        int permissionREAD_CONTACT = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS);
        int permissionWRITE_CONTACT = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CONTACTS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionWRITE_CONTACT != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS);
        }
        if (permissionREAD_CONTACT != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


}
