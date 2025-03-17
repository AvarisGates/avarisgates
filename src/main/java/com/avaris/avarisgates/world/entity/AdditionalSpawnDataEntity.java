package com.avaris.avarisgates.world.entity;

import com.avaris.avarisgates.AvarisGates;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.Packet;

import javax.swing.text.html.parser.Entity;

public interface AdditionalSpawnDataEntity {
    //Read additional entity data from a buffer (buf)
    void readAdditionalAddEntityData(FriendlyByteBuf buf);

    //Create a custom packet to return
    @SuppressWarnings("unchecked")
    static<T extends Entity & AdditionalSpawnDataEntity> Packet<ClientGamePacketListener> getPacket(T entity, ServerEntity serverEntity) {
        ClientboundAddEntityPacket vanillaPacket = new ClientboundAddEntityPacket(entity, serverEntity);
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        try{
            entity.writeAdditionalAddEntityData(friendlyByteBuf);
            //Idk how we handle networks so this whole file gotta be changed
            Packet<?> packet = AvarisGates.NETWORK.toClientboundPacket(
                    new S2CAddEntityDataMessage(vanillaPacket, friendlyByteBuf.array()).toClientboundMessage()
            );
            return (Packet<ClientGamePacketListener>) packet;
        } finally{
            friendlyByteBuf.release();
        }
    }
    void writeAdditionalAddEntityData(FriendlyByteBuf buf);
}
