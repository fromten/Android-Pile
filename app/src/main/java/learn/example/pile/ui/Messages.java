package learn.example.pile.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import learn.example.pile.R;

/**
 * Created on 2016/12/16.
 */

public class Messages {

    public static void showMessages(Context context,
                                    CharSequence title, CharSequence content,
                                    DialogInterface.OnClickListener listener)
    {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(content)
                .setNegativeButton(R.string.cancel,listener)
                .setPositiveButton(R.string.ok,listener)
                .show();
    }
}
