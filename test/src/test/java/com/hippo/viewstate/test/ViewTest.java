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
  public void testSingleByTag1() {
    TestViewState state = new TestViewState();

    state.singleByTag1();
    state.singleByTag1("1");

    TestViewImpl view1 = new TestViewImpl();
    state.attach(view1);
    assertEquals(0, view1.singleByTag10);
    assertEquals(1, view1.singleByTag11);
    assertEquals(0, view1.singleByTag12);

    state.singleByTag1("2", 2);
    assertEquals(0, view1.singleByTag10);
    assertEquals(1, view1.singleByTag11);
    assertEquals(1, view1.singleByTag12);

    state.detach();
    TestViewImpl view2 = new TestViewImpl();
    state.attach(view2);
    assertEquals(0, view2.singleByTag10);
    assertEquals(0, view2.singleByTag11);
    assertEquals(1, view2.singleByTag12);

    state.detach();
    state.singleByTag1();
    TestViewImpl view3 = new TestViewImpl();
    state.attach(view3);
    assertEquals(1, view3.singleByTag10);
    assertEquals(0, view3.singleByTag11);
    assertEquals(0, view3.singleByTag12);
  }

  @Test
  public void testSingleByTag2() {
    TestViewState state = new TestViewState();

    state.singleByTag1();
    state.singleByTag2();

    TestViewImpl view1 = new TestViewImpl();
    state.attach(view1);
    assertEquals(1, view1.singleByTag10);
    assertEquals(0, view1.singleByTag11);
    assertEquals(0, view1.singleByTag12);

    state.singleByTag2();
    assertEquals(1, view1.singleByTag10);
    assertEquals(0, view1.singleByTag11);
    assertEquals(0, view1.singleByTag12);
  }

  @Test
  public void testSkip() {
    TestViewState state = new TestViewState();

    state.skip();
    state.skip("1");

    TestViewImpl view1 = new TestViewImpl();
    state.attach(view1);
    assertEquals(0, view1.skip0);
    assertEquals(0, view1.skip1);
    assertEquals(0, view1.skip2);
  }

  public static final class TestViewImpl implements TestView {

    public int singleByTag10 = 0;
    public int singleByTag11 = 0;
    public int singleByTag12 = 0;
    public int singleByTag13 = 0;

    public int singleByTag20 = 0;

    public int skip0 = 0;
    public int skip1 = 0;
    public int skip2 = 0;

    @Override
    public void singleByTag1() {
      singleByTag10++;
    }

    @Override
    public void singleByTag1(String arg1) {
      singleByTag11++;
    }

    @Override
    public void singleByTag1(String arg1, int arg2) {
      singleByTag12++;
    }

    @Override
    public void singleByTag1(String arg1, int arg2, float... arg3) {
      singleByTag13++;
    }

    @Override
    public void singleByTag2() {
      singleByTag20++;
    }

    @Override
    public void skip() {
      skip0++;
    }

    @Override
    public void skip(String arg1) {
      skip1++;
    }

    @Override
    public void skip(String arg1, int arg2) {
      skip2++;
    }
  }
}
