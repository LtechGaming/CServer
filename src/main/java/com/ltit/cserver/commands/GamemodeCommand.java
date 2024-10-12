package com.ltit.cserver.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

public class GamemodeCommand extends Command {
    public GamemodeCommand(){
        super("gamemode");
        ArrayList<String> gamemodes = new ArrayList<String>();
        gamemodes.add("creative");
        gamemodes.add("survival");
        gamemodes.add("spectator");
        gamemodes.add("hybrid");
        var mode = ArgumentType.Word("gm");
        addSyntax((commandSender, commandContext) -> {
            if(!gamemodes.contains(commandContext.get("gm").toString())){
                commandSender.sendMessage("Needs to be \"creative\", \"spectator\" or \"survival\"");
            }
            if(commandSender instanceof Player player){
                if(Objects.equals(commandContext.get("gm").toString(), "creative")){
                    player.setGameMode(GameMode.CREATIVE);
                    player.setAllowFlying(true);
                    commandSender.sendMessage("Switched gamemode to creative");
                } else if (Objects.equals(commandContext.get("gm").toString(), "survival")) {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setAllowFlying(false);
                    commandSender.sendMessage("Switched gamemode to survival");
                } else if (Objects.equals(commandContext.get("gm").toString(), "spectator")) {
                    player.setGameMode(GameMode.SPECTATOR);
                    player.setAllowFlying(false);
                    commandSender.sendMessage("Switched gamemode to spectator");
                } else if (Objects.equals(commandContext.get("gm").toString(), "hybrid")) {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setAllowFlying(true);
                    commandSender.sendMessage("Switched gamemode to hybrid");
                } else{
                    commandSender.sendMessage("did not match");
                }
            }
        }, mode);
    }
}
