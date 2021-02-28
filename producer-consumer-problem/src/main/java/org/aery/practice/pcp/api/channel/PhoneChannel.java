package org.aery.practice.pcp.api.channel;

import java.util.function.LongFunction;

import org.aery.practice.pcp.error.CommunicationFinishedException;
import org.aery.practice.pcp.error.PhoneChannelSetupException;

public interface PhoneChannel extends CommunicationChannel {

	/* [static] field */

	/* [static] */

	/* [static] method */

	/* [instance] field */

	/* [instance] constructor */

	/* [instance] method */

	@Override
	public default Class<? extends CommunicationChannel> getChannelType() {
		return PhoneChannel.class;
	}

	public String provideMessage(long waitingMs) throws CommunicationFinishedException;

	public void setConnectedPhoneMsgProvider(LongFunction<String> connectedPhoneMsgProvider)
			throws PhoneChannelSetupException;

	/* [instance] getter/setter */

}
