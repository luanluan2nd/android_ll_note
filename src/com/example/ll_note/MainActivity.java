package com.example.ll_note;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.example.ll_note.MyAdapter;
import com.example.ll_note.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity   {

	private ListView listview;
	private int seletedMonth;
	private int seletedYear;
	private  List<List_item> diary_data;
	private MyAdapter adapter;
	private Button menu_month;
	private Button menu_add;
	private Button menu_year;
	private EditText yearEditText;
	private Context context;
	private YearInputClickListener yListener;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        Calendar curDate=Calendar.getInstance();
        int curYear=curDate.get(Calendar.YEAR);
        int curMonth=curDate.get(Calendar.MONTH)+1;
         listview=(ListView)this.findViewById(R.id.main_list);
         menu_month=(Button)findViewById(R.id.menu_month);
         menu_add=(Button)findViewById(R.id.menu_add);
         menu_year=(Button)findViewById(R.id.menu_year);
        setMonth(curMonth);
        seletedMonth=curMonth;
        seletedYear=curYear;
        adapter=new MyAdapter(this);
        setListData(seletedYear,seletedMonth);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new ListItemOnClickListener());
        MenuClickListener miclicklisterner=new MenuClickListener();
        menu_month.setOnClickListener(miclicklisterner);
        menu_add.setOnClickListener(miclicklisterner);
        menu_year.setOnClickListener(miclicklisterner);
        yListener=new YearInputClickListener();
        
    }
    class YearInputClickListener implements DialogInterface.OnClickListener{
    	public void onClick(DialogInterface dialog, int which) {
        	try {
        		seletedYear=Integer.valueOf(yearEditText.getText().toString());
        		menu_year.setText(""+seletedYear);
        		setListData(seletedYear,seletedMonth);
				adapter.notifyDataSetChanged();
			} catch (Exception e) {
				return;
			}
         }
    }
    public void setListData(int year,int month){
    	diary_data= new ArrayList<>();
        int  maxday=getDaysByYearMonth(year,month);
        for(int day=1;day<=maxday;day++){
        	Calendar date = Calendar.getInstance(); 
        	date.set(year, month-1, day);
        	diary_data.add(new List_item(date,true));
        }
        loadFile(year, month);
        adapter.setData(diary_data);
    }
    class MonthOnClickListener implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			setMonth(which+1);
			setListData(seletedYear,seletedMonth);
			adapter.notifyDataSetChanged();
			dialog.dismiss();
		}
    	
    }
    public void setMonth(int month){
    	seletedMonth=month;
		String mon[]={"JAN","FEB","MAR","APR","MAY","JUNE","JULY","AUG","SEPT","OCT","NOV","DEC"};
		menu_month.setText(mon[month-1]);
    }
    class MenuClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			int id=v.getId();
			if(id==R.id.menu_month){
				new AlertDialog.Builder(context)  
				.setTitle("MONTH")  
				.setSingleChoiceItems(new String[] {"JAN","FEB","MAR","APR","MAY","JUNE","JULY","AUG","SEPT","OCT","NOV","DEC"}, seletedMonth-1, new MonthOnClickListener())                 
				.setNegativeButton("取消", null)  
				.show();  
			}else if(id==R.id.menu_add){
				Calendar curDate=Calendar.getInstance();
		        int year=curDate.get(Calendar.YEAR);
		        int month=curDate.get(Calendar.MONTH)+1;
		        int day=curDate.get(Calendar.DAY_OF_MONTH);
		        Intent intent =new Intent();
				intent.setClass(MainActivity.this, EditActivity.class);
		        int i=-1;
		        if(year==seletedYear&&month==seletedMonth){
		        	i=day-1;
		        	List_item itemdata=diary_data.get(i);
		        	intent.putExtra("content",itemdata.content);
		        }else{
		            String path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/LL's Note/"+year+"/"+month+"/"+day;
		            intent.putExtra("content",load(path));
		        }
				intent.putExtra("year",year);
				intent.putExtra("month",month);
				intent.putExtra("day",day);
				startActivityForResult(intent,i);
			}else if(id==R.id.menu_year){
				yearEditText=new EditText(context);
				yearEditText.setText(seletedYear+"");
				new AlertDialog.Builder(context)
		        .setTitle("YEAR")  
		        .setView(yearEditText)
		        .setPositiveButton("确定", yListener)  
		        .setNegativeButton("取消", null)
		        .show();
			}
		}
    	
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1000){
        	if(requestCode!=-1){
        		String content = data.getStringExtra("content");
	        	List_item item=diary_data.get(requestCode);
	        	item.setContent(content);
	        	adapter.notifyDataSetChanged();
        	}
        }
    }
    class ListItemOnClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			List_item itemdata=diary_data.get((int)id);
			Intent intent =new Intent();
			intent.setClass(MainActivity.this, EditActivity.class);
			intent.putExtra("content", itemdata.content);
			intent.putExtra("year", itemdata.date.get(Calendar.YEAR));
			intent.putExtra("month",itemdata.date.get(Calendar.MONTH)+1);
			intent.putExtra("day", itemdata.date.get(Calendar.DAY_OF_MONTH));
			startActivityForResult(intent,(int)id);
		}
    	
    }
    public void loadFile(int year,int month){
    	String s=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/LL's Note/"+year+"/"+month;
    	File dir = new File(s);
    	String paths[]=dir.list();
    	if(paths!=null){
	    	for(String name:paths){
	    		diary_data.get(Integer.valueOf(name)-1).setContent(load(dir+"/"+name));
	    	}
    	}
    }

    public String load(String path)
    {
        try {
            FileInputStream inStream=new FileInputStream(path);
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            byte[] buffer=new byte[10];
            int length=-1;
            while((length=inStream.read(buffer))!=-1)   {
                stream.write(buffer,0,length);
            }
            stream.close();
            inStream.close();
            return stream.toString();
        } catch (FileNotFoundException e) {
        	return null;
        }catch (IOException e){
            return null;
        }
    }  

    public int getDaysByYearMonth(int year, int month) {  
        Calendar a = Calendar.getInstance();  
        a.set(Calendar.YEAR, year);  
        a.set(Calendar.MONTH, month - 1);  
        a.set(Calendar.DATE, 1);  
        a.roll(Calendar.DATE, -1);  
        int maxDate = a.get(Calendar.DATE);  
        return maxDate;  
    }  
}
