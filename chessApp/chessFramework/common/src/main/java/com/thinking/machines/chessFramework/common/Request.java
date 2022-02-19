package com.thinking.machines.chessFramework.common;
import java.util.*;
public class Request implements java.io.Serializable
{
private UserAction action;
private Object arguments;
public void setAction(UserAction action)
{
this.action=action;
}
public UserAction getAction()
{
return this.action;
}
public void setArguments(Object arguments)
{
this.arguments=arguments;
}
public Object getArguments()
{
return this.arguments;
}
}