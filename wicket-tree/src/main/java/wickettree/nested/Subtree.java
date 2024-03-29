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
package wickettree.nested;

import java.util.Iterator;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.IItemFactory;
import org.apache.wicket.markup.repeater.IItemReuseStrategy;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;

import wickettree.ITreeProvider;
import wickettree.NestedTree;
import wickettree.AbstractTree.State;

/**
 * A subtree handles all children of a single node (or the root nodes if a
 * <code>null</code> node was given to the constructor).
 * 
 * @see ITreeProvider#getChildren(Object)
 * @see ITreeProvider#getRoots()
 * 
 * @author Sven Meier
 */
public class Subtree<T> extends Panel
{

	private static final long serialVersionUID = 1L;

	private NestedTree<T> tree;

	/**
	 * Create a subtree for the children of the node contained in the given
	 * model or the root nodes if the model contains <code>null</code>.
	 * 
	 * @param id
	 *            component id
	 * @param tree
	 *            the containing tree
	 * @param model
	 */
	public Subtree(String id, final NestedTree<T> tree, final IModel<T> model)
	{
		super(id, model);

		if (tree == null)
		{
			throw new IllegalArgumentException("argument [tree] cannot be null");
		}
		this.tree = tree;

		RefreshingView<T> branches = new RefreshingView<T>("branches")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Iterator<IModel<T>> getItemModels()
			{
				return new ModelIterator();
			}

			@Override
			protected Item<T> newItem(String id, int index, IModel<T> model)
			{
				return newBranchItem(id, index, model);
			}

			@Override
			protected void populateItem(Item<T> item)
			{
				IModel<T> model = item.getModel();

				Component node = tree.newNodeComponent("node", model);
				item.add(node);

				item.add(tree.newSubtree("subtree", model));
			}
		};
		branches.setItemReuseStrategy(new IItemReuseStrategy()
		{
			private static final long serialVersionUID = 1L;

			public <S> Iterator<Item<S>> getItems(IItemFactory<S> factory,
					Iterator<IModel<S>> newModels, Iterator<Item<S>> existingItems)
			{
				return tree.getItemReuseStrategy().getItems(factory, newModels, existingItems);
			}
		});
		add(branches);
	}

	@SuppressWarnings("unchecked")
	public IModel<T> getModel()
	{
		return (IModel<T>)getDefaultModel();
	}

	public T getModelObject()
	{
		return getModel().getObject();
	}

	protected BranchItem<T> newBranchItem(String id, int index, IModel<T> model)
	{
		return new BranchItem<T>(id, index, model);
	}

	@Override
	public boolean isVisible()
	{
		T t = getModel().getObject();
		if (t == null) {
			// roots always visible
			return true;
		} else {			
			return tree.getState(t) == State.EXPANDED;
		}
	}
	
	private final class ModelIterator implements Iterator<IModel<T>>
	{
		private Iterator<? extends T> children;

		public ModelIterator()
		{
			T t = getModel().getObject();
			if (t == null)
			{
				children = tree.getProvider().getRoots();
			}
			else
			{
				children = tree.getProvider().getChildren(t);
			}
		}

		public void remove()
		{
			throw new UnsupportedOperationException();
		}

		public boolean hasNext()
		{
			return children.hasNext();
		}

		public IModel<T> next()
		{
			return tree.getProvider().model(children.next());
		}
	}
}
