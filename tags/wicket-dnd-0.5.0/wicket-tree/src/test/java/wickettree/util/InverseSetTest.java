/*
 * Copyright 2009 Sven Meier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wickettree.util;

import java.util.HashSet;

import junit.framework.TestCase;

import org.apache.wicket.model.IDetachable;

/**
 * Test for {@link InverseSet}.
 * 
 * @author Sven Meier
 */
public class InverseSetTest extends TestCase
{
	private TestSet set;

	@Override
	protected void setUp() throws Exception
	{
		set = new TestSet();
		set.add("A");
	}

	public void test() throws Exception
	{
		InverseSet<String> inverse = new InverseSet<String>(set);
		assertFalse(inverse.contains("A"));
		assertTrue(inverse.contains("B"));

		inverse.remove("B");
		assertFalse(inverse.contains("A"));
		assertFalse(inverse.contains("B"));

		inverse.add("A");
		assertTrue(inverse.contains("A"));
		assertFalse(inverse.contains("B"));

		inverse.detach();
		assertTrue(set.detached);
	}

	private class TestSet extends HashSet<String> implements IDetachable
	{
		boolean detached = false;

		public void detach()
		{
			detached = true;
		}
	}
}