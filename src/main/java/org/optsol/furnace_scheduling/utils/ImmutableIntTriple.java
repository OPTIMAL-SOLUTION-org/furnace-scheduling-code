/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public final class ImmutableIntTriple {
  private int first;
  private int second;
  private int third;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ImmutableIntTriple that = (ImmutableIntTriple) o;

    if (first != that.first) {
      return false;
    }
    if (second != that.second) {
      return false;
    }
    return third == that.third;
  }

  @Override
  public int hashCode() {
    int result = first;
    result = 31 * result + second;
    result = 31 * result + third;
    return result;
  }
}
