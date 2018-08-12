package net.liggesmeyer.arm.Preseter;

import net.liggesmeyer.arm.Messages;
import net.liggesmeyer.arm.regions.RegionKind;
import net.liggesmeyer.arm.regions.RentRegion;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SellPreset extends Preset{
    protected static final String SET_PRICE = " (?i)sellpreset (?i)price [+-]?([0-9]+[.])?[0-9]+";
    protected static final String REMOVE_PRICE = " (?i)sellpreset (?i)price (?i)remove";
    protected static final String SET_REGIONKIND = " (?i)sellpreset (?i)regionkind [^;\n ]+";
    protected static final String REMOVE_REGIONKIND = " (?i)sellpreset (?i)regionkind (?i)remove";
    protected static final String SET_AUTO_RESET = " (?i)sellpreset (?i)autoreset (false|true)";
    protected static final String REMOVE_AUTO_RESET = " (?i)sellpreset (?i)autoreset (?i)remove";
    protected static final String SET_HOTEL = " (?i)sellpreset (?i)hotel (false|true)";
    protected static final String REMOVE_HOTEL = " (?i)sellpreset (?i)hotel (?i)remove";
    protected static final String SET_DO_BLOCK_RESET = " (?i)sellpreset (?i)doblockreset (false|true)";
    protected static final String REMOVE_DO_BLOCK_RESET = " (?i)sellpreset (?i)doblockreset (?i)remove";
    protected static final String RESET = " (?i)sellpreset (?i)reset";
    protected static final String INFO = " (?i)sellpreset (?i)info";
    protected static ArrayList<SellPreset> list = new ArrayList<>();

    public SellPreset(Player player){
        super(player);
    }

    public static ArrayList<SellPreset> getList(){
        return SellPreset.list;
    }




    public static boolean hasPreset(Player player){
        for(int i = 0; i < getList().size(); i++) {
            if(getList().get(i).getAssignedPlayer() == player) {
                return true;
            }
        }
        return false;
    }

    public static SellPreset getPreset(Player player) {
        for(int i = 0; i < getList().size(); i++) {
            if(getList().get(i).getAssignedPlayer() == player) {
                return getList().get(i);
            }
        }
        return null;
    }

    public static boolean removePreset(Player player){
        for(int i = 0; i < getList().size(); i++) {
            if(getList().get(i).getAssignedPlayer() == player) {
                getList().remove(i);
                return true;
            }
        }
        return false;
    }

    public void getPresetInfo(Player player) {
        String price = "not defined";
        if(this.hasPrice()) {
            price = this.getPrice() + "";
        }
        RegionKind regKind = RegionKind.DEFAULT;
        if(this.hasRegionKind()) {
            regKind = this.getRegionKind();
        }

        player.sendMessage(ChatColor.GOLD + "=========[SellPreset INFO]=========");
        player.sendMessage(Messages.REGION_INFO_PRICE + price);
        player.sendMessage(Messages.REGION_INFO_TYPE + regKind.getName());
        player.sendMessage(Messages.REGION_INFO_AUTORESET + this.isAutoReset());
        player.sendMessage(Messages.REGION_INFO_HOTEL + this.isHotel());
        player.sendMessage(Messages.REGION_INFO_DO_BLOCK_RESET + this.isDoBlockReset());
    }

    public static boolean onCommand(CommandSender sender, String command, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX + Messages.COMMAND_ONLY_INGAME);
            return true;
        }
        Player player = (Player) sender;
        if(command.matches(SET_PRICE)) {
            if(hasPreset(player)) {
                getPreset(player).setPrice(Double.parseDouble(args[2]));
                player.sendMessage(Messages.PRESET_SET);
                return true;
            } else {
                getList().add(new SellPreset(player));
                getPreset(player).setPrice(Double.parseDouble(args[2]));
                player.sendMessage(Messages.PRESET_SET);
                return true;
            }
        } else if(command.matches(REMOVE_PRICE)) {
            if(hasPreset(player)){
                getPreset(player).removePrice();
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            } else {
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            }
        } else if(command.matches(SET_REGIONKIND)) {
            if(RegionKind.kindExists(args[2]) || args[2].equalsIgnoreCase(RegionKind.DEFAULT.getName())){
                RegionKind regkind = RegionKind.getRegionKind(args[2]);
                if(hasPreset(player)) {
                    getPreset(player).setRegionKind(regkind);
                    player.sendMessage(Messages.PRESET_SET);
                } else {
                    getList().add(new SellPreset(player));
                    getPreset(player).setRegionKind(regkind);
                    player.sendMessage(Messages.PRESET_SET);
                }
            } else {
                player.sendMessage(Messages.PREFIX + Messages.REGIONKIND_DOES_NOT_EXIST);
                return true;
            }
        } else if(command.matches(REMOVE_REGIONKIND)) {
            if(hasPreset(player)){
                getPreset(player).removeRegionKind();
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            } else {
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            }
        } else if(command.matches(SET_AUTO_RESET)) {
            if(hasPreset(player)) {
                getPreset(player).setAutoReset(Boolean.parseBoolean(args[2]));
                player.sendMessage(Messages.PRESET_SET);
                return true;
            } else {
                getList().add(new SellPreset(player));
                getPreset(player).setAutoReset(Boolean.parseBoolean(args[2]));
                player.sendMessage(Messages.PRESET_SET);
                return true;
            }
        } else if(command.matches(REMOVE_AUTO_RESET)) {
            if(hasPreset(player)){
                getPreset(player).removeAutoReset();
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            } else {
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            }
        }else if(command.matches(SET_HOTEL)) {
            if(hasPreset(player)) {
                getPreset(player).setHotel(Boolean.parseBoolean(args[2]));
                player.sendMessage(Messages.PRESET_SET);
                return true;
            } else {
                getList().add(new SellPreset(player));
                getPreset(player).setHotel(Boolean.parseBoolean(args[2]));
                player.sendMessage(Messages.PRESET_SET);
                return true;
            }
        } else if(command.matches(REMOVE_HOTEL)) {
            if(hasPreset(player)){
                getPreset(player).removeHotel();
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            } else {
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            }
        } else if(command.matches(RESET)) {
            if(removePreset(player)){
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            } else {
                player.sendMessage(Messages.PRESET_NOT_EXISTING);
                return true;
            }
        } else if(command.matches(INFO)) {
            if(hasPreset(player)){
                getPreset(player).getPresetInfo(player);
                return true;
            } else {
                player.sendMessage(Messages.PRESET_NOT_EXISTING);
                return true;
            }
        } else if(command.matches(SET_DO_BLOCK_RESET)) {
            if(hasPreset(player)) {
                getPreset(player).setDoBlockReset(Boolean.parseBoolean(args[2]));
                player.sendMessage(Messages.PRESET_SET);
                return true;
            } else {
                getList().add(new SellPreset(player));
                getPreset(player).setDoBlockReset(Boolean.parseBoolean(args[2]));
                player.sendMessage(Messages.PRESET_SET);
                return true;
            }
        } else if(command.matches(REMOVE_DO_BLOCK_RESET)) {
            if(hasPreset(player)){
                getPreset(player).removeDoBlockReset();
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            } else {
                player.sendMessage(Messages.PRESET_REMOVED);
                return true;
            }
        } else {
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "Bad syntax! Use:");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm sellpreset reset");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm sellpreset info");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm sellpreset price ([PRICE]/remove)");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm sellpreset regionkind ([REGIONKIND]/remove)");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm sellpreset autoreset (true/false/remove)");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm sellpreset hotel (true/false/remove)");
            player.sendMessage(Messages.PREFIX + ChatColor.DARK_GRAY + "/arm sellpreset doblockreset (true/false/remove)");
        }
        return true;
    }
}
