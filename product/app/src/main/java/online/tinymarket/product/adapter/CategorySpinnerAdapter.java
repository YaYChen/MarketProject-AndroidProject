package online.tinymarket.product.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import online.tinymarket.product.R;
import online.tinymarket.product.entity.Category;

public class CategorySpinnerAdapter extends BaseAdapter {
  private List<Category> itemList;
  private Activity activity;

  public CategorySpinnerAdapter(Activity activity, List<Category> list) {
    this.activity = activity;
    this.itemList = list;
  }

  @Override
  public int getCount() {
    return itemList.size();
  }

  @Override
  public Object getItem(int position) {
    return itemList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater layoutInflater= LayoutInflater.from(activity);
    convertView = layoutInflater.inflate(R.layout.view_spinner_item, null);
    if(convertView != null)
    {
      TextView tv_item_name = convertView.findViewById(R.id.tv_item_name);
      tv_item_name.setText(itemList.get(position).getName());
    }
    return convertView;
  }
}