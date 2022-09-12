package com.crnr.hcms.assignment.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Namadev
 * 
 * @version 1.0
 */
public enum CommonFileds {

	ACTIVE("Active"), INACTIVE("InActive"), ALL("All"), PUBLISHED("Published"), PENDING("Pending"),
	SUCCESS("Success"), FAILURE("Failure"), ERROR("Error"),REJECT("Reject"),RESTORE("Restore"),SUSPEND("Suspend");

	private String fetchType;

	private static final List<String> fetchTypeLst;

	static {
		fetchTypeLst = new ArrayList<>();
		for (CommonFileds fetchType : CommonFileds.values()) {
			fetchTypeLst.add(fetchType.getFetchType());
		}
	}

	CommonFileds(String fetchType) {
		this.fetchType = fetchType;
	}

	public String getFetchType() {
		return fetchType;
	}

	public static List<String> getFetchTypes() {
		return Collections.unmodifiableList(fetchTypeLst);
	}
}
