package com.thinking.machines.chessFramework.server;
import com.thinking.machines.chessFramework.common.*;
import java.util.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
class Game
{
public String user1;
public String user2;
public boolean player1;
public boolean player2;
public Set<ChessPiecesInfo> ds;
}
public class DataModel
{
public static Map<String,String> user_list;
public static Set<String> logged_in_users;
public static Set<String> playing_users;
public static Map<String,Game> games;
public static Map<String,String> invitations;
public static Map<String,String> acceptedInvitations;
public static Map<String,String> rejectedInvitations;
public static Map<String,String> uniqueIDs;
private void initialize()
{
user_list=new HashMap<>();
logged_in_users=new HashSet<>();
playing_users=new HashSet<>();
games=new HashMap<>();
invitations=new HashMap<>();
acceptedInvitations=new HashMap<>();
rejectedInvitations=new HashMap<>();
uniqueIDs=new HashMap<>();
}
private void collectInfoFromDatabase()
{
Set<DesignationDTOInterface> list;
String name;
String password;
try
{
DesignationDAOInterface dao=new DesignationDAO();
list=dao.getAll();
for(DesignationDTOInterface d:list)
{
name=d.getName();
password=d.getPassword();
user_list.put(name,password);
}
}catch(DAOException daoException)
{
System.out.println(daoException);
}catch(Throwable t)
{
System.out.println(t);
}
}
public DataModel()
{
initialize();
collectInfoFromDatabase();
}
public boolean is_logged_in_user(NewUserLogin user)
{
String name=user.getName();
String password=user.getPassword();
if(logged_in_users.contains(name))return false;
if(user_list.containsKey(name))
{
String pass=user_list.get(name);
if(pass.equals(password))return true;
}
return false;
}
public void add_logged_in_user(NewUserLogin user)
{
logged_in_users.add(user.getName());
}
public void remove_logged_in_user(String user)
{
logged_in_users.remove(user);
}
public Set<String> get_all_logged_in_user()
{
return logged_in_users;
}
public boolean is_playing_user(String user)
{
return playing_users.contains(user);
}
public void add_playing_user(String user)
{
playing_users.add(user);
}
public void remove_playing_user(String user)
{
playing_users.remove(user);
}
public void add_game(String id,Game game)
{
games.put(id,game);
}
public void remove_game(String id)
{
games.remove(id);
}
public Game get_game(String id)
{
return games.get(id);
}
public boolean has_invitation(String user)
{
return invitations.containsKey(user);
}
public void add_invitation(Invitation invitation)
{
invitations.put(invitation.whoGotInvited,invitation.whoInvited);
}
public String get_invitation(String user)
{
return invitations.get(user);
}
public void remove_invitation(String user)
{
invitations.remove(user);
}
public boolean has_acceptedInvitation(AcceptedInvitation acceptedInvitation)
{
if(!invitations.containsKey(acceptedInvitation.whoAccepted))return false;
if(acceptedInvitations.containsKey(acceptedInvitation.whoHadInvited))
{
String value=acceptedInvitations.get(acceptedInvitation.whoHadInvited);
if(value.equals(acceptedInvitation.whoAccepted))return true;
}
return false;
}
public void add_acceptedInvitation(AcceptedInvitation acceptedInvitation)
{
acceptedInvitations.put(acceptedInvitation.whoHadInvited,acceptedInvitation.whoAccepted);
}
public void remove_acceptedInvitation(String user)
{
acceptedInvitations.remove(user);
}

public boolean has_rejectedInvitation(AcceptedInvitation acceptedInvitation)
{
if(!invitations.containsKey(acceptedInvitation.whoAccepted))return false;
if(rejectedInvitations.containsKey(acceptedInvitation.whoHadInvited))
{
String value=rejectedInvitations.get(acceptedInvitation.whoHadInvited);
if(value.equals(acceptedInvitation.whoAccepted))return true;
}
return false;
}
public void add_rejectedInvitation(AcceptedInvitation acceptedInvitation)
{
rejectedInvitations.put(acceptedInvitation.whoHadInvited,acceptedInvitation.whoAccepted);
}
public void remove_rejectedInvitation(String user)
{
rejectedInvitations.remove(user);
}
public void add_uniqueID(String userName,String uniqueId)
{
uniqueIDs.put(userName,uniqueId);
}
public String get_uniqueID(String userName)
{
return uniqueIDs.get(userName);
}
}