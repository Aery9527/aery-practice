package org.aery.practice.pcp.impl.people;

import org.aery.practice.pcp.api.center.CallCenter;
import org.aery.practice.pcp.api.channel.PhoneChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Customer extends People {

	/* [static] field */

	/* [static] */

	/* [static] method */

	/* [instance] field */

	private final Logger logger = LoggerFactory.getLogger(getClass()); // slf4j

	@Autowired
	private CallCenter callCenter;

	/* [instance] constructor */
	
	public Customer() {
//		refreshReceiveChannelWaitingMs(300 * 1000);
//		refreshContactTimesToFinish(100);
	}

	/* [instance] method */

	public void makePhoneCall() {
		this.logger.info("Customer named \"" + super.getName() + "\" is joined the call ");

		this.logger.info(sayWithName("make phone call."));
		PhoneChannel phoneChannel = this.callCenter.makePhoneCall();

		simulateCommunication(phoneChannel, (receivedMsg, contactTimes) -> { // receiveAction, 這邊模擬從電話聽到的內容
			this.logger.info(sayWithName("receive " + receivedMsg));
			return Continuable.CONTINUE_CHANNEL;

		}, (contactTimes) -> { // sendAction, 這邊模擬對電話說的內容
			String sendMsg = String.valueOf(contactTimes);
			this.logger.info(sayWithName("send " + sendMsg));
			return sendMsg;

		}, () -> { // beforeBreakLoopAction, 這邊模擬最後準備掛電話時的動作
			this.logger.info(sayWithName("finished the call."));
			return Closeable.FINISH_CHANNEL;
		});
	}

	/* [instance] getter/setter */

}
