/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.tests.utils.validity;

import lombok.extern.slf4j.Slf4j;
import org.optsol.furnace_scheduling.model.constants.IFurnaceScheduling_Constants;
import org.optsol.furnace_scheduling.solver.IFurnaceScheduling_Solution;

@Slf4j
public class JobAssignedToAllowedMachine implements IValidityChecks {
  @Override
  public boolean checkValid(
      IFurnaceScheduling_Constants constants,
      IFurnaceScheduling_Solution solution) {

    boolean valid = true;

    Boolean[][][] x = solution.get_x();

    for (int i : constants.get_J0()) {
      for (int j : constants.get_J()) {
        for (int k : constants.get_M()) {
          try {
            if (x[i][j][k]) {
              if (!constants.get_M_j(i).contains(k) ||
                  !constants.get_M_j(j).contains(k)) {
                valid = false;
                log.warn("Assignment i,j,k (" + i + "," + j + "," + k + ") not allowed!");
              }
            }
          } catch (NullPointerException npe) {
            // x does not exist: go on...
          }
        }
      }
    }

    return valid;
  }
}
