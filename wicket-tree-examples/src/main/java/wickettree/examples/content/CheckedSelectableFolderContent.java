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
package wickettree.examples.content;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

import wickettree.AbstractTree;
import wickettree.ITreeProvider;
import wickettree.content.CheckedFolder;
import wickettree.examples.Foo;
import wickettree.provider.ProviderSubset;

/**
 * @author Sven Meier
 */
public class CheckedSelectableFolderContent extends Content
{

	private static final long serialVersionUID = 1L;

	private ProviderSubset<Foo> checked;

	private ProviderSubset<Foo> selected;

	public CheckedSelectableFolderContent(ITreeProvider<Foo> provider)
	{
		checked = new ProviderSubset<Foo>(provider, false);

		selected = new ProviderSubset<Foo>(provider, false);
	}

	public void detach()
	{
		checked.detach();

		selected.detach();
	}

	protected boolean isChecked(Foo foo)
	{
		return checked.getObject().contains(foo);
	}

	protected void check(Foo foo, boolean check, final AjaxRequestTarget target)
	{
		if (check)
		{
			checked.getObject().add(foo);
		}
		else
		{
			checked.getObject().remove(foo);
		}
	}

	protected boolean isSelected(Foo foo)
	{
		return selected.getObject().contains(foo);
	}

	protected void select(Foo foo, final AjaxRequestTarget target)
	{
		if (isSelected(foo))
		{
			selected.getObject().remove(foo);
		}
		else
		{
			selected.getObject().add(foo);
		}
	}

	@Override
	public Component newContentComponent(String id, final AbstractTree<Foo> tree, IModel<Foo> model)
	{
		return new CheckedFolder<Foo>(id, tree, model)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<Boolean> newCheckBoxModel(final IModel<Foo> model)
			{
				return new IModel<Boolean>()
				{
					private static final long serialVersionUID = 1L;

					public Boolean getObject()
					{
						return isChecked(model.getObject());
					}

					public void setObject(Boolean object)
					{
						check(model.getObject(), object, AjaxRequestTarget.get());
					}

					public void detach()
					{
					}
				};
			}

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
				CheckedSelectableFolderContent.this.select(getModelObject(), target);

				tree.updateNode(getModelObject(), target);
			}

			@Override
			protected boolean isSelected()
			{
				return CheckedSelectableFolderContent.this.isSelected(getModelObject());
			}
		};
	}
}