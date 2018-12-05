package com.example.rivas.salonappadmin.Apputilities;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.ArrayList;

public class Contact {


    public enum APP {
        WHATSAPP, CALL, MESSAGE
    }


    public static boolean contactExists(Context context, String number) {
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                return true;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }


    public static void saveContact(Context context, CoordinatorLayout layoutContact, String number, String msg, APP option){
        String DisplayName = "Salón M&N";
        String MobileNumber = number;

        ArrayList< ContentProviderOperation > ops = new ArrayList < ContentProviderOperation > ();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        if (DisplayName != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            DisplayName).build());
        }

        if (MobileNumber != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }
        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            switch (option){
                case WHATSAPP:
                    sendWhatsAppMessage(context,layoutContact,number,msg);
                    break;
                case MESSAGE:
                    break;
                case CALL:

            }
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar snackbar = Snackbar
                    .make(layoutContact, "Error al intentar guardar el contacto", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
    private static void sendWhatsAppMessage(Context context, CoordinatorLayout layoutContact, String number, String msg){
        msg = msg.replaceAll(" ","%20");

        try{
            String url = "https://api.whatsapp.com/send?phone="+number+"&text="+msg;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        }catch (Exception ex){
            Snackbar snackbar = Snackbar
                    .make(layoutContact, "Error al abrir WhatsApp", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }


    public static void showWhatsApp(final Context context, final CoordinatorLayout layoutContact, final String number, final String msg) {

        if(contactExists(context,number)){

            Snackbar snackbar = Snackbar
                    .make(layoutContact, "Contactar con el Salon", Snackbar.LENGTH_LONG);
            snackbar.setAction("Contactar", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendWhatsAppMessage(context,layoutContact,number,msg);
                }
            });
            snackbar.setActionTextColor(context.getResources().getColor(android.R.color.holo_blue_light));
            snackbar.show();
        }else{
            Snackbar snackbar = Snackbar
                    .make(layoutContact, "No se encontro el contacto", Snackbar.LENGTH_LONG);
            snackbar.setAction("Agregar", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveContact(context,layoutContact,number,msg, Contact.APP.WHATSAPP);
                }
            });
            snackbar.setActionTextColor(context.getResources().getColor(android.R.color.holo_orange_light));
            snackbar.show();
        }
    }


}
