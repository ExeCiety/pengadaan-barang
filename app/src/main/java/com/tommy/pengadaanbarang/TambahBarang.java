package com.tommy.pengadaanbarang;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TambahBarang extends AppCompatActivity implements OnClickListener {

    private EditText et_nbarang, et_hbarang, et_sbarang;
    private Button btn_tbarang;

    DbPengadaanBarang myDb = new DbPengadaanBarang(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang);

        et_nbarang = findViewById(R.id.et_nbarang);
        et_hbarang = findViewById(R.id.et_hbarang);
        et_sbarang = findViewById(R.id.et_sbarang);
        btn_tbarang = findViewById(R.id.btn_tambah_barang);

        btn_tbarang.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_tambah_barang); {
            if (et_nbarang.getText().toString().equals("") || et_hbarang.getText().toString().equals("") || et_sbarang.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Inputan Harus Di isi Semua!", Toast.LENGTH_SHORT).show();
            } else {
                String nama_barang = et_nbarang.getText().toString();
                int harga_barang = Integer.parseInt(et_hbarang.getText().toString());
                int stok_barang = Integer.parseInt(et_sbarang.getText().toString());

                ContentValues cv = new ContentValues();
                cv.put("nama_barang", nama_barang);
                cv.put("harga", harga_barang);
                cv.put("jml_stok", stok_barang);

                SQLiteDatabase db = myDb.getWritableDatabase();
                long insert = db.insert("barang", null, cv);
                db.close();

                if (insert == -1) {
                    Toast.makeText(getApplicationContext(), "Data Gagal Ditambahkan!", Toast.LENGTH_SHORT).show();
                } else {
                    clearFields();
                    Toast.makeText(getApplicationContext(), "Data Berhasil Ditambahkan!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void clearFields() {
        et_nbarang.setText("");
        et_hbarang.setText("");
        et_sbarang.setText("");
    }
}
