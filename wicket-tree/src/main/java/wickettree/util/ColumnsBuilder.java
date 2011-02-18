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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;

/**
 * A builder of columns for row type <code>T</code>.
 */
public class ColumnsBuilder<T> implements Iterable<IColumn<T>>
{
	private List<IColumn<T>> columns = new ArrayList<IColumn<T>>();

	/**
	 * Add the given column.
	 * 
	 * @param column
	 *            column to add
	 * @return this to allow chaining
	 */
	public ColumnsBuilder<T> add(IColumn<T> column)
	{
		columns.add(column);

		return this;
	}

	/**
	 * Iterator over all columns.
	 */
	public Iterator<IColumn<T>> iterator()
	{
		return columns.iterator();
	}
	
	/**
	 * Get all columns as array.
	 * 
	 * @return columns
	 */
	@SuppressWarnings("unchecked")
	public IColumn<T>[] toArray()
	{
		return columns.toArray(new IColumn[columns.size()]);
	}
}
