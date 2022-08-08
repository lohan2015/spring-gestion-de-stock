/*
 * Copyright (C) 2007 by Quentin Anciaux
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Library General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *	@author Quentin Anciaux
 */
package com.kinart.organisation.business.services;
//import org.allcolor.yahp.converter.CYaHPConverter;
//import org.allcolor.yahp.converter.IHtmlToPdfTransformer;

import com.kinart.paie.business.services.utils.ParameterUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple example to convert an URL pointing to an html file into a
 * PDF.
 *
 * @author Quentin Anciaux
 * @version 0.91
 */
public class ClsHtmlToPdfConverter {
	/** An handle to a yahp converter */
//	private static CYaHPConverter converter = new CYaHPConverter();

	/**
	 * Start the Simple Conversion tool
	 *
	 *
	 *
	 * @throws Exception if an error occured while converting. exit
	 * 		   status = 0 if all is ok.
	 */
	public void _generatePDF(String _strCheminFichierHtml, String _strCheminFichierPDF,String _strCheminFontes) throws Exception {
	
		String  url				 = _strCheminFichierHtml;
		String  outfile		 = _strCheminFichierPDF;
		

		if (outfile == null) {
			showUsage("--out file must exists !");
		} // end if

		try {
			ParameterUtil.println("Url = "+url);
			new URL(url);
		} // end try
		catch (final Exception e) {
			showUsage("--url must be a valid URL !");
		} // end catch

		try {
			ParameterUtil.println("-------------Begin getting out file");
			
			File fout = new File(outfile);
			
			ParameterUtil.println("-------------End getting out file");

			List			 headerFooterList = new ArrayList();
			
			
			ParameterUtil.println("-------------Begin opening out file");
			FileOutputStream out = new FileOutputStream(fout);
			ParameterUtil.println("-------------End opening out file");
			ParameterUtil.println("--------------------------before conversion");

			Map properties = new HashMap();

//				String strHeader="<table border=\"0\" style=\"width: 840px;\" cellspacing=\"0\" cellpadding=\"0\">\n" +
//				"<tr>\n" +
//				"	<td style=\"width: 200px;text-align: left;\">\n" +
//				"		Generated with YaHPConverter.\n" +
//				"	</td>\n" +
//				"	<td style=\"width: 200px;\">&nbsp;</td>\n" +
//				"	<td style=\"text-align: right;\">\n" +
//				"		Page $pagenum / $pagetotal\n" + "	</td>\n" +
//				"</tr>\n" + "</table>";
//				headerFooterList.add(new IHtmlToPdfTransformer.CHeaderFooter(
//						"",
//						IHtmlToPdfTransformer.CHeaderFooter.HEADER,
//						IHtmlToPdfTransformer.CHeaderFooter.ODD_PAGES));
//				headerFooterList.add(new IHtmlToPdfTransformer.CHeaderFooter(
//						"",
//						IHtmlToPdfTransformer.CHeaderFooter.HEADER,
//						IHtmlToPdfTransformer.CHeaderFooter.EVEN_PAGES));
//				headerFooterList.add(new IHtmlToPdfTransformer.CHeaderFooter(
//						"<table border=\"0\" style=\"width: 840px;\" cellspacing=\"0\" cellpadding=\"0\">\n" +
//						"<tr>\n" +
//						"	<td style=\"width: 150px;text-align: left;\">\n" +
//						"		&nbsp;\n" + "	</td>\n" +
//						"	<td style=\"width: 200px;\">&nbsp;</td>\n" +
//						"	<td style=\"text-align: right;\">\n" +
//						"		&copy; 2007\n" + "	</td>\n" +
//						"</tr>\n" + "</table>",
//						IHtmlToPdfTransformer.CHeaderFooter.FOOTER,
//						IHtmlToPdfTransformer.CHeaderFooter.ALL_PAGES));
//				properties.put(IHtmlToPdfTransformer.PDF_RENDERER_CLASS,
//						IHtmlToPdfTransformer.DEFAULT_PDF_RENDERER);
//				
//				if (_strCheminFontes != null)
//					properties.put(IHtmlToPdfTransformer.FOP_TTF_FONT_PATH, _strCheminFontes);
//			
//			converter.convertToPdf(new URL(url),
//				IHtmlToPdfTransformer.A4P, headerFooterList, out,
//				properties);
			ParameterUtil.println("after conversion");
			out.flush();
			out.close();
		} // end try
		catch (final Throwable t) {
			t.printStackTrace();
			System.err.println("An error occurs while converting '" +
				url + "' to '" + outfile + "'. Cause : " +
				t.getMessage());
			System.exit(-1);
		} // end catch

		System.exit(0);
	} // end main()

	/**
	 * Return the value of the given parameter if set
	 *
	 * @param args startup arguments
	 * @param name parameter name
	 *
	 * @return the value of the given parameter if set or null
	 */
	private static String getParameter(
		final String args[],
		final String name) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals(name)) {
				if ((i + 1) < args.length) {
					return args[i + 1];
				} // end if

				break;
			} // end if
		} // end for

		return null;
	} // end getParameter()

	/**
	 * return true if the given parameter is on the command line
	 *
	 * @param args startup arguments
	 * @param name parameter name
	 *
	 * @return true if the given parameter is on the command line
	 */
	private static boolean hasParameter(
		final String args[],
		final String name) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals(name)) {
				return true;
			} // end if
		} // end for

		return false;
	} // end hasParameter()

	/**
	 * Show the usage of the tool
	 *
	 * @param message An error message
	 */
	private static void showUsage(final String message) {
		if (message != null) {
			ParameterUtil.println(message);
		} // end if

		ParameterUtil.println(
			"Usage :\n\tjava -cp yahp-sample.jar:yahp.jar org.allcolor.yahp.sample.CSimpleConversion" +
			" --url [http|file]://myuri --out /path/to.pdf [font options] [renderer options] [security options] [--help|-h]");
		ParameterUtil.println("\t[font options]:");
		ParameterUtil.println("\t\t[--fontpath directory where TTF font files are located]");
		ParameterUtil.println("\t[renderer options]:");
		ParameterUtil.println("\t(Default renderer use Swing HtmlEditorKit. no option.)");
		ParameterUtil.println("\tFirefox Renderer: ");
		ParameterUtil.println(
			"\t\t--useFirefox\n\t\t--script path/to/script/fireprint");
		ParameterUtil.println("\tOpenOffice.org Renderer: ");
		ParameterUtil.println(
			"\t\t--useOOo\n\t\t--hostname hostname\n\t\t--port port nbr");
		ParameterUtil.println("\t[security options]:");
		ParameterUtil.println("\t\t[--password password]");
		ParameterUtil.println("\t\t[--ks keystore file path]");
		ParameterUtil.println("\t\t[--kspassword keystore file password]");
		ParameterUtil.println("\t\t[--keypassword private key password]");
		ParameterUtil.println("\t\t[--cryptreason reason]");
		ParameterUtil.println("\t\t[--cryptlocation location]");
		if (message != null) {
			System.exit(-2);
		} else {
			System.exit(0);
		}
	} // end showUsage()
} // end CSimpleConversion
