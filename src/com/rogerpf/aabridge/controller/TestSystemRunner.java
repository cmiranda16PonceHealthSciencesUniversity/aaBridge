/*******************************************************************************
 * Copyright (c) 2013 Roger Pfister.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Roger Pfister - initial API and implementation
 ******************************************************************************/
package com.rogerpf.aabridge.controller;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.rogerpf.aabridge.controller.Book.LinChapter;
import com.rogerpf.aabridge.igf.MassGi;
import com.rogerpf.aabridge.model.Card;
import com.rogerpf.aabridge.model.Deal;
import com.rogerpf.aabridge.model.Deal.DumbAutoDirectives;
import com.rogerpf.aabridge.model.Dir;
import com.rogerpf.aabridge.model.Hand;
import com.rogerpf.aabridge.model.Rank;
import com.rogerpf.aabridge.model.Suit;

public final class TestSystemRunner {

	/**   
	 */
	public static void addGeneralTestInfoToLog(ArrayList<String> log) {
		// ==============================================================================================

		log.add("");
		log.add("");
		log.add("");
		log.add("");
		log.add("");
		log.add("");
		log.add("To make your own tests. Edit the PLAY of a deal stopping at the 'moment of test'. ");
		log.add("For example at the moment that West has to discard correctly in a squeeze attempt.");
		log.add("Save the deal and move it to the tests folder.  The format of the file name needs to be");
		log.add("");
		log.add("        test_<anything1>__xFS__<anything2>__.lin");
		log.add("");
		log.add("          x   is  optional  if present it means NOT so x7H  means NOT the 7 of hearts");
		log.add("          F   is  the desired card Face -  AKQJT98765432");
		log.add("          S   is  the desired suit      -  CDHS");
		log.add("");
		log.add("<anything1> is normally a number e.g.  1001");
		log.add("<anything2> is normally a description e.g.   West needs to guard the king");
		log.add("");

	}

	/**   
	 */
	public static String makeTestsLogFileNameAndPath() {
		// ==============================================================================================
		String s;

//		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd__HH-mm-ss");
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
		s = App.autoSavesPath + "results__" + sdfDate.format(new Date()) + "__.txt";
		return s;
	}

	/** 
	 * Only used once we know that the name contains a double underscore
	 */
	static String extract1stPart(String testName) {
		// ==============================================================================================
		return testName.substring(0, testName.indexOf("__"));
	}

	/** 
	 * Only used once we know that the name contains a double underscore
	 */
	static String extract2ndPart(String name) {
		// ==============================================================================================
		int us1 = name.indexOf("__"); // double underscore
		int us2 = name.indexOf("__", us1 + 4); // double underscore
		return name.substring(us2 + 2);
	}

	/**
	 */
	static class TestInfo {
		// ---------------------------------- CLASS -------------------------------------
		Card card;
		boolean notTheCard;
		int testId;

		TestInfo(Card card, boolean notTheCard, int testId) { // constructor
			// ==============================================================================================
			this.card = card;
			this.notTheCard = notTheCard;
			this.testId = testId;
		}

	}

	/**   
	 */
	static TestInfo validateNameAndExtractDesiredResult(LinChapter test, ArrayList<String> log, Deal d) {
		// ==============================================================================================

		String name = test.filenamePlus;
		int us1 = name.indexOf("__"); // double underscore
		if (us1 < 0) {
			log.add(new String(name + " - BAD test name - could not find the first double underscores"));
			return null;
		}

		if (name.length() < 10) {
			log.add(new String(name + " - BAD test name - could not find the testId"));
			return null;
		}

		int testId = Aaa.extractPositiveInt(name.substring(5, 9));
		if (testId < 1) {
			log.add(new String(name + " - BAD test name - testId must ge greater than zero"));
			return null;
		}

		int us2 = name.indexOf("__", us1 + 4); // double underscore
		if (us2 < 0) {
			log.add(new String(name + " - BAD test name - could not find the second double underscores"));
			return null;
		}

		if (name.length() < us1 + 4) {
			log.add(new String(name + " - BAD test name - need at least 'fs__' after first pair of underscores "));
			return null;
		}

		int extra = 0;
		boolean notTheCard = false;
		char x = name.charAt(us1 + 2);
		if (x == 'x' || x == 'X') {
			notTheCard = true;
			extra++;
		}

		char rankCh = name.charAt(us1 + 2 + extra);
		int rankInt = Aaa.cmdFromChar(rankCh) & 0xff;
		if (rankInt < Rank.Two.v || Rank.Ace.v < rankInt) {
			log.add(new String(name + " - BAD test name - rank should be 1st char after underscores - AKQJT987654321 "));
			return null;
		}

		char suitCh = name.charAt(us1 + 3 + extra);
		Suit suit = Suit.suitFromInt(Aaa.cmdFromChar(suitCh) & 0xff);
		if (suit.v < Suit.Clubs.v || Suit.Spades.v < suit.v) {
			log.add(new String(name + " - BAD test name - suit should be 2nd char after underscores - CDHS "));
			return null;
		}

		if (d.isPlaying() == false) {
			log.add(new String(name + " - Bad Deal State - the deal needs to be in 'Playing' state  are you sure it is not finished or still bidding?"));
			return null;
		}

		Card card = d.getNextHandToPlay().getCardIfMatching(suit, Rank.rankFromInt(rankInt));

		if (card == null) {
			log.add(new String(name + " - Bad Card - the __" + rankCh + "" + suitCh + "__ card is not in the " + d.getNextHandToPlay().compass.toLongStr()
					+ " hand (which is next to play)"));
			return null;
		}

		return new TestInfo(card, notTheCard, testId);
	}

//	static protected String normalizeUnicode(String str) {
//		// ==============================================================================================
//	    Normalizer.Form form = Normalizer.Form.NFD;
//	    if (!Normalizer.isNormalized(str, form)) {
//	        return Normalizer.normalize(str, form);
//	    }
//	    return str;
//	}

	/**   
	 */
	static void performAllTests() {
		// ==============================================================================================
		ArrayList<String> log = new ArrayList<String>(); // ultra simple log maker

		performAllTests__inner(log);

		addGeneralTestInfoToLog(log);

		// save the resutls to a log file
		try {
			String logFilePath = makeTestsLogFileNameAndPath();
			FileWriter fw = new FileWriter(logFilePath);
			BufferedWriter bw = new BufferedWriter(fw);
			for (String line : log) {
				bw.write(line);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			fw.close();

//			if (App.showTestsLogAtEnd) {
			Desktop.getDesktop().open(new File(logFilePath));
//			}

		} catch (IOException e) {
			// Sigh - what do they expect to be done !
		}

		App.deal = new Deal(Deal.makeDoneHand, Dir.South);

		App.mg = new MassGi(App.deal);

		App.switchToNewMassGi("");

	}

	/**   
	 */
	static void performAllTests__inner(ArrayList<String> log) {
		// ==============================================================================================

		Book book = App.bookshelfArray.get(0).getBookByFrontNumb(96 /* The Play Engine tests */);

		if (book == null) {
			log.add("Can't load the 'tests' book - 96");
			return;
		}

		int ONE_TEST_ONLY_ID = 1001;

		Boolean oneTestOnly = false;

		int FAILS_FROM_ID = 7000;

		boolean postedHighTestMessage = false;

		// do all the tests
		int run = 0;
		int failCount = 0;
		int succeeded = 0;
		for (LinChapter test : book) {

			String testName = test.filenamePlus;

			if (testName.startsWith("test") == false)
				continue;
			if (testName.endsWith("__.lin") == false)
				continue;

			App.deal = null;
			test.load();

			Deal d = App.deal;

			if (d == null)
				continue;

			TestInfo ti = validateNameAndExtractDesiredResult(test, log, d);
			if (ti == null)
				continue;

			if (oneTestOnly && (ti.testId != ONE_TEST_ONLY_ID))
				continue;

			if (postedHighTestMessage == false && ti.testId >= FAILS_FROM_ID) {
				postedHighTestMessage = true;

				log.add("");
				log.add("");
				log.add(" Tests numbered above  " + FAILS_FROM_ID + "  are currently expected to fail (but may not) and are NOT counted in the fail total");
				log.add("");
			}

			run++;
			Hand hand = d.getNextHandToPlay();

			d.testId = ti.testId;

			DumbAutoDirectives dumbAutoDir = new DumbAutoDirectives();

			Card cardActual = hand.dumbAuto(dumbAutoDir); // <<<< -------- THE TEST --------

			d.testId = 0;

			boolean failed = false;

			String s = extract1stPart(testName) + "  ";

			if (ti.notTheCard) {
				if (ti.card == cardActual) {
					failed = true;
					if (ti.testId < FAILS_FROM_ID)
						failCount++;
					s += " -  FAIL  -  wanted        __(not)" + ti.card + "__   BUT   got it !";
				}
				else {
					succeeded++;
					s += " -  SUCCESS  - wanted      __(not)" + ti.card + "__   got   __" + cardActual + "__";
				}
				s += "   ";
			}
			else {
				if (ti.card != cardActual) {
					failed = true;
					if (ti.testId < FAILS_FROM_ID)
						failCount++;
					s += " -  FAIL  -  wanted   __" + ti.card + "__   got   __" + cardActual;
				}
				else {
					succeeded++;
					s += " -  SUCCESS  - wanted and got   __" + cardActual;
				}
				s += "__    ";
			}

			s += extract2ndPart(testName);

			if (failed && (ti.testId < FAILS_FROM_ID)) { // we not the first fail with two preceding blank lines
				log.add("****");
			}

			log.add(s);

			if (failed && (ti.testId < FAILS_FROM_ID)) { // we not the first fail with two preceding blank lines
				log.add("****");
				log.add("");
			}

		}

		String s1 = "===========================================================";
		String s2 = "           Ran " + run + "      Succeed " + succeeded + "    Failed " + failCount;

		log.add(0, "");
		log.add(1, s1);
		log.add(2, s2);
		log.add(3, s1);
		log.add(4, "");

	}
}
