package cn.ricoco.bridgingpractise.Command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.ricoco.bridgingpractise.Utils.PlayerUtils;
import cn.ricoco.bridgingpractise.variable;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RunCommand extends Command {
    public RunCommand(String name, String description) {
        super(name, description);
    }
    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if(!sender.isPlayer()){sender.sendMessage(variable.langjson.getString("notplayer"));return false;}
        if(args.length!=1){sender.sendMessage(variable.langjson.getString("usage"));return false;}
        Player p= Server.getInstance().getPlayer(sender.getName());
        String levelName=p.getPosition().getLevel().getName(),pname=p.getName();
        switch (args[0]){
            case "join":
                if(levelName!=variable.configjson.getJSONObject("pos").getJSONObject("pra").getString("l")){
                    Map<Integer,Position> m=new HashMap<>();
                    variable.blockpos.put(pname,m);
                    variable.blocklength.put(pname,0);
                    variable.playerinv.put(pname,p.getInventory());
                    variable.playerhunger.put(pname,p.getFoodData().getLevel());
                    JSONObject j=variable.configjson.getJSONObject("block").getJSONObject("pra");
                    PlayerUtils.addItemToPlayer(p,Item.get(j.getInteger("id"),j.getInteger("d"),j.getInteger("c")));
                    Position pos=Position.fromObject(new Vector3(variable.configjson.getJSONObject("pos").getJSONObject("pra").getDouble("x"),variable.configjson.getJSONObject("pos").getJSONObject("pra").getDouble("y"),variable.configjson.getJSONObject("pos").getJSONObject("pra").getDouble("z")),Server.getInstance().getLevelByName(variable.configjson.getJSONObject("pos").getJSONObject("pra").getString("l")));
                    p.teleport(pos);
                    variable.playerresp.put(pname,pos);
                    sender.sendMessage(variable.langjson.getString("joinedarena"));
                }else{
                    sender.sendMessage(variable.langjson.getString("stillinarena"));
                }
                break;
            case "leave":
                if(levelName==variable.configjson.getJSONObject("pos").getJSONObject("pra").getString("l")){
                    //insta remove blocks
                    p.getInventory().setContents(variable.playerinv.remove(pname).getContents());
                    variable.playerresp.remove(pname);
                    p.getFoodData().setLevel(variable.playerhunger.remove(pname));
                    p.teleport(Position.fromObject(new Vector3(variable.configjson.getJSONObject("pos").getJSONObject("exit").getDouble("x"),variable.configjson.getJSONObject("pos").getJSONObject("exit").getDouble("y"),variable.configjson.getJSONObject("pos").getJSONObject("exit").getDouble("z")),Server.getInstance().getLevelByName(variable.configjson.getJSONObject("pos").getJSONObject("exit").getString("l"))));
                    sender.sendMessage(variable.langjson.getString("leavearena"));
                }else{
                    sender.sendMessage(variable.langjson.getString("notinarena"));
                }
                break;
            default:
                sender.sendMessage(variable.langjson.getString("usage"));
        }
        return false;
    }
}