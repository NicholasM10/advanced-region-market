package net.alex9849.arm.SubRegions.commands;

import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.exceptions.InputException;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToolCommand extends BasicArmCommand {
    private final String rootCommand = "tool";
    private final String regex = "(?i)tool";
    private final List<String> usage = new ArrayList<>(Arrays.asList("tool"));

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex);
    }

    @Override
    public String getRootCommand() {
        return this.rootCommand;
    }

    @Override
    public List<String> getUsage() {
        return this.usage;
    }

    @Override
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException {
        if(!sender.hasPermission(Permission.SUBREGION_TOOL)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }
        if (!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;
        player.getInventory().addItem(new ItemStack(Material.FEATHER, 1));
        ItemStack subRegionTool = new ItemStack(Material.FEATHER, 1);
        //TODO Change me
        subRegionTool.getItemMeta().setDisplayName("Subregion Tool");
        player.getInventory().addItem(subRegionTool);
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        return new ArrayList<>();
    }
}
