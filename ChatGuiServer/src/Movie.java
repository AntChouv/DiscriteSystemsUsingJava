import java.io.*;
 
class Movie implements Serializable {
    private String name;
    private String direct;
 
    Movie(String n, String direct){
        
        this.name = n;
        this.direct = direct;
    }
    public String getMovieName(){ 
        return name; 
    }
    public String getDirector(){
        return direct; 
    }

}