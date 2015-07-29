/**
 * (C) 2015 NPException
 */
package net.gtn.dimensionalpocket.common.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;


/**
 * Methods taken from
 * http://lifelongprogrammer.blogspot.de/2013/11/java-use-zip-
 * stream-and-base64-to-compress-big-string.html and slightly modified
 *
 * @author NPException
 *
 */
public class Gzip {

	public static String compressToBase64(String text)
			throws IOException {
		ByteArrayOutputStream rstBao = new ByteArrayOutputStream();
		try (GZIPOutputStream zos = new GZIPOutputStream(rstBao)) {
			zos.write(text.getBytes("UTF-8"));
		}

		byte[] bytes = rstBao.toByteArray();
		return Base64.encodeBase64String(bytes);
	}

	public static String uncompressFromBase64(String compressedBase64)
			throws IOException {
		String result = null;

		byte[] bytes = Base64.decodeBase64(compressedBase64);

		try (GZIPInputStream zi = new GZIPInputStream(new ByteArrayInputStream(bytes));
				StringWriter output = new StringWriter(bytes.length*3);
				InputStreamReader input = new InputStreamReader(zi, "UTF-8");) {

			boolean incomplete = false;
			try {
				int n;
				while (-1 != (n = input.read())) {
					output.write(n);
				}
			} catch (Exception ex) {
				incomplete = true;
			}

			result = (incomplete ? "[INCOMPLETE]\n" : "") + output.toString();
		}
		return result;
	}

	// just to be able to quick test things
	public static void main(String[] args) throws Exception {
		String data = "";
		System.out.println(uncompressFromBase64(data));
	}
}
