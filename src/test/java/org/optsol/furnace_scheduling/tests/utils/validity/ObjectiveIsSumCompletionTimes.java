/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.tests.utils.validity;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.optsol.furnace_scheduling.model.constants.IFurnaceScheduling_Constants;
import org.optsol.furnace_scheduling.solver.IFurnaceScheduling_Solution;

@Slf4j
public class ObjectiveIsSumCompletionTimes implements IValidityChecks {

  @Override
  public boolean checkValid(
      IFurnaceScheduling_Constants constants,
      IFurnaceScheduling_Solution solution) {

    double doubleSumCompletionTimes = Arrays.stream(solution.get_C()).mapToDouble(c -> c).sum();

    boolean valid =
        Math.abs(
            doubleSumCompletionTimes -
                solution.getObjectiveValue()) < EPSILON;

    if (!valid) {
      log.warn("objective: " + solution.getObjectiveValue() + " / sumCompletionTimes: " +
          doubleSumCompletionTimes);
    }

    return valid;
  }
}
