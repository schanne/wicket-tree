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
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import wickettree.AbstractTree;

/**
 * @author Sven Meier
 */
public class CheckedFolder<T> extends Folder<T>
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public CheckedFolder(String id, AbstractTree<T> tree, IModel<T> model)
	{
		super(id, tree, model);

		add(newCheckBox("checkbox", model));
	}

	/**
	 * Hook method to create a new checkbox component.
	 * 
	 * @param id
	 * @param model
	 * @return
	 * @see #newCheckBoxModel(IModel)
	 */
	protected Component newCheckBox(String id, IModel<T> model)
	{
		return new AjaxCheckBox(id, newCheckBoxModel(model)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				CheckedFolder.this.onUpdate(target);
			}
		};
	}

	/**
	 * Create the model for the checkbox, default to {@link Boolean#FALSE}.
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
	 */
	protected void onUpdate(AjaxRequestTarget target)
	{
	}
}