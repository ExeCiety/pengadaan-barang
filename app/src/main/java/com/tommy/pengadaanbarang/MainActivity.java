package com.tommy.pengadaanbarang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private Button btn_tbarang, btn_tpbarang, btn_lbarang, btn_lpbarang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_tbarang = findViewById(R.id.btn_tbarang);
        btn_tpbarang = findViewById(R.id.btn_tpbarang);
        btn_lbarang = findViewById(R.id.btn_lbarang);
        btn_lpbarang = findViewById(R.id.btn_lpbarang);

        btn_tbarang.setOnClickListener(this);
        btn_tpbarang.setOnClickListener(this);
        btn_lbarang.setOnClickListener(this);
        btn_lpbarang.setOnClickListener(this);
    }

    public void onClick(View v) {

        if (v.getId() == R.id.btn_tbarang) {
            Intent i = new Intent(MainActivity.this, TambahBarang.class);
            startActivity(i);
        } else if (v.getId() == R.id.btn_tpbarang) {
            Intent i = new Intent(MainActivity.this, TambahPBarang.class);
            startActivity(i);
        } else if (v.getId() == R.id.btn_lbarang) {
            Intent i = new Intent(MainActivity.this, ListBarang.class);
            startActivity(i);
        } else if (v.getId() == R.id.btn_lpbarang) {
            Intent i = new Intent(MainActivity.this, ListPBarang.class);
            startActivity(i);
        }
    }
}
