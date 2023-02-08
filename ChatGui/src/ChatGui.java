import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;
/**
 *
 * @author anton
 */
public class ChatGui extends JFrame{

    Socket s;
    ObjectInputStream in;
    ObjectOutputStream out;
    
    private JLabel header = new JLabel("Client");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private JScrollPane scrollPane;

    
    private Font font = new Font("Roboto", Font.PLAIN, 20);
    
     Message messageReceived;
     Message messageSent;
     String name = null;
     String directorName;
     String msg = "Begin";
     Movie mv;
     
    ChatGui(){
        try{
            s = new Socket("localhost",5000);
            OutputStream outputStream = s.getOutputStream();
            out = new ObjectOutputStream(outputStream);
            
            InputStream inputStream = s.getInputStream();
            in = new ObjectInputStream(inputStream);
           createGUI(); 
           handleEvents();
           startSession();
        }catch(Exception e){
            
        }
        
    }
     private void handleEvents() {
        messageInput.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent ke) {
                
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode()==10){//KeyCode of Enter = 10   
                        msg = messageInput.getText();
                        messageSent = new Message(msg,name);
                    try {
                        out.writeObject(messageSent);
                        out.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(ChatGui.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    messageArea.append("Me : "+msg+"\n");
                    
                    messageInput.setText("");
                }
            }
            
        });
    }
    private void createGUI() {
        this.setTitle("Client Messenger[END]");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        header.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        header.setHorizontalTextPosition(SwingConstants.CENTER);
        header.setVerticalTextPosition(SwingConstants.BOTTOM);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        messageArea.setLineWrap(true);
        messageArea.setEditable(false);
        messageArea.setWrapStyleWord(true);

        DefaultCaret caret = (DefaultCaret)messageArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        this.setLayout(new BorderLayout());
        this.add(header,BorderLayout.NORTH);
        this.add(messageArea,BorderLayout.CENTER);
        scrollPane = new JScrollPane(messageArea);

        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
        this.getContentPane().add(scrollPane);
        
        
        this.add(messageInput,BorderLayout.SOUTH);

        
        
        this.setVisible(true);
        
        
        
    }
    public void startSession() throws ClassNotFoundException, IOException{
        try{
             
            if (msg.equals("Begin")){//Ωστε να μη χρειάζεται ο χρήστης να στείλει Begin
                                     //στέλνετε αυτόματα στην αρχή της συνδεσης
                System.out.println(msg);
                    messageSent = new Message(msg);
                    out.writeObject(messageSent);
                    out.flush();
                    messageReceived = (Message)in.readObject();

                    messageArea.append("Server : " + messageReceived.getMessage()+"\n");
                    msg = null;
            }
            while(true){
                messageReceived = (Message)in.readObject();
                msg = messageReceived.getMessage();
                name = messageReceived.getDirections();
                
                if (msg.equals("Movie")){
                    mv = messageReceived.getMovie();
                    messageArea.append("Server : " + mv.getMovieName()+" - "+mv.getDirector()+"\n");
                }else if (msg.equals("Director")){
                    for(Movie movie : messageReceived.getDirectorsMovies()){
                        messageArea.append("Server : " + movie.getMovieName()+" - "+movie.getDirector()+"\n");
                    }
                }else if (msg.equals("Type movie information")){
                    messageArea.append("""
                                       Server : Type Name Of the Movie...
                                       """);
                }
                else if (msg.equals("Type the Name Of the Director...")){
                    messageArea.append("""
                                       Server : Type the Name Of the Director...
                                       """);
                }else if (msg.equals("End")){
                    
                    messageArea.append("Server : " + messageReceived.getMessage()+"\n");
                    System.exit(0);
                }
                else if ((name == null)){ 
                    messageArea.append("Server : " + messageReceived.getMessage()+"\n");
                    name = messageReceived.getDirections();

                }else if (name.equals("Movie")){
                    //System.out.print("----------------");
                    messageArea.append("Server : " + messageReceived.getMessage()+"\n");
                }else{
                    messageArea.append("Server : " + messageReceived.getMessage()+"\n");
                }            
        }


        }
        catch (IOException e){
        }
        finally{
            if (s != null)
                s.close();
                
        }
        out.flush();
        out.close();
        in.close();
    }        
    public static void main(String[] args) {
        ChatGui cG = new ChatGui();
    }

   
    
}