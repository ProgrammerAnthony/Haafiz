package edu.com.app.ui.find.shake;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anthony.ultimateswipetool.dialogFragment.SwipeDialogFragment;

import java.util.Calendar;

import edu.com.app.R;
//import edu.com.app.widget.swipeDialogFragment.SwipeAwayDialogFragment;

/**
 * Created by Anthony on 2016/6/28.
 * Class Note:
 * test {@link SwipeDialogFragment}
 */
public class ShakeDialogFragment extends SwipeDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle("Title")
                .setIcon(R.mipmap.ic_launcher)
                .setItems(new String[]{
                        "Item 1",
                        "Item 2",
                        "Item 3",
                }, null)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", null)
                .create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setTiltEnabled(boolean tiltEnabled) {
        super.setTiltEnabled(tiltEnabled);
    }
}
