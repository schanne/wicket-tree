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
package wickettree.util;

import java.io.Serializable;

import junit.framework.TestCase;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;

/**
 * Test for {@link MultiColumn}.
 */
public class MultiColumnTest extends TestCase
{
	
	@SuppressWarnings("unused")
	private WicketTester tester;

	@Override
	protected void setUp() throws Exception
	{
		tester = new WicketTester();
	}
	
	public void testColumn() throws Exception
	{
		final Label label = new Label("test");
		
		AbstractColumn<String> column = new AbstractColumn<String>(Model.of("header"), "sort")
		{
			public void populateItem(Item<ICellPopulator<String>> cellItem, String componentId,
					IModel<String> rowModel)
			{
				cellItem.add(label);
			}
			
			@Override
			public String getCssClass()
			{
				return "css";
			}
		};

		MultiColumn<Serializable> multi = new MultiColumn<Serializable>(String.class, column);

		assertEquals("header", multi.getHeader("test").getDefaultModelObject());
		assertEquals("sort", multi.getSortProperty());
		assertEquals("css", multi.getCssClass());

		Item<ICellPopulator<Serializable>> item = new Item<ICellPopulator<Serializable>>("container", 0);
		multi.populateItem(item, "testId", new Model<Serializable>(new String()));
		assertEquals(label, item.get(0));
	}

	public void testDisplayModelSortPropertyColumn() throws Exception
	{
		final Label label = new Label("test");
		
		AbstractColumn<String> column = new AbstractColumn<String>(Model.of("header"), "sort")
		{
			public void populateItem(Item<ICellPopulator<String>> cellItem, String componentId,
					IModel<String> rowModel)
			{
				cellItem.add(label);
			}
			
			@Override
			public String getCssClass()
			{
				return "css";
			}
		};

		MultiColumn<Serializable> multi = new MultiColumn<Serializable>(Model.of("header2"), "sort2", String.class, column) {
			@Override
			public String getCssClass()
			{
				return "css2";
			}
		};

		assertEquals("header2", multi.getHeader("test").getDefaultModelObject());
		assertEquals("sort2", multi.getSortProperty());
		assertEquals("css2", multi.getCssClass());

		Item<ICellPopulator<Serializable>> item = new Item<ICellPopulator<Serializable>>("container", 0);
		multi.populateItem(item, "testId", new Model<Serializable>(new String()));
		assertEquals(label, item.get(0));
	}
}
