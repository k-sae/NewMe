package com.kareem.newme.Authentication;

/**
 * Created by kareem on 8/7/17.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kareem.newme.R;

public class CustomToast {
    public static final int TYPE_WARNING = 0;
    public static final int TYPE_SUCCESS = 1;
    private int type = 0;

    public CustomToast setType(int type)
    {
        this.type = type;
        return this;
    }
    // Custom Toast Method
    public void Show_Toast(Context context, View view, String error) {

        // Layout Inflater for inflating custom view
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate the layout over view
        View layout;
        if (type == 1)
        {
           layout =  inflater.inflate(R.layout.custom_toast_successful,
                    (ViewGroup) view.findViewById(R.id.toast_root));
        }
        else
        {
            layout =  inflater.inflate(R.layout.custom_toast_warning,
                    (ViewGroup) view.findViewById(R.id.toast_root));
        }

        // Get TextView id and set error
        TextView text = (TextView) layout.findViewById(R.id.toast_error);
        text.setText(error);

        Toast toast = new Toast(context);// Get Toast Context
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
        // Toast
        // gravity
        // and
        // Fill
        // Horizoontal

        toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
        toast.setView(layout); // Set Custom View over toast

        toast.show();// Finally show toast
    }

}