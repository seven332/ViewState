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

import com.hippo.viewstate.GenerateViewState;
import com.hippo.viewstate.strategy.StrategyType;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
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
    set.add(GenerateViewState.class.getName());
    set.add(StrategyType.class.getName());
    return set;
  }

  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
    StrategySet strategies = new StrategySet();

    Set<ViewStateBuilder> states = parse(roundEnvironment);

    for (ViewStateBuilder state : states) {
      try {
        JavaFileObject file = filer.createSourceFile(state.filename());
        Writer writer = file.openWriter();
        writer.write(state.javaSource(strategies));
        writer.close();
      } catch (IOException e) {
        throw new ViewStateException("Can't write java source file: " + state.filename(), e);
      }
    }

    if (!strategies.isEmpty()) {
      try {
        JavaFileObject file = filer.createSourceFile(strategies.filename());
        Writer writer = file.openWriter();
        writer.write(strategies.javaSource());
        writer.close();
      } catch (IOException e) {
        throw new ViewStateException("Can't write java source file: " + strategies.filename(), e);
      }
    }

    return false;
  }

  private Set<ViewStateBuilder> parse(RoundEnvironment roundEnvironment) {
    Set<ViewStateBuilder> viewStateSet = new HashSet<>();

    for (Element element : roundEnvironment.getElementsAnnotatedWith(GenerateViewState.class)) {
      if (element.getKind() != ElementKind.INTERFACE) {
        throw new ViewStateException("GenerateViewState can only describe interface");
      }
      if (element instanceof TypeElement) {
        viewStateSet.add(parseView((TypeElement) element));
      }
    }

    return viewStateSet;
  }

  private ViewStateBuilder parseView(TypeElement view) {
    ViewStateBuilder state = ViewStateBuilder.build(view);

    for (Element element : view.getEnclosedElements()) {
      if (element.getKind() == ElementKind.METHOD &&
          !element.getModifiers().contains(Modifier.STATIC) &&
          element instanceof ExecutableElement) {

        ExecutableElement method = (ExecutableElement) element;

        // Check return type, must be void
        String returnType = method.getReturnType().toString();
        if (!"void".equals(returnType)) {
          throw new ViewStateException("Return type of view method must be void, but it's " + returnType);
        }

        state.addCommand(ViewCommandBuilder.build(method));
      }
    }

    return state;
  }
}
