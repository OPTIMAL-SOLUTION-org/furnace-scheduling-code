/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.solver;

import org.optsol.furnace_scheduling.model.constants.IFurnaceScheduling_Constants;
import org.optsol.jdecor.ortools.AbstractOrtoolsModelFactory;
import org.optsol.jdecor.ortools.OrtoolsSolver;

public class FurnaceSchedulingSolver
    extends OrtoolsSolver<IFurnaceScheduling_Constants, IFurnaceScheduling_Solution> {

  public FurnaceSchedulingSolver(
      AbstractOrtoolsModelFactory<IFurnaceScheduling_Constants> modelFactory) {
    super(
        modelFactory,
        IFurnaceScheduling_Solution.class);
  }

  public FurnaceSchedulingSolver(
      AbstractOrtoolsModelFactory<IFurnaceScheduling_Constants> modelFactory,
      int timelimitSeconds) {
    super(
        timelimitSeconds,
        modelFactory,
        IFurnaceScheduling_Solution.class);
  }
}
