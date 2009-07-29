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

import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import wickettree.nested.BranchItem;
import wickettree.nested.Subtree;

/**
 * A tree with nested markup.
 * 
 * @author Sven Meier
 */
public abstract class NestedTree<T> extends AbstractTree<T>
{

	private static final long serialVersionUID = 1L;

	public NestedTree(String id, ITreeProvider<T> provider)
	{
		this(id, provider, null);
	}

	public NestedTree(String id, ITreeProvider<T> provider, IModel<Set<T>> expansion)
	{
		super(id, provider, expansion);

		add(newSubtree("subtree", new RootsModel()));
	}

	/**
	 * Create a new subtree.
	 * 
	 * @param id
	 *            component id
	 * @param model
	 *            the model of the new subtree
	 */
	public Component newSubtree(String id, IModel<T> model)
	{
		return new Subtree<T>(id, this, model);
	}

	/**
	 * Overriden to update the affected branch only.
	 * 
	 * @see #updateBranch(Object, AjaxRequestTarget)
	 */
	@Override
	protected void onStateChanged(T t)
	{
		updateBranch(t, AjaxRequestTarget.get());
	}

	/**
	 * Convenience method to update a branch of a node on an
	 * {@link AjaxRequestTarget}. Does nothing if target is <code>null</code>.
	 * 
	 * @param t
	 * @param target
	 */
	public void updateBranch(T t, final AjaxRequestTarget target)
	{
		if (target != null)
		{
			final IModel<T> model = getProvider().model(t);
			visitChildren(BranchItem.class, new IVisitor<BranchItem<T>>()
			{
				public Object component(BranchItem<T> branch)
				{
					if (model.equals(branch.getModel()))
					{
						target.addComponent(branch);
						return IVisitor.STOP_TRAVERSAL;
					}
					return IVisitor.CONTINUE_TRAVERSAL;
				}
			});
		}
	}

	private class RootsModel extends AbstractReadOnlyModel<T>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public T getObject()
		{
			return null;
		}
	}
}