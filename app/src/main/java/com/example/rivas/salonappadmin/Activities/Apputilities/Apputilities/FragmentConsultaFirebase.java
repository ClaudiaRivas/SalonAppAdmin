package com.example.rivas.salonappadmin.Activities.Apputilities.Apputilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.rivas.salon_rmr.Model.Item;
import com.example.rivas.salonappadmin.Activities.Apputilities.Apputilities.BaseFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FragmentConsultaFirebase extends BaseFragment {

    //adaptador
    protected RecyclerView.Adapter adaptadorItems;
    //lista
    protected ArrayList<Item> listaItems = new ArrayList<>();

    protected final long ONE_MEGABYTE = 1024 * 1024;

    public static StorageReference imgFirebase;

    protected void actualizarDatos(List<DocumentChange> cambios) {
        //para cada documento cambiado
        for(DocumentChange document_changed: cambios){
            //obtengo el documento
            DocumentSnapshot document = document_changed.getDocument();

            Log.d("CAMBIO","Cambios en documento "+ document.getString("nombre"));
            //obtengo la posicion del producto basado en el Id
            int posicion = posicionItem(document.getId());
            //si el documento fue eliminado
            if(document_changed.getType()==DocumentChange.Type.REMOVED){
                //se elimina de la lista tambien
                listaItems.remove(posicion);
            }else {
                //obtengo un objeto promocion del documento
                Item item = getItem(document);

                if(item.getRuta_img()!=null){
                    getImageFirebase(item);
                }else{
                    item.setImgItem(null);
                }

                //si la posicion es mayor a cero es por que existe en la lista y se actualiza
                if (posicion >= 0) {
                    listaItems.set(posicion, item);
                } else {
                    //si no , es por que es un elemento nuevo
                    listaItems.add(item);
                }
            }
        }
        //notifico al adaptador de los cambios
        if(adaptadorItems!=null){
            adaptadorItems.notifyDataSetChanged();
        }
    }

    protected int posicionItem(String id) {
        for(Item item : listaItems) {
            if(item.getId().equals(id)) {
                return listaItems.indexOf(item);
            }
        }
        return -1;
    }

    public Item getItem(DocumentSnapshot document) {
        Item item = new Item();
        //establecer parametros
        item.setId(document.getId());
        item.setNombre(document.getString("nombre"));
        item.setDescripcion(document.getString("descripcion"));
        item.setPrecio(document.getString("precio"));
        item.setRuta_img(document.getString("ruta_imagen"));
        return item;
    }

    private void getImageFirebase(final Item item){
        if(imgFirebase!=null){
            Log.d("IMAGEN","Entro en : getImagen con ruta "+item.getRuta_img()+" ID "+item.getId());
            String path = item.getId()+".jpg";
            imgFirebase.child(path).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    final int pos = posicionItem(item.getId());
                    Bitmap imagen;
                    try{
                        imagen= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    }catch (Exception ex){
                        imagen=null;
                    }

                    listaItems.get(pos).setImgItem(imagen);
                    updateAdapterImage(pos);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("IMAGEN","Entro en : Fail listener");
                }
            })
            ;
        }
    }

    protected void updateAdapterImage(int index){
        Log.d("IMAGEN","Entro en : updateAdapter");
        if(adaptadorItems!=null){
            Log.d("IMAGEN","Entro en : notifyItemChanged");
            adaptadorItems.notifyItemChanged(index);
        }
    }

}
