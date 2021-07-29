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
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_J_con;
import org.optsol.furnace_scheduling.model.variables.FurnaceSchedulingVars;
import org.optsol.furnace_scheduling.utils.ImmutableIntPair;
import org.optsol.jdecor.core.AbstractVariableManager;
import org.optsol.jdecor.ortools.AbstractOrtoolsConstraintManager;

public class Constraint_I<
    CONST extends IConstants_J_con>
    extends AbstractOrtoolsConstraintManager<CONST> {

  public Constraint_I() {
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

    //z_hj + z_jh <= 1.
    constraint.setUb(1.);

    constraint.setCoefficient(
        variables.getVar(FurnaceSchedulingVars.z, j, h),
        1.);

    constraint.setCoefficient(
        variables.getVar(FurnaceSchedulingVars.z, h, j),
        1.);
  }

}
