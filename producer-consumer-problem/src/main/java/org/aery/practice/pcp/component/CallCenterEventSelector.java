package org.aery.practice.pcp.component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.aery.practice.pcp.api.center.EmployeeCenter;
import org.aery.practice.pcp.api.center.queue.CallCenterEventQueue;
import org.aery.practice.pcp.api.center.queue.vo.CallCenterEvent;
import org.aery.practice.pcp.impl.people.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CallCenterEventSelector {

	/* [static] field */

	/* [static] */

	/* [static] method */

	/* [instance] field */

	private final Logger logger = LoggerFactory.getLogger(getClass()); // slf4j

	@Autowired
	private EmployeeCenter employeeCenter;

	@Autowired
	private CallCenterEventQueue callCenterEventQueue;

	private boolean end = false;

	private Thread loopThread;

	private ExecutorService threadPool;

	/* [instance] constructor */

	/* [instance] method */

	@PostConstruct
	public void initial() {
		this.loopThread = new Thread(this::loop, "EventSelector");
		this.loopThread.setDaemon(true);
		this.loopThread.start();

		this.threadPool = initialThreadPool();
	}

	@PreDestroy
	public void destory() {
		this.end = true;
		this.threadPool.shutdown();
	}

	private void loop() {
		while (!this.end) {
			try {
				selectFromCallCenterEventQueue();
			} catch (InterruptedException e) {
			} catch (Throwable t) {
				this.logger.error("", t);
			}
		}
	}

	private void selectFromCallCenterEventQueue() throws InterruptedException {
		CallCenterEvent callCenterEvent = this.callCenterEventQueue.get(3, TimeUnit.SECONDS);
		if (callCenterEvent == null) {
			return;
		}

		if (this.end) {
			return;
		}

		int processingLevel = callCenterEvent.getProcessingLevel();

		Employee employee = this.employeeCenter.getFreeEmployeeByLevel(processingLevel);

		boolean noFreeEmployeeOfThisLevel = employee == null;
		if (noFreeEmployeeOfThisLevel) {
			callCenterEvent.toNextLevel(); // 給下一個level的人處理
			this.callCenterEventQueue.put(callCenterEvent); // 將未處理的事件放回queue

		} else {
			this.threadPool.execute(() -> {
				try {
					employee.processEvent(callCenterEvent);
				} catch (Throwable t) {
					this.logger.error("employee process error of " + callCenterEvent, t);
				}
			});
		}
	}

	public ExecutorService initialThreadPool() {
		int corePoolSize = 0;
		int maximumPoolSize = 1024;
		long keepAliveTime = 60;
		TimeUnit unit = TimeUnit.SECONDS;
		BlockingQueue<Runnable> workQueue = new SynchronousQueue<>(); // 不累積
		RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy(); // 當thread pool滿的時候可以由此控制錯誤

		AtomicInteger threadCount = new AtomicInteger(1);
		ThreadFactory threadFactory = (runnable) -> {
			String name = "CallCenter" + String.valueOf(threadCount.incrementAndGet());
			Thread thread = new Thread(runnable, name);
			thread.setDaemon(true);
			return thread;
		};

		return new ThreadPoolExecutor( //
				corePoolSize, //
				maximumPoolSize, //
				keepAliveTime, //
				unit, //
				workQueue, //
				threadFactory, //
				handler //
		);
	}

	/* [instance] getter/setter */

}
