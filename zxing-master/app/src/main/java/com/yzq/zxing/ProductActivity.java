package com.yzq.zxing;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class ProductActivity extends AppCompatActivity {

    private String code;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        selectFragment(getIntent());
    }

    private void selectFragment(Intent intent){
        code = intent.getStringExtra("code");
        action = intent.getStringExtra("action");
        switch (action){
            case "search":
                ShowProductFragment showProductFragment = ShowProductFragment.newInstance(code);
                replaceFragment(showProductFragment);
                break;
            case "edit":
                EditProductFragment editProductFragment = EditProductFragment.newInstance(code);
                replaceFragment(editProductFragment);
                break;
                default:
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content,fragment);
        fragmentTransaction.commit();
    }

}
