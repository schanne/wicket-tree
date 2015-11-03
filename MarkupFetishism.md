Say we want to render a very simple tree with three nodes, a folder **A** containing a file **AA**.

Following is the what [NestedTree](http://code.google.com/p/wicket-tree/source/browse/trunk/wicket-tree/src/main/java/wickettree/NestedTree.java) is rendering (id, href, onclick attributes removed for brevity):

```
<ul class="tree">
	<li class="branch-last" >
		<div class="node">
			<a class="junction-expanded">
				<span class="sign">-</span> 
			</a>
			<span class="content">
				<a class="folder-open">
					<span class="label">A</span>
				</a>
			</span>
		</div>
	
		<ul class="subtree">
			<li class="branch-last">
				<div class="node">
					<span class="junction">
						<em></em> 
					</span>
					<span class="content">
						<a class="folder-other">
							<span class="label">AA</span>
						</a>
					</span>
				</div>
				
				<ul class="subtree">
				</ul>
			</li>
		</ul>
	</li>
</ul>
```

And here is the output of Wicket's standard tree implementation:

```
<div class="my-tree">
	<table class="wicket-tree-content">
		<tr>
			<td class="half-line">
				<a class="junction-open" />
			</td>
			<td>
				<table class="icon-panel">
					<tr>
						<td>
							<img src="../resources/org.apache.wicket.markup.html.tree.LabelIconPanel/res/folder-open.gif" class="icon"/>
						</td>
						<td class="content">
							<a>
								<span class="content">A</span>
							</a>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<table class="wicket-tree-content">
		<tr>
			<td class="spacer">
				<span/>
			</td>
			<td class="half-line">
				<a class="junction-corner"/>
			</td>
			<td>
				<table class="icon-panel">
					<tr>
						<td>
							<img src="../resources/org.apache.wicket.markup.html.tree.LabelIconPanel/res/item.gif" class="icon"/>
						</td>
						<td class="content">
							<a>
								<span class="content">AA</span>
							</a>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</div>
```

With this obsession in tabular markup you would expect the Wicket `TreeTable` component to utilize `<table>` tags, but it uses heavily styled `<div>` tags instead.

Here is [TableTree](http://code.google.com/p/wicket-tree/source/browse/trunk/wicket-tree/src/main/java/wickettree/TableTree.java)'s markup, using `<table>` tags as you'd expect it:

```
<table class="tree">
	<tbody>
		<tr>
			<td class="tree">
				<div class="branch-last">
					<div class="node">
						<a class="junction-expanded">
							<span class="sign">-</span> 
						</a>
						<span class="content">
							<a class="folder-open">
								<span class="label">A</span>
							</a>
						</span>
					</div>
				</div>
			</td>
			<td>
				... further columns
			</td>
		</tr>
		<tr>
			<td class="tree">
				<div class="branch-last">
					<div class="subtree">
						<div class="branch-last">
							<div class="node">
								<span class="junction">
									<em></em>
								</span>
								<span class="content">
									<a class="folder-other">
										<span class="label">AAA</span>
									</a>
								</span>
							</div>
						</div>
					</div>
				</div>
			</td>
			<td>
				... further columns
			</td>
		</tr>
	</tbody>
</table>
```

'nough said.