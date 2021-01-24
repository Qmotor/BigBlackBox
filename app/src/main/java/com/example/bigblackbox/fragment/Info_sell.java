package com.example.bigblackbox.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bigblackbox.DbUtil;
import com.example.bigblackbox.R;
import com.example.bigblackbox.adpater.PostingAdpater;
import com.example.bigblackbox.entity.Posting;
import com.example.bigblackbox.Post_detail;

import java.util.ArrayList;
import java.util.List;

public class Info_sell extends Fragment {
    private PostingAdpater mPostingAdpater;
    private List<Posting> p = new ArrayList<>();
    SQLiteOpenHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new DbUtil(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_info_sell, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ListView listView = view.findViewById(R.id.infoSellList);

        mPostingAdpater = new PostingAdpater(getContext(), p);
        listView.setAdapter(mPostingAdpater);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Posting posting = p.get(position);
                Intent intent = new Intent(getContext(), Post_detail.class);
                intent.putExtra("postID", posting.getPostID());
                startActivity(intent);
            }
        });
        showSellData();
    }
    private void showSellData(){
        p.clear();
        try (SQLiteDatabase db = helper.getReadableDatabase()) {
            try (Cursor cursor = db.rawQuery("select * from posting where postFollow = 3 order by postTime desc", new String[0])) {
                while (cursor.moveToNext()) {
                    p.add(new Posting(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5)));
                }
            }
        }
        mPostingAdpater.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        showSellData();
    }

}
