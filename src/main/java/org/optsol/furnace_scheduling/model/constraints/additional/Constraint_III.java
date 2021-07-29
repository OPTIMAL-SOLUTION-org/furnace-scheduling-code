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
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_V;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_p_jk;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_s_ijk;
import org.optsol.furnace_scheduling.model.variables.FurnaceSchedulingVars;
import org.optsol.jdecor.core.AbstractVariableManager;
import org.optsol.jdecor.ortools.AbstractOrtoolsConstraintManager;

public class Constraint_III<
    CONST extends IConstants_J & IConstants_M_j & IConstants_p_jk & IConstants_s_ijk & IConstants_V>
    extends AbstractOrtoolsConstraintManager<CONST> {

  public Constraint_III() {
    super("i", "j");
  }

  @Override
  protected Collection<ConstraintKey> createKeys(CONST constants) {
    Set<ConstraintKey> keys = new HashSet<>();

    for (int i : constants.get_J0()) {
      for (int j : constants.get_J()) {
        if (i != j) {
          if (constants.get_M_j(i).stream()
              .anyMatch(k -> constants.get_M_j(j).contains(k))) {
            keys.add(new ConstraintKey(i, j));
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

    //C_i + min_k:M_i(AND)M_j [s_ijk + p_jk] - V_III_ij ( 1 - sum_k:M_i(AND)M_j  [x_ijk] ) <= C_j

    //C_i - C_j + sum_k:M_i(AND)M_j [V_III_ij * x_ijk] <=
    // V_III_ij - min_k:M_i(AND)M_j [s_ijk + p_jk]
    constraint.setUb(
        get_V_III(i, j, constants) - calcMinSetupProcessTimeSum(constants, i, j));

    constraint.setCoefficient(
        variables.getVar(FurnaceSchedulingVars.C, i),
        1.);

    constraint.setCoefficient(
        variables.getVar(FurnaceSchedulingVars.C, j),
        -1.);

    for (int k : constants.get_M_j(i)) {
      if (constants.get_M_j(j).contains(k)) {
        constraint.setCoefficient(
            variables.getVar(FurnaceSchedulingVars.x, i, j, k),
            get_V_III(i, j, constants));
      }
    }
  }

  private double calcMinSetupProcessTimeSum(
      CONST constants,
      int i,
      int j) {
    return
        constants.get_M_j(i).stream()
            .filter(k -> constants.get_M_j(j).contains(k))
            .mapToDouble(k -> constants.get_s_ijk(i, j, k) + constants.get_p_jk(j, k))
            .min()
            .getAsDouble();
  }

  private double get_V_III(
      int i,
      int j,
      CONST constants) {
    double v_III = constants.get_V() + calcMinSetupProcessTimeSum(constants, i, j);

    if (v_III - constants.get_V() != calcMinSetupProcessTimeSum(constants, i, j)) {
      throw new Error("Precision trouble due to too big V");
    }

    return v_III;
  }
}
