package bungeemessenger.xyz.equinoxdev.xyz.commands;

import bungeemessenger.xyz.equinoxdev.xyz.BMSGR;
import bungeemessenger.xyz.equinoxdev.xyz.ConfigManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class msgCommand extends Command {

    public msgCommand() {
        super("msg");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        ConfigManager cm = BMSGR.getConfig();

        if(!(sender instanceof ProxiedPlayer)){
            sender.sendMessage(cm.getString("messages.playerOnly"));
            return;
        }

        ProxiedPlayer author = (ProxiedPlayer) sender;

        if(args.length <= 0 || args[0].isEmpty()){

            author.sendMessage(cm.getString("messages.missingPlayer"));
            return;

        }

        ProxiedPlayer receiver = BMSGR.getInstance().getProxy().getPlayer(args[0]);

        if (!(author.hasPermission(cm.getString("permissions.use")))) {

            author.sendMessage(cm.getString("messages.noPermission"));
            return;

        }

        if(receiver == null){
            author.sendMessage(cm.getString("messages.userNotOnline"));
            return;
        }

        HashMap<ProxiedPlayer, Boolean> status = BMSGR.getStatus();
        HashMap<ProxiedPlayer, List<ProxiedPlayer>> ignored = BMSGR.getIgnoredPlayers();
        Boolean blocked;
        if(ignored.containsKey(receiver)){

            List<ProxiedPlayer> list = ignored.get(receiver);
            if(list.contains(author)){
                blocked = true;
            } else {
                blocked = false;
            }

        } else {
            blocked = false;
        }

        if(blocked){
            author.sendMessage(cm.getString("messages.disabledMessages"));
            return;
        }

        Boolean get;
        if(status.containsKey(receiver)){
            get = status.get(receiver);
            if(!get){
                get = false;
            } else {
                get = true;
            }
        } else {
            get = true;
        }

        if(!get){
            author.sendMessage(cm.getString("messages.disabledMessages"));
            return;
        }

        if(args.length < 2){

            author.sendMessage(cm.getString("messages.missingMessage"));
            return;

        }

        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < args.length; i++) {
            if (i > 1) sb.append(" ");
            sb.append(args[i]);
        }
        String msg = sb.toString();

        if(author == receiver){
            author.sendMessage(cm.getString("messages.selfMessage"));
            return;
        }

        author.sendMessage(
                cm.getString("format.sender")
                .replace("<receiver>", receiver.getName())
                .replace("<message>", msg)
                .replace("<sender>", author.getName())
        );

        receiver.sendMessage(
                cm.getString("format.receiver")
                .replace("<receiver>", receiver.getName())
                .replace("<message>", msg)
                .replace("<sender>", author.getName())
        );

        HashMap<ProxiedPlayer, ProxiedPlayer> messages = BMSGR.getMessages();
        if (messages.containsKey(author) && messages.containsKey(receiver)) {
            messages.remove(author);
            messages.remove(receiver);
        }
        messages.put(author, receiver);
        messages.put(receiver, author);

    }
}
