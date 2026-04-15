package br.luciano.quempegou;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AmigosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);

        setTitle(R.string.amigos);
    }
}
