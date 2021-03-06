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

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import com.rogerpf.aabridge.controller.Aaa;
import com.rogerpf.aabridge.controller.App;
import com.rogerpf.aabridge.controller.Book.LinChapter;
import com.rogerpf.aabridge.model.Cc;
import com.rogerpf.aabridge.model.Lin;

/** *********************************************************************************  
 */
public class AaBookPanel extends JPanel {
	// ---------------------------------- CLASS -------------------------------------
	private static final long serialVersionUID = 1L;

	public static int defaultWidthPixels = App.onMacOrLinux ? 160 : 120;

	public AaBookPanelInner aaBookPanelInner;

	JScrollPane scroller;

	public AaBookPanel() { // Constructor
		// ==============================================================================================

		aaBookPanelInner = new AaBookPanelInner();
		setMinimumSize(new Dimension(defaultWidthPixels, 5000));

		// setVisible(false);
	}

	public void setCorrectWidth(boolean ddArrayListVis) {
		// ==============================================================================================

		int w = defaultWidthPixels;

		if (ddArrayListVis)
			w -= 21;

		setMinimumSize(new Dimension(w, 0));
	}

	@SuppressWarnings("serial")
	public void matchToAppBook() {
		// ==============================================================================================
		removeAll();

		aaBookPanelInner.stopTimers();
		aaBookPanelInner = new AaBookPanelInner();
		aaBookPanelInner.matchToAppBook();

		setLayout(new MigLayout(App.simple + ", flowy", "[100%]", "[100%]"));

		JScrollPane scroller = new JScrollPane(aaBookPanelInner);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		scroller.setBorder(BorderFactory.createEmptyBorder());

		add(scroller, "width 100%, height 100%");

		scroller.setLayout(new ScrollPaneLayout() {
			@Override
			public void layoutContainer(Container parent) {
				JScrollPane scrollPane = (JScrollPane) parent;
				scrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
				super.layoutContainer(parent);
				scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			}
		});
		scroller.setVisible(true);
		setVisible(true);
	}

	public int getLoadedChapterIndex() {
		// ==============================================================================================
		return aaBookPanelInner.getLoadedChapterIndex();
	}

	public void showChapterAsSelected(String s) {
		// ==============================================================================================
		aaBookPanelInner.showChapterAsSelected(s);
	}

	public void showChapterAsSelected(int ind) {
		// ==============================================================================================
		aaBookPanelInner.showChapterAsSelected(ind);
	}

	public void showChapterAsBroken(int ind) {
		// ==============================================================================================
		aaBookPanelInner.showChapterAsBroken(ind);
	}

	/**
	 * We are 90% filled by scroller which is the scroll bar plus inner
	 */
//	/**
//	 */
//	public void paintComponent(Graphics g) {
//		// ==============================================================================================
//		super.paintComponent(g);
//	}

}

/***********************************************************************************  
 */
class AaBookPanelInner extends ClickPanel {
	// ---------------------------------- CLASS -------------------------------------
	private static final long serialVersionUID = 1L;

	ArrayList<BookNameBtn> buttons = new ArrayList<BookNameBtn>();

	private int linfileChangePollTimer_ms = 933;

	String linfileChangePrevName = "";

	Timer linfileChangePollTimer = new Timer(linfileChangePollTimer_ms, new ActionListener() {
		// ==============================================================================================
		public void actionPerformed(ActionEvent evt) {

			if (buttons.size() != 3) // 3 = 1 + the 2 hidden "start and end" empty space buttons
				return;

			// as we there is only one chapter in this book it will have index 0
			boolean change = App.book.getChapterByIndex(0).hasLinFileChanged(linfileChangePollTimer_ms);

			if (change) {
				// System.out.println(" file change detected ");
				int prev_end = (App.mg.lin.linType == Lin.FullMovie || App.mg.lin.linType == Lin.Other) ? App.mg.get_current_pg_number_display() : -1;
				App.book.loadChapterByIndex(0);
				if (prev_end > 0)
					App.mg.jump_to_pg_number_display(prev_end);
			}
		}
	});

	public AaBookPanelInner() {
		// ==============================================================================================
		setLayout(new MigLayout(App.simple + ", flowy", "[]", "grow"));
		setVisible(true);
		setOpaque(true);

		linfileChangePollTimer.setInitialDelay(linfileChangePollTimer_ms);
		linfileChangePollTimer.start();
	}

	public void stopTimers() {
		// ==============================================================================================
		linfileChangePollTimer.stop();
	}

	public int getLoadedChapterIndex() {
		// ==============================================================================================
		for (BookNameBtn butt : buttons) {
			if (butt.currentLin) {
				return butt.index;
			}
		}
		return -1;
	}

	public void matchToAppBook() {
		// ==============================================================================================

		BookNameBtn button = new BookNameBtn(this, " ", -1, "");
		add(button);
		buttons.add(button);
		setBackground(Cc.g(Cc.darkGrayBg));

		int i = 0;
		while (i < App.book.size()) {
			LinChapter h = App.book.get(i);

			String mruKey = h.generateMruKey();

			button = new BookNameBtn(this, h.displayNoUscore, i, mruKey);
			add(button);
			buttons.add(button);
			if (i == 0) {
				button.setButtonCurrentLin(true);
			}
			i++;
		}

		button = new BookNameBtn(this, " ", -1, "");
		add(button);
		buttons.add(button);

		setVisible(true);
	}

	public void componentResized(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void showChapterAsSelected(String chapterName) {
		// ==============================================================================================
		for (BookNameBtn button : buttons) {
			if (button.getText().contains(chapterName)) {
				setButtonCurrentLin(button);
				scrollRectToVisible(button.getBounds());
				return;
			}
		}
	}

	public void setButtonCurrentLin(BookNameBtn button) {
		// ==============================================================================================
		for (BookNameBtn butt : buttons) {
			butt.setButtonCurrentLin(butt == button);
		}
	}

	public void showChapterAsSelected(int ind) {
		// ==============================================================================================
		for (BookNameBtn button : buttons) {
			if (button.index == ind) {
				setButtonCurrentLin(button);
				scrollRectToVisible(button.getBounds());
				return;
			}
		}
	}

	public void showChapterAsBroken(int ind) {
		// ==============================================================================================
		for (BookNameBtn button : buttons) {
			if (button.index == ind) {
				button.brokenLin = true;
				scrollRectToVisible(button.getBounds());
				return;
			}
		}
	}

	public void bookButtonClicked(BookNameBtn button) {
		// ==============================================================================================

		int index = button.index;
		if (index < 0)
			return; // top and bottom end spacers

		Boolean success = false;
		try {
			success = App.book.loadChapterByIndex(index);
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}

		if (success) {
			scrollRectToVisible(button.getBounds());
			setButtonCurrentLin(button);
		}
		else
			button.brokenLin = true;

	}

	public void paintComponent(Graphics g) {
//		super.paintComponent(g); we do it all below
		Graphics2D g2 = (Graphics2D) g;

		/**
		 *  This way we control the appearance what ever the system
		 */
		Rectangle2D.Float rect = new Rectangle2D.Float(0, 0, getWidth(), getHeight());
		g2.setColor(Cc.g(Cc.darkGrayBg));
		g2.fill(rect);
	}

}

/** *********************************************************************************  
 */
class BookNameBtn extends JButton implements MouseListener {
	// ---------------------------------- CLASS -------------------------------------
	private static final long serialVersionUID = 1L;

	boolean hover = false;
	boolean pressed = false;
	boolean currentLin = false;
	boolean brokenLin = false;
	boolean examined = false;
	String mruKey = "";
	int index;

	AaBookPanelInner boss;

	public BookNameBtn(AaBookPanelInner boss, String text, int index, String mruKey) { /* Constructor */
		// ==============================================================================================
		super();

		this.boss = boss;
		this.index = index;
		this.mruKey = mruKey;

		setBorder(new EmptyBorder(0, 0, 0, 0));
		setFocusable(false);
//		setMinimumSize(new Dimension(1, 1));

		addMouseListener(this);

		setText(text + "                                             ");
	}

	public void setButtonCurrentLin(boolean current) {
		if (current) {
			examined = true;
			brokenLin = false;
			currentLin = true;
		}
		else
			currentLin = false;
	}

	public void setButtonBrokenLin() {
		brokenLin = true;
	}

	public void mousePressed(MouseEvent e) {
		pressed = true;
		App.frame.repaint();
	}

	public void mouseReleased(MouseEvent e) {
		pressed = false;
		// a hack to make the two (empty) separation buttons act like background space
		if (e.getButton() == MouseEvent.BUTTON3 && getText().contentEquals("                                              ")) {
			App.frame.clickPasteTimer.start();
			return;
		}
		boss.bookButtonClicked(this);
		App.frame.repaint();
	}

	public void mouseEntered(MouseEvent e) {
		hover = true;
		App.frame.repaint();
	}

	public void mouseExited(MouseEvent e) {
		hover = false;
		App.frame.repaint();
	}

	public void mouseClicked(MouseEvent e) {
	}

	final static Color unexaminedColor = Aaa.veryWeedyBlack;
	final static Color examinedColor = Color.BLACK;
	final static Color hoverColor = Color.white; // new Color( 0, 255, 0);
	final static Color pressedColor = Aaa.tutorialLinkNorm_g;
	final static Color currentLinColor = Color.BLACK;
	final static Color brokenColor = Color.RED;
	final static Color hasChapterMarksColor = Aaa.buttonBkgColorStd;

	public void paintComponent(Graphics g) {
//		super.paintComponent(g);  we do it all
		Graphics2D g2 = (Graphics2D) g;
		Aaa.commonGraphicsSettings(g2);

		g2.setColor(currentLin ? Aaa.tutorialBackground : Cc.g(Cc.darkGrayBg));

		int wb = getWidth();
		int hb = getHeight();

		g2.fillRect(0, 0, wb, hb);

		if ((mruKey.isEmpty() == false) && (App.mruCollection.getChapterMarksCount(mruKey) > 0)) {
			g2.setColor(hasChapterMarksColor);
			g2.fillRect(AaBookPanel.defaultWidthPixels - 20, 0, 3, hb);
		}

		// System.out.println(index + " " + examined);

		if (pressed)
			g2.setColor(currentLin ? Aaa.tutorialLinkHover_h : pressedColor);
		else if (brokenLin)
			g2.setColor(brokenColor);
		else if (currentLin)
			g2.setColor(currentLinColor);
		else if (hover)
			g2.setColor(hoverColor);
		else if (examined)
			g2.setColor(examinedColor);
		else
			g2.setColor(unexaminedColor);

		int height = getHeight();
		String text = getText();
		char first = (text.length() > 0) ? text.charAt(0) : 0x00;
		float nudgeUp = ((first == '>') || (first == '<')) ? -0.04f * height * 1 : 0.0f;

		Aaa.drawLeftString(g2, text, 4, nudgeUp, getWidth(), height);
	}
}