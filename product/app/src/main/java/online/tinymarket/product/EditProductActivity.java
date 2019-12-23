package online.tinymarket.product;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import online.tinymarket.product.adapter.CategorySpinnerAdapter;
import online.tinymarket.product.entity.Category;
import online.tinymarket.product.entity.Product;
import online.tinymarket.product.enums.HttpOperating;
import online.tinymarket.product.handler.CategoryHandler;
import online.tinymarket.product.handler.ImgHandler;
import online.tinymarket.product.handler.ProductHandler;
import online.tinymarket.product.interfaces.IHandlerCategory;
import online.tinymarket.product.interfaces.IHandlerImg;
import online.tinymarket.product.interfaces.IHandlerProduct;
import online.tinymarket.product.service.CategoryService;
import online.tinymarket.product.service.ImgService;
import online.tinymarket.product.service.ProductService;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

public class EditProductActivity extends AppCompatActivity implements IHandlerProduct, IHandlerCategory, IHandlerImg, View.OnClickListener {

    public static final int RESULT_LOAD_IMAGE = 1002;
    public static final int RESULT_CAMERA_IMAGE = 1003;

    private boolean isNewProduct = false;

    private String code = "";

    private ImageView iv_product_image;
    private TextView tv_product_code;
    private EditText et_product_name;
    private EditText et_product_specification;
    private EditText et_product_purchasePrice;
    private EditText et_product_price;
    private Spinner sp_product_category;
    private Button btn_cancel;
    private Button btn_confirm;

    private Product product;
    private Category selectCategory;
    private String imgFileName;

    private List<Category> categories = new ArrayList<>();

    private Toast toast;

    private Uri imageUri;

    private Bitmap bitmap;

    public EditProductActivity(String code){
        this.code = code;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.code = getIntent().getExtras().getString("code");
        initialView();
        getCategories();
        getProduct();
    }

    private void initialView(){
        this.iv_product_image = findViewById(R.id.iv_product_image);
        this.tv_product_code = findViewById(R.id.tv_product_code);
        this.et_product_name = findViewById(R.id.et_product_name);
        this.et_product_specification = findViewById(R.id.et_product_specification);
        this.et_product_purchasePrice = findViewById(R.id.et_product_purchasePrice);
        this.et_product_price = findViewById(R.id.et_product_price);
        this.sp_product_category = findViewById(R.id.sp_product_category);
        this.btn_cancel = findViewById(R.id.btn_cancel);
        this.btn_cancel.setOnClickListener(this);
        this.btn_confirm = findViewById(R.id.btn_confirm);
        this.btn_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try{
            switch (v.getId()) {
                case R.id.btn_cancel:
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_confirm:
                    if(this.getProductValueAfterEditing()){
                        ProductHandler productHandler = new ProductHandler(this, HttpOperating.UploadData);
                        ProductService productService = new ProductService(productHandler.doHandler);
                        if(this.isNewProduct){
                            productService.insertProduct(this.product);
                        }else{
                            productService.updateProduct(this.product);
                        }
                    }
                    this.btn_confirm.setEnabled(false);
                    break;
                case R.id.iv_product_image:
                    this.showPopueWindow();
                    break;
                default:
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getCategories(){
        CategoryHandler categoryHandler = new CategoryHandler(this, HttpOperating.DownloadData);
        CategoryService categoryService = new CategoryService(categoryHandler.doHandler);
        categoryService.getAllCategories();
    }

    private void getProduct(){
        if(!this.code.equals("")){
            ProductHandler productHandler = new ProductHandler(this, HttpOperating.DownloadData);
            ProductService productService = new ProductService(productHandler.doHandler);
            productService.getProductByCode(this.code);
        }
    }

    private void getImg(String imgFileName){
        this.imgFileName = imgFileName;
        ImgHandler imgHandler = new ImgHandler(this, HttpOperating.DownloadData);
        ImgService imgService = new ImgService(imgHandler.doHandler);
        imgService.getProductImage(imgFileName);
    }

    public void handlerProductWhenOK(String msg){
        toast = Toast.makeText(
                this,
                "Operate success!Message: " + msg,
                Toast.LENGTH_LONG);
        toast.show();
        this.getProduct();
        this.btn_confirm.setEnabled(true);
    }

    public void handlerProductWhenOK(Product product){
        this.tv_product_code.setText(this.code);
        if(product != null){
            this.isNewProduct = false;
            this.product = product;
            this.getImg(this.product.getPrice());
            this.et_product_name.setText(this.product.getName());
            this.et_product_specification.setText(this.product.getSpecification());
            this.et_product_purchasePrice.setText(this.product.getPurchasePrice());
            this.et_product_price.setText(this.product.getPrice());
            this.sp_product_category.setSelection(this.categories.indexOf(this.product.getCategory()));
        }else{
            this.isNewProduct = true;
            this.product = new Product();
            this.product.setCode(this.code);
        }
    }

    public void handlerProductWhenNG(String errorMsg){
        toast = Toast.makeText(
                this,
                "Operate failed!Message: " + errorMsg,
                Toast.LENGTH_LONG);
        toast.show();
    }

    public void handlerCategoryWhenOK(String msg){
        toast = Toast.makeText(
                this,
                "Operate success!Message: " + msg,
                Toast.LENGTH_LONG);
        toast.show();
    }

    public void handlerCategoryWhenOK(List<Category> categories){
        try{
            CategorySpinnerAdapter mySpinnerAdapter = new CategorySpinnerAdapter(this, categories);
            sp_product_category.setAdapter(mySpinnerAdapter);
            sp_product_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    selectCategory = (Category)parent.getItemAtPosition(position);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
        }catch (Exception e){
            toast = Toast.makeText(
                    this,
                    e.getMessage(),
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void handlerCategoryWhenNG(String errorMsg){
        toast = Toast.makeText(
                this,
                "Operate failed!Message: " + errorMsg,
                Toast.LENGTH_LONG);
        toast.show();
    }

    public void handlerImgWhenOK(String msg){
        this.imgFileName = msg;
        this.iv_product_image.setImageBitmap(this.bitmap);
    }

    public void handlerImgWhenOK(Bitmap bitmap){
        this.iv_product_image.setImageBitmap(bitmap);
        this.bitmap = bitmap;
    }

    public void handlerImgWhenNG(String errorMsg){
        toast = Toast.makeText(
                this,
                "Operate failed!Message: " + errorMsg,
                Toast.LENGTH_LONG);
        toast.show();
    }

    private boolean getProductValueAfterEditing(){
        try{
            if(this.product == null){
                this.product = new Product();
                this.product.setCode(this.code);
            }
            this.product.setName(et_product_name.getText().toString());
            this.product.setSpecification(et_product_specification.getText().toString());
            this.product.setCategory(this.selectCategory);
            this.product.setPurchasePrice(et_product_purchasePrice.getText().toString());
            this.product.setPrice(et_product_price.getText().toString());
            this.product.setProductPicture(this.imgFileName);
            return true;
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void showPopueWindow(){
        View popView = View.inflate(this,R.layout.popup_camera_need_view,null);
        Button bt_album = popView.findViewById(R.id.btn_pop_album);
        Button bt_camera = popView.findViewById(R.id.btn_pop_camera);
        Button bt_cancel = popView.findViewById(R.id.btn_pop_cancel);
        //获取屏幕宽高
        int weight = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels*1/3;

        final PopupWindow popupWindow = new PopupWindow(popView,weight,height);
        //popupWindow.setAnimationStyle(R.style.anim_popup_dir);
        popupWindow.setFocusable(true);
        //点击外部popueWindow消失
        popupWindow.setOutsideTouchable(true);

        bt_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();

            }
        });
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage = new File(getExternalCacheDir(),
                        "output_image.jpg");
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(Build.VERSION.SDK_INT >= 24){
                    imageUri = FileProvider.getUriForFile(EditProductActivity.this,
                            "com.yzq.zxing.fileprovider",outputImage);
                }else{
                    imageUri = Uri.fromFile(outputImage);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,RESULT_CAMERA_IMAGE);
                popupWindow.dismiss();

            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });
        //popupWindow消失屏幕变为不透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        //popupWindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM,0,50);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK ) {
            if (requestCode == RESULT_LOAD_IMAGE && null != data) {

            }else if (requestCode == RESULT_CAMERA_IMAGE) {
                try{
                    bitmap = BitmapFactory.decodeStream(
                            getContentResolver().openInputStream(imageUri));
                    File imageFile = saveMyBitmap(bitmap);
                    ImgHandler handlerImg = new ImgHandler(this, HttpOperating.UploadData);
                    ImgService imgService = new ImgService(handlerImg.doHandler);
                    imgService.uploadImage(imageFile);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    public File saveMyBitmap(Bitmap mBitmap){
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File file = null;
        try {
            file = File.createTempFile(
                    this.code,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            FileOutputStream out=new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  file;
    }
}
