import com.thinking.machines.chessFramework.common.*;
import com.thinking.machines.chessFramework.server.*;
import java.awt.*;
import javax.swing.*;
class Chess
{
public static void main(String gg[])
{
try
{
Chess chess=new Chess();
ChessFrameworkServer server=new ChessFrameworkServer();
server.start();
}catch(Exception e)
{
System.out.println(e);
}
}
}