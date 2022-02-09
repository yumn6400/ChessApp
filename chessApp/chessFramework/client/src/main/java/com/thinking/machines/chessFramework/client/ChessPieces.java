package com.thinking.machines.chessFramework.client;
import com.thinking.machines.chessFramework.common.*;
import java.awt.*;
import java.util.*;
public interface ChessPieces
{
public void setPieces_color(Pieces_color pieces_color);
public Pieces_color getPieces_color();
public void setPieces_type(Pieces_type pieces_type);
public Pieces_type getPieces_type();
public boolean Process(Point initial_pos,Point final_pos,Map<Point,ChessPieces> pieces_identity,boolean enemy);
}
class Pawn_chessPieces implements ChessPieces
{
private Pieces_color pieces_color;
private Pieces_type pieces_type;
public void setPieces_color(Pieces_color pieces_color)
{
this.pieces_color=pieces_color;
}
public Pieces_color getPieces_color()
{
return this.pieces_color;
}
public void setPieces_type(Pieces_type pieces_type)
{
this.pieces_type=pieces_type;
}
public Pieces_type getPieces_type()
{
return this.pieces_type;
}
public boolean Process(Point initial_pos,Point final_pos,Map<Point,ChessPieces> pieces_identity,boolean enemy)
{
if(this.pieces_color==Pieces_color.WHITE)
{
if(initial_pos.x==1)
{
if((final_pos.x!=2)&&(final_pos.x!=3))return false;
if(final_pos.x==3)if(initial_pos.y!=final_pos.y)return false;
}
else
{
if(final_pos.x!=(initial_pos.x+1))return false;
}
if(final_pos.x==(initial_pos.x+1))
{
if(enemy)
{
if((final_pos.y!=(initial_pos.y-1))&&(final_pos.y!=(initial_pos.y+1)))return false;
}
else
{
if(final_pos.y!=(initial_pos.y))return false;
}
}
}
else if(this.pieces_color==Pieces_color.BLACK)
{
if(initial_pos.x==6)
{
if((final_pos.x!=5)&&(final_pos.x!=4))return false;
if(final_pos.x==4)if(initial_pos.y!=final_pos.y)return false;
}
else
{
if(final_pos.x!=(initial_pos.x-1))return false;
}
if(final_pos.x==(initial_pos.x-1))
{
if(enemy)
{
if((final_pos.y!=(initial_pos.y-1))&&(final_pos.y!=(initial_pos.y+1)))return false;
}
else
{
if(final_pos.y!=(initial_pos.y))return false;
}
}
}
return true;
}
}
class Rook_chessPieces implements ChessPieces
{
private Pieces_color pieces_color;
private Pieces_type pieces_type;
public void setPieces_color(Pieces_color pieces_color)
{
this.pieces_color=pieces_color;
}
public Pieces_color getPieces_color()
{
return this.pieces_color;
}
public void setPieces_type(Pieces_type pieces_type)
{
this.pieces_type=pieces_type;
}
public Pieces_type getPieces_type()
{
return this.pieces_type;
}
public boolean Process(Point initial_pos,Point final_pos,Map<Point,ChessPieces> pieces_identity,boolean enemy)
{
if((final_pos.x!=initial_pos.x)&&(final_pos.y!=initial_pos.y))return false;
Point p;
int x;
if(initial_pos.x==final_pos.x)
{
if(initial_pos.y<final_pos.y)
{
x=initial_pos.y;
x++;
while(x<final_pos.y)
{
p=new Point(initial_pos.x,x);
if(pieces_identity.containsKey(p))return false;
x++;
}
}
else
{
x=final_pos.y;
x++;
while(x<initial_pos.y)
{
p=new Point(initial_pos.x,x);
if(pieces_identity.containsKey(p))return false;
x++;
}
}
}
else if(initial_pos.y==final_pos.y)
{
if(initial_pos.x<final_pos.x)
{
x=initial_pos.x;
x++;
while(x<final_pos.x)
{
p=new Point(x,final_pos.y);
if(pieces_identity.containsKey(p))return false;
x++;
}
}
else
{
x=final_pos.x;
x++;
while(x<initial_pos.x)
{
p=new Point(x,initial_pos.y);
if(pieces_identity.containsKey(p))return false;
x++;
}
}
}
return true;
}
}
class Knight_chessPieces implements ChessPieces
{
private Pieces_color pieces_color;
private Pieces_type pieces_type;
public void setPieces_color(Pieces_color pieces_color)
{
this.pieces_color=pieces_color;
}
public Pieces_color getPieces_color()
{
return this.pieces_color;
}
public void setPieces_type(Pieces_type pieces_type)
{
this.pieces_type=pieces_type;
}
public Pieces_type getPieces_type()
{
return this.pieces_type;
}
public boolean Process(Point initial_pos,Point final_pos,Map<Point,ChessPieces> pieces_identity,boolean enemy)
{
if(final_pos.x!=(initial_pos.x-2)&&final_pos.x!=(initial_pos.x-1)&&final_pos.x!=(initial_pos.x+1)&&final_pos.x!=(initial_pos.x+2))
{
return false;
}
if((final_pos.x==(initial_pos.x-1))||(final_pos.x==(initial_pos.x+1)))
{
if((final_pos.y!=(initial_pos.y-2))&&(final_pos.y!=(initial_pos.y+2)))return false;
}
else if((final_pos.x==(initial_pos.x-2))||(final_pos.x==(initial_pos.x+2)))
{
if((final_pos.y!=(initial_pos.y-1))&&(final_pos.y!=(initial_pos.y+1)))return false;
}
return true;
}
}
class Bishop_chessPieces implements ChessPieces
{
private Pieces_color pieces_color;
private Pieces_type pieces_type;
public void setPieces_color(Pieces_color pieces_color)
{
this.pieces_color=pieces_color;
}
public Pieces_color getPieces_color()
{
return this.pieces_color;
}
public void setPieces_type(Pieces_type pieces_type)
{
this.pieces_type=pieces_type;
}
public Pieces_type getPieces_type()
{
return this.pieces_type;
}
public boolean Process(Point initial_pos,Point final_pos,Map<Point,ChessPieces> pieces_identity,boolean enemy)
{
if((initial_pos.x==final_pos.x)||(initial_pos.y==final_pos.y))return false;
int x=final_pos.x-initial_pos.x;
int y=final_pos.y-initial_pos.y;
if(x<0)x=-x;
if(y<0)y=-y;
if(x!=y)return false;
x=0;
y=0;
Point p;
if(final_pos.x>initial_pos.x)
{
if(final_pos.y>initial_pos.y)
{
x=initial_pos.y;
x++;
y++;
while(x<final_pos.y)
{
p=new Point(initial_pos.x+y,initial_pos.y+y);
if(pieces_identity.containsKey(p))return false;
x++;
y++;
}
}
else
{
x=final_pos.y;
x++;
y++;
while(x<initial_pos.y)
{
p=new Point(initial_pos.x+y,initial_pos.y-y);
if(pieces_identity.containsKey(p))return false;
x++;
y++;
}
}
}
else
{
if(final_pos.y>initial_pos.y)
{
x=initial_pos.y;
x++;
y++;
while(x<final_pos.y)
{
p=new Point(initial_pos.x-y,initial_pos.y+y);
if(pieces_identity.containsKey(p))return false;
x++;
y++;
}
}
else
{
x=final_pos.y;
x++;
y++;
while(x<initial_pos.y)
{
p=new Point(initial_pos.x-y,initial_pos.y-y);
if(pieces_identity.containsKey(p))return false;
x++;
y++;
}
}
}
return true;
}
}
class King_chessPieces implements ChessPieces
{
private Pieces_color pieces_color;
private Pieces_type pieces_type;
public void setPieces_color(Pieces_color pieces_color)
{
this.pieces_color=pieces_color;
}
public Pieces_color getPieces_color()
{
return this.pieces_color;
}
public void setPieces_type(Pieces_type pieces_type)
{
this.pieces_type=pieces_type;
}
public Pieces_type getPieces_type()
{
return this.pieces_type;
}
public boolean Process(Point initial_pos,Point final_pos,Map<Point,ChessPieces> pieces_identity,boolean enemy)
{
int x=final_pos.x-initial_pos.x;
int y=final_pos.y-initial_pos.y;
if((x!=-1)&&(x!=0)&&(x!=1))return false;
if((y!=-1)&&(y!=0)&&(y!=1))return false;
return true;
}
}
class Queen_chessPieces implements ChessPieces
{
private Pieces_color pieces_color;
private Pieces_type pieces_type;
public void setPieces_color(Pieces_color pieces_color)
{
this.pieces_color=pieces_color;
}
public Pieces_color getPieces_color()
{
return this.pieces_color;
}
public void setPieces_type(Pieces_type pieces_type)
{
this.pieces_type=pieces_type;
}
public Pieces_type getPieces_type()
{
return this.pieces_type;
}
public boolean Process(Point initial_pos,Point final_pos,Map<Point,ChessPieces> pieces_identity,boolean enemy)
{
Point p;
int x=0;
int y=0;
if((initial_pos.x==final_pos.x)||(initial_pos.y==final_pos.y))
{
if(initial_pos.x==final_pos.x)
{
if(initial_pos.y<final_pos.y)
{
x=initial_pos.y;
x++;
while(x<final_pos.y)
{
p=new Point(initial_pos.x,x);
if(pieces_identity.containsKey(p))return false;
x++;
}
}
else
{
x=final_pos.y;
x++;
while(x<initial_pos.y)
{
p=new Point(initial_pos.x,x);
if(pieces_identity.containsKey(p))return false;
x++;
}
}
}
else if(initial_pos.y==final_pos.y)
{
if(initial_pos.x<final_pos.x)
{
x=initial_pos.x;
x++;
while(x<final_pos.x)
{
p=new Point(x,final_pos.y);
if(pieces_identity.containsKey(p))return false;
x++;
}
}
else
{
x=final_pos.x;
x++;
while(x<initial_pos.x)
{
p=new Point(x,initial_pos.y);
if(pieces_identity.containsKey(p))return false;
x++;
}
}
}
}
else
{
x=final_pos.x-initial_pos.x;
y=final_pos.y-initial_pos.y;
if(x<0)x=-x;
if(y<0)y=-y;
if(x!=y)return false;
x=0;
y=0;
if(final_pos.x>initial_pos.x)
{
if(final_pos.y>initial_pos.y)
{
x=initial_pos.y;
x++;
y++;
while(x<final_pos.y)
{
p=new Point(initial_pos.x+y,initial_pos.y+y);
if(pieces_identity.containsKey(p))return false;
x++;
y++;
}
}
else
{
x=final_pos.y;
x++;
y++;
while(x<initial_pos.y)
{
p=new Point(initial_pos.x+y,initial_pos.y-y);
if(pieces_identity.containsKey(p))return false;
x++;
y++;
}
}
}
else
{
if(final_pos.y>initial_pos.y)
{
x=initial_pos.y;
x++;
y++;
while(x<final_pos.y)
{
p=new Point(initial_pos.x-y,initial_pos.y+y);
if(pieces_identity.containsKey(p))return false;
x++;
y++;
}
}
else
{
x=final_pos.y;
x++;
y++;
while(x<initial_pos.y)
{
p=new Point(initial_pos.x-y,initial_pos.y-y);
if(pieces_identity.containsKey(p))return false;
x++;
y++;
}
}
}
}
return true;
}
}