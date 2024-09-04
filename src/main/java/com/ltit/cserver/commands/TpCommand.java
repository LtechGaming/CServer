package com.ltit.cserver.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

public class TpCommand extends Command {
    public TpCommand(){
        super("tp", "teleport");
        var pos = ArgumentType.RelativeBlockPosition("xyz");
        addSyntax((sender, context) -> {
            if(sender instanceof Player player){
                sender.sendMessage("Success");
                player.teleport(new Pos(context.get("xyz")));
            }
        }, pos);
    }
}
