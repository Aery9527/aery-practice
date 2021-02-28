package org.aery.practice.pcp.impl.center;

import org.aery.practice.pcp.api.center.CallCenter;
import org.aery.practice.pcp.api.center.EmployeeCenter;
import org.aery.practice.pcp.api.channel.PhoneChannel;
import org.aery.practice.pcp.api.channel.processor.PhoneChannelProcessor;
import org.aery.practice.pcp.impl.channel.processor.vo.PhoneChannelInitPack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CallCenterPreset implements CallCenter {

	/* [static] field */

	/* [static] */

	/* [static] method */

	/* [instance] field */

	@Autowired
	private EmployeeCenter employeeCenter;

	@Autowired
	private PhoneChannelProcessor phoneChannelProcessor;

	/* [instance] constructor */

	/* [instance] method */

	@Override
	public PhoneChannel makePhoneCall() {
		PhoneChannelInitPack initialPack = this.phoneChannelProcessor.newCommunication();
		PhoneChannel phoneForCustomer = initialPack.getForCustomeChannel();
		PhoneChannel phoneForEmployee = initialPack.getForEmployeeChannel();

		this.employeeCenter.putChannel(phoneForEmployee);

		return phoneForCustomer;
	}

	/* [instance] getter/setter */

}
