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

import java.io.Serializable;
import java.util.Iterator;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

import wickettree.AbstractTree;
import wickettree.ITreeProvider;

/**
 * A provider wrapping a Swing {@link TreeModel}.
 * 
 * EXPERIMENTAL !
 * 
 * @author Sven Meier
 */
public abstract class TreeModelProvider<T> implements ITreeProvider<T>
{

	private TreeModel treeModel;

	private boolean rootVisible;

	private Listener listener;
	
	public TreeModelProvider(TreeModel treeModel)
	{
		this(treeModel, true);
	}

	public TreeModelProvider(TreeModel treeModel, boolean rootVisible)
	{
		this.treeModel = treeModel;
		this.rootVisible = rootVisible;
		
		treeModel.addTreeModelListener(listener);
	}

	public Iterator<T> getRoots()
	{
		if (rootVisible)
		{
			return new Iterator<T>()
			{
				boolean next = true;

				public boolean hasNext()
				{
					return next;
				}

				public T next()
				{
					next = false;
					return cast(treeModel.getRoot());
				}

				public void remove()
				{
					throw new UnsupportedOperationException();
				}
			};
		}
		else
		{
			return getChildren(cast(treeModel.getRoot()));
		}
	}

	public boolean hasChildren(T object)
	{
		return !treeModel.isLeaf(object);
	}

	public Iterator<T> getChildren(final T object)
	{
		return new Iterator<T>()
		{
			private int size = treeModel.getChildCount(object);
			private int index = -1;

			public boolean hasNext()
			{
				return index < size - 1;
			}

			public T next()
			{
				index++;
				return cast(treeModel.getChild(object, index));
			}

			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}

	@SuppressWarnings("unchecked")
	protected T cast(Object object)
	{
		return (T)object;
	}

	public abstract IModel<T> model(T object);

	public void detach()
	{
		// TODO clear changes
	}

	public void update(AbstractTree<T> tree, AjaxRequestTarget target)
	{
		// TODO add changes to target
	}
	
	private class Listener implements TreeModelListener, Serializable {
		public void treeNodesChanged(TreeModelEvent e)
		{
			// TODO record change
		}
		
		public void treeNodesInserted(TreeModelEvent e)
		{
			// TODO record change
		}
		
		public void treeNodesRemoved(TreeModelEvent e)
		{
			// TODO record change
		}
		
		public void treeStructureChanged(TreeModelEvent e)
		{
			// TODO record change
		}
	}
}
