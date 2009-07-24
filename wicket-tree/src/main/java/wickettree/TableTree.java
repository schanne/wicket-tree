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
import org.apache.wicket.markup.repeater.IItemReuseStrategy;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import wickettree.table.AbstractToolbar;
import wickettree.table.ITreeColumn;
import wickettree.table.TreeModel;

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

		datagrid = new DataGridView<T>("rows", columns, new DataProvider())
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
				return TableTree.this.newRowItem(id, index, model);
			}
		};
		datagrid.setRowsPerPage(rowsPerPage);
		add(datagrid);

		topToolbars = new ToolbarsContainer("topToolbars");
		bottomToolbars = new ToolbarsContainer("bottomToolbars");
		add(topToolbars);
		add(bottomToolbars);
	}

	/**
	 * Sets the item reuse strategy. This strategy controls the creation of
	 * {@link Item}s.
	 * 
	 * @see RefreshingView#setItemReuseStrategy(IItemReuseStrategy)
	 * @see IItemReuseStrategy
	 * 
	 * @param strategy
	 *            item reuse strategy
	 * @return this for chaining
	 */
	public final AbstractTree<T> setItemReuseStrategy(IItemReuseStrategy strategy)
	{
		datagrid.setItemReuseStrategy(strategy);

		return this;
	}

	@Override
	public IItemReuseStrategy getItemReuseStrategy()
	{
		return datagrid.getItemReuseStrategy();
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

	public int getRowsPerPage()
	{
		return datagrid.getRowsPerPage();
	}

	private class DataProvider implements IDataProvider<T>, Iterator<T>, Iterable<T>
	{

		private static final long serialVersionUID = 1L;

		private Branch branch;

		private Branch previousBranch;

		private int size = -1;

		@SuppressWarnings("unused")
		public int size()
		{
			if (size == -1)
			{
				size = 0;
				for (T t : this)
				{
					size++;
				}
			}
			return size;
		}

		public Iterator<? extends T> iterator(int first, int count)
		{
			branch = new Branch(null, getProvider().getRoots());

			for (int i = 0; i < first; i++)
			{
				next();
			}

			return this;
		}

		public Iterator<T> iterator()
		{
			branch = new Branch(null, getProvider().getRoots());

			return this;
		}

		public boolean hasNext()
		{
			while (branch != null)
			{
				if (branch.hasNext())
				{
					return true;
				}
				branch = branch.parent;
			}

			return false;
		}

		public T next()
		{
			if (!hasNext())
			{
				throw new IllegalStateException();
			}

			T next = branch.next();

			previousBranch = branch;

			if (getState(next) == State.EXPANDED)
			{
				branch = new Branch(previousBranch, getProvider().getChildren(next));
			}

			return next;
		}

		public IModel<T> model(T object)
		{
			return previousBranch.wrapModel(getProvider().model(object));
		}

		public void remove()
		{
			throw new UnsupportedOperationException();
		}

		public void detach()
		{
			branch = null;
			previousBranch = null;
			size = -1;
		}
	}

	private class Branch implements Iterator<T>
	{
		private Branch parent;

		private Iterator<T> children;

		public Branch(Branch parent, Iterator<T> children)
		{
			this.parent = parent;
			this.children = children;
		}

		public IModel<T> wrapModel(IModel<T> model)
		{
			boolean[] branches = new boolean[getDepth()];

			Branch branch = this;
			for (int c = branches.length - 1; c >= 0; c--)
			{
				branches[c] = branch.hasNext();

				branch = branch.parent;
			}

			return new TreeModel<T>(model, branches);
		}

		public int getDepth()
		{
			if (parent == null)
			{
				return 1;
			}
			else
			{
				return parent.getDepth() + 1;
			}
		}

		public boolean hasNext()
		{
			return children.hasNext();
		}

		public T next()
		{
			if (!hasNext())
			{
				throw new IllegalStateException();
			}

			return children.next();
		}

		public void remove()
		{
			throw new UnsupportedOperationException();
		}
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

		// @see #updateNode(T, AjaxRequestTarget)
		item.setOutputMarkupId(true);

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
	 * Update the complete row item.
	 */
	public void updateNode(T t, final AjaxRequestTarget target)
	{
		if (target != null)
		{
			final IModel<T> model = getProvider().model(t);
			visitChildren(Item.class, new IVisitor<Item<T>>()
			{
				public Object component(Item<T> item)
				{
					TreeModel<T> treeModel = (TreeModel<T>)item.getModel();

					if (model.equals(treeModel.getWrappedModel()))
					{
						target.addComponent(item);
						return IVisitor.STOP_TRAVERSAL;
					}
					return IVisitor.CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER;
				}
			});
		}
	}
}