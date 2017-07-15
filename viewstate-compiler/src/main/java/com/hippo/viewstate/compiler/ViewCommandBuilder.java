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

import com.hippo.viewstate.strategy.StrategyType;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;

public class ViewCommandBuilder {

  public final String name;
  public final List<Parameter> parameters;
  public final String tag;
  public final String strategy;

  public ViewCommandBuilder(String name, List<Parameter> parameters, String tag, String strategy) {
    this.name = name;
    this.parameters = parameters;
    this.tag = tag;
    this.strategy = strategy;
  }

  public void append(StringBuilder sb, String viewName, ViewCommandSet commandSet, StrategySet strategies) {
    boolean hasStrategy = tag != null && strategy != null;

    String commandName = commandSet.requireCommandName(name);

    // Method header
    sb.append("  @Override").append("\n");
    sb.append("  public void ").append(name).append("(");
    int i = 0;
    for (Parameter parameter : parameters) {
      if (i++ != 0) {
        sb.append(", ");
      }
      sb.append(parameter.type).append(" ").append(parameter.name);
    }
    sb.append(") {").append("\n");

    // Method body
    if (hasStrategy) {
      sb.append("    execute(new ").append(commandName).append("(");
      i = 0;
      for (Parameter parameter : parameters) {
        if (i++ != 0) {
          sb.append(", ");
        }
        sb.append(parameter.name);
      }
      sb.append("));").append("\n");
    } else {
      sb.append("    if (getView() != null) {").append("\n");
      sb.append("      getView().").append(name).append("(");
      i = 0;
      for (Parameter parameter : parameters) {
        if (i++ != 0) {
          sb.append(", ");
        }
        sb.append(parameter.name);
      }
      sb.append(");").append("\n");
      sb.append("    }").append("\n");
    }

    // Method footer
    sb.append("  }").append("\n");
    sb.append("\n");


    if (hasStrategy) {
      // Command class header
      sb.append("  private static class ").append(commandName)
          .append(" extends com.hippo.viewstate.ViewCommand<").append(viewName).append("> {")
          .append("\n");
      sb.append("\n");

      // Command fields
      if (!parameters.isEmpty()) {
        for (Parameter parameter : parameters) {
          sb.append("    private final ").append(parameter.type).append(" ").append(parameter.name).append(";").append("\n");
        }
        sb.append("\n");
      }

      // Command construction header
      sb.append("    public ").append(commandName).append("(");
      i = 0;
      for (Parameter parameter : parameters) {
        if (i++ != 0) {
          sb.append(", ");
        }
        sb.append(parameter.type).append(" ").append(parameter.name);
      }
      sb.append(") {").append("\n");

      // Command construction body
      sb.append("      super(\"").append(tag).append("\", ").append(strategies.requireStrategy(strategy)).append(");").append("\n");
      for (Parameter parameter : parameters) {
        sb.append("      this.").append(parameter.name).append(" = ").append(parameter.name).append(";").append("\n");
      }

      // Command construction footer
      sb.append("    }").append("\n");
      sb.append("\n");

      // Command method
      sb.append("    @Override").append("\n");
      sb.append("    public void execute(").append(viewName).append(" view) {").append("\n");
      sb.append("      view.").append(name).append("(");
      i = 0;
      for (Parameter parameter : parameters) {
        if (i++ != 0) {
          sb.append(", ");
        }
        sb.append(parameter.name);
      }
      sb.append(");").append("\n");
      sb.append("    }").append("\n");

      // Command class footer
      sb.append("  }").append("\n");
      sb.append("\n");
    }
  }

  public static ViewCommandBuilder build(ExecutableElement element) {
    String name = element.getSimpleName().toString();

    List<Parameter> parameters = new ArrayList<>();
    for (VariableElement var : element.getParameters()) {
      Parameter parameter = new Parameter();
      parameter.type = var.asType().toString();
      parameter.name = var.getSimpleName().toString();
      parameters.add(parameter);
    }

    String tag;
    String strategy;
    StrategyType strategyType = element.getAnnotation(StrategyType.class);
    if (strategyType != null) {
      tag = strategyType.tag();
      try {
        strategyType.value();
        throw new ViewStateException("Can't get value of StrategyType");
      } catch (MirroredTypeException e) {
        strategy = e.getTypeMirror().toString();
      }
    } else {
      tag = null;
      strategy = null;
    }

    return new ViewCommandBuilder(name, parameters, tag, strategy);
  }

  private static class Parameter {
    public String type;
    public String name;
  }
}
