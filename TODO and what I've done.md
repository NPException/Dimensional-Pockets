TODO
====

### ~~Exiting the cell.~~
- Done. breaking the block, placing it elsewhere, going in and back out works fine, too.

### Persisting the backLinkMap of the TeleportingRegistry
- the methods already exist, but the way i tried it it just derped out.
 - I try tackle that tomorrow - NPE

### Portal code.
- The portal code works, but barely.
- Doesn't spawn you in any desirable position.
 
### The Lights. (very low priority)
- The blocks are supposed to have light given the light from the world.
 - right now they just emit the maximum possible amount of light

### Textures for frame block.
- Gel, ask the boys what textures they want for the frame, etc.
- I don't care what it look like.

### ~~Smoother gen for the dimension.~~
- ~~NPE, look at the gen code for the dimension.~~
- ~~The code for populating the chunk lies in ChunkGeneratorPocket.provideChunk(int x, int z);~~
- Done. Just generating rooms as needed. Creating a room takes below 1ms on my machine. - NPE
 - also fixed an anoying bug

### Book thingy, for information on how to use the mod.
- Nox, I've started making a book or ItemInfoTool, I think.
- Make it a quick intro guide to the mod, how it works, etc.

### Redstone Compatablity.
- NPE or Nox, good luck, should be fairly easy.
- Use the tileEntity as a bridge for the block method that detects if there is a change.
	
### Recipes.
- Speaks for itself again.
	
### Hopefully, all these are done.
- Once all the above are done, and we have enough time, see if we can add support for other mods, etc.
	
Remember, COMMIT OFTEN.
INCLUDING YOU GEL.
