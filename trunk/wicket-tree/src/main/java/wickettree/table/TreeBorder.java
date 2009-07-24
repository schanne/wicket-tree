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

import org.apache.wicket.Component;
import org.apache.wicket.IComponentBorder;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;

/**
 * @author Sven Meier
 */
public class TreeBorder implements IComponentBorder
{

	private static final long serialVersionUID = 1L;

	private boolean[] branches;

	public TreeBorder(boolean[] branches) {
		this.branches = branches;
	}
	
	public void renderBefore(Component component)
	{
		Response response = RequestCycle.get().getResponse();

		for (int i = 0; i < branches.length; i++)
		{
			if (i > 0)
			{
				response.write("<div class=\"subtree\">");
			}

			if (branches[i])
			{
				response.write("<div class=\"branch\">");
			}
			else
			{
				response.write("<div class=\"branch-last\">");
			}
		}
	}

	public void renderAfter(Component component)
	{
		Response response = RequestCycle.get().getResponse();

		for (int i = 0; i < branches.length; i++)
		{
			response.write("</div>");
		}
	}
}
