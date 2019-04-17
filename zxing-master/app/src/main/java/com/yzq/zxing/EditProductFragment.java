package com.yzq.zxing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapter.MySpinnerAdapter;
import entity.Category;
import entity.Product;
import entity.Result;
import service.CategoryService;
import service.ImgService;
import service.ProductService;

import static com.yzq.zxing.ProductActivity.RESULT_CAMERA_IMAGE;
import static com.yzq.zxing.ProductActivity.RESULT_LOAD_IMAGE;


/**
 * A placeholder fragment for edit product info.
 */
public class EditProductFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_CODE = "code";

    private String code;

    private View view;

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

    private ProductService productService = new ProductService();
    private CategoryService categoryService = new CategoryService();
    private ImgService imgService = new ImgService();

    public EditProductFragment() {
        // Required empty public constructor
    }

    public static EditProductFragment newInstance(String code) {
        EditProductFragment fragment = new EditProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CODE, code);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            code = getArguments().getString(ARG_CODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_show_product, container, false);
        initialView();
        refresh();
        return view;
    }

    @Override
    public void onClick(View v) {
        try{
            switch (v.getId()) {
                case R.id.btn_cancel:
                    this.refresh();
                    break;
                case R.id.btn_confirm:
                    if(this.getProductValueAfterEditing()){
                        Result result = productService.insertProduct(this.product);
                        Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.iv_product_image:
                    showPopueWindow();
                    break;
                default:
            }
        }catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initialView(){
        iv_product_image = view.findViewById(R.id.iv_product_image);
        iv_product_image.setOnClickListener(this);
        tv_product_code = view.findViewById(R.id.tv_product_code);
        et_product_name = view.findViewById(R.id.et_product_name);
        et_product_specification = view.findViewById(R.id.et_product_specification);
        et_product_purchasePrice = view.findViewById(R.id.et_product_purchasePrice);
        et_product_price = view.findViewById(R.id.et_product_price);
        sp_product_category = view.findViewById(R.id.sp_product_category);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_confirm = view.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        initialCategorySpinner();
    }

    private void initialCategorySpinner(){
        try{
            categories = categoryService.getAllCategories();
            MySpinnerAdapter mySpinnerAdapter = new MySpinnerAdapter(this.getActivity(), categories);
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
            Toast.makeText(this.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void refresh(){
        try{
            product = productService.getProductByCode(code);
            if(product != null){
                iv_product_image.setImageBitmap(imgService.getProductImage(product.getProductPicture()));
                tv_product_code.setText(code);
                et_product_name.setText(product.getName());
                et_product_specification.setText(product.getSpecification());
                et_product_purchasePrice.setText(product.getPurchasePrice());
                et_product_price.setText(product.getPrice());
                sp_product_category.setSelection(categories.indexOf(product.getCategory()));
            }
        }catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean getProductValueAfterEditing(){
        try{
            this.product.setName(et_product_name.getText().toString());
            this.product.setSpecification(et_product_specification.getText().toString());
            this.product.setProductPicture(this.imgFileName);
            this.product.setCategory(this.selectCategory);
            this.product.setPurchasePrice(et_product_purchasePrice.getText().toString());
            this.product.setPrice(et_product_price.getText().toString());
            return true;
        }catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void showPopueWindow(){
        View popView = View.inflate(getActivity(),R.layout.popup_camera_need_view,null);
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
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                popupWindow.dismiss();

            }
        });
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeCamera(RESULT_CAMERA_IMAGE);
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
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1.0f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        //popupWindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM,0,50);

    }

    private void takeCamera(int num) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            photoFile = createImageFile();
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
            }
        }

        startActivityForResult(takePictureIntent, num);//跳转界面传回拍照所得数据
    }

    private File createImageFile() {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File image = null;
        try {
            image = File.createTempFile(
                    generateFileName(),  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        //mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String generateFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        return imageFileName;
    }
}
