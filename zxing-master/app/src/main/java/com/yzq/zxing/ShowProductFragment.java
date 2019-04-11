package com.yzq.zxing;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import entity.Product;
import service.ProductService;

/**
 * A placeholder fragment for show product info.
 */
public class ShowProductFragment extends Fragment {

    private static final String ARG_CODE = "code";

    private String code;

    private View view;

    private ImageView iv_product_image;
    private TextView tv_product_name;
    private TextView tv_product_category;
    private TextView tv_product_specification;
    private TextView tv_product_price;

    private Product product;
    private ProductService productService = new ProductService();

    public ShowProductFragment() {
    }

    public static ShowProductFragment newInstance(String code) {
        ShowProductFragment fragment = new ShowProductFragment();
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

    private void initialView(){
        iv_product_image = view.findViewById(R.id.iv_product_image);
        tv_product_name = view.findViewById(R.id.tv_product_name);
        tv_product_category = view.findViewById(R.id.tv_product_category);
        tv_product_specification = view.findViewById(R.id.tv_product_specification);
        tv_product_price = view.findViewById(R.id.tv_product_price);
    }

    private void refresh(){
        try{
            product = productService.getProductByCode(code);
            iv_product_image.setImageBitmap(productService.getProductImage(product.getProductPicture()));
            tv_product_name.setText(R.string.product_name + product.getName());
            tv_product_category.setText(R.string.product_category + product.getCategory().getName());
            tv_product_specification.setText(R.string.product_specification + product.getSpecification());
            tv_product_price.setText(R.string.product_price + product.getPrice());
        }catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}
