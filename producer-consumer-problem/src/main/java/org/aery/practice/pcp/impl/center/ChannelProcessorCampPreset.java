package org.aery.practice.pcp.impl.center;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.aery.practice.pcp.api.center.ChannelProcessorCamp;
import org.aery.practice.pcp.api.channel.CommunicationChannel;
import org.aery.practice.pcp.api.channel.processor.ChannelProcessor;
import org.aery.practice.pcp.error.ChannelProcessorDuplicateException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChannelProcessorCampPreset implements ChannelProcessorCamp {

	/* [static] field */

	/* [static] */

	/* [static] method */

	/* [instance] field */

	/** 存放要處理{@link CommunicationChannel}的map, 利用Class做取用 */
	private final Map<Class<?>, ChannelProcessor<?, ?>> channelProcessMap = new HashMap<>();

	@Autowired
	private ObjectProvider<ChannelProcessor<?, ?>> channelProcessors;

	/* [instance] constructor */

	/* [instance] method */

	@PostConstruct
	public void initial() {
		this.channelProcessors.forEach((channelProcessor) -> {
			Class<?> channelType = channelProcessor.getChannelType();
			ChannelProcessor<?, ?> lastChannelProcessor = this.channelProcessMap.put(channelType, channelProcessor);

			boolean isChannelProcessorDuplicate = lastChannelProcessor != null;
			if (isChannelProcessorDuplicate) {
				throw new ChannelProcessorDuplicateException("handle channelType(" + channelType + ") is duplicate of ("
						+ channelProcessor.getClass().getName() + ") and (" + lastChannelProcessor.getClass().getName()
						+ ")");
			}
		});
	}

	@Override
	public ChannelProcessor<?, ?> getChannelProcessor(Class<?> channelType) {
		ChannelProcessor<?, ?> channel = this.channelProcessMap.get(channelType);
		return channel;
	}

	/* [instance] getter/setter */

}
