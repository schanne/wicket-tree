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
package wickettree.theme;

import org.apache.wicket.request.resource.CompressedResourceReference;

/**
 * @author Sven Meier
 */
public class WindowsTheme extends CompressedResourceReference
{
	private static final long serialVersionUID = 1L;

	public WindowsTheme()
	{
		super(WindowsTheme.class, "windows/theme.css");
	}
}
