package org.aery.practice.pcp.impl.center;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.BiFunction;

import javax.annotation.PostConstruct;

import org.aery.practice.pcp.api.center.EmployeeCenter;
import org.aery.practice.pcp.api.center.EmployeeLevelCalculator;
import org.aery.practice.pcp.api.center.queue.CallCenterEventQueue;
import org.aery.practice.pcp.api.center.queue.vo.CallCenterEvent;
import org.aery.practice.pcp.api.channel.CommunicationChannel;
import org.aery.practice.pcp.constant.PropertiesKeys;
import org.aery.practice.pcp.impl.people.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = PropertiesKeys.PropertiesAutoBind.EMPLOYEE)
@Component
public class EmployeeCenterPreset implements EmployeeCenter {

	/* [static] field */

	/* [static] */

	public static final class EmployeeLevelInfo {
		private String name;

		private int count;

		public EmployeeLevelInfo() {

		}

		public EmployeeLevelInfo(String name, int count) {
			this.name = name;
			this.count = count;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}
	}

	/* [static] method */

	/* [instance] field */

	private final Logger logger = LoggerFactory.getLogger(getClass()); // slf4j

	@Autowired
	private ConfigurableListableBeanFactory beanFactory;

	@Autowired
	private CallCenterEventQueue callCenterEventQueue;

	@Autowired
	private EmployeeLevelCalculator employeeLevelCalculator;

	/** auto bind by {@link ConfigurationProperties} */
	private List<EmployeeLevelInfo> levels;

	/** index越小employee的等級越高 */
	private List<Queue<Employee>> employeesOfLevel;

	/* [instance] constructor */

	/* [instance] method */

	@PostConstruct
	public void initial() {
		printEmployeeSetInfo(this.levels);
		this.employeesOfLevel = initialEmployeesOfLevel(this.levels);
	}

	@Override
	public void putChannel(CommunicationChannel channel) {
		int processLevel = this.levels.size() - 1;
		CallCenterEvent callCenterEvent = new CallCenterEvent(channel, processLevel);
		this.callCenterEventQueue.put(callCenterEvent);
	}

	@Override
	public Employee getFreeEmployeeByLevel(int level) {
		int lowestLevel = this.employeesOfLevel.size() - 1;
		if (level < 0) {
			level = 0;
		} else if (level > lowestLevel) {
			level = lowestLevel;
		}

		Queue<Employee> employeeQueue = this.employeesOfLevel.get(level);
		return employeeQueue.poll();
	}

	public void printEmployeeSetInfo(List<EmployeeLevelInfo> levels) {
		for (int index = 0; index < levels.size(); index++) {
			EmployeeLevelInfo employeeLevelInfo = levels.get(index);
			String name = employeeLevelInfo.getName();
			int count = employeeLevelInfo.getCount();
			this.logger.info("employee level " + index + " " + name + "(" + count + ")");
		}
	}

	public List<Queue<Employee>> initialEmployeesOfLevel(List<EmployeeLevelInfo> levels) {
		final int levelCount = levels.size() - 1;
		List<Queue<Employee>> employeesOfLevel = new ArrayList<>(levelCount + 1);

		BiFunction<EmployeeLevelInfo, Integer, Queue<Employee>> employeeQueueCreateAction = (employeeLevelInfo,
				level) -> {
			String name = employeeLevelInfo.getName();
			int count = employeeLevelInfo.getCount();
			int levelFactor = this.employeeLevelCalculator.calculateLevelFactor(levelCount, level);

			Queue<Employee> employeesQueue = new LinkedList<>();
			for (int index = 0; index < count; index++) {
				Employee employee = new Employee(name, level, levelFactor, employeesQueue);
				this.beanFactory.autowireBean(employee);
				employeesQueue.add(employee);
			}

			return employeesQueue;
		};

		for (int level = 0; level < levels.size(); level++) {
			EmployeeLevelInfo employeeLevelInfo = levels.get(level);
			Queue<Employee> employeeQueue = employeeQueueCreateAction.apply(employeeLevelInfo, level);
			employeesOfLevel.add(employeeQueue);
		}
		return employeesOfLevel;
	}

	/* [instance] getter/setter */

	public List<EmployeeLevelInfo> getLevels() {
		return levels;
	}

	public void setLevels(List<EmployeeLevelInfo> levels) {
		this.levels = levels;
	}

}
