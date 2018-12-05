package com.example.rivas.salonappadmin.Apputilities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

public class BaseFragment extends Fragment {
    protected FragmentNavigation mFragmentNavigation;
    int mInt = 0;

    public static final String ARGS_INSTANCE = "instance";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mInt = args.getInt(ARGS_INSTANCE);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation) {
            mFragmentNavigation = (FragmentNavigation) context;
        }
    }

    public interface FragmentNavigation {
        public void pushFragment(Fragment fragment);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("CAMBIO","Cambios en : on destroy ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("CAMBIO","Cambios en : on resume ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("CAMBIO","Cambios en : on start ");
    }
}
