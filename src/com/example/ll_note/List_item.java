package com.example.ll_note;

import java.util.Calendar;

public class List_item{
	public boolean empty;
	public Calendar date;
	public String content;
	
	public  List_item(Calendar date,boolean empty) {
		this.empty=empty;
		this.date=date;
		this.content=null;
	}
	public void setContent(String content){
		if(content==null||content.equals(""))
			empty=true;
		else{
			empty=false;
		}
		this.content=content;
	}
	public String getDayOfWeek(int i){
		String s0[]={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
		String s1[]={"SUN","MON","TUE","WED","THU","FRI","SAT"};
		int a=date.get(Calendar.DAY_OF_WEEK);
		if(i==1)
			return s1[a-1];
		else if(i==2){
			return ""+a;
		}else{
			return s0[a-1];
		}
	}
	public int getDayOfMonth(){
		return date.get(Calendar.DAY_OF_MONTH);
	}

}