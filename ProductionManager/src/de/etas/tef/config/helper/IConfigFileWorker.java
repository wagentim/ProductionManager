package de.etas.tef.config.helper;

public interface IConfigFileWorker extends IFileWorker
{
	static final int TAG_FILE_COMMENT_START = 0x00;
	static final int TAG_FILE_COMMENT_FINISH = 0x01;
	
	static final int TAG_BLOCK_COMMENT_START = 0x02;
	static final int TAG_BLOCK_COMMENT_FINISH = 0x03;
	
	static final int TAG_BLOCK_NAME_START = 0x04;
	static final int TAG_BLOCK_NAME_FINISH = 0x05;
	
	static final int TAG_FILE_NEW = 0x06;
}
