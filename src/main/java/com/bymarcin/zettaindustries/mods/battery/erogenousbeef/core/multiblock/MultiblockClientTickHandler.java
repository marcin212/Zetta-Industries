package com.bymarcin.zettaindustries.mods.battery.erogenousbeef.core.multiblock;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class MultiblockClientTickHandler {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.START) {
            MultiblockRegistry.tickStart(Minecraft.getMinecraft().theWorld);
        } else if(event.phase == TickEvent.Phase.END) { //Probably could just to else, but better to be safe than sorry
            MultiblockRegistry.tickEnd(Minecraft.getMinecraft().theWorld);
        }
    }
}
