package info.mrconst.qlient.data;

import java.util.HashMap;

/**
 * Created by konst on 30.04.2014.
 */
public class MorseTable {
    public static class Symbol {
        public String lat;
        public String cyr;
        public String morse;

        public Symbol(String lat, String cyr, String morse) {
            this.lat = lat;
            this.cyr = cyr;
            this.morse = morse;
        }
    }
    static Symbol[] mSymTable;
    static {
        mSymTable = new Symbol[]
            {
                new Symbol("А", "A", ".-"),
                new Symbol("Б", "B", "-..."),
                new Symbol("В", "W", ".--"),
                new Symbol("Г", "G", "--."),
                new Symbol("Д", "D", "-.."),
                new Symbol("Е", "E", "."),
                new Symbol("Ж", "V", "...-"),
                new Symbol("З", "Z", "--.."),
                new Symbol("И", "I", ".."),
                new Symbol("Й", "J", ".---"),
                new Symbol("К", "K", "-.-"),
                new Symbol("Л", "L", ".-.."),
                new Symbol("М", "M", "--"),
                new Symbol("Н", "N", "-."),
                new Symbol("О", "O", "---"),
                new Symbol("П", "P", ".--."),
                new Symbol("Р", "R", ".-."),
                new Symbol("С", "S", "..."),
                new Symbol("Т", "T", "-"),
                new Symbol("У", "U", "..-"),
                new Symbol("Ф", "F", "..-."),
                new Symbol("Х", "H", "...."),
                new Symbol("Ц", "C", "-.-."),
                new Symbol("Ч", "Ö", "---."),
                new Symbol("Ш", "CH", "----"),
                new Symbol("Щ", "Q", "--.-"),
                new Symbol("Ъ", "Ñ", "--.--"),
                new Symbol("Ы", "Y", "-.--"),
                new Symbol("Ь", "X", "-..-"),
                new Symbol("Э", "É", "..-.."),
                new Symbol("Ю", "Ü", "..--"),
                new Symbol("Я", "Ä", ".-.-"),
                new Symbol("1", "1", ".----"),
                new Symbol("2", "2", "..---"),
                new Symbol("3", "3", "...--"),
                new Symbol("4", "4", "....-"),
                new Symbol("5", "5", "....."),
                new Symbol("6", "6", "-...."),
                new Symbol("7", "7", "--..."),
                new Symbol("8", "8", "---.."),
                new Symbol("9", "9", "----."),
                new Symbol("0", "0", "-----"),
                new Symbol(".", ".", "......"),
                new Symbol(":", ":", "---..."),
                new Symbol(";", ";", "-.-.-."),
                new Symbol("(", ")", "-.--.-"),
                new Symbol("_", "_", ".----."),
                new Symbol("\"", "\"", ".-..-."),
                new Symbol("-", "-", "-....-"),
                new Symbol("/", "/", "-..-."),
            };
    }

    public static Symbol find(String morse) {
        for (Symbol s : mSymTable) {
            if (s.morse.equals(morse))
                return s;
        }

        return null;
    }
}
