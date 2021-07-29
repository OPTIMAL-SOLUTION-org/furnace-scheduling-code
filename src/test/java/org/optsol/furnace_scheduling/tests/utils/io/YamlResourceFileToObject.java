/*
 * @author Fath, Philipp
 * @author Sayah, David
 */

package org.optsol.furnace_scheduling.tests.utils.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.FileReader;
import java.io.IOException;

public class YamlResourceFileToObject<T> {

  public T read(
      Class<T> clazz,
      String resourceFilePathAndName) {
    try {
      return
          new ObjectMapper(new YAMLFactory()).readValue(
              new FileReader(
                  getClass().getClassLoader().getResource(resourceFilePathAndName).getFile()),
              clazz);
    } catch (IOException e) {
      throw new Error(e.getMessage());
    }
  }
}
