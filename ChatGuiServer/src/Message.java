
import java.io.Serializable;
import java.util.ArrayList;


public class Message implements Serializable {
    private String message = null;
    private Movie movie = null;
    private String directions = null;
    private ArrayList<Movie> resultMovieList = null;
    
 
    Message(String msg,Movie mv){
       message = msg;
       movie = mv;
    }
    Message(Movie mv){
       movie = mv;
    }
     Message(String m){
       message = m;
    }
     Message(String m,String n){
       message = m;
       directions = n;
    }
   Message(String m,ArrayList<Movie> result){
        message = m;
        resultMovieList = result;
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