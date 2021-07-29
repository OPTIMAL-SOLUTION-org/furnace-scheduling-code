/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.solver;

import org.optsol.furnace_scheduling.solver.vars.IVar_C;
import org.optsol.furnace_scheduling.solver.vars.IVar_x;
import org.optsol.furnace_scheduling.solver.vars.IVar_z;
import org.optsol.jdecor.core.ISolution;

public interface IFurnaceScheduling_Solution
    extends ISolution,
            IVar_x,
            IVar_z,
            IVar_C {
}
