package com.kyra.expensemanager;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */

public class ProfileFragment extends Fragment {

    DBManager dbManager;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final DBManager dbManager = new DBManager(getContext());
        Bundle bundle = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        assert bundle != null;
        String  id = (String) bundle.get("UserID");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView tvUsername = view.findViewById(R.id.tvUsername);
        Cursor cursor = dbManager.getCurrentUser(id);
        cursor.moveToFirst();
        tvUsername.setText(cursor.getString(cursor.getColumnIndex(DBManager.colUser)));
        return view;
    }

}
