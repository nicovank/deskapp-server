package com.oswego.reslife.deskapp.sql;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Objects;

/**
 * This helper class will allow to put SQL queries in separate files,
 * and call them using the filename they are stored in.
 *
 * <p>
 * The given directory will be recursively traversed, and the id of any given file will
 * be its path from the root, with directories concatenated using periods.
 *
 * <p>
 * For example, a file stored in <code>ROOT/init/table.sql</code> can be fetched using
 * <code>getSQLQuery("init.table")</code>.
 */
public class SQLQueryManager {

	// Where the queries and their id will be stored
	private HashMap<String, String> queries = new HashMap<>();

	private static final String FILE_EXTENSION = ".sql";

	/**
	 * Creates a new SQL query manager.
	 *
	 * @param path the path where the SQL queries are stored.
	 * @throws IOException if the given path is not a directory, or cannot be found, or other problem reading the files.
	 */
	public SQLQueryManager(String path) throws IOException {
		// Start loading queries from the root directory.
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try {
			URI absolutePathURI = Objects.requireNonNull(loader.getResource("sql")).toURI();
			Path absolutePath = Paths.get(absolutePathURI);
			walkDirectory(absolutePath, new String[0]);
		} catch (URISyntaxException e) {
			throw new IOException();
		}
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
	private void walkDirectory(Path directory, String[] prefix) throws IOException {

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
			for (Path entry : stream) {
				if (Files.isDirectory(entry)) {
					// Recursively walk through the directories
					String[] newPrefix = new String[prefix.length + 1];
					System.arraycopy(prefix, 0, newPrefix, 0, prefix.length);
					newPrefix[prefix.length] = entry.getFileName().toString();
					walkDirectory(entry, newPrefix);

				} else {
					if (entry.toString().endsWith(FILE_EXTENSION)) {
						String id = entry.getFileName().toString().replace(FILE_EXTENSION, "");

						if (prefix.length != 0) {
							// If there were subdirectories, prepend them to the id.
							id = String.join(".", prefix) + "." + id;
						}

						String content = readFile(entry.toAbsolutePath().toString());
						queries.put(id, content);
					}
				}
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
