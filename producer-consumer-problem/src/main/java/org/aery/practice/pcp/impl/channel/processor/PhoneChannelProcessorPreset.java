package org.aery.practice.pcp.impl.channel.processor;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.aery.practice.pcp.api.center.EmployeeLevelCalculator;
import org.aery.practice.pcp.api.center.queue.vo.CallCenterEvent;
import org.aery.practice.pcp.api.channel.enums.EmployeeHandleResult;
import org.aery.practice.pcp.api.channel.processor.PhoneChannelProcessor;
import org.aery.practice.pcp.impl.channel.PhoneChannelPreset;
import org.aery.practice.pcp.impl.channel.processor.vo.PhoneChannelInitPack;
import org.aery.practice.pcp.impl.people.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhoneChannelProcessorPreset implements PhoneChannelProcessor {

	/* [static] field */

	/* [static] */

	/* [static] method */

	/* [instance] field */

	@Autowired
	private EmployeeLevelCalculator employeeLevelCalculator;

	/* [instance] constructor */

	/* [instance] method */

	@Override
	public PhoneChannelInitPack newCommunication() {
		String channelId = UUID.randomUUID().toString();
		AtomicBoolean finishFlag = new AtomicBoolean(false);
		PhoneChannelPreset forCustomeChannel = new PhoneChannelPreset(channelId, finishFlag);
		PhoneChannelPreset forEmployeeChannel = new PhoneChannelPreset(channelId, finishFlag);

		forCustomeChannel.setConnectedPhoneMsgProvider(forEmployeeChannel::provideMessage);
		forEmployeeChannel.setConnectedPhoneMsgProvider(forCustomeChannel::provideMessage);

		return new PhoneChannelInitPack(forCustomeChannel, forEmployeeChannel);
	}

	@Override
	public EmployeeHandleResult processChannel(Employee employee, CallCenterEvent callCenterEvent) {
		int levelFactor = employee.getLevelFactor();
		EmployeeHandleResult employeeHandleResult = this.employeeLevelCalculator.dice(levelFactor);
		return employeeHandleResult;
	}

	/* [instance] getter/setter */

}
