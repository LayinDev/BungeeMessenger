package bungeemessenger.xyz.equinoxdev.xyz.commands;

import bungeemessenger.xyz.equinoxdev.xyz.BMSGR;
import bungeemessenger.xyz.equinoxdev.xyz.ConfigManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.lang3.ArrayUtils;

import javax.jws.HandlerChain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ignoreCommand extends Command {

    public ignoreCommand(){
        super("ignore");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        ConfigManager cm = BMSGR.getConfig();
        HashMap<ProxiedPlayer, Boolean> status = BMSGR.getStatus();

        if(!(sender instanceof ProxiedPlayer)){
            sender.sendMessage(cm.getString("messages.playerOnly"));
            return;
        }

        ProxiedPlayer author = (ProxiedPlayer) sender;
        if (!(author.hasPermission(cm.getString("permissions.use")))) {
            author.sendMessage(cm.getString("messages.noPermission"));
            return;
        }

        if(args.length <= 0){

            Boolean get;
            if (status.containsKey(author)) {

                get = status.get(author);
                if (get) {
                    get = false;
                    author.sendMessage(cm.getString("messages.messagesOff"));
                } else {
                    get = true;
                    author.sendMessage(cm.getString("messages.messagesOn"));
                }
                    status.remove(author);
                    status.put(author, get);
                    return;
                }

                if (!status.containsKey(author)) {
                    get = false;
                    author.sendMessage(cm.getString("messages.messagesOff"));
                    status.put(author, get);
                    return;
                }

        } else {

            ProxiedPlayer target = BMSGR.getInstance().getProxy().getPlayer(args[0]);

            if(author == target){
                author.sendMessage(cm.getString("messages.selfIgnore"));
                return;
            }

            if(target == null){
                author.sendMessage(cm.getString("messages.userNotOnline"));
                return;
            }
            HashMap<ProxiedPlayer, List<ProxiedPlayer>> ignored = BMSGR.getIgnoredPlayers();
            if(!ignored.containsKey(author)){

                List<ProxiedPlayer> players = new ArrayList<>();
                players.add(target);
                ignored.put(author, players);
                author.sendMessage(cm.getString("messages.ignoredUser"));
                return;

            } else {

                List<ProxiedPlayer> players = ignored.get(author);
                if(!players.contains(target)){

                    players.add(target);
                    ignored.remove(author);
                    ignored.put(author, players);
                    author.sendMessage(cm.getString("messages.ignoredUser"));
                    BMSGR.getInstance().getLogger().info(players.toString());

                } else {

                    players.remove(target);
                    ignored.remove(author);
                    ignored.put(author, players);
                    author.sendMessage(cm.getString("messages.allowedUser"));
                    BMSGR.getInstance().getLogger().info(players.toString());

                }
                return;

            }

        }


    }
}
