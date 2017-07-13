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
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

public class ViewStateProcessor extends AbstractProcessor {

  private Filer filer;

  @Override
  public synchronized void init(ProcessingEnvironment env) {
    super.init(env);
    filer = env.getFiler();
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> set = new HashSet<>();
    set.add(StrategyType.class.getName());
    return set;
  }

  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
    StrategySet strategies = new StrategySet();

    Map<String, ViewStateBuilder> states = parse(roundEnvironment);

    for (ViewStateBuilder state : states.values()) {
      try {
        JavaFileObject file = filer.createSourceFile(state.filename());
        Writer writer = file.openWriter();
        writer.write(state.javaSource(strategies));
        writer.close();
      } catch (IOException e) {
        throw new RuntimeException("Can't write java source file: " + state.filename(), e);
      }
    }

    if (!strategies.isEmpty()) {
      try {
        JavaFileObject file = filer.createSourceFile(strategies.filename());
        Writer writer = file.openWriter();
        writer.write(strategies.javaSource());
        writer.close();
      } catch (IOException e) {
        throw new RuntimeException("Can't write java source file: " + strategies.filename(), e);
      }
    }

    return false;
  }

  private Map<String, ViewStateBuilder> parse(RoundEnvironment roundEnvironment) {
    Map<String, ViewStateBuilder> viewStateMap = new HashMap<>();

    for (Element element : roundEnvironment.getElementsAnnotatedWith(StrategyType.class)) {
      if (element instanceof ExecutableElement && element.getKind() == ElementKind.METHOD) {
        parseCommand((ExecutableElement) element, viewStateMap);
      }
    }

    return viewStateMap;
  }

  private ViewStateBuilder findViewState(Element element, Map<String, ViewStateBuilder> stateMap) {
    Element enclosing = element.getEnclosingElement();

    if (enclosing instanceof TypeElement) {
      TypeElement typeElement = (TypeElement) enclosing;
      String name = typeElement.getQualifiedName().toString();

      ViewStateBuilder state = stateMap.get(name);
      if (state == null) {
        state = ViewStateBuilder.build(typeElement);
        stateMap.put(name, state);
      }

      return state;
    }

    return null;
  }

  private void parseCommand(ExecutableElement element, Map<String, ViewStateBuilder> stateMap) {
    ViewStateBuilder state = findViewState(element, stateMap);
    if (state != null) {
      state.addCommand(ViewCommandBuilder.build(element));
    }
  }
}
