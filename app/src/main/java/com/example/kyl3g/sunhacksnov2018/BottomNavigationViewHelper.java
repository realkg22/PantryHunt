package com.example.kyl3g.sunhacksnov2018;

import android.annotation.SuppressLint;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;

import java.lang.reflect.Field;

public class BottomNavigationViewHelper {
    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView navBar = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = navBar.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(navBar, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < navBar.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) navBar.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }
}
