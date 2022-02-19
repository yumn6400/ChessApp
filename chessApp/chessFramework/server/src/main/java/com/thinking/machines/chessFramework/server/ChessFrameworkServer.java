package com.thinking.machines.chessFramework.server;
import com.thinking.machines.chessFramework.common.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.charset.*;
import java.lang.reflect.*;
public class ChessFrameworkServer
{
private ServerSocket serverSocket;
private ServerApplication application;
private Set<ChessPiecesInfo> recordList;
private DataModel model;
public ChessFrameworkServer()
{
model=new DataModel();
recordList=new HashSet<>();
application=new ServerApplication(this,model);
}
public void setRecordList(Set<ChessPiecesInfo> recordList)
{
this.recordList=recordList;
}
public Set<ChessPiecesInfo> getRecordList()
{
return this.recordList;
}
public void start()
{
try
{
serverSocket=new ServerSocket(5500);
Socket socket;
RequestProcessor requestProcessor;
while(true)
{
socket=serverSocket.accept();
requestProcessor=new RequestProcessor(this,socket,application);
}
}catch(Exception e)
{
System.out.println(e);
}
}
}