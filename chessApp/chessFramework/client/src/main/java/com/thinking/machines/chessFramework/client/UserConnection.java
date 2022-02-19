package com.thinking.machines.chessFramework.client;
import com.thinking.machines.chessFramework.common.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
public class UserConnection extends JFrame
{
private JList list;
private java.util.Vector<String> v;
private JButton b1,b2;
private Container container;
private JPanel p1;
private ImageIcon chess_dp;
private String userName;
private javax.swing.Timer t1=null;
private javax.swing.Timer t2=null;
private javax.swing.Timer t3=null;
private String uniqueId;
public UserConnection(String name,String password)
{
this.userName=name;
NewUserLogin login=new NewUserLogin();
login.setName(name);
login.setPassword(password);
Request request=new Request();
request.setAction(UserAction.NEW_USER_LOGIN);
request.setArguments(login);
try
{
Response response=JSONUtil.fromJSON(ChessFrameworkClient.execute(request),Response.class);
if(response.getSuccess()==false)
{
System.out.println("User not be added");
return;
}
else
{
System.out.println("User added");
}
}catch(Throwable t)
{
System.out.println(t);
}
chess_dp=new ImageIcon("C:/chess/part-5/chessApp/chess_pieces/chess_logo.png");
b1=new JButton(chess_dp);
b2=new JButton("Invite to play");
b2.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
if(list.getSelectedIndex()<0)
{
return;
}
String flag="Play match with "+(String)list.getSelectedValue();
int selectedOption=JOptionPane.showConfirmDialog(UserConnection.this,flag,"Match",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
if(selectedOption==JOptionPane.YES_OPTION)
{
Invitation invitation=new Invitation();
invitation.whoGotInvited=(String)list.getSelectedValue();
invitation.whoInvited=userName;
Request request=new Request();
request.setAction(UserAction.INVITE_USER);
request.setArguments(invitation);
try
{
ChessFrameworkClient.execute(request);
}catch(Throwable t)
{
System.out.println(t);
}
AcceptedInvitation acceptedInvitation=new AcceptedInvitation();
acceptedInvitation.whoHadInvited=userName;
acceptedInvitation.whoAccepted=(String)list.getSelectedValue();
t3=new javax.swing.Timer(500,new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
Request request=new Request();
request.setAction(UserAction.AS_INVITATION_ACCEPTED_OR_NOT);
request.setArguments(acceptedInvitation);
String result;
try
{
Response response=JSONUtil.fromJSON(ChessFrameworkClient.execute(request),Response.class);
result=(String)response.getResult();
if((response.getSuccess()==true)&&result.equalsIgnoreCase("No"))
{
JOptionPane.showMessageDialog(UserConnection.this,"Invitation rejected");
list.clearSelection();
t3.stop();
}
else if((response.getSuccess()==true)&&result!=null)
{
String uniqueID=(String)response.getResult();
ChessFrameworkClient client=new ChessFrameworkClient(uniqueID,1,userName);
dispose();
list.clearSelection();
t1.stop();
t2.stop();
t3.stop();
}
else
{
System.out.println("Not saving");
list.clearSelection();
}
}catch(Throwable t)
{
System.out.println(t);
}
}
});
t3.start();
}
}
});
v=new java.util.Vector<>();
list=new JList(v);
p1=new JPanel();
p1.setLayout(new BorderLayout());
p1.add(list,BorderLayout.CENTER);
p1.add(b2,BorderLayout.SOUTH);
p1.add(new JButton("Online Users"),BorderLayout.NORTH);
container=getContentPane();
container.setLayout(new BorderLayout());
container.add(b1,BorderLayout.CENTER);
container.add(p1,BorderLayout.EAST);
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
int width=470;
int height=330;
setLocation((d.width/2)-(width/2),(d.height/2)-(height/2));
setSize(width,height);
setVisible(true);
setDefaultCloseOperation(EXIT_ON_CLOSE);
t1=new javax.swing.Timer(3000,new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
Request request=new Request();
request.setAction(UserAction.LIST_OF_ONLINE_USERS);
request.setArguments(null);
try
{
Response response=JSONUtil.fromJSON(ChessFrameworkClient.execute(request),Response.class);
if(response.getSuccess()==true)
{
ArrayList<String> l=(ArrayList<String>)response.getResult();
v.clear();
for(String s:l)
{
if(!s.equals(userName))v.add(s);
}
list.updateUI();
repaint();
}
}catch(Throwable t)
{
System.out.println(t);
}
}
});
t1.start();
t2=new javax.swing.Timer(4000,new ActionListener(){
public void actionPerformed(ActionEvent ev)
{
Request request=new Request();
request.setAction(UserAction.HAVE_ANY_INVITATION);
request.setArguments(userName);
try
{
Response response=JSONUtil.fromJSON(ChessFrameworkClient.execute(request),Response.class);
if(response.getSuccess()==true)
{
String name=(String)response.getResult();
String player="Wants to play with "+name;
int selectedOption=JOptionPane.showConfirmDialog(UserConnection.this,player,"Match",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
if(selectedOption==JOptionPane.YES_OPTION)
{
AcceptedInvitation acceptedInvitation=new AcceptedInvitation();
acceptedInvitation.whoHadInvited=name;
acceptedInvitation.whoAccepted=userName;
request=new Request();
request.setAction(UserAction.INVITATION_ACCEPTED);
request.setArguments(acceptedInvitation);
try
{
response=JSONUtil.fromJSON(ChessFrameworkClient.execute(request),Response.class);
String uniqueID=(String)response.getResult();
ChessFrameworkClient client=new ChessFrameworkClient(uniqueID,2,userName);
dispose();
t1.stop();
t2.stop();
t3.stop();
}catch(Throwable t)
{
System.out.println(t);
}
}
else if(selectedOption==JOptionPane.NO_OPTION)
{
AcceptedInvitation acceptedInvitation=new AcceptedInvitation();
acceptedInvitation.whoHadInvited=name;
acceptedInvitation.whoAccepted=userName;
try
{
request=new Request();
request.setAction(UserAction.INVITATION_REJECTED);
request.setArguments(acceptedInvitation);
ChessFrameworkClient.execute(request);
}catch(Throwable t)
{
System.out.println(t);
}
}
}
}catch(Throwable t)
{
System.out.println(t);
}
}
});
t2.start();
}
}