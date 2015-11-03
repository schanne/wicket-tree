With over 10 years experience in Swing programming I dare to say that [JTree](http://java.sun.com/javase/6/docs/api/javax/JTree.html) isn't an outstanding component of this toolkit.

Rather than that it's clumsy to program with, and implementing a [TreeModel](http://java.sun.com/javase/6/docs/api/javax/swing/TreeModel.html) is still a daunting task:
  * constructing tree paths requires you to know a node's parent which is often not available,
  * change notifications are hard to get right (just look for special cases in [TreeModelListener's](http://java.sun.com/javase/6/docs/api/javax/swing/event/TreeModelListener.html) javadoc).

Now mix in problems we encounter when working with rich web interfaces:
  * web applications have to minimize state so you probably have to work with different node instances across several request (see IDetachable#detach()),
  * on an AJAX request all components to be redrawn have to be added explicitly to the AJAX response (see AjaxRequestTarget#addComponent()).

The result is the overly complicated tree component we have in Wicket today.

In case you really want to reuse a [TreeModel](http://java.sun.com/javase/6/docs/api/javax/swing/TreeModel.html) in your Wicket application you can use an [adapter class](http://code.google.com/p/wicket-tree/source/browse/trunk/wicket-tree/src/main/java/wickettree/provider/TreeModelProvider.java).
(Disclaimer: I didn't bother to test this out as I cannot imagine anyone reusing an existing tree model implementation with Wicket - if you do, please let me know.)