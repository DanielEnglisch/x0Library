package org.xeroserver.x0library.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import org.xeroserver.x0library.objtools.StringTools;

public final class CommandParser {
	
	private String flag_prefix = null;
	private String arg_prefix = null;
	
	public CommandParser(){
		flag_prefix = "--";
		arg_prefix = "-";
	}
	
	public CommandParser(String flag_prefix, String arg_prefix){
		this.flag_prefix = flag_prefix;
		this.arg_prefix = arg_prefix;
	}


	public final Command parse(String cmd) {

		// Trim spaces before and after
		cmd = cmd.replaceAll("^\\s+", "");
		cmd = cmd.replaceAll("\\s+$", "");

		return parse(cmd.split("\\s"));
	}

	public final Command parse(String[] cmd) {

		try {

			if (cmd.length == 0 || cmd[0].replaceAll("\\s", "").equals(""))
				throw new EmptyCommandException();

		} catch (EmptyCommandException e) {
			e.printStackTrace();
			return null;
		}

		// Trim spaces before and after
		cmd[0] = cmd[0].replace("\\s", "");
		cmd[cmd.length - 1] = cmd[cmd.length - 1].replace("\\s", "");
			
				return parseSingleArgsFlagsValues(cmd);

	}

	private Command parseSingleArgsFlagsValues(String[] cmd) {

		ArrayList<String> cmds = new ArrayList<String>(Arrays.asList(cmd));

		ArrayList<String> flags = new ArrayList<String>();
		;
		ArrayList<String> values = new ArrayList<String>();
		HashMap<String, String> arguments = new HashMap<String, String>();

		if (cmd.length > 1) {

			// Filter Flags
			for (int i = 1; i < cmds.size(); i++) {
				if (cmds.get(i).startsWith(flag_prefix)) {
					flags.add(cmds.get(i).substring(flag_prefix.length()));
					cmds.remove(i);
				}
			}

			// Filter Args
			for (int i = 1; i < cmds.size(); i++) {
				if (cmds.get(i).startsWith(arg_prefix)) {
					String key = cmds.get(i).substring(arg_prefix.length());
					String value = "";

					cmds.set(i, "");

					if (i + 1 < cmds.size()) {
						if (cmds.get(i + 1).startsWith("\"") || cmds.get(i + 1).startsWith("\'")) {

							value += StringTools.removeFirstChar(cmds.get(i + 1)) + " ";
							cmds.set(i + 1, "");

							i += 2;

							while (!cmds.get(i).endsWith("\"") && !cmds.get(i).endsWith("\'")) {
								value += cmds.get(i) + " ";
								cmds.set(i, "");
								i++;
							}

							value += StringTools.removeLastChar(cmds.get(i));
							cmds.set(i, "");

						} else {
							value = cmds.get(i + 1);
							cmds.set(i + 1, "");
						}

					}

					arguments.put(key, value);
				}
			}

			// Filter Values
			cmds.remove(0);
			for (String s : cmds) {
				if (!s.equals(""))
					values.add(s);
			}

		}

		return new Command(cmd[0], Collections.unmodifiableMap(arguments), flags.toArray(new String[flags.size()]),
				values.toArray(new String[values.size()]));
	}

	/*private Command parseValueChain(String[] cmd) {

		ArrayList<String> values = new ArrayList<String>();

		if (cmd.length > 1) {
			for (int i = 1; i < cmd.length; i++) {
				String value = "";

				if (cmd[i].startsWith("\'") || cmd[i].startsWith("\"")) {
					cmd[i] = StringTools.removeFirstChar(cmd[i]);

					for (int x = i; x < cmd.length; x++) {
						if (!cmd[x].endsWith("\'") && !cmd[x].endsWith("\"")) {
							value += cmd[x] + " ";
						} else {
							cmd[x] = StringTools.removeLastChar(cmd[x]);
							value += cmd[x];
							i = x;
							break;
						}
					}
				} else {
					value = cmd[i];
				}

				values.add(value);
			}
		}

		return new Command(cmd[0], values.toArray(new String[values.size()]));
	}*/
}



class EmptyCommandException extends Exception {

	private static final long serialVersionUID = 1491999797810276213L;

}
