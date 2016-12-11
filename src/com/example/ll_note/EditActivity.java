package com.example.ll_note;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.ll_note.R;

public class EditActivity extends Activity {

	private EditText editText;
	private Button btn_back;
	private Button btn_done;
	private Button btn_time;
	private int year,month,day;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		Intent intent=getIntent();
		String content=intent.getStringExtra("content");
		year=intent.getIntExtra("year",-1);
		month=intent.getIntExtra("month",-1);
		day=intent.getIntExtra("day",-1);
		Calendar date=Calendar.getInstance();
		date.set(year, month-1,day);
		String day_of_week[]={"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
		String month[]={"JAN","FEB","MAR","APR","MAY","JUNE","JULY","AUG","SEPT","OCT","NOV","DEC"};
		String title=day_of_week[date.get(Calendar.DAY_OF_WEEK)-1]+"/"+month[date.get(Calendar.MONTH)]+" "+String.format("%02d",day)+"/"+year;
		TextView text_title = (TextView) findViewById(R.id.edit_title);  
		SpannableStringBuilder builder = new SpannableStringBuilder(title);  
		if(date.get(Calendar.DAY_OF_WEEK)==1){
			ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.rgb(255, 128, 128));
			builder.setSpan(redSpan, 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
		}
		text_title.setText(builder);
		btn_back=(Button)findViewById(R.id.edit_back);
		btn_done=(Button)findViewById(R.id.edit_done);
		btn_time=(Button)findViewById(R.id.edit_time);
		editText=(EditText)findViewById(R.id.edit_content);
		editText.setText(content);
		MenuClickListener clicklistener=new MenuClickListener();
		btn_done.setOnClickListener(clicklistener);
		btn_back.setOnClickListener(clicklistener);
		btn_time.setOnClickListener(clicklistener);
		editText.setOnFocusChangeListener(new EditFocusListener());
	}

	class EditFocusListener implements OnFocusChangeListener{
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			int id=v.getId();
			if(id==R.id.edit_content){
				if(hasFocus==true){
					btn_done.setVisibility(View.VISIBLE);
					btn_back.setVisibility(View.GONE);
					btn_time.setVisibility(View.VISIBLE);
				}
			}
		}
		
	}
	class MenuClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			int id=v.getId();
			if(id==R.id.edit_done){
				String content = editText.getText().toString();
				save(year,month,day,content);
		        Intent intent = new Intent();
		        intent.putExtra("content", content);
		        setResult(1000, intent);
		        finish();
			}else if(id==R.id.edit_back){
				Intent intent = new Intent();
		        setResult(1001, intent);
		        finish();
			}else {
				Editable editable = editText.getText();  
				SimpleDateFormat sdf= new SimpleDateFormat("HH:mm");
				editable.insert(editText.getSelectionStart(), sdf.format(Calendar.getInstance().getTime()));  
			}
		}
	}

    public static boolean save(int year,int month,int day,String content){
    	String pbdir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
    	File dir0 = new File(pbdir+"/LL's Note");
    	if (!dir0.exists()) {
            if(dir0.mkdirs()==false)
         	   return false;
        }
    	File dir1 = new File(pbdir+"/LL's Note/"+year);
    	if (!dir1.exists()) {
            if(dir1.mkdirs()==false)
         	   return false;
        }
    	File dir2 = new File(pbdir+"/LL's Note/"+year+"/"+month);
    	if (!dir2.exists()) {
            if(dir2.mkdirs()==false)
         	   return false;
        }
           try {
               FileOutputStream outStream=new FileOutputStream(dir2.getPath()+"/"+day);
               String string=content;
               outStream.write(string.getBytes());
               outStream.close();
               return true;
           } catch (FileNotFoundException e) {
               return false;
           }catch (IOException e){
               return false;
           } 
    }
}
