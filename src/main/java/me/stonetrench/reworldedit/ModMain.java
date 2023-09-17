package me.stonetrench.reworldedit;

import com.fox2code.foxloader.loader.Mod;
import com.fox2code.foxloader.registry.CommandCompat;
import me.stonetrench.reworldedit.commands.*;

public class ModMain extends Mod {

    @Override
    public void onPreInit() {
        WorldEditHelper.Initialize();

        CommandCompat.registerCommand(new SelectPosition1());
        CommandCompat.registerCommand(new SelectPosition2());

        CommandCompat.registerCommand(new StackSelection());

        CommandCompat.registerCommand(new CopySelection());
        CommandCompat.registerCommand(new PasteSelection());

        CommandCompat.registerCommand(new UndoCommand());
        CommandCompat.registerCommand(new RedoCommand());
    }

    @Override
    public void onPostInit() {
        CommandCompat.registerCommand(new SetSelection());
        CommandCompat.registerCommand(new ReplaceSelection());
    }
}
