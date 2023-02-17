import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;


public class Main {

  private static Connection conn;

  public Main()
  {
    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/rentaldb?"
              + "user=" + "root" + "&password=" + "Haris@2000");

      if (conn == null) {
        System.out.println("Connection not established");
      }
    } catch (Exception e) {
      System.out.println("Unable to establish connection!");
    }

  }

  public static void main(String[] args) {
    Main c = new Main();

    try {
      Scanner sc = new Scanner(System.in);
      checkadmin();
    } catch (Exception e) {
      System.out.println(e.getLocalizedMessage());
    }


  }

  private static boolean checkempty(String input) {
    if (input.isEmpty()) {
      System.out.println("input cannot be empty. please enter again.");
      return false;
    }
    return true;

  }

  private static void checkadmin() {

    Scanner sc = new Scanner(System.in);

    String username = " ";
    String password = " ";

    System.out.println("\nWelcome to Car Rental Service");

    do {
      System.out.println("\nPlease enter username ");
      username = sc.nextLine();
    } while (!checkempty(username));

    boolean checkadminusername = validateadminusername(username);
    boolean checkusername = usernameexists(username);

    boolean checkuser;
    boolean gpassword;


    if (checkadminusername) {

      System.out.println("\nWelcome admin");
      do {
        System.out.println("Please enter password ");
        password = sc.nextLine();
        gpassword = validateadminpassword(username, password);

      } while (!checkempty(password) || !gpassword);

      adminfunctionalties();

    } else if (checkusername) {

      System.out.println("\nWelcome user");

      do {
        System.out.println("Please enter password ");
        password = sc.nextLine();
        gpassword = checkusernamepassword(username, password);


      } while (!checkempty(password) || !gpassword);

      userfunctionalties(username);

    } else {
      System.out.println("You have not registered! Please Sign in");
      signin();
    }


  }

  private static boolean checkusernamepassword(String username, String password) {
    try {
      CallableStatement stmt = conn.prepareCall("{ ? = call usercheck( ?,?)}");
      stmt.registerOutParameter(1, Types.INTEGER);
      stmt.setString(2, username);
      stmt.setString(3, password);
      stmt.execute();
      int check = 0;
      check = stmt.getInt(1);
      if (check == 0)
        return false;
      else
        return true;
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return true;
  }

  private static boolean validateadminusername(String username) {
    int check;

    try {
      CallableStatement cStmt = conn.prepareCall("{ ? = call admincheckusername( ?)}");

      cStmt.registerOutParameter(1, Types.INTEGER);
      cStmt.setString(2, username);
      cStmt.execute();

      check = cStmt.getInt(1);
      if (check == 0)
        return false;
      else
        return true;


    } catch (Exception e) {
      System.out.println(e.getMessage());
    }


    return false;
  }

  private static boolean validateadminpassword(String username, String password) {
    int check;

    try {
      CallableStatement cStmt = conn.prepareCall("{ ? = call admincheckpassword( ?,?)}");

      cStmt.registerOutParameter(1, Types.INTEGER);
      cStmt.setString(2, username);
      cStmt.setString(3, password);

      cStmt.execute();

      check = cStmt.getInt(1);
      if (check == 0)
        return false;
      else
        return true;


    } catch (Exception e) {
      System.out.println(e.getMessage());
    }


    return false;
  }

  private static boolean validateuser(String username, String password) {
    int check;

    try {
      CallableStatement cStmt = conn.prepareCall("{ ? = call usercheck( ?,?)}");

      cStmt.registerOutParameter(1, Types.INTEGER);
      cStmt.setString(2, username);
      cStmt.setString(3, password);
      cStmt.execute();

      check = cStmt.getInt(1);
      if (check == 0)
        return false;
      else
        return true;


    } catch (Exception e) {
      System.out.println(e.getMessage());
    }


    return false;
  }

  private static void adminfunctionalties() {
    Scanner sc = new Scanner(System.in);
    String ch;
    System.out.println("\n");
    do {
      System.out.println("MENU\n");
      System.out.println("1.Add new cars");
      System.out.println("2.Remove car");
      System.out.println("3.Update car Information");
      System.out.println("4.View all cars");
      System.out.println("5.Delete Customer");
      System.out.println("6.Display Customer");
      System.out.println("7.Visualize data of cars");
      System.out.println("8.Exit");
      System.out.println("\nPlease enter the choice you want to perform::");
      ch = sc.nextLine();

      switch (ch) {
        case "1":
          System.out.println("\n");
          addcar();
          break;
        case "2":
          removecar();
          break;
        case "3":
          System.out.println("\n");
          updatecar();
          break;
        case "4":
          System.out.println("\n");
          viewcars();
          break;
        case "5":
          System.out.println("\n");
          deletecustomer();
          break;
        case "6":
          displaycustomers();
          System.out.println("\n");
          break;
        case "7" :
          displaycardata();
          System.out.println("\n");
          break;
        case "8":
          System.exit(4);
          break;
        default:
          System.out.println("invalid choice");
      }


    } while (true);


  }

 private static void displaycardata()
 {
        String choice=" ";
        Scanner sc = new Scanner(System.in);

        System.out.println("\n\tWelcome to data Visualization");
         System.out.println("\t******************************");
        System.out.println("\n\tThis data visualization tells how many times a particular car is rented.");

        System.out.println("\n\tAvailable carid for data visualization");
        printcaridforadmin();

        System.out.println("\n\tPlease enter the car id you want to view visualize data for::");
        choice = sc.nextLine();

        // check if that car exists
        getnumberoftimesrented(choice);

 }

 // barath should do
 private static void printcaridforadmin()
 {
            try
            {
                  CallableStatement stmt = conn.prepareCall("{call printcarid()}");

                  ResultSet ps1 = stmt.executeQuery();

              System.out.printf("\t------%n");
              System.out.printf("\t CAR ID %n");
              System.out.printf("\t------%n");


                  while (ps1.next()) {
                    String out =String.format("\t| %-2d |%n",

                            ps1.getInt(1));
                            System.out.println(out);
                  }

                  ps1.close();
                  stmt.close();

            }
            catch (Exception e)
            {
              System.out.println(e.getMessage());
            }
 }
 private static void getnumberoftimesrented(String choice)
 {
           try
           {
              int check =0;
              CallableStatement stmt = conn.prepareCall("{? = call returnnumberoftimesrented(?)}");
              stmt.registerOutParameter(1,Types.INTEGER);
              stmt.setInt(2,Integer.parseInt(choice));
              stmt.execute();
              check = stmt.getInt(1);
              if(check == 0)
                System.out.println("No data for this car");
              else
                displaystars(choice,check);
           }
           catch (Exception e)
           {
             System.out.println(e.getMessage());
           }
 }

 private static void displaystars(String carid, int count)
 {    String star = "*";

        String carname = getcarname(carid);
        System.out.println("\t* -> 1 time rented");

   System.out.printf("---------------------------------------%n");
   System.out.printf("        VISUALIZATION TABLE               %n");

   System.out.printf("---------------------------------------%n");
   System.out.printf("| %-12s | %-15s |%n", "CAR NAME", "RENTED");
   System.out.printf("---------------------------------------%n");
   System.out.printf("| %-12s | %-15s |%n", carname, star.repeat(count));

 }

 private static String getcarname(String car_id)
 {
            try
            {
                  CallableStatement stmt = conn.prepareCall("{? = call getcarname(?)}");
                  stmt.registerOutParameter(1,Types.VARCHAR);
                  stmt.setInt(2,Integer.parseInt(car_id));
                  stmt.execute();
                  String cname = stmt.getNString(1);
                  return cname;
            }
            catch (Exception e)
            {
              System.out.println(e.getMessage());
            }

            return "";
 }

  private static boolean validateage(String age) {
    if (age.contains("-") || age.equals("0") || age.contains(".")) {
      return false;
    }
    return true;
  }


  private static boolean validateusername(String uname) {
    if (uname.length() > 10) {
      return false;
    }
    return true;
  }

  private static void signin() {
    String cname = " ";
    String cage = " ";
    String cusername = " ";
    String cpassword = " ";
    String caddress = " ";
    String cphn = " ";
    boolean check1;

    Scanner sc = new Scanner(System.in);

    boolean checkage;
    System.out.println(" Registration page");
    do {
      System.out.println("Please enter name");

      cname = sc.nextLine();
      check1= checkforvalidstrings(cname);
      if(!check1)
        System.out.println("Invalid input");
    }
    while (!checkempty(cname) && !check1);

    System.out.println("Please enter age");
    do {
      cage = sc.nextLine();
      checkage = validateage(cage);
      if (!checkage) {
        System.out.println("please enter valid age.");
      }

    } while (!checkage || !checkempty(cage));

    if (Integer.parseInt(cage) > 18) {
      do {
        System.out.println("Please enter address");
        caddress = sc.nextLine();
      }
      while (!checkempty(caddress));
      do {
        System.out.println("Please enter Phonenumber");
        cphn = sc.nextLine();
        check1 = checkphnlength(cphn);
        if(!check1)
          System.out.println("Invalid Phone number");

      } while (!checkempty(cphn) || !check1);


      boolean checkuname;
      System.out.println("Please enter Username");
      do {
        cusername = sc.nextLine();
        checkuname = validateusername(cusername);
        if (!checkuname) {
          System.out.println("username cannot exceed length 10.");
        }
      }
      while (!checkuname || !checkempty(cusername));
      boolean checkusername = true;

      do {

        checkusername = checkexistingusername(cusername);
        if (checkusername) {
          System.out.println("Username Already exists");
          System.out.println("Please enter different user name");

          cusername = sc.nextLine();

          checkusername = true;
        } else {
          checkusername = false;
        }

      } while (checkusername);

      boolean checkpwd;
      System.out.println("Please enter password");
      do {
        cpassword = sc.nextLine();
        checkpwd = validatepwd(cpassword);
        if (!checkpwd) {
          System.out.println("password cannot exceed length 20");
        }

      }
      while (!checkpwd || !checkempty(cpassword));
      createuser(cname, cage, caddress, cphn, cusername, cpassword);

      System.out.println("Successfully Registered");
      userfunctionalties(cusername);

    } else {
      System.out.println("Your age is not a Legal Age to rent car");
    }


  }

  private static boolean validatepwd(String pwd) {
    if (pwd.length() > 20) {
      return false;
    }
    return true;
  }


  private static boolean checkexistingusername(String username) {
    int check;

    try {
      CallableStatement cStmt = conn.prepareCall("{ ? = call checkuernameexists( ?)}");

      cStmt.registerOutParameter(1, Types.INTEGER);
      cStmt.setString(2, username);
      cStmt.execute();

      check = cStmt.getInt(1);
      if (check == 0)
        return false;
      else
        return true;

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }


    return false;
  }

  private static void createuser(String name, String age, String addres, String phn, String username, String pass) {

    try {
      CallableStatement cStmt = conn.prepareCall("{call addcustomer( ?,?,?,?,?,?)}");

      cStmt.setString(1, name);
      cStmt.setString(2, age);
      cStmt.setString(3, addres);
      cStmt.setString(4, phn);
      cStmt.setString(5, username);
      cStmt.setString(6, pass);

      cStmt.execute();

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }

  private static void userfunctionalties(String cusername) {
    Scanner sc = new Scanner(System.in);
    String ch;

    clearscreen();
    do {
      System.out.println("\nMENU\n");
      System.out.println("1.Rent car");
      System.out.println("2.Return car");
      System.out.println("3.Write review ");
      System.out.println("4.Account settings");
      System.out.println("5.Exit.");
      System.out.println("\nPlease enter the choice you want to perform::");
      ch = sc.nextLine();
      switch (ch) {
        case "1":
          System.out.println("\n");
          customerentcar(cusername);
          break;

        case "2":
          customereturncar(cusername);
          break;

        case "3":
          writereviewaboutcar(cusername);
          break;

        case "4":
          accountsetting(cusername);
          break;

        case "5":
          System.exit(4);
          break;

        default:
          System.out.println("Invalid choice");
      }
    } while (true);

  }

  private static void customerentcar(String cusername) {

    boolean check1;
    check1 = checkcustomerpendingcar(cusername);

    if(!check1)
    {
      String carchoice = " ";
      String carrentaldays = " ";
      String ch = " ";
      boolean checkc;

      Scanner sc = new Scanner(System.in);

      System.out.println("\nCar Information\n");
      displaycarsforcustomers();
      System.out.println("\nPlease select Car id:");

      do {
        carchoice = sc.nextLine();
        checkc = checkavailabilityofcar(carchoice);
      } while (!checkempty(carchoice));


      if (checkc) {

        System.out.println("\nThe car entered is Available");
        System.out.println("\nSelected Car Details");
        displayselectedcar(carchoice);

        boolean checkday;
        System.out.println("\nHow many days you want to rent for?");
        do {
          carrentaldays = sc.nextLine();
          checkday = validateage(carrentaldays);
          if (!checkday) {
            System.out.println("please enter valid day");
          }
        } while (!checkday || !checkempty(carrentaldays));

        //check for any review
        boolean checkreview  = checkifanyreview(carchoice);

        if(checkreview)
        {
          System.out.println("\nReview of the car you rented\n");
          displayreviews(carchoice);
        }
        else
        {
          System.out.println("\nThere are no Reviews for this car\n");
        }

        System.out.println("\nAre you sure you want to Rent car(Yes/No)");
        ch = sc.nextLine();
        ch = ch.toLowerCase();
        if (ch.equals("yes")) {

          int insurancechoince = askinfoaboutinsrance(carchoice);
          // erroe of unkown column car-id
          addtorentaltable(carchoice, cusername, carrentaldays, insurancechoince);

          System.out.println("\nCar is rented!, Please collect your car Tomorrow\n");
          updatecarstatus(carchoice);

        } else {
          System.out.println("No worries! Come back when you want to Rent car");
        }
      } else {
        System.out.println("\nOOPS! Car not available\n");
      }
    }
    else
      System.out.println("You have pending car, to be returned Return them then rent ANOTHER, THANK YOU");

  }

  private static boolean checkifanyreview(String carid)
  {
              int check=0;

              try
              {
                   CallableStatement stmt = conn.prepareCall("{? = call checkifanyreview(?)}");
                   stmt.registerOutParameter(1,Types.INTEGER);
                   stmt.setString(2,carid);
                   stmt.execute();
                   check = stmt.getInt(1);
                   if(check==0)
                     return false;
                   else
                     return true;
              }
              catch (Exception e)
              {
                System.out.println(e.getMessage());
              }

              return true;
  }
  private  static  boolean checkcustomerpendingcar(String username)
  {
            try
            {
                  CallableStatement stmt = conn.prepareCall("{? = call checkexistingrentedcar(?)}");
                  stmt.registerOutParameter(1,Types.INTEGER);
                  stmt.setString(2,username);
                  stmt.execute();
                  int check =0;
                  check = stmt.getInt(1);
                  if(check==0)
                    return false;
                  else
                    return true;
            }
            catch (Exception e)
            {
              System.out.println(e.getMessage());
            }

            return true;
  }


  private static void displayreviews(String carchoice)
  {
         try
         {
                CallableStatement stmt = conn.prepareCall("{call displayreview(?)}");
                 stmt.setString(1,carchoice);

                 ResultSet ps1 = stmt.executeQuery();

                 System.out.println("Review  Rating   Date_of_Review  ");

                 while (ps1.next()) {
                   String out = String.format("%-20s   %-20f    %-20s",

                           ps1.getString(1),
                           ps1.getDouble(2),
                           ps1.getString(3));

                   System.out.println(out);
                 }

                 ps1.close();
                 stmt.close();

           System.out.println("\nHope you had chosen the right car");
         }
         catch (Exception e)
         {
           System.out.println(e.getMessage());
         }
  }
  private static void displaycarsforcustomers() {
    try {
      CallableStatement cStmt = conn.prepareCall("{call displaycar()}");
      ResultSet ps1 = cStmt.executeQuery();

      System.out.printf("------------------------------------%n");
      System.out.printf("              ALL CARS     %n");
      System.out.printf("------------------------------------%n");
      System.out.printf("| %-4s | %-8s | %4s | %-7s |%n", "CID", "NAME", "MODEL", "COLOR");
      System.out.printf("------------------------------------%n");

      while (ps1.next()) {
        String out =String.format("| %-4d | %-8s | %-5s | %-7s |%n",

                ps1.getInt(1),
                ps1.getString(2),
                ps1.getString(3),
                ps1.getString(4));

                System.out.println(out);
      }

      ps1.close();
      cStmt.close();

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static boolean checkavailabilityofcar(String carid) {
    int check = 0;

    try {
      CallableStatement cStmt = conn.prepareCall("{? = call checkcaravailablity(?)}");
      cStmt.registerOutParameter(1, Types.INTEGER);
      cStmt.setString(2, carid);
      cStmt.execute();

      check = cStmt.getInt(1);
      if (check == 0)
        return false;
      else
        return true;
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return true;
  }

  private static void displayselectedcar(String carid) {
    try {

      CallableStatement Cstmt = conn.prepareCall("{call displayselectedcar(?) }");
      Cstmt.setString(1, carid);

      ResultSet ps1 = Cstmt.executeQuery();
      System.out.printf("------------------------------------------------------------------------------%n");
      System.out.printf("                       RENT CAR DETAILS                                       %n");

      System.out.printf("------------------------------------------------------------------------------%n");
      System.out.printf("| %-10s | %-8s | %-8s | %-9s | %-15s | %-9s |%n", "NAME", "MODEL", "COLOR", "RATING", "RENT PRICE/HR", "LATEFEE");
      System.out.printf("------------------------------------------------------------------------------%n");

      //System.out.println("NAME     MODEL   Color   Rating   RentPrice/Hr  Latefee ");

      while (ps1.next()) {

        String out = String.format("| %-10s | %-8s | %-8s | %-9f | %-15f | %-9f |%n",

                ps1.getString(1),
                ps1.getString(2),
                ps1.getString(3),
                ps1.getDouble(4),
                ps1.getDouble(5),
                ps1.getDouble(6));

                System.out.println(out);
      }

      ps1.close();
      Cstmt.close();

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void addtorentaltable(String cid, String cusername, String noofdays, int insurancechoice) {
    try {

      CallableStatement Stmt = conn.prepareCall("{call addtorentaltable(?,?,?,?)}");

      // changed here
      Stmt.setString(1, cid);
      Stmt.setString(2, cusername);
      Stmt.setString(3, noofdays);
      Stmt.setInt(4, insurancechoice);

      Stmt.execute();

    } catch (Exception e) {
      System.out.println("add to rental error");
      System.out.println(e.getMessage());
    }
  }

  private static void updatecarstatus(String carid) {
    try {
      CallableStatement stmt = conn.prepareCall("{call updatecar(?,?,?)}");
      stmt.setString(1, carid);
      stmt.setString(2, "carstatus");
      stmt.setString(3, "BOOKED");
      stmt.execute();

    } catch (Exception e) {
      System.out.println("error in update car");
      System.out.println(e.getMessage());
    }
  }

  private static int askinfoaboutinsrance(String cid) {
    Scanner sc = new Scanner(System.in);
    String ch = "  ";


    System.out.println("\nYour safety is our PRIORTY");
    System.out.println("Please see the Insurance Plan and choose one\n");
    do {
      displayinsurancedetails();
      System.out.println("\nPlease select which insurance you want by giving insurance id?");
      ch = sc.nextLine();

      if (ch.equals("1")) {
        addinfotoinsurancetable("state farm ", "2000 ", cid);
        return 1;

      } else if (ch.equals("2")) {
        addinfotoinsurancetable("GEICO", "4000 ", cid);
        return 2;

      } else {
        System.out.println("Such a Insurance plan does not EXIST!");
        System.out.println("Please revist plan and choose one");

      }

    } while (ch.isEmpty());

    return 0;

  }


  private static void displayinsurancedetails() {

    System.out.println("insurance id        insurance name        insurance amount");
    System.out.println("1                   state farm              2000");
    System.out.println("2                   GEICO                   4000");
  }


  private static void addinfotoinsurancetable(String insurancename, String insuranceamount, String cid) {
    try {
      CallableStatement stmt = conn.prepareCall("{call addinsuranceinfo(?,?)}");
      stmt.setString(1, insuranceamount);
      stmt.setString(2, insurancename);

      stmt.execute();

    } catch (Exception e) {
      System.out.println("Adding to insurance table error");
      System.out.println(e.getMessage());
    }
  }

  private static void customereturncar(String cuser) {

    int checkrentalcar=0;
    checkrentalcar = checkifanyreturncar(cuser);

    if(checkrentalcar == 0)
    {
      System.out.println("You do not have cars to return, Please rent one\n");
      return;
    }
    else {

      Scanner sc = new Scanner(System.in);
      String ch = " ";
      String ndays = " ";

      System.out.println("\nHope you had Nice ride");

      System.out.println("\nInformation about the car you rented");
      displayrentedcar(cuser);
      System.out.println("\nDo you want to return the car?(Yes/no)");
      ch = sc.nextLine();
      ch = ch.toLowerCase();

      if (ch.equals("yes")) {
        System.out.println("Please enter How many days you used the car?");
        ndays = sc.nextLine();
        checklatefees(ndays, cuser);

      } else {
        System.out.println("Fine, Please make sure you do not exceed the number of days you wanted to rent the car");
      }

    }
  }

  private static int checkifanyreturncar(String username)
  {
          try
          {
                  CallableStatement stmt = conn.prepareCall("{? = call checkifreturncar(?)}");
                  stmt.registerOutParameter(1,Types.INTEGER);
                  stmt.setString(2,username);
                  stmt.execute();
                  int ch = stmt.getInt(1);
                  return ch;
          }
          catch (Exception e)
          {
            System.out.println(e.getMessage());
          }
          return 0;

  }

  private static void displayrentedcar(String cusername) {
    try {
      CallableStatement stmt = conn.prepareCall("{call displayrentedcar(?)}");

      stmt.setString(1, cusername);

      ResultSet ps1 = stmt.executeQuery();

      System.out.printf("---------------------------%n");
      System.out.printf("     RENTED CAR  %n");
      System.out.printf("---------------------------%n");
      //System.out.println("CID        NAME");
      System.out.printf("| %-10s | %-10s |%n", "CID", "NAME");
      System.out.printf("---------------------------%n");
      while (ps1.next()) {
        String out = String.format("| %-10d | %-10s |%n ",
                ps1.getInt(1),
                ps1.getString(2));
        System.out.println(out);
      }

      ps1.close();
      stmt.close();

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void checklatefees(String ndays, String cusername) {
    try {
      CallableStatement stmt = conn.prepareCall("{? = call checklatefee(?,?)}");

      stmt.registerOutParameter(1, Types.INTEGER);
      stmt.setInt(2, Integer.parseInt(ndays));
      stmt.setString(3, cusername);
      stmt.execute();

      int daysgivenbyuser = stmt.getInt(1);

      if (daysgivenbyuser < Integer.parseInt(ndays))
      {
        System.out.println("\nYou have returned the car LATE, SO LATE FEES IS APPLICABLE\n");
        int differencedays = Integer.parseInt(ndays) - daysgivenbyuser;
        findlatefee(differencedays, cusername);

      } else {
        System.out.println("\nLooks like you have return you car in time, GOOD WORK!\n");
        calculatetotal(0.0, cusername);

      }


    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }


  private static void findlatefee(int differencedays, String username) {
    try {
      CallableStatement stmt = conn.prepareCall("{? = call latefeecalculation(?,?)}");
      stmt.registerOutParameter(1, Types.DOUBLE);
      stmt.setString(2, username);
      stmt.setInt(3, differencedays);
      stmt.execute();

      double latefee = stmt.getDouble(1);
      System.out.println("\nLate fee is :$" + latefee);
      calculatetotal(latefee, username);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void calculatetotal(Double latefee, String username) {
    try {
      double total;

      CallableStatement stmt = conn.prepareCall("{? = CALL calculatetotal(?)}");
      stmt.registerOutParameter(1, Types.DOUBLE);
      stmt.setString(2, username);

      stmt.execute();
      total = stmt.getDouble(1);

      adddateintopayment(total, latefee, username);

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void adddateintopayment(double total, double latefee, String username) {

    try {

      LocalDate currdate = LocalDate.now();
      CallableStatement stmt = conn.prepareCall("{CALL addtopaymenttable(?,?,?,?)}");
      stmt.setString(1, username);
      stmt.setDouble(2, latefee);
      stmt.setDouble(3, total);
      stmt.setDate(4, Date.valueOf(currdate));

      ResultSet ps1 = stmt.executeQuery();

      System.out.printf("------------------------------------------------------------------------------------%n");
      System.out.printf("                                  PAYMENT        %n");
      System.out.printf("------------------------------------------------------------------------------------%n");
      System.out.printf("| %-12s | %-15s | %-15s | %-15s | %-15s |%n", "PAYMENT ID", "AMOUNT", "TAX", "LATE FEE", "INSURANCE");
      System.out.printf("------------------------------------------------------------------------------------%n");


      while (ps1.next()) {
        String out = String.format("| %-12d | %-15f | %-15f | %-15f | %-15f |%n ",

                ps1.getInt(1),
                ps1.getDouble(2),
                ps1.getDouble(3),
                ps1.getDouble(4),
                ps1.getDouble(5) );
        System.out.println(out);
      }

      ps1.close();
      stmt.close();

      printtotalamount(username);
      changestatusofcarincarandrental(username);

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }

  private static void printtotalamount(String username) {
    try {
      CallableStatement stmt = conn.prepareCall("{? = call printtotal(?)}");
      stmt.registerOutParameter(1, Types.DOUBLE);
      stmt.setString(2, username);
      stmt.execute();
      Double amt = stmt.getDouble(1);
      System.out.println("\nTotal Money to be paid is  $" + amt);

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void changestatusofcarincarandrental(String username) {
    try {
      CallableStatement stmt = conn.prepareCall("{call changestatusofcar(?)}");
      stmt.setString(1, username);
      stmt.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }


  private static void writereviewaboutcar(String username)
  {
    int checkrentalcar=0;

    checkrentalcar = checkifreviewcanbewritten(username);

    if(checkrentalcar == 0)
    {
      System.out.println("You have not rented any car till now, Please rent one to review\n");
      return;
    }
    else {

      Scanner sc = new Scanner(System.in);

      String rating = " ";
      String review = " ";
      String choice = " ";
      boolean check1= true;

      System.out.println("\n");
      displayusedcar(username);
      System.out.println("\nPlease choose the car-id which you want to review about?");
      choice = sc.nextLine();

      System.out.println("enter your rating ( FOR /5):");
      do {
        rating = sc.nextLine();
        if(rating.isEmpty()) {
          System.out.println("Review cannot be left empty, Enter again");
          check1 = false;
        }
        else if(Integer.parseInt(rating)>5) {
          System.out.println("Review cannot exceed 5, Enter again");
          check1 = false;
        }
        else if(rating.contains("-")) {
          System.out.println("Review cannot be Negative, Enter again");
          check1 = false;
        }

      }while (!check1);

      System.out.println("enter your review (Textual Description MAX 20 Characters):");
      do {
        review = sc.nextLine();
        if (review.length() > 20)
          System.out.println("Please limit review length to 20, Enter review again");
      }while (review.length()>20);

      LocalDate currdate = LocalDate.now();
      writeintoreviewtable(choice, username, review, rating, Date.valueOf(currdate.toString()));

    }

  }

  private static int  checkifreviewcanbewritten(String username)
  {
        try
        {
          CallableStatement stmt = conn.prepareCall("{? = call checkifreviewcar(?)}");
          stmt.registerOutParameter(1,Types.INTEGER);
          stmt.setString(2,username);
          stmt.execute();
          int ch = stmt.getInt(1);
          return ch;
        }
        catch (Exception e)
        {
          System.out.println(e.getMessage());
        }
          return 0;

  }
  private static void displayusedcar(String username)
  {
    try {
      CallableStatement stmt = conn.prepareCall("{call displayusedcar(?)}");

      stmt.setString(1, username);

      ResultSet ps1 = stmt.executeQuery();

      System.out.printf("--------------------------%n");
      System.out.printf("       CAR REVIEW     %n");
      System.out.printf("--------------------------%n");
      System.out.printf("| %-8s | %-8s |%n", "CAR ID", "NAME");
      System.out.printf("--------------------------%n");



      while (ps1.next()) {
        String out = String.format("| %-8d | %-8s |%n",


                ps1.getInt(1),
                ps1.getString(2));
        System.out.println(out);
      }

      ps1.close();
      stmt.close();

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void writeintoreviewtable(String carid , String username , String review , String rating , Date currdate)
  {
          try
          {
                 CallableStatement stmt = conn.prepareCall("{call writeintoreview(?,?,?,?,?)}");
                 stmt.setString(1,username);
                 stmt.setString(2,carid);
                 stmt.setString(3,review);
                 stmt.setString(4,rating);
                 stmt.setDate(5,currdate);

                 stmt.execute();
                 System.out.println("Your review is Recorded Successfully");

          }
          catch (Exception e)
          {
            System.out.println(e.getMessage());
          }

  }


  private static void accountsetting(String username)
  {
    Scanner sc = new Scanner(System.in);
    System.out.println("\nAccount Settings");
    System.out.println("1.Change password");
    System.out.println("\nPlease enter the operation you want to perform? ");
    String choice = sc.nextLine();

    switch (choice) {
      case "1":
        changepassword(username);
        break;
      default:
        System.out.println("Invalid choice");
        break;
    }

  }

  private static void changeusername(String username) {
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter your new username:");
    String newusername = sc.nextLine();
    // validate new username

    try {

      CallableStatement stmt = conn.prepareCall("{call changeusername(?,?)}");
      stmt.setString(1, newusername);
      stmt.setString(2, username);
      stmt.execute();
      System.out.println("Successfuly Updated your username\n");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }


  }

  private static void changepassword(String username) {

    Scanner sc = new Scanner(System.in);
    System.out.println("Enter your new password:");
    String password = sc.nextLine();
    // validate password

    try {

      CallableStatement stmt = conn.prepareCall("{call changepwd(?,?)}");
      stmt.setString(1, username);
      stmt.setString(2, password);
      stmt.execute();
      System.out.println("Password is changed Successfully!\n");

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }


  private static void addcar() {

    clearscreen();
    Scanner sc = new Scanner(System.in);
    String carname, carmodel, category, carcolor, latefee, carmilage,
            carrating, carrentalprice, carstatus;

    carrating = "0.0";
    carstatus = "AVAILABLE";
    boolean check ;

    System.out.println("\nEnter information of the car\n");
    System.out.println("enter car name");
    do {
      carname = sc.nextLine();
      check = checkforvalidstrings(carname);
      if(!check)
        System.out.println("Invalid Inputs");
    }while (!check);

    carname = carname.toUpperCase();

    System.out.println("enter car model");
    carmodel = sc.nextLine();
    System.out.println("enter car category(SUV/SEDAN/HATCHBACK)");
    do {
      category = sc.nextLine();
      check = checkforvalidstrings(category);
      if(!check)
        System.out.println("Invalid Inputs");
    }while (!check);

    carmodel = carmodel.toUpperCase();

    System.out.println("enter car color");
    do {
      carcolor = sc.nextLine();
      check = checkforvalidstrings(carcolor);
      if(!check)
        System.out.println("Invalid Inputs");
    }while (!check);

    carcolor = carcolor.toUpperCase();

    System.out.println("enter latefee/day");
    do {
      latefee = sc.nextLine();
      if(latefee.contains("-"))
      {
        System.out.println("Invalid Input , Please enter again");
        check = false;
      }
      else
        check =true;
    }while (!check);

    System.out.println("enter mileage");
    do {
      carmilage = sc.nextLine();
      if(carmilage.contains("-"))
      {
        System.out.println("Invalid Input , Please enter again");
        check = false;
      }
      else
        check =true;

    }while (!check);

    System.out.println("enter car rent price/hr");
    do {
      carrentalprice = sc.nextLine();

      if(carrentalprice.contains("-"))
      {
        System.out.println("Invalid Input , Please enter again");
        check = false;
      }
      else
        check =true;

    }while (!check);
    System.out.println("\nAdded successfully\n");

    try {
      CallableStatement cStmt = conn.prepareCall("{call addcar( ?,?,?,?,?,?,?,?,?)}");
      cStmt.setString(1, carname);
      cStmt.setString(2, carmodel);
      cStmt.setString(3, category);
      cStmt.setString(4, carcolor);
      cStmt.setString(5, latefee);
      cStmt.setString(6, carmilage);
      cStmt.setString(7, carrating);
      cStmt.setString(8, carrentalprice);
      cStmt.setString(9, carstatus);

      cStmt.execute();


    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }

  private static void removecar() {

    Scanner sc = new Scanner(System.in);
    boolean check = true;
    clearscreen();

    displayalcars();
    String cid;
    System.out.println("\nEnter car id to remove:");
    do {
      cid = sc.nextLine();
      check = checkcarpresent(cid);
      if (!check) {
        System.out.println("\nInvalid car id please enter again");
      }
    } while (!check);

    deletecar(cid);
    System.out.println("Sucessfully Deleted\n");

  }

  private static void displayalcars() {
    try {
      CallableStatement cStmt = conn.prepareCall("{call displaycar()}");

      ResultSet ps1 = cStmt.executeQuery();

      System.out.printf("------------------------------------%n");
      System.out.printf("              ALL CARS     %n");
      System.out.printf("------------------------------------%n");
      System.out.printf("| %-4s | %-8s | %4s | %-7s |%n", "CID", "NAME", "MODEL", "COLOR");
      System.out.printf("------------------------------------%n");
      while (ps1.next()) {
        String out = String.format("| %-4d | %-8s | %-5s | %-7s |%n",


                ps1.getInt(1),
                ps1.getString(2),
                ps1.getString(3),
                ps1.getString(4));
                System.out.println(out);
      }

      ps1.close();
      cStmt.close();

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static boolean checkcarpresent(String cid) {
    int check = 0;
    try {
      CallableStatement cStmt = conn.prepareCall("{? = call checkcar(?)}");
      cStmt.registerOutParameter(1, Types.INTEGER);
      cStmt.setString(2, cid);
      cStmt.execute();

      check = cStmt.getInt(1);
      if (check == 0)
        return false;
      else
        return true;
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return true;

  }

  private static void deletecar(String cid) {
    try {
      CallableStatement cStmt = conn.prepareCall("{call deletecar(?)}");
      cStmt.setString(1, cid);
      cStmt.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void updatecar() {
    Scanner sc = new Scanner(System.in);

    viewcars();
    boolean check = true;
    String choice, input;
    String tval;
    String carid;
    boolean check1;

    System.out.println("Enter car id You want to update:");
    carid = sc.nextLine();

    if (checkavailabilityofcar(carid)) {
      System.out.println("Available updates are");

      do {
        System.out.println("1.update car name");
        System.out.println("2.update car model");
        System.out.println("3.update car category");
        System.out.println("4.update car color");
        System.out.println("5.update late fee");
        System.out.println("6.update mileage");
        System.out.println("7.update car rent price");
        System.out.println("8.update car rating");
        System.out.println("9.update car status");
        System.out.println("\nEnter your choice:");
        choice = sc.nextLine();

        switch (choice) {
          case "1":
            System.out.println("Enter new name:");
            do {
              input = sc.nextLine();
              check1 = checkforvalidstrings(input);
              if(!check1)
                System.out.println("Invalid Input please enter again");
            }while (!check1);

            tval = "carname";
            check = false;
            executeupdate(carid, tval, input);
            System.out.println("Successfully Updated");
            break;
          case "2":
            System.out.println("enter new car model:");
            input = sc.nextLine();
            tval = "carmodel";
            check = false;
            executeupdate(carid, tval, input);
            System.out.println("Successfully Updated");
            break;
          case "3":
            System.out.println("enter new car category(SUV/SEDAN/HATCHBACK):");
            input = sc.nextLine();
            tval = "category";
            check = false;
            executeupdate(carid, tval, input);
            System.out.println("Successfully Updated");
            break;
          case "4":
            System.out.println("enter new car color:");
            input = sc.nextLine();
            tval = "carcolor";
            check = false;
            executeupdate(carid, tval, input);
            System.out.println("Successfully Updated");
            break;
          case "5":
            System.out.println("enter new late fee:");
            input = sc.nextLine();
            tval = "latefee";
            check = false;
            executeupdate(carid, tval, input);
            System.out.println("Successfully Updated");
            break;

          case "6":
            System.out.println("enter new car mileage:");
            input = sc.nextLine();
            tval = "carmilage";
            check = false;
            executeupdate(carid, tval, input);
            System.out.println("Successfully Updated");
            break;
          case "7":
            System.out.println("enter new car rent price:");
            input = sc.nextLine();
            tval = "carrentalprice";
            check = false;
            executeupdate(carid, tval, input);
            System.out.println("Successfully Updated");
            break;
          case "8":
            System.out.println("enter new car rating:");
            input = sc.nextLine();
            tval = "carrating";
            check = false;
            executeupdate(carid, tval, input);
            System.out.println("Successfully Updated");
            break;
          case "9":
            System.out.println("enter new car status(AVAILABLE/BOOKED):");
            input = sc.nextLine();
            input=input.toUpperCase();
            tval = "carstatus";
            check = false;
            executeupdate(carid, tval, input);
            System.out.println("Successfully Updated");
            break;
          default:
            System.out.println("invalid choice");

        }

      }
      while (check);
    } else {
      System.out.println("invalid car id, because car is currently used by some customer.");
    }


  }

  private static void executeupdate(String carid, String colname, String colval) {

    try {
      CallableStatement cStmt = conn.prepareCall("{call updatecar(?,?,?)}");
      cStmt.setString(1, carid);
      cStmt.setString(2, colname);
      cStmt.setString(3, colval);
      cStmt.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }


  private static void viewcars() {
    displayalcars();
    System.out.println("\n");
  }

  private static void deletecustomer() {
    Scanner sc = new Scanner(System.in);

    displaycustomers();

    System.out.println("\nPlease enter customer username you want to delete:");
    String username = sc.nextLine();

    boolean checkusernameexists = usernameexists(username);

    if (checkusernameexists) {

      try {
        CallableStatement stmt = conn.prepareCall("{call deleteuser(?)}");
        stmt.setString(1, username);
        stmt.execute();
        System.out.println("Sucessfully delete the customer\n");

      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    } else {
      System.out.println("Selected username does not exist!\n");
    }
  }

  private static boolean usernameexists(String username) {
    try {
      int check = 0;
      CallableStatement stmt = conn.prepareCall("{? = call checkuernameexists(?)}");
      stmt.registerOutParameter(1, Types.INTEGER);
      stmt.setString(2, username);
      stmt.execute();
      check = stmt.getInt(1);

      if (check == 0)
        return false;
      else
        return true;

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return true;
  }

  public static void displaycustomers() {
    try {
      CallableStatement stmt = conn.prepareCall("{call displaycustomer()}");
      ResultSet ps1 = stmt.executeQuery();
      System.out.printf("-----------------------------------%n");
      System.out.printf("       CUSTOMER DETAILS           %n");
      System.out.printf("-----------------------------------%n");

      System.out.printf("| %-14s | %-14s |%n", "CUSTOMER ID", "USERNAME");
      System.out.printf("----------------------------------------------%n");


      while (ps1.next()) {
        String out = String.format("| %-14d | %-14s |%n",
                ps1.getInt(1),
                ps1.getString(2));

        System.out.println(out);
      }

      ps1.close();
      stmt.close();

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void clearscreen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  private static boolean checkforvalidstrings(String s)
  {
            char[] str = s.toCharArray();

            for (char c : str) {
              if (Character.isDigit(c)) {
                return false;
              }
            }

            return true;
  }


  private static boolean checkphnlength(String s)
  {
          if(s.length()==10)
            return true;
          else
            return false;

  }

  private static boolean checkphncontainsalpha(String s)
  {
          char[] str = s.toCharArray();

          for (char c : str) {
            if (Character.isAlphabetic(c)) {
              return false;
            }
          }

          return true;
  }


}



