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
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.IItemReuseStrategy;
import org.apache.wicket.model.IModel;

/**
 * @author Sven Meier
 */
public abstract class AbstractTree<T> extends Panel
{
	private static final long serialVersionUID = 1L;

	private ITreeProvider<T> provider;

	public AbstractTree(String id, ITreeProvider<T> provider)
	{
		this(id, provider, null);
	}

	public AbstractTree(String id, ITreeProvider<T> provider, IModel<Set<T>> state)
	{
		super(id, state);

		this.provider = provider;

		// see #onNodeStateChanged(Component)
		setOutputMarkupId(true);

		add(new HeaderContributor(new IHeaderContributor()
		{
			private static final long serialVersionUID = 1L;

			public void renderHead(IHeaderResponse response)
			{
				ResourceReference theme = getTheme();
				if (theme != null)
				{
					response.renderCSSReference(theme);
				}
			}
		}));
	}

	/**
	 * Returns the theme reference.
	 * 
	 * @return theme reference
	 */
	protected ResourceReference getTheme()
	{
		return null;
	}

	public abstract AbstractTree<T> setItemReuseStrategy(IItemReuseStrategy strategy);

	public abstract IItemReuseStrategy getItemReuseStrategy();

	public ITreeProvider<T> getProvider()
	{
		return provider;
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

	public void expand(T t)
	{
		if (getModel() != null)
		{
			getModelObject().add(t);
		}
	}

	public void collapse(T t)
	{
		if (getModel() != null)
		{
			getModelObject().remove(t);
		}
	}

	public State getState(T t)
	{
		if (getModel() == null)
		{
			return State.EXPANDED;
		}
			
		if (getModelObject().contains(t))
		{
			return State.EXPANDED;
		}
		else
		{
			return State.COLLAPSED;
		}
	}

	public boolean hasChildren(T t)
	{
		return provider.hasChildren(t);
	}

	@Override
	protected void onDetach()
	{
		provider.detach();

		super.onDetach();
	}

	public Component newNodeComponent(String id, final IModel<T> model)
	{
		return new Node<T>(id, model)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Component createContent(String id, IModel<T> model)
			{
				return AbstractTree.this.newContentComponent(id, model);
			}

			@Override
			protected boolean hasChildren()
			{
				return AbstractTree.this.hasChildren(model.getObject());
			}

			@Override
			protected State getState()
			{
				return AbstractTree.this.getState(model.getObject());
			}

			@Override
			protected void onCollapse()
			{
				AbstractTree.this.collapse(model.getObject());

				onNodeStateChanged(this);
			}

			@Override
			protected void onExpand()
			{
				AbstractTree.this.expand(model.getObject());

				onNodeStateChanged(this);
			}
		};
	}

	protected void onNodeStateChanged(Component nodeComponent)
	{
		AjaxRequestTarget target = AjaxRequestTarget.get();
		if (target != null)
		{
			target.addComponent(this);
		}
	}

	protected abstract Component newContentComponent(String id, IModel<T> model);

	/**
	 * Convenience method to update a single {@link Node} on an
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