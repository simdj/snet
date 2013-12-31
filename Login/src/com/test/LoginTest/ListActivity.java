package com.test.LoginTest;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@TargetApi(11)
public class ListActivity extends Activity implements OnTouchListener{
	private HashMap<String, String> login_info;
	private ArrayList<WorkData> work_data_arr;
	private EditText etStartDate, etEndDate;
	View tmpV;
	static final int DATE_DIALOG_ID = 0;

	private int year;
	private int month;
	private int day;
	private Calendar c;
	// for list
	private ListView myList;
	private MyAdapter myAdapter;
	float deviceWidth;
	float deviceHeight;
	public Typeface typeFace;
	// for add
	private Button btnAdd;
	//for show
	private Button btnShow;
	// for detail view
	private View vDetail;
	private TextView tvDetailPlan;
	private TextView tvDetailResult;
	private EditText etDetailPlan;
	private EditText etDetailResult;
	private Button btnDetailCancel;
	private Button btnDetailEdit;
	// for detail ani
	private Animation transRightAni;
	private Animation transLeftAni;
	float startX = 0;
	float basePnt = 0;
	float nowX;
	float preX;
	float diffX;
	int detailPosition;
	int nowDetailPostion = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		work_data_arr = new ArrayList<WorkData>();
		setting_info();
		init();
		initDate();

		new RestAPITask().execute();

	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public class PageAnimationListener implements AnimationListener {

		public void onAnimationEnd(Animation arg0) {

		}

		public void onAnimationRepeat(Animation arg0) {
			// TODO Auto-generated method stub

		}

		public void onAnimationStart(Animation arg0) {
			// TODO Auto-generated method stub

		}

	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	private void setting_info() {
		login_info = new HashMap<String, String>();
		// work_info= new HashMap<String, String>();

		Bundle bundle = getIntent().getExtras();
		login_info.put("BIZ_UNIT", bundle.getString("BIZ_UNIT"));
		login_info.put("EMP_NO", bundle.getString("EMP_NO"));
		login_info.put("SALES_GRP", bundle.getString("SALES_GRP"));

	}

	public void parsing_select_query(String xml_result)
			throws ParserConfigurationException, SAXException, IOException {
		// EditText view_result = (EditText) findViewById(R.id.editText1);
		// view_result.setText(xml_result);
		Log.d("hi", xml_result);

		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xml_result));
		is.setEncoding("EUC-KR");

		DocumentBuilder db = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = db.parse(is);

		Element root = doc.getDocumentElement();

		NodeList rows = root.getElementsByTagName("row");

		for (int i = 0; i < rows.getLength(); i++) {
			Node row_node = rows.item(i);
			if (row_node.getNodeType() == Node.ELEMENT_NODE) {
				Element row_elmnt = (Element) row_node;

				WorkData tmp_work = new WorkData();

				for (int k = 0; k < WorkData.select_fields.length; k++) {
					try {
						tmp_work.work_info
								.put(WorkData.select_fields[k],
										row_elmnt
												.getElementsByTagName(
														WorkData.select_fields[k])
												.item(0).getFirstChild()
												.getNodeValue());

					} catch (Exception e) {

					}
				}

				// work_data init complete append go
				work_data_arr.add(tmp_work);

			}

		}
		// view_result.setText(test_res);
		make_list_view();

	}

	// ���⑥닔瑜�遺�Ⅴ吏��딆쓬
	public void make_list_view() {
		System.out
				.println("make_list_view works!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		ArrayList<WorkData> tmpList = new ArrayList<WorkData>();
		for (int i = 0; i < 5; i++) {
			WorkData tmpV = new WorkData();
			tmpV.work_info.put("write_dt", "" + i);
			tmpV.work_info.put("project_nm", "abcd" + i);
			tmpList.add(tmpV);
		}
		myAdapter = new MyAdapter(getApplicationContext(), work_data_arr,
				vDetail, this);
		myList.setAdapter(myAdapter);

	}

	class RestAPITask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			RestAPI r = new RestAPI();
			// http://211.41.190.38/Y9200.ASP?USR_ID=UNIERP&EMP_NO=9999999&PASS_WORD_REAL=9
			// String url_addr="http://211.41.190.38/Y9201_S.asp";
			// String url_addr="http://211.41.190.38/list.asp";

			String url_addr = "http://211.41.190.38/query.asp";
			System.out.println(url_addr);
			ArrayList<NameValuePair> url_parameters = new ArrayList<NameValuePair>();

			System.out.println(login_info.get("EMP_NO"));
			System.out.println(login_info.get("BIZ_UNIT"));
			System.out.println(login_info.get("SALES_GRP"));

			url_parameters.add(new BasicNameValuePair("EMP_NO", login_info
					.get("EMP_NO")));
			url_parameters.add(new BasicNameValuePair("BIZ_UNIT", login_info
					.get("BIZ_UNIT")));
			url_parameters.add(new BasicNameValuePair("SALES_GRP", login_info
					.get("SALES_GRP")));

			url_parameters.add(new BasicNameValuePair("query",
					get_select_query("", "", "", "", "")));

			r.post(url_addr, url_parameters);
			r.getResponseString();
			return r.getResponseText();

		}

		@Override
		protected void onPostExecute(String xml_result) {

			System.out.println(xml_result);

			try {
				parsing_select_query(xml_result);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private String get_select_query(String start_date, String end_date,
			String emp_no, String biz_unit, String salesgrp_cd) {
		String query = "";
		query += " SELECT  ino, a.write_dt, a.project_code, b.preproject_NM project_nm ";
		query += "      	, a.contractmethod, dbo.ufn_GetCodeName('YS014', A.CONTRACTMETHOD) contractmethod_nm ";
		query += "		, a.act_plan, a.remark act_result ";
		query += "	    , a.report_yn, a.week_end ";
		query += "	     , b.cust_cd, d.bp_nm ";
		query += "	     , b.est_amt ";
		query += "	     , a.emp_no, e.name emp_nm";

		query += "	  FROM y_week_work_ko473 a INNER      JOIN pms_pre_sale  b on a.project_code = b.preproject_no ";
		query += "	                           LEFT OUTER JOIN b_biz_partner d on b.cust_cd = d.bp_cd";
		query += "	                           LEFT OUTER JOIN haa010t       e on a.emp_no = e.emp_no";

		query += "	 WHERE convert(char(8), a.write_dt, 112) BETWEEN '20130101' AND '20130131'"; // select
		query += "	   AND A.EMP_NO  = '2006031' "; // may be select
		query += "	   AND B.BIZ_UNIT = 'D11' "; // may be select
		query += "	   AND B.salesgrp_cd = 'S111'"; // may be select

		// query+="FOR XML PATH";
		return query;
	}

	private String get_insert_query(String plan, String result) {
		String query = "";
		query += "INSERT INTO Y_Week_Work_KO473 ";
		query += "	( PROJECT_CODE, 	WRITE_DT,	 KEYMAN_NO, 	EMP_NO,		 ContractMethod, 	 ACT_Plan,   	REMARK,";
		query += "		  YESAN_AMT              					, REPORT_YN,  INSRT_USER_ID, INSRT_DT, 		UPDT_USER_ID, 	UPDT_DT   ) ";

		query += " VALUES ";
		query += "	( '201101-005'  , '2013-11-14'  , '0'  ,	 '9999999'  , 			'2'  , 			'"
				+ plan + "'  , '" + result + "', ";
		query += " 		 CAST(REPLACE('0', ',', '') AS NUMERIC(21,6) ) , 'N'  , 'unierp', 		Getdate()  , 	'unierp', 		Getdate()  ) ";

		return query;

	}

	private String get_update_query() {
		String query = "";
		query += "UPDATE  Y_Week_Work_KO473   SET  ";
		query += "	 KEYMAN_NO = '0' ";
		query += "	, ContractMethod = '2' ";
		query += "	, ACT_Plan = '계획         '   ";
		query += "	, REMARK = '결과' ";
		query += "	, YESAN_AMT = CAST(REPLACE('0', ',', '') AS NUMERIC(21,6) ) ";
		query += "	, REPORT_YN = 'N' ";
		query += "	, UPDT_USER_ID = 'unierp'";
		query += "	, UPDT_DT = Getdate() ";

		query += "	 WHERE INO = CAST(REPLACE('3,818', ',', '') AS INT) ";

		return query;

	}

	public void initDate() {
		etStartDate = (EditText) findViewById(R.id.etStartDate);
		etEndDate = (EditText) findViewById(R.id.etEndDate);
		etStartDate.setKeyListener(null);
		etEndDate.setKeyListener(null);
		etStartDate.setTextSize(10);
		etEndDate.setTextSize(10);

		etStartDate.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				tmpV = v;
				showDialog(DATE_DIALOG_ID);
			}
		});

		etEndDate.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				tmpV = v;
				showDialog(DATE_DIALOG_ID);
			}
		});

		c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.SUNDAY);
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		int dateIdx = c.get(Calendar.DAY_OF_WEEK);
		c.add(Calendar.DAY_OF_MONTH, 1 - dateIdx);
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		etStartDate.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(month + 1).append("-").append(day).append("-")
				.append(year).append(" "));

		c.add(Calendar.DAY_OF_MONTH, 6);
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		etEndDate.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(month + 1).append("-").append(day).append("-")
				.append(year).append(" "));

	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, year, month,
					day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int y, int monthOfYear,
				int dayOfMonth) {
			year = y;
			month = monthOfYear;
			day = dayOfMonth;
			if (tmpV.getId() == R.id.etStartDate) {
				updateDateEt(etStartDate);
			} else {
				updateDateEt(etEndDate);
			}

		}

		public void updateDateEt(EditText et) {

			et.setText(new StringBuilder()
					// Month is 0 based, just add 1
					.append(month + 1).append("-").append(day).append("-")
					.append(year).append(" "));
		}
	};

	public void init() {
		// setting
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		deviceWidth = dm.widthPixels;
		deviceHeight = dm.heightPixels;

		// for fonts
		typeFace = Typeface.createFromAsset(getAssets(), "fonts/NanumPen.ttf");
		tvDetailPlan = (TextView) findViewById(R.id.DetailPlan);
		tvDetailPlan.setTypeface(typeFace, Typeface.BOLD);
		tvDetailPlan.setTextSize(25.0f);
		tvDetailResult = (TextView) findViewById(R.id.DetailResult);
		tvDetailResult.setTypeface(typeFace, Typeface.BOLD);
		tvDetailResult.setTextSize(25.0f);
		etDetailPlan = (EditText) findViewById(R.id.EditDetailPlan);
		etDetailPlan.setTypeface(typeFace, Typeface.BOLD);
		etDetailPlan.setTextSize(20.0f);
		etDetailResult = (EditText) findViewById(R.id.EditDetailResult);
		etDetailResult.setTypeface(typeFace, Typeface.BOLD);
		etDetailResult.setTextSize(20.0f);
		btnDetailCancel = (Button) findViewById(R.id.DetailCancel);
		btnDetailCancel.setTypeface(typeFace, Typeface.BOLD);
		btnDetailCancel.setTextSize(25.0f);
		btnDetailEdit = (Button) findViewById(R.id.DetailEdit);
		btnDetailEdit.setTypeface(typeFace, Typeface.BOLD);
		btnDetailEdit.setTextSize(25.0f);

		// for Add
		
		
		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnAdd.setTypeface(typeFace, Typeface.BOLD);
		btnAdd.setTextSize(25.0f);

		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		myList = (ListView) findViewById(R.id.MyList);
		myList.setItemsCanFocus(true);
		myList.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				System.out.println("ontouch");
				switch(action){
				case MotionEvent.ACTION_UP:
						if(vDetail.getVisibility() == View.VISIBLE) changeDetail(0, MyAdapter.fromAdapter);
					default:
						break;
				}
				//then find other listener >> maybe myList's touchlistener
				return false;
			}
		});

		vDetail = (View) findViewById(R.id.DetailLayout);
		vDetail.setTranslationX(deviceWidth * 0.15f);
		vDetail.setVisibility(View.GONE);
		// for margin to detail
		RelativeLayout.LayoutParams relParams = (RelativeLayout.LayoutParams) vDetail
				.getLayoutParams();
		relParams.rightMargin = (int) (deviceWidth * 0.15f);
		vDetail.setLayoutParams(relParams);
		// setting for ani
		basePnt = vDetail.getTranslationX();
		deviceWidth = basePnt * (100.0f / 15.0f) + 10.0f;
		transLeftAni = AnimationUtils
				.loadAnimation(this, R.anim.translate_left);
		transRightAni = AnimationUtils.loadAnimation(this,
				R.anim.translate_right);

		PageAnimationListener aniListener = new PageAnimationListener();

		transLeftAni.setAnimationListener(aniListener);
		transRightAni.setAnimationListener(aniListener);

		// register touch event
		vDetail.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					preX = event.getX();
					startX = v.getX();
					System.out.println("startX = " + startX);
					diffX = 0.0f;
					break;
				case MotionEvent.ACTION_MOVE:
					nowX = event.getX();
					diffX = nowX - preX;
					// System.out.println("startX = "+startX +
					// ", nowX = "+nowX);
					if ((startX + diffX) < basePnt) {
						startX = basePnt;
						preX = nowX;
						v.setX(basePnt);
					} else {
						startX += diffX; // !!!
						v.setX(startX);
					}
					// System.out.println("startX = "+startX
					// +", get = "+v.getX());
					startX = v.getX();
					break;
				case MotionEvent.ACTION_UP:
					v.setX(basePnt);
					// System.out.println("upX = "+upX+", nowX = "+nowX);
					//using direction of velocity
					if (1 < diffX ) {
						changeDetail(nowDetailPostion, startX);
					}
					break;
				case MotionEvent.ACTION_CANCEL:
					break;
				default:
					break;
				}
				return true;
			}
		});

	}

	void changeDetail(int position, float nowX) {
		if( nowX == MyAdapter.fromAdapter){
			if(vDetail.getVisibility() == View.VISIBLE){
				vDetail.setVisibility(View.INVISIBLE);
				vDetail.startAnimation(transRightAni);
				return ;
			}
		}
		// setting detaillayout
		if (position != nowDetailPostion) {
			String tmp = "";
			for (int i = 0; i < work_data_arr.get(position).select_fields.length; i++) {
				String tmpKey = work_data_arr.get(position).select_fields[i];
				tmp += tmpKey;
				tmp += " : ";
				tmp += work_data_arr.get(position).work_info.get(tmpKey) + "\n";
			}
			etDetailPlan.setText(tmp);
		}

		vDetail.setTranslationX(basePnt);
		if (vDetail.getVisibility() != View.VISIBLE) {
			vDetail.setVisibility(View.VISIBLE);
			vDetail.startAnimation(transLeftAni);
		} else {
			vDetail.setVisibility(View.INVISIBLE);
			System.out.println(deviceWidth + "," + nowX);
			Animation toRightAni = new TranslateAnimation(nowX, deviceWidth, 0,
					0);
			toRightAni.setDuration(500);
			toRightAni.setFillAfter(true);
			toRightAni.setAnimationListener(new AnimationListener() {
				public void onAnimationStart(Animation animation) {
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					vDetail.setTranslationX(deviceWidth);
				}
			});
			vDetail.startAnimation(toRightAni);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (vDetail.getVisibility() == View.VISIBLE) {
					changeDetail(0, MyAdapter.fromAdapter);
					return false;
				}
			}
			if (keyCode == KeyEvent.KEYCODE_HOME) {

			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.v("onclick", "click");
		return true;
	}

}
