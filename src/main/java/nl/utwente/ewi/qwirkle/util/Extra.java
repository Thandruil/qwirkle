package nl.utwente.ewi.qwirkle.util;

public class Extra {
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
