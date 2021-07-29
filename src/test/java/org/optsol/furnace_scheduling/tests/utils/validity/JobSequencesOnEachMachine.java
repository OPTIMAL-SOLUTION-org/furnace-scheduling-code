/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.tests.utils.validity;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.optsol.furnace_scheduling.model.constants.IFurnaceScheduling_Constants;
import org.optsol.furnace_scheduling.solver.IFurnaceScheduling_Solution;
import org.optsol.furnace_scheduling.utils.ImmutableIntPair;

@Slf4j
public class JobSequencesOnEachMachine implements IValidityChecks {

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

  @Override
  public boolean checkValid(
      IFurnaceScheduling_Constants constants,
      IFurnaceScheduling_Solution solution) {

    boolean valid = true;

    Map<Integer, Set<ImmutableIntPair>> machineJobs =
        getMachineJobAssignmentsMap(solution.get_x());

    Double[] C = solution.get_C();

    for (int k : constants.get_M()) {
      if (machineJobs.containsKey(k)) {
        List<ImmutableIntPair> sortedJobPairs =
            machineJobs.get(k).stream()
                .sorted(Comparator.comparing(o -> C[o.getFirst()]))
                .collect(Collectors.toList());

        ImmutableIntPair lastPair = null;
        for (ImmutableIntPair pair : sortedJobPairs) {
          if (lastPair == null) {
            if (pair.getFirst() != 0) {
              log.warn(
                  "First job of Machine " + k + " should be dummy job, but is job " +
                      pair.getFirst());
              valid = false;
            }
          } else {
            if (lastPair.getSecond() != pair.getFirst()) {
              log.warn("Job sequence of machine " + k + " incorrect!");
              valid = false;
            }
          }

          if (!(C[pair.getFirst()]
              + constants.get_s_ijk(pair.getFirst(), pair.getSecond(), k)
              + constants.get_p_jk(pair.getSecond(), k)
              <= C[pair.getSecond()] + EPSILON)) {
            log.warn("CompletionTimes of jobs " + pair.getFirst() + "->" + pair.getSecond() + ": "
                + C[pair.getFirst()] + "->" + C[pair.getSecond()] +
                " suggest overlap, considering setup/processtime: "
                + constants.get_s_ijk(pair.getFirst(), pair.getSecond(), k) + "/"
                + constants.get_p_jk(pair.getSecond(), k));
            valid = false;
          }

          lastPair = pair;
        }
      }
    }

    return valid;
  }
}
