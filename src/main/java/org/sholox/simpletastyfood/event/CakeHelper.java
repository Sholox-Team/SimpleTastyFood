package org.sholox.simpletastyfood.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import org.sholox.simpletastyfood.Simpletastyfood;

//@EventBusSubscriber(modid = Simpletastyfood.MOD_ID)
//public class CakeHelper {
//    @SubscribeEvent
//    public static void playerUsedItemOnBlockEvent(UseItemOnBlockEvent event) {
//        Block block = event.getLevel()
//                .getBlockState(event.getPos())
//                .getBlock();
//
//        if (block == Blocks.CAKE) {
//            Player player = event.getPlayer();
//
//            System.out.println("USED ON CAKE");
//        }
//    }
//}