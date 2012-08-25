/*  
 *  Ep1c G4m3 -- A parody platformer
 * 
 *  Copyright (C) 2011  Voracious Softworks
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */

package com.voracious.graphics.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Utility class for drawing text in the game's font.
 * 
 * @author Voracious Softworks
 */

public class Text extends Sprite {

	private static final long serialVersionUID = 6401867774229024720L;
	public static final int FONT_WIDTH = 6;
	public static final int FONT_HEIGHT = 8;

	public static final int DEF_SIZE = 8;
	public static final int DEF_SPACING = 1;
	public static final Color DEF_COLOR = Color.BLACK;

	private String myText;
	private int mySpacing;
	private int mySize;
	private Color myColor;
	public static final String image = "/font.png";
	private static final String chars = "" + //
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ.,!?\"'/\\<>()[]{}" + //
			"abcdefghijklmnopqrstuvwxyz_               " + //
			"0123456789+-=*:;";//

	/**
	 * Create a Drawable string out of a regular String.
	 * 
	 * @param text
	 *            string to be made into an image
	 */

	public Text(String text) {
		super(text.length()*(FONT_WIDTH + (DEF_SIZE - FONT_HEIGHT)) + (text.length()-1)*DEF_SPACING, DEF_SIZE, parseString(text, DEF_SIZE, DEF_SPACING, DEF_COLOR));
		myText = text;
		mySpacing = DEF_SPACING;
		mySize = DEF_SIZE;
		myColor = DEF_COLOR;
	}

	/**
	 * Create a Drawable string out of a regular String and Initialize it's
	 * location.
	 * 
	 * @param text
	 *            message
	 * @param size
	 *            font size in pixels
	 */
	public Text(String text, int size) {
		super(text.length()*(FONT_WIDTH + (size - FONT_HEIGHT)) + (text.length()-1)*DEF_SPACING, size, parseString(text, size, DEF_SPACING, DEF_COLOR));
		myText = text;
		mySpacing = DEF_SPACING;
		mySize = DEF_SIZE;
		myColor = DEF_COLOR;
	}

	/**
	 * Create a Drawable string out of a regular String and Initialize it's
	 * location.
	 * 
	 * @param text
	 *            message
	 * @param size
	 *            font size in pixels
	 * @param spacing
	 *            font spacing in pixels
	 */
	public Text(String text, int size, int spacing) {
		super(text.length()*(FONT_WIDTH + (size - FONT_HEIGHT)) + (text.length()-1)*spacing, size, parseString(text, size, spacing, DEF_COLOR));
		myText = text;
		mySpacing = DEF_SPACING;
		mySize = DEF_SIZE;
		myColor = DEF_COLOR;
	}

	/**
	 * Create a Drawable string out of a regular String and Initialize it's
	 * location.
	 * 
	 * @param text
	 *            message
	 * @param size
	 *            font size in pixels
	 * @param spacing
	 *            font spacing in pixels
	 * @param color
	 *            font color
	 */
	public Text(String text, int size, int spacing, Color color) {
		super(text.length()*(FONT_WIDTH + (size - FONT_HEIGHT)) + (text.length()-1)*spacing, size, parseString(text, size, spacing, color));
		myText = text;
		mySpacing = DEF_SPACING;
		mySize = DEF_SIZE;
		myColor = DEF_COLOR;
	}

	/**
	 * Use the font file to create an image of text.
	 * 
	 * @param text
	 *            string to parse
	 * @return string converted into an image
	 */

	private static BufferedImage parseString(String text, int size, int spacing, Color color) {
		int numLines = 1;
		int maxCharsPerLine = 0;
		BufferedImage fontImg;

		try {
			fontImg = ImageIO.read(Text.class.getResourceAsStream(image));

			int tempCount = 0;
			for (int i = 0; i < text.length(); i++) {
				char tempChar = text.charAt(i);

				if (tempChar == '\n') {
					numLines++;
					tempCount = 0;
				} else {
					tempCount++;
				}

				if (tempCount > maxCharsPerLine)
					maxCharsPerLine = tempCount;
			}

			int letterWidth = FONT_WIDTH + (size - FONT_HEIGHT);
			int letterHeight = size;
			BufferedImage result = new BufferedImage(letterWidth
					* maxCharsPerLine + (maxCharsPerLine - 1) * spacing,
					(letterHeight + 1) * numLines, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = result.createGraphics();

			int currentLine = 0;
			int currentCharInLine = 0;
			for (int i = 0; i < text.length(); i++) {
				if (text.charAt(i) == '\n') {
					currentLine++;
					currentCharInLine = 0;
				} else {
					int charCode = chars.indexOf(text.charAt(i));

					int row = (charCode) / (fontImg.getWidth() / FONT_WIDTH);
					BufferedImage letter = fontImg.getSubimage(((charCode)
							% (fontImg.getWidth() / FONT_WIDTH) - row)
							* FONT_WIDTH, row * FONT_HEIGHT, FONT_WIDTH,
							FONT_HEIGHT);

					if (size != 8)
						letter = scale(letter, size);

					g2.drawImage(letter, null, letterWidth * currentCharInLine
							+ (currentCharInLine * spacing), letterHeight
							* currentLine + currentLine);
					currentCharInLine++;
				}
			}

			if (color != Color.WHITE)
				result = changeColor(result, color);

			g2.dispose();

			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Scales a character image to the specified height
	 * 
	 * @param image
	 *            the image to scale
	 * @param size
	 *            the new height
	 * @return scaled image
	 */
	private static BufferedImage scale(BufferedImage image, int size) {
		BufferedImage result = new BufferedImage(
				(int) (size * ((double) FONT_WIDTH / (double) FONT_HEIGHT)),
				size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = result.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.drawImage(image, 0, 0,
				(int) (size * ((double) FONT_WIDTH / (double) FONT_HEIGHT)),
				size, null);
		g2.dispose();
		return result;
	}

	/**
	 * Changes white pixels to the specified color in an image Meant for
	 * changing font color
	 * 
	 * @param image
	 *            image to recolor
	 * @param color
	 *            color to change white to
	 * @return the recolored imaged
	 */
	private static BufferedImage changeColor(BufferedImage image,
			final Color color) {
		ImageFilter filter = new RGBImageFilter() {
			public final int filterRGB(int x, int y, int rgb) {
				if (rgb == Color.WHITE.getRGB()) {
					return color.getRGB();
				} else {
					return rgb;
				}
			}
		};
		ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
		Image out = Toolkit.getDefaultToolkit().createImage(ip);
		BufferedImage result = new BufferedImage(out.getWidth(null),
				out.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		result.createGraphics().drawImage(out, 0, 0, null);

		return result;
	}

	/**
	 * Supplies the image string.
	 * 
	 * @return string Image text
	 */

	@Override
	public String toString() {
		return myText;
	}

	/**
	 * Supplies the font size.
	 * 
	 * @return size modifier
	 */

	public int getSize() {
		return mySize;
	}

	/**
	 * Supplies the font spacing.
	 * 
	 * @return pixels between each letter
	 */

	public int getSpacing() {
		return mySpacing;
	}

	/**
	 * Supplies font color.
	 * 
	 * @return font color
	 */

	public Color getColor() {
		return myColor;
	}
}
