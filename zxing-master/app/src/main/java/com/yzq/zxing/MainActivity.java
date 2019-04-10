package com.yzq.zxing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;
import com.yzq.zxinglibrary.encode.CodeCreator;

import java.util.List;


/**
 * @author: yaychen
 * @date: 2019/04/09 23:50
 * @declare :
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button searchBtn;
    private Button editBtn;

    private String action;

    private int REQUEST_CODE_SCAN = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
    }


    private void initView() {
        /*扫描按钮*/
        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);

        editBtn = findViewById(R.id.editBtn);
        editBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchBtn:
                action = "search";
                startScan();
                break;
            case R.id.editBtn:
                action = "edit";
                startScan();
                break;
            default:
        }
    }

    private void startScan(){
        AndPermission.with(this)
                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                        /*ZxingConfig是配置类
                         *可以设置是否显示底部布局，闪光灯，相册，
                         * 是否播放提示音  震动
                         * 设置扫描框颜色等
                         * 也可以不传这个参数
                         * */
                        ZxingConfig config = new ZxingConfig();
                        config.setPlayBeep(true);//是否播放扫描完成声音 默认为true
                        config.setShake(true);//是否震动  默认为true
                        config.setDecodeBarCode(false);//是否扫描条形码 默认为true
                        config.setReactColor(R.color.red);//设置扫描框四个角的颜色 默认为淡蓝色
                        config.setFrameLineColor(R.color.red);//设置扫描框边框颜色 默认无色
                        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                        startActivityForResult(intent, REQUEST_CODE_SCAN);
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Uri packageURI = Uri.parse("package:" + getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);

                        Toast.makeText(MainActivity.this, "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                    }
                }).start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String code = data.getStringExtra(Constant.CODED_CONTENT);
                Intent intent = new Intent(MainActivity.this,ProductActivity.class);
                intent.putExtra("code",code);
                intent.putExtra("action",action);
                startActivity(intent);
            }
        }
    }

}
