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
package wickettree;

import java.io.Serializable;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.DefaultItemReuseStrategy;
import org.apache.wicket.markup.repeater.IItemReuseStrategy;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import wickettree.provider.ProviderSubset;

/**
 * Abstract base class for {@link NestedTree} and {@link TableTree}. Uses its
 * model for storing the {@link State} of its {@link Node}s.
 * 
 * Note that a tree has no notion of a <em>selection</em>. Handling state of
 * nodes besides expanse/collapse is irrelevant to a tree implementation.
 * 
 * @see #newContentComponent(String, IModel)
 * 
 * @author Sven Meier
 */
public abstract class AbstractTree<T> extends Panel
{
	private static final long serialVersionUID = 1L;

	private ITreeProvider<T> provider;

	private IItemReuseStrategy itemReuseStrategy;

	protected AbstractTree(String id, ITreeProvider<T> provider)
	{
		this(id, provider, null);
	}

	protected AbstractTree(String id, ITreeProvider<T> provider, IModel<Set<T>> state)
	{
		super(id, state);

		this.provider = provider;

		// see #onNodeStateChanged(Component)
		setOutputMarkupId(true);
	}

	/**
	 * Sets the item reuse strategy. This strategy controls the creation of
	 * {@link Item}s.
	 * 
	 * @see IItemReuseStrategy
	 * 
	 * @param strategy
	 *            item reuse strategy
	 * @return this for chaining
	 */
	public AbstractTree<T> setItemReuseStrategy(IItemReuseStrategy strategy)
	{
		this.itemReuseStrategy = strategy;

		return this;
	}

	/**
	 * @return currently set item reuse strategy. Defaults to
	 *         <code>DefaultItemReuseStrategy</code> if none was set.
	 * 
	 * @see DefaultItemReuseStrategy
	 */
	public IItemReuseStrategy getItemReuseStrategy()
	{
		if (itemReuseStrategy == null)
		{
			return DefaultItemReuseStrategy.getInstance();
		}
		return itemReuseStrategy;
	}

	/**
	 * Get the provider of the tree nodes.
	 * 
	 * @return provider
	 */
	public ITreeProvider<T> getProvider()
	{
		return provider;
	}

	/**
	 * Uses a {@link ProviderSubset} as model if none is inited in super
	 * implementation.
	 */
	@Override
	protected IModel<?> initModel()
	{
		IModel<?> model = super.initModel();

		if (model == null)
		{
			model = Model.of((Serializable)new ProviderSubset<T>(provider));
		}

		return model;
	}

	@SuppressWarnings("unchecked")
	public IModel<Set<T>> getModel()
	{
		return (IModel<Set<T>>)getDefaultModel();
	}

	public Set<T> getModelObject()
	{
		return getModel().getObject();
	}

	/**
	 * Expand the given node, tries to update the affected branch if the change
	 * happens on an {@link AjaxRequestTarget}.
	 * 
	 * @see #updateBranch(Object, AjaxRequestTarget)
	 */
	public void expand(T t)
	{
		getModelObject().add(t);

		updateBranch(t, AjaxRequestTarget.get());
	}

	/**
	 * Collapse the given node, tries to update the affected branch if the
	 * change happens on an {@link AjaxRequestTarget}.
	 * 
	 * @see #updateBranch(Object, AjaxRequestTarget)
	 */
	public void collapse(T t)
	{
		getModelObject().remove(t);

		updateBranch(t, AjaxRequestTarget.get());
	}

	/**
	 * Get the given node's {@link State}.
	 */
	public State getState(T t)
	{
		if (getModelObject().contains(t))
		{
			return State.EXPANDED;
		}
		else
		{
			return State.COLLAPSED;
		}
	}

	/**
	 * Overriden to detach the {@link ITreeProvider}.
	 */
	@Override
	protected void onDetach()
	{
		provider.detach();

		super.onDetach();
	}

	/**
	 * Create a new component for a node.
	 */
	public Component newNodeComponent(String id, final IModel<T> model)
	{
		return new Node<T>(id, this, model)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Component createContent(String id, IModel<T> model)
			{
				return AbstractTree.this.newContentComponent(id, model);
			}
		};
	}

	/**
	 * Create a new component for the content of a node.
	 */
	protected abstract Component newContentComponent(String id, IModel<T> model);

	/**
	 * Convenience method to update a single branch on an
	 * {@link AjaxRequestTarget}. Does nothing if target is <code>null</code>.
	 * 
	 * This default implementation adds this whole component for rendering.
	 * 
	 * @param t
	 * @param target
	 */
	public void updateBranch(T t, final AjaxRequestTarget target)
	{
		if (target != null)
		{
			target.addComponent(this);
		}
	}

	/**
	 * Convenience method to update a single node on an
	 * {@link AjaxRequestTarget}. Does nothing if target is <code>null</code>.
	 * 
	 * @param t
	 * @param target
	 */
	public void updateNode(T t, final AjaxRequestTarget target)
	{
		if (target != null)
		{
			final IModel<T> model = getProvider().model(t);
			visitChildren(Node.class, new IVisitor<Node<T>>()
			{
				public Object component(Node<T> node)
				{
					if (model.equals(node.getModel()))
					{
						target.addComponent(node);
						return IVisitor.STOP_TRAVERSAL;
					}
					return IVisitor.CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER;
				}
			});
		}
	}

	public static enum State {
		COLLAPSED, EXPANDED
	}
}