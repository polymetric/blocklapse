package net.nodium.mcplugins.blocklapse;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.ParseException;

public final class Blocklapse extends JavaPlugin {
    public Timelapse timelapse;

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("blocklapse") || cmd.getName().equalsIgnoreCase("bl")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("currently timelapse from console is not supported, sorry");
            }

            if (args.length < 1) {
                sender.sendMessage("do /blocklapse commands");
                return true;
            }

            if (args[0].equalsIgnoreCase("start")) {
                if (timelapse != null) {
                    if (!timelapse.isStopped()) {
                        sender.sendMessage("timelapse already started");
                    }
                }

                if (args.length != 7) {
                    sender.sendMessage("not enough arguments");
                    return true;
                }

                long timeStart, timeEnd, intervalPlayback, intervalLookup;

                try {
                    timeStart = Utils.parseTime(args[1] + " " + args[2]);
                    timeEnd = Utils.parseTime(args[3] + " " + args[4]);
                    intervalPlayback = Integer.parseInt(args[5]);
                    intervalLookup = Integer.parseInt(args[6]);
                } catch (ParseException e) {
                    sender.sendMessage("can't parse date");
                    return true;
                }

                timelapse = new Timelapse(this, sender, timeStart, timeEnd, intervalPlayback, intervalLookup);
                timelapse.start();

                return true;
            } else if (args[0].equalsIgnoreCase("stop")) {
                if (!timelapse.isStopped()) {
                    timelapse.stop();
                    return true;
                } else {
                    sender.sendMessage("no timelapse started");
                    return true;
                }
            }
        }

        return false;
    }
}
