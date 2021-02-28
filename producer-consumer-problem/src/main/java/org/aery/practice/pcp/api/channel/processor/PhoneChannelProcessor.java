package org.aery.practice.pcp.api.channel.processor;

import org.aery.practice.pcp.api.channel.PhoneChannel;
import org.aery.practice.pcp.impl.channel.processor.vo.PhoneChannelInitPack;

public interface PhoneChannelProcessor extends ChannelProcessor<PhoneChannel, PhoneChannelInitPack> {

	/* [static] field */

	/* [static] */

	/* [static] method */

	/* [instance] field */

	/* [instance] constructor */

	/* [instance] method */

	@Override
	public default Class<PhoneChannel> getChannelType() {
		return PhoneChannel.class;
	}

	/* [instance] getter/setter */

}
