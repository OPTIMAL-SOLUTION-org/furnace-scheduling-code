/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.tests.utils.validity;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.optsol.furnace_scheduling.model.constants.IFurnaceScheduling_Constants;
import org.optsol.furnace_scheduling.solver.IFurnaceScheduling_Solution;

@Slf4j
public class SolutionValidityCheck implements IValidityChecks {

  private final static List<IValidityChecks> checks =
      List.of(
          new EachJobIsAssignedOnce(),
          new ObjectiveIsSumCompletionTimes(),
          new JobAssignedToAllowedMachine(),
          new JobSequencesOnEachMachine(),
          new Conflicts()
      );

  @Override
  public boolean checkValid(
      IFurnaceScheduling_Constants constants,
      IFurnaceScheduling_Solution solution) {

    boolean valid = true;

    StringBuilder stringBuilder = new StringBuilder().append(System.getProperty("line.separator"));
    for (IValidityChecks check : checks) {
      if (check.checkValid(constants, solution)) {
        stringBuilder.append(check.getClass().getSimpleName())
            .append(": valid!")
            .append(System.getProperty("line.separator"));
      } else {
        stringBuilder.append(check.getClass().getSimpleName() + ": INVALID!")
            .append(System.getProperty("line.separator"));
        valid = false;
      }
    }

    if (valid) {
      log.info(stringBuilder.toString());
    } else {
      log.warn(stringBuilder.toString());
    }

    return valid;
  }
}
