package com.example.rivas.salonappadmin.Activities;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.rivas.salonappadmin.Apputilities.BaseFragment;
import com.example.rivas.salonappadmin.Fragment.ContactFragment;
import com.example.rivas.salonappadmin.Fragment.HomeFragment;
import com.example.rivas.salonappadmin.Fragment.ProductFragment;
import com.example.rivas.salonappadmin.Fragment.ServiceFragment;
import com.example.rivas.salonappadmin.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ncapdevi.fragnav.FragNavController;
import com.ncapdevi.fragnav.FragNavTransactionOptions;

import javax.annotation.Nullable;

public class PrincipalActivity extends AppCompatActivity implements BaseFragment.FragmentNavigation, FragNavController.TransactionListener, FragNavController.RootFragmentListener{



    //para manejo de fragmentos
    private final int INDEX_HOME        = FragNavController.TAB1;
    private final int INDEX_PRODUCTS    = FragNavController.TAB2;
    private final int INDEX_SERVICES    = FragNavController.TAB3;
    private final int INDEX_CONTACT     = FragNavController.TAB4;

    public static FragNavController mNavController;

    private static HomeFragment frm1 = new HomeFragment();
    private static ProductFragment  frm2 = new ProductFragment();
    private static ServiceFragment frm3 = new ServiceFragment();
    private static ContactFragment  frm4 = new ContactFragment();


    DocumentReference mDocRef = FirebaseFirestore.getInstance().document("informacion/contacto");
    public static String txtWhatsapp;
    public static String txtDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);


        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //para manejo de fragmentos
        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.container)
                .transactionListener(this)
                .rootFragmentListener(this, 4)
                .defaultTransactionOptions(FragNavTransactionOptions.newBuilder().customAnimations(R.anim.alpha_in, R.anim.alpha_out,R.anim.alpha_in,R.anim.alpha_out).build())
                .build();

        //TODO prueba
        bottomNav.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                mNavController.clearStack();
            }
        });
        mDocRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    txtWhatsapp     = documentSnapshot.getString("whatsapp");
                    txtDirection    = documentSnapshot.getString("direccion");
                }
            }
        });
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            mNavController.switchTab(INDEX_HOME);
                            break;
                        case R.id.nav_product:
                            mNavController.switchTab(INDEX_PRODUCTS);
                            break;
                        case R.id.nav_service:
                            mNavController.switchTab(INDEX_SERVICES);
                            break;
                        case R.id.nav_contact:
                            mNavController.switchTab(INDEX_CONTACT);
                            break;
                    }
                    return true;
                }
            };
    //para manejo de fragmentos
    @Override
    public void onBackPressed() {
        if (!mNavController.isRootFragment()) {
            mNavController.popFragment();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (mNavController != null) {
            mNavController.pushFragment(fragment);
        }
    }

    @Override
    public Fragment getRootFragment(int i) {
        switch (i) {
            case INDEX_HOME:
                return frm1;
            case INDEX_PRODUCTS:
                return frm2;
            case INDEX_SERVICES:
                return frm3;
            case INDEX_CONTACT:
                return frm4;
        }
        throw new IllegalStateException("------Need to send an index that we know");
    }

    @Override
    public void onTabTransaction(Fragment fragment, int i) {

    }

    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {

    }



}

