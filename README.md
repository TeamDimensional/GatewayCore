# Gateway Core

This mod includes various features used in Dimension Gateway that could not be made through Groovyscript.

Gateway Core is fully open source under BSD 3-clause license. You can include it in any modpacks, fork it, or steal code from it as long as you're crediting me.

## Features

* JEI handler for CoFH World generation
  * This only supports a small subset of possible generators. More will be added if they're needed for Dimension Gateway.
* JEI handler for Immersive Engineering multiblocks
* JEI handler for Roots Flower Growth Ritual
* Item tier system (configurable)
  * All items and fluids have a tier number, which defaults to 1.
  * Tiers can be set through IDs or predicates, on items or fluids.
  * Setting the tier to 0 removes the tooltips. Setting the tier to a negative number makes it "Future Content".
  * Items can also be "gated" or "unlock a tier". These are purely cosmetic and don't do anything by itself.
  * In case of items with fluids (i.e. Bucket of Ethanol), the higher tier is chosen between the item itself and the fluid inside of it.
  * JEI will also show tiers as tooltips on fluid items (i.e. Ethanol).
  * By default, no tiers are set. This is intended to be configured with an interface such as Groovyscript.
* Item tooltip system
  * Tooltips can be set through IDs or predicates, on items or fluids. Any number of tooltips can be added to the same item. 
  * By default, no tooltips are set. This is intended to be configured with an interface such as Groovyscript.
* The Tinker's Construct materials system reimagined for the pack
  * Replaces materials: Wood, Flint, Netherrack, Bone (configurable)
  * Removes all other materials registered by other mods, except for whitelisted ones (configurable)
  * Adds materials for various mods (configurable):
    * The Aurorian: Silentwood, Aurorian Stone, Crystal
    * Botania: Livingwood, Livingrock
    * Essentialcraft IV Unofficial: Elemental Block
    * Nature's Aura: Ancient Wood
  * (This list will be expanded over time as the pack is developed)

## Tweaks

* Stops Block Drops from caching the drops whenever the list of installed mods changes (configurable).
* Adds fluid ID tooltips to JEI when advanced tooltips are enabled (configurable).
* Stops Tinker's Construct from scanning all crafting recipes during post-init (configurable).

## Bugfixes

* Shows all Tinker's Construct materials in the book, not just the ones that have a head part registered.

## Credits

* Tinker's Construct devs (I borrowed some code/assets from their code base)
