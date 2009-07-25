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

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import wickettree.AbstractTree;
import wickettree.examples.content.CheckedFolderContent;
import wickettree.examples.content.CheckedSelectableFolderContent;
import wickettree.examples.content.Content;
import wickettree.examples.content.FolderContent;
import wickettree.examples.content.LabelContent;
import wickettree.examples.content.PanelContent;
import wickettree.examples.content.SelectableFolderContent;
import wickettree.theme.HumanTheme;
import wickettree.theme.WindowsTheme;

/**
 * @author Sven Meier
 */
public abstract class ExamplePage extends WebPage
{

	private ResourceReference theme;

	private AbstractTree<Foo> tree;

	private FooProvider provider = new FooProvider();

	private Content content;

	private List<Content> contents;

	private List<ResourceReference> themes;

	public ExamplePage()
	{
		content = new CheckedFolderContent(provider);

		Form<Void> form = new Form<Void>("form");
		add(form);

		form.add(new DropDownChoice<Content>("content",
				new PropertyModel<Content>(this, "content"), initContents(),
				new ChoiceRenderer<Content>("class.simpleName"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean wantOnSelectionChangedNotifications()
			{
				return true;
			}
		});

		form.add(new DropDownChoice<ResourceReference>("theme",
				new PropertyModel<ResourceReference>(this, "theme"), initThemes(),
				new ChoiceRenderer<ResourceReference>("class.simpleName"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean wantOnSelectionChangedNotifications()
			{
				return true;
			}
		});

		tree = createTree(provider);
		form.add(tree);
		
		form.add(new Button("submit") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
			}
		});
	}

	protected abstract AbstractTree<Foo> createTree(FooProvider provider);

	private List<Content> initContents()
	{
		contents = new ArrayList<Content>();

		contents.add(new LabelContent(provider));
		contents.add(new FolderContent(provider));
		contents.add(new SelectableFolderContent(provider));
		contents.add(new CheckedFolderContent(provider));
		contents.add(new CheckedSelectableFolderContent(provider));
		contents.add(new PanelContent());

		content = contents.get(0);

		return contents;
	}

	private List<ResourceReference> initThemes()
	{
		themes = new ArrayList<ResourceReference>();

		themes.add(new WindowsTheme());
		themes.add(new HumanTheme());

		theme = themes.get(0);

		return themes;
	}

	protected ResourceReference getTheme()
	{
		return theme;
	}

	@Override
	public void detachModels()
	{
		for (Content content : contents)
		{
			content.detach();
		}

		super.detachModels();
	}

	protected Component newContentComponent(String id, IModel<Foo> model)
	{
		return content.newContentComponent(id, tree, model);
	}
}