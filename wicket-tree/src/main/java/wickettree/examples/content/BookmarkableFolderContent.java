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
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

import wickettree.AbstractTree;
import wickettree.ITreeProvider;
import wickettree.content.Folder;
import wickettree.examples.Foo;

/**
 * @author Sven Meier
 */
public class BookmarkableFolderContent extends Content
{

	private static final long serialVersionUID = 1L;

	private ITreeProvider<Foo> provider;

	private Class<? extends Page> pageClass;

	public BookmarkableFolderContent(ITreeProvider<Foo> provider, Class<? extends Page> pageClass)
	{
		this.provider = provider;

		this.pageClass = pageClass;
	}

	public void detach()
	{
		provider.detach();
	}

	@Override
	public Component newContentComponent(String id, final AbstractTree<Foo> tree, IModel<Foo> model)
	{
		return new Folder<Foo>(id, tree, model)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Link<?> newLink(String id, IModel<Foo> model)
			{
				Foo foo = model.getObject();

				if (!provider.hasChildren(foo))
				{
					return new BookmarkablePageLink<Void>(id, pageClass, new PageParameters("foo="
							+ foo.getId()))
					{

						private static final long serialVersionUID = 1L;

						@Override
						protected void onComponentTag(ComponentTag tag)
						{
							super.onComponentTag(tag);

							String styleClass = getStyleClass();
							if (styleClass != null)
							{
								tag.put("class", styleClass);
							}
						}
					};
				}
				else
				{
					return super.newLink(id, model);
				}
			}
		};
	}
}