/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wickettree.provider;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.model.IModel;

import wickettree.ITreeProvider;

/**
 * A subset of a {@link ITreeProvider}s tree.
 * 
 * @author Sven Meier
 */
public class ProviderSubset<T> implements IModel<Set<T>>
{

	private static final long serialVersionUID = 1L;

	private ITreeProvider<T> provider;

	private Set<IModel<T>> models = new HashSet<IModel<T>>();

	private Set<T> set = new SetImpl();

	public ProviderSubset(ITreeProvider<T> provider)
	{
		this(provider, false);
	}

	public ProviderSubset(ITreeProvider<T> provider, boolean addRoots)
	{
		this.provider = provider;

		if (addRoots)
		{
			Iterator<T> roots = provider.getRoots();
			while (roots.hasNext())
			{
				getObject().add(roots.next());
			}
		}
	}

	public Set<T> getObject()
	{
		return set;
	}

	public void setObject(Set<T> object)
	{
		throw new UnsupportedOperationException();
	}

	public void detach()
	{
		for (IModel<T> model : models)
		{
			model.detach();
		}
	}

	@SuppressWarnings("unchecked")
	private IModel<T> model(Object o)
	{
		return provider.model((T)o);
	}

	private class SetImpl implements Set<T>, Serializable
	{

		private static final long serialVersionUID = 1L;

		public int size()
		{
			return models.size();
		}

		public boolean isEmpty()
		{
			return models.size() == 0;
		}

		public void clear()
		{
			models.clear();
		}

		@SuppressWarnings("unchecked")
		public boolean contains(Object o)
		{
			return models.contains(model(o));
		}

		public boolean add(T t)
		{
			return models.add(model(t));
		}

		@SuppressWarnings("unchecked")
		public boolean remove(Object o)
		{
			return models.remove(model(o));
		}

		public Iterator<T> iterator()
		{
			throw new UnsupportedOperationException();
		}

		public boolean addAll(Collection<? extends T> ts)
		{
			for (T t : ts)
			{
				add(t);
			}
			return true;
		}

		@SuppressWarnings("unchecked")
		public boolean containsAll(Collection<?> cs)
		{
			for (Object c : cs)
			{
				if (!models.contains(model(c)))
				{
					return false;
				}
			}
			return true;
		}

		@SuppressWarnings("unchecked")
		public boolean removeAll(Collection<?> cs)
		{
			for (Object c : cs)
			{
				models.remove(model(c));
			}
			return true;
		}

		public boolean retainAll(Collection<?> c)
		{
			throw new UnsupportedOperationException();
		}

		public Object[] toArray()
		{
			throw new UnsupportedOperationException();
		}

		public <S> S[] toArray(S[] a)
		{
			throw new UnsupportedOperationException();
		}
	}
}
