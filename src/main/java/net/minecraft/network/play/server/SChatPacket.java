package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.UUID;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;

public class SChatPacket implements IPacket<IClientPlayNetHandler> {

	public SChatPacket(ITextComponent message, ChatType type) {

	}

	public SChatPacket(ITextComponent message, ChatType type, UUID uuid) {

	}

	@Override
	public void processPacket(IClientPlayNetHandler arg0) {
	}

	@Override
	public void readPacketData(PacketBuffer arg0) throws IOException {
	}

	@Override
	public void writePacketData(PacketBuffer arg0) throws IOException {
	}
}