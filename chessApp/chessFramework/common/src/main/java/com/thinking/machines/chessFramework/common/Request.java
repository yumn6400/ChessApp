package com.thinking.machines.chessFramework.common;
import java.util.*;
public class Request implements java.io.Serializable
{
private Object arguments;
public void setArguments(Object arguments)
{
this.arguments=arguments;
}
public Object getArguments()
{
return this.arguments;
}
}