import com.thinking.machines.chessFramework.common.*;
import com.thinking.machines.chessFramework.client.*;
public class ChessUI
{
public static void main(String gg[])
{
int x=Integer.parseInt(gg[0]);
try
{
ChessFrameworkClient client=new ChessFrameworkClient(x);
}catch(Throwable t)
{
System.out.println(t);
}
}
}