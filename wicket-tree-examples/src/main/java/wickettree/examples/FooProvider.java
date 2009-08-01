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
package wickettree.examples;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import wickettree.ITreeProvider;

/**
 * @author Sven Meier
 */
public class FooProvider implements ITreeProvider<Foo>
{

	private static final long serialVersionUID = 1L;

	private static List<Foo> foos = new ArrayList<Foo>();

	static
	{
		Foo fooA = new Foo("A");
		{
			Foo fooAA = new Foo(fooA, "AA");
			{
				new Foo(fooAA, "AAA");
				new Foo(fooAA, "AAB");
			}
			Foo fooAB = new Foo(fooA, "AB");
			{
				new Foo(fooAB, "ABA");
				Foo fooABB = new Foo(fooAB, "ABB");
				{
					new Foo(fooABB, "ABBA");
					Foo fooABBB = new Foo(fooABB, "ABBB");
					{
						new Foo(fooABBB, "ABBBA");
					}
				}
				new Foo(fooAB, "ABC");
				new Foo(fooAB, "ABD");
			}
			Foo fooAC = new Foo(fooA, "AC");
			{
				new Foo(fooAC, "ACA");
				new Foo(fooAC, "ACB");
			}
		}
		foos.add(fooA);

		Foo fooB = new Foo("B");
		{
			new Foo(fooB, "BA");
			new Foo(fooB, "BB");
		}
		foos.add(fooB);

		Foo fooC = new Foo("C");
		foos.add(fooC);
	}

	public void detach()
	{
	}

	public Iterator<Foo> getRoots()
	{
		return foos.iterator();
	}

	public boolean hasChildren(Foo foo)
	{
		return foo.getParent() == null || !foo.getFoos().isEmpty();
	}

	public Iterator<Foo> getChildren(Foo foo)
	{
		return foo.getFoos().iterator();
	}

	public IModel<Foo> model(Foo foo)
	{
		return new FooModel(foo);
	}

	public static Foo get(String id)
	{
		return get(foos, id);
	}

	private static Foo get(List<Foo> foos, String id)
	{
		for (Foo foo : foos)
		{
			if (foo.getId().equals(id))
			{
				return foo;
			}

			Foo temp = get(foo.getFoos(), id);
			if (temp != null)
			{
				return temp;
			}
		}

		return null;
	}

	private static class FooModel extends LoadableDetachableModel<Foo>
	{

		private String id;

		public FooModel(Foo foo)
		{
			super(foo);
			
			id = foo.getId();
		}

		@Override
		protected Foo load()
		{
			return get(id);
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (obj instanceof FooModel) {
				return ((FooModel)obj).id == this.id; 
			}
			return super.equals(obj);
		}
		
		@Override
		public int hashCode()
		{
			return id.hashCode();
		}
	}
}
