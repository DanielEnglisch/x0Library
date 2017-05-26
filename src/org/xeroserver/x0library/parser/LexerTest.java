package org.xeroserver.x0library.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LexerTest {

	private File input = null;
	private int chNr = 0;
	private char ch = ' ';
	private int lnNr = 0;
	private List<String> line = new ArrayList<>();
	private sy symbol;
	private String identStr;
	private String numberStr;

	private enum sy {
		EOF, IDENT, NUMBER
	}

	public static void main(String[] args) {
		LexerTest t = new LexerTest(new File("C:\\Users\\Xer0\\Desktop\\test.atg"));
		t.init();
	}

	public LexerTest(File f) {
		this.input = f;
	}

	private boolean isIdent(char c) {
		return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
	}

	private boolean isNumber(char c) {
		return (c >= '0' && c <= '9');
	}

	private void NewCh() {
		if (lnNr < line.size()) {
			if (chNr < line.get(lnNr).length()) {
				ch = line.get(lnNr).charAt(chNr);
				chNr++;
			} else {
				lnNr++;
				chNr = 0;
				ch = ' ';
			}
		} else
			ch = 0;

	}

	private void NewSy() {
		while (ch == ' ')
			NewCh();

		if (isIdent(ch)) {
			identStr = "" + ch;
			NewCh();
			while (isIdent(ch)) {
				identStr += ch;
				NewCh();
			}
			// System.out.println("IDENT: " + identStr);
			symbol = sy.IDENT;
		} else if (isNumber(ch)) {
			numberStr = "" + ch;
			NewCh();
			while (isNumber(ch)) {
				numberStr += ch;
				NewCh();
			}
			// System.out.println("NUMBER: " + numberStr);
			symbol = sy.NUMBER;
		} else {
			switch (ch) {
			case 0:
				symbol = sy.EOF;
				break;
			}
		}

	}

	public void init() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(input));
			while (reader.ready()) {
				line.add(reader.readLine());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		NewCh();
		NewSy();

		while (symbol != sy.EOF) {
			System.out.println(symbol);
			if (symbol == sy.IDENT)
				System.out.println(identStr);
			if (symbol == sy.NUMBER)
				System.out.println(numberStr);
			NewSy();

		}

	}

}
