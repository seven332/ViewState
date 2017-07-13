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

import java.util.ArrayList;
import java.util.List;

public class StrategySet {

  private List<String> strategies = new ArrayList<>();

  public boolean isEmpty() {
    return strategies.isEmpty();
  }

  public String filename() {
    return "com.hippo.viewstate.strategy.Strategies";
  }

  public String javaSource() {
    StringBuilder sb = new StringBuilder();

    sb.append("package com.hippo.viewstate.strategy;").append("\n");
    sb.append("\n");
    sb.append("public final class Strategies {").append("\n");
    sb.append("  private Strategies() {}").append("\n");
    int i = 0;
    for (String strategy: strategies) {
      sb.append("  public static final Strategy STRATEGY_").append(i++).append(" = new ").append(strategy).append("();").append("\n");
    }
    sb.append("}").append("\n");

    return sb.toString();
  }

  public String requireStrategy(String strategy) {
    int index = strategies.indexOf(strategy);
    if (index == -1) {
      strategies.add(strategy);
      index = strategies.size() - 1;
    }
    return "com.hippo.viewstate.strategy.Strategies.STRATEGY_" + index;
  }
}
