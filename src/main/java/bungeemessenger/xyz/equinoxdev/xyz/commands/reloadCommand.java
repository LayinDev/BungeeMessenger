package bungeemessenger.xyz.equinoxdev.xyz.commands;

import bungeemessenger.xyz.equinoxdev.xyz.BMSGR;
import bungeemessenger.xyz.equinoxdev.xyz.ConfigManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class reloadCommand extends Command {

    public reloadCommand(){
        super("msgreload");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        ConfigManager cm = BMSGR.getConfig();
        if(!sender.hasPermission(cm.getString("permissions.reload"))){
            sender.sendMessage(cm.getString("messages.noPermission"));
            return;
        }

        cm.init();
        sender.sendMessage(cm.getString("messages.reload"));

    }
}
