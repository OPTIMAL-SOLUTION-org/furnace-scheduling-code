/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.model;

import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import java.util.ArrayList;
import java.util.List;
import org.optsol.furnace_scheduling.model.constants.IFurnaceScheduling_Constants;
import org.optsol.furnace_scheduling.model.constraints.Constraint_b;
import org.optsol.furnace_scheduling.model.constraints.Constraint_c;
import org.optsol.furnace_scheduling.model.constraints.Constraint_d;
import org.optsol.furnace_scheduling.model.constraints.Constraint_e;
import org.optsol.furnace_scheduling.model.constraints.Constraint_f;
import org.optsol.furnace_scheduling.model.constraints.Constraint_g;
import org.optsol.furnace_scheduling.model.constraints.Constraint_h;
import org.optsol.furnace_scheduling.model.constraints.Constraint_i;
import org.optsol.furnace_scheduling.model.constraints.additional.Constraint_I;
import org.optsol.furnace_scheduling.model.constraints.additional.Constraint_II;
import org.optsol.furnace_scheduling.model.constraints.additional.Constraint_III;
import org.optsol.furnace_scheduling.model.constraints.additional.Constraint_IV;
import org.optsol.furnace_scheduling.model.constraints.additional.Constraint_V;
import org.optsol.furnace_scheduling.model.objective.Objective_a;
import org.optsol.furnace_scheduling.model.variables.FurnaceSchedulingVars;
import org.optsol.jdecor.core.AbstractVariableManager;
import org.optsol.jdecor.core.IConstraintManager;
import org.optsol.jdecor.core.IObjectiveManager;
import org.optsol.jdecor.ortools.AbstractOrtoolsModelFactory;
import org.optsol.jdecor.ortools.OrtoolsVariableManager;
import org.optsol.jdecor.ortools.SolverEngine;

public class FurnaceSchedulingModel
    extends AbstractOrtoolsModelFactory<IFurnaceScheduling_Constants> {

  private final boolean I_active;
  private final boolean II_active;
  private final boolean III_active;
  private final boolean IV_active;
  private final boolean V_active;

  public FurnaceSchedulingModel(
      SolverEngine solverEngine,
      boolean I_active,
      boolean II_active,
      boolean III_active,
      boolean IV_active,
      boolean V_active) {
    super(solverEngine);
    this.I_active = I_active;
    this.II_active = II_active;
    this.III_active = III_active;
    this.IV_active = IV_active;
    this.V_active = V_active;
  }

  public FurnaceSchedulingModel(SolverEngine solverEngine) {
    this(
        solverEngine,
        false,
        false,
        false,
        false,
        false);
  }

  @Override
  protected AbstractVariableManager<MPSolver, MPVariable> generateVarManager() {
    return
        new OrtoolsVariableManager.Builder()
            // x : bool (is job i direct predecessor of job j on machine k?)
            .addIntVar(FurnaceSchedulingVars.x)
            .addLowerBound(FurnaceSchedulingVars.x, 0.)
            .addUpperBound(FurnaceSchedulingVars.x, 1.)

            // z : bool (is job j must start after completing job i?)
            .addIntVar(FurnaceSchedulingVars.z)
            .addLowerBound(FurnaceSchedulingVars.z, 0.)
            .addUpperBound(FurnaceSchedulingVars.z, 1.)

            // get_C : real+ (completion time of job j)
            .addLowerBound(FurnaceSchedulingVars.C, 0.)

            .build();
  }

  @Override
  protected IObjectiveManager<
      ? super IFurnaceScheduling_Constants, MPVariable, MPSolver> generateObjective() {
    return new Objective_a<>();
  }

  @Override
  protected List<
      IConstraintManager<
          ? super IFurnaceScheduling_Constants,
          MPVariable,
          MPSolver>> generateConstraints() {

    List<IConstraintManager<
        ? super IFurnaceScheduling_Constants,
        MPVariable,
        MPSolver>> constraints = new ArrayList<>();
    constraints.add(new Constraint_b<>());
    constraints.add(new Constraint_c<>());
    constraints.add(new Constraint_d<>());
    constraints.add(new Constraint_e<>());
    constraints.add(new Constraint_f<>());
    constraints.add(new Constraint_g<>());
    constraints.add(new Constraint_h<>());
    constraints.add(new Constraint_i<>());

    if (I_active) {
      constraints.add(new Constraint_I<>());//valid inequality 1)
    }
    if (II_active) {
      constraints.add(new Constraint_II<>());
    }
    if (III_active) {
      constraints.add(new Constraint_III<>());
    }
    if (IV_active) {
      constraints.add(new Constraint_IV<>());
    }
    if (V_active) {
      constraints.add(new Constraint_V<>()); //symmetry breaking
    }

    return constraints;
  }
}
