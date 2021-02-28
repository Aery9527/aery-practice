package org.aery.practice.pcp.api.channel.processor;

import org.aery.practice.pcp.api.center.queue.vo.CallCenterEvent;
import org.aery.practice.pcp.api.channel.CommunicationChannel;
import org.aery.practice.pcp.api.channel.enums.EmployeeHandleResult;
import org.aery.practice.pcp.api.channel.processor.vo.ChannelInitialPack;
import org.aery.practice.pcp.impl.people.Employee;

public interface ChannelProcessor< //
		ChannelType extends CommunicationChannel, //
		InitialPackType extends ChannelInitialPack<ChannelType> //
> {

	/* [static] field */

	/* [static] */

	/* [static] method */

	/* [instance] field */

	/* [instance] constructor */

	/* [instance] method */

	public Class<ChannelType> getChannelType();

	public InitialPackType newCommunication();

	public EmployeeHandleResult processChannel(Employee employee, CallCenterEvent callCenterEvent);

	/* [instance] getter/setter */

}
