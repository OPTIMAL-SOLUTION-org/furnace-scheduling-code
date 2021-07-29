/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.optsol.furnace_scheduling.model.constants.FurnaceScheduling_Constants;
import org.optsol.furnace_scheduling.model.constants.IFurnaceScheduling_Constants;
import org.optsol.furnace_scheduling.solver.FurnaceSchedulingSolver;
import org.optsol.furnace_scheduling.solver.IFurnaceScheduling_Solution;
import org.optsol.furnace_scheduling.tests.utils.InstanceNames;
import org.optsol.furnace_scheduling.tests.utils.ModelFactories;
import org.optsol.furnace_scheduling.tests.utils.io.YamlResourceFileToObject;
import org.optsol.furnace_scheduling.tests.utils.solution.SolutionPrinter;
import org.optsol.furnace_scheduling.tests.utils.validity.SolutionValidityCheck;
import org.optsol.jdecor.ortools.AbstractOrtoolsModelFactory;

@Slf4j
public class ComputationalExperiments {


  @Test
  public void runExperiments() {

    // RUN ALL INSTANCES WITH ALL SOLVER CONFIGURATIONS AND TIMELIMIT=600sec

    StringBuilder stringBuilder =
        new StringBuilder()
            .append(System.getProperty("line.separator"))
            .append("instance,solver,time(s),state,objective,bestbound")
            .append(System.getProperty("line.separator"));

    Map<String, AbstractOrtoolsModelFactory<IFurnaceScheduling_Constants>> factoryMap =
        ModelFactories.getAllModelFactories_SCIP();

    for (String modelFactoryName : factoryMap.keySet()) {
      for (String instance : InstanceNames.INSTANCES) {

        log.info("Start solving model of instance " + instance + " with " + modelFactoryName +
            " solver...");

        IFurnaceScheduling_Constants constants =
            new YamlResourceFileToObject<FurnaceScheduling_Constants>().read(
                FurnaceScheduling_Constants.class,
                "instances/" + instance + ".yaml");
        try {

          IFurnaceScheduling_Solution solution =
              new FurnaceSchedulingSolver(
                  factoryMap.get(modelFactoryName),
                  600)
                  .generateSolution(constants);

          log.info(
              "...finished solving model of instance" + System.getProperty("line.separator")
                  + instance + " with " + modelFactoryName + " solver: "
                  + solution.getSolutionState() + ": "
                  + (solution.getObjectiveValue() != null ?
                  Math.round(solution.getObjectiveValue() * 100.) / 100. : "null")
                  + "("
                  + (solution.getBestObjectiveBound() != null ?
                  Math.round(solution.getBestObjectiveBound() * 100.) / 100. : "null")
                  + ") in " + solution.getSolutionTime().toSeconds() + " seconds"
                  + System.getProperty("line.separator"));

          stringBuilder
              .append(instance).append(",")
              .append(modelFactoryName).append(",")
              .append(solution.getSolutionTime().toSeconds()).append(",")
              .append(solution.getSolutionState()).append(",");
          if (solution.getObjectiveValue() != null) {
            stringBuilder
                .append(Math.round(solution.getObjectiveValue() * 100.) / 100.).append(",")
                .append(Math.round(solution.getBestObjectiveBound() * 100.) / 100.);
          } else {
            stringBuilder
                .append("null").append(",")
                .append("null");
          }
          stringBuilder
              .append(System.getProperty("line.separator"));

          if (solution.getObjectiveValue() != null) {
            SolutionPrinter.printSolution(solution);

            assertTrue(new SolutionValidityCheck().checkValid(constants, solution));
          }

        } catch (Exception ex) {
          fail(ex);
        }
      }
    }

    log.info(stringBuilder.toString());
  }


  @Test
  public void runRootNodeRelaxations() {

    StringBuilder stringBuilder =
        new StringBuilder()
            .append(System.getProperty("line.separator"))
            .append("instance,solver,time(s),state,objective")
            .append(System.getProperty("line.separator"));

    Map<String, AbstractOrtoolsModelFactory<IFurnaceScheduling_Constants>> factoryMap =
        ModelFactories.getAllModelFactories_GLOP();

    for (String modelFactoryName : factoryMap.keySet()) {
      for (String instance : InstanceNames.INSTANCES) {

        log.info("start solving model of instance " + instance + " with " + modelFactoryName +
            " solver...");

        IFurnaceScheduling_Constants constants =
            new YamlResourceFileToObject<FurnaceScheduling_Constants>().read(
                FurnaceScheduling_Constants.class,
                "instances/" + instance + ".yaml");
        try {

          IFurnaceScheduling_Solution solution =
              new FurnaceSchedulingSolver(
                  factoryMap.get(modelFactoryName),
                  100)
                  .generateSolution(constants);

          log.info(
              "...finished solving model of instance " + instance + " with " + modelFactoryName +
                  " solver: "
                  + solution.getSolutionState() + ": " +
                  (solution.getObjectiveValue() != null ?
                      Math.round(solution.getObjectiveValue() * 100.) / 100. : "--")
                  + " in " + solution.getSolutionTime().toSeconds() + " seconds");

          StringBuilder solutionString =
              new StringBuilder().append(instance).append(",")
                  .append(modelFactoryName).append(",")
                  .append(solution.getSolutionTime().toSeconds()).append(",")
                  .append(solution.getSolutionState()).append(",");
          if (solution.getObjectiveValue() != null) {
            solutionString
                .append(Math.round(solution.getObjectiveValue() * 100.) / 100.);
          } else {
            solutionString
                .append("--");
          }
          solutionString
              .append(System.getProperty("line.separator"));
          stringBuilder.append(solutionString);

          log.info(solutionString.toString());

        } catch (Exception ex) {
          fail(ex);
        }
      }
    }

    log.info(stringBuilder.toString());
  }
}
