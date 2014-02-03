vaadin-filtertablestate
=======================

Extend FilterTable component with FilterTableState extension to access the following capabilities: 
-Save the state of table including filters, sorting, the order and visibility and width of columns. 
-Set a default profile 
-Delete a profile 
-Reset the table to it's original state 
-Add functions on server side 
-Set a default function on server side 
-Keep the existing column switching function 

You have to implement FilterTableStateHandler interface which handle the saving process of profiles into a database. You can implement FilterTableClickFunctionHandler interface optionally.


!!!IMPORTANT!!!

In the module descriptor you have to inherit this widget BEFORE com.vaadin.DefaultWidgetSet, because it uses com.google.gwt.query.Query.
See: https://github.com/gwtquery/gwtquery/issues/213
