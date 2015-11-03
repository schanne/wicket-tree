To run wicket-tree-examples, do the following:

  * svn checkout http://wicket-tree.googlecode.com/svn/trunk/ wicket-tree
  * cd wicket-tree
  * mvn clean install
  * cd wicket-tree-examples/
  * mvn jetty:run

Then open http://localhost:8080/ in your browser.

To edit the examples in Eclipse:

  * mvn eclipse:eclipse

Then import the new project into your workspace.

(The Classpath Variable M2\_REPO has to be defined in Eclipse - let it point to your local Maven repository, usually '.m2/repository' inside your home directory.)