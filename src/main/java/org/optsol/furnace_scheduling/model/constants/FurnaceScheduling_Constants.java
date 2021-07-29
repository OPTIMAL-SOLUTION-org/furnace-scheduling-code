/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.model.constants;

import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.optsol.furnace_scheduling.utils.ImmutableIntPair;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FurnaceScheduling_Constants implements IFurnaceScheduling_Constants {

  private int _n;
  private Set<ImmutableIntPair> _J_con;
  private int _m;
  private Set<ImmutableIntPair> _M_con;
  private Map<Integer, Set<Integer>> _M_j;
  private Map<Integer, Map<Integer, Double>> _p_jk;
  private Map<Integer, Map<Integer, Map<Integer, Double>>> _s_ijk;
  private double _V;
  private Set<ImmutableIntPair> _M_sym;
}
