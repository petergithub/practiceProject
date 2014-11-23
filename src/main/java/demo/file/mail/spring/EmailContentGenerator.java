package demo.file.mail.spring;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Use the freemarker to generate email.
 */
public class EmailContentGenerator {

	protected static final Logger logger = Logger
			.getLogger(EmailContentGenerator.class);
	private String encode;
	private String templateDir;
	private String previewHtmlFileDir;
	private Configuration freemarkerCfg = null;

	/**
	 * get the configuration file of fm.
	 */
	protected Configuration getFreemarkerCfg() {
		if (null == freemarkerCfg) {
			freemarkerCfg = new Configuration();
			freemarkerCfg.setDefaultEncoding(encode);
			freemarkerCfg.setClassForTemplateLoading(this.getClass(),
					templateDir);
		}
		return freemarkerCfg;
	}

	public String preview(Map<?, ?> propMap, String htmlFileName,
			String templateFile) {
		if (geneHtmlFile(templateFile, propMap, htmlFileName)) {
			return previewHtmlFileDir + "/" + htmlFileName;
		} else {
			return null;
		}
	}

	private boolean geneHtmlFile(String templateFileName, Map<?, ?> propMap,
			String htmlFileName) {
		String sRootDir = getAbsolutePath();
		try {
			Template t = getFreemarkerCfg().getTemplate(templateFileName);
			creatDirs(sRootDir, previewHtmlFileDir);
			File afile = new File(sRootDir + "/" + previewHtmlFileDir + "/"
					+ htmlFileName);
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(afile), encode));
			t.process(propMap, out);
		} catch (TemplateException e) {
			logger.error("Error while processing FreeMarker template "
					+ templateFileName, e);
			return false;
		} catch (IOException e) {
			logger.error(
					("Error while generate Static Html File " + htmlFileName),
					e);
			return false;
		}
		return true;
	}

	private String getAbsolutePath() {
		File f = new File("");
		return f.getAbsolutePath();
	}

	private boolean creatDirs(String aParentDir, String aSubDir) {
		File aFile = new File(aParentDir);
		if (aFile.exists()) {
			File aSubFile = new File(aParentDir + "/" + aSubDir);
			if (!aSubFile.exists()) {
				return aSubFile.mkdirs();
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public String generateEmailContent(Map<?, ?> propMap, String templateFile) {
		String result = null;
		try {
			Template t = getFreemarkerCfg().getTemplate(templateFile);
			result = FreeMarkerTemplateUtils.processTemplateIntoString(t,
					propMap);
		} catch (TemplateException e) {
			logger.error("Error while processing FreeMarker template ", e);
		} catch (IOException e) {
			logger.error("Error while generate Email Content ", e);
		}
		return result;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public String getPreviewHtmlFileDir() {
		return previewHtmlFileDir;
	}

	public void setPreviewHtmlFileDir(String htmlFileDir) {
		this.previewHtmlFileDir = htmlFileDir;
	}

	public String getTemplateDir() {
		return templateDir;
	}

	public void setTemplateDir(String template) {
		this.templateDir = template;
	}
}
