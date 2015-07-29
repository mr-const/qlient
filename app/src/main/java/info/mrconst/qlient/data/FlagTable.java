package info.mrconst.qlient.data;

import java.util.HashMap;

/**
 * Таблица сопоставлений семафорной азбуки в латиннице и кириллице
 */
public class FlagTable {
    private static HashMap<Character, Character> sLatCyrTable = new HashMap<>(26);
    static {
        sLatCyrTable.put('A', 'Н');
        sLatCyrTable.put('B', 'В');
        sLatCyrTable.put('C', 'Е');
        sLatCyrTable.put('D', 'Н');
        sLatCyrTable.put('E', 'И');
        sLatCyrTable.put('F', 'Г');
        sLatCyrTable.put('G', 'О');
        sLatCyrTable.put('H', 'Б');
        sLatCyrTable.put('I', 'Х');
        sLatCyrTable.put('J', 'П');
        sLatCyrTable.put('K', 'Ы');
        sLatCyrTable.put('L', 'М');

        sLatCyrTable.put('M', 'Ч');
        sLatCyrTable.put('N', 'А');
        sLatCyrTable.put('O', 'Ю');
        sLatCyrTable.put('P', 'Р');
        sLatCyrTable.put('Q', 'З');
        sLatCyrTable.put('R', 'Т');
        sLatCyrTable.put('S', 'Ц');
        sLatCyrTable.put('T', 'Щ');
        sLatCyrTable.put('U', 'У');
        sLatCyrTable.put('V', 'Ф');
        sLatCyrTable.put('W', 'Я');
        sLatCyrTable.put('X', 'К');
        sLatCyrTable.put('Y', 'Ж');
        sLatCyrTable.put('Z', 'Д');
    }

    public static Character find(Character lat) {
        return sLatCyrTable.get(lat);
    }
}
