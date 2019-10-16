package com.yzq.zxing;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import entity.Product;
import service.ImgService;
import service.ProductService;
import session.SessionUserInfo;

/**
 * A placeholder fragment for show product info.
 */
public class ShowProductFragment extends Fragment {

    private View view;

    private ImageView iv_product_image;
    private TextView tv_product_name;
    private TextView tv_product_category;
    private TextView tv_product_specification;
    private TextView tv_product_price;
    private TextView tv_product_nothing;
    private Button btn_return;

    private ProductService productService;
    private ImgService imgService;

    private static final int REQUEST_OK = 1;
    private static final int REQUEST_NG = 0;

    private Toast toast;

    private Handler handlerProduct = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REQUEST_OK:
                    showProduct((Product)msg.obj);
                    break;
                case REQUEST_NG:
                    toast = Toast.makeText(
                            getActivity(),
                            "Load product failed!Message: " + msg.obj.toString(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    break;
                default:
                    break;
            }
        }
    };

    private Handler handlerImg = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REQUEST_OK:
                    showImg((Bitmap)msg.obj);
                    break;
                case REQUEST_NG:
                    toast = Toast.makeText(
                            getActivity(),
                            "Load img failed!Message: " + msg.obj.toString(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    break;
                default:
                    break;
            }
        }
    };


    public ShowProductFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_show_product, container, false);
        initialView();
        loadProduct();
        return view;
    }

    private void initialView(){
        iv_product_image = view.findViewById(R.id.iv_product_image);
        tv_product_name = view.findViewById(R.id.tv_product_name);
        tv_product_category = view.findViewById(R.id.tv_product_category);
        tv_product_specification = view.findViewById(R.id.tv_product_specification);
        tv_product_price = view.findViewById(R.id.tv_product_price);
        tv_product_nothing = view.findViewById(R.id.tv_show_nothing);
        btn_return = view.findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadProduct(){
        try{
            this.productService = new ProductService(this.handlerProduct);
            this.productService.getProductByCode(SessionUserInfo.getInstance().code);
        }catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showProduct(Product product){
        if(product == null){
            tv_product_nothing.setVisibility(View.VISIBLE);
        }else{
            this.loadImg(product);
            tv_product_name.setText(this.getString(R.string.product_name) + product.getName());
            tv_product_category.setText(this.getString(R.string.product_category) + product.getCategory().getName());
            tv_product_specification.setText(this.getString(R.string.product_specification) + product.getSpecification());
            tv_product_price.setText(this.getString(R.string.product_price) + product.getPrice());
        }
    }

    private void loadImg(Product product){
        try{
            this.imgService = new ImgService(this.handlerImg);
            this.imgService.getProductImage(product.getProductPicture());
        }catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showImg(Bitmap bitmap){
        if(bitmap != null){
            iv_product_image.setImageBitmap(bitmap);
            iv_product_image.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(getActivity(), "The picture is null...", Toast.LENGTH_SHORT).show();
        }
    }

}
