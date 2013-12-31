package com.test.LoginTest;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class CallAPI {
	/*
	 * EMP_NO = Request("EMP_NO") START_DATE = Request("START_DATE") //20130101
	 * END_DATE = Request("END_DATE") //20130101 BIZ_UNIT = Request("BIZ_UNIT")
	 * SALES_GRP_CD = Request("SALES_GRP_CD")
	 */
	public String select_work_data(String EMP_NO, String START_DATE,
			String END_DATE, String BIZ_UNIT, String SALES_GRP_CD) {
		RestAPI r = new RestAPI();

		String url_addr = "http://211.41.190.38/Y9201_S.asp";
		System.out.println(url_addr);
		ArrayList<NameValuePair> url_parameters = new ArrayList<NameValuePair>();

		url_parameters.add(new BasicNameValuePair("EMP_NO", EMP_NO));
		url_parameters.add(new BasicNameValuePair("START_DATE", START_DATE));
		url_parameters.add(new BasicNameValuePair("END_DATE", END_DATE));
		url_parameters.add(new BasicNameValuePair("BIZ_UNIT", BIZ_UNIT));
		url_parameters
				.add(new BasicNameValuePair("SALES_GRP_CD", SALES_GRP_CD));

		r.post(url_addr, url_parameters);
		r.getResponseString();
		return r.getResponseText();
	}

	/*
	 * PROJECT_CODE=Request("PROJECT_CODE") //201101-005
	 * WRITE_DT=Request("WRITE_DT") //2013-11-14 EMP_NO=Request("EMP_NO")
	 * CONTRACT_METHOD=Request("CONTRACT_METHOD") //2
	 * ACT_PLAN=Request("ACT_PLAN") //plan REMARK=Request("REMARK") //result
	 * INSRT_USER_ID=Request("INSRT_USER_ID") //unierp
	 * UPDT_USER_ID=Request("UPDT_USER_ID") //unierp
	 */
	public String insert_work_data(String PROJECTPCODE, String WRITE_DT,
			String EMP_NO, String CONTRACT_METHOD, String ACT_PLAN,
			String REMARK, String INSRT_USER_ID, String UPDT_USER_ID) {

		RestAPI r = new RestAPI();

		String url_addr = "http://211.41.190.38/Y9201_I.asp";
		System.out.println(url_addr);
		ArrayList<NameValuePair> url_parameters = new ArrayList<NameValuePair>();

		url_parameters
				.add(new BasicNameValuePair("PROJECTPCODE", PROJECTPCODE));
		url_parameters.add(new BasicNameValuePair("WRITE_DT", WRITE_DT));
		url_parameters.add(new BasicNameValuePair("EMP_NO", EMP_NO));
		url_parameters.add(new BasicNameValuePair("CONTRACT_METHOD",
				CONTRACT_METHOD));
		url_parameters.add(new BasicNameValuePair("ACT_PLAN", ACT_PLAN));
		url_parameters.add(new BasicNameValuePair("REMARK", REMARK));
		url_parameters.add(new BasicNameValuePair("INSRT_USER_ID",
				INSRT_USER_ID));
		url_parameters
				.add(new BasicNameValuePair("UPDT_USER_ID", UPDT_USER_ID));

		r.post(url_addr, url_parameters);
		r.getResponseString();
		return r.getResponseText();
	}

	/*
	 * KEYMAN_NO=Request("KEYMAN_NO") //0
	 * CONTRACT_METHOD=Request("CONTRACT_METHOD") //2
	 * ACT_PLAN=Request("ACT_PLAN") //plan REMARK=Request("REMARK") //result
	 * 
	 * YESAN_AMT=Request("YESAN_AMT") //0 REPORT_YN=Request("REPORT_YN") //N
	 * UPDT_USER_ID=Request("UPDT_USER_ID") //unierp
	 * 
	 * INO=Request("INO") //ino!!!!!
	 */
	public String update_work_data(String KEYMAN_NO, String CONTRACTMETHOD,
			String ACT_PLAN, String REMARK, String YESAM_AMT, String REPORT_YN,
			String UPDT_USER_ID, String INO) {
		RestAPI r = new RestAPI();

		String url_addr = "http://211.41.190.38/Y9201_U.asp";
		System.out.println(url_addr);
		ArrayList<NameValuePair> url_parameters = new ArrayList<NameValuePair>();

		url_parameters.add(new BasicNameValuePair("KEYMAN_NO", KEYMAN_NO));
		url_parameters.add(new BasicNameValuePair("CONTRACTMETHOD",
				CONTRACTMETHOD));
		url_parameters.add(new BasicNameValuePair("ACT_PLAN", ACT_PLAN));
		url_parameters.add(new BasicNameValuePair("REMARK", REMARK));
		url_parameters.add(new BasicNameValuePair("YESAM_AMT", YESAM_AMT));
		url_parameters.add(new BasicNameValuePair("REPORT_YN", REPORT_YN));
		url_parameters
				.add(new BasicNameValuePair("UPDT_USER_ID", UPDT_USER_ID));
		url_parameters.add(new BasicNameValuePair("INO", INO));

		r.post(url_addr, url_parameters);
		r.getResponseString();
		return r.getResponseText();
	}
}
