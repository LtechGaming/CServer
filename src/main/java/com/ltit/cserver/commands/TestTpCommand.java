package com.ltit.cserver.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

public class TestTpCommand extends Command {
    public TestTpCommand(){
        super("testtp");
        setDefaultExecutor((sender, context) -> {
            if(sender instanceof Player player){
                player.teleport(new Pos(Double.MAX_VALUE, 100, 0));
            }
        });
    }
}
