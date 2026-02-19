package com.fomdev.translation.lang;

import com.fomdev.translation.api.LangUtil;
import com.fomdev.translation.api.LanguageProvider;
import com.fomdev.translation.api.Translatable;

import java.util.HashMap;
import java.util.Map;

@Translatable(lang = "zh_cn")
public class Lang_ZHCN implements LanguageProvider {
    @Override
    public Map<String, String> getTranslation() {
        return LangUtil.buildDictionaryFromLines(new HashMap<>(),
                "'zh_cn' : '中文'",
                "'tile.langutil.chat.ban.info'       : '成功地禁用了语言 '",
                "'tile.langutil.chat.banned.info'    : ' 已经被禁用了'",
                "'tile.langutil.chat.invargv.err'    : '错误的参数'",
                "'tile.langutil.chat.langexist.err'  : '目标语言不存在'",
                "'tile.langutil.chat.langinuse.info' : ' 正在使用中'",
                "'tile.langutil.chat.notbanned.info' : ' 尚未被禁用哦'",
                "'tile.langutil.chat.permission.err' : '你没有权限这么做哦，这个可是需要管理员权限的呢'",
                "'tile.langutil.chat.translate.info' : '成功的将Spigot插件（包含本插件的附属）的语言切换为 '",
                "'tile.langutil.chat.unban.info'     : '成功的解禁了语言 '"
        );
    }
}