package com.crnr.hcms.assignment.constant;

/**
 * @author Namadev
 * 
 * @version 1.0
 */
public class ApiConstants {

	public interface PageDefaults {
		String _LIMIT = "100";
		String _OFFSET = "0";
		String _SORT_NAME_ASC = "id";
	}

	public interface API {
		String API_VERSION = "/v1";
		String API_PATIENTS_PATH = "/api/patients";
	}

}
