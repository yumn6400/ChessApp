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
private String uniqueId;
private String userName;
private int userID;
public ChessFrameworkClient(String uniqueId,int userID,String userName)
{
this.uniqueId=uniqueId;
this.userName=userName;
this.userID=userID;
application=new ClientApplication(this,uniqueId,userID,userName);
}
public static String execute(Request request)throws Throwable
{
try
{
String requestJsonString=JSONUtil.toJSON(request);
System.out.println(requestJsonString);
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
String responseJsonString=new String(response,StandardCharsets.UTF_8);
System.out.println(responseJsonString);	
return responseJsonString;
}catch(Exception e)
{
System.out.println(e);
}
return null;
}
}