package com.tommy.pengadaanbarang;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListBarang extends AppCompatActivity implements OnClickListener {

    private ListView lv_data_barang;
    private EditText et_nbarang, et_hbarang, et_sbarang;
    private Button btn_ubarang, btn_dbarang;

    DbPengadaanBarang myDb = new DbPengadaanBarang(this);
    SimpleCursorAdapter adapter;

    String selected_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_barang);

        lv_data_barang = findViewById(R.id.lv_data_barang);
        et_nbarang = findViewById(R.id.et_nbarang);
        et_hbarang = findViewById(R.id.et_hbarang);
        et_sbarang = findViewById(R.id.et_sbarang);
        btn_ubarang = findViewById(R.id.btn_update_barang);
        btn_dbarang = findViewById(R.id.btn_delete_barang);

        btn_ubarang.setOnClickListener(this);
        btn_dbarang.setOnClickListener(this);

        fetchData();

        lv_data_barang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Cursor row = (Cursor) adapter.getItemAtPosition(position);
                selected_id = row.getString(0);

                String nama_barang = row.getString(1);
                String harga_barang = row.getString(2);
                String stok_barang = row.getString(3);

                et_nbarang.setText(nama_barang);
                et_hbarang.setText(harga_barang);
                et_sbarang.setText(stok_barang);

            }
        });

    }

    public void onClick(View v) {
        if (v == btn_ubarang) {
            if (!selected_id.equals("")) {
                String nama_barang = et_nbarang.getText().toString();
                int harga_barang = Integer.parseInt(et_hbarang.getText().toString());
                int stok_barang = Integer.parseInt(et_sbarang.getText().toString());

                ContentValues cv = new ContentValues();
                cv.put("nama_barang", nama_barang);
                cv.put("harga", harga_barang);
                cv.put("jml_stok", stok_barang);

                SQLiteDatabase db = myDb.getWritableDatabase();
                long update = db.update("barang", cv, "_id=?", new String[] {selected_id});
                db.close();

                if (update == -1) {
                    Toast.makeText(getApplicationContext(), "Data Gagal Diupdate!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Data Berhasil Diupdate!", Toast.LENGTH_SHORT).show();
                }

                selected_id = "";
                clearFields();
                fetchData();
            } else {
                Toast.makeText(getApplicationContext(), "Tolong Pilih Data Yang Mau Diubah!", Toast.LENGTH_SHORT).show();
            }
        } else if (v == btn_dbarang) {
            if (!selected_id.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Apakah Anda Yakin?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = myDb.getWritableDatabase();
                        db.delete("barang", "_id=?", new String[]{selected_id});
                        db.close();

                        selected_id = "";
                        clearFields();
                        fetchData();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            } else {
                Toast.makeText(getApplicationContext(), "Tolong Pilih Data Yang Mau Didelete!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void fetchData() {
        SQLiteDatabase db = myDb.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM barang", null);
        res.moveToNext();
        if (res.getCount() != 0) {
            adapter = new SimpleCursorAdapter(this, R.layout.table_data, res,
                    new String[] {"nama_barang", "harga", "jml_stok"},
                    new int[] {R.id.tv_data_nbarang, R.id.tv_data_hbarang, R.id.tv_data_sbarang}, 1);

            lv_data_barang.setAdapter(adapter);
        } else {
            Toast.makeText(getApplicationContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearFields() {
        et_nbarang.setText("");
        et_hbarang.setText("");
        et_sbarang.setText("");
    }
}
