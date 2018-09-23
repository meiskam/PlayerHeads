# PlayerHeads
Bukkit Plugin - Drops a player's head when s/he dies, also mob heads
* Drop player heads on player death
* Drop mob heads on death
* Configure drop rate chances
* Configure whether player-kills are required to drop

All original credit goes to meiskam for creating the plugin and zand, Dragoboss, any many others for maintaining it.

# 4.x Branch
Same simple plugin, new improvements.

This is my personal revision attempting to modernize parts of the PlayerHeads plugin, it contains some significant behavior changes.

Credit to MagmaVoid_ for going through every single mob to find a texture.

**If you want a version supporting 1.13+ that tries to maintain the old behavior, see the [3.x](https://github.com/crashdemons/PlayerHeads/tree/3.x) branch or any 3.x releases** - These are builds that I have added code shimming to the original code to work with 1.13 with minimal changes.


## What's New?
### Textured Mobheads
* Old, hard-to-maintain, hard-to-replace username-based heads are on the way *out*. ("Legacy Heads")
* Heads with texture-urls are *in* for good. (easily searchable on many sites)
* **Legacy heads are still recognized by the 4.x plugin, and when broken are converted to updated versions.**
### All current mobs supported
* Want a head from a Dolphin? A Phantom? How about a Cod? You can get one now.
### 1.13 and 1.13.1-ready
* You need one of the shiniest versions if you want all these new heads.
### New configuration options
* Control whether vanilla heads drop for support mobs
* Control whether heads should be converted to/from vanilla mobheads.
### More consistent behavior
* `canbehead` and `canbeheadmob` permissions now actually work when `pkonly`/`mobpkonly` are set, allowing better control over configuration.
* Settings now match entity names
### More abuse-resistant
* Clicking heads to display information now prevents users from spamming the action (prevents possible performance hit)

## Should I use 3.x or 4.x?
* 4.x supports new mobs
* 4.x has more reliable head texturing that needs to be updated less often
* 4.x's code introduces some configuration changes and behaviors different from 3.12/3.x
* 3.x has less significant changes to the plugin and is more likely to have consistent behavior to old 3.10 / 3.12 versions
* 3.x is more likely to have head skins go missing or be abruptly changed by associated usernames.
