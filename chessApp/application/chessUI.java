import com.thinking.machines.chessFramework.common.*;
import com.thinking.machines.chessFramework.client.*;
public class ChessUI
{
public static void main(String gg[])
{
String name=gg[0];
String password=gg[1];
UserConnection client=new UserConnection(name,password);
}
}