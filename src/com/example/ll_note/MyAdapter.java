package com.example.ll_note;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ll_note.List_item;
import com.example.ll_note.R;


import java.util.List;

public class MyAdapter extends BaseAdapter {


    private Context context;

    private List<List_item> data;

    public MyAdapter(Context context) {
        this.context = context;

    }

    public void setData(List<List_item> data){
    	this.data=data;

    }
    @Override
    public int getCount() {
    		return data.size();
    }

    @Override
    public List_item getItem(int position) {
    		return data.get(position);
    }

    @Override
    public long getItemId(int position) {
    		return position;   
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder = null;
        List_item item = getItem(position);
            System.out.println("1");
	        if(convertView == null){
	            viewholder = new ViewHolder();
	            convertView = View.inflate(context, R.layout.list_item, null);
	            viewholder.list_item=(RelativeLayout)convertView.findViewById(R.id.list_item);
	            viewholder.day_of_week = (TextView) convertView.findViewById(R.id.item_day_of_week);
	            viewholder.day_of_month = (TextView) convertView.findViewById(R.id.item_day_of_month);
	            viewholder.content = (TextView) convertView.findViewById(R.id.item_content);
	            convertView.setTag(R.id.tag_item, viewholder);
	        }else{
	        	viewholder = (ViewHolder) convertView.getTag(R.id.tag_item);
	        }
	        System.out.println("2");
	        System.out.println("------"+Integer.valueOf(item.getDayOfWeek(2)));
            
            System.out.println("2.5");
             if(!item.empty){
            	 viewholder.day_of_week.setText(item.getDayOfWeek(1));
                 viewholder.day_of_month.setText(""+item.getDayOfMonth());
                 viewholder.content.setText(item.content);
                 viewholder.content.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
                 System.out.println("2.60");
                 viewholder.day_of_week.setVisibility(View.VISIBLE);
	             viewholder.day_of_month.setVisibility(View.VISIBLE);
	             viewholder.content.setTextColor(Color.rgb(87, 87, 87)); 
	             viewholder.content.setTextSize(20);
	             viewholder.list_item.setBackgroundColor(Color.rgb(252, 237, 237));
	             if(Integer.valueOf(item.getDayOfWeek(2))==1){
	          	   viewholder.day_of_week.setTextColor(Color.rgb(255, 128, 128));
	          	   viewholder.day_of_month.setTextColor(Color.rgb(255, 128, 128));
	             }else{
	          	   viewholder.day_of_week.setTextColor(Color.rgb(255, 255, 255)); 
	          	   viewholder.day_of_month.setTextColor(Color.rgb(255, 255, 255));
	             }
            }else{
            	System.out.println("2.61");
	             viewholder.day_of_week.setVisibility(View.GONE);
	             viewholder.day_of_month.setVisibility(View.GONE);
	             System.out.println("2.71");
	             viewholder.content.setText("¡ñ");
	             viewholder.content.setGravity(Gravity.CENTER);
	             viewholder.content.setTextSize(14);
	             viewholder.list_item.setBackgroundColor(Color.rgb(234, 234, 234));
	             if(Integer.valueOf(item.getDayOfWeek(2))==1){
	          	   viewholder.content.setTextColor(Color.rgb(255, 166, 166));
	             }else{
	          	   viewholder.content.setTextColor(Color.rgb(255, 255, 255)); 
	             }
	             System.out.println("2.81");
            }
             System.out.println("3");
   
        return convertView;
    }

    private static class ViewHolder {
    	RelativeLayout list_item;
        TextView day_of_week;
        TextView day_of_month;
        TextView content;
    }


}