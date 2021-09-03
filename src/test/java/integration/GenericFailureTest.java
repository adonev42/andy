package integration;

import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class GenericFailureTest extends IntegrationTestBase {

    @Override
    protected void addSteps(ExecutionFlow flow) {
        ExecutionStep badStep = mock(ExecutionStep.class);

        doThrow(new RuntimeException("Some super error here"))
                .when(badStep).execute(any(Context.class), any(ResultBuilder.class));

        flow.addSteps(Arrays.asList(badStep));
    }

    @Test
    void genericFailureHasPrecedence() {
        Result result = run(Action.TESTS, "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass");

        assertThat(result.hasGenericFailure()).isTrue();
        assertThat(result.getGenericFailure())
                .isNotEmpty()
                .contains("Some super error here");
    }

}
