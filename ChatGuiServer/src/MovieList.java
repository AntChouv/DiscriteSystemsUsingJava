import java.io.*;
import java.util.*;
/*
Η κλάση MyObjectOutputStream βοηθάει γιατι όταν έρχεται το σημείο να γράψουμε
κανούριο αντικείμενο στο αρχείο που έχει ήδη μέσα κάποια, ενώ δεν παίρνουμε κανένα error
όταν πάμε να διαβάσουμε τα αντκείμενα παίρνουμε StreamCorruptedException.

Αυτό γίνεται διότι όταν προσθέτουμε αντικείμενο στο αρχείο η μέθοδος
ObjectOutputStream θα γράψει την επικεφαλίδα στο τέλος του αρχείου.
Κάθε φορά που ανοίγουμε το αρχείο και το πρώτο αντικείμενο γράφεται η 
ObjectOuputStream γράφει την κεφαλιδα στο τέλος. Ετσι η κεφαλίσα γράφεται πολλές
φορές.

Έτσι όταν το μεγαθος του αρχείου είναι > 0 χρησιμοποιούμε την MyObjectOutputStream()
*/


class MyObjectOutputStream extends ObjectOutputStream {
 
    MyObjectOutputStream() throws IOException{
        super();
    }
    MyObjectOutputStream(OutputStream o) throws IOException{
        super(o);
    }
    @Override
    public void writeStreamHeader() throws IOException{//Κάνουμε override την  writeStreamHeader()
                                                       //να μη γράψει τιποτα
        return;
    }
}
public class MovieList {
 
     
    private static File f = new File("MoviesNew1.txt");
 
    public void AddNewMovie(Movie mv){

         if (ChatGuiServer.directorsList.containsKey(mv.getDirector())){//τη βάζουμε στις hashmap που χρησιμοποιεί ο server
             ChatGuiServer.directorsList.get(mv.getDirector()).add(mv);
        }else{
            ChatGuiServer.directorsList.put(mv.getDirector(), new ArrayList<Movie>());
            ChatGuiServer.directorsList.get(mv.getDirector()).add(mv);
        }
        ChatGuiServer.movieList.put(mv.getMovieName(),mv);
 
        if (mv != null) {
            try { 
                FileOutputStream file_out = null;
                file_out = new FileOutputStream("MoviesNew1.txt", true);
 
                if (f.length() == 0) {
                    ObjectOutputStream out = new ObjectOutputStream(file_out);
                    out.writeObject(mv);
                    out.flush();
                    out.close();
                }else {//Εξηγήθηκε στην αρχή
                    MyObjectOutputStream out = null;
                    out = new MyObjectOutputStream(file_out);
                    out.writeObject(mv);

                    out.flush();
                    out.close();
                }
                file_out.close();
            }
            catch (Exception e) {
                System.out.println("Error Occurred" + e);
            }
        }
    }
    public void createHashMap() throws ClassNotFoundException{//Καλείται μία φορά όταν ξεκινάει ο server
        
        Movie mv;
        try {
            f.createNewFile();
        } 
        catch (IOException e) {
        }
        if (f.length() != 0) {
 
            try { 
                FileInputStream file_in = null;
 
                file_in = new FileInputStream("MoviesNew1.txt");
                ObjectInputStream in = new ObjectInputStream(file_in);
 
                
                
               while (file_in.available() != 0) {
                    mv = (Movie)in.readObject();
                    if (ChatGuiServer.directorsList.get(mv.getDirector())!= null){
                        ChatGuiServer.directorsList.get(mv.getDirector()).add(mv);
                    }else{
                        
                        ChatGuiServer.directorsList.put(mv.getDirector(), new ArrayList<>());
                        ChatGuiServer.directorsList.get(mv.getDirector()).add(mv);
                    }
                    ChatGuiServer.movieList.put(mv.getMovieName(),mv);
                }
                in.close();
                file_in.close();
            }
            catch (IOException e) {
                System.out.println("Error Occurred" + e);
            }
        }
    }
}