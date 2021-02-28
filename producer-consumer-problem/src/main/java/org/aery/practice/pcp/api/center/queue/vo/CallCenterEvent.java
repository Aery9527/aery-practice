package org.aery.practice.pcp.api.center.queue.vo;

import java.util.concurrent.atomic.AtomicInteger;

import org.aery.practice.pcp.api.channel.CommunicationChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallCenterEvent {

	/* [static] field */

	/* [static] */

	/* [static] method */

	/* [instance] field */

	private final Logger logger = LoggerFactory.getLogger(getClass()); // slf4j

	private final CommunicationChannel channel;

	/** 現在應該由哪個level的employee處理這個事件 */
	private final AtomicInteger processingLevelAI;

	/* [instance] constructor */

	public CallCenterEvent(CommunicationChannel channel, int processLevel) {
		this.channel = channel;
		this.processingLevelAI = new AtomicInteger(processLevel);
	}

	/* [instance] method */

	public <ChannelType> ChannelType castChannel(Class<ChannelType> type) {
		return type.cast(this.channel);
	}

	public boolean toNextLevel() {
		int processingLevel = this.processingLevelAI.get();
		if (processingLevel == 0) {
			return false;
		} else if (processingLevel < 0) {
			this.logger.error("processingLevel(" + processingLevel + ") Below 0, reset to 0.");
			this.processingLevelAI.set(0);
			return false;
		} else {
			this.processingLevelAI.decrementAndGet();
			return true;
		}
	}

	/* [instance] getter/setter */

	public int getProcessingLevel() {
		return this.processingLevelAI.get();
	}

	public CommunicationChannel getChannel() {
		return channel;
	}

}
