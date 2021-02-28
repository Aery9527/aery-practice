package org.aery.practice.pcp.impl.channel;

import org.aery.practice.pcp.api.channel.CommunicationChannel;

public abstract class CommunicationChannelAbstract implements CommunicationChannel {

	/* [static] field */

	/* [static] */

	/* [static] method */

	/* [instance] field */

	private final String channelId;

	/* [instance] constructor */

	public CommunicationChannelAbstract(String channelId) {
		this.channelId = channelId;
	}

	/* [instance] method */

	@Override
	public String toString() {
		Class<? extends CommunicationChannel> channelType = getChannelType();
		return channelType.getSimpleName() + "(" + this.channelId + ")";
	}

	/* [instance] getter/setter */

	@Override
	public String getChannelId() {
		return this.channelId;
	}

}
