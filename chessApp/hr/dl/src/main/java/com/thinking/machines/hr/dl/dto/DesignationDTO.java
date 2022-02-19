package com.thinking.machines.hr.dl.dto;
import com.thinking.machines.hr.dl.interfaces.dto.*;
public class DesignationDTO implements DesignationDTOInterface
{
private String name;
private String password;
public DesignationDTO()
{
this.name="";
this.password="";
}
public void setName(String name)
{
this.name=name;
}
public String getName()
{
return this.name;
}
public void setPassword(String password)
{
this.password=password;
}
public String getPassword()
{
return this.password;
}
}