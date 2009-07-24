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
package wickettree.table;

import org.apache.wicket.model.IModel;

/**
 * @author Sven Meier
 */
public class TreeModel<T> implements IModel<T>
{

	private static final long serialVersionUID = 1L;

	private IModel<T> model;

	private boolean[] branches;

	public TreeModel(IModel<T> model, boolean[] branches)
	{
		this.model = model;
		this.branches = branches;
	}

	public IModel<T> getWrappedModel()
	{
		return model;
	}

	public T getObject()
	{
		return model.getObject();
	}

	public void setObject(T object)
	{
		throw new UnsupportedOperationException();
	}

	public void detach()
	{
		model.detach();
	}

	public int getDepth()
	{
		return branches.length;
	}

	public boolean[] getBranches()
	{
		return branches;
	}

	@Override
	public int hashCode()
	{
		return model.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof TreeModel<?>)
		{
			return this.model.equals(((TreeModel<?>)obj).model);
		}
		return false;
	}
}