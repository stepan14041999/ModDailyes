package ru.stepan1404.daylies.messages;

import java.util.List;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import ru.stepan1404.daylies.Daylies;

public class MessageGui implements IMessage {
    
    private String text;
	private int day;

    public MessageGui() { }
    
    public MessageGui(int day, String text) {
    	this.day = day;
    	this.text = text;
    }

    /*public MessageGui(String text) {
        this.text = text;
    }*/

    @Override
    public void fromBytes(ByteBuf buf) {
    	day = ByteBufUtils.readVarInt(buf, 5);
        text = ByteBufUtils.readUTF8String(buf); // this class is very useful in general for writing more complex objects
    }

    @Override
    public void toBytes(ByteBuf buf) {
    	ByteBufUtils.writeVarInt(buf, day, 5);
        ByteBufUtils.writeUTF8String(buf, text);
    }

    public static class MessageGuiHandler implements IMessageHandler<MessageGui, IMessage> {
    
		@Override
        public IMessage onMessage(MessageGui message, MessageContext ctx) {
            Daylies.proxy.openGui(message.day, message.text);
            return null; // no response in this case
        }
    }
}
