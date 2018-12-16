package net.daveyx0.multimob.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class NBTUtil {
	 
	 //Sets blockpos to NBT
	 public static void setBlockPosToNBT(BlockPos pos, String key, NBTTagCompound compound)
	 {
		 compound.setInteger(key + "X", pos.getX());
		 compound.setInteger(key + "Y", pos.getY());
		 compound.setInteger(key + "Z", pos.getZ());
	 }
	 
	 //Gets blockpos from NBT, use with setBlockPosToNBT
	 public static BlockPos getBlockPosFromNBT(String key, NBTTagCompound compound)
	 {
		 int X = compound.getInteger(key + "X");
		 int Y = compound.getInteger(key + "Y");
		 int Z = compound.getInteger(key + "Z");
		 return new BlockPos(X,Y,Z);
	 }
	 
	 //Sets blockstate to NBT
	 public static void setBlockStateToNBT(IBlockState state, String key, NBTTagCompound compound)
	 {
		 int stateID = Block.getStateId(state);
		 compound.setInteger(key + "ID", stateID);
	 }
	 
	 //Receive a blockstate from NBT, use with setBlockStateToNBT
	 public static IBlockState getBlockStateFromNBT(String key, NBTTagCompound compound)
	 {
		 return Block.getStateById(compound.getInteger(key + "ID"));
	 }
	 
}
