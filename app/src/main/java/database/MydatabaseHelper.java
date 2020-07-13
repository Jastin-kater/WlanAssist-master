package database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/*
 *
 *类描述：
 *创建人：R
 *创建时间：${DATA}18:57
 *
 */public class MydatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_MACH_HAN = "CREATE TABLE IF NOT EXISTS mach ("
            +"id INTEGER PRIMARY KEY AUTOINCREMENT ,"
            +"weld_vol real ,"
            +"weld_cur real ,"
            +"envir_temp real ,"
            +"speed_songsi real ,"
            +"envir_humi real ,"
            +"speed_weld real ,"
            +"welding_layer_temp real ,"
            +"info_mach text ,"
            +"ID_proj text ,"
            +"num_unit text ,"
            +"procedure_tech text ,"
            +"num_mach text ,"
            +"tech_weld text ,"
            +"num_person text ,"
            +"num_weld_point text ,"
            +"place_weld text ,"
            +"name_welding_layer text ,"
            +"alert_working text ,"
            +"weld_type INTEGER ,"
            +"time INTEGER "
            +")";

    private static final String CREATE_ALERT_V = "CREATE TABLE IF NOT EXISTS voltage1("
            +"id INTEGER PRIMARY KEY AUTOINCREMENT,"
            +"time text ,"
            +"name text ,"
            +"weld_vol real "
            +")";
    private static final String CREATE_ALERT_I = "CREATE TABLE IF NOT EXISTS current("
            +"id INTEGER PRIMARY KEY AUTOINCREMENT,"
            +"time text ,"
            +"name text ,"
            +"weld_cur real "
            +")";
    private static final String CREATE_ALERT_songsi = "CREATE TABLE IF NOT EXISTS songsi("
            +"id INTEGER PRIMARY KEY AUTOINCREMENT,"
            +"time text ,"
            +"name text ,"
            +"speed_songsi real "
            +")";
    private static final String CREATE_ALERT_hanjie = "CREATE TABLE IF NOT EXISTS hanjie("
            +"id INTEGER PRIMARY KEY AUTOINCREMENT,"
            +"time text ,"
            +"name text ,"
            +"speed_hanjie real "
            +")";
     public MydatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MydatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

         db.execSQL(CREATE_MACH_HAN);
         db.execSQL(CREATE_ALERT_V);
         db.execSQL(CREATE_ALERT_I);
         db.execSQL(CREATE_ALERT_songsi);
         db.execSQL(CREATE_ALERT_hanjie);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }




}
