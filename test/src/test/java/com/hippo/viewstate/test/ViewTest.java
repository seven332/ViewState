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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ViewTest {

  @Test
  public void testAttachAndDetach() {
    TestViewState state = new TestViewState();

    state.test();
    state.test("1");

    TestViewImpl view1 = new TestViewImpl();
    state.attach(view1);
    assertEquals(0, view1.test0);
    assertEquals(1, view1.test1);
    assertEquals(0, view1.test2);

    state.test("2", 2);
    assertEquals(0, view1.test0);
    assertEquals(1, view1.test1);
    assertEquals(1, view1.test2);

    state.detach();
    TestViewImpl view2 = new TestViewImpl();
    state.attach(view2);
    assertEquals(0, view2.test0);
    assertEquals(0, view2.test1);
    assertEquals(1, view2.test2);

    state.detach();
    state.test();
    TestViewImpl view3 = new TestViewImpl();
    state.attach(view3);
    assertEquals(1, view3.test0);
    assertEquals(0, view3.test1);
    assertEquals(0, view3.test2);
  }

  public static final class TestViewImpl implements TestView {

    public int test0 = 0;
    public int test1 = 0;
    public int test2 = 0;
    public int test3 = 0;

    @Override
    public void test() {
      test0++;
    }

    @Override
    public void test(String arg1) {
      test1++;
    }

    @Override
    public void test(String arg1, int arg2) {
      test2++;
    }

    @Override
    public void test(String arg1, int arg2, float... arg3) {
      test3++;
    }
  }
}
