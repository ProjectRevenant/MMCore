package com.gestankbratwurst.core.mmcore.util;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermission("admin")
@CommandAlias("servermessage|smsg")
public class MsgCommand extends BaseCommand {

  @Subcommand("info")
  @Syntax("<Target> <Message>")
  public void onInfo(CommandSender sender, OnlinePlayer target, String message) {
    Msg.sendInfo(target.getPlayer(), message);
  }

  @Subcommand("warning")
  @Syntax("<Target> <Message>")
  public void onWarning(CommandSender sender, OnlinePlayer target, String message) {
    Msg.sendWarning(target.getPlayer(), message);
  }

  @Subcommand("error")
  @Syntax("<Target> <Message>")
  public void onError(CommandSender sender, OnlinePlayer target, String message) {
    Msg.sendError(target.getPlayer(), message);
  }

}
