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

package com.hippo.viewstate.test;

/*
 * Created by Hippo on 2017/7/13.
 */

import com.hippo.viewstate.GenerateViewState;
import com.hippo.viewstate.strategy.SingleByTag;
import com.hippo.viewstate.strategy.StrategyType;

@GenerateViewState
public interface TestView {

  String TAG_TEST = "test";

  @StrategyType(value = SingleByTag.class, tag = TAG_TEST)
  void test();

  @StrategyType(value = SingleByTag.class, tag = TAG_TEST)
  void test(String arg1);

  @StrategyType(value = SingleByTag.class, tag = TAG_TEST)
  void test(String arg1, int arg2);

  @StrategyType(value = SingleByTag.class, tag = TAG_TEST)
  void test(String arg1, int arg2, float... arg3);

  void skip();

  void skip(String arg1);

  void skip(String arg1, int arg2);
}
