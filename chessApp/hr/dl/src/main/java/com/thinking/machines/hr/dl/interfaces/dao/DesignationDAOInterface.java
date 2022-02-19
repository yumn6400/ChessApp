package com.thinking.machines.hr.dl.interfaces.dao;
import java.util.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import java.util.*;
public interface DesignationDAOInterface
{
public DesignationDTOInterface getByUserName(String name)throws DAOException;
public Set<DesignationDTOInterface> getAll()throws DAOException;
}