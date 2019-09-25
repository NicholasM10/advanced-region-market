package net.alex9849.arm.entitylimit.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.commands.BasicArmCommand;
import net.alex9849.arm.entitylimit.EntityLimit;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuyExtraCommand implements BasicArmCommand {
    private final String rootCommand = "buyextra";
    private final String regex = "(?i)buyextra [^;\n ]+ [^;\n ]+";
    private final List<String> usage = new ArrayList<>(Arrays.asList("buyextra [REGION] [ENTITYTYPE]"));

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
        if (!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Player player = (Player) sender;
        if (!sender.hasPermission(Permission.MEMBER_ENTITYLIMIT_BUY_EXTRA)) {
            throw new InputException(sender, Messages.NO_PERMISSION);
        }

        Region region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName());

        if(region == null) {
            throw new InputException(player, Messages.REGION_DOES_NOT_EXIST);
        }

        if(!(region.getRegion().hasOwner(player.getUniqueId()) || region.getRegion().hasMember(player.getUniqueId()))) {
            throw new InputException(player, Messages.REGION_NOT_OWN);
        }

        if(region.isSubregion()) {
            throw new InputException(player, Messages.ENTITYLIMITGROUP_EXTRA_ENTITIES_BUY_SUBREGION_ERROR);
        }

        List<Entity> entities = region.getFilteredInsideEntities(false, true,
                true, true, true, true,
                false, false, false);

        if(args[2].equalsIgnoreCase("total")) {

            if(region.getEntityLimitGroup().getHardLimit() <= region.getEntityLimitGroup().getSoftLimit(region.getExtraTotalEntitys())) {
                throw new InputException(player, region.getEntityLimitGroup().getConvertedMessage(Messages.ENTITYLIMITGROUP_EXTRA_ENTITIES_HARDLIMIT_REACHED, entities, region.getExtraTotalEntitys()));
            }
            if(AdvancedRegionMarket.getInstance().getEcon().getBalance(player) < region.getEntityLimitGroup().getPricePerExtraEntity()) {
                throw new InputException(player, Messages.NOT_ENOUGHT_MONEY);
            }
            AdvancedRegionMarket.getInstance().getEcon().withdrawPlayer(player, region.getEntityLimitGroup().getPricePerExtraEntity());
            region.setExtraTotalEntitys(region.getExtraTotalEntitys() + 1);
            player.sendMessage(Messages.PREFIX + region.getEntityLimitGroup().getConvertedMessage(Messages.ENTITYLIMITGROUP_EXTRA_ENTITIES_EXPAND_SUCCESS, entities, region.getExtraTotalEntitys()));

        } else {

            EntityLimit.LimitableEntityType limitableEntityType = EntityLimit.getLimitableEntityType(args[2]);
            if(limitableEntityType == null) {
                throw new InputException(player, Messages.ENTITYTYPE_DOES_NOT_EXIST.replace("%entitytype%", args[2]));
            }
            EntityLimit entityLimit = region.getEntityLimitGroup().getEntityLimit(limitableEntityType);
            if(entityLimit == null) {
                throw new InputException(player, region.getEntityLimitGroup().getConvertedMessage(Messages.ENTITYLIMITGROUP_ENTITYLIMIT_ALREADY_UNLIMITED, entities, region.getExtraEntityAmount(limitableEntityType)));
            }
            if(region.getEntityLimitGroup().getHardLimit(limitableEntityType) <= region.getEntityLimitGroup().getSoftLimit(limitableEntityType, region.getExtraEntityAmount(limitableEntityType))) {
                throw new InputException(player, entityLimit.getConvertedMessage(Messages.ENTITYLIMITGROUP_EXTRA_ENTITIES_HARDLIMIT_REACHED, entities, region.getExtraEntityAmount(limitableEntityType)));
            }
            if(AdvancedRegionMarket.getInstance().getEcon().getBalance(player) < region.getEntityLimitGroup().getPricePerExtraEntity(limitableEntityType)) {
                throw new InputException(player, Messages.NOT_ENOUGHT_MONEY);
            }
            AdvancedRegionMarket.getInstance().getEcon().withdrawPlayer(player, region.getEntityLimitGroup().getPricePerExtraEntity(limitableEntityType));
            region.setExtraEntityAmount(limitableEntityType, region.getExtraEntityAmount(limitableEntityType) + 1);
            player.sendMessage(Messages.PREFIX + entityLimit.getConvertedMessage(Messages.ENTITYLIMITGROUP_EXTRA_ENTITIES_EXPAND_SUCCESS, entities, region.getExtraEntityAmount(limitableEntityType)));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();
        if (!player.hasPermission(Permission.MEMBER_ENTITYLIMIT_BUY_EXTRA)) {
            return returnme;
        }

        if(args.length >= 1) {
            if(args.length == 1) {
                if (this.rootCommand.startsWith(args[0])) {
                    returnme.add(this.rootCommand);
                }
            } else if((args.length == 2) && (args[0].equalsIgnoreCase(this.rootCommand))) {
                if (this.rootCommand.startsWith(args[0])) {
                    returnme.addAll(AdvancedRegionMarket.getInstance().getRegionManager().completeTabRegions(player, args[1], PlayerRegionRelationship.OWNER, true, false));

                }
            } else if((args.length == 3) && (args[0].equalsIgnoreCase(this.rootCommand))) {
                for(EntityLimit.LimitableEntityType entityType : EntityLimit.entityTypes) {
                    if(entityType.toString().toLowerCase().startsWith(args[2])) {
                        Region region = AdvancedRegionMarket.getInstance().getRegionManager().getRegionbyNameAndWorldCommands(args[1], player.getWorld().getName());
                        if(region != null) {
                            if(region.getEntityLimitGroup().containsLimit(entityType)) {
                                returnme.add(entityType.toString());
                            }
                        } else {
                            returnme.add(entityType.toString());
                        }
                    }
                }
                if("total".startsWith(args[2])) {
                    returnme.add("total");
                }
            }
        }
        return returnme;
    }
}