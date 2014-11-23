package demo.web.pageutil;

/**
 * Class/Interface Description Here
 * 
 * @author woody
 */
public class ResultPageInfo {
	private static int DEFAULT_PAGE_SIZE = 5;
	private int m_curPageNo;
	private int m_pageSize;
	private int m_totalRecords;

	public ResultPageInfo(String _pageSize, int _totalRecords) {
		m_curPageNo = (_totalRecords > 0) ? 1 : 0;

		String sPageSize = _pageSize;
		if (sPageSize == null) {
			m_pageSize = DEFAULT_PAGE_SIZE;
		} else {
			try {
				m_pageSize = Integer.parseInt(sPageSize);
			} catch (Exception e) {
				m_pageSize = DEFAULT_PAGE_SIZE;
			}
		}

		m_totalRecords = _totalRecords;
	}

	/**
	 * Go to page number
	 * 
	 * @param gotoPageNumber int
	 */
	public void gotoPage(int gotoPageNumber) {
		if (m_totalRecords > 0) {
			m_curPageNo = gotoPageNumber;
		}
	}

	/**
	 * @return Returns the pageSize.
	 */
	public int getPageSize() {
		return m_pageSize;
	}

	/**
	 * @param _pageSize The pageSize to set.
	 */
	public void setPageSize(int _pageSize) {
		m_pageSize = _pageSize;
	}

	/**
	 * @return Returns the totalRecords.
	 */
	public int getTotalRecords() {
		return m_totalRecords;
	}

	/**
	 * @param _totalRecords The totalRecords to set.
	 */
	public void setTotalRecords(int _totalRecords) {
		m_totalRecords = _totalRecords;
	}

	/**
	 * @return Returns the curPageNo.
	 */
	public int getCurPageNo() {
		return m_curPageNo;
	}

	// ----------------------------------------------------
	public boolean hasNext() {
		if (m_totalRecords <= 0) return false;

		if (m_curPageNo >= getTotalPageNumber()) return false;

		return true;
	}

	public boolean hasPrevious() {
		return (m_curPageNo > 1);
	}

	public void next() {
		if (getTotalPageNumber() > m_curPageNo) {
			m_curPageNo++;
		}
		// stay on the last page;
	}

	public void previous() {
		if (m_curPageNo > 1) m_curPageNo--;
		// stay on the first page;
	}

	public int getPageStartPosition() {
		if (m_curPageNo <= 0) return 0;

		return (m_curPageNo - 1) * m_pageSize + 1;
	}

	public int getTotalPageNumber() {
		if (m_totalRecords <= 0) return 0;
		// else
		return ((m_totalRecords % m_pageSize) == 0 ? (m_totalRecords / m_pageSize)
				: (m_totalRecords / m_pageSize) + 1);
	}

	public int getPageEndPosition() {
		if (m_curPageNo == getTotalPageNumber()) return m_totalRecords;

		return m_curPageNo * m_pageSize;
	}

	public String drawPageControl() {
		StringBuffer sb = new StringBuffer();
		sb.append("<select class=\"DropDown\" name=\"pageSel\" onChange=\"jumpPage();\">");
		if (m_totalRecords > 0) {
			int totalPage = getTotalPageNumber();
			for (int i = 1; i <= totalPage; i++) {
				if (m_curPageNo == i) {
					sb.append("<option value=").append(i).append(" selected>")
							.append(i).append("</option>");
				} else {
					sb.append("<option value=").append(i).append('>').append(i)
							.append("</option>");
				}
			}
		}
		sb.append("</select>");
		return sb.toString();
	}

	public void refresh() {
		if (m_curPageNo >= 1) {
			if ((m_pageSize * (m_curPageNo - 1)) >= m_totalRecords) {
				m_curPageNo--;
			}
		}
	}

}
