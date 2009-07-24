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

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.link.Link;
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

	public Node(String id, IModel<T> model)
	{
		super(id, model);

		setOutputMarkupId(true);

		add(createJunction("junction"));

		add(createContent("content", model));
	}

	@SuppressWarnings("unchecked")
	public IModel<T> getModel()
	{
		return (IModel<T>)getDefaultModel();
	}

	protected Link<?> createJunction(String id)
	{
		return new AjaxFallbackLink<Void>(id)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				if (getState() == State.EXPANDED)
				{
					onCollapse();
				}
				else
				{
					onExpand();
				}
			}

			@Override
			public boolean isEnabled()
			{
				return hasChildren();
			}
		};
	}

	@Override
	public String getVariation()
	{
		if (hasChildren())
		{
			if (getState() == State.EXPANDED)
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

	protected abstract boolean hasChildren();

	protected abstract Component createContent(String id, IModel<T> model);

	protected abstract State getState();

	protected abstract void onExpand();

	protected abstract void onCollapse();
}
