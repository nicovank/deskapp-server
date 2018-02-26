package com.oswego.reslife.deskapp.api.sql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * This helper class will allow to put SQL queries in separate files,
 * and call them using the filename they are stored in.
 *
 * <p>
 * The given directory will be recursively traversed, and the id of any given file will
 * be its path from the root, with directories concatenated using periods.
 *
 * For example, a file stored in <code>ROOT/init/table.sql</code> can be fetched using
 * <code>getSQLQuery("init.table")</code>.
 */
public class SQLQueryManager {

	// Where the queries and their id will be stored
	HashMap<String, String> queries = new HashMap<>();

	private static final String FILE_EXTENSION = ".sql";

	/**
	 * Creates a new SQL query manager.
	 *
	 * @param path the path where the SQL queries are stored.
	 * @throws IOException if the given path is not a directory, or cannot be found, or other problem reading the files.
	 */
	public SQLQueryManager(String path) throws IOException {
		// Start loading queries from the root directory.
		walkDirectory(new File(path), new String[0]);
	}

	/**
	 * Returns the query associated with the given id, or null if it does not exist.
	 *
	 * @param id the id to look up for.
	 * @return the associated query, or null if it does not exist.
	 */
	public String getSQLQuery(String id) {
		return queries.get(id);
	}

	/**
	 * Walks the given directory adding all files ending with the proper extension to the HashMap.
	 * The id is built from the prefix and the filename.
	 *
	 * @param directory The directory to start loading from.
	 * @param prefix    The current prefix of visited directories.
	 * @throws IOException if the given path is not a directory, or cannot be found, or other problem reading the files.
	 */
	private void walkDirectory(File directory, String[] prefix) throws IOException {
		if (!directory.exists()) {
			throw new FileNotFoundException();
		}

		if (!directory.isDirectory()) {
			throw new NotDirectoryException(directory.getAbsolutePath());
		}

		File[] SQLFiles = directory.listFiles(file -> file.getName().endsWith(FILE_EXTENSION));
		File[] subdirectories = directory.listFiles(File::isDirectory);

		if (SQLFiles != null) {
			for (File queryFile : SQLFiles) {

				String id = queryFile.getName().replace(FILE_EXTENSION, "");
				;
				if (prefix.length != 0) {
					// If there were subdirectories, prepend them to the id.
					id = String.join(".", prefix) + "." + id;
				}

				String content = readFile(queryFile.getPath());

				queries.put(id, content);
			}
		}

		if (subdirectories != null) {
			for (File subdirectory : subdirectories) {
				String[] newPrefix = new String[prefix.length + 1];
				System.arraycopy(prefix, 0, newPrefix, 0, prefix.length);
				walkDirectory(subdirectory, newPrefix);
			}
		}
	}

	/**
	 * Reads a file and returns its content as a String decoded with the UTF-8 charset.
	 *
	 * @param path The path to the file to read.
	 * @return the contents of the file.
	 * @throws IOException if there is an error accessing or reading the file.
	 */
	private static String readFile(String path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, "UTF-8");
	}
}
