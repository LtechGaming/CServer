package com.ltit.cserver.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

public class SpawnCommand extends Command {
    public SpawnCommand(){
        super("spawn");
        setDefaultExecutor((sender, context) -> {
            if(sender instanceof Player player){
                player.teleport(new Pos(0, 102, 0));
            }
        });
    }
}
