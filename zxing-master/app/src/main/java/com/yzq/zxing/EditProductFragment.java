package com.yzq.zxing;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.MySpinnerAdapter;
import entity.Category;
import entity.Product;
import service.CategoryService;
import service.ProductService;


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
    private List<Category> categories = new ArrayList<>();
    private ProductService productService = new ProductService();
    private CategoryService categoryService = new CategoryService();

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
        switch (v.getId()) {
            case R.id.btn_cancel:

                break;
            case R.id.btn_confirm:

                break;
            default:
        }
    }

    private void initialView(){
        iv_product_image = view.findViewById(R.id.iv_product_image);
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
                iv_product_image.setImageBitmap(productService.getProductImage(product.getProductPicture()));
                tv_product_code.setText(code);
                et_product_name.setText(product.getName());
                et_product_specification.setText(product.getSpecification());
                et_product_purchasePrice.setText(product.getPurchasePrice());
                et_product_price.setText(product.getPrice());
            }
        }catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
