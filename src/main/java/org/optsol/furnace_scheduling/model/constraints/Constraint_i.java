/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.model.constraints;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_M;
import org.optsol.furnace_scheduling.model.variables.FurnaceSchedulingVars;
import org.optsol.jdecor.core.AbstractVariableManager;
import org.optsol.jdecor.ortools.AbstractOrtoolsConstraintManager;

public class Constraint_i<
    CONST extends IConstants_M>
    extends AbstractOrtoolsConstraintManager<CONST> {

  @Override
  protected void configureConstraint(
      MPConstraint constraint,
      CONST constants,
      AbstractVariableManager<MPSolver, MPVariable> variables,
      ConstraintKey index) throws Exception {

    //C_0 = 0.
    constraint.setBounds(0., 0.);

    constraint.setCoefficient(
        variables.getVar(FurnaceSchedulingVars.C, 0),
        1.);
  }
}
