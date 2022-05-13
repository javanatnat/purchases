package ru.javanatnat.purchases.statistics;

import org.junit.jupiter.api.Test;
import ru.javanatnat.purchases.request.ReadJsonDataException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StatReaderTest {
    private static final StatReaderImpl reader = new StatReaderImpl();
    private static final String CORRECT_STAT_JSON = "src/test/resources/data/testStat.json";
    private static final String INCORRECT_STAT_JSON = "src/test/resources/data/testStatError.json";

    @Test
    public void parseCorrectFileTest() {
        StatisticIntervalCriteriaImpl stat = reader.read(CORRECT_STAT_JSON);
        assertThat(stat).isNotNull();
        assertThat(stat.getCountIntervalDays()).isEqualTo(2);
        assertThat(stat.getStartDate()).isEqualTo("2020-01-14");
        assertThat(stat.getEndDate()).isEqualTo("2020-01-15");
    }

    @Test
    public void parseIncorrectFileTest() {
        assertThatThrownBy(() -> reader.read(INCORRECT_STAT_JSON))
                .isExactlyInstanceOf(ReadJsonDataException.class);
    }
}
