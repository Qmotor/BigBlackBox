package com.example.bigblackbox.tool;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolHelper {
    public static List<Map<String,String>> CursorToList(Cursor cursor){
        List<Map<String,String>> list=new ArrayList<>();
        int colums = cursor.getColumnCount();
        while (cursor.moveToNext()) {
            Map<String,String> map=new HashMap<>();
            for (int i = 0; i < colums; i++) {
                String columname = cursor.getColumnName(i);//获取每列的列名
                String columvalue = cursor.getString(cursor.getColumnIndex(columname));//获取每列的值
                map.put(columname,columvalue);
                if (columvalue == null) {
                    columvalue = "";
                }
            }
            list.add(map);
        }
        return list;
    }

    public static Cursor loadDB(Context context, String sql) {
        DbUtil mHelper = new DbUtil(context);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }
    public static boolean excuteDB(Context context, String sql) {
        DbUtil mHelper = new DbUtil(context);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.execSQL(sql);
        return true;
    }


}
