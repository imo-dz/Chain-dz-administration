package com.example.chaindzadministration.Controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.chaindzadministration.R;


public class LoadingDialog
{
    Activity activity;
    AlertDialog dialog;
    public LoadingDialog (Activity activity)
    {
        this.activity=activity;
    }
    public void start()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.loading_dialog_layout, null);
        builder.setView(dialogView);



        builder.setCancelable(false);
        dialog =builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        ImageView loadingImage = dialogView.findViewById(R.id.logo_loading);
        Animation rotation = AnimationUtils.loadAnimation(activity, R.anim.rotate);
        loadingImage.startAnimation(rotation);

    }
    public void dismiss()
    {
        dialog.dismiss();
    }
}
