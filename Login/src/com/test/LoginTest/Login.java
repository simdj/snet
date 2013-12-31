package com.test.LoginTest;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
//xml parser

public class Login extends Activity {

	public static final String PREFS_NAME = "prefs";		
	SharedPreferences prefs;		

	EditText id_input = null;
	EditText emp_no_input = null;
	EditText password_input = null;
	
	CheckBox auto_login_check= null;
	CheckBox save_id_check=null;

	Button login_but= null;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		final String id_saved = prefs.getString("id", null);
		final String emp_no_saved = prefs.getString("emp_no", null);
		final String password_saved = prefs.getString("password", null);


		setContentView(R.layout.login);
		System.out.println(id_saved+emp_no_saved+password_saved);

		id_input = (EditText) findViewById(R.id.login);
		emp_no_input = (EditText) findViewById(R.id.emp_no);
		password_input = (EditText) findViewById(R.id.password);
		
		auto_login_check = (CheckBox) findViewById(R.id.check_auto_login);
		save_id_check = (CheckBox) findViewById(R.id.check_save_id);
		
		
		login_but = (Button) findViewById(R.id.loginbutton);

		
		if (id_saved != null){
			id_input.setText(id_saved);
		}

		if (id_saved != null && emp_no_saved != null && password_saved!=null){
			id_input.setText(id_saved);
			emp_no_input.setText(emp_no_saved);
			password_input.setText(password_saved);
			new RestAPITask().execute(id_input.getText().toString(), emp_no_input.getText().toString(), password_input.getText().toString());
		}else{
			
			id_input.setText("UNIERP");
			emp_no_input.setText("9999999");
			password_input.setText("9");

				

			login_but.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					new RestAPITask().execute(id_input.getText().toString(), emp_no_input.getText().toString(), password_input.getText().toString());			
				}
			});
		}


	}


	public void login(String xml_result) throws Exception{
		System.out.println(xml_result);
		
		//parsing start
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xml_result));
		is.setEncoding("EUC-KR");


		DocumentBuilder db=DocumentBuilderFactory.newInstance().newDocumentBuilder();;
		Document doc= db.parse(is);

		Element root=doc.getDocumentElement();
		String LRETURN=root.getElementsByTagName("LRETURN").item(0).getChildNodes().item(0).getNodeValue();
		String LMESSAGE=root.getElementsByTagName("LMESSAGE").item(0).getChildNodes().item(0).getNodeValue();
		
		
		
		
		//parsing end
		
		
		Toast.makeText(getApplicationContext(), LMESSAGE, Toast.LENGTH_LONG).show();

		//login success
		if(LRETURN.equals("0")){

			if(save_id_check.isChecked()){
				prefs.edit().putString("id", id_input.getText().toString()).commit();
			}
			//auto login checked -> save 
			if( auto_login_check.isChecked() ){
				prefs.edit().putString("id", id_input.getText().toString()).commit();
				prefs.edit().putString("emp_no", emp_no_input.getText().toString()).commit();
				prefs.edit().putString("password", password_input.getText().toString()).commit();
			}

			//go to main page
			go_to_main(xml_result);
		}
	}

	public void go_to_main(String login_result_xml) throws Exception{

		//after login 
		Intent i = new Intent(Login.this, LoginTestActivity.class);
		i.putExtra("login_result_xml", login_result_xml);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();




	}

	class RestAPITask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {

			//System.out.println(params[0]);
			//System.out.println(params[1]);
			//System.out.println(params[2]);

			RestAPI r = new RestAPI();
			//http://211.41.190.38/Y9200.ASP?USR_ID=UNIERP&EMP_NO=9999999&PASS_WORD_REAL=9
			String url_addr="http://211.41.190.38/Y9200.ASP?USR_ID="+params[0]+"&EMP_NO="+params[1]+"&PASS_WORD_REAL="+params[2];
			System.out.println(url_addr);
			r.get(url_addr);
			r.getResponseString();
			return r.getResponseText();

		}

		@Override
		protected void onPostExecute(String xml_result) 
		{
			try {
				login(xml_result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

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

}

