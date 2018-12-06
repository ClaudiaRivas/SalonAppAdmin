package com.example.rivas.salonappadmin.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rivas.salonappadmin.Activities.PrincipalActivity;
import com.example.rivas.salonappadmin.Apputilities.BaseFragment;
import com.example.rivas.salonappadmin.Apputilities.UtilidadesImagenes;
import com.example.rivas.salonappadmin.Model.Item;
import com.example.rivas.salonappadmin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import fcm.androidtoandroid.FirebasePush;
import fcm.androidtoandroid.connection.PushNotificationTask;
import fcm.androidtoandroid.model.Notification;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends BaseFragment {

    TextView lblTitulo;
    ImageView imgItem;
    EditText txtNombre,txtDesc,txtPrecio;
    Button btnRotar,btnCambiar, btnEliminarImg,btnCancelar,btnGuardar;
    Item item;
    String ruta_db,ruta_img,titulo;

    CollectionReference dbFirebase;
    StorageReference imgFirebase;

    Bitmap bitmapItem;
    boolean cambioImagen =false;

    CoordinatorLayout layoutContact;


    //identificador del permiso de lectura de imagen
    final int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 23;
    final int IMAGE_CODE_REQUEST=34;


    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private final int MINUTOS = 5;

    SharedPreferences.Editor editor;
    SharedPreferences prefs;


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



        editor  = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        prefs   = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        layoutContact = view.findViewById(R.id.layoutEdit);

        lblTitulo   = view.findViewById(R.id.lblEditInformacion);
        imgItem     = view.findViewById(R.id.imgEditElemento);
        txtNombre   = view.findViewById(R.id.txtEditNombre);
        txtDesc     = view.findViewById(R.id.txtEditDescripcion);
        txtPrecio   = view.findViewById(R.id.txtEditPrecio);
        btnRotar    = view.findViewById(R.id.btnEditRotarImg);
        btnCambiar  = view.findViewById(R.id.btnEditCambiarImg);
        btnEliminarImg  = view.findViewById(R.id.btnEditEliminarImg);

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
            dbFirebase  = FirebaseFirestore.getInstance().collection(ruta_db);
            imgFirebase = FirebaseStorage.getInstance().getReference(ruta_img);
        }


        btnCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarImg();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepararSubidaProducto();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        btnEliminarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarImagen();
            }
        });

        btnRotar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmapItem!=null){
                    cambioImagen=true;
                    bitmapItem = UtilidadesImagenes.rotarImagen(bitmapItem);
                    imgItem.setImageBitmap(bitmapItem);
                }
            }
        });

        return view;
    }



    private void seleccionarImg() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            //ask for permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_CODE);
            }
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGE_CODE_REQUEST);
        }
    }


    private boolean camposValido(){
        return !txtPrecio.getText().toString().isEmpty() && !txtNombre.getText().toString().isEmpty();
    }

    private void prepararSubidaProducto(){
        if(camposValido()) {
            DocumentReference referenciaProducto;

            String nombre = txtNombre.getText().toString();
            String precio = txtPrecio.getText().toString();
            String desc   = txtDesc.getText().toString();

            HashMap<String,Object> productoBD = new HashMap<>();
            productoBD.put("nombre",nombre);
            productoBD.put("precio",precio);
            productoBD.put("descripcion",desc);
            //si el producto que tengo no es nulo es por que es una edicion
            if(item!=null){
                referenciaProducto  = dbFirebase.document(item.getId());
            }else{
                referenciaProducto  = dbFirebase.document();
            }
            //si la imagen cambio entonces se procede a
            if(cambioImagen){
                actualizarImagen(referenciaProducto,productoBD);
            }else{
                guardarDatos(referenciaProducto,productoBD,true);
            }
        }else{
            Snackbar snackbar = Snackbar
                    .make(layoutContact, "Debe llenar nombre y precio", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }



    private void actualizarImagen(final DocumentReference referencia, final HashMap<String,Object> datos){
        //id de la imagen
        String id_img = referencia.getId()+".jpg";
        //referencia a la imagen en firebase
        final StorageReference refImgFirebase = imgFirebase.child(id_img);
        if(bitmapItem!=null ){
            datos.put("ruta_imagen",FieldValue.delete());
            guardarDatos(referencia,datos,false);
            //establecememos la ruta
            datos.put("ruta_imagen",id_img);

            //imagen en bytes
            byte[] imagenEnBytes = UtilidadesImagenes.getImagenEnBytes(bitmapItem);

            //supervisa el estado de la carga
            refImgFirebase
                    .putBytes(imagenEnBytes)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            guardarDatos(referencia,datos,true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Snackbar snackbar = Snackbar
                                    .make(layoutContact, "Error al subir imagen", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });


            //en caso de liminacion, si el producto tiene datos
        }else if(item!=null){
            //si tiene una ruta de imagen entonces se borra
            if(item.getRuta_img()!=null){
                datos.put("ruta_imagen",FieldValue.delete());
                //borrando de firebase
                refImgFirebase
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                guardarDatos(referencia,datos,true);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        }
    }

    private void guardarDatos(final DocumentReference referencia, final HashMap<String,Object> datos, final boolean cerrar){
        referencia.set(datos,SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        if(lanzarNotificacion()){
                            notificacion();
                        }

                        if(cerrar){
                            Snackbar snackbar = Snackbar
                                    .make(layoutContact, "Datos guardados", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            PrincipalActivity.mNavController.clearStack();
                            //getActivity().onBackPressed();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar snackbar = Snackbar
                                .make(layoutContact, "Ha ocurrido un error al intentar guardar", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
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
        String contenido = titulo.replaceAll("Editar","");
        contenido = contenido.replaceAll("Agregar","");
        contenido = contenido.replaceAll("Promoción","Promocione");

        if(contenido.contains("Producto") || contenido.contains("Servicio")){
            contenido+="s actualizados, Miralos ahora!";
        }else{
            contenido+="s actualizadas, Miralas ahora!";
        }

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

    private void limpiarImagen(){
        cambioImagen=true;
        bitmapItem=null;
        item.setImgItem(null);
        imgItem.setImageResource(R.drawable.ic_product);
        btnEliminarImg.setEnabled(false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_CODE_REQUEST && resultCode==RESULT_OK){

            Uri selectedImage = data.getData();
            Bitmap bmp = null;
            try {
                bmp = UtilidadesImagenes.getImagenDesdeRuta(getContext(),selectedImage);
            } catch (IOException e) {

                Snackbar snackbar = Snackbar
                        .make(layoutContact, "Error al cargar imagen", Snackbar.LENGTH_LONG);
                snackbar.show();
                e.printStackTrace();
            }
            if(bmp!=null){
                //se reduce la imagen
                bmp = UtilidadesImagenes.reducirImagen(bmp);
                //si la reduccion fue exitosa retorna un bitmap reducido
                if(bmp!=null){
                    cambioImagen =true;
                    bitmapItem = bmp;
                    imgItem.setImageBitmap(bitmapItem);
                    btnEliminarImg.setEnabled(true);
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==READ_EXTERNAL_STORAGE_PERMISSION_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                seleccionarImg();
            }else{
                Snackbar snackbar = Snackbar
                        .make(layoutContact, "Se debe otorgar el permiso", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }
}
