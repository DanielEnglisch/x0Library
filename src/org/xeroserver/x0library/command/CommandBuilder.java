package org.xeroserver.x0library.command;

/**
 * <h3>A class to build command strings which can be used as input for the
 * CommandParser.</h3>
 */
public class CommandBuilder {

	private String arg = "-", flag = "--", cmd = "";

	/**
	 * 
	 * @param head
	 *            Head of the command (e.g <b>apt-get</b> update)
	 * @param arg_prefix
	 *            Prefix of upcoming arguments (e.g <b>-</b> or <b>/</b>)
	 *            Default: -
	 * @param flag_prefix
	 *            Prefix of upcoming flags (e.g <b>--</b> or <b>#</b>) Default:
	 *            --
	 */
	public CommandBuilder(String head, String arg_prefix, String flag_prefix) {
		this.arg = arg_prefix;
		this.flag = flag_prefix;
		this.cmd += head + " ";
	}

	/**
	 * 
	 * @param head
	 *            Head of the command (e.g <b>apt-get</b> update)
	 */
	public CommandBuilder(String head) {
		this.cmd += head + " ";
	}

	/**
	 * Returns the built command.
	 * 
	 * @return The final command string.
	 */
	public String build() {
		return cmd.substring(0, cmd.length() - 1);
	}

	/**
	 * Adds an argument to the existing command string.
	 * 
	 * @param name
	 *            Name of the argument (e.g -<b>name</b> '<b>value</b>')
	 * @param value
	 *            Value of the argument (e.g -<b>name</b> '<b>value</b>')
	 * @return Adjusted version of CommandBuilder
	 */
	public CommandBuilder addArgument(String name, String value) {
		this.cmd += arg + name + " " + "\'" + value + "\' ";
		return this;
	}

	/**
	 * Adds a flag to the existing command string.
	 * 
	 * @param name
	 *            Name of the flag (e.g --<b>name</b>)
	 * @return Adjusted version of CommandBuilder
	 */

	public CommandBuilder addFlag(String name) {
		this.cmd += flag + name + " ";
		return this;
	}

	/**
	 * Adds a value to the existing command string.
	 * 
	 * @param value
	 *            Simple value to add to the command string (e.g <b>csgo</b>)
	 * @return Adjusted version of CommandBuilder
	 */
	public CommandBuilder addValue(String value) {
		this.cmd += value + " ";
		return this;
	}

	/**
	 * Resets / reinitializes the CommandBuilder
	 * 
	 * @param head
	 *            Head of the command (e.g <b>apt-get</b> update)
	 */
	public void reset(String head) {
		this.cmd += head + " ";
		arg = "-";
		flag = "--";
	}

	/**
	 * Resets / reinitializes the CommandBuilder
	 * 
	 * @param head
	 *            Head of the command (e.g <b>apt-get</b> update)
	 * @param arg_prefix
	 *            Prefix of upcoming arguments (e.g <b>-</b> or <b>/</b>)
	 *            Default: -
	 * @param flag_prefix
	 *            Prefix of upcoming flags (e.g <b>--</b> or <b>#</b>) Default:
	 *            --
	 */
	public void reset(String head, String arg_prefix, String flag_prefix) {
		arg = arg_prefix;
		flag = flag_prefix;
		cmd += head + " ";
	}

}
