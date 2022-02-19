package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
import java.sql.*;
import java.util.*;
public class DesignationDAO implements DesignationDAOInterface
{
public DesignationDTOInterface getByUserName(String name)throws DAOException
{
DesignationDTO designationDTO=null;
try
{
Class.forName("com.mysql.cj.jdbc.Driver");
Connection c;
c=DriverManager.getConnection("jdbc:mysql://localhost:3306/chessdb","Pradhyumn","Pradhyumn");
Statement s;
s=c.createStatement();
ResultSet r;
r=s.executeQuery("select * from Player where name='"+name+"';");
if(r.next())
{
String n=r.getString("name");
String password=r.getString("password");
designationDTO=new DesignationDTO();
designationDTO.setName(n);
designationDTO.setPassword(password);
}
r.close();
s.close();
c.close();
}catch(Exception e)
{
throw new DAOException(e.getMessage());
}
return designationDTO;
}
public Set<DesignationDTOInterface> getAll() throws DAOException
{
Set<DesignationDTOInterface> list=new HashSet<>();
DesignationDTOInterface designationDTO=null;
try
{
Class.forName("com.mysql.cj.jdbc.Driver");
Connection c;
c=DriverManager.getConnection("jdbc:mysql://localhost:3306/chessdb","Pradhyumn","Pradhyumn");
Statement s;
s=c.createStatement();
ResultSet r;
r=s.executeQuery("select * from Player;");
while(r.next())
{
String name=r.getString("name");
String password=r.getString("password");
designationDTO=new DesignationDTO();
designationDTO.setName(name);
designationDTO.setPassword(password);
list.add(designationDTO);
}
r.close();
s.close();
c.close();
}catch(Exception e)
{
throw new DAOException(e.getMessage());
}
return list;
}
}