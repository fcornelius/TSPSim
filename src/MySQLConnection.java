import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//MYSQL CONNECTION.Jar muss im Projekt eingebunden sein!!! 
//XAMPP Installieren -> MYSQL starten, apache starten
//Browser "localhost" eingeben
//sql Datei in DB einbinden und TSP Programm starten ->
//In der Konsole muss eine Info Meldung erscheinen das alles OK ist, ansonsten ERROR

//IN eclipse muss unter project -> properties -> java build parth -> libraries -> addJar
//Die mysql-connector.jar Datei aus dem aktuellen TSP Project im ordner bin->mysql ausgewählt werden und bestätigen

class MySQLConnection{
	
  private static Connection con = null;
  private String dbHost = "localhost"; // Hostname
  private String dbPort = "3306";      // Port -- Standard: 3306
  private String dbName = "test";   // Datenbankname //Testtabelle
  private String dbUser = "root";     // Datenbankuser
  private String dbPass = "";      // Datenbankpasswort
  
  //Konstruktor initalisiert die Verbindung
  public MySQLConnection(){
    try {
      Class.forName("com.mysql.jdbc.Driver"); // Datenbanktreiber für JDBC Schnittstellen laden.
      
      // Verbindung zur JDBC-Datenbank herstellen.
      con = DriverManager.getConnection("jdbc:mysql://"+this.dbHost+":"+ this.dbPort+"/"+this.dbName+"?"+"user="+this.dbUser+"&"+"password="+this.dbPass);
    } catch (ClassNotFoundException e) {
      System.out.println("Treiber nicht gefunden");
    } catch (SQLException e) {
      System.out.println("Verbindung zur Datenbank nicht moglich");
      System.out.println("SQLException: " + e.getMessage());
      System.out.println("SQLState: " + e.getSQLState());
      System.out.println("VendorError: " + e.getErrorCode());
    }
  }
  
  //Neue MYSQL Verbindung erstellen
  private static Connection getInstance(){
    if(con == null)
    new MySQLConnection();
    return con;
  }
  
  //Gebe Tabelle in die Konsole aus
  @SuppressWarnings("null")
public void getTestVerbindungZuTestTable(){
    con = getInstance();
    
    if(con != null){ //Solange die Verbindung besteht
      // Abfrage-Statement erzeugen.
      Statement query;
      try {
    	  query = MySQLConnection.con.createStatement();
    	  String sql = "SELECT id FROM java_table";
    	  query.executeQuery(sql);
          ResultSet result = query.executeQuery(sql); 
          
          while (result.next()){
	          int return_value = result.getInt("id");  //Hier das Attribut ausgeben       
	          System.out.println("Die Test ID ist: " + return_value); //String Variable vom Rückgabewert ausgeben
	          
	          System.out.println("Verbindung zur Datenbank Tabelle besteht und ist OK");
          }
    
        //Verindung beenden!!!
        con.close();
      } catch (SQLException e) {  //Ansonsten Fehlermeldung
        e.printStackTrace();
      }
    }
  }
}