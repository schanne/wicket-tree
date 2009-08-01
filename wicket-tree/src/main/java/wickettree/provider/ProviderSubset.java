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
package wickettree.provider;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

import wickettree.ITreeProvider;

/**
 * A subset of a {@link ITreeProvider}'s tree offering automatic detachment.
 * 
 * @see ITreeProvider#model(Object)
 * 
 * @author Sven Meier
 */
public class ProviderSubset<T> implements Set<T>, IDetachable
{

	private static final long serialVersionUID = 1L;

	private ITreeProvider<T> provider;

	private Set<IModel<T>> models = new HashSet<IModel<T>>();

	/**
	 * Create an empty subset.
	 * 
	 * @param provider
	 *            the provider of the complete set
	 */
	public ProviderSubset(ITreeProvider<T> provider)
	{
		this(provider, false);
	}

	/**
	 * Create a subset optionally containing all roots of the provider.
	 * 
	 * @param provider
	 *            the provider of the complete set
	 * @param addRoots
	 *            should all roots be added to this subset
	 */
	public ProviderSubset(ITreeProvider<T> provider, boolean addRoots)
	{
		this.provider = provider;

		if (addRoots)
		{
			Iterator<T> roots = provider.getRoots();
			while (roots.hasNext())
			{
				add(roots.next());
			}
		}
	}

	public void detach()
	{
		for (IModel<T> model : models)
		{
			model.detach();
		}
	}

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
		IModel<T> model = model(o);
		
		boolean contains = models.contains(model);
		
		model.detach();
		
		return contains;
	}

	public boolean add(T t)
	{
		return models.add(model(t));
	}

	@SuppressWarnings("unchecked")
	public boolean remove(Object o)
	{
		IModel<T> model = model(o);
		
		boolean removed = models.remove(model);
		
		model.detach();
		
		return removed;
	}

	public Iterator<T> iterator()
	{
		return new Iterator<T>()
		{

			private Iterator<IModel<T>> iterator = models.iterator();

			public boolean hasNext()
			{
				return iterator.hasNext();
			}

			public T next()
			{
				return iterator.next().getObject();
			}

			public void remove()
			{
				iterator.remove();
			}
		};
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
			if (!contains(c))
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
			remove(c);
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
	
	@SuppressWarnings("unchecked")
	private IModel<T> model(Object o)
	{
		return provider.model((T)o);
	}
}
