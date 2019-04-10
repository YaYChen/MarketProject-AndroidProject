package com.yzq.zxing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * A placeholder fragment for edit product info.
 */
public class EditProductFragment extends Fragment {

    private View view;

    private ImageView iv_product_image;
    private TextView tv_product_code;
    private EditText et_product_name;
    private EditText et_product_specification;
    private EditText et_product_purchasePrice;
    private EditText et_product_price;
    private Spinner sp_product_category;

    public EditProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_show_product, container, false);
        initialView();
        return view;
    }

    private void initialView(){
        iv_product_image = view.findViewById(R.id.iv_product_image);
        tv_product_code = view.findViewById(R.id.tv_product_code);
        et_product_name = view.findViewById(R.id.et_product_name);
        et_product_specification = view.findViewById(R.id.et_product_specification);
        et_product_purchasePrice = view.findViewById(R.id.et_product_purchasePrice);
        et_product_price = view.findViewById(R.id.et_product_price);
        sp_product_category = view.findViewById(R.id.sp_product_category);
        initialCategorySpinner();
    }

    private void initialCategorySpinner(){

    }
}
