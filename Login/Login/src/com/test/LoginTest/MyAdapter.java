package com.test.LoginTest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

@TargetApi(11)
public class MyAdapter extends BaseAdapter {
	private LayoutInflater myInflater;
	public ArrayList<ListItem> myItems = new ArrayList<ListItem>();
	public ArrayList<WorkData> work_info;
	private Context context;
	private View detail;

	static int detailPosition;

	// caller activity
	private ListActivity caller;
	static int fromAdapter = 123456789;
	// for myList 
	Date today;
	final int listTextSize = 10;
	String [] intToDay =  {"일", "월", "화", "수", "목", "금", "토"};
	

	public MyAdapter(Context c, ArrayList<WorkData> info, View d, Activity a) {
		System.out.println("MyAdapter start");
		// init
		work_info = info;
		context = c;
		detail = d;
		caller = (ListActivity) a;
		
		myInflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		init_myItems(info);
		notifyDataSetChanged();
		
		
	}

	void init_myItems(ArrayList<WorkData> info) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.SUNDAY);
		// Date() >> current date (for init) 
		today = new Date(2013,1,22); //test date
		// init myItems
		for (int i = 0; i < info.size(); i++) {
			ListItem listItem = new ListItem();
			// tmp
			listItem.elem1 = "" + strToDate(c,info.get(i).work_info.get("write_dt"));
			listItem.elem2 = "" + info.get(i).work_info.get("project_nm");
			myItems.add(listItem);
		}
	}
	
	String strToDate(Calendar c, String str){
		//str's form : yyyy-mm-dd
		String yyyy = str.substring(0, 4);
		String mm = str.substring(5,7);
		String dd = str.substring(8,10);
		@SuppressWarnings("deprecation")
		Date tmpDate = new Date(Integer.parseInt(yyyy),Integer.parseInt(mm),Integer.parseInt(dd));
		c.setTime(tmpDate);
		String day; int diff = today.getDate() - tmpDate.getDate();
		if(diff == 0){
			day = "오늘";
		}else if(diff == 1){
			day = "내일";
		}else if(diff == -1){
			day = "어제";
		}else{
			day = intToDay[c.get(Calendar.DAY_OF_WEEK)-1];
		}
		return day+"."+yyyy+"."+mm+"."+dd;
	}

	public int getCount() {
		return myItems.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = myInflater.inflate(R.layout.item, null);// convertView
																	// is a
																	// frame
			holder.tv2 = (TextView) convertView
					.findViewById(R.id.ItemText2);
			holder.tv2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					caller.changeDetail(position, fromAdapter);
				}
			});
			
			holder.tv1 = (TextView) convertView.findViewById(R.id.ItemText1);// test
			convertView.setTag(holder);
			holder.tv1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					caller.changeDetail(position, fromAdapter);
				}
			});

			holder.btn = (Button) convertView.findViewById(R.id.ItemButton);
			holder.btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					detailPosition = position;
					caller.changeDetail(detailPosition, 0);

					// setting detail View
					System.out.println("onclick");
				}
			});
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv1.setTextColor(Color.BLACK);
		// holder.Text.setTypeface(typeFace);
		holder.tv1.setTextSize(listTextSize);
		holder.tv1.setText(((ListItem) (myItems.get(position))).elem1);

		// Fill EditText with the value you have in data source
		holder.tv2.setText(((ListItem) (myItems.get(position))).elem2);
		// holder.actv.setTypeface(typeFace);
		holder.tv2.setTextSize(listTextSize);

		return convertView;
	}

}

class ViewHolder {
	TextView tv1;
	TextView tv2;
	Button btn;
}

class ListItem {
	String elem1;
	String elem2;
}
