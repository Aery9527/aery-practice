package org.aery.practice.pcp.api.channel.enums;

public enum EmployeeHandleResult {
	SUCCESS, FAILURE;

	public boolean isSuccess() {
		return equals(SUCCESS);
	}
}
