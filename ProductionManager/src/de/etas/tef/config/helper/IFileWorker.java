package de.etas.tef.config.helper;

import java.io.IOException;

public interface IFileWorker extends IWorker
{
	void readFile(String filePath, Object result) throws IOException;
	void writeFile(String filePath, Object result) throws IOException;
}
