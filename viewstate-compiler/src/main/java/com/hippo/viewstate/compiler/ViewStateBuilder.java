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
import java.util.Set;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public class ViewStateBuilder {

  public final String name;
  public final String intfName;
  public final List<ViewCommandBuilder> commands;

  public ViewStateBuilder(String name, String intfName) {
    this.name = name;
    this.intfName = intfName;
    this.commands = new ArrayList<>();
  }

  public void addCommand(ViewCommandBuilder command) {
    commands.add(command);
  }

  public void setUpInterface(Set<ViewStateBuilder> viewStateSet) {
    if (intfName == null) return;

    ViewStateBuilder intf = null;
    for (ViewStateBuilder viewState : viewStateSet) {
      if (viewState.name.equals(intfName)) {
        intf = viewState;
        break;
      }
    }

    if (intf == null) {
      throw new ViewStateException(intfName + " must be decorated by GenerateViewState");
    }

    for (ViewCommandBuilder command1 : commands) {
      for (ViewCommandBuilder command2 : intf.commands) {
        if (command1.isTheSame(command2)) {
          throw new ViewStateException("Same method in sub-interface is not allowed");
        }
      }
    }
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
    String superClassName = intfName == null
        ? "com.hippo.viewstate.ViewState"
        : intfName + "State";
    sb.append("public class ").append(className).append("<T extends ").append(name).append(">")
        .append(" extends ").append(superClassName).append("<").append("T")
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

    String intfName;
    List<? extends TypeMirror> interfaces = element.getInterfaces();
    if (interfaces.size() == 0) {
      intfName = null;
    } else if (interfaces.size() == 1) {
      intfName = interfaces.get(0).toString();
    } else {
      throw new ViewStateException("No support for multi-interface");
    }

    return new ViewStateBuilder(name, intfName);
  }
}
