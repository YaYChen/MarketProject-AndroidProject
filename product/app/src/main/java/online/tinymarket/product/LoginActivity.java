package online.tinymarket.product;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import online.tinymarket.product.entity.User;
import online.tinymarket.product.service.UserService;
import online.tinymarket.product.session.SessionUserInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * @author: yaychen
 * @date: 2019/10/09 23:50
 * @declare :
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText loginNameET;
    private EditText passwordET;
    private Button loginBtn;
    private ProgressBar progressBar;

    private static final int LOGIN_OK = 1;
    private static final int LOGIN_NG = 0;

    private Toast toast;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOGIN_OK:
                    progressBar.setVisibility(View.GONE);
                    toast = Toast.makeText(
                            getBaseContext(),
                            "Login Success!Welcome " + SessionUserInfo.getInstance().userName,
                            Toast.LENGTH_LONG);
                    toast.show();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
                case LOGIN_NG:
                    progressBar.setVisibility(View.GONE);
                    toast = Toast.makeText(
                            getBaseContext(),
                            "Login Failed!Message: " + msg.obj.toString(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
    }

    private void initView() {
        loginNameET = findViewById(R.id.loginNameET);
        passwordET = findViewById(R.id.loginPasswordET);
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                this.login();
                break;
            default:
        }
    }

    private void login(){
        try{
            User user = new User();
            user.setLoginName(loginNameET.getText().toString());
            user.setPassword(passwordET.getText().toString());
            UserService service = new UserService(user,this.handler);
            Thread thread = new Thread(service);
            thread.run();
            this.progressBar.setVisibility(View.VISIBLE);
        }catch (Exception e){
            toast = Toast.makeText(getBaseContext(),"Login failed!" + e.getMessage(),Toast.LENGTH_LONG);
            toast.show();
        }
    }

}
