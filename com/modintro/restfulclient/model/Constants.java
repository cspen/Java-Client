package com.modintro.restfulclient.model;

/**
 * Application wide constants.
 *
 */

public interface Constants {
	
	String APP_URL = "http://modintro.com/employees/";
	String DEPT_URL = "http://modintro.com/departments/";
	
	Object[] colNames = {"EmployeeID", "Last_name", "First_name",
			"Department", "Full_time", "Hire_date", "Salary" };
	
	int ID_COL = 0;
	int FIRST_NAME_COL = 1;
	int LAST_NAME_COL = 2;
	int DEPARTMENT_COL = 3;
	int FULL_TIME_COL = 4;
	int HIRE_DATE_COL = 5;
	int SALARY_COL = 6;
	int ETAG_COL = 7;
	int LAST_MOD_COL = 8;

}
