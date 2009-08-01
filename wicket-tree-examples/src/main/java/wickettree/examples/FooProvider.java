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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import wickettree.ITreeProvider;

/**
 * A provider of {@link Foo}s.
 * 
 * For simplicity all foos are kept as class members, in a real world scenario
 * these would be fetched from a database. If {@link Foo}s were
 * {@link Serializable} you could of course just keep references in instance
 * variables.
 * 
 * @see #model(Foo)
 * 
 * @author Sven Meier
 */
public class FooProvider implements ITreeProvider<Foo>
{

	private static final long serialVersionUID = 1L;

	/**
	 * All root {@link Foo}s.
	 */
	private static List<Foo> roots = new ArrayList<Foo>();

	/**
	 * Initialize roots.
	 */
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
		roots.add(fooA);

		Foo fooB = new Foo("B");
		{
			new Foo(fooB, "BA");
			new Foo(fooB, "BB");
		}
		roots.add(fooB);

		Foo fooC = new Foo("C");
		roots.add(fooC);
	}

	/**
	 * Nothing to do.
	 */
	public void detach()
	{
	}

	public Iterator<Foo> getRoots()
	{
		return roots.iterator();
	}

	public boolean hasChildren(Foo foo)
	{
		return foo.getParent() == null || !foo.getFoos().isEmpty();
	}

	public Iterator<Foo> getChildren(Foo foo)
	{
		return foo.getFoos().iterator();
	}

	/**
	 * Creates a {@link FooModel}.
	 */
	public IModel<Foo> model(Foo foo)
	{
		return new FooModel(foo);
	}

	/**
	 * Get a {@link Foo} by its id.
	 */
	public static Foo get(String id)
	{
		return get(roots, id);
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

	/**
	 * A {@link Model} which uses an id to load its {@link Foo}.
	 * 
	 * If {@link Foo}s were {@link Serializable} you could just use a standard
	 * {@link Model}.
	 * 
	 * @see #equals(Object)
	 * @see #hashCode()
	 */
	private static class FooModel extends LoadableDetachableModel<Foo>
	{
		private static final long serialVersionUID = 1L;

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

		/**
		 * Important! Models must be identifyable by their contained object.
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (obj instanceof FooModel)
			{
				return ((FooModel)obj).id.equals(this.id);
			}
			return false;
		}

		/**
		 * Important! Models must be identifyable by their contained object.
		 */
		@Override
		public int hashCode()
		{
			return id.hashCode();
		}
	}
}
