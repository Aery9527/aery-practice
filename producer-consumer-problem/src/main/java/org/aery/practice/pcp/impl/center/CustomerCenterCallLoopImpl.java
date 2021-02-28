package org.aery.practice.pcp.impl.center;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.aery.practice.pcp.api.center.CustomerCenter;
import org.aery.practice.pcp.constant.PropertiesEL;
import org.aery.practice.pcp.impl.people.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomerCenterCallLoopImpl implements CustomerCenter {

	private final Logger logger = LoggerFactory.getLogger(getClass()); // slf4j

	@Autowired
	private ConfigurableListableBeanFactory beanFactory;

	@Value(PropertiesEL.Customer.COUNT)
	private int customerCount;

	private ExecutorService threadPool;

	@PostConstruct
	public void initial() {
		initialThreadPool();
		initialCustomerWorking();
	}

	@PreDestroy
	public void destory() {
		this.logger.info("shutdown customer ThreadPool.");

		this.threadPool.shutdown();
	}

	public void initialThreadPool() {
		this.logger.info("start customer ThreadPool.");

		AtomicInteger threadCount = new AtomicInteger(1);
		this.threadPool = Executors.newFixedThreadPool(this.customerCount, (runnable) -> {
			String name = "customer" + String.valueOf(threadCount.incrementAndGet());
			Thread thread = new Thread(runnable, name);
			thread.setDaemon(true);
			return thread;
		});
	}

	public void initialCustomerWorking() {
		for (int i = 1; i <= this.customerCount; i++) {
			callPhone();
		}
	}

	public void customerMakeAPhoneCall() {
		Customer customer = new Customer();
		this.beanFactory.autowireBean(customer);
		customer.makePhoneCall();

		if (!threadPool.isShutdown()) {
			callPhone(); // call loop
		}
	}

	private void callPhone() {
		this.threadPool.execute(() -> {
			try {
				customerMakeAPhoneCall();
			} catch (Throwable t) {
				this.logger.error("", t);
			}
		});
	}

}
