/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.model.constraints.additional;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_J;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_M_j;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_p_jk;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_s_ijk;
import org.optsol.furnace_scheduling.model.variables.FurnaceSchedulingVars;
import org.optsol.jdecor.core.AbstractVariableManager;
import org.optsol.jdecor.ortools.AbstractOrtoolsConstraintManager;

public class Constraint_II<
    CONST extends
        IConstants_J &
        IConstants_M_j &
        IConstants_p_jk &
        IConstants_s_ijk>
    extends AbstractOrtoolsConstraintManager<CONST> {

  public Constraint_II() {
    super("j");
  }

  @Override
  protected Collection<ConstraintKey> createKeys(CONST constants) {
    Set<ConstraintKey> keys = new HashSet<>();

    for (int j : constants.get_J()) {
      keys.add(new ConstraintKey(j));
    }

    return keys;
  }

  @Override
  protected void configureConstraint(
      MPConstraint constraint,
      CONST constants,
      AbstractVariableManager<MPSolver, MPVariable> variables,
      ConstraintKey index) throws Exception {

    int j = index.get("j");

    //sum_i:J0,i!=j sum_k:M_i(AND)M_j (s_ijk + p_jk) * x_ijk <= C_j
    //sum_i:J0,i!=j sum_k:M_i(AND)M_j (s_ijk + p_jk) * x_ijk - C_j <= 0.
    constraint.setUb(0.);

    for (Integer i : constants.get_J0()) {
      if (i != j) {
        for (Integer k : constants.get_M_j(i)) {
          if (constants.get_M_j(j).contains(k)) {
            constraint.setCoefficient(
                variables.getVar(FurnaceSchedulingVars.x, i, j, k),
                constants.get_s_ijk(i, j, k) + constants.get_p_jk(j, k));
          }
        }
      }
    }

    constraint.setCoefficient(
        variables.getVar(FurnaceSchedulingVars.C, j),
        -1.);
  }
}
