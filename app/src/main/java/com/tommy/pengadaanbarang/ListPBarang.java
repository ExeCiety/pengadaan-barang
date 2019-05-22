package com.tommy.pengadaanbarang;

import android.accessibilityservice.FingerprintGestureController;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class ListPBarang extends AppCompatActivity implements OnClickListener {

    DbPengadaanBarang myDb = new DbPengadaanBarang(this);
    SimpleCursorAdapter adapter;

    EditText et_stpbarang;
    Spinner s_nbarang;
    DatePicker dp_tgl_add;
    ListView lv_data_pbarang;
    Button btn_update_pbarang, btn_delete_pbarang;

    String selected_id = "";
    String today = "";
    String barang[];
    String pengadaan[];

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pbarang);

        et_stpbarang = findViewById(R.id.et_stpbarang);
        s_nbarang = findViewById(R.id.sp_nbarang2);
        dp_tgl_add = findViewById(R.id.dp_tgl_add);
        lv_data_pbarang = findViewById(R.id.lv_data_pbarang);
        dp_tgl_add = findViewById(R.id.dp_tgl_add);
        btn_update_pbarang = findViewById(R.id.btn_update_pbarang);
        btn_delete_pbarang = findViewById(R.id.btn_delete_pbarang);

        fetchData();

        Calendar calendar = Calendar.getInstance();
        dp_tgl_add.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                today = "";
                today += dp_tgl_add.getDayOfMonth() + "-" + dp_tgl_add.getMonth() + "-"+ dp_tgl_add.getYear();
            }
        });

        lv_data_pbarang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor row = (Cursor) parent.getItemAtPosition(position);
                selected_id = row.getString(0);

                pengadaan = new String[4];

                pengadaan[0] = row.getString(0);
                pengadaan[1] = row.getString(1);
                pengadaan[2] = row.getString(2);
                pengadaan[3] = row.getString(3);

                String id_barang = row.getString(1);
                String tgl_add_barang = row.getString(2);
                String stok_pengadaan = row.getString(3);

                SQLiteDatabase db = myDb.getWritableDatabase();
                Cursor res = db.rawQuery("SELECT * FROM barang WHERE _id=" + id_barang, null);
                res.moveToNext();
                String nama_barang[] = new String[res.getCount()];
                barang = new String[3];

                nama_barang[0] = res.getString(1);
                barang[0] = res.getString(0);
                barang[1] = res.getString(1);
                barang[2] = res.getString(3);

                ArrayAdapter<String> test = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, nama_barang);
                String tgl[] = tgl_add_barang.split("-");

                s_nbarang.setAdapter(test);
                et_stpbarang.setText(stok_pengadaan);
                dp_tgl_add.updateDate(Integer.parseInt(tgl[2]), Integer.parseInt(tgl[1]), Integer.parseInt(tgl[0]));
            }
        });

        btn_update_pbarang.setOnClickListener(this);
        btn_delete_pbarang.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == btn_update_pbarang) {

            int stok_pengadaan = Integer.parseInt(et_stpbarang.getText().toString());

            SQLiteDatabase db = myDb.getWritableDatabase();
            ContentValues cv = new ContentValues();

            Cursor cek = db.rawQuery("SELECT * FROM barang WHERE _id=" + Integer.parseInt(barang[0]), null);
            cek.moveToNext();

            int stok_awal = Integer.parseInt(cek.getString(3));
            int stok_akhir = 0;

            cv.put("tgl_add_barang", today);
            cv.put("stok", stok_pengadaan);

            long update = db.update("pengadaan", cv, "_id=?", new String[] {String.valueOf(selected_id)});

            if (update == -1) {
                Toast.makeText(getApplicationContext(), "Data Gagal Diupdate!", Toast.LENGTH_SHORT).show();
            } else {
                ContentValues cv2 = new ContentValues();
                int stok_barang = 0;

                if (Integer.parseInt(pengadaan[3]) >= stok_awal) {
                    stok_akhir = stok_pengadaan - Integer.parseInt(pengadaan[3]);
                    stok_barang = stok_awal + stok_akhir;
                } else {
                    stok_akhir = Integer.parseInt(pengadaan[3]) - stok_pengadaan;
                    stok_barang = stok_awal - stok_akhir;
                }

                cv2.put("jml_stok", stok_barang);

                long update2 = db.update("barang", cv2, "_id=?", new String[] {barang[0]});

                if (update2 == -1) {
                    Toast.makeText(getApplicationContext(), "Data Gagal Diupdate!", Toast.LENGTH_SHORT).show();
                } else {
                    fetchData();
                    selected_id = "";
                    Toast.makeText(getApplicationContext(), "Data Berhasil Diupdate!", Toast.LENGTH_LONG).show();
                }
            }
        } else if(v == btn_delete_pbarang) {
            if (!selected_id.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Apakah Anda Yakin?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = myDb.getWritableDatabase();

                        int stok_akhir = Integer.parseInt(barang[2]) - Integer.parseInt(pengadaan[3]);

                        ContentValues cv = new ContentValues();
                        cv.put("jml_stok", stok_akhir);

                        long update = db.update("barang", cv, "_id=?", new String[] {barang[0]});

                        if (update == -1) {
                            Toast.makeText(getApplicationContext(), "Data Gagal Dihapus!", Toast.LENGTH_SHORT).show();
                        } else {
                            long delete = db.delete("pengadaan", "_id=?", new String[]{selected_id});
                            db.close();

                            if (delete == -1) {
                                Toast.makeText(getApplicationContext(), "Data Gagal Dihapus!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Data Berhasil Dihapus!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        selected_id = "";
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

        Cursor res = db.rawQuery("SELECT * FROM pengadaan", null);
        if (res.getCount() != 0) {
            adapter = new SimpleCursorAdapter(this, R.layout.table_data, res,
                    new String[] {"id_barang", "tgl_add_barang", "stok"},
                    new int[] {R.id.tv_data_nbarang, R.id.tv_data_hbarang, R.id.tv_data_sbarang}, 1);

            lv_data_pbarang.setAdapter(adapter);
        } else {
            Toast.makeText(getApplicationContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
        }
    }
}
