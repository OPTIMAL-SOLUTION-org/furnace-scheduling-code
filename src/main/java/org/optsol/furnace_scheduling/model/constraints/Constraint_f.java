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
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_V;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_p_jk;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_s_ijk;
import org.optsol.furnace_scheduling.model.variables.FurnaceSchedulingVars;
import org.optsol.jdecor.core.AbstractVariableManager;
import org.optsol.jdecor.ortools.AbstractOrtoolsConstraintManager;

public class Constraint_f<
    CONST extends IConstants_J & IConstants_M_j & IConstants_p_jk & IConstants_s_ijk & IConstants_V>
    extends AbstractOrtoolsConstraintManager<CONST> {

  public Constraint_f() {
    super("i", "j", "k");
  }

  @Override
  protected Collection<ConstraintKey> createKeys(CONST constants) {
    Set<ConstraintKey> keys = new HashSet<>();

    for (int i : constants.get_J0()) {
      for (int j : constants.get_J()) {
        if (i != j) {
          for (int k : constants.get_M_j(i)) {
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

    //C_i + s_ijk + p_jk - V_f_ijk (1 - x_ijk) <= C_j

    //C_i + V_f_ijk x_ijk - C_j <= V_f_ijk - s_ijk - p_jk
    constraint.setUb(
        get_V_f(i, j, k, constants) - constants.get_s_ijk(i, j, k) - constants.get_p_jk(j, k));

    constraint.setCoefficient(
        variables.getVar(FurnaceSchedulingVars.C, i),
        1.);

    constraint.setCoefficient(
        variables.getVar(FurnaceSchedulingVars.x, i, j, k),
        get_V_f(i, j, k, constants));

    constraint.setCoefficient(
        variables.getVar(FurnaceSchedulingVars.C, j),
        -1.);
  }

  private double get_V_f(
      int i,
      int j,
      int k,
      CONST constants) {
    double v_f = constants.get_V() + constants.get_s_ijk(i, j, k) + constants.get_p_jk(j, k);

    if (v_f - constants.get_V() != constants.get_s_ijk(i, j, k) + constants.get_p_jk(j, k)) {
      throw new Error("Precision trouble due to too big V");
    }

    return v_f;
  }
}
