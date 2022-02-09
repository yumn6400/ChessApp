package com.thinking.machines.chessFramework.server;
import java.util.*;
import java.net.*;
import com.thinking.machines.chessFramework.common.*;
import java.nio.charset.*;
import java.lang.reflect.*;
import java.io.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
public class RequestProcessor
{
private Socket socket;
private ChessFrameworkServer server;
private ServerApplication application;
public RequestProcessor(ChessFrameworkServer server,Socket socket,ServerApplication application)
{
this.server=server;
this.socket=socket;
this.application=application;
run();
}
public void run()
{
try
{
InputStream is=socket.getInputStream();
OutputStream os=socket.getOutputStream();
int bytesToReceive=1024;
byte tmp[]=new byte[1024];
byte header[]=new byte[1024];
int bytesReadCount;
int i,j,k;
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
int requestLength=0;
i=1;
j=1023;
while(j>=0)
{
requestLength=requestLength+(header[j]*i);
i=i*10;
j--;
}
System.out.println("Request length: "+requestLength);
byte ack[]=new byte[1];
ack[0]=1;
os.write(ack,0,1);
os.flush();
byte request[]=new byte[requestLength];
bytesToReceive=requestLength;
i=0;
j=0;
while(j<bytesToReceive)
{
bytesReadCount=is.read(tmp);
if(bytesReadCount==-1)continue;
for(k=0;k<bytesReadCount;k++)
{
request[i]=tmp[k];
i++;
}
j=j+bytesReadCount;
}
Set<ChessPiecesInfo> projectList = new HashSet<>();
String requestJsonString=new String(request,StandardCharsets.UTF_8);
System.out.println(requestJsonString);
JsonParser parser = new JsonParser();
JsonObject rootObject = parser.parse(requestJsonString).getAsJsonObject();
JsonElement projectElement = rootObject.get("arguments");
if(projectElement!=null)
{
Gson gson = new Gson();
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
}
Response responseObject=application.process(projectList);
String responseJSONString=JSONUtil.toJSON(responseObject);
byte objectBytes[]=responseJSONString.getBytes(StandardCharsets.UTF_8);
int responseLength=objectBytes.length;
int x;
i=1023;
x=responseLength;
header=new byte[1024];
while(x>0)
{
header[i]=(byte)(x%10);
x=x/10;
i--;
}
os.write(header,0,1024);
os.flush();
System.out.println("Response  Header sent: "+responseLength);
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1)continue;
break;
}
System.out.println("Acknowledgement received");
int bytesToSend=responseLength;
int chunkSize=1024;
j=0;
while(j<bytesToSend)
{
if((bytesToSend-j)<chunkSize)chunkSize=bytesToSend-j;
os.write(objectBytes,j,chunkSize);
os.flush();
j=j+chunkSize;
}
System.out.println("Response send");
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1)continue;
break;
}
System.out.println("Acknowledge received");
socket.close();
System.out.println("*****************************");
}catch(Exception e)
{
System.out.println(e);
}
}
}