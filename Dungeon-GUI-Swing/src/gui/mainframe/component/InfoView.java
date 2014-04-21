package gui.mainframe.component;

import gui.JDGUISwing;
import gui.JDJPanel;
import gui.Paragraph;
import gui.engine2D.DrawUtils;
import gui.mainframe.MainFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.util.LinkedList;
import java.util.StringTokenizer;

import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import util.JDColor;

/**
 * @author Duke1
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */
public class InfoView extends JDJPanel {

	String newline = "\n";

	boolean emptyLine = true;

	DefaultStyledDocument doc = new DefaultStyledDocument();

	JTextPane verlauftxt2;

	Image bgImage;

	Paragraph[] actualP;

	Paragraph[] paintedP;

	Image offscreenImage;

	int sizex = 300;

	int sizey = 150;

	MainFrame m;

	public InfoView(JDGUISwing gui, MainFrame m) {
		super(gui);
		this.m = m;
		this.setLayout(new BorderLayout());
		if (MainFrame.imageSource != null) {
			MediaTracker tracker = new MediaTracker(m);
			
			bgImage = MainFrame.imageSource.loadImage("tafel3.gif");
			tracker.addImage(bgImage, 1);
		
			try {
				tracker.waitForAll();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		verlauftxt2 = new JTextPane(doc);

		this.setPreferredSize(new Dimension(sizex, sizey));
		this.setOpaque(false);
		

	}
	private static final int texSizeX = 96;

	private static final int texSizeY = 96;
	

	@Override
	public void paint(Graphics g) {
		if (m != null) {
			if (offscreenImage == null) {

				offscreenImage = m.createImage(this.getWidth(), this
						.getHeight());
			}
			if (actualP != null) {
				if (!Paragraph.areEqual(paintedP, actualP)) {
					if (bgImage != null) {
						// System.out.println("Male tafel");
						Graphics g2 = offscreenImage.getGraphics();
						int i = 0;
						int j = 0;
						while (i * texSizeX < this.getWidth()) {
							j = 0;
							while (j * texSizeY < this.getHeight()) {

								g2.drawImage(JDJPanel.getBackGroundImage(), i * texSizeX, j
										* texSizeY, texSizeX, texSizeY, null);
								j++;
							}
							i++;
						}
						g2.drawImage(bgImage, 0, 0, this.getWidth(), this
								.getHeight(), null);
						
						

						for (i = 0; i < actualP.length; i++) {
							if (actualP[i] != null) {

								String text = actualP[i].getText();
								g2.setFont(new Font(actualP[i].getFont(), 0,
										actualP[i].getSize()));
								g2.setColor(DrawUtils.convertColor(actualP[i]
										.getC()));
								if (text != null) {

									g2.drawString(text, calcx(actualP[i], i),
											calcy(actualP[i], i));
								}
							}
						}
						paintedP = actualP;
					}
				}
			}

			g.drawImage(offscreenImage, 0, 0, null);
		}
	}

	public int calcx(Paragraph p, int i) {
		int l = 0;
		if (p.getText() != null) {
			l = p.getText().length();
		}
		int size = p.getSize();
		int space = l * size / 2;
		return (this.getWidth() - space) / 2;

	}

	public int calcy(Paragraph p, int i) {

		return 50 + 25 * i;

	}

	public InfoView(int x, int y, boolean emptyLine, JDGUISwing gui) {
		super(gui);
		this.emptyLine = emptyLine;
		verlauftxt2 = new JTextPane(doc);
		verlauftxt2.setEditable(false);
		verlauftxt2.setBorder(new EtchedBorder());
		verlauftxt2.setBackground(Color.lightGray);
		verlauftxt2.setMargin(new Insets(5, 5, 5, 5));

		this.add(verlauftxt2);

		verlauftxt2.setPreferredSize(new Dimension(x, y));
		verlauftxt2.setMinimumSize(new Dimension(x, y));
		SimpleAttributeSet set = new SimpleAttributeSet();
		try {
			doc.insertString(doc.getLength(), "", set);
		} catch (Exception e) {
		}
	}

	public void resetText2() {

		try {
			doc.remove(0, doc.getLength());
			doc.insertString(0, "\n", new SimpleAttributeSet());
		} catch (BadLocationException ble) {
			System.err.println("Couldn't insert initial text.");
		}
	}

	public void resetText() {
		actualP = null;
	}

	/**
	 * 
	 */
	public void setText(Paragraph[] p) {

		if (p != null) {
			
				Paragraph []x = processParagraph(p);
				if(!Paragraph.areEqual(x, this.actualP)) {
					this.actualP = x;
					repaint();
				}

		} else {
			resetText();
		}
	}
	
	private Paragraph[] processParagraph(Paragraph [] p) {
		
		LinkedList list = new LinkedList();
		for (int i = 0; i < p.length; i++) {
			if (p[i] != null && p[i].getText() != null) {

				StringTokenizer tokenizer = new StringTokenizer(p[i]
						.getText(), "\n");
				while (tokenizer.hasMoreTokens()) {
					String s = tokenizer.nextToken();
					Paragraph sub = new Paragraph(s);
					sub.setC(p[i].getC());
					sub.setSize(p[i].getSize());
					sub.setFont(p[i].getFont());
					list.add(sub);
				}

			}
		}
		Paragraph[] b = new Paragraph[list.size()];
		for (int j = 0; j < list.size(); j++) {
			b[j] = ((Paragraph) (list.get(j)));

		}
		return b;
	}

	public void setText2(Paragraph[] pars) {

		try {
			doc.remove(0, doc.getLength());
			if (emptyLine) {
				doc.insertString(0, "\n", new SimpleAttributeSet());
			}
		} catch (BadLocationException ble) {
			System.err.println("Couldn't insert initial text.");
		}
		if (pars != null) {

			for (int i = 0; i < pars.length; i++) {
				SimpleAttributeSet style = new SimpleAttributeSet();
				

				boolean bold = pars[i].isBold();
				boolean just = pars[i].isJustified();
				boolean centered = pars[i].isCentered();
				if (centered)
					StyleConstants.setAlignment(style,
							StyleConstants.ALIGN_CENTER);
				if (just)
					StyleConstants.setAlignment(style,
							StyleConstants.ALIGN_JUSTIFIED);
				if (bold)
					StyleConstants.setBold(style, true);

				JDColor color = pars[i].getC();
				if (color != null) {

				StyleConstants.setForeground(style,
							DrawUtils.convertColor(color));
				}
				String font = pars[i].getFont();
				if (font != null) {
					StyleConstants.setFontFamily(style, font);
				}
				int size = pars[i].getSize();
				if (size != 0) {
					StyleConstants.setFontSize(style, size);
				}
				
				String s = pars[i].getText();
				if (s == null) {
					s = new String();
				}
				int l = s.length();
				int k = doc.getLength();
				try {

					doc.insertString(doc.getLength(), s + newline, style);
					// Formatierungen werden nicht korrekt angenommen
					doc.setParagraphAttributes(k, l, style, true);
					// Hier wird fuer jeden Absatz gepluggt

				} catch (BadLocationException ble) {
					System.err.println("Couldn't insert initial text.");
				}
			}
		} else {
			try {

				doc.insertString(0, "Info ist null!" + newline,
						new SimpleAttributeSet());

			} catch (BadLocationException ble) {
				System.err.println("Couldn't insert initial text.");
			}
		}

	}

}
