package org.aery.practice.pcp.api.channel.processor.vo;

import org.aery.practice.pcp.api.channel.CommunicationChannel;

public class ChannelInitialPack<ChannelType extends CommunicationChannel> {

	/* [static] field */

	/* [static] */

	/* [static] method */

	/* [instance] field */

	private ChannelType forCustomeChannel;

	private ChannelType forEmployeeChannel;

	/* [instance] constructor */

	public ChannelInitialPack(ChannelType forCustomeChannel, ChannelType forEmployeeChannel) {
		this.forCustomeChannel = forCustomeChannel;
		this.forEmployeeChannel = forEmployeeChannel;
	}

	/* [instance] method */

	/* [instance] getter/setter */

	public ChannelType getForCustomeChannel() {
		return forCustomeChannel;
	}

	public void setForCustomeChannel(ChannelType forCustomeChannel) {
		this.forCustomeChannel = forCustomeChannel;
	}

	public ChannelType getForEmployeeChannel() {
		return forEmployeeChannel;
	}

	public void setForEmployeeChannel(ChannelType forEmployeeChannel) {
		this.forEmployeeChannel = forEmployeeChannel;
	}

}
