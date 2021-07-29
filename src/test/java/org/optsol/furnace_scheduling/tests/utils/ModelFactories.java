/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.tests.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.optsol.furnace_scheduling.model.FurnaceSchedulingModel;
import org.optsol.furnace_scheduling.model.constants.IFurnaceScheduling_Constants;
import org.optsol.jdecor.ortools.AbstractOrtoolsModelFactory;
import org.optsol.jdecor.ortools.SolverEngine;


public final class ModelFactories {
  public static Map<String, AbstractOrtoolsModelFactory<IFurnaceScheduling_Constants>>
  getAllModelFactories_SCIP() {
    Map<String, AbstractOrtoolsModelFactory<IFurnaceScheduling_Constants>> factories =
        new HashMap<>();

    factories.put(
        "PURE",
        generateModelFactory(SolverEngine.SCIP));

    factories.put(
        "II",
        generateModelFactory(SolverEngine.SCIP, Constraint.II));

    factories.put(
        "II_I",
        generateModelFactory(SolverEngine.SCIP, Constraint.II, Constraint.I));
    factories.put(
        "II_III",
        generateModelFactory(SolverEngine.SCIP, Constraint.II, Constraint.III));
    factories.put(
        "II_IV",
        generateModelFactory(SolverEngine.SCIP, Constraint.II, Constraint.IV));
    factories.put(
        "II_V",
        generateModelFactory(SolverEngine.SCIP, Constraint.II, Constraint.V));


    factories.put(
        "I_II_II_IV",
        generateModelFactory(
            SolverEngine.SCIP,
            Constraint.I,
            Constraint.II,
            Constraint.III,
            Constraint.IV));
    factories.put(
        "I_II_II_IV_V",
        generateModelFactory(
            SolverEngine.SCIP,
            Constraint.I,
            Constraint.II,
            Constraint.III,
            Constraint.IV,
            Constraint.V));

    return factories;
  }

  public static Map<String, AbstractOrtoolsModelFactory<IFurnaceScheduling_Constants>>
  getAllModelFactories_GLOP() {
    Map<String, AbstractOrtoolsModelFactory<IFurnaceScheduling_Constants>> factories =
        new HashMap<>();

    factories.put(
        "PURE-LP",
        generateModelFactory(SolverEngine.GLOP));

    factories.put(
        "I-LP",
        generateModelFactory(SolverEngine.GLOP, Constraint.I));
    factories.put(
        "II-LP",
        generateModelFactory(SolverEngine.GLOP, Constraint.II));
    factories.put(
        "III-LP",
        generateModelFactory(SolverEngine.GLOP, Constraint.III));
    factories.put(
        "IV-LP",
        generateModelFactory(SolverEngine.GLOP, Constraint.IV));

    factories.put(
        "V-LP",
        generateModelFactory(SolverEngine.GLOP, Constraint.V));

    return factories;
  }

  public static AbstractOrtoolsModelFactory<IFurnaceScheduling_Constants> generateModelFactory(
      SolverEngine solverEngine,
      Constraint... configs) {
    return
        new FurnaceSchedulingModel(
            solverEngine,
            Arrays.asList(configs).contains(Constraint.I),
            Arrays.asList(configs).contains(Constraint.II),
            Arrays.asList(configs).contains(Constraint.III),
            Arrays.asList(configs).contains(Constraint.IV),
            Arrays.asList(configs).contains(Constraint.V));
  }

  public enum Constraint {
    I, II, III, IV, V
  }
}
