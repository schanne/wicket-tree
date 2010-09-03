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

import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import wickettree.AbstractTree;
import wickettree.NestedTree;

/**
 * @author Sven Meier
 */
public class NestedTreePage extends ContentPage
{

	private static final long serialVersionUID = 1L;

	private NestedTree<Foo> tree;

	@Override
	protected AbstractTree<Foo> createTree(FooProvider provider, IModel<Set<Foo>> state)
	{
		tree = new NestedTree<Foo>("tree", provider, state)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Component newContentComponent(String id, IModel<Foo> model)
			{
				return NestedTreePage.this.newContentComponent(id, model);
			}
		};
		return tree;
	}
}