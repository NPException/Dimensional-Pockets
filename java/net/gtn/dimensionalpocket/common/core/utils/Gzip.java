/**
 * (C) 2015 NPException
 */
package net.gtn.dimensionalpocket.common.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;


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

		try (GZIPInputStream zi = new GZIPInputStream(new ByteArrayInputStream(bytes))) {
			result = IOUtils.toString(zi, "UTF-8");
		}
		return result;
	}
}
