package com.example.morina.mylawdialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class AlertDialog {

    public void showFirstUseDialog(final Context context){
        android.app.AlertDialog.Builder build1 = new android.app.AlertDialog.Builder(context);
        build1.setMessage(R.string.dialog_first_use);
        build1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, AddLawActivity.class);
                dialog.dismiss();
                context.startActivity(intent);
            }
        });
        android.app.AlertDialog dialog1 = build1.create();
        dialog1.show();
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
    }
}
