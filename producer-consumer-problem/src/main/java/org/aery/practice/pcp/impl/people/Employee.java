package org.aery.practice.pcp.impl.people;

import java.util.Queue;
import java.util.function.Consumer;

import org.aery.practice.pcp.api.center.ChannelProcessorCamp;
import org.aery.practice.pcp.api.center.queue.CallCenterEventQueue;
import org.aery.practice.pcp.api.center.queue.vo.CallCenterEvent;
import org.aery.practice.pcp.api.channel.CommunicationChannel;
import org.aery.practice.pcp.api.channel.enums.EmployeeHandleResult;
import org.aery.practice.pcp.api.channel.processor.ChannelProcessor;
import org.aery.practice.pcp.error.ChannelProcessorException;
import org.aery.practice.pcp.error.CommunicationFinishedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Employee extends People {

	/* [static] field */

	/* [static] */

	/* [static] method */

	/* [instance] field */

	private final Logger logger = LoggerFactory.getLogger(getClass()); // slf4j

	private final String title;

	private final int level;

	/** 所屬的queue, 當處理完成後將返回該queue, 成為free的員工 */
	private final Queue<Employee> belongedQueue;

	/** 用來運算此empty是否可以處理event的因子, {@link Employee#LEVEL_FACTOR_FLOOR} ~ {@link Employee#LEVEL_FACTOR_CEILING} */
	private final int levelFactor;

	@Autowired
	private CallCenterEventQueue callCenterEventQueue;

	@Autowired
	private ChannelProcessorCamp channelProcessorCamp;

	/* [instance] constructor */

	public Employee(String title, int level, int levelFactor, Queue<Employee> belongedQueue) {
		this.title = title;
		this.level = level;
		this.levelFactor = levelFactor;

		this.belongedQueue = belongedQueue;

		setNotListenChannelFirstTimesWhenDialogStart(true);

		this.logger.info("Employee named \"" + super.getName() + "(" + this.title + ")(LevelFactor:" + this.levelFactor
				+ ")\" is ready for answer the call.");
	}

	/* [instance] method */

	@Override
	public String toString() {
		return super.toString() + "(" + title + ")(LevelFactor:" + this.levelFactor + ")";
	}

	public void processEvent(CallCenterEvent callCenterEvent) {
		CommunicationChannel channel = callCenterEvent.getChannel();
		Class<? extends CommunicationChannel> channelType = channel.getChannelType();

		ChannelProcessor<?, ?> channelProcessor = this.channelProcessorCamp.getChannelProcessor(channelType);
		boolean cannottHandleThisChannel = channelProcessor == null;
		if (cannottHandleThisChannel) {
			throw new ChannelProcessorException("can't handle " + channelProcessor.getClass().toString());
		} else {
			this.logger.info(sayWithName("start process " + channel));
			processChannel(channelProcessor, callCenterEvent);
			this.logger.info(sayWithName("end process " + channel));
		}
	}

	public void processChannel(ChannelProcessor<?, ?> channelProcessor, CallCenterEvent callCenterEvent) {
		CommunicationChannel channel = callCenterEvent.getChannel();

		Consumer<Runnable> handleCommunicationFinishedExceptionAction = (runnable) -> {
			try {
				runnable.run();
			} catch (CommunicationFinishedException e) {
				this.logger.info(sayWithName("channel has been close of " + channel));
			}
		};

		Consumer<String> sendMsgAction = (msg) -> {
			this.logger.info(sayWithName("send msg : " + msg));
			channel.send(msg);
		};

		Runnable receiveMsgAction = () -> {
			String receiveMsg = channel.receiveImmediately();
			this.logger.info(sayWithName("receive msg : " + receiveMsg));
		};

		EmployeeHandleResult employeeHandleResult = channelProcessor.processChannel(this, callCenterEvent);
		if (employeeHandleResult.isSuccess()) {
			handleCommunicationFinishedExceptionAction.accept(() -> {
				sendMsgAction.accept("My Name is " + getName() + ", glad to serve you, good bye.");
				receiveMsgAction.run();
				channel.finish();
			});
		} else {
			handleCommunicationFinishedExceptionAction.accept(() -> {
				boolean canToNextLevel = callCenterEvent.toNextLevel();
				if (canToNextLevel) {
					sendMsgAction.accept(
							"My name is " + getName() + ", I can't help you and I will forward you to next level ("
									+ this.level + " > " + callCenterEvent.getProcessingLevel() + ").");
					this.callCenterEventQueue.put(callCenterEvent); // 放回event queue
				} else { // 這個理論上不應該發生才是
					sendMsgAction.accept("My name is " + getName() + ", there is no more level for service, good bye.");
					channel.finish();
				}
			});
		}

		this.belongedQueue.add(this); // 將自己放回queue, 表示自己任務已經處理完了, 可以準備接下一通電話
	}

	@Override
	public String sayWithName(String msg) {
		return getClass().getSimpleName() + "(" + super.getName() + ")(" + this.title + ") " + msg;
	}

	/* [instance] getter/setter */

	public String getTitle() {
		return title;
	}

	public int getLevel() {
		return level;
	}

	public int getLevelFactor() {
		return levelFactor;
	}

}
