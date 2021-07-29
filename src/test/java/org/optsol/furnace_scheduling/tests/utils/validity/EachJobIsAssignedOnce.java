/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.tests.utils.validity;

import lombok.extern.slf4j.Slf4j;
import org.optsol.furnace_scheduling.model.constants.IFurnaceScheduling_Constants;
import org.optsol.furnace_scheduling.solver.IFurnaceScheduling_Solution;

@Slf4j
public class EachJobIsAssignedOnce implements IValidityChecks {
  @Override
  public boolean checkValid(
      IFurnaceScheduling_Constants constants,
      IFurnaceScheduling_Solution solution) {

    boolean valid = true;

    Boolean[][][] x = solution.get_x();

    for (int j : constants.get_J()) {
      int sumAssignmentsOfJ = 0;
      for (int i : constants.get_J0()) {
        for (int k : constants.get_M()) {
          try {
            if (x[i][j][k]) {
              sumAssignmentsOfJ++;
            }
          } catch (NullPointerException npe) {
            // x does not exist: go on...
          }
        }
      }
      if (sumAssignmentsOfJ != 1) {
        valid = false;
        log.warn("Job j=" + j + " is assigned " + sumAssignmentsOfJ + "x");
      }
    }

    return valid;
  }
}
