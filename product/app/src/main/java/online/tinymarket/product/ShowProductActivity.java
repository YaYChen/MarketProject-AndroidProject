package online.tinymarket.product;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import online.tinymarket.product.entity.Product;
import online.tinymarket.product.enums.HttpOperating;
import online.tinymarket.product.handler.ImgHandler;
import online.tinymarket.product.handler.ProductHandler;
import online.tinymarket.product.interfaces.IHandlerImg;
import online.tinymarket.product.interfaces.IHandlerProduct;
import online.tinymarket.product.service.ImgService;
import online.tinymarket.product.service.ProductService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ShowProductActivity extends AppCompatActivity implements IHandlerProduct, IHandlerImg {

    private String code = "";

    private ImageView iv_product_image;
    private TextView tv_product_name;
    private TextView tv_product_category;
    private TextView tv_product_specification;
    private TextView tv_product_price;
    private TextView tv_product_nothing;
    private Button btn_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.code = getIntent().getExtras().getString("code");
        initialView();
        getProduct();
    }

    private void initialView(){
        iv_product_image = findViewById(R.id.iv_product_image);
        tv_product_name = findViewById(R.id.tv_product_name);
        tv_product_category = findViewById(R.id.tv_product_category);
        tv_product_specification = findViewById(R.id.tv_product_specification);
        tv_product_price = findViewById(R.id.tv_product_price);
        tv_product_nothing = findViewById(R.id.tv_show_nothing);
        btn_return = findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowProductActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getProduct(){
        if(!this.code.equals("")){
            ProductHandler productHandler = new ProductHandler(this, HttpOperating.DownloadData);
            ProductService productService = new ProductService(productHandler.doHandler);
            productService.getProductByCode(this.code);
        }
    }

    private void getImg(String imgName){
        if(!imgName.equals("")){
            ImgHandler imgHandler = new ImgHandler(this,HttpOperating.DownloadData);
            ImgService imgService = new ImgService(imgHandler.doHandler);
            imgService.getProductImage(imgName);
        }
    }

    public void handlerProductWhenOK(String msg){

    }

    public void handlerProductWhenOK(Product product){
        if(product != null){
            this.getImg(product.getProductPicture());
            this.tv_product_name.setText(product.getName());
            this.tv_product_specification.setText(product.getSpecification());
            this.tv_product_price.setText(product.getPrice());
            this.tv_product_category.setText(product.getCategory().getName());
        }else{
            Toast.makeText(
                    this,
                    "The product is not exist!",
                    Toast.LENGTH_LONG).show();
            this.tv_product_nothing.setVisibility(View.VISIBLE);
        }
    }

    public void handlerProductWhenNG(String errorMsg){
        Toast.makeText(
                this,
                "Operate failed!Message: " + errorMsg,
                Toast.LENGTH_LONG).show();
    }

    public void handlerImgWhenOK(String msg){

    }

    public void handlerImgWhenOK(Bitmap bitmap){
        this.iv_product_image.setImageBitmap(bitmap);
        this.iv_product_image.setVisibility(View.VISIBLE);
    }

    public void handlerImgWhenNG(String errorMsg){
        Toast.makeText(
                this,
                "Operate failed!Message: " + errorMsg,
                Toast.LENGTH_LONG).show();
    }
}
