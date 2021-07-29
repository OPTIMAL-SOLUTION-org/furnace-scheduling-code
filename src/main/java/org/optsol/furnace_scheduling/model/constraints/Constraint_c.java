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
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_M_j;
import org.optsol.furnace_scheduling.model.variables.FurnaceSchedulingVars;
import org.optsol.jdecor.core.AbstractVariableManager;
import org.optsol.jdecor.ortools.AbstractOrtoolsConstraintManager;

public class Constraint_c<
    CONST extends IConstants_J & IConstants_M_j>
    extends AbstractOrtoolsConstraintManager<CONST> {

  public Constraint_c() {
    super("i");
  }

  @Override
  protected Collection<ConstraintKey> createKeys(CONST constants) {
    return
        constants.get_J()
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

    int i = index.get("i");

    //sum_j:J,i!=j sum_k:M_i(AND)M_j x_ijk <= 1
    constraint.setUb(1.);

    for (Integer j : constants.get_J()) {
      if (i != j) {
        for (Integer k : constants.get_M_j(i)) {
          if (constants.get_M_j(j).contains(k)) {
            constraint.setCoefficient(
                variables.getVar(FurnaceSchedulingVars.x, i, j, k),
                1.);
          }
        }
      }
    }
  }
}
