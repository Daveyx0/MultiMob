package net.daveyx0.multimob.client;

import javax.annotation.Nullable;

import net.daveyx0.multimob.client.renderer.MMRenderManager;
import net.daveyx0.multimob.common.MMCommonProxy;
import net.daveyx0.multimob.core.MMReference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value= Side.CLIENT, modid = MMReference.MODID)
public class MMClientProxy extends MMCommonProxy {
		
		private final Minecraft MINECRAFT = Minecraft.getMinecraft();
			@Override
		    public void preInit(FMLPreInitializationEvent event) {
		        OBJLoader.INSTANCE.addDomain(MMReference.MODID);
		        MinecraftForge.EVENT_BUS.register(MMItemModelManager.INSTANCE);
		    }

		    @Override
		    public void init(FMLInitializationEvent event) {
		    	
		    }

		    @Override
		    public void postInit(FMLPostInitializationEvent event) 
		    {
		    	MMRenderManager.init();
		    }
		    
			@Nullable
			@Override
			public EntityPlayer getClientPlayer() {
				return MINECRAFT.player;
			}

			@Nullable
			@Override
			public World getClientWorld() {
				return MINECRAFT.world;
			}

			@Override
			public IThreadListener getThreadListener(final MessageContext context) {
				if (context.side.isClient()) {
					return MINECRAFT;
				} else {
					return context.getServerHandler().player.mcServer;
				}
			}

			@Override
			public EntityPlayer getPlayer(final MessageContext context) {
				if (context.side.isClient()) {
					return MINECRAFT.player;
				} else {
					return context.getServerHandler().player;
				}
			}
		    
	}