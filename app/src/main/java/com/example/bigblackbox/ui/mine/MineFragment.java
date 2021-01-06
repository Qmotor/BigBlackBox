package com.example.bigblackbox.ui.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bigblackbox.R;

public class MineFragment extends Fragment {

    private MineViewModel mineViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mineViewModel =
                ViewModelProviders.of(this).get(MineViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mine, container, false);
        final TextView textView = root.findViewById(R.id.text_mine);
        mineViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}