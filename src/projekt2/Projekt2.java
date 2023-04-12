package projekt2;

import java.util.*;
import java.time.Year;

/**
 *
 * @author emir.bolin
 */
public class Projekt2 {

    /**
     * @param args the command line arguments
     */

    static int NUMBEROFSEATS = 21;
    
    /**
     * This function creates a so to say "database" which is a string array with 21 seats.
     * Every seat is a string that is pipe separated and the format is: seat|personalnumber|first-name|second-name|gender.
     * This so called seatStr is usually split into string array with 5 values.
     * @param args
     */
    public static void main (String[] args) {
        menu(createDatabase());
    }
    
    /**
     * Creates a string array with 21 seatStr.
     * @return data
     */
    static String[] createDatabase(){
        String[] data = new String[NUMBEROFSEATS];
        int i = 0;
        while(i < NUMBEROFSEATS){
            data[i] = Integer.toString(i+1)+"|0|0|0|0";
            i++;
        }
        return data;
    }
    
    /**
     * This function creates a menu for user interaction.
     * @param data
     * @return recursive call to itself
     */
    static String[] menu(String[] data){        
        Scanner scan = new Scanner(System.in);
        
        System.out.println("Choose number followed by enter.");
        System.out.println("[1] Book");
        System.out.println("[2] Unbook");
        System.out.println("[3] Find booking");
        System.out.println("[4] Show passengers");
        System.out.println("[5] Show unbooked seats");
        System.out.println("[6] Show profit");
        System.out.println("[7] Exit");
        
        int input = scan.nextInt();
        
        switch (input){
            case 1 -> createBooking(data);
            case 2 -> createUnbooking(data);
            case 3 -> searchBooking(data);
            case 4 -> sortPassengersByAge(data);
            case 5 -> showUnbookedSeats(getUnbookedSeats(data, 1));
            case 6 -> showProfit(data);
            case 7 -> System.exit(0);
        }
        return menu(data);
    }
    
    /**
     * This function updates seatStr for hor the specific seat.
     * @param data
     * @param seat is index in data
     * @param seatStr
     * @return updated version of data
     */
    static String[] book(String [] data, int seat, String seatStr){
        data[seat-1] = seatStr;
        System.out.println("Saved.");
        return data;
    }
    
    /**
     * This function is called when booking a seat. User can choose windowseat or not.
     * @param data
     * @return
     */
    static String[] createBooking(String[] data){        
        Scanner scan = new Scanner(System.in);
        
        int window = getInputWindowSeat();
        String[] unbookedSeats;
        
        if(window == 1){
            // User wants windowseat
            unbookedSeats = getUnbookedSeats(data, 2);
        }
        else{
            // User does not want windowseat
            unbookedSeats = getUnbookedSeats(data, 3);
        }
        showUnbookedSeats(unbookedSeats);
        String seat = getInputSeat(scan, unbookedSeats);
        String personalnumber = getInputPersonalnumber();  // Todo check if personalnumber is allready booked.
        String fName = getInputName("State your first name:");
        String sName = getInputName("State your second name:");
        String gender = getInputName("State your gender:");
        return book(data, Integer.parseInt(seat), createSeatStr(seat, personalnumber, fName, sName, gender));
    }
    
    /**
     * This function is called from the menu when user wants to unbook seat.
     * @param data
     * @return Updated version of data
     */
    static String[] createUnbooking(String[] data){
        Scanner scan = new Scanner(System.in);
        System.out.println("State your personalnumber in format YYYYMMDD or first name and second name.");
        return removeBooking(data, scan.nextLine());
    }
    
    /**
     * This function is called from the menu when a user is searching for a seat booking.
     * @param data
     */
    static void searchBooking(String[] data){       
        Scanner scan = new Scanner(System.in);
        System.out.println("State your personalnumber in format YYYYMMDD or first name and second name.");
        getBooking(data, scan.nextLine(),false);
    }
    
    /**
     * This function returns either all unbooked seats, all unbooked windowseats or all unbooked non windowseats.
     * @param data
     * @param typeOfSeats
     * @return an array with unbooked seats
     */
    static String[] getUnbookedSeats(String[] data, int typeOfSeats){
        String[] res = {};       
        for (String seatStr : data) {
            String[] seatArray = seatStr.split("\\|");
            if (seatArray[1].equals("0")) {
                if(typeOfSeats == 1){
                    // all seats
                    res = myAppend(res, seatStr);
                }
                else if(typeOfSeats == 2 && isWindow(Integer.parseInt(seatArray[0]))){
                    // Only windowseats
                    res = myAppend(res, seatStr);
                }
                else if(typeOfSeats == 3 && !isWindow(Integer.parseInt(seatArray[0]))){
                    // Only non windowseats
                    res = myAppend(res, seatStr);
                }
            }
        }
        return res;
    }
    
    /**
     * This utility function adds an element to a string array.
     * @param array
     * @param x string to be added.
     * @return an extended string array
     */
    static String[] myAppend(String[] array, String x) {
        array = Arrays.copyOf(array, array.length + 1);
        array[array.length - 1] = x;
        return array;
    }
    
    /**
     * This utility function removes the last element from the string array.
     * @param arr
     * @return an updated version of string array
     */
    static String[] myReduce(String[] arr) {
        if (arr == null || arr.length == 0) {
            return arr;
        }
        String[] res = new String[arr.length - 1];
        for (int i = 0; i < res.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }    
    
    /**
     * This function shows the profit made by the booked seats
     * @param data
     */
    static void showProfit(String[] data){
        double profit = calculateProfit(getPassengerBirthYear(data));
        System.out.println("Your profit is "+String.format("%.2f", profit)+" SEK");
    }

    /**
     * This function fetches passengers birth years.
     * @param data
     * @return a string array with birth years
     */
    static String[] getPassengerBirthYear(String[] data){
        String res = "";     
        for (String seatStr : data) {
            String[] seatArray = seatStr.split("\\|");
            if (!seatArray[1].equals("0")) {   
                // Booked seat has personalnumber. Append only years to result
                res += seatArray[1].substring(0, 4)+"|";
            }
        }
        return res.split("\\|");
    }
    
    /**
     * This function calculates the profit based on all passengers.
     * @param bookedSeats
     * @return total profit
     */
    static double calculateProfit(String[] bookedSeats){
        double profit = 0;
        if((bookedSeats.length == 1 && bookedSeats[0].equals("")) || bookedSeats.length == 0){
            // If there is no booked seat
            return profit;
        }
        else{
            int bornYear = Integer.parseInt(bookedSeats[bookedSeats.length-1]);
            profit = calculatePrice(bornYear);
            // Calls itself recursively to calculate profit for the rest of the list
            return profit + calculateProfit(myReduce(bookedSeats));
        }
    }
    
    /**
     * This function calculates the profit based on passengers birth year.
     * @param bornYear
     * @return the price for the passenger
     */
    static double calculatePrice(int bornYear){
        int age = calculateAge(bornYear);
        if (age < 18) {
            return 149.90;
        } 
        else if (age > 69) {
            return 200.0;
        } else {
            return 299.90;
        }        
    }
    
    /**
     * This function calculates the age of the passenger. Birth year - this year.
     * @param bornYear
     * @return age of the passenger
     */
    static int calculateAge(int bornYear){
        Year currentYear = Year.now();
        int age = currentYear.getValue() - bornYear;
        return age;
    }
    
    /**
     * This function collects user input into one pipe separated string.
     * @param seat
     * @param personalnumber
     * @param fName
     * @param sName
     * @param gender
     * @return a pipe separated string so called seatStr
     */
    static String createSeatStr(String seat, String personalnumber, String fName, String sName, String gender){
        String seatStr = seat+"|"+personalnumber+"|"+fName+"|"+sName+"|"+gender;
        return seatStr;
    }
    
    /**
     * This function is called from the menu showing which seats are unbooked.
     * @param data
     */
    static void showUnbookedSeats(String[] data){
        String seats = "Available seats are: ";
        for (String seatStr : data) {
            seats += seatStr.split("\\|")[0] + ", ";
        }
        if (seats.endsWith(", ")){
            System.out.println(seats.substring(0, seats.length()-2));
        }
        else
            System.out.println("There are no available seats.");
    }
    
    /**
     * This function allows user to search for booking via personalnumber or first and second name.
     * @param data
     * @param searchStr single word is personalnumber, while more than one word is names.
     * @param showSeat is a boolean that trigger the type of result.
     * @return either a seatStr or only seat number
     */
    static String getBooking(String[] data, String searchStr, boolean showSeat){
        for (String seatStr : data) {
            String[] seatStrArray = seatStr.split("\\|");
            if(!searchStr.contains(" ")){
                // Searching for personalnumber
                // Check for multiple personalnumber matches will not be needed once booking does the check
                if(seatStrArray[1].equals(searchStr))
                    if(showSeat)
                        return seatStrArray[0];
                    else
                        return showSeatStr(seatStrArray);
            }
            else
            {
                // Searching for name and surname
                // Todo implement check for multiple name matches so personalnumber is unique
                String[] nameStrArray = searchStr.split(" ",2);
                if(seatStrArray[2].equals(nameStrArray[0]) && seatStrArray[3].equals(nameStrArray[1]))
                    if(showSeat)
                        return seatStrArray[0];
                    else
                        return showSeatStr(seatStrArray);
            }
        }
        return "Your booking was not found.";
    }
        
    /**
     * This function removes a booking by changing seatStr to zeros.
     * @param data
     * @param searchStr
     * @return updated version of data
     */
    static String[] removeBooking(String[] data, String searchStr){
        String bookedSeat = getBooking(data, searchStr, true);
        
        if(bookedSeat.contains("Your booking was not found.")){
            System.out.println(bookedSeat);
            return data;
        }
        // Unbook by overwriting with zeros.
        return book(data, Integer.parseInt(bookedSeat), createSeatStr(bookedSeat,"0","0","0","0"));
    }
    
    /**
     * This function presents information about the passenger and the booking.
     * Children and windowseats are highlighted in the output.
     * @param seatStrArray
     * @return a string with information about the booking
     */
    static String showSeatStr(String[] seatStrArray){
        String seat = "seat:";
        String personalnumber = seatStrArray[1];
        int bornYear = Integer.parseInt(personalnumber.substring(0, 4));
        int age = calculateAge(bornYear);
        if(age < 18){
            personalnumber = personalnumber + " (under 18)";
        }
        if(isWindow(Integer.parseInt(seatStrArray[0]))){
            seat = "windowseat:";
        }
        String res = seatStrArray[2]+" "+seatStrArray[3]+" "+ personalnumber + " " + seat + seatStrArray[0];
        System.out.println(res);
        return res;
    }
    
    /**
     * This function implements an algorithm for what is windowseat or not.
     * @param seat
     * @return a boolean where true is a windowseat
     */
    static boolean isWindow(int seat){
        if(seat % 2 == 0 && seat % 4 == 0 && seat != 20){
            // Seat has a even number and modulo 4 is 0. 20 is an exception to the window rule
            return true;
        }
        else if(seat % 4 == 1){
            // Seat has an odd number and modulo 4 is 1
            return true;
        }
        return false;
    }

    /**
     * This function is called from the menu listing all the passengers, oldest to youngest.
     * @param data
     */
    static void sortPassengersByAge(String[] data) {
        // Variable appends personalnumbers
        String res = "";
        for (String seatStr : data) {
            String[] seatArray = seatStr.split("\\|");
            if (!seatArray[1].equals("0")) 
                // Booked seat has personalnumber
                res += seatArray[1]+"|";
        }
        if(res.length() == 0)
            System.out.println("There are no booked passengers.");
        
        String[] people = res.split("\\|");
        int n = people.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (Integer.parseInt(people[j]) > Integer.parseInt(people[j + 1])){
                    String temp = people[j];
                    people[j] = people[j + 1];
                    people[j + 1] = temp;
                }
            }
        }
        // Shows sorted bookings
        for(String personalnumber : people){
            getBooking(data, personalnumber, false);
        }
    }
    
    /**
     * This function takes information from the user and returns the seat number if the seat si empty.
     * @param scan
     * @param unbookedSeats
     * @return seat number as a string
     */
    static String getInputSeat(Scanner scan, String[] unbookedSeats){
        System.out.println("State your seat number:");
        String seat = scan.nextLine();
        for(String seatStr : unbookedSeats){
            String[] seatArray = seatStr.split("\\|");
            if(seatArray[0].equals(seat)){
                return seat;
            }
        }
        System.out.println("Wrong seat number.");
        return getInputSeat(scan, unbookedSeats);
    }
    
    /**
     * This function validates the user input regarding the personalnumber in format: YYYYMMDD.
     * @return a valid personalnumber as a string
     */
    static String getInputPersonalnumber() {
        Scanner scan = new Scanner(System.in);
        
        System.out.println("State your personalnumber in format YYYYMMDD:");
        int pn = 0;
        try{
            pn = scan.nextInt();
            String personalnumber = Integer.toString(pn);
            if(personalnumber.length() == 8 && 
                    pn > 19000000 &&
                    pn < 20230408){
                // Todo check if the input is only numbers
                return personalnumber;
            }
        } catch(Exception e){
            System.out.println("You can only use numbers.");
            
        }
        System.out.println("Wrong format of the personalnumber.");
        return getInputPersonalnumber();
    }
    
    /**
     * This function validates the user input about the name.
     * @param text
     * @return
     */
    static String getInputName(String text) {
        Scanner scan = new Scanner(System.in);
        
        System.out.println(text);
        String input = scan.nextLine();
        if(input.length() > 0){
            return input;
        }
        System.out.println("Wrong input.");
        return getInputName(text);
    }
    
    /**
     * This function checks if input from user is valid. Used for searching windowseats.
     * @return integer 1 or 0
     */
    static int getInputWindowSeat(){
        Scanner scan = new Scanner(System.in);
        
        System.out.println("Do you want to have a windowseat? Press 1 for yes, otherwise 0.");
        int window;
        try
        {
            window = scan.nextInt();
        }catch (Exception e){
            System.out.println("Wrong input. State 0 or 1.");
            return getInputWindowSeat();
        }
        if(window == 1 || window == 0){
            return window;
        }
        
        return getInputWindowSeat();
    }
}
