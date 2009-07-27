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

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import wickettree.AbstractTree;
import wickettree.DefaultTableTree;
import wickettree.TableTree;
import wickettree.table.TreeColumn;
import wickettree.table.TreeModel;

/**
 * @author Sven Meier
 */
public class TableTreePage extends ExamplePage
{

	private static final long serialVersionUID = 1L;

	private TableTree<Foo> tree;

	@Override
	protected AbstractTree<Foo> createTree(FooProvider provider)
	{
		IColumn<Foo>[] columns = createColumns();

		tree = new DefaultTableTree<Foo>("tree", columns, provider, Integer.MAX_VALUE)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Component newContentComponent(String id, IModel<Foo> model)
			{
				return TableTreePage.this.newContentComponent(id, model);
			}
		};
		return tree;
	}

	@SuppressWarnings("unchecked")
	private IColumn<Foo>[] createColumns()
	{
		ArrayList<IColumn<Foo>> columns = new ArrayList<IColumn<Foo>>();

		columns.add(new PropertyColumn<Foo>(Model.of("ID"), "id"));

		columns.add(new TreeColumn<Foo>(Model.of("Tree")));

		columns.add(new AbstractColumn<Foo>(Model.of("Depth"))
		{
			private static final long serialVersionUID = 1L;

			public void populateItem(Item<ICellPopulator<Foo>> cellItem, String componentId,
					IModel<Foo> rowModel)
			{
				TreeModel<Foo> model = (TreeModel<Foo>)rowModel;

				cellItem.add(new Label(componentId, "" + model.getDepth()));
			}

			@Override
			public String getCssClass()
			{
				return "number";
			}
		});

		columns.add(new PropertyColumn<Foo>(Model.of("Bar"), "bar"));
		columns.add(new PropertyColumn<Foo>(Model.of("Baz"), "baz"));

		return columns.toArray(new IColumn[columns.size()]);
	}
}
