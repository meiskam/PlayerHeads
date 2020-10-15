# PlayerHeads
Bukkit Plugin - Drops a player's head when s/he dies, also mob heads
* Drop player heads on player death
* Drop mob heads on death
* Configure drop rate chances
* Configure whether player-kills are required to drop

Same simple plugin, new improvements.

All credit goes to meiskam for creating the plugin and zand, Dragoboss, any many others for maintaining it.

# Core behavior / API
This is the core code branch of Playerheads, which provides the basic working code of the plugin, interfaces for server version compatibility, and an event API for external plugins to use - as well as API documentation.

While this branch controls the basic function of the plugin, the implementation branches determine how those functions are performed on specific servers, as defined by the compatibility interfaces (those branches "provide compatibility").


## What's New since 3.x?
### Textured Mobheads
* Old, hard-to-maintain, hard-to-replace username-based heads ("Legacy Heads") are on the way *out*. 
* Heads with texture-urls are *in* for good. (easily searchable on many sites)
* **Legacy heads are still recognized by the 4.x plugin, and when broken are converted to updated versions.**
### All current 1.13 mobs supported
* Want a head from a Dolphin? A Phantom? How about a Cod? You can get one now.
### 1.13 through 1.13.2-ready
* You need one of the shiniest versions if you want all these new heads.
### New configuration options
* Control whether vanilla heads drop for supported mobs
* Control whether heads should be converted to/from vanilla mobheads.
### More consistent behavior
* `canbehead` and `canbeheadmob` permissions now actually work when `pkonly`/`mobpkonly` are set, allowing better control over configuration.
* Settings now match entity names
### More abuse-resistant
* Clicking heads to display information now prevents users from spamming the action (prevents possible performance hit)
