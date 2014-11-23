package org.pu.bean;

/**
 * @author kenhe
 * @version $Revision: 1.1 $
 */
public class FolderBean {
	private int fileNumber = 0;
	private int folderNumber = 0;
	private long spaceUsage = 0;

	/**
	 * @return Returns the fileNumber.
	 */
	public int getFileNumber() {
		return fileNumber;
	}

	/**
	 * @param fileNumber The fileNumber to set.
	 */
	public void setFileNumber(int fileNumber) {
		this.fileNumber = fileNumber;
	}

	/**
	 * @return Returns the folderNumber.
	 */
	public int getFolderNumber() {
		return folderNumber;
	}

	/**
	 * @param folderNumber The folderNumber to set.
	 */
	public void setFolderNumber(int folderNumber) {
		this.folderNumber = folderNumber;
	}

	/**
	 * @return Returns the spaceUsage.
	 */
	public long getSpaceUsage() {
		return spaceUsage;
	}

	/**
	 * @param spaceUsage The spaceUsage to set.
	 */
	public void setSpaceUsage(long spaceUsage) {
		this.spaceUsage = spaceUsage;
	}

	public String toString() {
		return "file#=" + fileNumber + "&folder#=" + folderNumber + "&spaceusage=" + spaceUsage;
	}
}
