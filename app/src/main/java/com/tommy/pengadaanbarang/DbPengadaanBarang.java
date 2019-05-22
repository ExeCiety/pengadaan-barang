package com.tommy.pengadaanbarang;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbPengadaanBarang extends SQLiteOpenHelper {

    public DbPengadaanBarang(Context context) {
        super(context, "db_pengadaan_barang", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE barang (_id INTEGER PRIMARY KEY AUTOINCREMENT, nama_barang TEXT, harga INTEGER, jml_stok INTEGER);");
        db.execSQL("CREATE TABLE pengadaan (_id INTEGER PRIMARY KEY AUTOINCREMENT, id_barang INTEGER, tgl_add_barang DATE, stok INTEGER, FOREIGN KEY (id_barang) REFERENCES barang(_id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS barang");
        db.execSQL("DROP TABLE IF EXISTS pengadaan");

        onCreate(db);
    }
}
