package com.timvisee.dungeonmaze.populator.maze.spawner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.timvisee.dungeonmaze.DungeonMaze;
import com.timvisee.dungeonmaze.api.DungeonMazeAPI;
import com.timvisee.dungeonmaze.event.generation.DMGenerationSpawnerCause;
import com.timvisee.dungeonmaze.event.generation.DMGenerationSpawnerEvent;
import com.timvisee.dungeonmaze.populator.maze.DMMazeBlockPopulator;
import com.timvisee.dungeonmaze.populator.maze.DMMazeBlockPopulatorArgs;

public class BlazeSpawnerRoomPopulator extends DMMazeBlockPopulator {
	public static final int MIN_LAYER = 1;
	public static final int MAX_LAYER = 4;
	public static final int CHANCE_OF_SPANWER_ROOM = 2; //Promile
	public static final double CHANCE_OF_SPANWER_ROOM_ADDITION_PER_LEVEL = -0.167; /* to 1 */
	public static final double MIN_SPAWN_DISTANCE = 5; // Chunks

	@Override
	public void populateMaze(DMMazeBlockPopulatorArgs args) {
		World w = args.getWorld();
		Chunk c = args.getSourceChunk();
		Random rand = args.getRandom();
		int x = args.getChunkX();
		int y = args.getChunkY();
		int yFloor = args.getFloorY();
		int yCeiling = args.getCeilingY();
		int z = args.getChunkZ();
		
		// Make sure the distance between the spawn and the current chunk is allowed
		if(distance(0, 0, c.getX(), c.getZ()) < MIN_SPAWN_DISTANCE)
			return;
		
		// Apply chances
		if (rand.nextInt(1000) < CHANCE_OF_SPANWER_ROOM + (CHANCE_OF_SPANWER_ROOM_ADDITION_PER_LEVEL * (y - 30) / 6)) {
			
			// Register the current room as constant room
			DungeonMaze.instance.registerConstantRoom(w.getName(), c, x, y, z);
			
			// Netherbrick floor in the bottom of the room
			for(int xx = x; xx <= x + 7; xx += 1)
	            for(int zz = z; zz <= z + 7; zz += 1)
	                c.getBlock(xx, yFloor, zz).setTypeId(112);
			
			// Cobblestone layer underneeth the stone floor
			for(int xx = x + 0; xx <= x + 7; xx += 1)
	            for(int zz = z + 1; zz <= z + 6; zz += 1)
	                c.getBlock(xx, yFloor - 1, zz).setTypeId(4);
			
			// Break out the walls and things inside the room
			for (int xx = 0; xx < 8; xx++)
				for (int yy = yFloor + 1; yy < yCeiling; yy++)
					for(int zz = 0; zz < 8; zz++)
						c.getBlock(x + xx, yy, z + zz).setTypeId(0);
			
			// Generate corners
			for(int yy = yFloor + 1; yy < yCeiling; yy++) {
				c.getBlock(x + 0, yy, z + 0).setTypeId(112);
				c.getBlock(x + 7, yy, z + 0).setTypeId(112);
				c.getBlock(x + 0, yy, z + 7).setTypeId(112);
				c.getBlock(x + 7, yy, z + 7).setTypeId(112);
			}
			
			// Generate fences in the corners
			for(int yy = yFloor + 1; yy < yCeiling; yy++) {
				c.getBlock(x + 1, yy, z + 0).setTypeId(113);
				c.getBlock(x + 0, yy, z + 1).setTypeId(113);
				c.getBlock(x + 6, yy, z + 0).setTypeId(113);
				c.getBlock(x + 7, yy, z + 1).setTypeId(113);
				c.getBlock(x + 1, yy, z + 7).setTypeId(113);
				c.getBlock(x + 0, yy, z + 6).setTypeId(113);
				c.getBlock(x + 6, yy, z + 7).setTypeId(113);
				c.getBlock(x + 7, yy, z + 6).setTypeId(113);
			}
			
			// Generate platform in the middle
			for (int xx=x + 2; xx <= x + 5; xx+=1)
	            for (int zz=z + 2; zz <= z + 5; zz+=1)
	                c.getBlock(xx, yFloor + 1, zz).setTypeId(112);
			
			// Generate stairs off the platform
			c.getBlock(x + 3, yFloor + 1, z + 2).setTypeId(114);
			c.getBlock(x + 3, yFloor + 1, z + 2).setData((byte) 2);
			c.getBlock(x + 4, yFloor + 1, z + 2).setTypeId(114);
			c.getBlock(x + 4, yFloor + 1, z + 2).setData((byte) 2);
			
			c.getBlock(x + 3, yFloor + 1, z + 5).setTypeId(114);
			c.getBlock(x + 3, yFloor + 1, z + 5).setData((byte) 3);
			c.getBlock(x + 4, yFloor + 1, z + 5).setTypeId(114);
			c.getBlock(x + 4, yFloor + 1, z + 5).setData((byte) 3);
			
			c.getBlock(x + 2, yFloor + 1, z + 3).setTypeId(114);
			c.getBlock(x + 2, yFloor + 1, z + 3).setData((byte) 0);
			c.getBlock(x + 2, yFloor + 1, z + 4).setTypeId(114);
			c.getBlock(x + 2, yFloor + 1, z + 4).setData((byte) 0);
			
			c.getBlock(x + 5, yFloor + 1, z + 3).setTypeId(114);
			c.getBlock(x + 5, yFloor + 1, z + 3).setData((byte) 1);
			c.getBlock(x + 5, yFloor + 1, z + 4).setTypeId(114);
			c.getBlock(x + 5, yFloor + 1, z + 4).setData((byte) 1);

			// Generate poles on the platform
			c.getBlock(x + 2, yFloor + 2, z + 2).setTypeId(113);
			c.getBlock(x + 5, yFloor + 2, z + 2).setTypeId(113);
			c.getBlock(x + 2, yFloor + 2, z + 5).setTypeId(113);
			c.getBlock(x + 5, yFloor + 2, z + 5).setTypeId(113);
			
			// Generate the spawner
			if (DungeonMazeAPI.allowMobSpawner("Blaze")) {
				int spawnerX = x + 3 + rand.nextInt(2);
				int spawnerY = yFloor + 2;
				int spawnerZ = z + 3 + rand.nextInt(2);
				Block spawnerBlock = c.getBlock(spawnerX, spawnerY, spawnerZ);
				spawnerBlock = c.getBlock(spawnerX, spawnerY, spawnerZ);
				
				// Call the spawner generation event
				DMGenerationSpawnerEvent event = new DMGenerationSpawnerEvent(spawnerBlock, EntityType.BLAZE, DMGenerationSpawnerCause.BLAZE_SPAWNER_ROOM, rand);
				Bukkit.getServer().getPluginManager().callEvent(event);
				
				// Make sure the event isn't cancelled yet
				if(!event.isCancelled()) {
					// Change the block into a creature spawner
					spawnerBlock.setTypeId(52);
					
					// Cast the created s pawner into a CreatureSpawner object
					CreatureSpawner theSpawner = (CreatureSpawner) spawnerBlock.getState();
					
					// Set the spawned type of the spawner
					theSpawner.setSpawnedType(event.getSpawnedType());
				}
			}
		
			// Generate hidden content/recourses underneath the platform
			Block block1 = c.getBlock(x + 3, yFloor, z + 3);
			block1.setTypeId(54);
			Chest chestBlock1 = (Chest) block1.getState();
			addItemsToChest(rand, chestBlock1);

			Block block2 = c.getBlock(x + 4, yFloor, z + 4);
			block2.setTypeId(54);
			Chest chestBlock2 = (Chest) block2.getState();
			addItemsToChest(rand, chestBlock2);
		}
	}
	
	public void addItemsToChest(Random random, Chest chest) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		if(random.nextInt(100) < 80) {
			items.add(new ItemStack(50, 4, (short) 0));
		}
		if(random.nextInt(100) < 40) {
			items.add(new ItemStack(50, 8, (short) 0));
		}
		if(random.nextInt(100) < 20) {
			items.add(new ItemStack(50, 12, (short) 0));
		}
		if(random.nextInt(100) < 40) {
			items.add(new ItemStack(260, 1, (short) 0));
		}
		if(random.nextInt(100) < 10) {
			items.add(new ItemStack(262, 16, (short) 0));
		}
		if(random.nextInt(100) < 5) {
			items.add(new ItemStack(262, 24, (short) 0));
		}
		if(random.nextInt(100) < 20) {
			items.add(new ItemStack(264, 1, (short) 0));
		}
		if(random.nextInt(100) < 50) {
			items.add(new ItemStack(265, 1, (short) 0));
		}
		if(random.nextInt(100) < 60) {
			items.add(new ItemStack(266, 1, (short) 0));
		}
		if(random.nextInt(100) < 10) {
			items.add(new ItemStack(267, 1, (short) 0));
		}
		if(random.nextInt(100) < 40) {
			items.add(new ItemStack(268, 1, (short) 0));
		}
		if(random.nextInt(100) < 20) {
			items.add(new ItemStack(272, 1, (short) 0));
		}
		if(random.nextInt(100) < 80) {
			items.add(new ItemStack(296, 1, (short) 0));
		}
		if(random.nextInt(100) < 10) {
			items.add(new ItemStack(296, 2, (short) 0));
		}
		if(random.nextInt(100) < 5) {
			items.add(new ItemStack(296, 3, (short) 0));
		}
		if(random.nextInt(100) < 20) {
			items.add(new ItemStack(297, 1, (short) 0));
		}
		if(random.nextInt(100) < 20) {
			items.add(new ItemStack(298, 1, (short) 0));
		}
		if(random.nextInt(100) < 20) {
			items.add(new ItemStack(299, 1, (short) 0));
		}
		if(random.nextInt(100) < 20) {
			items.add(new ItemStack(300, 1, (short) 0));
		}
		if(random.nextInt(100) < 20) {
			items.add(new ItemStack(301, 1, (short) 0));
		}
		if(random.nextInt(100) < 40) {
			items.add(new ItemStack(302, 1, (short) 0));
		} 
		if(random.nextInt(100) < 40) {
			items.add(new ItemStack(303, 1, (short) 0));
		}
		if(random.nextInt(100) < 40) {
			items.add(new ItemStack(304, 1, (short) 0));
		}
		if(random.nextInt(100) < 40) {
			items.add(new ItemStack(305, 1, (short) 0));
		}
		if(random.nextInt(100) < 10) {
			items.add(new ItemStack(306, 1, (short) 0));
		}
		if(random.nextInt(100) < 10) {
			items.add(new ItemStack(307, 1, (short) 0));
		}
		if(random.nextInt(100) < 10) {
			items.add(new ItemStack(308, 1, (short) 0));
		}
		if(random.nextInt(100) < 10) {
			items.add(new ItemStack(309, 1, (short) 0));
		}
		if(random.nextInt(100) < 30) {
			items.add(new ItemStack(318, 3, (short) 0));
		}
		if(random.nextInt(100) < 20) {
			items.add(new ItemStack(318, 5, (short) 0));
		}
		if(random.nextInt(100) < 10) {
			items.add(new ItemStack(318, 7, (short) 0));
		}
		if(random.nextInt(100) < 80) {
			items.add(new ItemStack(319, 1, (short) 0));
		}
		if(random.nextInt(100) < 10) {
			items.add(new ItemStack(320, 1, (short) 0));
		}
		if(random.nextInt(100) < 15) {
			items.add(new ItemStack(331, 5, (short) 0));
		}
		if(random.nextInt(100) < 10) {
			items.add(new ItemStack(331, 8, (short) 0));
		}
		if(random.nextInt(100) < 5) {
			items.add(new ItemStack(331, 13, (short) 0));
		}
		if(random.nextInt(100) < 3) {
			items.add(new ItemStack(331, 21, (short) 0));
		}
		if(random.nextInt(100) < 10) {
			items.add(new ItemStack(345, 1, (short) 0));
		}
		if(random.nextInt(100) < 80) {
			items.add(new ItemStack(349, 1, (short) 0));
		}
		if(random.nextInt(100) < 20) {
			items.add(new ItemStack(350, 1, (short) 0));
		}
		if(random.nextInt(100) < 20) {
			items.add(new ItemStack(350, 1, (short) 0));
		}
		if(random.nextInt(100) < 20) {
			items.add(new ItemStack(351, 1, (short) 3));
		}
		if(random.nextInt(100) < 5) {
			items.add(new ItemStack(354, 1, (short) 0));
		}
		if(random.nextInt(100) < 80) {
			items.add(new ItemStack(357, 3, (short) 0));
		}
		if(random.nextInt(100) < 20) {
			items.add(new ItemStack(357, 5, (short) 0));
		}
		
		int itemCountInChest = 3;
		switch (random.nextInt(8)) {
		case 0:
			itemCountInChest = 2;
			break;
		case 1:
			itemCountInChest = 2;
			break;
		case 2:
			itemCountInChest = 3;
			break;
		case 3:
			itemCountInChest = 3;
			break;
		case 4:
			itemCountInChest = 3;
			break;
		case 5:
			itemCountInChest = 4;
			break;
		case 6:
			itemCountInChest = 4;
			break;
		case 7:
			itemCountInChest = 5;
			break;
		default:
			itemCountInChest = 3;
			break;
		}
		
		// Add the selected items to a random place inside the chest
		for (int i = 0; i < itemCountInChest; i++) {
			chest.getInventory().setItem(random.nextInt(chest.getInventory().getSize()), items.get(random.nextInt(items.size())));
		}
		chest.update();
	}
	
	public double distance(int x1, int y1, int x2, int y2) {
		double dx   = x1 - x2;         //horizontal difference 
		double dy   = y1 - y2;         //vertical difference 
		double dist = Math.sqrt( dx*dx + dy*dy ); //distance using Pythagoras theorem
		return dist;
	}
	
	/**
	 * Get the minimum layer
	 * @return Minimum layer
	 */
	@Override
	public int getMinimumLayer() {
		return MIN_LAYER;
	}
	
	/**
	 * Get the maximum layer
	 * @return Maximum layer
	 */
	@Override
	public int getMaximumLayer() {
		return MAX_LAYER;
	}
}