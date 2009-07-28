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
import org.apache.wicket.model.IModel;

import wickettree.AbstractTree;
import wickettree.ITreeProvider;
import wickettree.content.Folder;
import wickettree.examples.Foo;

/**
 * @author Sven Meier
 */
public class SelectableFolderContent extends Content
{

	private static final long serialVersionUID = 1L;

	private ITreeProvider<Foo> provider;

	private IModel<Foo> selected;

	public SelectableFolderContent(ITreeProvider<Foo> provider)
	{
		this.provider = provider;
	}

	public void detach()
	{
		if (selected != null)
		{
			selected.detach();
		}
	}

	protected boolean isSelected(Foo foo)
	{
		return selected != null && selected.equals(provider.model(foo));
	}

	protected void select(Foo foo, AbstractTree<Foo> tree, final AjaxRequestTarget target)
	{
		if (selected != null)
		{
			tree.updateNode(selected.getObject(), target);

			selected.detach();
			selected = null;
		}

		selected = provider.model(foo);

		tree.updateNode(foo, target);
	}

	@Override
	public Component newContentComponent(String id, final AbstractTree<Foo> tree, IModel<Foo> model)
	{
		return new Folder<Foo>(id, tree, model)
		{
			private static final long serialVersionUID = 1L;

			/**
			 * Always clickable.
			 */
			@Override
			protected boolean isClickable()
			{
				return true;
			}

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				SelectableFolderContent.this.select(getModelObject(), tree, target);
			}

			@Override
			protected boolean isSelected()
			{
				return SelectableFolderContent.this.isSelected(getModelObject());
			}
		};
	}
}