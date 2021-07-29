/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.model.objective;

import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_J;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_M;
import org.optsol.furnace_scheduling.model.variables.FurnaceSchedulingVars;
import org.optsol.jdecor.core.AbstractVariableManager;
import org.optsol.jdecor.ortools.AbstractOrtoolsObjectiveManager;

public class Objective_a<
    CONST extends IConstants_J & IConstants_M>
    extends AbstractOrtoolsObjectiveManager<CONST> {

  @Override
  protected void configureObjective(
      MPObjective objective,
      CONST constants,
      AbstractVariableManager<MPSolver, MPVariable> variables) throws Exception {

    // min sum_j C_j
    objective.setMinimization();

    for (int j : constants.get_J()) {
      objective.setCoefficient(
          variables.getVar(FurnaceSchedulingVars.C, j),
          1.);
    }
  }
}
