package hu.bme.onlab.ui;

import android.content.DialogInterface;

public class DialogDismissListener implements DialogInterface.OnClickListener {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
}
