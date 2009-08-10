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
package wickettree;

import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.repeater.IItemFactory;
import org.apache.wicket.markup.repeater.IItemReuseStrategy;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import wickettree.table.AbstractToolbar;
import wickettree.table.ITreeColumn;
import wickettree.table.ITreeDataProvider;
import wickettree.table.NodeModel;
import wickettree.table.TreeDataProvider;

/**
 * A tree with tabular markup.
 * 
 * @author Sven Meier
 */
public abstract class TableTree<T> extends AbstractTree<T> implements IPageable
{

	/**
	 * The component id that toolbars must be created with in order to be added
	 * to the tree table.
	 */
	public static final String TOOLBAR_COMPONENT_ID = "toolbar";

	private static final long serialVersionUID = 1L;

	private final DataGridView<T> datagrid;

	private final IColumn<T>[] columns;

	private final RepeatingView topToolbars;

	private final RepeatingView bottomToolbars;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            component id
	 * @param columns
	 *            list of column definitions
	 * @param provider
	 *            provider of the tree
	 * @param rowsPerPage
	 *            number of rows per page
	 */
	public TableTree(String id, IColumn<T>[] columns, ITreeProvider<T> provider, int rowsPerPage)
	{
		this(id, columns, provider, rowsPerPage, null);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            component id
	 * @param columns
	 *            list of column definitions
	 * @param provider
	 *            provider of the tree
	 * @param rowsPerPage
	 *            number of rows per page
	 * @param state
	 *            state of nodes
	 */
	public TableTree(String id, IColumn<T>[] columns, ITreeProvider<T> provider, int rowsPerPage,
			IModel<Set<T>> state)
	{
		super(id, provider, state);

		if (columns == null || columns.length < 1)
		{
			throw new IllegalArgumentException("Argument `columns` cannot be null or empty");
		}
		for (IColumn<T> column : columns)
		{
			if (column instanceof ITreeColumn)
			{
				((ITreeColumn<T>)column).setTree(this);
			}
		}
		this.columns = columns;

		datagrid = new DataGridView<T>("rows", columns, newDataProvider(provider))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Item<ICellPopulator<T>> newCellItem(String id, int index,
					IModel<ICellPopulator<T>> model)
			{
				Item<ICellPopulator<T>> item = TableTree.this.newCellItem(id, index, model);

				final IColumn<?> column = TableTree.this.columns[index];
				if (column instanceof IStyledColumn)
				{
					item.add(new AttributeAppender("class", true, Model
							.of(((IStyledColumn<?>)column).getCssClass()), " "));
				}

				return item;
			}

			@Override
			protected Item<T> newRowItem(String id, int index, IModel<T> model)
			{
				Item<T> item = TableTree.this.newRowItem(id, index, model);

				// @see #updateNode(T, AjaxRequestTarget)
				item.setOutputMarkupId(true);

				return item;
			}
		};
		datagrid.setRowsPerPage(rowsPerPage);
		datagrid.setItemReuseStrategy(new IItemReuseStrategy()
		{
			private static final long serialVersionUID = 1L;

			public <S> Iterator<Item<S>> getItems(IItemFactory<S> factory,
					Iterator<IModel<S>> newModels, Iterator<Item<S>> existingItems)
			{
				return TableTree.this.getItemReuseStrategy().getItems(factory, newModels,
						existingItems);
			}
		});
		add(datagrid);

		topToolbars = new ToolbarsContainer("topToolbars");
		bottomToolbars = new ToolbarsContainer("bottomToolbars");
		add(topToolbars);
		add(bottomToolbars);
	}

	protected ITreeDataProvider<T> newDataProvider(ITreeProvider<T> provider)
	{
		return new TreeDataProvider<T>(provider)
		{
			@Override
			protected boolean iterateChildren(T object)
			{
				return TableTree.this.getState(object) == State.EXPANDED;
			}
		};
	}

	public IColumn<T>[] getColumns()
	{
		return columns;
	}

	/**
	 * @see DataTable
	 */
	public void addBottomToolbar(AbstractToolbar toolbar)
	{
		addToolbar(toolbar, bottomToolbars);
	}

	/**
	 * @see DataTable
	 */
	public void addTopToolbar(AbstractToolbar toolbar)
	{
		addToolbar(toolbar, topToolbars);
	}

	private void addToolbar(AbstractToolbar toolbar, RepeatingView container)
	{
		if (toolbar == null)
		{
			throw new IllegalArgumentException("argument [toolbar] cannot be null");
		}

		if (!toolbar.getId().equals(TOOLBAR_COMPONENT_ID))
		{
			throw new IllegalArgumentException(
					"Toolbar must have component id equal to AbstractDataTable.TOOLBAR_COMPONENT_ID");
		}

		toolbar.setRenderBodyOnly(true);

		// create a container item for the toolbar (required by repeating view)
		WebMarkupContainer item = new ToolbarContainer(container.newChildId());
		item.setRenderBodyOnly(true);
		item.add(toolbar);

		container.add(item);
	}

	public final int getRowCount()
	{
		return datagrid.getRowCount();
	}

	public int getPageCount()
	{
		return datagrid.getPageCount();
	}

	public void setCurrentPage(int page)
	{
		datagrid.setCurrentPage(page);
	}

	public int getCurrentPage()
	{
		return datagrid.getCurrentPage();
	}

	/**
	 * @return number of rows per page
	 */
	public int getRowsPerPage()
	{
		return datagrid.getRowsPerPage();
	}

	/**
	 * Sets the number of items to be displayed per page
	 * 
	 * @param items
	 *            number of items to display per page
	 * 
	 */
	public void setRowsPerPage(int items)
	{
		datagrid.setRowsPerPage(items);
	}

	/**
	 * @see DataTable
	 */
	protected Item<ICellPopulator<T>> newCellItem(final String id, final int index,
			final IModel<ICellPopulator<T>> model)
	{
		return new Item<ICellPopulator<T>>(id, index, model);
	}

	/**
	 * @see DataTable
	 */
	protected Item<T> newRowItem(final String id, int index, final IModel<T> model)
	{
		Item<T> item = new Item<T>(id, index, model);

		return item;
	}

	/**
	 * @see DataTable
	 */
	private final class ToolbarContainer extends WebMarkupContainer
	{
		private static final long serialVersionUID = 1L;

		private ToolbarContainer(String id)
		{
			super(id);
		}

		@Override
		public boolean isVisible()
		{
			return ((Component)iterator().next()).isVisible();
		}
	}

	/**
	 * @see DataTable
	 */
	private class ToolbarsContainer extends RepeatingView
	{
		private static final long serialVersionUID = 1L;

		private ToolbarsContainer(String id)
		{
			super(id);
		}

		@Override
		public boolean isVisible()
		{
			// only visible if at least one child is visible
			final boolean[] visible = new boolean[] { false };
			visitChildren(new IVisitor<Component>()
			{

				public Object component(Component component)
				{
					if (component.isVisible())
					{
						visible[0] = true;
						return STOP_TRAVERSAL;
					}
					else
					{
						return CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER;
					}
				}

			});
			return visible[0];
		}
	}

	/**
	 * Overriden to update the complete row item of the node.
	 * 
	 * @see #newRowItem(String, int, IModel)
	 */
	@Override
	public void updateNode(T t, final AjaxRequestTarget target)
	{
		if (target != null)
		{
			final IModel<T> model = getProvider().model(t);
			visitChildren(Item.class, new IVisitor<Item<T>>()
			{
				public Object component(Item<T> item)
				{
					NodeModel<T> nodeModel = (NodeModel<T>)item.getModel();

					if (model.equals(nodeModel.getWrappedModel()))
					{
						target.addComponent(item);
						return IVisitor.STOP_TRAVERSAL;
					}
					return IVisitor.CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER;
				}
			});
			model.detach();
		}
	}
}