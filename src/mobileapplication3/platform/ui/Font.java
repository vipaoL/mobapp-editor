package mobileapplication3.platform.ui;

import java.util.Vector;

public class Font {
	public static final int
		STYLE_PLAIN = 0,
		STYLE_BOLD = 1,
		STYLE_ITALIC = 2,
		STYLE_UNDERLINED = 4,
		SIZE_SMALL = 8,
		SIZE_MEDIUM = 0,
		SIZE_LARGE = 16,
		FACE_SYSTEM = 0,
		FACE_MONOSPACE = 32,
		FACE_PROPORTIONAL = 64,
		FONT_STATIC_TEXT = 0,
		FONT_INPUT_TEXT = 1;

	private javax.microedition.lcdui.Font font;
	
	public Font() {
		this(javax.microedition.lcdui.Font.getDefaultFont().getSize());
	}
	
	public Font(int size) {
		javax.microedition.lcdui.Font defFont = javax.microedition.lcdui.Font.getDefaultFont();
		this.font = javax.microedition.lcdui.Font.getFont(defFont.getFace(), defFont.getStyle(), size);
	}
	
	protected Font(javax.microedition.lcdui.Font font) {
		this.font = font;
	}
	
	protected javax.microedition.lcdui.Font getFont() {
		return font;
	}
	
	public int getFace() {
		return font.getFace();
	}

	public int getStyle() {
		return font.getStyle();
	}

	public int getSize() {
		return font.getSize();
	}

	public int getHeight() {
		return font.getHeight();
	}

	public int stringWidth(String str) {
		return font.stringWidth(str);
	}
	
	public int substringWidth(String str, int offset, int len) {
		return font.substringWidth(str, offset, len);
	}
	
	public static Font getDefaultFont() {
		return new Font(javax.microedition.lcdui.Font.getDefaultFont());
	}
	
	public static int defaultFontStringWidth(String str) {
		return javax.microedition.lcdui.Font.getDefaultFont().stringWidth(str);
	}
	
	public static int defaultFontSubstringWidth(String str, int offset, int len) {
		return javax.microedition.lcdui.Font.getDefaultFont().substringWidth(str, offset, len);
	}
	
	public static int getDefaultFontHeight() {
		return javax.microedition.lcdui.Font.getDefaultFont().getHeight();
	}
	
	public static int getDefaultFontSize() {
		return javax.microedition.lcdui.Font.getDefaultFont().getSize();
	}
	
	public int[][] getLineBounds(String text, int w, int padding) {
        Vector lineBoundsVector = new Vector(text.length() / 5);
        int charOffset = 0;
        if (font.stringWidth(text) <= w - padding * 2 && text.indexOf('\n') == -1) {
            lineBoundsVector.addElement(new int[]{0, text.length()});
        } else {
            while (charOffset < text.length()) {
                int maxSymsInCurrLine = 1;
                boolean maxLineLengthReached = false;
                boolean lineBreakSymFound = false;
                for (int lineLength = 1; lineLength <= text.length() - charOffset; lineLength++) {
                    if (font.substringWidth(text, charOffset, lineLength) > w - padding * 2) {
                        maxLineLengthReached = true;
                        break;
                    }
                    
                    maxSymsInCurrLine = lineLength;
                    
                    if (charOffset + lineLength < text.length()) {
                        if (text.charAt(charOffset+lineLength) == '\n') {
                            lineBoundsVector.addElement(new int[]{charOffset, lineLength});
                            charOffset = charOffset + lineLength + 1;
                            lineBreakSymFound = true;
                            break;
                        }
                    }
                }
                
                if (lineBreakSymFound) {
                    continue;
                }
                

                boolean spaceFound = false;

                int maxRightBorder = charOffset + maxSymsInCurrLine;
                
                if (maxRightBorder >= text.length()) {
                    lineBoundsVector.addElement(new int[]{charOffset, maxSymsInCurrLine});
                    break;
                }
                
                if (!maxLineLengthReached) {
                    lineBoundsVector.addElement(new int[]{charOffset, maxSymsInCurrLine}); //
                    charOffset = maxRightBorder;
                } else {
                    for (int i = maxRightBorder; i > charOffset; i--) {
                        if (text.charAt(i) == ' ') {
                            lineBoundsVector.addElement(new int[]{charOffset, i - charOffset});
                            charOffset = i + 1;
                            spaceFound = true;
                            break;
                        }
                    }

                    if (!spaceFound) {
                        lineBoundsVector.addElement(new int[]{charOffset, maxRightBorder - charOffset});
                        charOffset = maxRightBorder;
                    }
                }
            }
        }
        
        int[][] lineBounds = new int[lineBoundsVector.size()][];
        for (int i = 0; i < lineBoundsVector.size(); i++) {
            lineBounds[i] = (int[]) lineBoundsVector.elementAt(i);
        }
        return lineBounds;
    }
}
