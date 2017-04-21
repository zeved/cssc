# cssc
CSS checker - _work in progress_

- accepts 2 arguments, a path to the CSS stylesheet file, and a path to a folder containing the HTML-based files (blade, PHP)
- uses JSoup and CSSParser packages
- prints out the number of unused ID and class definitions

_known bugs_: 
  - fails with some fancy CSS rules
  - probably (*most likely*) doesn't scan JavaScript inside HTML files for CSS classes / IDs
