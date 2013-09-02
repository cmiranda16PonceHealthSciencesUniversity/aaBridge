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
package com.rogerpf.aabridge.view;

import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;

import net.miginfocom.swing.MigLayout;

import com.rogerpf.aabridge.controller.Aaa;
import com.rogerpf.aabridge.controller.App;

/**   
 */
class AaRopPrefs1_DealChoices extends ClickPanel implements ItemListener {

	private static final long serialVersionUID = 1L;

	ButtonGroup rbGroupDeal = new ButtonGroup();
	QRadioButton userBids;
	QRadioButton chosenGame28;
	QRadioButton chosenGame27;
	QRadioButton chosenGame26;

	QRadioButton twoSuitSlam_E1;
	QRadioButton twoSuitSlam_E2;
	QRadioButton twoSuitSlam_M1;
	QRadioButton twoSuitSlam_M2;
	QRadioButton twoSuitSlam_I1;
	QRadioButton twoSuitSlam_I2;
	QRadioButton twoSuitSlam_H1;
	QRadioButton twoSuitSlam_H2;

	QRadioButton ntSmall_M;

	QRadioButton ntGrand_E;
	QRadioButton ntGrand_M;
	QRadioButton ntGrand_H;

	QLabel topLine;

	public AaRopPrefs1_DealChoices() {
		setLayout(new MigLayout("insets 0 0 0 0, gap 0! 0!, flowy", "[][]", "[][]"));

		String rbInset = "gapx 7";

		// @formatter:off	
		add(topLine  = new QLabel("New Deals  -  Choose one of the Deal Options below  (NS always get the best hands)"), "gapy 8");
		topLine.setForeground(Aaa.optionsTitleGreen);
		add(userBids       = new QRadioButton(this, rbGroupDeal, "userBids",       "23+ points,  YOU pick the contract - you need to be declarer to pick the contract  "), rbInset + ", gapy 5");
		add(chosenGame28   = new QRadioButton(this, rbGroupDeal, "chosenGame28",   "28    Game -  EASY  "), rbInset);
		add(chosenGame27   = new QRadioButton(this, rbGroupDeal, "chosenGame27",   "27    Game -  MODERATE  "), rbInset);
		add(chosenGame26   = new QRadioButton(this, rbGroupDeal, "chosenGame26",   "26    Game -  HARDER  "), rbInset);
		add(twoSuitSlam_E1 = new QRadioButton(this, rbGroupDeal, "twoSuitSlam_E1", "28+   E  -  slam two suit fit 5-5, 5-4 -  EASIEST 1 "), rbInset);
		add(twoSuitSlam_E2 = new QRadioButton(this, rbGroupDeal, "twoSuitSlam_E2", "28+   E  -  slam two suit fit 5-4, 5-4 -  EASIEST 2 "), rbInset);
		add(twoSuitSlam_M1 = new QRadioButton(this, rbGroupDeal, "twoSuitSlam_M1", "29+   M -  slam two suit fit 5-4, 5-3 -  MODERATE 1 "), rbInset);
		add(twoSuitSlam_M2 = new QRadioButton(this, rbGroupDeal, "twoSuitSlam_M2", "29+   M -  slam two suit fit 5-3, 5-3 -  MODERATE 2 "), rbInset);
		add(twoSuitSlam_I1 = new QRadioButton(this, rbGroupDeal, "twoSuitSlam_I1", "30+    I  -  slam two suit fit 5-4, 4-4 -  INTERESTING 1 "), rbInset);
		add(twoSuitSlam_I2 = new QRadioButton(this, rbGroupDeal, "twoSuitSlam_I2", "30+    I  -  slam two suit fit 5-3, 4-4 -  INTERESTING 2 "), rbInset);
		add(twoSuitSlam_H1 = new QRadioButton(this, rbGroupDeal, "twoSuitSlam_H1", "31+   H  -  slam two suit fit 4-4, 4-4 -  HARDER 1 "), rbInset);
		add(twoSuitSlam_H2 = new QRadioButton(this, rbGroupDeal, "twoSuitSlam_H2", "31+   H  -  slam two suit fit 4-4, 4-3 -  HARDER 2 "), rbInset);
 		add(ntSmall_M      = new QRadioButton(this, rbGroupDeal, "ntSmall_M",      "32+  6NT -  Easy or Hard?    what do you think?  "), rbInset);
		add(ntGrand_E      = new QRadioButton(this, rbGroupDeal, "ntGrand_E",      "37+  7NT -  EASY  "), rbInset);
		add(ntGrand_M      = new QRadioButton(this, rbGroupDeal, "ntGrand_M",      "35+  7NT -  MODERATE  "), rbInset);
		add(ntGrand_H      = new QRadioButton(this, rbGroupDeal, "ntGrand_H",      "33+  7NT -  HARDEST  "), rbInset);
				
		// @formatter:on
	}

	/** This listens for the check box changed event */
	public void itemStateChanged(ItemEvent e) {

		boolean b = (e.getStateChange() == ItemEvent.SELECTED);
		Object source = e.getItemSelectable();

		// we are only interested in the selected values for the buttons
		if (b == false) {
			; // do nothing
		}
		else if (source == userBids) {
			App.dealCriteria = "userBids";
		}
		else if (source == chosenGame28) {
			App.dealCriteria = "chosenGame28";
		}
		else if (source == chosenGame27) {
			App.dealCriteria = "chosenGame27";
		}
		else if (source == chosenGame26) {
			App.dealCriteria = "chosenGame26";
		}
		else if (source == twoSuitSlam_E1) {
			App.dealCriteria = "twoSuitSlam_E1";
		}
		else if (source == twoSuitSlam_E2) {
			App.dealCriteria = "twoSuitSlam_E2";
		}
		else if (source == twoSuitSlam_M1) {
			App.dealCriteria = "twoSuitSlam_M1";
		}
		else if (source == twoSuitSlam_M2) {
			App.dealCriteria = "twoSuitSlam_M2";
		}
		else if (source == twoSuitSlam_I1) {
			App.dealCriteria = "twoSuitSlam_I1";
		}
		else if (source == twoSuitSlam_I2) {
			App.dealCriteria = "twoSuitSlam_I2";
		}
		else if (source == twoSuitSlam_H1) {
			App.dealCriteria = "twoSuitSlam_H1";
		}
		else if (source == twoSuitSlam_H2) {
			App.dealCriteria = "twoSuitSlam_H2";
		}
		else if (source == ntSmall_M) {
			App.dealCriteria = "ntSmall_M";
		}
		else if (source == ntGrand_E) {
			App.dealCriteria = "ntGrand_E";
		}
		else if (source == ntGrand_M) {
			App.dealCriteria = "ntGrand_M";
		}
		else if (source == ntGrand_H) {
			App.dealCriteria = "ntGrand_H";
		}

		if (App.allConstructionComplete) {
			App.savePreferences();
			App.frame.repaint();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// setBackground(SystemColor.control);
	}

}
