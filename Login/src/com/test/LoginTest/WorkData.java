package com.test.LoginTest;

import java.util.HashMap;

public class WorkData {


	public HashMap<String, String> work_info;
	public WorkData(){
		work_info = new HashMap<String, String>();
	}
	static String select_fields[] = {
		
		"ino",
		
		"write_dt",		
		
		"project_code", "project_nm",
		
		"contractmethod","contractmethod_nm",

		"act_plan","act_result",

		"report_yn", "week_end",

		"cust_cd", "bp_nm",
		
		"yesan_amt",

		"emp_no", "emp_nm",
		
	};
	
	static String insert_field[]={
		"project_code", "write_dt", "keyman_no", "emp_no",
		"contractmethod", "act_plan", "remark", "yesan_amt", 
		"report_yn", "insrt_user_id", "insrt_dt", "updt_user_id", "updt_dt"
	};
	
	static String update_field[]={
		"keyman_no", "contractmethod", "act_plan", "remark", "yesam_amt", "report_yn", "updt_user_id", "updt_dt", "ino"
	};


	
}



