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
package wickettree.content;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import wickettree.AbstractTree;

/**
 * This class adds a {@link CheckBox} to a {@link Folder}. Subclasses have to
 * override {@link #newCheckBoxModel(IModel)} to do anything useful with the
 * checkbox state.
 * 
 * @see #newCheckBoxModel(IModel)
 * 
 * @author Sven Meier
 */
public class CheckedFolder<T> extends Folder<T>
{

	private static final long serialVersionUID = 1L;

	public CheckedFolder(String id, AbstractTree<T> tree, IModel<T> model)
	{
		super(id, tree, model);

		add(newCheckBox("checkbox", model));
	}

	/**
	 * Hook method to create a new checkbox component. This default
	 * implementation uses an {@link AjaxCheckBox}.
	 * 
	 * @param id
	 * @param model
	 * @return created component
	 * 
	 * @see #newCheckBoxModel(IModel)
	 * @see #onUpdate(AjaxRequestTarget)
	 */
	protected Component newCheckBox(String id, IModel<T> model)
	{
		return new AjaxCheckBox(id, newCheckBoxModel(model))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				CheckedFolder.this.onUpdate(target);
			}
		};
	}

	/**
	 * Create the model for the checkbox, defaults to {@link Boolean#FALSE}.
	 * 
	 * @param model
	 * @return wrapping model
	 */
	protected IModel<Boolean> newCheckBoxModel(IModel<T> model)
	{
		return Model.of(Boolean.FALSE);
	}

	/**
	 * Hook method to be notified of an update of the checkbox.
	 * 
	 * @param target
	 * @see #newCheckBox(String, IModel)
	 */
	protected void onUpdate(AjaxRequestTarget target)
	{
	}
}