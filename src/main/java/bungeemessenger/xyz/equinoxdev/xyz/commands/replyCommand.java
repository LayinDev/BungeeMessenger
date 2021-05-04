package bungeemessenger.xyz.equinoxdev.xyz.commands;

import bungeemessenger.xyz.equinoxdev.xyz.BMSGR;
import bungeemessenger.xyz.equinoxdev.xyz.ConfigManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class replyCommand extends Command {

    public replyCommand(){
        super("r");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        ConfigManager cm = BMSGR.getConfig();
        HashMap<ProxiedPlayer, ProxiedPlayer> messages = BMSGR.getMessages();
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

        if(!messages.containsKey(author)){
            sender.sendMessage(cm.getString("messages.noUserReply"));
            return;
        }

        ProxiedPlayer receiver = BMSGR.getInstance().getProxy().getPlayer(messages.get(author).getName());

        if(receiver == null){
            sender.sendMessage(cm.getString("messages.noUserReply"));
            return;
        }

        if(author == receiver){
            author = receiver;
            receiver = (ProxiedPlayer) sender;
        }

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

        if(args.length <= 0){
            sender.sendMessage(cm.getString("messages.missingMessage"));
            return;
        }

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(" ");
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

        if (messages.containsKey(author) && messages.containsKey(receiver)) {
            messages.remove(author);
            messages.remove(receiver);
        }
        messages.put(author, receiver);
        messages.put(receiver, author);


    }
}
