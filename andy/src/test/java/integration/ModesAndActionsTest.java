package integration;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static integration.BaseMetaTestsTest.failedMetaTest;
import static integration.CodeChecksTest.codeCheck;
import static integration.CodeChecksTest.requiredCodeCheck;
import static org.assertj.core.api.Assertions.assertThat;

public class ModesAndActionsTest extends IntegrationTestBase {

    @Test
    void practiceModeRunsEverything() {
        Result result = run(Action.FULL_WITH_HINTS, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecks");

        assertThat(result.getTests().getTestsSucceeded()).isEqualTo(2);
        assertThat(result.getCoverage().getCoveredLines()).isEqualTo(11);
        assertThat(result.getMutationTesting().getKilledMutants()).isEqualTo(8);
        assertThat(result.getMutationTesting().getTotalNumberOfMutants()).isEqualTo(9);
        assertThat(result.getCodeChecks().getNumberOfPassedChecks()).isEqualTo(3);
        assertThat(result.getCodeChecks().getTotalNumberOfChecks()).isEqualTo(3);
        assertThat(result)
                .has(codeCheck("Trip Repository should be mocked", true, 1))
                .has(codeCheck("Trip should not be mocked", true, 1))
                .has(codeCheck("getTripById should be set up", true, 1));
        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(4);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(3);
        assertThat(result.getMetaTests()).has(failedMetaTest("DoesNotCheckInvalidTripId"));
        assertThat(result.getFinalGrade()).isEqualTo(91);
    }

    @Test
    void noRequiredChecksDefined() {
        Result result = run(Action.FULL_WITH_HINTS, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecksWithoutRequiredChecks");

        assertThat(result.getTests().getTestsSucceeded()).isEqualTo(2);
        assertThat(result.getCoverage().getCoveredLines()).isEqualTo(11);
        assertThat(result.getMutationTesting().getKilledMutants()).isEqualTo(8);
        assertThat(result.getMutationTesting().getTotalNumberOfMutants()).isEqualTo(9);
        assertThat(result.getCodeChecks().getNumberOfPassedChecks()).isEqualTo(3);
        assertThat(result.getCodeChecks().getTotalNumberOfChecks()).isEqualTo(3);
        assertThat(result.getRequiredCodeChecks().hasChecks()).isFalse();
        assertThat(result.getRequiredCodeChecks().wasExecuted()).isTrue();
        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(4);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(3);
        assertThat(result.getMetaTests()).has(failedMetaTest("DoesNotCheckInvalidTripId"));
        assertThat(result.getFinalGrade()).isEqualTo(91);
    }

    @Test
    void failingRequiredCodeChecks() {
        Result result = run(Action.FULL_WITH_HINTS, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecksRequiredCodeCheckFailing");

        assertThat(result.getTests().getTestsSucceeded()).isEqualTo(2);
        assertThat(result.getCoverage().getCoveredLines()).isEqualTo(11);
        assertThat(result.getMutationTesting().getKilledMutants()).isEqualTo(8);
        assertThat(result.getMutationTesting().getTotalNumberOfMutants()).isEqualTo(9);
        assertThat(result.getCodeChecks().wasExecuted()).isFalse();
        assertThat(result.getMetaTests().wasExecuted()).isFalse();
        assertThat(result)
                .has(requiredCodeCheck("Trip Repository should be mocked required", true, 1))
                .has(requiredCodeCheck("Trip should be mocked required", false, 1))
                .has(requiredCodeCheck("getTripById should be set up required", true, 1));
        assertThat(result.getRequiredCodeChecks().allChecksPass()).isFalse();
        assertThat(result.getFinalGrade()).isEqualTo(0);
    }

    @Test
    void runOnlyTests() {
        Result result = run(Action.TESTS, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecks");

        assertThat(result.getTests().wasExecuted()).isTrue();
        assertThat(result.getTests().getTestsSucceeded()).isEqualTo(2);

        assertThat(result.getCoverage().wasExecuted()).isTrue(); // Since 2023, tests also run coverage
        assertThat(result.getMetaTests().wasExecuted()).isFalse();
        assertThat(result.getMutationTesting().wasExecuted()).isFalse();
        assertThat(result.getCodeChecks().wasExecuted()).isFalse();
        assertThat(result.getRequiredCodeChecks().wasExecuted()).isFalse();

        assertThat(result.getFinalGrade()).isEqualTo(0);
    }

    @Test
    void runOnlyTestsAndCoverageToolsDuringExam() {
        Result result = run(Action.FULL_WITH_HINTS, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecksExam");

        assertThat(result.getTests().wasExecuted()).isTrue();
        assertThat(result.getTests().getTestsSucceeded()).isEqualTo(2);

        assertThat(result.getCoverage().wasExecuted()).isTrue();
        assertThat(result.getCoverage().getCoveredLines()).isEqualTo(11);

        assertThat(result.getMutationTesting().wasExecuted()).isTrue();
        assertThat(result.getMutationTesting().getKilledMutants()).isEqualTo(8);
        assertThat(result.getMutationTesting().getTotalNumberOfMutants()).isEqualTo(9);

        assertThat(result.getMetaTests().wasExecuted()).isFalse();
        assertThat(result.getCodeChecks().wasExecuted()).isFalse();
        assertThat(result.getRequiredCodeChecks().wasExecuted()).isFalse();

        assertThat(result.getFinalGrade()).isEqualTo(0);
    }

    @ParameterizedTest
    @CsvSource({"TESTS", "COVERAGE", "FULL_WITHOUT_HINTS"})
    void gradingModeShouldRunEverything(Action action) {
        Result result = run(action, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecksGrading");

        assertThat(result.getTests().getTestsSucceeded()).isEqualTo(2);
        assertThat(result.getCoverage().getCoveredLines()).isEqualTo(11);
        assertThat(result.getMutationTesting().getKilledMutants()).isEqualTo(8);
        assertThat(result.getMutationTesting().getTotalNumberOfMutants()).isEqualTo(9);
        assertThat(result.getCodeChecks().getNumberOfPassedChecks()).isEqualTo(3);
        assertThat(result.getCodeChecks().getTotalNumberOfChecks()).isEqualTo(3);
        assertThat(result)
                .has(codeCheck("Trip Repository should be mocked", true, 1))
                .has(codeCheck("Trip should not be mocked", true, 1))
                .has(codeCheck("getTripById should be set up", true, 1))
                .has(requiredCodeCheck("Trip Repository should be mocked required", true, 1))
                .has(requiredCodeCheck("getTripById should be set up required", true, 1));
        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(4);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(3);
        assertThat(result.getMetaTests()).has(failedMetaTest("DoesNotCheckInvalidTripId"));
        assertThat(result.getFinalGrade()).isEqualTo(91);
    }

}
