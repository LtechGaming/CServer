package com.ltit.cserver.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.location.RelativeVec;

public class TpCommand extends Command {
    public TpCommand(){
        super("tp", "teleport");
        var pos = ArgumentType.RelativeBlockPosition("xyz");
        addSyntax((sender, context) -> {
            if(sender instanceof Player player){
                sender.sendMessage("Success");
                sender.sendMessage(context.get("xyz").toString());
                player.teleport(new Pos(context.get("xyz")));
                //var test = new Pos(new RelativeVec(new Vec(0 ,0 ,0), RelativeVec.CoordinateType.LOCAL, true, true, true));
            }
        }, pos);
    }
}
