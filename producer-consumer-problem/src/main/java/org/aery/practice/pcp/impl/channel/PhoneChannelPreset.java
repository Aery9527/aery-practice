package org.aery.practice.pcp.impl.channel;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;
import java.util.function.LongFunction;
import java.util.function.Supplier;

import org.aery.practice.pcp.api.channel.PhoneChannel;
import org.aery.practice.pcp.error.CommunicationFinishedException;
import org.aery.practice.pcp.error.PhoneChannelSetupException;

public class PhoneChannelPreset extends CommunicationChannelAbstract implements PhoneChannel {

	/* [static] field */

	/* [static] */

	/* [static] method */

	/* [instance] field */

	private final AtomicBoolean finishFlag;

	/** 當操作{@link #outputBuffer}的IO時一定包在此物件的鎖內, 會額外抽一個物件用來鎖outputBuffer操作是因為code在追的時候比較容易 */
	private final Object outputUseControl = new Object();

	/** 用以模擬訊息傳輸的buffer */
	private final StringBuffer outputBuffer = new StringBuffer();

	/** 模擬電話的另外一端 */
	private LongFunction<String> connectedPhoneMsgProvider;

	/* [instance] constructor */

	public PhoneChannelPreset(String channelId, AtomicBoolean finishFlag) {
		super(channelId);
		this.finishFlag = finishFlag;
	}

	/* [instance] method */

	@Override
	public boolean isFinish() {
		return this.finishFlag.get();
	}

	@Override
	public String receive(long waitingMs) throws CommunicationFinishedException {
		throwIfFinished();
		return this.connectedPhoneMsgProvider.apply(waitingMs);
	}

	@Override
	public void send(String msg) throws CommunicationFinishedException {
		synchronized (this.outputUseControl) {
			throwIfFinished();
			this.outputBuffer.append(msg);
			this.outputUseControl.notify();
		}
	}

	@Override
	public String provideMessage(long waitingMs) throws CommunicationFinishedException {
		BooleanSupplier checkOutputHasMsgAction = () -> {
			boolean haveMsg = this.outputBuffer.length() != 0;
			return haveMsg;
		};

		Supplier<String> fetchOutputMsgAction = () -> {
			String msg = this.outputBuffer.toString();
			this.outputBuffer.delete(0, this.outputBuffer.length());
			return msg;
		};

		Runnable waitAction = () -> {
			boolean immediately = waitingMs <= 0;
			if (immediately) {
				return;
			}

			try {
				this.outputUseControl.wait(waitingMs);
			} catch (InterruptedException e) {
			}
		};

		synchronized (this.outputUseControl) {
			throwIfFinished();

			boolean hasMsg = checkOutputHasMsgAction.getAsBoolean();
			if (hasMsg) {
				return fetchOutputMsgAction.get();
			}

			waitAction.run();
			throwIfFinished();

			hasMsg = checkOutputHasMsgAction.getAsBoolean();
			if (hasMsg) {
				return fetchOutputMsgAction.get();
			} else {
				return null;
			}
		}
	}

	@Override
	public void finish() {
		this.finishFlag.set(true);

		synchronized (this.outputUseControl) {
			this.outputUseControl.notifyAll();
		}
	}

	private void throwIfFinished() throws CommunicationFinishedException {
		if (this.finishFlag.get()) {
			throw new CommunicationFinishedException();
		}
	}

	/* [instance] getter/setter */

	public void setConnectedPhoneMsgProvider(LongFunction<String> connectedPhoneMsgProvider)
			throws PhoneChannelSetupException {
		if (this.connectedPhoneMsgProvider == null) {
			this.connectedPhoneMsgProvider = connectedPhoneMsgProvider;
		} else {
			throw new PhoneChannelSetupException("can't setup twice of \"connectedPhoneMsgProvider\"");
		}
	}

}
