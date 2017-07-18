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

package com.hippo.viewstate.strategy;

/*
 * Created by Hippo on 2017/7/13.
 */

import com.hippo.viewstate.ViewCommand;
import java.util.List;

/**
 * Handles the view command.
 */
public interface Strategy {

  /**
   * Called when the view command is added to a view state.
   */
  <T> void onAppend(List<ViewCommand<T>> commands, ViewCommand<T> command);

  /**
   * Called when the view command executes on a actual view.
   */
  <T> void onExecute(List<ViewCommand<T>> commands, ViewCommand<T> command);
}
