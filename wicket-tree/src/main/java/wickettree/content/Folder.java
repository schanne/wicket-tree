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
package wickettree.content;

import org.apache.wicket.model.IModel;

import wickettree.AbstractTree;
import wickettree.ITreeProvider;
import wickettree.AbstractTree.State;

/**
 * A typical folder representation of nodes in a tree.
 * 
 * @author Sven Meier
 */
public class Folder<T> extends StyledLinkLabel<T>
{

	private static final long serialVersionUID = 1L;

	private AbstractTree<T> tree;

	@SuppressWarnings("unchecked")
	public Folder(String id, AbstractTree<T> tree, IModel<T> model)
	{
		super(id, model);

		this.tree = tree;
	}

	/**
	 * Delegates to others methods depending wether the given model is a folder,
	 * expanded, collapsed or selected.
	 * 
	 * @see ITreeProvider#hasChildren(Object)
	 * @see AbstractTree#getState(Object)
	 * @see #isSelected()
	 * @see #getOpenStyleClass()
	 * @see #getClosedStyleClass()
	 * @see #getOtherStyleClass(Object)
	 * @see #getSelectedStyleClass()
	 */
	protected String getStyleClass()
	{
		T t = getModelObject();

		String styleClass;
		if (tree.hasChildren(t))
		{
			if (tree.getState(t) == State.EXPANDED)
			{
				styleClass = getOpenStyleClass();
			}
			else
			{
				styleClass = getClosedStyleClass();
			}
		}
		else
		{
			styleClass = getOtherStyleClass(t);
		}

		if (isSelected())
		{
			styleClass += " " + getSelectedStyleClass();
		}

		return styleClass;
	}

	/**
	 * Optional attribute which decides if an additional "selected" style class
	 * should be rendered.
	 * 
	 * @return defaults to <code>false</code>
	 */
	protected boolean isSelected()
	{
		return false;
	}

	protected String getOtherStyleClass(T t)
	{
		return "folder-other";
	}

	protected String getClosedStyleClass()
	{
		return "folder-closed";
	}

	protected String getOpenStyleClass()
	{
		return "folder-open";
	}

	protected String getSelectedStyleClass()
	{
		return "selected";
	}
}