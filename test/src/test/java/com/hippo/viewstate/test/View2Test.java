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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class View2Test {

  @Test
  public void testSingleByTag2() {
    TestView2State<TestView2> state = new TestView2State<>();

    state.singleByTag2();
    state.singleByTag2("1");

    TestView2Impl view1 = new TestView2Impl();
    state.attach(view1);
    assertEquals(0, view1.singleByTag20);
    assertEquals(1, view1.singleByTag21);

    state.singleByTag2();
    assertEquals(1, view1.singleByTag20);
    assertEquals(1, view1.singleByTag21);

    state.detach();
    TestView2Impl view2 = new TestView2Impl();
    state.attach(view2);
    assertEquals(1, view2.singleByTag20);
    assertEquals(0, view2.singleByTag21);

    state.detach();
    state.singleByTag2("1");
    TestView2Impl view3 = new TestView2Impl();
    state.attach(view3);
    assertEquals(0, view3.singleByTag20);
    assertEquals(1, view3.singleByTag21);
  }

  public static class TestView2Impl extends ViewTest.TestViewImpl implements TestView2 {

    public int singleByTag21 = 0;

    @Override
    public void singleByTag2(String arg1) {
      singleByTag21++;
    }
  }
}
