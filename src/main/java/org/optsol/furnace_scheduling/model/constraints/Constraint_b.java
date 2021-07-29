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

public class Constraint_b<
    CONST extends IConstants_J & IConstants_M_j>
    extends AbstractOrtoolsConstraintManager<CONST> {

  public Constraint_b() {
    super("j");
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

    int j = index.get("j");

    //sum_i:J0,i!=j sum_k:M_i(AND)M_j x_ijk = 1
    constraint.setBounds(1., 1.);

    for (Integer i : constants.get_J0()) {
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
