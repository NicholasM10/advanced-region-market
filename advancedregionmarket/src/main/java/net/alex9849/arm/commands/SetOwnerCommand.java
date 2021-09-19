package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.CmdSyntaxException;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.handler.CommandHandler;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetOwnerCommand extends BasicArmCommand {

    public SetOwnerCommand(AdvancedRegionMarket plugin) {
        super(false, plugin, "setowner",
                Arrays.asList("(?i)setowner [^;\n ]+ [^;\n ]+"),
                Arrays.asList("setowner [REGION] [PLAYER]"),
                Arrays.asList(Permission.ADMIN_SETOWNER));
    }

    @Override
    protected boolean runCommandLogic(CommandSender sender, String command, String commandLabel) throws InputException, CmdSyntaxException {
        if(!(sender instanceof ConsoleCommandSender))
        {
            Player playersender = (Player) sender;
            String[] args = command.split(" ");

            Region region = getPlugin().getRegionManager()
                    .getRegionbyNameAndWorldCommands(args[1], playersender.getWorld().getName());
            if (region == null) {
                throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
            }

            OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
            if (player == null) {
                throw new InputException(sender, "Player not found!");
            }

            region.setSold(player);
            sender.sendMessage(Messages.PREFIX + "Owner set!");
            return true;
        } else {
            //Player playersender = (Player) sender;
            String[] args = command.split(" ");

            OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
            if (player == null) {
                throw new InputException(sender, "Player not found!");
            }

            Region region = getPlugin().getRegionManager()
                    .getRegionbyNameAndWorldCommands(args[1], player.getPlayer().getWorld().getName());
            if (region == null) {
                throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
            }

            region.setSold(player);
            Bukkit.getServer().getConsoleSender().sendMessage(Messages.PREFIX + "Owner set!");;
            //sender.sendMessage(Messages.PREFIX + "Owner set!");
            return true;
        }
    }

    @Override
    protected List<String> onTabCompleteArguments(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (args.length == 2) {
            PlayerRegionRelationship playerRegionRelationship = null;
            if (player.hasPermission(Permission.ADMIN_SETOWNER)) {
                playerRegionRelationship = PlayerRegionRelationship.ALL;
            } else {
                playerRegionRelationship = PlayerRegionRelationship.OWNER;
            }
            returnme.addAll(getPlugin().getRegionManager()
                    .completeTabRegions(player, args[1], playerRegionRelationship, true, true));

        } else if (args.length == 3) {
            returnme.addAll(CommandHandler.tabCompleteOnlinePlayers(args[2]));
        }
        return returnme;
    }
}
