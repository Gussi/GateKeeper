Whitelist
=========

The purpose of this plugin is two-fold; First and foremost provide extensible whitelist
mechanism, accepting usernames, ip and ip ranges (cidr to begin with), supporting crud operations
both via commands or from another plugin. Second would be blacklist, to negate any whitelist
entry. 

### Datasources

MySQL only to begin with, we must be able to do changes to the data on the fly, without reloading
anything. This would be possible with flat file format, given that we can monitor time modified
to a given file, and update when needed.

### External datasource

It would be handy if an external plugin could create/read/update/delete a set of information.
Currently there's no support for it.

### Commands

/whitelist (/wl) and /blacklist (/bl), pretty much tabula rasa at the moment for these ones, but
they must be flexible enough for crud operations.


Contributing
------------

Fork it, edit, commit, push, send pull request.
