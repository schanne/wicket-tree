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
package wickettree.examples.content;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import wickettree.AbstractTree;
import wickettree.content.Folder;
import wickettree.examples.Foo;

/**
 * @author Sven Meier
 */
public class EditableFolderContent extends Content
{

	private static final long serialVersionUID = 1L;

	@Override
	public Component newContentComponent(String id, final AbstractTree<Foo> tree, IModel<Foo> model)
	{
		return new Folder<Foo>(id, tree, model)
		{

			private EditLabel label;

			/**
			 * Always clickable.
			 */
			@Override
			protected boolean isClickable()
			{
				return true;
			}

			/**
			 * Start edit on click.
			 */
			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				label.onEdit(target);
			}

			@Override
			protected Component newLabelComponent(String id, IModel<Foo> model)
			{
				label = new EditLabel(id, new PropertyModel<String>(model, "bar"));
				return label;
			}
		};
	}

	private class EditLabel extends AjaxEditableLabel<String>
	{

		public EditLabel(String id, IModel<String> model)
		{
			super(id, model);
		}

		@Override
		public void onEdit(AjaxRequestTarget target)
		{
			super.onEdit(target);
		}
	}
}