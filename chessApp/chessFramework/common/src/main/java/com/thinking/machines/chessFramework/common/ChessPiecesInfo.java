package com.thinking.machines.chessFramework.common;
import java.awt.*;
public class ChessPiecesInfo
{
private Point pieces_position;
private Pieces_color pieces_color;
private Pieces_type pieces_type;
public void setPieces_position(Point pieces_position)
{
this.pieces_position=pieces_position;
}
public Point getPieces_position()
{
return this.pieces_position;
}
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
}