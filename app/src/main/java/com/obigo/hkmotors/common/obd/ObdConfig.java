package com.obigo.hkmotors.common.obd;

import com.github.pires.obd.commands.ObdCommand;
import com.github.pires.obd.commands.control.TroubleCodesCommand;

import java.util.ArrayList;

/**
 * If it has serialized command, put it below
 */
public final class ObdConfig {

    public static ArrayList<ObdCommand> getCommands() {
        ArrayList<ObdCommand> cmds = new ArrayList<>();

        // Control
        cmds.add(new TroubleCodesCommand());

        // ADD command below

        return cmds;
    }

}
