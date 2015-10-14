package tests.xcat.daiyonkaigi.guchiruna.tokenize;


import junit.framework.TestCase;

import org.atilika.kuromoji.Token;

import java.util.List;

import xcat.daiyonkaigi.guchiruna.tokenize.StringToToken;

public class StringToTokenTest extends TestCase {
    public void testTokenize() throws Exception {
        List<Token> list = StringToToken.tokenize("冷やし中華");
        for(Token token : list){
            //TODO テスト書く
            //System.out.println(token.getAllFeatures().toString());
        }
    }
}