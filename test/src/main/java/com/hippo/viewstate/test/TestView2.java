/*
 * Copyright 2018 Hippo Seven
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

package com.hippo.viewstate.test;

import com.hippo.viewstate.GenerateViewState;
import com.hippo.viewstate.strategy.SingleByTag;
import com.hippo.viewstate.strategy.StrategyType;

@GenerateViewState
public interface TestView2 extends TestView {

  @StrategyType(value = SingleByTag.class, tag = TAG_SINGLE_BY_TAG_2)
  void singleByTag2(String arg1);
}
