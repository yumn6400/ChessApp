package com.thinking.machines.chessFramework.client;
import com.thinking.machines.chessFramework.common.*;
import java.net.*;
import java.nio.charset.*;
import java.io.*;
import java.util.*;
import com.google.gson.reflect.*;
import com.google.gson.*;
import java.text.*;
import java.lang.reflect.*;
public class ChessFrameworkClient
{
private ClientApplication application;
private int user_id;
public ChessFrameworkClient(int user_id)
{
this.user_id=user_id;
application=new ClientApplication(this,user_id);
}
public Set<ChessPiecesInfo> execute(Object arguments)throws Throwable
{
try
{
Request request=new Request();
request.setArguments(arguments);
String requestJsonString=JSONUtil.toJSON(request);
byte objectBytes[]=requestJsonString.getBytes(StandardCharsets.UTF_8);
int requestLength=objectBytes.length;
byte header[]=new byte[1024];
int x,i;
i=1023;
x=requestLength;
while(x>0)
{
header[i]=(byte)(x%10);
x=x/10;
i--;
}
Socket socket=new Socket("localhost",5500);
OutputStream os=socket.getOutputStream();
os.write(header,0,1024);
os.flush();
System.out.println("Header sent");
InputStream is=socket.getInputStream();
byte ack[]=new byte[1];
System.out.println("Acknowledgement sent");
int bytesReadCount;
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1)continue;
break;
}
int bytesToSend=requestLength;
int chunkSize=1024;
int j=0;
while(j<bytesToSend)
{
if((bytesToSend-j)<chunkSize)chunkSize=bytesToSend-j;
os.write(objectBytes,j,chunkSize);
os.flush();
j=j+chunkSize;
}
int bytesToReceive=1024;
byte tmp[]=new byte[1024];
int k;
i=0;
j=0;
while(j<bytesToReceive)
{
bytesReadCount=is.read(tmp);
if(bytesReadCount==-1)continue;
for(k=0;k<bytesReadCount;k++)
{
header[i]=tmp[k];
i++;
}
j=j+bytesReadCount;
}
int responseLength=0;
i=1;
j=1023;
while(j>=0)
{
responseLength=responseLength+(header[j]*i);
i=i*10;
j--;
}
System.out.println("Header received: "+responseLength);
ack[0]=1;
os.write(ack,0,1);
os.flush();
System.out.println("Acknowledgement sent");
byte response[]=new byte[responseLength];
bytesToReceive=responseLength;
i=0;
j=0;
while(j<bytesToReceive)
{
bytesReadCount=is.read(tmp);
if(bytesReadCount==-1)continue;
for(k=0;k<bytesReadCount;k++)
{
response[i]=tmp[k];
i++;
}
j=j+bytesReadCount;
}
System.out.println("Response received");
ack[0]=1;
os.write(ack);
os.flush();
socket.close();
/*
String responseJsonString=new String(response,StandardCharsets.UTF_8);
Response responseObject=JSONUtil.fromJSON(responseJsonString,Response.class);
if(responseObject.getSuccess())
{
return responseObject.getResult();
}
else
{
//throw responseObject.getException();
}*/

String responseJsonString=new String(response,StandardCharsets.UTF_8);
JsonParser parser = new JsonParser();
JsonObject rootObject = parser.parse(responseJsonString).getAsJsonObject();
JsonElement projectElement = rootObject.get("result");

Gson gson = new Gson();
Set<ChessPiecesInfo> projectList = new HashSet<>();
if (projectElement.isJsonObject())
{
ChessPiecesInfo info = gson.fromJson(projectElement, ChessPiecesInfo.class);
projectList.add(info);
}
else if (projectElement.isJsonArray())
{
Type projectListType = new TypeToken<Set<ChessPiecesInfo>>() {}.getType();
projectList = gson.fromJson(projectElement, projectListType);
}
return projectList;
}catch(Exception e)
{
System.out.println(e);
}
return null;
}
}