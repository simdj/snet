package com.test.LoginTest;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginTestActivity extends Activity {
	/** Called when the activity is first created. */
	TextView textView;

	private HashMap<String, String> login_info;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Bundle bundle = getIntent().getExtras();
		String login_result_xml = bundle.getString("login_result_xml");
		try {
			parsing_and_save_login_info(login_result_xml);
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





		//Button btnPrefs = (Button) findViewById(R.id.btnPrefs);
		//Button btnGetPrefs = (Button) findViewById(R.id.btnGetPreferences);
		Button btnLogout = (Button) findViewById(R.id.logout_btn);
		Button btnList = (Button) findViewById(R.id.list_btn);
		
		//textView = (TextView) findViewById(R.id.txtPrefs);

		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
//				case R.id.btnPrefs:
//				{
//					Intent intent = new Intent(LoginTestActivity.this,PrefsActivity.class);
//					startActivity(intent);
//				}
//				break;
//
//				case R.id.btnGetPreferences:
//					displaySharedPreferences();
//					break;
				case R.id.logout_btn:
					logout();
					break;
				case R.id.list_btn:
				{
					Intent intent = new Intent(LoginTestActivity.this,ListActivity.class);
					intent.putExtra("BIZ_UNIT", login_info.get("BIZ_UNIT"));
					intent.putExtra("SALES_GRP", login_info.get("SALES_GRP"));
					intent.putExtra("EMP_NO", login_info.get("EMP_NO"));
					startActivity(intent);
				}
				break;

				default:
					break;
				}
			}
		};

		//btnPrefs.setOnClickListener(listener);
		//btnGetPrefs.setOnClickListener(listener);
		btnLogout.setOnClickListener(listener);
		btnList.setOnClickListener(listener);
	}

	private void displaySharedPreferences() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(LoginTestActivity.this);

		String username = prefs.getString("id", "");
		String password = prefs.getString("password", "");

		StringBuilder builder = new StringBuilder();
		builder.append("id " + username + "\n");
		builder.append("password: " + password + "\n");

		//textView.setText(builder.toString());
	}

	public void logout(){
		final String PREFS_NAME = "prefs";		
		SharedPreferences prefs;
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		prefs.edit().putString("id", null).commit();
		prefs.edit().putString("emp_no", null).commit();
		prefs.edit().putString("password", null).commit();

		Intent i = new Intent(this, Login.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle("어플 종료")
		.setMessage("어플을 종료하시겠습니까?")
		.setPositiveButton("종료", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();    
			}

		})
		.setNegativeButton("취소", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}

		})
		.show();
	}



	//only once called
	private void parsing_and_save_login_info(String login_result_xml) throws ParserConfigurationException, SAXException, IOException{
		login_info= new HashMap<String, String>();


		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(login_result_xml));
		is.setEncoding("EUC-KR");


		DocumentBuilder db=DocumentBuilderFactory.newInstance().newDocumentBuilder();;
		Document doc= db.parse(is);

		Element root=doc.getDocumentElement();

		String fields[] = {  
				"BIZ_UNIT",				"BIZ_UNIT_NM",
				"SALES_GRP",			"SALES_GRP_NM",
				"DEPT_CD",				"DEPT_NM",
				"USR_ID",				"USR_NM",
				"EMP_NO",				"EMP_NM",
				"LEVEL_FG"
		};
		for(int i=0;i<fields.length;i++){
			try{
				login_info.put(fields[i],  root.getElementsByTagName(fields[i]).item(0).getChildNodes().item(0).getNodeValue() );
			}catch(Exception e){
				login_info.put(fields[i],"");
			}
		}

		

	}
	
	
	
}

