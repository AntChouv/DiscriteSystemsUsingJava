
import java.io.Serializable;
import java.util.ArrayList;


public class Message implements Serializable {
    private String message = null;//είναι το μήνυμα που στέλνει ο client και ο server που είναι οι 
                                  //ερωτήσεις και οι απαντήσεις
    private Movie movie = null;
    private String directions = null;//στέλνεται όταν πρέπει να ολοκληρωθεί κάποια ενέργεια ώστε να ξέρει ο server
                               //τι πρέπει να κάνει. ΠΧ όλα το directions γίνει director τοτε ξέρει ο server ότι πρέπει
                               //να κάνει search με το όνομα του director που δώθηκε στο message. Το directions αρχικοποιείται
                               //απο τον server. ΠΧ όταν ο client στείλει Search τότε ο server to λαμβάνει και ρωτάει
                               //movie η Director. Ο client στελνει μηνυμα 'Director' στο message. Ο server το λαμβάνει 
                               //και στέλνει μήνυμα ρωτόντας το όνομα και βάζει στο directions το 'Director' ωστε να ξέρει η
                               //εφαρμογή του Client τι όνομα περιμένει o server. Έτσι μετά απο τον client στέλνει μήνυμα
                               //message(message,name) όπου στο message είναι το όνομα και στο name το Director.
    private ArrayList<Movie> resultMovieList = null;
    
 
     Message(String m){
       message = m;
    }
     Message(String m,String n){
       message = m;
       directions = n;
    }
     Message(String msg,Movie mv){
       message = msg;
       movie = mv;
    }
    Message(String m,ArrayList<Movie> result){
        message = m;
        resultMovieList = result;
    }
     Message(Movie mv){
       movie = mv;
    }

    public Movie getMovie(){ 
        return movie; 
    }
    public String getMessage(){
        return message; 
    }
    public String getDirections(){
        return directions;
    }
    public ArrayList<Movie> getDirectorsMovies(){
        return resultMovieList;
    }

}