package net.daveyx0.multimob.message;

import net.daveyx0.multimob.network.MMNetworkWrapper;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class MMMessageRegistry {

	private static int id = 0;
	public static MMNetworkWrapper networkWrapper;
	
	public static void preInit()
	{
		networkWrapper = new MMNetworkWrapper("multimob");
		registerMessages();
	}
	
	public static void registerMessages()
	{
		registerMessage(MessageMMParticle.Handler.class, MessageMMParticle.class, Side.CLIENT);
		registerMessage(MessageMMTameable.Handler.class, MessageMMTameable.class, Side.CLIENT);
		registerMessage(MessageMMVariant.Handler.class, MessageMMVariant.class, Side.CLIENT);
	}

	public static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side receivingSide) {
		
		networkWrapper.network.registerMessage(messageHandler, requestMessageType, id++, receivingSide);
	}
	
	public static SimpleNetworkWrapper getNetwork()
	{
		return networkWrapper.network;
	}
	
	public static MMNetworkWrapper getWrapper()
	{
		return networkWrapper;
	}
}
