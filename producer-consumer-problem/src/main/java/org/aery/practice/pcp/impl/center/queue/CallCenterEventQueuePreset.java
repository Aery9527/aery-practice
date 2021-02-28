package org.aery.practice.pcp.impl.center.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.aery.practice.pcp.api.center.queue.CallCenterEventQueue;
import org.aery.practice.pcp.api.center.queue.vo.CallCenterEvent;
import org.springframework.stereotype.Component;

@Component
public class CallCenterEventQueuePreset implements CallCenterEventQueue {

	/* [static] field */

	/* [static] */

	/* [static] method */

	/* [instance] field */

	private final BlockingQueue<CallCenterEvent> queue = new LinkedBlockingQueue<>();

	/* [instance] constructor */

	/* [instance] method */

	@Override
	public void put(CallCenterEvent callCenterEvent) {
		this.queue.add(callCenterEvent);
	}

	@Override
	public CallCenterEvent get(long timeout, TimeUnit unit) throws InterruptedException {
		return this.queue.poll(timeout, unit);
	}

	/* [instance] getter/setter */

}
