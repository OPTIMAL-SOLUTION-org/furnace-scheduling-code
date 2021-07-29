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
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_J;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_M_j;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_M_sym;
import org.optsol.furnace_scheduling.model.variables.FurnaceSchedulingVars;
import org.optsol.furnace_scheduling.utils.ImmutableIntPair;
import org.optsol.jdecor.core.AbstractVariableManager;
import org.optsol.jdecor.ortools.AbstractOrtoolsConstraintManager;

public class Constraint_V<
    CONST extends IConstants_J & IConstants_M_sym & IConstants_M_j>
    extends AbstractOrtoolsConstraintManager<CONST> {

  public Constraint_V() {
    super("h", "k", "l");
  }

  @Override
  protected Collection<ConstraintKey> createKeys(CONST constants) {
    Set<ConstraintKey> keys = new HashSet<>();

    for (int h : constants.get_J()) {
      for (ImmutableIntPair m_sym : constants.get_M_sym()) {
        int k = m_sym.getFirst();
        int l = m_sym.getSecond();

        if (constants.get_M_j(h).contains(l)) {
          keys.add(new ConstraintKey(h, k, l));
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

    int h = index.get("h");
    int k = index.get("k");
    int l = index.get("l");

    //sum_j:{1..h-1},k:M_j x_0jk >= x_0hl
    // 0. <= sum_j:{1...h-1},k:M_j x_0jk - x_0hl
    constraint.setLb(0.);

    for (int j : IntStream.rangeClosed(1, h - 1).boxed().collect(Collectors.toSet())) {
      if (constants.get_M_j(j).contains(k)) {
        constraint.setCoefficient(
            variables.getVar(FurnaceSchedulingVars.x, 0, j, k),
            1.);
      }
    }

    constraint.setCoefficient(
        variables.getVar(FurnaceSchedulingVars.x, 0, h, l),
        -1.);
  }
}
