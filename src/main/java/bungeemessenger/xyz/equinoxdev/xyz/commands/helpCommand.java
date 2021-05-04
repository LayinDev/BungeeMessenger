package bungeemessenger.xyz.equinoxdev.xyz.commands;

import bungeemessenger.xyz.equinoxdev.xyz.BMSGR;
import bungeemessenger.xyz.equinoxdev.xyz.ConfigManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class helpCommand extends Command {

 public helpCommand(){
     super("msghelp");
 }

    @Override
    public void execute(CommandSender sender, String[] args) {

        ConfigManager cm = BMSGR.getConfig();

        if(!sender.hasPermission(cm.getString("permissions.use"))){
            sender.sendMessage(cm.getString("messages.noPermission"));
            return;
        }

        List<String> lines = cm.getStringList("messages.helpMessage");
        String string = "";
        for(String line : lines){
            string = string + line + '\n';
        }
        sender.sendMessage(string);

    }
}
