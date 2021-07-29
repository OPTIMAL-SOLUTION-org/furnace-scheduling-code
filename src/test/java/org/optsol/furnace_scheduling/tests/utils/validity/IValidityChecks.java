/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.tests.utils.validity;

import org.optsol.furnace_scheduling.model.constants.IFurnaceScheduling_Constants;
import org.optsol.furnace_scheduling.solver.IFurnaceScheduling_Solution;

public interface IValidityChecks {
  double EPSILON = 0.001;

  boolean checkValid(
      IFurnaceScheduling_Constants constants,
      IFurnaceScheduling_Solution solution);
}
