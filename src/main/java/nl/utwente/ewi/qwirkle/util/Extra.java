package nl.utwente.ewi.qwirkle.util;

/**
 * Some extra functions which may come in handy.
 */
public class Extra {
    /**
     * Checks if a given String can be parsed to an Integer.
     * @param s The String to be checked.
     * @return If the given String can be parsed to an Integer.
     */
    public static boolean isInteger(String s) {
        try {
            //// TODO: 19-1-16 Nettere manier...?! 
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }
}
