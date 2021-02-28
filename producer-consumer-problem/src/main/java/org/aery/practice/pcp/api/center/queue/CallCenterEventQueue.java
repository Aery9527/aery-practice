package org.aery.practice.pcp.api.center.queue;

import java.util.concurrent.TimeUnit;

import org.aery.practice.pcp.api.center.queue.vo.CallCenterEvent;

public interface CallCenterEventQueue {

	/* [static] field */

	/* [static] */

	/* [static] method */

	/* [instance] field */

	/* [instance] constructor */

	/* [instance] method */

	public void put(CallCenterEvent callCenterEvent);

	public CallCenterEvent get(long timeout, TimeUnit unit) throws InterruptedException;

	/* [instance] getter/setter */

}
