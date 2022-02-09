package com.thinking.machines.chessFramework.server;
import com.thinking.machines.chessFramework.common.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
public class ServerApplication
{
private ChessFrameworkServer server;
private boolean flag;
public ServerApplication(ChessFrameworkServer server)
{
this.server=server;
}
public Response process(Set<ChessPiecesInfo> recordList)
{
Response response=new Response();
if(flag==true&&recordList.size()==0)
{
response.setSuccess(true);
response.setResult(this.server.getRecordList());
response.setException(null);
flag=false;
}
else if(flag==false&&recordList.size()==0)
{
response.setSuccess(true);
response.setResult(null);
response.setException(null);
}
else
{
this.server.setRecordList(recordList);
flag=true;
response.setSuccess(true);
response.setResult(null);
response.setException(null);
}
return response;
}
}