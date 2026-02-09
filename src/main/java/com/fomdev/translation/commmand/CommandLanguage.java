package com.fomdev.translation.commmand;

import com.fomdev.sasm.api.PluginClassUtil;
import com.fomdev.translation.api.LangUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandLanguage {
    public static class LanguageCommand implements CommandExecutor {
        private static List<String> bannedLanguages;

        @Override
        public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
            switch (args.length) {
                case 0 -> {
                    return false;
                }

                case 1 -> {
                    if (args[0].equals("reload")) {
                        PluginClassUtil.rescanCache();

                        try {
                            LangUtil.syncDictionaryFromPackages();
                        } catch (Exception ignored) {

                        }
                        return true;

                    }

                    return false;
                }

                case 2 -> {
                    switch (args[0]) {
                        case "disable" -> {
                            if (LangUtil.getCurrentLanguage().equals(args[1])) {
                                sendLangInUseError(sender, args[1]);
                                return true;
                            }

                            if (bannedLanguages.contains(args[1])) {
                                sendLangBannedError(sender, args[1]);
                                return true;
                            }

                            bannedLanguages.add(args[1]);
                            sendBanSuccess(sender, args[1]);
                            return true;
                        }

                        case "enable" -> {
                            if (!bannedLanguages.contains(args[1])) {
                                sendLangNotBannedError(sender, args[1]);
                                return true;
                            }

                            bannedLanguages.remove(args[1]);
                            sendUnbanSuccess(sender, args[1]);
                            return true;
                        }

                        case "switch" -> {
                            if (!sender.isOp()) {
                                sendPermissionError(sender);
                                return true;
                            }

                            if (!LangUtil.hasLanguage(args[1])) {
                                sendLangExistError(sender);
                                return true;
                            }

                            if (bannedLanguages.contains(args[1])) {
                                sendLangBannedError(sender, args[1]);
                                return true;
                            }

                            if (LangUtil.getCurrentLanguage().equals(args[1])) {
                                sendLangInUseError(sender, args[1]);
                                return true;
                            }

                            LangUtil.setLanguage(args[1]);
                            sendTranslateSuccess(sender, args[1]);
                            return true;
                        }

                        default -> {
                            sendInvalidArguments(sender);
                            return false;
                        }
                    }

                }

                default -> {
                    sendInvalidArguments(sender);
                    return false;
                }
            }
        }

        private static void sendBanSuccess(CommandSender sender, String lang) {
            sender.sendMessage(ChatColor.YELLOW + LangUtil.getTranslation("tile.langutil.chat.ban.info") + LangUtil.getTranslation(lang, lang));
        }

        private static void sendInvalidArguments(CommandSender sender) {
            sender.sendMessage(ChatColor.DARK_RED + LangUtil.getTranslation("tile.langutil.chat.invargv.err"));
        }

        private static void sendLangBannedError(CommandSender sender, String lang) {
            sender.sendMessage(ChatColor.RED + LangUtil.getTranslation(lang, lang) + LangUtil.getTranslation("tile.langutil.chat.banned.info"));
        }

        private static void sendLangExistError(CommandSender sender) {
            sender.sendMessage(ChatColor.RED + LangUtil.getTranslation("tile.langutil.chat.langexist.err"));
        }

        private static void sendLangInUseError(CommandSender sender, String lang) {
            sender.sendMessage(ChatColor.RED + LangUtil.getTranslation(lang, lang) + LangUtil.getTranslation("tile.langutil.chat.langinuse.info"));
        }

        private static void sendLangNotBannedError(CommandSender sender, String lang) {
            sender.sendMessage(ChatColor.RED + LangUtil.getTranslation(lang, lang) + LangUtil.getTranslation("tile.langutil.chat.notbanned.info"));
        }

        private static void sendPermissionError(CommandSender sender) {
            sender.sendMessage(ChatColor.DARK_RED + LangUtil.getTranslation("tile.langutil.chat.permission.err"));
        }

        private static void sendTranslateSuccess(CommandSender sender, String lang) {
            sender.sendMessage(ChatColor.GREEN + LangUtil.getTranslation("tile.langutil.chat.translate.info") + LangUtil.getTranslation(lang, lang));
        }

        private static void sendUnbanSuccess(CommandSender sender, String lang) {
            sender.sendMessage(ChatColor.GREEN + LangUtil.getTranslation("tile.langutil.chat.unban.info") + LangUtil.getTranslation(lang, lang));
        }

        static {
            bannedLanguages = new ArrayList<>();
        }
    }

    public static class LanguageCompletion implements TabCompleter {
        @Override
        public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
            switch (args.length) {
                case 1 -> {
                    return List.of
                            (
                                    "disable",
                                    "enable",
                                    "reload",
                                    "switch"
                            );
                }

                case 2 -> {
                    if (args[0].equals("reload")) {
                        return Collections.emptyList();
                    }

                    if (args[0].equals("enable")) {
                        return LanguageCommand.bannedLanguages;
                    }

                    List<String> languages = new ArrayList<>(LangUtil.getAvailableLanguages());
                    languages.removeAll(LanguageCommand.bannedLanguages);
                    return languages;
                }

                default -> {
                    return Collections.emptyList();
                }
            }
        }
    }
}