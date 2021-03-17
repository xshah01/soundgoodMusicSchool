import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class soundgood_music_school {

    public void gap(int a, int b) {

        for (int i = b; i<a; i++) {
            System.out.print(" ");
        }

    }

    public static void instrumentListing(Statement myStatement) throws SQLException {
                                                                                                      
        String query1 = "SELECT * FROM soundgoodMusicSchool.instrument";
        ResultSet quantity = myStatement.executeQuery(query1);

        soundgood_music_school tab = new soundgood_music_school();

        System.out.println();
        System.out.println("Available instruments: ");
        System.out.println("________________________________________________________________________________________");
        System.out.println();

        while (quantity.next()) {
            System.out.print("Instrument: " + (quantity.getString("instrument")));
            tab.gap(15, quantity.getString(2).length());
            System.out.print("Brand: " + (quantity.getString("brand")));
            tab.gap(15, quantity.getString(4).length());
            System.out.print("Price: " + (quantity.getString("price")));
            tab.gap(15, quantity.getString(3).length());
            System.out.println("Stock quantity: " + (quantity.getString("quantity")));
        }
    }

    public void instrumentRent(Statement myStatement, Statement myStatement2, Statement myStatement3, Scanner in) throws SQLException {

        System.out.println();
        System.out.println("Enter Student ID: ");
        System.out.println();
        String student_id = in.next();
        System.out.println();
        System.out.println("Enter brand of instrument: ");
        System.out.println();
        String instrumentToRent = in.next();

        String query2 = "SELECT quantity FROM soundgoodMusicSchool.instrument WHERE brand = '" + instrumentToRent + "'";
        ResultSet stock = myStatement.executeQuery(query2);

        String query3 = "SELECT count(rental_id) as rentals FROM soundgoodMusicSchool.student_instrument WHERE student_id = " + student_id + " AND soundgoodMusicSchool.student_instrument.return_date IS NULL";
        ResultSet max = myStatement2.executeQuery(query3);

        String query4 = "SELECT firstname, lastname FROM soundgoodMusicSchool.person INNER JOIN soundgoodMusicSchool.student ON soundgoodMusicSchool.person.person_id = soundgoodMusicSchool.student.person_id WHERE student_id = " + student_id;
        ResultSet name = myStatement3.executeQuery(query4);

        if (stock.next()) {

            if (stock.getString("quantity").equals("0")) {
                System.out.println();
                System.out.println("The rental could not be issued. See below information for cause.");
                System.out.println("Error: " + instrumentToRent + " is out of stock");
            }

            else if (max.next()) {

                if (max.getString("rentals").equals("2")) {
                    System.out.println();
                    System.out.println("The rental could not be issued. See below information for cause.");
                    System.out.println("Error: Maximum rentals reached (2) for student: " + student_id);
                }

                else {

                    DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    LocalDateTime localtime = LocalDateTime.now();

                    String query5 = "INSERT INTO soundgoodMusicSchool.student_instrument (instrument_id, rental_id, student_id, rental_date) " +
                                    "SELECT (SELECT instrument_id FROM soundgoodMusicSchool.instrument WHERE brand = '" + instrumentToRent + "'), MAX(rental_id)+1," + student_id + ", '" + date.format(localtime) + "' " +
                                    "FROM soundgoodMusicSchool.student_instrument";
                    myStatement.executeUpdate(query5);
                    
                    String query6 = "UPDATE soundgoodMusicSchool.instrument INNER JOIN soundgoodMusicSchool.student_instrument " +
                                    "ON soundgoodMusicSchool.instrument.instrument_id = soundgoodMusicSchool.student_instrument.instrument_id SET quantity = quantity-1 WHERE brand LIKE '" + instrumentToRent + "%'";
                    myStatement.executeUpdate(query6);

                    String query7 = "SELECT instrument FROM soundgoodMusicSchool.instrument WHERE brand = '" + instrumentToRent + "'";
                    ResultSet instrument = myStatement.executeQuery(query7);

                    System.out.println();
                    while (instrument.next()) {
                        while (name.next()) {
                            System.out.println("Instrument " + instrumentToRent + " (" + instrument.getString("instrument") + ") " + "is now rented by student: " + student_id +
                                    " (" + name.getString("firstname") + " " + name.getString("lastname") + ")");
                            System.out.println("Rental has been successfully activated");
                        }
                    }

                }

            }

        }

    }

    public void rentalTerminate(Statement myStatement, Statement myStatement2, Scanner in) throws SQLException {

        System.out.println();
        System.out.println("Enter Student ID: ");
        System.out.println();
        String student_id = in.next();
        System.out.println();
        System.out.println("Enter brand of instrument: ");
        System.out.println();
        String toReturn = in.next();

        String query8 = "SELECT firstname, lastname FROM soundgoodMusicSchool.person INNER JOIN soundgoodMusicSchool.student ON soundgoodMusicSchool.person.person_id = soundgoodMusicSchool.student.person_id WHERE student_id = " + student_id;
        ResultSet name = myStatement2.executeQuery(query8);

        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime localtime = LocalDateTime.now();

        String query9 = "UPDATE soundgoodMusicSchool.student_instrument " +
                        "SET return_date = '" + date.format(localtime) + "' WHERE student_id = " + student_id + " " +
                        "AND instrument_id = (SELECT instrument_id FROM soundgoodMusicSchool.instrument WHERE brand = '" + toReturn + "')";
        myStatement.executeUpdate(query9);

        String query10 = "UPDATE soundgoodMusicSchool.instrument INNER JOIN soundgoodMusicSchool.student_instrument " +
                        "ON soundgoodMusicSchool.instrument.instrument_id = soundgoodMusicSchool.student_instrument.instrument_id SET quantity = quantity+1 WHERE brand LIKE '"+toReturn+"%'";
        myStatement.executeUpdate(query10);

        String query11 = "SELECT instrument FROM soundgoodMusicSchool.instrument WHERE brand = '"+toReturn+"'";
        ResultSet instrument = myStatement.executeQuery(query11);

        System.out.println();
        while(instrument.next()) {
            while (name.next()) {
                System.out.println("Instrument " + toReturn + " (" + instrument.getString("instrument") + ") " + "is returned by student: " + student_id +
                        " (" + name.getString("firstname") + " " + name.getString("lastname") + ")");
                System.out.println("Rental has been successfully terminated");
            }
        }
    }

    public static void activeRentals(Statement myStatement, Statement myStatement2) throws SQLException {

        String query12 = "SELECT count(rental_id)as rentals FROM soundgoodMusicSchool.student_instrument WHERE soundgoodMusicSchool.student_instrument.return_date IS NULL";
        ResultSet rentals = myStatement.executeQuery(query12);

        String query13 = "SELECT * FROM soundgoodMusicSchool.student_instrument WHERE soundgoodMusicSchool.student_instrument.return_date IS NULL";
        ResultSet active = myStatement2.executeQuery(query13);

        soundgood_music_school tab = new soundgood_music_school();

        System.out.println();
        System.out.println();
        System.out.println("Active rentals: ");
        System.out.println("__________________________________________________________________________________________________________");
        System.out.println();

        while (rentals.next()) {

            if (rentals.getString("rentals").equals("0")) {
                System.out.println("There is no active rentals");
            }

            else {

                while (active.next()) {
                        System.out.print("Rental ID: " + (active.getString("rental_id")));
                        tab.gap(15, active.getString(2).length());
                        System.out.print("Student ID: " + (active.getString("student_id")));
                        tab.gap(15, active.getString(3).length());
                        System.out.print("Instrument ID: " + (active.getString("instrument_id")));
                        tab.gap(15, active.getString(1).length());
                        System.out.println("Rental Date: " + (active.getString("rental_date")));
                }
            }
        }
    }

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/soundgoodMusicSchool?autoReconnect=true&useSSL=false";
        String user = "root";
        String password = "SecretPass";

        try {

            Connection myConnection = DriverManager.getConnection(url, user, password);
            Statement myStatement = myConnection.createStatement();
            Statement myStatement2 = myConnection.createStatement();
            Statement myStatement3 = myConnection.createStatement();

            /*
            Create a Scanner object which has Standard input (System.in) as source and pass it as a parameter to the method.
            */
            Scanner in = new Scanner(System.in);
            soundgood_music_school parameter = new soundgood_music_school();
            System.out.println();
            System.out.println("Soundgood Music School JDBC Menu");
            System.out.println();
            System.out.println("1. For listing all instruments available and all active rentals, write 'list'");
            System.out.println("2. For activating a rental, write 'rent'");
            System.out.println("3. For terminating a rental, write 'terminate'");

            String input = in.next();

            while (!input.equals("cancel")) {

                switch (input) {

                    case "list":
                        instrumentListing(myStatement);
                        activeRentals(myStatement, myStatement2);
                        break;

                    case "rent":
                        parameter.instrumentRent(myStatement, myStatement2, myStatement3, in);
                        break;

                    case "terminate":
                        parameter.rentalTerminate(myStatement, myStatement2, in);
                        break;

                    default:
                        System.out.println();
                        System.out.println("There is no such an option in the menu, please try again");

                }

                System.out.println();
                input = in.next();

            }

            in.close();
            myConnection.close();
            myStatement.close();
            myStatement2.close();
            myStatement3.close();

        }

        catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

}