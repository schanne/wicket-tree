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
package wickettree.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import wickettree.AbstractTree;
import wickettree.content.CheckedFolder;
import wickettree.provider.ProviderSubset;
import wickettree.theme.HumanTheme;
import wickettree.theme.WindowsTheme;

/**
 * @author Sven Meier
 */
public abstract class ExamplePage extends WebPage
{

	private ResourceReference theme = new WindowsTheme();

	private ProviderSubset<Foo> state;

	private ProviderSubset<Foo> selected;

	private ProviderSubset<Foo> checked;

	private AbstractTree<Foo> tree;

	private FooProvider provider;

	public ExamplePage()
	{
		provider = new FooProvider();

		state = new ProviderSubset<Foo>(provider, true);

		selected = new ProviderSubset<Foo>(provider, false);

		checked = new ProviderSubset<Foo>(provider, false);

		Form<Void> form = new Form<Void>("form");
		add(form);

		form.add(new DropDownChoice<ResourceReference>("theme",
				new PropertyModel<ResourceReference>(this, "theme"), getThemes(),
				new ChoiceRenderer<ResourceReference>("class.simpleName"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean wantOnSelectionChangedNotifications()
			{
				return true;
			}
		});

		tree = createTree(provider, state);
		add(tree);

		add(new Link<Void>("refresh")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
			}
		});
	}

	protected abstract AbstractTree<Foo> createTree(FooProvider provider, IModel<Set<Foo>> state);

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
		tree.updateNode(foo, target);
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
		tree.updateNode(foo, target);
	}

	private List<ResourceReference> getThemes()
	{
		List<ResourceReference> themes = new ArrayList<ResourceReference>();

		themes.add(new WindowsTheme());
		themes.add(new HumanTheme());

		return themes;
	}

	protected ResourceReference getTheme()
	{
		return theme;
	}

	@Override
	public void detachModels()
	{
		state.detach();
		selected.detach();
		checked.detach();
		
		super.detachModels();
	}
	
	protected Component newContentComponent(String id, IModel<Foo> model)
	{
		return new CheckedFolder<Foo>(id, tree, model)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return true;
			}

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				ExamplePage.this.select(getModelObject(), target);
			}

			@Override
			protected boolean isSelected()
			{
				return ExamplePage.this.isSelected(getModelObject());
			}

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
		};
	}
}