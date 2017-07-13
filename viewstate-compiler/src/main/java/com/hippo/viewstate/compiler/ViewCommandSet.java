/*
 * Copyright 2017 Hippo Seven
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hippo.viewstate.compiler;

/*
 * Created by Hippo on 2017/7/13.
 */

import java.util.HashMap;
import java.util.Map;

public class ViewCommandSet {

  private Map<String, Integer> map = new HashMap<>();

  private static String commandName(String name) {
    return Character.toUpperCase(name.charAt(0)) + name.substring(1) + "Command";
  }

  /**
   * Ask for class name of a ViewCommand class.
   */
  public String requireCommandName(String name) {
    String commandName = commandName(name);

    Integer count = map.get(name);
    if (count == null) {
      count = 1;
    } else {
      count += 1;
      commandName = commandName + count;
    }
    map.put(name, count);

    return commandName;
  }
}
