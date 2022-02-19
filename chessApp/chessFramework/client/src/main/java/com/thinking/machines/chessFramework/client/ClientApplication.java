package com.thinking.machines.chessFramework.client;
import com.thinking.machines.chessFramework.common.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import com.google.gson.reflect.*;
import com.google.gson.*;
class CloseButtonHandler extends WindowAdapter
{
private ChessFrameworkClient client;
private String uniqueID;
CloseButtonHandler(ChessFrameworkClient client,String uniqueID)
{
this.client=client;
this.uniqueID=uniqueID;
}
public static void exit()
{
System.exit(0);
}
public void windowClosing(WindowEvent ev)
{
Request request=new Request();
request.setAction(UserAction.TERMINATE_GAME);
request.setArguments(uniqueID);
try
{
client.execute(request);
}catch(Throwable t)
{
System.out.println(t);
}
System.exit(0);
}
}
public class ClientApplication extends JFrame implements ActionListener
{
private Container container;
private JButton[][] button;
private ChessFrameworkClient client;
private ImageIcon king_black_icon;
private ImageIcon king_white_icon;
private ImageIcon queen_black_icon;
private ImageIcon queen_white_icon;
private ImageIcon bishop_black_icon;
private ImageIcon bishop_white_icon;
private ImageIcon rook_black_icon;
private ImageIcon rook_white_icon;
private ImageIcon knight_black_icon;
private ImageIcon knight_white_icon;
private ImageIcon pawn_black_icon;
private ImageIcon pawn_white_icon;
private ImageIcon ok_icon;
private ImageIcon undo_icon;
private Point prev_position;
private Point new_position;
public Pieces_color pieces_color;
public Pieces_type pieces_type;
private Map<Point,ChessPieces> pieces_identity;
private java.util.Set<ChessPiecesInfo> pieces_DS;
private javax.swing.Timer timer;
private ChessPieces chessPieces;
private ChessPieces c1;
private ChessPieces c2;
private Icon icon1;
private Icon icon2;
private int flag=0;
private ChessPiecesInfo chessPiecesInfo;
private Panel p1,p2,p3;
private JButton ok_button,undo_button;
private boolean ok_button_checked;
private boolean undo_button_checked;
private JLabel text;
private JLabel turn;
private int user_id=1;
private String uniqueID;
private String userName;
public ClientApplication(ChessFrameworkClient client,String uniqueID,int user_id,String userName)
{  
this.uniqueID=uniqueID;
this.userName=userName;
this.user_id=user_id;
this.client=client;  
initialize();
container=getContentPane();
container.setLayout(new BorderLayout());
p1=new Panel();
p1.setLayout(new GridLayout(8,8));
for(int i=0;i<=7;i++)
{
for(int j=0;j<=7;j++)
{
piecesInitialize(i,j);
button[i][j].addActionListener(this);
if(i%2==0)
{
if(j%2==0)button[i][j].setBackground(Color.WHITE);
else button[i][j].setBackground(new Color(50,205,50));
}
else
{
if(j%2==0)button[i][j].setBackground(new Color(50,205,50));
else button[i][j].setBackground(Color.WHITE);
}
p1.add(button[i][j]);
if(i==0||i==1||i==6||i==7)
{
chessPiecesInfo=new ChessPiecesInfo();
chessPiecesInfo.setPieces_position(new Point(i,j));
chessPiecesInfo.setPieces_color(chessPieces.getPieces_color());
chessPiecesInfo.setPieces_type(chessPieces.getPieces_type());
pieces_DS.add(chessPiecesInfo);
}
}
}
container.add(p1,BorderLayout.CENTER);
ok_button=new JButton(ok_icon);
undo_button=new JButton(undo_icon);
ok_button.addActionListener(this);
undo_button.addActionListener(this);
p3=new Panel();
p3.setLayout(new BorderLayout());
p2=new Panel();
p2.setLayout(new GridLayout(1,5));
p2.add(new JLabel("  "));
p2.add(undo_button);
p2.add(text);
p2.add(ok_button);
p2.add(new JLabel("  "));
p3.add(p2,BorderLayout.SOUTH);
container.add(p3,BorderLayout.SOUTH);
container.add(turn,BorderLayout.NORTH);
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
int width=600;
int height=600;
setLocation((d.width/2)-(width/2),(d.height/2)-(height/2));
setSize(width,height);
setVisible(true);
CloseButtonHandler cbh=new CloseButtonHandler(client,uniqueID);
addWindowListener(cbh);
timer=new javax.swing.Timer(1000,new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
Set<ChessPiecesInfo> pieces_DS=null;
try
{
GenerateRequestObject generateRequestObject=new GenerateRequestObject();
generateRequestObject.uniqueID=uniqueID;
generateRequestObject.userName=userName;
generateRequestObject.info=pieces_DS;
Request request=new Request();
request.setAction(UserAction.PLAY_GAME);
request.setArguments(generateRequestObject);

JsonParser parser = new JsonParser();
JsonObject rootObject = parser.parse(client.execute(request)).getAsJsonObject();

Boolean success = rootObject.get("success").getAsBoolean();
if(success==false)
{
return;
}
JsonElement projectElement = rootObject.get("result");
if(projectElement==null&&success==true)
{
JOptionPane.showMessageDialog(ClientApplication.this,"Game ends");
CloseButtonHandler.exit();
}
Gson gson = new Gson();
Set<ChessPiecesInfo> projectList = new HashSet<>();
if (projectElement.isJsonObject())
{
ChessPiecesInfo info = gson.fromJson(projectElement, ChessPiecesInfo.class);
projectList.add(info);
}
else if (projectElement.isJsonArray())
{
java.lang.reflect.Type projectListType = new TypeToken<Set<ChessPiecesInfo>>() {}.getType();
projectList = gson.fromJson(projectElement, projectListType);
}
if(projectList.size()>0)performTask(projectList);
}catch(Throwable t)
{
System.out.println(t);
}
}
});
if(user_id==1)
{
turn.setText(" White Turn");
}
if(user_id==2)
{
turn.setText(" Black Turn");
text.setText("   ");
ok_button.setEnabled(false);
undo_button.setEnabled(false);
for(int i=0;i<=7;i++)
{
for(int j=0;j<=7;j++)
{
button[i][j].setEnabled(false);
}
}
timer.start();
}
else
{
text.setText("    Your turn ");
}
}
public void initialize()
{
king_black_icon=new ImageIcon("../chess_pieces/king_black.png");
king_white_icon=new ImageIcon("../chess_pieces/king_white.png");
queen_black_icon=new ImageIcon("../chess_pieces/queen_black.png");
queen_white_icon=new ImageIcon("../chess_pieces/queen_white.png");
rook_black_icon=new ImageIcon("../chess_pieces/rook_black.png");
rook_white_icon=new ImageIcon("../chess_pieces/rook_white.png");
knight_black_icon=new ImageIcon("../chess_pieces/knight_black.png");
knight_white_icon=new ImageIcon("../chess_pieces/knight_white.png");
bishop_black_icon=new ImageIcon("../chess_pieces/bishop_black.png");
bishop_white_icon=new ImageIcon("../chess_pieces/bishop_white.png");
pawn_black_icon=new ImageIcon("../chess_pieces/pawn_black.png");
pawn_white_icon=new ImageIcon("../chess_pieces/pawn_white.png");
ok_icon=new ImageIcon("../chess_pieces/ok.png");
undo_icon=new ImageIcon("../chess_pieces/undo.png");
ChessPieces c1=null;
ChessPieces c2=null;
Icon icon1=null;
Icon icon2=null;
pieces_identity=new HashMap<>();
pieces_DS=new HashSet<>();
button = new JButton[8][8];
text=new JLabel("   ");
turn=new JLabel("   ");
}

public void piecesInitialize(int i,int j)
{
if(i==0)
{
if(j==0||j==7)
{
button[i][j]=new JButton(rook_white_icon);
chessPieces=new Rook_chessPieces();
chessPieces.setPieces_color(Pieces_color.WHITE);
chessPieces.setPieces_type(Pieces_type.ROOK);
pieces_identity.put(new Point(i,j),chessPieces);
}
else if(j==1||j==6)
{
button[i][j]=new JButton(knight_white_icon);
chessPieces=new Knight_chessPieces();
chessPieces.setPieces_color(Pieces_color.WHITE);
chessPieces.setPieces_type(Pieces_type.KNIGHT);
pieces_identity.put(new Point(i,j),chessPieces);
}
else if(j==2||j==5)
{
button[i][j]=new JButton(bishop_white_icon);
chessPieces=new Bishop_chessPieces();
chessPieces.setPieces_color(Pieces_color.WHITE);
chessPieces.setPieces_type(Pieces_type.BISHOP);
pieces_identity.put(new Point(i,j),chessPieces);
}
else if(j==3)
{
button[i][j]=new JButton(queen_white_icon);
chessPieces=new Queen_chessPieces();
chessPieces.setPieces_color(Pieces_color.WHITE);
chessPieces.setPieces_type(Pieces_type.QUEEN);
pieces_identity.put(new Point(i,j),chessPieces);
}
else if(j==4)
{
button[i][j]=new JButton(king_white_icon);
chessPieces=new King_chessPieces();
chessPieces.setPieces_color(Pieces_color.WHITE);
chessPieces.setPieces_type(Pieces_type.KING);
pieces_identity.put(new Point(i,j),chessPieces);
}
}
else if(i==1)
{
button[i][j]=new JButton(pawn_white_icon);
chessPieces=new Pawn_chessPieces();
chessPieces.setPieces_color(Pieces_color.WHITE);
chessPieces.setPieces_type(Pieces_type.PAWN);
pieces_identity.put(new Point(i,j),chessPieces);
}
else if(i==6)
{
button[i][j]=new JButton(pawn_black_icon);
chessPieces=new Pawn_chessPieces();
chessPieces.setPieces_color(Pieces_color.BLACK);
chessPieces.setPieces_type(Pieces_type.PAWN);
pieces_identity.put(new Point(i,j),chessPieces);
}
else if(i==7)
{
if(j==0||j==7)
{
button[i][j]=new JButton(rook_black_icon);
chessPieces=new Rook_chessPieces();
chessPieces.setPieces_color(Pieces_color.BLACK);
chessPieces.setPieces_type(Pieces_type.ROOK);
pieces_identity.put(new Point(i,j),chessPieces);
}
else if(j==1||j==6)
{
button[i][j]=new JButton(knight_black_icon);
chessPieces=new Knight_chessPieces();
chessPieces.setPieces_color(Pieces_color.BLACK);
chessPieces.setPieces_type(Pieces_type.KNIGHT);
pieces_identity.put(new Point(i,j),chessPieces);
}
else if(j==2||j==5)
{
button[i][j]=new JButton(bishop_black_icon);
chessPieces=new Bishop_chessPieces();
chessPieces.setPieces_color(Pieces_color.BLACK);
chessPieces.setPieces_type(Pieces_type.BISHOP);
pieces_identity.put(new Point(i,j),chessPieces);
}
else if(j==3)
{
button[i][j]=new JButton(queen_black_icon);
chessPieces=new Queen_chessPieces();
chessPieces.setPieces_color(Pieces_color.BLACK);
chessPieces.setPieces_type(Pieces_type.QUEEN);
pieces_identity.put(new Point(i,j),chessPieces);
}
else if(j==4)
{
button[i][j]=new JButton(king_black_icon);
chessPieces=new King_chessPieces();
chessPieces.setPieces_color(Pieces_color.BLACK);
chessPieces.setPieces_type(Pieces_type.KING);
pieces_identity.put(new Point(i,j),chessPieces);
}
}
else button[i][j]=new JButton(new String("   "));
}



public void actionPerformed(ActionEvent ev)
{
if(ev.getSource()==ok_button)
{
pieces_DS=new HashSet<>();
chessPiecesInfo=new ChessPiecesInfo();
chessPiecesInfo.setPieces_position(new_position);
chessPiecesInfo.setPieces_color(c1.getPieces_color());
chessPiecesInfo.setPieces_type(c1.getPieces_type());
pieces_DS.add(chessPiecesInfo);
chessPiecesInfo=new ChessPiecesInfo();
chessPiecesInfo.setPieces_position(prev_position);
chessPiecesInfo.setPieces_color(null);
chessPiecesInfo.setPieces_type(null);
pieces_DS.add(chessPiecesInfo);
if(flag==3&&c2.getPieces_type()==Pieces_type.KING)
{
if(c2.getPieces_color()==Pieces_color.WHITE)
{
JOptionPane.showMessageDialog(this,"Black Win");



}
else if(c2.getPieces_color()==Pieces_color.BLACK)
{
JOptionPane.showMessageDialog(this,"White Win");


}
chessPiecesInfo=new ChessPiecesInfo();
chessPiecesInfo.setPieces_position(null);
chessPiecesInfo.setPieces_color(null);
chessPiecesInfo.setPieces_type(null);
pieces_DS.add(chessPiecesInfo);
}
try
{
flag=0;
GenerateRequestObject generateRequestObject=new GenerateRequestObject();
generateRequestObject.uniqueID=uniqueID;
generateRequestObject.userName=userName;
generateRequestObject.info=pieces_DS;
Request request=new Request();
request.setAction(UserAction.PLAY_GAME);
request.setArguments(generateRequestObject);
client.execute(request);
text.setText("   ");
ok_button.setEnabled(false);
undo_button.setEnabled(false);
for(int i=0;i<=7;i++)
{
for(int j=0;j<=7;j++)
{
button[i][j].setEnabled(false);
}
}
timer.start();
System.out.println("Timer start");
}catch(Throwable t)
{
System.out.println(t);
}
}
if(ev.getSource()==undo_button)
{
if(new_position==null)
{
if(prev_position!=null)setBackground(prev_position.x,prev_position.y);
prev_position=null;
flag=0;
icon1=null;
icon2=null;
}
else if(flag==2||flag==3)
{
c1=pieces_identity.get(prev_position);
c2=pieces_identity.get(new_position);
if(button[prev_position.x][prev_position.y].getIcon()==null)
{
pieces_identity.remove(new_position);
pieces_identity.put(prev_position,c2);
button[new_position.x][new_position.y].setIcon(icon2);
button[prev_position.x][prev_position.y].setIcon(icon1);
} 
else
{
button[prev_position.x][prev_position.y].setIcon(icon1);
button[new_position.x][new_position.y].setIcon(null);
pieces_identity.remove(prev_position);
pieces_identity.remove(new_position);
pieces_identity.put(prev_position,c2);
} 
if(prev_position!=null)setBackground(prev_position.x,prev_position.y);
if(new_position!=null)setBackground(new_position.x,new_position.y);
prev_position=null;
new_position=null;
flag=0;
icon1=null;
icon2=null;
}
}

if((flag>3)||(flag==2&&(new_position!=null)))
{
return;
}
int row=0;
int col=0;
Point p;
for (row = 0; row<=7; row++) 
{
for (col = 0; col<=7; col++) 
{
if (button[row][col] == ev.getSource())
{
p=new Point(row,col);
if(pieces_identity.containsKey(p))
{
flag++;
}
if(flag==0)
{
return;
}
else if(flag==1)
{
chessPieces=pieces_identity.get(p);
if(chessPieces==null)
{
return;
}
if((this.user_id==1)&&chessPieces!=null&&chessPieces.getPieces_color()==Pieces_color.WHITE)
{
if(prev_position!=null)setBackground(prev_position.x,prev_position.y);
if(new_position!=null)setBackground(new_position.x,new_position.y);
prev_position=null;
new_position=null;
flag++;
}
else if((this.user_id==2)&&chessPieces!=null&&chessPieces.getPieces_color()==Pieces_color.BLACK)
{
if(prev_position!=null)setBackground(prev_position.x,prev_position.y);
if(new_position!=null)setBackground(new_position.x,new_position.y);
prev_position=null;
new_position=null;
flag++;
}
else
{
if(prev_position!=null)setBackground(prev_position.x,prev_position.y);
if(new_position!=null)setBackground(new_position.x,new_position.y);
prev_position=null;
new_position=null;
flag=0;
return;
}
button[row][col].setBackground(Color.YELLOW);
prev_position=p;
}
else if(flag==2||flag==3)
{
new_position=p;
c1=pieces_identity.get(prev_position);
c2=pieces_identity.get(new_position);
if(flag==3&&(c1.getPieces_color()==c2.getPieces_color()))
{
flag=0;
if(prev_position!=null)setBackground(prev_position.x,prev_position.y);
if(new_position!=null)setBackground(new_position.x,new_position.y);
prev_position=null;
new_position=null;
return;
}

button[row][col].setBackground(Color.YELLOW);
icon1=button[prev_position.x][prev_position.y].getIcon();
icon2=button[new_position.x][new_position.y].getIcon();
boolean enemy=false;
if(flag==3)enemy=true;
if(!c1.Process(prev_position,new_position,pieces_identity,enemy))
{
icon1=null;
icon2=null;
flag=0;
if(prev_position!=null)setBackground(prev_position.x,prev_position.y);
if(new_position!=null)setBackground(new_position.x,new_position.y);
prev_position=null;
new_position=null;
return;
}
if(button[new_position.x][new_position.y].getIcon()==null)
{
pieces_identity.remove(prev_position);
pieces_identity.put(new_position,c1);
button[prev_position.x][prev_position.y].setIcon(icon2);
button[new_position.x][new_position.y].setIcon(icon1);
} 
else
{
button[new_position.x][new_position.y].setIcon(icon1);
button[prev_position.x][prev_position.y].setIcon(null);
pieces_identity.remove(prev_position);
pieces_identity.remove(new_position);
pieces_identity.put(new_position,c1);
} 
}
}
}
}
}
public void setBackground(int i,int j)
{
if(i%2==0)
{
if(j%2==0)button[i][j].setBackground(Color.WHITE);
else button[i][j].setBackground(new Color(50,205,50));
}
else
{
if(j%2==0)button[i][j].setBackground(new Color(50,205,50));
else button[i][j].setBackground(Color.WHITE);
}
}
public void performTask(Set<ChessPiecesInfo> pieces_DS)
{
if(pieces_DS.size()==3)
{
if(user_id==1)
{
JOptionPane.showMessageDialog(this,"Black Win");
Request request=new Request();
request.setAction(UserAction.TERMINATE_GAME);
request.setArguments(uniqueID);
try
{
client.execute(request);
}catch(Throwable t)
{
System.out.println(t);
}
System.exit(0);
}
else if(user_id==2)
{
JOptionPane.showMessageDialog(this,"White Win");
Request request=new Request();
request.setAction(UserAction.TERMINATE_GAME);
request.setArguments(uniqueID);
try
{
client.execute(request);
}catch(Throwable t)
{
System.out.println(t);
}
System.exit(0);
}
}


if(pieces_DS.size()!=0)
{
ok_button.setEnabled(true);
undo_button.setEnabled(true);
for(int i=0;i<=7;i++)
{
for(int j=0;j<=7;j++)
{
button[i][j].setEnabled(true);
}
}
timer.stop();
text.setText("    Your turn ");
System.out.println("Timer stop");
}
else
{
return;
}
int pos_x=0;
int pos_y=0;
for(ChessPiecesInfo info: pieces_DS)
{
pos_x=info.getPieces_position().x;
pos_y=info.getPieces_position().y;
if(info.getPieces_color()==Pieces_color.WHITE)
{
if(info.getPieces_type()==Pieces_type.KING)
{
button[pos_x][pos_y].setIcon(new ImageIcon("../chess_pieces/king_white.png"));
chessPieces=new King_chessPieces();
chessPieces.setPieces_color(Pieces_color.WHITE);
chessPieces.setPieces_type(Pieces_type.KING);
pieces_identity.put(new Point(pos_x,pos_y),chessPieces);
}
else if(info.getPieces_type()==Pieces_type.QUEEN)
{
button[pos_x][pos_y].setIcon(new ImageIcon("../chess_pieces/queen_white.png"));
chessPieces=new Queen_chessPieces();
chessPieces.setPieces_color(Pieces_color.WHITE);
chessPieces.setPieces_type(Pieces_type.QUEEN);
pieces_identity.put(new Point(pos_x,pos_y),chessPieces);
}
else if(info.getPieces_type()==Pieces_type.PAWN)
{
button[pos_x][pos_y].setIcon(new ImageIcon("../chess_pieces/pawn_white.png"));
chessPieces=new Pawn_chessPieces();
chessPieces.setPieces_color(Pieces_color.WHITE);
chessPieces.setPieces_type(Pieces_type.PAWN);
pieces_identity.put(new Point(pos_x,pos_y),chessPieces);
}
else if(info.getPieces_type()==Pieces_type.BISHOP)
{
button[pos_x][pos_y].setIcon(new ImageIcon("../chess_pieces/bishop_white.png"));
chessPieces=new Bishop_chessPieces();
chessPieces.setPieces_color(Pieces_color.WHITE);
chessPieces.setPieces_type(Pieces_type.BISHOP);
pieces_identity.put(new Point(pos_x,pos_y),chessPieces);
}
else if(info.getPieces_type()==Pieces_type.KNIGHT)
{
button[pos_x][pos_y].setIcon(new ImageIcon("../chess_pieces/knight_white.png"));
chessPieces=new Knight_chessPieces();
chessPieces.setPieces_color(Pieces_color.WHITE);
chessPieces.setPieces_type(Pieces_type.KNIGHT);
pieces_identity.put(new Point(pos_x,pos_y),chessPieces);
}
else if(info.getPieces_type()==Pieces_type.ROOK)
{
button[pos_x][pos_y].setIcon(new ImageIcon("../chess_pieces/rook_white.png"));
chessPieces=new Rook_chessPieces();
chessPieces.setPieces_color(Pieces_color.WHITE);
chessPieces.setPieces_type(Pieces_type.ROOK);
pieces_identity.put(new Point(pos_x,pos_y),chessPieces);
}
}

else if(info.getPieces_color()==Pieces_color.BLACK)
{
if(info.getPieces_type()==Pieces_type.KING)
{
button[pos_x][pos_y].setIcon(new ImageIcon("../chess_pieces/king_black.png"));
chessPieces=new King_chessPieces();
chessPieces.setPieces_color(Pieces_color.BLACK);
chessPieces.setPieces_type(Pieces_type.KING);
pieces_identity.put(new Point(pos_x,pos_y),chessPieces);
}
else if(info.getPieces_type()==Pieces_type.QUEEN)
{
button[pos_x][pos_y].setIcon(new ImageIcon("../chess_pieces/queen_black.png"));
chessPieces=new Queen_chessPieces();
chessPieces.setPieces_color(Pieces_color.BLACK);
chessPieces.setPieces_type(Pieces_type.QUEEN);
pieces_identity.put(new Point(pos_x,pos_y),chessPieces);
}
else if(info.getPieces_type()==Pieces_type.PAWN)
{
button[pos_x][pos_y].setIcon(new ImageIcon("../chess_pieces/pawn_black.png"));
chessPieces=new Pawn_chessPieces();
chessPieces.setPieces_color(Pieces_color.BLACK);
chessPieces.setPieces_type(Pieces_type.PAWN);
pieces_identity.put(new Point(pos_x,pos_y),chessPieces);
}
else if(info.getPieces_type()==Pieces_type.BISHOP)
{
button[pos_x][pos_y].setIcon(new ImageIcon("../chess_pieces/bishop_black.png"));
chessPieces=new Bishop_chessPieces();
chessPieces.setPieces_color(Pieces_color.BLACK);
chessPieces.setPieces_type(Pieces_type.BISHOP);
pieces_identity.put(new Point(pos_x,pos_y),chessPieces);
}
else if(info.getPieces_type()==Pieces_type.KNIGHT)
{
button[pos_x][pos_y].setIcon(new ImageIcon("../chess_pieces/knight_black.png"));
chessPieces=new Knight_chessPieces();
chessPieces.setPieces_color(Pieces_color.BLACK);
chessPieces.setPieces_type(Pieces_type.KNIGHT);
pieces_identity.put(new Point(pos_x,pos_y),chessPieces);
}
else if(info.getPieces_type()==Pieces_type.ROOK)
{
button[pos_x][pos_y].setIcon(new ImageIcon("../chess_pieces/rook_black.png"));
chessPieces=new Rook_chessPieces();
chessPieces.setPieces_color(Pieces_color.BLACK);
chessPieces.setPieces_type(Pieces_type.ROOK);
pieces_identity.put(new Point(pos_x,pos_y),chessPieces);
}
}
else 
{
button[pos_x][pos_y].setIcon(null);
pieces_identity.remove(new Point(pos_x,pos_y));
}
}
}
}