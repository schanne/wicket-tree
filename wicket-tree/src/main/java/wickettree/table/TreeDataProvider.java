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
package wickettree.table;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import wickettree.ITreeProvider;

/**
 * An adapter of a {@link ITreeProvider} to a {@link IDataProvider}.
 * 
 * @author Sven Meier
 */
public abstract class TreeDataProvider<T> implements ITreeDataProvider<T>
{
	private static final long serialVersionUID = 1L;

	private final ITreeProvider<T> provider;

	private Branch currentBranch;

	private Branch previousBranch;

	private int size = -1;

	public TreeDataProvider(ITreeProvider<T> provider)
	{
		this.provider = provider;
	}

	public int size()
	{
		if (size == -1)
		{
			size = 0;

			Iterator<? extends T> iterator = iterator(0, Integer.MAX_VALUE);
			while (iterator.hasNext())
			{
				iterator.next();
				
				size++;
			}
		}
		return size;
	}

	public Iterator<? extends T> iterator(int first, int count)
	{
		currentBranch = new Branch(null, provider.getRoots());

		Iterator<T> iterator = new Iterator<T>()
		{

			public boolean hasNext()
			{
				while (currentBranch != null)
				{
					if (currentBranch.hasNext())
					{
						return true;
					}
					currentBranch = currentBranch.parent;
				}

				return false;
			}

			public T next()
			{
				if (!hasNext())
				{
					throw new IllegalStateException();
				}

				T next = currentBranch.next();

				previousBranch = currentBranch;

				if (iterateChildren(next))
				{
					currentBranch = new Branch(previousBranch, provider.getChildren(next));
				}

				return next;
			}

			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};

		for (int i = 0; i < first; i++)
		{
			iterator.next();
		}

		return iterator;
	}

	/**
	 * Hook method to decide wether the given node's children should be
	 * iterated.
	 */
	protected abstract boolean iterateChildren(T object);

	public NodeModel<T> model(T object)
	{
		return previousBranch.wrapModel(provider.model(object));
	}

	public void detach()
	{
		currentBranch = null;
		previousBranch = null;
		size = -1;
	}

	private class Branch implements Iterator<T>
	{
		private Branch parent;

		private Iterator<? extends T> children;

		public Branch(Branch parent, Iterator<? extends T> children)
		{
			this.parent = parent;
			this.children = children;
		}

		public NodeModel<T> wrapModel(IModel<T> model)
		{
			boolean[] branches = new boolean[getDepth()];

			Branch branch = this;
			for (int c = branches.length - 1; c >= 0; c--)
			{
				branches[c] = branch.hasNext();

				branch = branch.parent;
			}

			return new NodeModel<T>(model, branches);
		}

		public int getDepth()
		{
			if (parent == null)
			{
				return 1;
			}
			else
			{
				return parent.getDepth() + 1;
			}
		}

		public boolean hasNext()
		{
			return children.hasNext();
		}

		public T next()
		{
			if (!hasNext())
			{
				throw new IllegalStateException();
			}

			return children.next();
		}

		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
}