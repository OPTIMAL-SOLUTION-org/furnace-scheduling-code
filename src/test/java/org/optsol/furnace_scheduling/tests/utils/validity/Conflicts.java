/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.tests.utils.validity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.optsol.furnace_scheduling.model.constants.IFurnaceScheduling_Constants;
import org.optsol.furnace_scheduling.solver.IFurnaceScheduling_Solution;
import org.optsol.furnace_scheduling.utils.ImmutableIntPair;

@Slf4j
public class Conflicts implements IValidityChecks {

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

  private static Map<Integer, Integer> getJobMachineAssignments(IFurnaceScheduling_Solution solution) {
    Map<Integer, Set<ImmutableIntPair>> machineJobs =
        getMachineJobAssignmentsMap(solution.get_x());

    Map<Integer, Integer> jobMachines = new HashMap<>();

    for (int k : machineJobs.keySet()) {
      for (ImmutableIntPair pair : machineJobs.get(k)) {
        if (jobMachines.containsKey(pair.getSecond())) {
          throw new Error("Unexpected job-machine assignments!");
        }
        jobMachines.put(pair.getSecond(), k);
      }
    }

    return jobMachines;
  }

  @Override
  public boolean checkValid(
      IFurnaceScheduling_Constants constants,
      IFurnaceScheduling_Solution solution) {

    boolean valid = true;

    Map<Integer, Integer> jobMachineAssignments = getJobMachineAssignments(solution);

    Boolean[][] z = solution.get_z();
    Double[] C = solution.get_C();

    for (ImmutableIntPair j_con : constants.get_J_con()) {
      int j = j_con.getFirst();
      int h = j_con.getSecond();

      int k = jobMachineAssignments.get(j);
      int l = jobMachineAssignments.get(h);

      if (constants.get_M_con().stream()
          .anyMatch(m_con -> m_con.getFirst() == k && m_con.getSecond() == l)) {
        //CONFLICTING ASSIGNMENT
        if (z[h][j] && !z[j][h]) {
          //CHECK NO OVERLAP OF PROCESSING TIME
          if (!(C[h] <= C[j] - constants.get_p_jk(j, k) + EPSILON)) {
            log.warn(
                "job pair " + j + "<->" + h + " conflicting on machines " + k + "/" + l);
            valid = false;
          }
        } else if (!z[h][j] && z[j][h]) {
          //CHECK NO OVERLAP OF PROCESSING TIME
          if (!(C[j] <= C[h] - constants.get_p_jk(h, l) + EPSILON)) {
            log.warn(
                "job pair " + j + "<->" + h + " conflicting on machines " + k + "/" + l);
            valid = false;
          }
        } else {
          log.warn(
              "z variables for job pair " + j + "<->" + h +
                  " does not indicate correctly existing conflict!");
          valid = false;
        }
      } else {
        //NON-CONFLICTING ASSIGNMENT
        /*
        if (z[h][j] || z[j][h]) {
          log.warn(
              "z variables for job pair " + j + "<->" + h + " indicate non-existing conflict!");
          valid = false;
        }*/
      }
    }


    return valid;
  }
}
