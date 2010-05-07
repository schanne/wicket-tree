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
package wickettree.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import wickettree.AbstractTree;
import wickettree.examples.content.BookmarkableFolderContent;
import wickettree.examples.content.CheckedFolderContent;
import wickettree.examples.content.CheckedSelectableFolderContent;
import wickettree.examples.content.Content;
import wickettree.examples.content.EditableFolderContent;
import wickettree.examples.content.FolderContent;
import wickettree.examples.content.LabelContent;
import wickettree.examples.content.MixedContent;
import wickettree.examples.content.MultiLineLabelContent;
import wickettree.examples.content.MultiSelectableFolderContent;
import wickettree.examples.content.PanelContent;
import wickettree.examples.content.SelectableFolderContent;
import wickettree.provider.InverseSet;
import wickettree.provider.ProviderSubset;
import wickettree.theme.HumanTheme;
import wickettree.theme.WindowsTheme;

/**
 * @author Sven Meier
 */
public abstract class ExamplePage extends WebPage
{

	private static final long serialVersionUID = 1L;

	private ResourceReference theme;

	private AbstractTree<Foo> tree;

	private FooProvider provider = new FooProvider();

	private Set<Foo> state = new ProviderSubset<Foo>(provider);

	private Content content;

	private List<Content> contents;

	private List<ResourceReference> themes;

	public ExamplePage()
	{
		content = new CheckedFolderContent(provider);

		Form<Void> form = new Form<Void>("form");
		add(form);

		tree = createTree(provider, newStateModel());
		tree.add(new HeaderContributor(new IHeaderContributor()
		{
			private static final long serialVersionUID = 1L;

			public void renderHead(IHeaderResponse response)
			{
				response.renderCSSReference(theme);
			}
		}));
		form.add(tree);

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

		form.add(new Link<Void>("expandAll")
		{
			@Override
			public void onClick()
			{
				((IDetachable)state).detach();
				state = new InverseSet<Foo>(new ProviderSubset<Foo>(provider));
			}
		});

		form.add(new Link<Void>("collapseAll")
		{
			@Override
			public void onClick()
			{
				((IDetachable)state).detach();
				state = new ProviderSubset<Foo>(provider);
			}
		});

		form.add(new Button("submit")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
			}
		});
	}

	private IModel<Set<Foo>> newStateModel()
	{
		return new AbstractReadOnlyModel<Set<Foo>>()
		{
			@Override
			public Set<Foo> getObject()
			{
				return state;
			}

			/**
			 * Super class doesn't detach - would be nice though.
			 */
			@Override
			public void detach()
			{
				((IDetachable)state).detach();
			}
		};
	}

	protected abstract AbstractTree<Foo> createTree(FooProvider provider, IModel<Set<Foo>> state);

	private List<Content> initContents()
	{
		contents = new ArrayList<Content>();

		contents.add(new BookmarkableFolderContent(tree));
		contents.add(new LabelContent());
		contents.add(new MultiLineLabelContent());
		contents.add(new FolderContent());
		contents.add(new EditableFolderContent());
		contents.add(new SelectableFolderContent(provider));
		contents.add(new MultiSelectableFolderContent(provider));
		contents.add(new CheckedFolderContent(provider));
		contents.add(new CheckedSelectableFolderContent(provider));
		contents.add(new PanelContent());
		contents.add(new MixedContent(contents));

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