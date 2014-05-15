TODO
====

### Exiting the cell.
- Probably the hardest.
- As you can see inside of TeleporterRegistry, I started making a system for it, and then got REALLY tired, so, all yours. :P
- Needs to retain the position of the block that owns that cell, contained within chunkSet of TileDimensionalPocket.
- So, you have the position of the block that is clicked to want to go out, if you have a system that registers the position of the tileEntities that HAVE that position, you can reverse engineer the coordSet to get out.
- That's really it.
- If you didn't understand that.
- The chunkSet with TileDimensionalPocket contains the chunk data for the cell.
- Now when you're in the cell, and right click a frame block, you pass the coordinates to the method onBlockActivated which I've put in BlockDimensionalPocketFrame already.
- You can find the chunk coordinates from that.
- And if you retain what tileEntity took what chunk coordinate set, then you should be able to spawn at the block.
- Look at my helper class for tips on traversing the world.
	
### Portal code.
- The portal code works, but barely.
- Doesn't spawn you in any desirable position.
 
### The Lights.
- The blocks are supposed to have light given the light from the world.
- Good luck. :P
	
### ~~Investigate the frame rate tank.~~
- Jezza, you derp. Think about what could have caused your fps drop
 - Hint: it's not your codes fault
	
### Textures for frame block.
- Gel, ask the boys what textures they want for the frame, etc.
- I don't care what it look like.

### ~~Smoother gen for the dimension.~~
- ~~NPE, look at the gen code for the dimension.~~
- ~~The code for populating the chunk lies in ChunkGeneratorPocket.provideChunk(int x, int z);~~
- Done. Just generating rooms as needed. Creating a room takes below 1ms on my machine. - NPE

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
