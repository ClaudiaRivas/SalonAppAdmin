package com.example.rivas.salonappadmin.Apputilities;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridDecoracion extends RecyclerView.ItemDecoration {

    private int elementos;
    private int espacio;
    private boolean incluyeBorde;

    public GridDecoracion(int elementos, int espacio, boolean incluyeBorde) {
        this.elementos = elementos;
        this.espacio = espacio;
        this.incluyeBorde = incluyeBorde;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % elementos; // item column

        if (incluyeBorde) {
            outRect.left = espacio - column * espacio / elementos; // espacio - column * ((1f / elementos) * espacio)
            outRect.right = (column + 1) * espacio / elementos; // (column + 1) * ((1f / elementos) * espacio)

            if (position < elementos) { // top edge
                outRect.top = espacio;
            }
            outRect.bottom = espacio; // item bottom
        } else {
            outRect.left = column * espacio / elementos; // column * ((1f / elementos) * espacio)
            outRect.right = espacio - (column + 1) * espacio / elementos; // espacio - (column + 1) * ((1f /    elementos) * espacio)
            if (position >= elementos) {
                outRect.top = espacio; // item top
            }
        }
    }
}