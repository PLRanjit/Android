package com.manageexp.expensemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    private String[] foodMaterial = {"food", "breakfast", "lunch", "dinner", "Biryani", "sandwich", "pizza","dabeli","roti","chocolate"};
    private String[] shoppingCart = {"shopping", "shop", "clothes", "shoe",
            "shoes", "sandal", "sandals", "chapal", "chapals", "gold", "silver", "ornament"};
    private String[] GroceryCart = {"grocery", "oil", "rice", "dal", "vegetables", "tomato", "egg", "fruit", "juice"};
    private String[] Entertainment = {"entertainment", "movie", "circus", "zoo", "park", "trip"};
    private String[] Transport = {"cab", "rickshaw", "taxi", "to", "transport","bus","petrol","uber","ola"};

    Map<String, String> map;

    String[][] checkList = {foodMaterial, shoppingCart, GroceryCart, Entertainment, Transport};


    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String desc, String amt, String date) {
        String category = "OTHERS";
        int imageId = 0;

        for(int i = 0; i < checkList.length; i++){
            for(int j = 0; j < checkList[i].length; j++){
                if(Arrays.asList(name.toLowerCase().split(" ")).contains(checkList[i][j])){
                    if(i==0){
                        category = "FOOD";
                        //   imageId = R.drawable.ic_food;
                        break;
                    }else if(i==1){
                       category = "SHOPPING";
                        //    imageId = R.drawable.ic_shopping;
                        break;
                    }else if(i==2){
                        category = "GROCERY";
                        //    imageId = R.drawable.ic_grocery;
                        break;
                    }else if(i==3){
                        category = "ENTERTAINMENT";
                        //    imageId = R.drawable.ic_entertainment;
                        break;
                    }else if(i==4){
                        category = "TRANSPORT";
                        //    imageId = R.drawable.ic_transport;
                        break;
                    }else{
                        category = "OTHERS";
                        //                imageId = R.drawable.ic_others;
                        break;
                    }
                }
            }
        }

        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.SUBJECT, name);
        contentValue.put(DatabaseHelper.DESC, desc);
        contentValue.put(DatabaseHelper.AMOUNT, amt);
        contentValue.put(DatabaseHelper.DATE, date);
        contentValue.put(DatabaseHelper.CATG, category);
        //    contentValue.put(DatabaseHelper.IMAGES, imageId);
       database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.SUBJECT, DatabaseHelper.DESC, DatabaseHelper.AMOUNT, DatabaseHelper.DATE };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String name, String desc, String amount, String date) {

        String category = "OTHERS";
      //  int imageId = 0;
        for(int i = 0; i < checkList.length; i++){
            for(int j = 0; j < checkList[i].length; j++){
                if(Arrays.asList(name.toLowerCase().split(" ")).contains(checkList[i][j])){
                    if(i==0){
                        category = "FOOD";
                        //     imageId = R.drawable.ic_food;
                        break;
                    }else if(i==1){
                        category = "SHOPPING";
                        //   imageId = R.drawable.ic_shopping;
                        break;
                    }else if(i==2){
                        category = "GROCERY";
                        //    imageId = R.drawable.ic_grocery;
                        break;
                    }else if(i==3){
                        category = "ENTERTAINMENT";
                        //    imageId = R.drawable.ic_entertainment;
                        break;
                    }else if(i==4){
                        category = "TRANSPORT";
                        //    imageId = R.drawable.ic_transport;
                        break;
                    }else{
                        category = "OTHERS";
                        //    imageId = R.drawable.ic_others;
                        break;
                    }
                }
            }
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SUBJECT, name);
        contentValues.put(DatabaseHelper.DESC, desc);
        contentValues.put(DatabaseHelper.AMOUNT, amount);
        contentValues.put(DatabaseHelper.DATE, date);
        contentValues.put(DatabaseHelper.CATG, category);
        //  contentValues.put(DatabaseHelper.IMAGES, imageId);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

    public Cursor fetchPie(String dateMan) {

        String[] columns = new String[] { DatabaseHelper.CATG, "SUM("+DatabaseHelper.AMOUNT+") as 'EXPENSE'"};
        //Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, DatabaseHelper.CATG, null, null) ;
        String[] args = {dateMan};
        String whereClause = DatabaseHelper.DATE + " like ?";
        String[] whereArgs = new String[] {"%" + dateMan + "%"};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, whereClause, whereArgs, DatabaseHelper.CATG, null, null) ;
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

}
