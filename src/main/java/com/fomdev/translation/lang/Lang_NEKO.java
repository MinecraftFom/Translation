package com.fomdev.translation.lang;

import com.fomdev.translation.api.LangUtil;
import com.fomdev.translation.api.LanguageProvider;
import com.fomdev.translation.api.Translatable;

import java.util.HashMap;
import java.util.Map;

@Translatable(lang = "ne_ko")
public class Lang_NEKO implements LanguageProvider {
    @Override
    public Map<String, String> getTranslation() {
        return LangUtil.buildDictionaryFromLines(new HashMap<>(),
                "'ne_ko' : '喵喵喵～'",
                "'tile.langutil.chat.ban.info'       : '喵！成功地禁用了语言 '",
                "'tile.langutil.chat.banned.info'    : ' 已经被禁用呢喵～'",
                "'tile.langutil.chat.invargv.err'    : '错误的参数欸主人！看看帮助页面把，主人喵～（/language）'",
                "'tile.langutil.chat.langexist.err'  : '目标语言不存在呢主人！请听听我说的提示呢主人～这么杂鱼可不行呢～'",
                "'tile.langutil.chat.langinuse.info' : ' 正在使用中呢， 主人！'",
                "'tile.langutil.chat.notbanned.info' : ' 尚未被禁用哦，主人！'",
                "'tile.langutil.chat.permission.err' : '你没有权限这么做哦，这个可是需要管理员权限的呢喵！'",
                "'tile.langutil.chat.translate.info' : '欢迎来到喵星！ '",
                "'tile.langutil.chat.unban.info'     : '喵！成功的解禁了语言 '"
        );
    }
}