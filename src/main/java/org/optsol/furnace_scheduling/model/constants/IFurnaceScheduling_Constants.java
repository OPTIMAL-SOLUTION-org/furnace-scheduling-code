/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.model.constants;

import org.optsol.furnace_scheduling.model.constants.sub.IConstants_J;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_J_con;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_M;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_M_con;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_M_j;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_M_sym;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_V;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_p_jk;
import org.optsol.furnace_scheduling.model.constants.sub.IConstants_s_ijk;

public interface IFurnaceScheduling_Constants
    extends IConstants_J,
            IConstants_J_con,
            IConstants_M,
            IConstants_M_con,
            IConstants_M_j,
            IConstants_p_jk,
            IConstants_s_ijk,
            IConstants_V,
            IConstants_M_sym {

}
