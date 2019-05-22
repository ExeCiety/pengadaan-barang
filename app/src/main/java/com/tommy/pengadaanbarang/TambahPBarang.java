package com.tommy.pengadaanbarang;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class TambahPBarang extends AppCompatActivity implements OnClickListener {

    private Spinner s_nbarang;
    private DatePicker dp_tbarang;
    private EditText et_stbarang;
    Button btn_tambah_pbarang;

    DbPengadaanBarang myDb = new DbPengadaanBarang(this);

    String today = "";
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pbarang);

        s_nbarang = findViewById(R.id.s_nbarang);
        dp_tbarang = findViewById(R.id.dp_tbarang);
        et_stbarang = findViewById(R.id.et_stbarang);
        btn_tambah_pbarang = findViewById(R.id.btn_tambah_pbarang);

        btn_tambah_pbarang.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        dp_tbarang.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                today = "";
                today += dp_tbarang.getDayOfMonth() + "-" + dp_tbarang.getMonth() + "-"+ dp_tbarang.getYear();
            }
        });

        index = s_nbarang.getSelectedItemPosition();

        s_nbarang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                index = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fetchDataBarang();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_tambah_pbarang) {
            if (et_stbarang.getText().toString().equals("") || today == "" || s_nbarang.getSelectedItemPosition() == -1) {
                Toast.makeText(getApplicationContext(), "Inputan Harus Di isi Semua!", Toast.LENGTH_SHORT).show();
            } else {
                //index -= 1;
                int id_barang=Integer.parseInt(id_barangArr[index]);
                int stok = Integer.parseInt(et_stbarang.getText().toString());

                ContentValues cv = new ContentValues();
                cv.put("id_barang", id_barang);
                cv.put("tgl_add_barang", today);
                cv.put("stok", stok);

                SQLiteDatabase db = myDb.getWritableDatabase();
                long insert = db.insert("pengadaan", null, cv);

                if (insert == -1) {
                    Toast.makeText(getApplicationContext(), "Data Gagal Ditambahkan!", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor res = db.rawQuery("SELECT * FROM barang WHERE _id=" + id_barang, null);
                    res.moveToFirst();

                    String stok_barang = res.getString(3);
                    int stok_akhir = Integer.parseInt(stok_barang) + stok;

                    ContentValues cv2 = new ContentValues();
                    cv2.put("jml_stok", stok_akhir);

                    long update = db.update("barang", cv2, "_id=?", new String[] {String.valueOf(id_barang)});
                    db.close();

                    if (update == -1) {
                        Toast.makeText(getApplicationContext(), "Data Gagal Ditambahkan!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Data Berhasil Ditambahkan!", Toast.LENGTH_SHORT).show();
                    }
                    today = "";
                }
            }
        }
    }

    String id_barangArr[];
    String nama_barangArr[];
    public void fetchDataBarang() {
        SQLiteDatabase db = myDb.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM barang", null);
        int row = res.getCount();
        id_barangArr = new String[row];
        nama_barangArr = new String[row];
        int x = 0;

        while (res.moveToNext()){
            id_barangArr[x] = res.getString(0);
            nama_barangArr[x] = res.getString(1);
            x++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nama_barangArr);
        s_nbarang.setAdapter(adapter);
    }
}
