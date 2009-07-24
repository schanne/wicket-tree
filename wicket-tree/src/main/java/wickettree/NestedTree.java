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
package wickettree;

import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.DefaultItemReuseStrategy;
import org.apache.wicket.markup.repeater.IItemReuseStrategy;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import wickettree.nested.Subtree;

/**
 * A tree with nested markup.
 * 
 * @author Sven Meier
 */
public abstract class NestedTree<T> extends AbstractTree<T>
{

	private static final long serialVersionUID = 1L;

	private IItemReuseStrategy itemReuseStrategy;

	public NestedTree(String id, ITreeProvider<T> provider)
	{
		this(id, provider, null);
	}

	public NestedTree(String id, ITreeProvider<T> provider, IModel<Set<T>> expansion)
	{
		super(id, provider, expansion);

		add(newSubtree("subtree", new RootsModel()));
	}

	public Subtree<T> newSubtree(String id, IModel<T> model)
	{
		return new Subtree<T>(id, this, model)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Component newNodeComponent(String id, IModel<T> model)
			{
				return NestedTree.this.newNodeComponent(id, model);
			}

			@Override
			protected IItemReuseStrategy getItemReuseStrategy()
			{
				return NestedTree.this.getItemReuseStrategy();
			}

			@Override
			protected State getState(T t)
			{
				return NestedTree.this.getState(t);
			}
		};
	}

	/**
	 * Sets the item reuse strategy. This strategy controls the creation of
	 * {@link Item}s.
	 * 
	 * @see RefreshingView#setItemReuseStrategy(IItemReuseStrategy)
	 * @see IItemReuseStrategy
	 * 
	 * @param strategy
	 *            item reuse strategy
	 * @return this for chaining
	 */
	public final AbstractTree<T> setItemReuseStrategy(IItemReuseStrategy strategy)
	{
		this.itemReuseStrategy = strategy;

		return this;
	}

	public IItemReuseStrategy getItemReuseStrategy()
	{
		if (itemReuseStrategy == null)
		{
			return DefaultItemReuseStrategy.getInstance();
		}
		return itemReuseStrategy;
	}

	@Override
	protected void onNodeStateChanged(Component nodeComponent)
	{
		AjaxRequestTarget target = AjaxRequestTarget.get();
		if (target != null)
		{
			// just add the nodes parental branch
			target.addComponent(nodeComponent.getParent());
		}
	}

	private class RootsModel extends AbstractReadOnlyModel<T>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public T getObject()
		{
			return null;
		}
	}
}
