package de.etas.tef.production.shell;

public interface IShellContentHandler
{
	String getShellCommand();

	String getShellFile();

	void processContent(StringBuffer sb_content);

	void processError(StringBuffer sb_error);
}
