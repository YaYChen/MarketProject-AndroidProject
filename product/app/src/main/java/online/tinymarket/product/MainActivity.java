package online.tinymarket.product;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import online.tinymarket.product.enums.ProductOperating;
import online.tinymarket.product.session.SessionUserInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

/**
 * @author: yaychen
 * @date: 2019/04/09 23:50
 * @declare :
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button searchBtn;
    private Button editBtn;

    private ProductOperating operating;

    private int REQUEST_CODE_SCAN = 1001;

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
        Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, this.REQUEST_CODE_SCAN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String barcode = bundle.getString("result");
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
    }

}

