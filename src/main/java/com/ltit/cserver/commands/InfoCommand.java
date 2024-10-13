package com.ltit.cserver.commands;

import net.minestom.server.command.builder.Command;

public class InfoCommand extends Command {
    public InfoCommand(){
        super("info", "serverinfo");
        setDefaultExecutor(((sender, context) -> {
            sender.sendMessage("LTIT CServer Beta 0.0.2");
        }));
    }
}
