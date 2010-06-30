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
package wickettree.examples;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.time.Duration;

import wickettree.DefaultNestedTree;
import wickettree.util.TimeoutTreeProvider;

/**
 * @author Sven Meier
 */
public class TimeoutPage extends ExamplePage
{

	private static final long serialVersionUID = 1L;

	private FooProvider provider = new FooProvider(true);

	public TimeoutPage()
	{
		final TimeoutTreeProvider<Foo> timeoutProvider = new TimeoutTreeProvider<Foo>(provider,
				Duration.seconds(5));

		add(new DefaultNestedTree<Foo>("tree", timeoutProvider)
		{
			/**
			 * Overriden to bind provider to the content component.
			 */
			@Override
			protected Component newContentComponent(String id, IModel<Foo> model)
			{
				return timeoutProvider.bind(super.newContentComponent(id, model));
			}
		});
	}
}