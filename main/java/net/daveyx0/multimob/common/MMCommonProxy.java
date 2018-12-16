package net.daveyx0.multimob.common;

import javax.annotation.Nullable;

import net.daveyx0.multimob.core.MMReference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

@Mod.EventBusSubscriber(modid = MMReference.MODID)
public class MMCommonProxy
{


public void preInit(FMLPreInitializationEvent event) {

}

public void init(FMLInitializationEvent event) {

}

public void postInit(FMLPostInitializationEvent event) {

}


@Nullable
public EntityPlayer getClientPlayer(){
	throw new RuntimeException ("Tried to get the client player on the dedicated server");
}

@Nullable
public World getClientWorld(){
	throw new RuntimeException ("Tried to get the client world on the dedicated server");
}


public IThreadListener getThreadListener(final MessageContext context) {
	if (context.side.isServer()) {
		return context.getServerHandler().player.mcServer;
	} else {
		throw new RuntimeException ("Tried to get the IThreadListener from a client-side MessageContext on the dedicated server");
	}
}


public EntityPlayer getPlayer(final MessageContext context){
	if (context.side.isServer()) {
		return context.getServerHandler().player;
	} else {
		throw new RuntimeException ("Tried to get the player from a client-side MessageContext on the dedicated server");
	}
}
}