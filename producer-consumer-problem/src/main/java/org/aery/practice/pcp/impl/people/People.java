package org.aery.practice.pcp.impl.people;

import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import org.aery.practice.pcp.api.channel.CommunicationChannel;
import org.aery.practice.pcp.error.CommunicationFinishedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class People {

	/* [static] field */

	/* [static] */

	public static enum Continuable {
		FINISH_CHANNEL, CONTINUE_CHANNEL;

		public boolean isFinish() {
			return equals(FINISH_CHANNEL);
		}
	}

	public static enum Closeable {
		FINISH_CHANNEL, KEEP_CHANNEL;

		public boolean isFinish() {
			return equals(FINISH_CHANNEL);
		}
	}

	/* [static] method */

	/* [instance] field */

	private final Logger logger = LoggerFactory.getLogger(getClass()); // slf4j

	private final String name;

	private boolean notListenChannelFirstTimesWhenDialogStart = false;

	/** 模擬{@link CommunicationChannel#receive(long)}等多久之後會下一動 */
	private long receiveChannelWaitingMs;

	/**
	 * 模擬{@link CommunicationChannel#receive(long)}跟{@link CommunicationChannel#send(String)}來回幾次之後會操作{@link CommunicationChannel#finish()}中斷溝通
	 */
	private int contactTimesToFinish;

	/* [instance] constructor */

	public People() {
		int numberBit = 8;
		int nameNumber = (int) ((Math.random() * Math.pow(10, numberBit)));
		this.name = String.format("%0" + numberBit + "d", nameNumber);

		refreshReceiveChannelWaitingMs(3000); // 預設0~3000ms
		refreshContactTimesToFinish(5); // 預設0~5次
	}

	/* [instance] method */

	@Override
	public String toString() {
		return super.getClass().getSimpleName() + "(" + this.name + ")";
	}

	public void simulateCommunication(CommunicationChannel channel,
			BiFunction<String, Integer, Continuable> receiveAction, IntFunction<String> sendAction,
			Supplier<Closeable> beforeBreakLoopAction) {
		this.logger.info(sayWithName("start process " + channel));
		this.logger.info(sayWithName(getInfo()));

		int contactTimes = 0;
		while (true) {
			if (channel.isFinish()) {
				this.logger.info(sayWithName("the call has been hung up."));
				break;
			}

			try {
				String receivedMsg;
				if (contactTimes == 0 && this.notListenChannelFirstTimesWhenDialogStart) {
					receivedMsg = "";
				} else {
					receivedMsg = channel.receive(this.receiveChannelWaitingMs); // will be block
				}

				contactTimes++;

				Continuable continuable = receiveAction.apply(receivedMsg, contactTimes);
				if (continuable == null) {
					continuable = Continuable.CONTINUE_CHANNEL;
				}

				boolean manualOrTimsReachToFinish = continuable.isFinish() || contactTimes > this.contactTimesToFinish;
				if (manualOrTimsReachToFinish) {
					Closeable closeable = beforeBreakLoopAction.get();
					if (closeable == null) {
						closeable = Closeable.FINISH_CHANNEL;
					}
					if (closeable.isFinish()) {
						channel.finish();
					}
					break;
				} else {
					String sendMsg = sendAction.apply(contactTimes);
					channel.send(sendMsg);
				}

			} catch (CommunicationFinishedException e) {
				this.logger.error(sayWithName("phone has been hung up."));
			}
		}

		this.logger.info(sayWithName("stop process " + channel));
	}

	public String sayWithName(String msg) {
		return getClass().getSimpleName() + "(" + this.name + ") " + msg;
	}

	protected void refreshReceiveChannelWaitingMs(long factor) {
		this.receiveChannelWaitingMs = (long) (Math.random() * factor);
//		this.receiveChannelWaitingMs = 300 * 1000;
	}

	protected void refreshContactTimesToFinish(int factor) {
		this.contactTimesToFinish = (int) (Math.random() * (factor + 1));
	}

	public String getInfo() {
		return "waitingMs:" + this.receiveChannelWaitingMs + "ms, contactTimes:" + this.contactTimesToFinish;
	}

	/* [instance] getter/setter */

	public String getName() {
		return name;
	}

	public boolean isNotListenChannelFirstTimesWhenDialogStart() {
		return notListenChannelFirstTimesWhenDialogStart;
	}

	public void setNotListenChannelFirstTimesWhenDialogStart(boolean notListenChannelFirstTimesWhenDialogStart) {
		this.notListenChannelFirstTimesWhenDialogStart = notListenChannelFirstTimesWhenDialogStart;
	}

}
