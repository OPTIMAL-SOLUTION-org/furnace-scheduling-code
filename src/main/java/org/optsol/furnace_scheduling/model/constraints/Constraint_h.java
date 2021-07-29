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
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_J_con;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_M_con;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_M_j;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_V;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_p_jk;
import org.optsol.furnace_scheduling.model.variables.FurnaceSchedulingVars;
import org.optsol.furnace_scheduling.utils.ImmutableIntPair;
import org.optsol.jdecor.core.AbstractVariableManager;
import org.optsol.jdecor.ortools.AbstractOrtoolsConstraintManager;

public class Constraint_h<
    CONST extends
        IConstants_J
        & IConstants_J_con
        & IConstants_M_j
        & IConstants_M_con
        & IConstants_p_jk
        & IConstants_V>
    extends AbstractOrtoolsConstraintManager<CONST> {

  public Constraint_h() {
    super("j", "h", "k");
  }

  @Override
  protected Collection<ConstraintKey> createKeys(CONST constants) {
    Set<ConstraintKey> keys = new HashSet<>();

    for (ImmutableIntPair j_con : constants.get_J_con()) {
      int j = j_con.getFirst();
      int h = j_con.getSecond();
      for (int k : constants.get_M_j(j)) {
        if (constants.get_M_con().stream()
            .anyMatch(pair -> {
              int l1 = pair.getFirst();
              int l2 = pair.getSecond();

              return (l1 == k && constants.get_M_j(h).contains(l2)) ||
                  (constants.get_M_j(h).contains(l1) && l2 == k);
            })
        ) {
          keys.add(new ConstraintKey(j, h, k));
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

    int j = index.get("j");
    int h = index.get("h");
    int k = index.get("k");

    //C_h + p_jk - V_h_jk (2 - z_hj - sum_i:J0,i!=j,k:M_i x_ijk) <= C_j

    //C_h  - C_j + V_h_jk * z_hj + V_h_jk * sum_i:J0,i!=j,k:M_i x_ijk <= 2V_h_jk - p_jk
    constraint.setUb(2 * get_V_h(j, k, constants) - constants.get_p_jk(j, k));

    constraint.setCoefficient(
        variables.getVar(FurnaceSchedulingVars.C, h),
        1.);

    constraint.setCoefficient(
        variables.getVar(FurnaceSchedulingVars.C, j),
        -1.);

    constraint.setCoefficient(
        variables.getVar(FurnaceSchedulingVars.z, h, j),
        get_V_h(j, k, constants));

    for (int i : constants.get_J0()) {
      if (i != j) {
        if (constants.get_M_j(i).contains(k)) {
          constraint.setCoefficient(
              variables.getVar(FurnaceSchedulingVars.x, i, j, k),
              get_V_h(j, k, constants));
        }
      }
    }
  }

  private double get_V_h(
      int j,
      int k,
      CONST constants) {
    double v_h = constants.get_V() + constants.get_p_jk(j, k);

    if (v_h - constants.get_V() != constants.get_p_jk(j, k)) {
      throw new Error("Precision trouble due to too big V");
    }

    return v_h;
  }
}
