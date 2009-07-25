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
package wickettree.content;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * A styled link with a label.
 * 
 * @see #newLink(String, IModel)
 * @see #newLabel(String, IModel)
 * @see #isClickable()
 * @see #onClick(AjaxRequestTarget)
 * 
 * @author Sven Meier
 */
public abstract class StyledLinkLabel<T> extends Panel
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public StyledLinkLabel(String id, IModel<T> model)
	{
		super(id, model);

		Link<?> link = newLink("link", model);
		link.add(new AbstractBehavior()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentTag(Component component, ComponentTag tag)
			{
				String styleClass = getStyleClass();
				if (styleClass != null)
				{
					tag.put("class", styleClass);
				}
			}
		});
		add(link);

		link.add(newLabel("label", model));
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

	/**
	 * Hook method to create a new link component.
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	protected Link<?> newLink(String id, IModel<T> model)
	{
		return new AjaxFallbackLink<Void>(id)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return StyledLinkLabel.this.isClickable();
			}

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				StyledLinkLabel.this.onClick(target);
			}
		};
	}

	/**
	 * Hook method to create a new label component.
	 * 
	 * @param id
	 * @param model
	 * @return
	 * @see #newLabelModel(IModel)
	 */
	protected Component newLabel(String id, IModel<T> model)
	{
		return new Label(id, newLabelModel(model));
	}

	/**
	 * Create the model for the label, defaults to the model itself.
	 * 
	 * @param model
	 * @return wrapping model
	 */
	protected IModel<?> newLabelModel(IModel<T> model)
	{
		return model;
	}

	/**
	 * Get a style class for the link.
	 */
	protected String getStyleClass()
	{
		return null;
	}

	/**
	 * Clicking is disabled by default.
	 */
	public boolean isClickable()
	{
		return false;
	}

	/**
	 * Hook method to be notified of a click on the link. This component must be
	 * enabled to receive click notifications.
	 * 
	 * @param target
	 */
	protected void onClick(AjaxRequestTarget target)
	{
	}
}