package com.kyra.expensemanager;

import android.support.annotation.NonNull;
import android.text.TextUtils;

public class SentenceCase {

    public SentenceCase() {
    }

    public String caseConvertCapWords(String str){
        String[] strWord = str.split(" ");
        for (int i = 0; i < strWord.length; i++) {
            String word = strWord[i];
            strWord[i] = properCase(word);
//            Log.d("Word",word);
        }
        str = TextUtils.join(" ",strWord);
//        Log.d("Appended String",str);
        return str;
    }

    @NonNull
    private String properCase (@NonNull String str) {
        // Empty strings should be returned as-is.

        if (str.length() == 0) return "";

        // Strings with only one character upperCased.

        if (str.length() == 1) return str.toUpperCase();

        // Otherwise uppercase first letter, lowercase the rest.

        return str.substring(0,1).toUpperCase()
                + str.substring(1).toLowerCase();
    }
}
