/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.model.constraints;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import java.util.Collection;
import java.util.stream.Collectors;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_J;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_M;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_M_j;
import org.optsol.furnace_scheduling.model.variables.FurnaceSchedulingVars;
import org.optsol.jdecor.core.AbstractVariableManager;
import org.optsol.jdecor.ortools.AbstractOrtoolsConstraintManager;

public class Constraint_d<
    CONST extends IConstants_J & IConstants_M & IConstants_M_j>
    extends AbstractOrtoolsConstraintManager<CONST> {

  public Constraint_d() {
    super("k");
  }

  @Override
  protected Collection<ConstraintKey> createKeys(CONST constants) {
    return
        constants.get_M()
            .stream()
            .map(ConstraintKey::new)
            .collect(Collectors.toSet());
  }

  @Override
  protected void configureConstraint(
      MPConstraint constraint,
      CONST constants,
      AbstractVariableManager<MPSolver, MPVariable> variables,
      ConstraintKey index) throws Exception {

    int k = index.get("k");

    // sum_j:J,k:M_j x0jk <= 1.
    constraint.setUb(1.);

    for (int j : constants.get_J()) {
      if (constants.get_M_j(j).contains(k)) {
        constraint.setCoefficient(
            variables.getVar(FurnaceSchedulingVars.x, 0, j, k),
            1.);
      }
    }
  }
}
