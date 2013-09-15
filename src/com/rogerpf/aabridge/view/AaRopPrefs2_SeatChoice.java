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
import com.rogerpf.aabridge.model.Zzz;

/**   
 */
class AaRopPrefs2_SeatChoice extends ClickPanel implements ItemListener {

	private static final long serialVersionUID = 1L;

	QLabel watchLabel;
	QLabel changeLabel;

	QLabel whichSeatNew;
	ButtonGroup rbGroupSeatNew = new ButtonGroup();
	QRadioButton southNew;
	QRadioButton eastNew;
	QRadioButton westNew;

	QLabel existingDealsLabel;

	ButtonGroup rbRespectLinYou = new ButtonGroup();
	QRadioButton useLinYou;
	QRadioButton overrideLinYou;

	QLabel whichSeatLin;
	ButtonGroup rbGroupSeatLin = new ButtonGroup();
	QRadioButton southLin;
	QRadioButton eastLin;
	QRadioButton westLin;

	QLabel oldSouthLabel;
	ButtonGroup rbGroupOldSouth = new ButtonGroup();
	QRadioButton oldSouthSouth;
	QRadioButton oldSouthDeclarer;

	QLabel finLabel;
	ButtonGroup rbGroupFin = new ButtonGroup();
	QRadioButton showFinAsFinished;
	QRadioButton showFinReview;

	QLabel unFinLabel;
	ButtonGroup rbGroupUnFin = new ButtonGroup();
	QRadioButton showUnFinAsPlay;
	QRadioButton showUnFinReview;

	public AaRopPrefs2_SeatChoice() {
		setLayout(new MigLayout("insets 0 0 0 0, gap 0! 0!, flowy", "[][]", "[][]"));

		String rbInset = "gapx 7";

		// @formatter:off

		add(whichSeatNew   = new QLabel("For  NEW  deals - Which Seat is You?  -  Declarer (recommended) or defend sitting East / West  -  This is for NEW deals   "), "gapy 8");
		whichSeatNew.setForeground(Aaa.optionsTitleGreen);
		
		boolean eastVal  = (App.youSeatForNewDeal == Zzz.East);
		boolean westVal  = (App.youSeatForNewDeal == Zzz.West);
		boolean southVal = !(eastVal || westVal);
		
		add(westNew        = new QRadioButton(this, rbGroupSeatNew,  westVal,  "west",  "West"), "split2, gapx 5, flowx");
		add(eastNew        = new QRadioButton(this, rbGroupSeatNew,  eastVal,  "east",  "East"), "flowy");
		add(southNew       = new QRadioButton(this, rbGroupSeatNew,  southVal, "south", "South  (Declarer)"), "gapx 37");

		add(changeLabel    = new QLabel("You can change the  'You Seat'  at ANY TIME by clicking on that hands compass banner area  "), "gapy 12");
		changeLabel.setForeground(Aaa.heartsColor);

		add(watchLabel     = new QLabel("When loading  existing  deals  -  Which Seat is You?  "), "gapy 12");
		watchLabel.setForeground(Aaa.optionsTitleGreen);
		add(useLinYou      = new QRadioButton(this, rbRespectLinYou, !App.respectLinYou, "useLinYou", "Use the  'You Seat'  set in the deal  (if any)   use for preset bridge problems"), rbInset);
		add(overrideLinYou = new QRadioButton(this, rbRespectLinYou,  App.respectLinYou, "overrideLinYou", "Use the  'You Seat'  set below"), rbInset);

		boolean eastValLin  = (App.youSeatForLinDeal == Zzz.East);
		boolean westValLin  = (App.youSeatForLinDeal == Zzz.West);
		boolean southValLin = !(eastValLin || westValLin);
		
		add(westLin        = new QRadioButton(this, rbGroupSeatLin,  westValLin,  "LHO  ",  "LHO"), "split2, gapx 3, flowx");
		add(eastLin        = new QRadioButton(this, rbGroupSeatLin,  eastValLin,  "RHO",  "RHO"), "flowy");
		add(southLin       = new QRadioButton(this, rbGroupSeatLin,  southValLin, "Declarer", "Declarer"), "gapx 35");

		add(watchLabel     = new QLabel("When loading  existing  deals  -  the bottom (South) seat should be ?   "), "gapy 12");
		watchLabel.setForeground(Aaa.optionsTitleGreen);
		add(oldSouthSouth  = new QRadioButton(this, rbGroupOldSouth,  !App.putDeclarerSouth, "oldSouthSouth", "Actual South hand  "), rbInset);
		add(oldSouthDeclarer = new QRadioButton(this, rbGroupOldSouth, App.putDeclarerSouth, "oldSouthDeclarer", "Declarer  "), rbInset);

		add(finLabel  = new QLabel("When loading existing  finished  deals -  "), "gapy 12");
		finLabel.setForeground(Aaa.optionsTitleGreen);
		add(showFinAsFinished = new QRadioButton(this, rbGroupFin,  !App.showFinAsReview, "showFinAsFinished", "Show as Finished  -  all 4 hands visible   (normal)"), rbInset);
		add(showFinReview     = new QRadioButton(this, rbGroupFin, App.showFinAsReview, "showFinReview", "Show in Review Mode  -  opps hands hidden  (any  'lin file'  results are also not shown)  "), rbInset);

		add(unFinLabel  = new QLabel("When loading existing  Un-finished  deals -  "), "gapy 25");
		unFinLabel.setForeground(Aaa.optionsTitleGreen);
		add(showUnFinAsPlay   = new QRadioButton(this, rbGroupUnFin,  !App.showUnFinAsReview, "showUnFinAsPlay", "Show in Play / Bidding mode   (normal)  "), rbInset);
		add(showUnFinReview   = new QRadioButton(this, rbGroupUnFin, App.showUnFinAsReview, "showUnFinReview", "Show in Review Mode  -  opps hands hidden  "), rbInset);


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

		else if (source == southNew) {
			App.youSeatForNewDeal = Zzz.South;
		}
		else if (source == westNew) {
			App.youSeatForNewDeal = Zzz.West;
		}
		else if (source == eastNew) {
			App.youSeatForNewDeal = Zzz.East;
		}

		else if (source == useLinYou) {
			App.respectLinYou = true;
		}
		else if (source == overrideLinYou) {
			App.respectLinYou = false;
		}

		else if (source == southLin) {
			App.youSeatForLinDeal = Zzz.South; // declarer
		}
		else if (source == westLin) {
			App.youSeatForLinDeal = Zzz.West; // LHO
		}
		else if (source == eastLin) {
			App.youSeatForLinDeal = Zzz.East; // RHO
		}

		else if (source == oldSouthSouth) {
			App.putDeclarerSouth = false;
		}
		else if (source == oldSouthDeclarer) {
			App.putDeclarerSouth = true;
		}

		else if (source == showFinAsFinished) {
			App.showFinAsReview = false;
		}
		else if (source == showFinReview) {
			App.showFinAsReview = true;
		}

		else if (source == showUnFinAsPlay) {
			App.showUnFinAsReview = false;
		}
		else if (source == showUnFinReview) {
			App.showUnFinAsReview = true;
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
