/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.model.constraints;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_J;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_M_j;
import org.optsol.furnace_scheduling.model.variables.FurnaceSchedulingVars;
import org.optsol.jdecor.core.AbstractVariableManager;
import org.optsol.jdecor.ortools.AbstractOrtoolsConstraintManager;

public class Constraint_e<
    CONST extends IConstants_J & IConstants_M_j>
    extends AbstractOrtoolsConstraintManager<CONST> {

  public Constraint_e() {
    super("i", "j", "k");
  }

  @Override
  protected Collection<ConstraintKey> createKeys(CONST constants) {
    Set<ConstraintKey> keys = new HashSet<>();

    for (int i : constants.get_J()) {
      for (int j : constants.get_J()) {
        if (i != j) {
          for (Integer k : constants.get_M_j(i)) {
            if (constants.get_M_j(j).contains(k)) {
              keys.add(new ConstraintKey(i, j, k));
            }
          }
        }
      }
    }

    return keys;
  }

  @Override
  protected void configureConstraint(
      MPConstraint constraint,
      CONST constants,
      AbstractVariableManager<MPSolver, MPVariable> variables,
      ConstraintKey index) throws Exception {

    int i = index.get("i");
    int j = index.get("j");
    int k = index.get("k");

    //sum_h:J0,h!=i,h!=j,k:M_h x_hik >= x_ijk

    //sum_h:J0,h!=i,h!=j,k:M_h x_hik - x_ijk >= 0.
    constraint.setLb(0.);

    for (int h : constants.get_J0()) {
      if (h != i && h != j) {
        if (constants.get_M_j(h).contains(k)) {
          constraint.setCoefficient(
              variables.getVar(FurnaceSchedulingVars.x, h, i, k),
              1.);
        }
      }
    }

    constraint.setCoefficient(
        variables.getVar(FurnaceSchedulingVars.x, i, j, k),
        -1.);
  }
}
