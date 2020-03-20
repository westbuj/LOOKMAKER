package com.jnn.jw.mid;

import java.io.StringReader;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.jnn.jw.lab.MainActivity;
import com.jnn.util.ANR;
import com.jnn.util.SimpleDOMParser;
import com.jnn.util.SimpleElement;

public class Rules {
	
	
	public ArrayList<KitEntry> kit=null;
	public Bitmap mainimage=null;
	
	public Rules(Context c,String XML)
	{
		this.parseModelRules(c,XML);
	}
	
	public void parseModelRules(Context c,String XML) {
		try {
			this.kit = new ArrayList<KitEntry>();

			StringReader sR = new StringReader(XML);

			// DocumentBuilder db= new DocumentBuilder();
			SimpleDOMParser dP = new SimpleDOMParser();

			SimpleElement cDoc = null;

			cDoc = dP.parse(sR);

			Object[] e = cDoc.getChildElements();// .getDocumentElement();//.getFirstChild();

			for (int j = 0; j < e.length; j++) {
				SimpleElement n = (SimpleElement) e[j];

				if (n.getTagName().equals("modelimage")) {

					this.mainimage = ANR.getBitmap(MainActivity.mContext, n
							.getAttribute("src"));
					

				}

				if (n.getTagName().equals("layer")) {// Load the layer

					KitEntry k = new KitEntry(n.getAttribute("name"),
							new Integer(n.getAttribute("order")).intValue());

					k.mMaskURL = n.getAttribute("mask");

					k.cShade = Color.rgb(255, 255, 255);

					Object[] palNodes = n.getChildElements();
					for (int pal = 0; pal < palNodes.length; pal++) {
						Palette oPal = null;

						SimpleElement palette = (SimpleElement) palNodes[pal];
						if (palette.getTagName().equals("palette")) {
							oPal = k.addPalette(palette.getAttribute("name"),
									palette.getAttribute("details"), palette
											.getAttribute("productURL"));
					//		oPal.addPigment(k,"Remove "+ palette.getAttribute("Name"),255,255,255);
							// Now get the palette Entries
							Object[] palEntryNodes = palette.getChildElements();
							for (int pe = 0; pe < palEntryNodes.length; pe++) {
								SimpleElement pEntry = (SimpleElement) palEntryNodes[pe];

								if (pEntry.getTagName().equals("paletteEntry")) {

									int red = new Integer(pEntry
											.getAttribute("red")).intValue();
									int green = new Integer(pEntry
											.getAttribute("green")).intValue();
									int blue = new Integer(pEntry
											.getAttribute("blue")).intValue();

									oPal.addPigment(k, pEntry
											.getAttribute("name"), red, green,
											blue);

								}
							}

						}
						if (palette.getTagName().equals("externalPalette")) {
							Palette sPal = null;
							String exPallet = ANR.getFile(c.getResources(), palette
									.getAttribute("src"));
							StringReader sR2 = new StringReader(exPallet);

							// DocumentBuilder db= new DocumentBuilder();
							SimpleDOMParser dP2 = new SimpleDOMParser();

							SimpleElement cDoc2 = null;

							cDoc2 = dP2.parse(sR2);

							Object[] e2 = cDoc2.getChildElements();// .getDocumentElement();//.getFirstChild();

							for (int j2 = 0; j2 < e2.length; j2++) {

								SimpleElement sPalette = (SimpleElement) e2[j2];
								if (sPalette.getTagName().equals("palette")) {
									sPal = k.addPalette(sPalette
											.getAttribute("name"), sPalette
											.getAttribute("details"), sPalette
											.getAttribute("productURL"));
									// sPal.mParentLayer=k;

									// Now get the palette Entries
									Object[] palEntryNodes = sPalette
											.getChildElements();
									for (int pe = 0; pe < palEntryNodes.length; pe++) {
										SimpleElement pEntry = (SimpleElement) palEntryNodes[pe];

										if (pEntry.getTagName().equals(
												"paletteEntry")) {

											int red = new Integer(pEntry
													.getAttribute("red"))
													.intValue();
											int green = new Integer(pEntry
													.getAttribute("green"))
													.intValue();
											int blue = new Integer(pEntry
													.getAttribute("blue"))
													.intValue();

											sPal.addPigment(k, pEntry
													.getAttribute("name"), red,
													green, blue);

										}
									}
								}
							}
						}

					}

					k.id=this.kit.size();
					this.kit.add(k.id,k);
					

				}

			}
			

		} catch (Exception e) {
			ANR.alert(MainActivity.mContext, e.getMessage());
			return;
		}
	}

}
