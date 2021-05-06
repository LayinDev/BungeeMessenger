package bungeemessenger.xyz.equinoxdev.xyz.commands;

import bungeemessenger.xyz.equinoxdev.xyz.BMSGR;
import bungeemessenger.xyz.equinoxdev.xyz.ConfigManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.apache.commons.lang3.ArrayUtils;

import javax.jws.HandlerChain;
import java.util.*;
import java.util.stream.Collectors;

public class ignoreCommand extends Command implements TabExecutor {

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
            List<ProxiedPlayer> players;
            if(!ignored.containsKey(author)){
                players = new ArrayList<>();
                players.add(target);
                ignored.put(author, players);
                author.sendMessage(cm.getString("messages.ignoredUser"));

            } else {

                players = ignored.get(author);
                if(!players.contains(target)){

                    players.add(target);
                    ignored.remove(author);
                    ignored.put(author, players);
                    author.sendMessage(cm.getString("messages.ignoredUser"));

                } else {

                    players.remove(target);
                    ignored.remove(author);
                    ignored.put(author, players);
                    author.sendMessage(cm.getString("messages.allowedUser"));

                }

            }
            return;

        }


    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args){

        if(args.length <= 0){
            return Collections.emptyList();
        }
        List<ProxiedPlayer> players = BMSGR.getInstance().getProxy().getPlayers().stream().filter(player -> player.getName().startsWith(args[0])).collect(Collectors.toList());
        List<String> results = new ArrayList<>();
        players.forEach(player -> results.add(player.getName()));
        players.clear();
        return results;
    }

}
