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

/**
 * Wraps a method of view to a class.
 */
public abstract class ViewCommand<T> {

  private final String tag;
  private final Strategy strategy;

  public ViewCommand(String tag, Strategy strategy) {
    this.tag = tag;
    this.strategy = strategy;
  }

  public String getTag() {
    return tag;
  }

  Strategy getStrategy() {
    return strategy;
  }

  public abstract void execute(T t);
}
