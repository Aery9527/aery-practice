package org.aery.practice.pcp.test.center;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aery.practice.pcp.component.CallCenterEventSelector;
import org.aery.practice.pcp.impl.center.CustomerCenterCallLoopImpl;
import org.aery.practice.pcp.impl.center.EmployeeCenterPreset;
import org.aery.practice.pcp.impl.center.EmployeeCenterPreset.EmployeeLevelInfo;
import org.aery.practice.pcp.impl.center.EmployeeLevelCalculatorPreset;
import org.aery.practice.pcp.impl.people.Employee;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeCenterPresetTest {

    @Autowired
    private EmployeeCenterPreset employeeCenter;

    @MockBean // @PostConstruct會fork thread做事影響其他bean資料, 因此mock以阻止其thread start的行為
    private CustomerCenterCallLoopImpl customerCenter;

    @MockBean // @PostConstruct會fork thread做事影響其他bean資料, 因此mock以阻止其thread start的行為
    private CallCenterEventSelector callCenterEventSelector;

    @Test
    public void test_getFreeEmployeeByLevel() {
        List<EmployeeLevelInfo> levels = mockData_levels();
        Map<Integer, Integer> levelFactorMap = mockData_levelFactorMap(levels.size());

        this.employeeCenter.setLevels(levels);
        this.employeeCenter.initial();

        SoftAssertions softAssertions = new SoftAssertions();

        for (int level = 0; level < levels.size(); level++) {
            EmployeeLevelInfo info = levels.get(level);
            String name = info.getName();
            int count = info.getCount();

            for (int employeeCount = 0; employeeCount <= count; employeeCount++) {
                Employee employee = this.employeeCenter.getFreeEmployeeByLevel(level);

                boolean isFinal = employeeCount == count;
                if (isFinal) {
                    softAssertions.assertThat(employee).isNull();
                } else {
                    softAssertions.assertThat(employee).isNotNull();
                    softAssertions.assertThat(employee.getTitle()).isEqualTo(name);
                    softAssertions.assertThat(employee.getLevel()).isEqualTo(level);
                    softAssertions.assertThat(employee.getLevelFactor()).isEqualTo(levelFactorMap.get(level));
                }
            }
        }

        softAssertions.assertAll();
    }

    private List<EmployeeLevelInfo> mockData_levels() {
        List<EmployeeLevelInfo> mockData = new ArrayList<>();

        mockData.add(new EmployeeLevelInfo("A", 1));
        mockData.add(new EmployeeLevelInfo("B", 2));
        mockData.add(new EmployeeLevelInfo("C", 1));
        mockData.add(new EmployeeLevelInfo("D", 3));
        mockData.add(new EmployeeLevelInfo("E", 5));

        return mockData;
    }

    private Map<Integer, Integer> mockData_levelFactorMap(int levelCount) {
        int piece = EmployeeLevelCalculatorPreset.LEVEL_FACTOR_CEILING / levelCount;

        Map<Integer, Integer> levelFactorMap = new HashMap<>();
        for (int index = 0; index < levelCount; index++) {
            int currentLevel = levelCount - 1 - index;

            boolean isHighestLevel = currentLevel == 0;
            int levelFactor = isHighestLevel ? 100 : (piece * (levelCount - currentLevel));
            levelFactorMap.put(currentLevel, levelFactor);
        }

        return levelFactorMap;
    }

}
