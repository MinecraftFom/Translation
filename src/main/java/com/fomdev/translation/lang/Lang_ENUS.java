package com.fomdev.translation.lang;

import com.fomdev.translation.api.LangUtil;
import com.fomdev.translation.api.LanguageProvider;
import com.fomdev.translation.api.Translatable;

import java.util.HashMap;
import java.util.Map;

@Translatable
public class Lang_ENUS implements LanguageProvider {
    @Override
    public Map<String, String> getTranslation() {
        return LangUtil.buildDictionaryFromLines(new HashMap<>(),
                "'en_us' : 'English'",
                "'tile.langutil.chat.ban.info'       : 'Successfully Banned The Target Language '",
                "'tile.langutil.chat.banned.info'    : ' Is Already Banned'",
                "'tile.langutil.chat.invargv.err'    : 'Invalid Arguments'",
                "'tile.langutil.chat.langexist.err'  : 'The Target Language Does Not Exist'",
                "'tile.langutil.chat.langinuse.info' : ' Is Already In Use'",
                "'tile.langutil.chat.notbanned.info' : ' Is Not Banned'",
                "'tile.langutil.chat.permission.err' : 'You Don't Have Permission To Do That. This Requires Operator'",
                "'tile.langutil.chat.translate.info' : 'Successfully Translated The Spigot Plugin Language Into '",
                "'tile.langutil.chat.unban.info'     : 'Successfully Unbanned Language '"
        );
    }
}