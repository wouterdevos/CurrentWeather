package com.wouterdevos.currentweather.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.wouterdevos.currentweather.R;

public class PermissionDialogFragment extends DialogFragment {

    public static final String TAG = PermissionDialogFragment.class.getSimpleName();

    public static final String ARG_NEVER_SHOW_AGAIN = "ARG_NEVER_SHOW_AGAIN";

    public PermissionDialogFragment() {
        // Required empty public constructor
    }

    public static PermissionDialogFragment newInstance(boolean neverShowAgain) {
        PermissionDialogFragment fragment = new PermissionDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_NEVER_SHOW_AGAIN, neverShowAgain);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        boolean neverShowAgain = false;
        if (getArguments() != null) {
            neverShowAgain = getArguments().getBoolean(ARG_NEVER_SHOW_AGAIN);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);

        if (neverShowAgain) {
            builder.setMessage(R.string.message_request_permission_rationale);
            builder.setNegativeButton(android.R.string.no, null);
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startApplicationDetailsSettingsActivity();
                    dialog.dismiss();
                }
            });
        } else {
            builder.setMessage(R.string.message_location_rationale);
            builder.setPositiveButton(android.R.string.ok, null);
        }

        return builder.create();
    }

    private void startApplicationDetailsSettingsActivity() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + getContext().getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
    }
}
