package com.example.rivas.salonappadmin.Fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.rivas.salonappadmin.Activities.PrincipalActivity;
import com.example.rivas.salonappadmin.Apputilities.BaseFragment;
import com.example.rivas.salonappadmin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fcm.androidtoandroid.FirebasePush;
import fcm.androidtoandroid.connection.PushNotificationTask;
import fcm.androidtoandroid.model.Notification;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditContactFragment extends BaseFragment {

    EditText txtDesc, txtHorarioSemana,txtHorarioSabado,txtHorarioDomingo,txtDireccion,txtWhatsApp,txtFacebook,txtInstagram;
    CoordinatorLayout coordinatorLayout;
    FloatingActionButton btnSave;

    String desc,h_semana,h_sabado,h_domingo,dir,whatsapp,fb,instagram;

    DocumentReference mDocRef = FirebaseFirestore.getInstance().document("informacion/contacto");


    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private final int MINUTOS = 5;

    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    public void setInfo(String desc,String h_semana,String h_sabado,String h_domingo,String dir,String whatsapp,String fb,String instagram){
        this.desc = desc;
        this.h_semana = h_semana;
        this.h_sabado = h_sabado;
        this.h_domingo = h_domingo;
        this.dir = dir;
        this.whatsapp = whatsapp;
        this.fb = fb;
        this.instagram = instagram;
    }

    public EditContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_contact, container, false);

        coordinatorLayout   = view.findViewById(R.id.frmEditContact);

        txtDesc             = view.findViewById(R.id.txtEditInformacion);
        txtHorarioSemana    = view.findViewById(R.id.txtEditLunesViernes);
        txtHorarioSabado    = view.findViewById(R.id.txtEditSabado);
        txtHorarioDomingo   = view.findViewById(R.id.txtEditDomingo);
        txtDireccion        = view.findViewById(R.id.txtEditDireccion);
        txtWhatsApp         = view.findViewById(R.id.txtEditWhatsapp);
        txtFacebook         = view.findViewById(R.id.txtEditFacebook);
        txtInstagram        = view.findViewById(R.id.txtEditInstagram);

        btnSave             = view.findViewById(R.id.btnSaveContact);

        txtDesc.setText(desc);
        txtHorarioSemana.setText(h_semana);
        txtHorarioSabado.setText(h_sabado);
        txtHorarioDomingo.setText(h_domingo);
        txtDireccion.setText(dir);
        txtWhatsApp.setText(whatsapp);
        txtFacebook.setText(fb);
        txtInstagram.setText(instagram);

        editor  = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        prefs   = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(camposValidos()){
                    guardarDatos();
                }else{
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Debes llenar los campos", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
        return view;
    }

    private boolean camposValidos(){
        return ( !txtDesc.getText().toString().isEmpty() && !txtHorarioSemana.getText().toString().isEmpty() && !txtHorarioSabado.getText().toString().isEmpty() && !txtHorarioDomingo.getText().toString().isEmpty() && !txtDireccion.getText().toString().isEmpty() && !txtWhatsApp.getText().toString().isEmpty() && !txtFacebook.getText().toString().isEmpty() && !txtInstagram.getText().toString().isEmpty() );
    }

    private void guardarDatos(){


        Map<String,Object> dataToSave = new HashMap<>();
        dataToSave.put("informacion_salon",txtDesc.getText().toString());
        dataToSave.put("horarios_lunes_viernes",txtHorarioSemana.getText().toString());
        dataToSave.put("horarios_sabado",txtHorarioSabado.getText().toString());
        dataToSave.put("horarios_domingo",txtHorarioDomingo.getText().toString());
        dataToSave.put("direccion",txtDireccion.getText().toString());
        dataToSave.put("whatsapp",txtWhatsApp.getText().toString());
        dataToSave.put("facebook",txtFacebook.getText().toString());
        dataToSave.put("instagram",txtInstagram.getText().toString());

        mDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Datos guardados", Snackbar.LENGTH_LONG);
                snackbar.show();
                PrincipalActivity.mNavController.clearStack();

                if(lanzarNotificacion()){
                    notificacion();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Ocurrio un error al intentar guardar", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
        //PrincipalActivity.mNavController.clearStack();
    }



    //lanza notififacion cada X MINUTOS
    private boolean lanzarNotificacion(){
        Date now = new Date();
        String fecha_previa = prefs.getString("date",null);
        if(fecha_previa!=null){
            Date previous = getDateHoraFecha(fecha_previa);
            if (now.getTime() - previous.getTime() >= MINUTOS*60*1000) {
                fecha_previa = getStringHoraFecha(now);
                editor.putString("date",fecha_previa);
                editor.apply();
                return true;
            }else{
                return false;
            }
        }else{
            fecha_previa = getStringHoraFecha(now);
            editor.putString("date",fecha_previa);
            editor.apply();
            return true;
        }
    }
    private Date getDateHoraFecha(String horaFecha){
        String pattern = "yyyy-MM-dd hh:mm:ss a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(horaFecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    private String getStringHoraFecha(Date today){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);
        return dateToStr;
    }

    private void notificacion(){
        String contenido = "Información sobre el Salón actualizada, mirala ahora!";

        FirebasePush firebasePush = new FirebasePush("AIzaSyBJz_QHlWmyXf0QAybSR1EHfdUzyo5d5UE");

        firebasePush.setAsyncResponse(new PushNotificationTask.AsyncResponse() {
            @Override
            public void onFinishPush(@NotNull String ouput) {
                Log.e("NOTIFICACION", ouput);
            }
        });
        firebasePush.setNotification(new Notification("Salón M&N",contenido));
        firebasePush.sendToTopic("salon");
    }

}
