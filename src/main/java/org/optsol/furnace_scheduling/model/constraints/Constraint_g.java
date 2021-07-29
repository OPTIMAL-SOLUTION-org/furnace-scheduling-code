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
import org.optsol.furnace_scheduling.model.variables.FurnaceSchedulingVars;
import org.optsol.furnace_scheduling.utils.ImmutableIntPair;
import org.optsol.jdecor.core.AbstractVariableManager;
import org.optsol.jdecor.ortools.AbstractOrtoolsConstraintManager;

public class Constraint_g<
    CONST extends IConstants_J & IConstants_J_con & IConstants_M_con & IConstants_M_j>
    extends AbstractOrtoolsConstraintManager<CONST> {

  public Constraint_g() {
    super("j", "h", "k", "l");
  }

  @Override
  protected Collection<ConstraintKey> createKeys(CONST constants) {
    Set<ConstraintKey> keys = new HashSet<>();

    for (ImmutableIntPair j_con : constants.get_J_con()) {
      int j = j_con.getFirst();
      int h = j_con.getSecond();
      for (ImmutableIntPair m_con : constants.get_M_con()) {
        int k = m_con.getFirst();
        int l = m_con.getSecond();
        if (constants.get_M_j(j).contains(k)) {
          if (constants.get_M_j(h).contains(l)) {
            keys.add(new ConstraintKey(j, h, k, l));
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

    int j = index.get("j");
    int h = index.get("h");
    int k = index.get("k");
    int l = index.get("l");

    //sum_i:J0,i!=j,k:M_i x_ijk + sum_i:J0,i!=h,l:M_i x_ihl - 1. <= z_hj + z_jh
    //sum_i:J0,i!=j,k:M_i x_ijk + sum_i:J0,i!=h,l:M_i x_ihl - z_hj - z_jh <= 1.
    constraint.setUb(1.);

    for (int i : constants.get_J0()) {
      if (i != j) {
        if (constants.get_M_j(i).contains(k)) {
          constraint.setCoefficient(
              variables.getVar(FurnaceSchedulingVars.x, i, j, k),
              1.);
        }
      }
    }

    for (int i : constants.get_J0()) {
      if (i != h) {
        if (constants.get_M_j(i).contains(l)) {
          constraint.setCoefficient(
              variables.getVar(FurnaceSchedulingVars.x, i, h, l),
              1.);
        }
      }
    }

    constraint.setCoefficient(
        variables.getVar(FurnaceSchedulingVars.z, h, j),
        -1.);

    constraint.setCoefficient(
        variables.getVar(FurnaceSchedulingVars.z, j, h),
        -1.);

  }
}
