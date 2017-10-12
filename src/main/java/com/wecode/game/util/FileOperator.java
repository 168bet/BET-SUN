package com.wecode.game.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.wecode.game.exception.FileExistException;

public class FileOperator {
	private static String rootPath = null;

	static private String getRootPath() {
		if (rootPath == null) {
			rootPath = new FileOperator().getClass().getResource("/../..").getPath();
		}
		return rootPath;
	}

	public static String saveFile(String path, byte[] content) throws IOException,
			FileExistException {
		String savePath = FileOperator.getRootPath() + path;
		File file = new File(savePath);
		if (file.exists()) {
			throw new FileExistException();
		}
		file.createNewFile();
		FileOutputStream out = new FileOutputStream(savePath);
		out.write(content);
		out.close();
		return path;
	}
}
