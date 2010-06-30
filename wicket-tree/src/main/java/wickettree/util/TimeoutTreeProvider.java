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

import java.util.Collections;
import java.util.Iterator;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.time.Duration;

import wickettree.AbstractTree;
import wickettree.ITreeProvider;
import wickettree.nested.BranchItem;
import wickettree.provider.ProviderSubset;

/**
 * Wrapper of a ITreeProvider handling timeouts when accessing childrens of a
 * node.
 * 
 * @see #getChildren(Object)
 * @see #bind(Component)
 */
public class TimeoutTreeProvider<T> implements ITreeProvider<T>
{

	private ITreeProvider<T> provider;

	/**
	 * All previously timed out nodes.
	 */
	private ProviderSubset<T> timedOut;

	private Duration delay;

	/**
	 * Wrap the given provider.
	 * 
	 * @param provider
	 *            provider to wrap
	 * @param delay
	 *            delay to re-access node's children after a {@link Timeout}
	 */
	public TimeoutTreeProvider(ITreeProvider<T> provider, Duration delay)
	{
		this.provider = provider;
		this.delay = delay;

		timedOut = new ProviderSubset<T>(provider);
	}

	/**
	 * Has access to the given object's children previously timed out.
	 */
	public boolean hasTimedOut(T t)
	{
		return timedOut.contains(t);
	}

	public Iterator<? extends T> getRoots()
	{
		return provider.getRoots();
	}

	public boolean hasChildren(T object)
	{
		return provider.hasChildren(object);
	}

	/**
	 * Delegate to the wrapped {@link ITreeProvider}, handling possible
	 * {@link Timeout}s.
	 */
	public Iterator<? extends T> getChildren(T t)
	{
		try
		{
			return provider.getChildren(t);
		}
		catch (Timeout timeout)
		{
			timedOut.add(t);

			return Collections.<T> emptyList().iterator();
		}
	}

	public IModel<T> model(T object)
	{
		return provider.model(object);
	}

	public void detach()
	{
		provider.detach();

		timedOut.detach();
	}

	/**
	 * Bind to the given node component to retry access to children in case of a
	 * previous {@link Timeout}.
	 * 
	 * @see AbstractTree#newNodeComponent(String, IModel)
	 */
	public Component bind(Component component)
	{
		component.add(new AbstractDefaultAjaxBehavior()
		{
			@SuppressWarnings("unchecked")
			@Override
			public void renderHead(IHeaderResponse response)
			{
				super.renderHead(response);

				T t = (T)getComponent().getDefaultModelObject();
				if (hasTimedOut(t))
				{
					response.renderOnLoadJavascript(getJsTimeoutCall());
				}
			}

			protected final String getJsTimeoutCall()
			{
				return "setTimeout(\"" + getCallbackScript() + "\", " + delay.getMilliseconds()
						+ ");";
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void respond(AjaxRequestTarget target)
			{
				T t = (T)getComponent().getDefaultModelObject();

				timedOut.remove(t);

				target.addComponent(getComponent().findParent(BranchItem.class));
			}
		});

		return component;
	}

	/**
	 * Exception to be thrown by wrapped {@link ITreeProvider} if access to a
	 * node's children times out.
	 * 
	 * {@link ITreeProvider#getChildren(Object)}
	 */
	public static final class Timeout extends WicketRuntimeException
	{
	}
}