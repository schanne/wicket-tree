A clean slate development of tree components for [Wicket](http://wicket.apache.org).

**News: wicket-tree is part of Wicket 6 !**

### Current solution ###
We identified the following basic issues concerned with the currently available tree components in wicket and wicket-extensions:
  * integration of [TreeModel](http://docs.oracle.com/javase/1.5.0/docs/api/javax/swing/tree/TreeModel.html) (which is not Swing's highlight)
  * cumbersome change notification using Swing's [TreeModelListener](http://docs.oracle.com/javase/1.5.0/docs/api/javax/swing/event/TreeModelListener.html)
  * no nested markup for trees supported
  * no `<table>` markup for tabular trees supported
  * markup limitation for node content ([height problem](https://issues.apache.org/jira/browse/WICKET-1384))
  * duplicated code for handling table [IColumn](http://svn.apache.org/repos/asf/wicket/trunk/wicket-extensions/src/main/java/org/apache/wicket/extensions/markup/html/repeater/data/table/IColumn.java) and tree [IColumn](http://svn.apache.org/repos/asf/wicket/trunk/wicket-extensions/src/main/java/org/apache/wicket/extensions/markup/html/tree/table/IColumn.java)
  * complicated code for partial update rendering

### Proposed redesign ###
The wicket-tree components try to address these problems:
  * simple interface [ITreeProvider](http://code.google.com/p/wicket-tree/source/browse/trunk/wicket-tree/src/main/java/wickettree/ITreeProvider.java) similar to [IDataProvider](http://svn.apache.org/repos/asf/wicket/trunk/wicket-core/src/main/java/org/apache/wicket/markup/repeater/data/IDataProvider.java):
    * support for multiple roots (and no root at all)
    * get a node's children
    * model factory for each node
    * that's all
  * fully generified
  * no change notification required, always-up-to-date rendering like other repeaters
  * nested markup for [NestedTree](http://code.google.com/p/wicket-tree/source/browse/trunk/wicket-tree/src/main/java/wickettree/NestedTree.java)
  * `<table>` markup for [TableTree](http://code.google.com/p/wicket-tree/source/browse/trunk/wicket-tree/src/main/java/wickettree/TableTree.java)
  * arbitrary markup for node content allowed
  * utilizes [DataTable](http://svn.apache.org/repos/asf/wicket/trunk/wicket-extensions/src/main/java/org/apache/wicket/extensions/markup/html/repeater/data/table/DataTable.java) for [TableTree](http://code.google.com/p/wicket-tree/source/browse/trunk/wicket-tree/src/main/java/wickettree/TableTree.java):
    * reuse your column implementations
    * reuse toolbars ([almost](http://code.google.com/p/wicket-tree/source/browse/trunk/wicket-tree/src/main/java/wickettree/table/AbstractToolbar.java))
    * use pagination
    * use sorting
  * partial updates supported for [NestedTree](http://code.google.com/p/wicket-tree/source/browse/trunk/wicket-tree/src/main/java/wickettree/NestedTree.java) using standard AJAX features

To achieve a lightweight solution some features from the original components were rejected:
  * [IRenderable](http://svn.apache.org/repos/asf/wicket/trunk/wicket-extensions/src/main/java/org/apache/wicket/extensions/markup/html/tree/table/IRenderable.java) optimizations not supported
    * if really useful should be added to [DataGridView](http://svn.apache.org/repos/asf/wicket/trunk/wicket-extensions/src/main/java/org/apache/wicket/extensions/markup/html/repeater/data/grid/DataGridView.java)
    * [TableTree](http://code.google.com/p/wicket-tree/source/browse/trunk/wicket-tree/src/main/java/wickettree/TableTree.java) would automatically benefit
  * no caching of tree structure in component structure
    * always see up-to-date data
    * caching is better done in [ITreeProvider](http://code.google.com/p/wicket-tree/source/browse/trunk/wicket-tree/src/main/java/wickettree/ITreeProvider.java) if really needed
  * no selection handling
    * handling state of nodes besides expanse/collapse is irrelevant to a tree implementation
    * better implemented in client code (see examples)
  * no partial updates for [TableTree](http://code.google.com/p/wicket-tree/source/browse/trunk/wicket-tree/src/main/java/wickettree/TableTree.java)
    * Wicket's AJAX doesn't support inserting/removing elements, these features should be added to Wicket core instead of adding it to a single (tree) component
    * [DataTable](http://svn.apache.org/repos/asf/wicket/trunk/wicket-extensions/src/main/java/org/apache/wicket/extensions/markup/html/repeater/data/table/DataTable.java) doesn't support partial updates either