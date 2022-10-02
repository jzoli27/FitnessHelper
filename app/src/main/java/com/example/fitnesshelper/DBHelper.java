package com.example.fitnesshelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.fitnesshelper.models.Altgyakorlatmodell;
import com.example.fitnesshelper.models.Machine;

import java.util.ArrayList;
import java.util.List;

public class DBHelper  extends SQLiteOpenHelper {

    public static final String DBNAME = "Login.db";

    public DBHelper(@Nullable Context context) {
        super(context, "Login.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("CREATE TABLE Altalanos_gyakorlat(altalanos_gyakorlat_id INTEGER primary key autoincrement," +
                "gyakorlat_neve TEXT, ajanlott_mennyiseg INTEGER, izomcsoport_id INTEGER )");
        MyDB.execSQL("CREATE TABLE Gep(gep_id INTEGER primary key autoincrement, " +
                "gep_megnevezese TEXT, gep_kepe TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists Altalanos_gyakorlat");
        MyDB.execSQL("drop Table if exists Gep");
    }

    public Boolean insertData(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result =MyDB.insert("users",null,contentValues);
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Boolean checkusername(String username){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[] {username});
        if (cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }

    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username ,password});
        if (cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }


    public List<Altgyakorlatmodell> getEverything(){
        List<Altgyakorlatmodell> returnlList = new ArrayList<>();

        String query = " SELECT * FROM Altalanos_gyakorlat ";

        SQLiteDatabase db = this.getReadableDatabase();
        //Cursorban benne a lesz a lekérdezés eredménye
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()){
            //Ha van a cursorban 1 elem akkor megnézem a többit is és beleteszem a listába
            do {
                int altalanos_gyakorlat_id = cursor.getInt(0);
                String gyakorlat_neve = cursor.getString(1);
                int ajanlott_mennyiseg = cursor.getInt(2);
                int izomcsoport_id = cursor.getInt(3);

                Altgyakorlatmodell altgyakorlatmodell = new Altgyakorlatmodell(
                        altalanos_gyakorlat_id,gyakorlat_neve, ajanlott_mennyiseg, izomcsoport_id
                );
                returnlList.add(altgyakorlatmodell);
            } while (cursor.moveToNext());

        }else{
            // hiba, nem adunk vissza semmit
        }
        cursor.close();
        db.close();
        return returnlList;
    }

    public boolean addOne(Machine machine){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("gep_megnevezese",machine.getGep_megnevezese());
        cv.put("gep_kepe",machine.getGep_kepe());

        long insert = db.insert("Gep", null, cv);
        if (insert == -1){
            return false;
        }else{
            return true;
        }

    }

    public boolean addToExercises(Altgyakorlatmodell altgyakorlatmodell){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("gyakorlat_neve",altgyakorlatmodell.getGyakorlat_neve());
        cv.put("ajanlott_mennyiseg",altgyakorlatmodell.getAjanlott_mennyiseg());
        cv.put("izomcsoport_id",altgyakorlatmodell.getIzomcsoport_id());

        long insert = db.insert("Altalanos_gyakorlat", null, cv);
        if (insert == -1){
            return false;
        }else{
            return true;
        }

    }

    public boolean deleteOneExercise(String gyakorlatnev){
        SQLiteDatabase db = this.getWritableDatabase();
        String querryString = "DELETE FROM Altalanos_gyakorlat WHERE gyakorlat_neve = '"+gyakorlatnev+"'" ;
        //String queryCheck = "SELECT * FROM Altalanos_gyakorlat WHERE gyakorlat_neve = '"+gyakorlatnev+"'";

        Cursor cursor = db.rawQuery(querryString,null);
        /*
        Cursor checkCursor = db.rawQuery(queryCheck,null);
        Boolean success= checkCursor.getCount()>0 ? false:true;
        if (checkCursor.getCount() >0 ){
            return false;
        }else {
            return true;
        }

         */

        if (cursor.getCount()<=0){
            return true;
        }else{
            return false;
        }
    }

}
