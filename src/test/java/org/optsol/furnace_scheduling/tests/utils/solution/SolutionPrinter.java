/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.tests.utils.solution;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.optsol.furnace_scheduling.solver.IFurnaceScheduling_Solution;
import org.optsol.furnace_scheduling.utils.ImmutableIntPair;

@Slf4j
public class SolutionPrinter {

  public static void printSolution(IFurnaceScheduling_Solution solution) {
    Boolean[][][] x_ijk = solution.get_x();
    Double[] C_j = solution.get_C();

    Map<Integer, Set<ImmutableIntPair>> machineJobs = getMachineJobAssignmentsMap(x_ijk);

    StringBuilder stringBuilder = new StringBuilder().append(System.getProperty("line.separator"));

    machineJobs.keySet().stream()
        .sorted()
        .forEach(
            machine -> {
              stringBuilder.append("Machine " + machine + ": ");

              machineJobs.get(machine).stream()
                  .sorted(Comparator.comparing(o -> C_j[o.getFirst()]))
                  .forEach(pair ->
                      stringBuilder.append(" | "
                          + pair.getFirst()
                          + " (" + Math.round(C_j[pair.getFirst()]) + ") -> "
                          + pair.getSecond()));

              List<ImmutableIntPair> jobPairList =
                  machineJobs.get(machine).stream()
                      .sorted(Comparator.comparing(o -> C_j[o.getFirst()]))
                      .collect(Collectors.toList());

              stringBuilder.append(
                  " (" + Math.round(C_j[jobPairList.get(jobPairList.size() - 1).getSecond()]) +
                      ")");

              stringBuilder.append(System.getProperty("line.separator"));
            }
        );

    log.info(stringBuilder.toString());
  }

  private static Map<Integer, Set<ImmutableIntPair>> getMachineJobAssignmentsMap(
      Boolean[][][] x_ijk) {
    Map<Integer, Set<ImmutableIntPair>> machineJobs = new HashMap<>();

    for (int i = 0; i < x_ijk.length; i++) {
      for (int j = 0; j < x_ijk[i].length; j++) {
        for (int k = 0; k < x_ijk[i][j].length; k++) {
          if (x_ijk[i][j][k] != null && x_ijk[i][j][k]) {
            machineJobs.putIfAbsent(k, new HashSet<>());
            machineJobs.get(k).add(new ImmutableIntPair(i, j));
          }
        }
      }
    }
    return machineJobs;
  }
}
