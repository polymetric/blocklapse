package net.nodium.mcplugins.blocklapse;

import de.diddiz.LogBlock.LogBlock;
import de.diddiz.LogBlock.QueryParams;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Consumer;

import java.sql.Date;

public class Timelapse {
    // pointer to the instance of the plugin
    private final Blocklapse blocklapse;
    // pointer to the bukkit scheduler
    private final BukkitScheduler scheduler;
    // pointer to the sender of the command
    private final CommandSender sender;

    private final LogBlock logBlock;

    private QueryParams params;

    // time in the log that the timelapse should star - equivalent to LogBlock's "since" parameter - specified in ms since unix epoch
    private long timeStart;
    // the time that the build was completed - equivalent to LogBlock's "before" parameter - specified in ms since unix epoch
    private long timeEnd;
    // real-time interval between actual calls to LogBlock. specified in game ticks
    private long intervalPlayback;
    // how far should one increment of intervalPlayback move forward on the log? currently specified in minutes - anything finer would require modifications to LogBlock itself
    private long intervalLookup;
    // loop iterator
    private long timeCurrent;

    private boolean paused;

    private boolean stopped;

    public Timelapse(Blocklapse blocklapse, CommandSender sender, long timeStart, long timeEnd) {
        this(blocklapse, sender, timeStart, timeEnd, 1, 1);
    }

    public Timelapse(Blocklapse blocklapse, CommandSender sender, long timeStart, long timeEnd, long intervalPlayback, long intervalLookup) {
        this.blocklapse = blocklapse;
        this.sender = sender;
        this.scheduler = blocklapse.getServer().getScheduler();
        this.logBlock = (LogBlock) blocklapse.getServer().getPluginManager().getPlugin("LogBlock");
        this.params = new QueryParams(logBlock);
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.intervalPlayback = intervalPlayback;
        this.intervalLookup = intervalLookup;
        this.timeCurrent = 0;
        this.paused = false;
        this.stopped = false;
    }

    public void start() {
        sender.sendMessage("Timelapse started!");
        timeCurrent = timeStart;
        scheduler.runTaskTimer(blocklapse, bukkitTask -> {
            if (stopped) {
                bukkitTask.cancel();
                sender.sendMessage("stopped timelapse");
                return;
            }
            try {
                sender.sendMessage(String.valueOf(Utils.minutesAgo(timeStart)));
                sender.sendMessage(String.valueOf(Utils.minutesAgo(timeEnd)));
                sender.sendMessage(String.valueOf(Utils.minutesAgo(timeCurrent)));

                if (timeCurrent >= timeEnd - 1) {
                    bukkitTask.cancel();
                    sender.sendMessage("timelapse finished");
                    return;
                }
                params.since = Utils.minutesAgo(timeCurrent);
                params.before = Utils.minutesAgo(timeCurrent) + 1;
//                    logBlock.getCommandsHandler().new CommandRedo(sender, params, true);
                timeCurrent += 60000;
            } catch (Exception e) {

            }
        }, 0L, intervalPlayback);
    }

    public void stop() {
        stopped = true;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isStopped() {
        return stopped;
    }
}
