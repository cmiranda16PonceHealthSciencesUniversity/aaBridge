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

import net.miginfocom.swing.MigLayout;

import com.rogerpf.aabridge.controller.Aaa;
import com.rogerpf.aabridge.controller.App;

/**
 */
public class BottomLeftPanel extends ClickPanel {

	private static final long serialVersionUID = 1L;

	public EmptyPanel c0_2_0__empt = new EmptyPanel();
	public MsgDisplayPanel c0_2_0__mdp = new MsgDisplayPanel();
	public WeTheyScorePanel c0_2_0__wtsp = new WeTheyScorePanel();
	public UndoPanel c0_2_0__undo = new UndoPanel();
	public NextBoardButtonPanel c0_2_1__nbbp = new NextBoardButtonPanel();

	RpfResizeButton quicksave;
	RpfResizeButton clock;
	RpfResizeButton anti;

	/**
	 */
	BottomLeftPanel() { /* Constructor */

		setLayout(new MigLayout("insets 0 0 0 0, gap 0! 0!, flowy", "3%[]4%", "5%[70%]5%[20%]"));
		add(c0_2_0__empt, "hidemode 3, width 100%, height 100%");
		add(c0_2_0__mdp, " hidemode 3, width 100%, height 100%");
		add(c0_2_0__wtsp, "hidemode 3, width 100%, height 100%");
		add(c0_2_0__undo, "hidemode 3, width 100%, height 100%");
		add(c0_2_1__nbbp, "            width 100%, height 100%");

	}

	/**   
	 */
	public void dealMajorChange() {

	}

	/**
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Aaa.baizeGreen);
	}

	/**   
	 */
	public void matchPanelsToDealState() {

		if (App.isMode(Aaa.REVIEW_PLAY) || App.isMode(Aaa.REVIEW_BIDDING) || App.isMode(Aaa.EDIT_CHOOSE) || App.isMode(Aaa.EDIT_HANDS)) {

			c0_2_0__empt.setVisible(true);
			c0_2_0__undo.setVisible(false);
			c0_2_0__mdp.setVisible(false);
			c0_2_0__wtsp.setVisible(false);
		}
		else if (App.deal.isBidding() || App.isMode(Aaa.EDIT_BIDDING)) {

			c0_2_0__empt.setVisible(false);
			boolean showMsg = (!App.autoBid[App.deal.getNextHandToBid().compass]) && !App.isMode(Aaa.EDIT_BIDDING) && App.showBidPlayMsgs;
			c0_2_0__undo.setVisible(!showMsg);
			c0_2_0__mdp.textArea.setText("Please BID using the\nBidding Panel and or\nyour Keyboard.");
			c0_2_0__mdp.setVisible(showMsg);
			c0_2_0__wtsp.setVisible(false);
		}
		else if (App.deal.isPlaying()) {

			c0_2_0__empt.setVisible(false);
			boolean showMsg = (!App.autoPlay[App.deal.getNextHandToPlay().compass]) && !App.isMode(Aaa.EDIT_PLAY) && App.showBidPlayMsgs
					&& App.deal.lessThanTwoCardsPlayed();
			c0_2_0__undo.setVisible(!showMsg);
			c0_2_0__mdp.textArea.setText("PLAY by clicking on\na CARD or by using\nyour Keyboard.");
			c0_2_0__mdp.setVisible(showMsg);
			c0_2_0__wtsp.setVisible(false);
		}
		else if (App.deal.isFinished()) {
			boolean showUndo = (App.isMode(Aaa.EDIT_PLAY) || App.isMode(Aaa.EDIT_BIDDING));
			c0_2_0__empt.setVisible(false);
			c0_2_0__undo.setVisible(showUndo);
			c0_2_0__mdp.setVisible(false);
			c0_2_0__wtsp.setVisible(!showUndo);
		}
		else {
			assert(false);
		}

		c0_2_1__nbbp.setReviewButtonText();

	}

}

/**   
 */
class UndoPanel extends ClickPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 */
	UndoPanel() { /* Constructor */

		setLayout(new MigLayout("insets 0 0 0 0, gap 0! 0!", "push[]5%", "12%[]"));

		RpfResizeButton b;
		b = new RpfResizeButton(1, "mainUndo", -3, 25);
		add(b, "align right");
	}

	/**
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Aaa.baizeGreen);
	}
}