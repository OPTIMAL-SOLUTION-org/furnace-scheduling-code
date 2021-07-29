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
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_J_con;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_M_j;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_V;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_p_jk;
import org.optsol.furnace_scheduling.model.variables.FurnaceSchedulingVars;
import org.optsol.furnace_scheduling.utils.ImmutableIntPair;
import org.optsol.jdecor.core.AbstractVariableManager;
import org.optsol.jdecor.ortools.AbstractOrtoolsConstraintManager;

public class Constraint_IV<
    CONST extends IConstants_J & IConstants_M_j & IConstants_p_jk & IConstants_J_con & IConstants_V>
    extends AbstractOrtoolsConstraintManager<CONST> {

  public Constraint_IV() {
    super("j", "h");
  }

  @Override
  protected Collection<ConstraintKey> createKeys(CONST constants) {
    Set<ConstraintKey> keys = new HashSet<>();

    for (ImmutableIntPair j_con : constants.get_J_con()) {
      int j = j_con.getFirst();
      int h = j_con.getSecond();
      keys.add(new ConstraintKey(j, h));
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

    //C_h + min_k:M_j [p_jk] - V_IV_j ( 2 - z_hj - sum_i:J0,i!=j sum_k:M_i(AND)M_j  [x_ijk]) <=
    // C_j

    //C_h -  C_j + V_IV_j * z_hj + sum_i:J0,i!=j sum_k:M_i(AND)M_j [V_IV_j*x_ijk] <=
    // 2*V_IV_j - min_k:M_j [p_jk]
    constraint.setUb(2 * get_V_IV(j, constants) - calcMinProcessTime(constants, j));

    constraint.setCoefficient(
        variables.getVar(FurnaceSchedulingVars.C, h),
        1.);

    constraint.setCoefficient(
        variables.getVar(FurnaceSchedulingVars.C, j),
        -1.);

    constraint.setCoefficient(
        variables.getVar(FurnaceSchedulingVars.z, h, j),
        get_V_IV(j, constants));

    for (int i : constants.get_J0()) {
      if (i != j) {
        for (int k : constants.get_M_j(i)) {
          if (constants.get_M_j(j).contains(k)) {
            constraint.setCoefficient(
                variables.getVar(FurnaceSchedulingVars.x, i, j, k),
                get_V_IV(j, constants));
          }
        }
      }
    }
  }

  private double calcMinProcessTime(
      CONST constants,
      int j) {
    return
        constants.get_M_j(j).stream()
            .mapToDouble(k -> constants.get_p_jk(j, k))
            .min()
            .getAsDouble();
  }

  private double get_V_IV(
      int j,
      CONST constants) {
    double v_IV = constants.get_V() + calcMinProcessTime(constants, j);

    if (v_IV - constants.get_V() != calcMinProcessTime(constants, j)) {
      throw new Error("Precision trouble due to too big V");
    }

    return v_IV;
  }
}
