package projekt2;

import java.util.*;

/**
 *
 * @author emir.bolin
 */
public class Projekt2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String[] data = new String[21];
        for (int i = 0; i < 21; i++) {
            data[i] = Integer.toString(i+1)+"|0|0|||||";
        }
        data[0] = "1|0|0|299.90|051127|Emir|Bolin|He";
        data[1] = "2|1|1|299.90|751020|Tarik|Bolin|He";
        
        String[] unbookedSeats = getUnbookedSeats(data);
        for (String unbookedSeat : unbookedSeats) {
            System.out.println(unbookedSeat);
        }
    }
    
    static String[] getUnbookedSeats(String[] data){
        String[] res = {};     
        
        for (String seatStr : data) {
            String[] seatArray = seatStr.split("\\|");
            if (seatArray[1].equals("0")) {                
                res = append(res, seatStr);                
            }
        }
        return res;
    }
    
    static String[] append(String array[], String x) {
        array = Arrays.copyOf(array, array.length + 1);
        array[array.length - 1] = x;
        return array;
    }
}
