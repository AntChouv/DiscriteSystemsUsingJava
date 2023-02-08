import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author anton
 */
public class ChatGuiServer {
    public static HashMap<String, ArrayList<Movie>> directorsList = new HashMap<>();//HashMap με κλειδί όνομα σκηνοθέτη και τιμή
                                                                                    //ArrayList με ταινίες του σκηνοθέτη
    public static HashMap<String, Movie> movieList = new HashMap<>();//HashMap με κλειδί όνομα ταινίας και τιμή
                                                                     //αντικείμενο μορφή movie με όλες τις πληροφορίες της ταινίας
    
     ServerSocket ss;
     Socket s;
     
     ObjectInputStream in;
     ObjectOutputStream out;
     
     Message messageReceived;//Τα μηνύματα που θα λαμβάνει το server
     Message messageSent;//Τα μηνύματα που θα στέλνει
     
     String movieName;
     String directorName;
     
     MovieList mvl = new MovieList();
     Movie mv = null;
     Movie mv2;
     
     Boolean nameExpected = false;//Ελεγχός αν περιμένει ο Server περιμένει όνομα είτε για εισαγωγή ή για ψάξιμο
     Boolean iminentSearch = false;//Έλεγψος αν πρόκειται να γίνει ψάξιμο


     public ChatGuiServer() throws IOException {}
    public void startSession() throws IOException, ClassNotFoundException{
           
            try{
                ss = new ServerSocket(5000,50);
                mvl.createHashMap();//Δημιουργούμε τα ΗashMap
                System.out.println("New Session");
                System.out.println("Waiting for connection");
                s = ss.accept();
                OutputStream outputStream = s.getOutputStream();
                out = new ObjectOutputStream(outputStream);
                InputStream inputStream = s.getInputStream();
                in = new ObjectInputStream(inputStream);
                Boolean communicate = true;
                while(communicate){
                    messageReceived =(Message) in.readObject();
                    switch(messageReceived.getMessage()) {
                        case "Begin" -> {
                             messageSent = new Message("Listening... What would you like to do?");
                        }
                        case "Search" -> {
                             if (nameExpected == false && iminentSearch == false){//Αν δεν έχει προηγηθεί άλλη εντολή πρίν
                                  String str=null;
                                  messageSent = new Message("Type \"Director\" to search based on Directors Name or \"Movie\" to search based on Movies Name",str);
                                  iminentSearch = true;
                             }else if(nameExpected == true && iminentSearch == false){//Αν περιμένει όνομα για να κάνει insert ταινίας
                                                                                      //Θα παίρνει το search σαν όνομα της ταινίας
                                 insertMovie();
                             }else if(nameExpected == true && iminentSearch == true){//Αν περιμένει όνομα για να κάνει search ταινίας
                                                                                      //Θα παίρνει το search σαν όνομα της ταινίας για
                                                                                      // ψάξει
                                 searchMovieList();
                             }
                             else{
                                 messageSent = new Message("1I did not understand that...");
                             }
                           
                        }
                         case "Insert" -> {
                            if (nameExpected == false && iminentSearch == false){//Αν δεν έχει προηγηθεί άλλη εντολή πρίν
                                 messageSent = new Message("Type movie information","Insert");
                                nameExpected = true;
                            }else if(nameExpected == true && iminentSearch == true){//Αν περιμένει να δωθεί όνομα για να ψάξει και δωθεί 
                                                                                    //το Insert το θα ψάξει κανονικά μήπως υπάρχει
                                                                                    //ταινία με το όνομα Insert
                                searchMovieList();
                            }
                            else{
                                messageSent = new Message("Type 'Movie' or 'Director'...");//Όταν περιμένει Movie ή Director ως κρητίριο
                                                                                           //για να ψάξει και λαμβάνει Insert
                            }
                        }
                        case "End" ->{
                            if(nameExpected == true && iminentSearch == true){//Αν περιμένει να δωθεί όνομα για να ψάξει και δωθεί 
                                                                                    //το End το θα ψάξει κανονικά μήπως υπάρχει
                                                                                    //ταινία με το όνομα End
                                searchMovieList();
                            }
                            else if(nameExpected == true && iminentSearch == false){//Αν περιμένει όνομα για να κάνει insert ταινίας
                                                                                      //Θα παίρνει το End σαν όνομα της ταινίας
                                 insertMovie();
                             }
                            else{
                                messageSent = new Message("End");
                                out.writeObject(messageSent);
                                out.flush();
                                communicate = false;
                                break;
                            }
     
                            
                        }
                        case "Movie" -> {
                            if (iminentSearch == true && nameExpected == false){
                                messageSent = new Message("Type the movie's name","Movie");
                                nameExpected = true;
                            }else{
                                messageSent = new Message("3I did not understand that...");
                            }
                        }
                        case "Director" -> {
                             if (iminentSearch == true){
                                 messageSent = new Message("Type the Directors's name","Director");
                                 nameExpected = true;
                            }else{
                                messageSent = new Message("4I did not understand that...");
                            }
                           
                        }
                        default -> {//Οτιδήποτε άλλο μήνυμα θα είναι όνομα
                                if (nameExpected == false){//Αν δεν αναμένεται όνομα και πληκτρολογιο και δώσει ο χρήστης
                                                          //κάτι που δεν είναι απο τις παραπάνω εντολές
                                    messageSent = new Message("5I did not understand that...");
                                }else
                                     if (messageReceived.getDirections().equalsIgnoreCase("Movie")){
                                        searchMovieList();
                                    }
                                    else if (messageReceived.getDirections().equalsIgnoreCase("Director")){                        
                                        directorName = messageReceived.getMessage();
                                        if (directorsList.containsKey(directorName)){
                                            messageSent = new Message("Director",directorsList.get(directorName));
                                        }else{
                                            messageSent = new Message("No Result Fount. What else would you like to do?");
                                        }
                                            nameExpected = false;
                                            iminentSearch = false;
                                    }else if (messageReceived.getDirections().equalsIgnoreCase("Insert")){
                                       insertMovie();                                            
                                    }
                                }
                        }
                    out.writeObject(messageSent);
                    out.flush();
                    out.reset();
                }
                out.close();
                in.close();
                s.close();
            
            }catch (IOException e){}          
     }
    
        public void searchMovieList(){
            movieName = messageReceived.getMessage();
            mv = movieList.get(movieName);
            if (mv != null){
                messageSent = new Message("Movie",mv);
                mv = null;
            }else {
                messageSent = new Message("No Result Found. What else would you like to do?");
            }
            nameExpected = false;
            iminentSearch = false;
        }
        public void insertMovie(){
            if (mv == null){  
                mv = new Movie(messageReceived.getMessage(),null);
                messageSent = new Message("Type the Name Of the Director...","Insert"); 
            }else{
                mv2 = new Movie(mv.getMovieName(),messageReceived.getMessage());
                mvl.AddNewMovie(mv2);
                messageSent = new Message("Movie Inserted. What else would you like to do?");
                mv = null;
                nameExpected = false;
                iminentSearch = false;
           }
        }
        
    public static void main(String[] args) throws IOException, ClassNotFoundException {
            //ChatGuiServer cgs = new ChatGuiServer();
            while (true){
                ChatGuiServer cgs = new ChatGuiServer();
                cgs.startSession();
            }

        
        
    }

    
}
