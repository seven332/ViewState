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
import javax.lang.model.element.TypeElement;

public class ViewStateBuilder {

  public final String name;
  public final List<ViewCommandBuilder> commands;

  public ViewStateBuilder(String name) {
    this.name = name;
    this.commands = new ArrayList<>();
  }

  public void addCommand(ViewCommandBuilder command) {
    commands.add(command);
  }

  public String filename() {
    return name + "State";
  }

  public String javaSource(StrategySet strategies) {
    StringBuilder sb = new StringBuilder();

    // Package
    String packageName = substringBeforeLast(name, '.');
    sb.append("package ").append(packageName).append(";").append("\n");
    sb.append("\n");

    // Class
    String className = substringAfterLast(name, '.') + "State";
    sb.append("public final class ").append(className)
        .append(" extends com.hippo.viewstate.ViewState<").append(name)
        .append("> implements ").append(name).append(" {").append("\n");
    sb.append("\n");

    // Commands
    ViewCommandSet commandSet = new ViewCommandSet();
    for (ViewCommandBuilder command : commands) {
      command.append(sb, name, commandSet, strategies);
    }

    sb.append("}");

    return sb.toString();
  }

  private static String substringBeforeLast(String str, char ch) {
    return str.substring(0, str.lastIndexOf(ch));
  }

  private static String substringAfterLast(String str, char ch) {
    return str.substring(str.lastIndexOf(ch) + 1);
  }

  public static ViewStateBuilder build(TypeElement element) {
    // Check naked interface
    String name = element.getQualifiedName().toString();
    if (!name.contains(".")) {
      throw new ViewStateException("No support for naked interface");
    }

    return new ViewStateBuilder(name);
  }
}
