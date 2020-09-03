package online.tinymarket.product;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import online.tinymarket.product.enums.ProductOperating;
import online.tinymarket.product.session.SessionUserInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * @author: yaychen
 * @date: 2019/04/09 23:50
 * @declare :
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button searchBtn;
    private Button editBtn;

    private ProductOperating operating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        this.CheckoutAuth();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        this.CheckoutAuth();
    }


    private void initView() {
        /*扫描按钮*/
        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);
        editBtn = findViewById(R.id.editBtn);
        editBtn.setOnClickListener(this);
    }

    private void CheckoutAuth(){
        if(SessionUserInfo.getInstance().userId == 0){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchBtn:
                operating = ProductOperating.Search;
                startScan();
                break;
            case R.id.editBtn:
                operating = ProductOperating.Edit;
                startScan();
                break;
            default:
        }
    }

    private void startScan(){
        // 创建IntentIntegrator对象
        IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
        // 开始扫描
        intentIntegrator.initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String barcode = result.getContents() == null ? null:result.getContents();
            if (barcode == null) {
                Toast.makeText(this, "取消扫描", Toast.LENGTH_LONG).show();
            } else {
                if(operating == ProductOperating.Search){
                    Intent showProductIntent = new Intent(MainActivity.this, ShowProductActivity.class);
                    showProductIntent.putExtra("code",barcode);
                    startActivity(showProductIntent);
                }else{
                    Intent editProductIntent = new Intent(MainActivity.this,EditProductActivity.class);
                    editProductIntent.putExtra("code",barcode);
                    startActivity(editProductIntent);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}

