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
package wickettree.provider;

import java.util.Iterator;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import junit.framework.TestCase;

/**
 * Test for {@link TreeModelProvider}.
 * 
 * @author Sven Meier
 */
public class TreeModelProviderTest extends TestCase
{
	private TreeModel treeModel;

	public TreeModelProviderTest()
	{
		treeModel = TreeAccess.getDefaultTreeModel();
	}

	public void test() throws Exception
	{
		TreeModelProvider<DefaultMutableTreeNode> provider = new TreeModelProvider<DefaultMutableTreeNode>(
				treeModel) {
			@Override
			public IModel<DefaultMutableTreeNode> model(DefaultMutableTreeNode object)
			{
				return Model.of(object);
			}
		};
		
		Iterator<DefaultMutableTreeNode> roots = provider.getRoots();
		assertTrue(roots.hasNext());
		roots.next();
		assertFalse(roots.hasNext());
	}
	
	private static class TreeAccess extends JTree {
		public static TreeModel getDefaultTreeModel()
		{
			return JTree.getDefaultTreeModel();
		}
	}
}
