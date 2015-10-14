package xcat.daiyonkaigi.guchiruna.tokenize;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

import java.util.List;


/**
 * 文字列をトークン化し、kuromojiライブラリを実行するクラスです。
 */
public class StringToToken {
    public static List<Token> tokenize(String sentence) {
        Tokenizer.Builder builder = Tokenizer.builder();
        builder.mode(Tokenizer.Mode.NORMAL);
        Tokenizer kuromoji = builder.build();
        return kuromoji.tokenize(sentence);
    }
}
