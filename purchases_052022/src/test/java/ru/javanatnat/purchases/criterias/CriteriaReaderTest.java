package ru.javanatnat.purchases.criterias;

import org.junit.jupiter.api.Test;
import ru.javanatnat.purchases.request.ReadJsonDataException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CriteriaReaderTest {

    private static final CriteriaReaderImpl reader = new CriteriaReaderImpl();
    private static final String NOT_EXIST_FILE = "notExistFile.json";
    private static final String CORRECT_SEARCH_JSON = "src/test/resources/data/testSearch.json";
    private static final String BREAK_SEARCH_JSON = "src/test/resources/data/testSearchBreak.json";
    private static final String PART_SEARCH_JSON = "src/test/resources/data/testSearchPart.json";

    @Test
    public void notExistFileTest() {
        assertThatThrownBy(() -> reader.read(NOT_EXIST_FILE))
                .isExactlyInstanceOf(ReadJsonDataException.class);
    }

    @Test
    public void parseCorrectFileTest() {
        List<Criteria> criterias = reader.read(CORRECT_SEARCH_JSON);
        assertThat(criterias.size()).isEqualTo(9);
        assertThat(criterias)
                .doesNotContainNull()
                .containsExactly(
                        new LastNameCriteriaImpl("Иванова"),
                        new LastNameCriteriaImpl("И"),
                        new LastNameCriteriaImpl("М"),
                        new ProductMinTimesCriteriaImpl("Чипсы", 3),
                        new ProductMinTimesCriteriaImpl("Сливки", 2),
                        new MinMaxExpensesCriteriaImpl(350, 1000),
                        new MinMaxExpensesCriteriaImpl(0, 350),
                        new BadCustomersCriteriaImpl(3),
                        new BadCustomersCriteriaImpl(1)
                );
    }

    @Test
    public void parseBreakFileTest() {
        assertThatThrownBy(() -> reader.read(BREAK_SEARCH_JSON))
                .isExactlyInstanceOf(ReadJsonDataException.class);
    }

    @Test
    public void parsePartCorrectFileTest() {
        List<Criteria> criterias = reader.read(PART_SEARCH_JSON);
        assertThat(criterias.size()).isEqualTo(2);
        assertThat(criterias)
                .doesNotContainNull()
                .containsExactly(
                        new LastNameCriteriaImpl("И"),
                        new LastNameCriteriaImpl("М")
                );
    }
}
