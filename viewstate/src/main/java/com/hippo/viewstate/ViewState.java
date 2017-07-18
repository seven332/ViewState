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

package com.hippo.viewstate;

/*
 * Created by Hippo on 2017/7/13.
 */

import com.hippo.viewstate.strategy.Strategy;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for ViewState.
 */
public abstract class ViewState<T> {

  private T view;
  private List<ViewCommand<T>> commands = new ArrayList<>();
  private boolean isRestoring;

  /**
   * Attaches a view. Only one view can be attached. The view must be clean.
   */
  public final void attach(T view) {
    if (view == null) {
      throw new NullPointerException("view == null");
    }
    if (this.view != null) {
      throw new IllegalStateException("This ViewState is already attached");
    }
    this.view = view;

    isRestoring = true;
    for (ViewCommand<T> command : new ArrayList<>(commands)) {
      command.getStrategy().onExecute(commands, command);
      command.execute(view);
    }
    isRestoring = false;
  }

  /**
   * Detach the view. The view should not be attached again.
   */
  public final void detach() {
    this.view = null;
  }

  /**
   * Returns the attached view.
   */
  public final T getView() {
    return view;
  }

  /**
   * Returns {@code true} if the ViewState is under restoring.
   */
  public final boolean isRestoring() {
    return isRestoring;
  }

  protected final void execute(ViewCommand<T> command) {
    Strategy strategy = command.getStrategy();
    strategy.onAppend(commands, command);

    if (view != null) {
      strategy.onExecute(commands, command);
      command.execute(view);
    }
  }
}
