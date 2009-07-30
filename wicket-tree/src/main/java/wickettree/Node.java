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

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import wickettree.AbstractTree.State;

/**
 * Representation of a single node in the tree.
 * 
 * @author Sven Meier
 */
public abstract class Node<T> extends Panel
{

	private static final long serialVersionUID = 1L;

	private AbstractTree<T> tree;

	public Node(String id, AbstractTree<T> tree, IModel<T> model)
	{
		super(id, model);

		this.tree = tree;

		setOutputMarkupId(true);

		add(createJunctionComponent("junction"));

		add(createContent("content", model));
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

	protected MarkupContainer createJunctionComponent(String id)
	{
		return new AjaxFallbackLink<Void>(id)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				toggle();
			}

			@Override
			public boolean isEnabled()
			{
				return tree.hasChildren(Node.this.getModelObject());
			}
		};
	}

	@Override
	public String getVariation()
	{
		T t = getModelObject();

		if (tree.hasChildren(t))
		{
			if (tree.getState(t) == State.EXPANDED)
			{
				return "expanded";
			}
			else
			{
				return "collapsed";
			}
		}
		return null;
	}

	private void toggle()
	{
		T t = getModelObject();

		if (tree.getState(t) == State.EXPANDED)
		{
			tree.collapse(t);
		}
		else
		{
			tree.expand(t);
		}
	}

	protected abstract Component createContent(String id, IModel<T> model);
}
