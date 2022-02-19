package com.thinking.machines.chessFramework.server;
import com.thinking.machines.chessFramework.common.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
import java.net.*;
import java.nio.charset.*;
import java.lang.reflect.*;
import java.io.*;
public class ServerApplication
{
private ChessFrameworkServer server;
private boolean flag;
private DataModel model;
private javax.swing.Timer timer;
public ServerApplication(ChessFrameworkServer server,DataModel model)
{
this.server=server;
this.model=model;
}
public Response process(String requestJsonString)
{
Response response=new Response();
Set<ChessPiecesInfo> projectList=new HashSet<>();
JsonParser parser = new JsonParser();
JsonObject rootObject = parser.parse(requestJsonString).getAsJsonObject();
JsonElement action=rootObject.get("action");
if(action.getAsString().equals("NEW_USER_LOGIN"))
{
JsonElement arguments=rootObject.get("arguments");
Gson gson = new Gson();
NewUserLogin user=gson.fromJson(arguments,NewUserLogin.class);
if(model.is_logged_in_user(user))
{
model.add_logged_in_user(user);
response.setSuccess(true);
response.setResult(null);
response.setException(null);
}
else
{
response.setSuccess(false);
response.setResult(null);
response.setException(null);
}
}
else if(action.getAsString().equals("LIST_OF_ONLINE_USERS"))
{
response.setSuccess(true);
response.setResult(model.get_all_logged_in_user());
response.setException(null);
}
else if(action.getAsString().equals("HAVE_ANY_INVITATION"))
{
JsonElement arguments=rootObject.get("arguments");
if(model.has_invitation(arguments.getAsString()))
{
response.setSuccess(true);
response.setResult(model.get_invitation(arguments.getAsString()));
response.setException(null);
}
else
{
response.setSuccess(false);
response.setResult(null);
response.setException(null);
}
}
else if(action.getAsString().equals("INVITE_USER"))
{
JsonElement arguments=rootObject.get("arguments");
Gson gson = new Gson();
Invitation invitation=gson.fromJson(arguments,Invitation.class);
model.add_invitation(invitation);
response.setSuccess(true);
response.setResult(null);
response.setException(null);
}
else if(action.getAsString().equals("INVITATION_ACCEPTED"))
{
JsonElement arguments=rootObject.get("arguments");
Gson gson = new Gson();
AcceptedInvitation acceptedInvitation=gson.fromJson(arguments,AcceptedInvitation.class);
model.add_acceptedInvitation(acceptedInvitation);

String uniqueID=UUID.randomUUID().toString();
Game game=new Game();
game.user1=acceptedInvitation.whoHadInvited;
game.user2=acceptedInvitation.whoAccepted;
game.player1=true;
game.player2=false;
game.ds=new HashSet<>();
model.add_game(uniqueID,game);
model.add_uniqueID(acceptedInvitation.whoHadInvited,uniqueID);
model.add_uniqueID(acceptedInvitation.whoAccepted,uniqueID);
response.setSuccess(true);
response.setResult(uniqueID);
response.setException(null);
}
else if(action.getAsString().equals("INVITATION_REJECTED"))
{
JsonElement arguments=rootObject.get("arguments");
Gson gson = new Gson();
AcceptedInvitation acceptedInvitation=gson.fromJson(arguments,AcceptedInvitation.class);
model.add_rejectedInvitation(acceptedInvitation);
response.setSuccess(true);
response.setResult(null);
response.setException(null);
}
else if(action.getAsString().equals("AS_INVITATION_ACCEPTED_OR_NOT"))
{
JsonElement arguments=rootObject.get("arguments");
Gson gson = new Gson();
AcceptedInvitation acceptedInvitation=gson.fromJson(arguments,AcceptedInvitation.class);
if(model.has_acceptedInvitation(acceptedInvitation))
{
String uniqueID=model.get_uniqueID(acceptedInvitation.whoAccepted);
response.setSuccess(true);
response.setResult(uniqueID);
response.setException(null);
model.remove_logged_in_user(acceptedInvitation.whoHadInvited);
model.remove_logged_in_user(acceptedInvitation.whoAccepted);
model.add_playing_user(acceptedInvitation.whoHadInvited);
model.add_playing_user(acceptedInvitation.whoAccepted);
model.remove_invitation(acceptedInvitation.whoAccepted);
model.remove_acceptedInvitation(acceptedInvitation.whoHadInvited);
}
else if(model.has_rejectedInvitation(acceptedInvitation))
{
response.setSuccess(true);
response.setResult("No");
response.setException(null);
model.remove_invitation(acceptedInvitation.whoAccepted);
model.remove_rejectedInvitation(acceptedInvitation.whoHadInvited);
}
else
{
response.setSuccess(false);
response.setResult(null);
response.setException(null);
}
}
else if(action.getAsString().equals("TERMINATE_GAME"))
{
JsonElement arguments=rootObject.get("arguments");
String userID=arguments.getAsString();
model.remove_game(userID);
}
else if(action.getAsString().equals("PLAY_GAME"))
{
JsonElement projectElement = rootObject.get("arguments");
Gson gson = new Gson();
GenerateRequestObject obj = gson.fromJson(projectElement, GenerateRequestObject.class);
String id=obj.uniqueID;
String user=obj.userName;
Set<ChessPiecesInfo> s=obj.info;
Game game=model.get_game(id);
if(game==null)
{
response.setSuccess(true);
response.setResult(null);
response.setException(null);
return response;
}
String user1=null;
String  user2=null;
boolean player1=false;
boolean player2=false;
Set<ChessPiecesInfo> ds=null;
if(game!=null)
{
user1=game.user1;
user2=game.user2;
player1=game.player1;
player2=game.player2;
ds=game.ds;
if(user.equalsIgnoreCase(user1))
{
if(player1==false)
{
response.setSuccess(false);
response.setResult(null);
response.setException(null);
return response;
}
}
else if(user.equalsIgnoreCase(user2))
{
if(player2==false)
{
response.setSuccess(false);
response.setResult(null);
response.setException(null);
return response;
}
}
}
JsonObject infoo=rootObject.getAsJsonObject("arguments");
JsonElement jsonInfo = infoo.get("info");
if(jsonInfo!=null)
{
if (jsonInfo.isJsonObject())
{
ChessPiecesInfo info = gson.fromJson(jsonInfo, ChessPiecesInfo.class);
projectList.add(info);
}
else if (jsonInfo.isJsonArray())
{
Type projectListType = new TypeToken<Set<ChessPiecesInfo>>() {}.getType();
projectList = gson.fromJson(jsonInfo, projectListType);
}
}
if(projectList.size()==0)
{
model.add_game(id,game);
response.setSuccess(true);
response.setResult(game.ds);
response.setException(null);
}
else
{
model.remove_game(id);
game=new Game();
game.user1=user1;
game.user2=user2;
game.player1=(!player1);
game.player2=(!player2);
game.ds=projectList;
model.add_game(id,game);
response.setSuccess(true);
response.setResult(null);
response.setException(null);
}
}
return response;
}
}