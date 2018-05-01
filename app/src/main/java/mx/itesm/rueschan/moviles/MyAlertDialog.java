package mx.itesm.rueschan.moviles;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;


public class MyAlertDialog extends DialogFragment {

    private  String errorMessage;

    public MyAlertDialog(){

    }

    @SuppressLint("ValidFragment")
    public MyAlertDialog(String errorMsg) {
        errorMessage = errorMsg;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

         // set Dialog Title
        builder.setTitle("Sorry")
                // Set Dialog Message
                .setMessage(errorMessage)

                // positive button
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

         return builder.create();

    }

}
