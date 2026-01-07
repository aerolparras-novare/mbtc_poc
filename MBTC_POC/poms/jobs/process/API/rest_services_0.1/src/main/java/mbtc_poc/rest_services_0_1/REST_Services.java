
package mbtc_poc.rest_services_0_1;

import routines.DataOperation;
import routines.TalendDataGenerator;
import routines.DataQuality;
import routines.Relational;
import routines.Mathematical;
import routines.DataQualityDependencies;
import routines.SQLike;
import routines.Numeric;
import routines.TalendStringUtil;
import routines.TalendString;
import routines.MDM;
import routines.StringHandling;
import routines.DQTechnical;
import routines.TalendDate;
import routines.DataMasking;
import routines.DqStringHandling;
import routines.system.*;
import routines.system.api.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Comparator;

@SuppressWarnings("unused")

/**
 * Job: REST_Services Purpose: <br>
 * Description: <br>
 * 
 * @author Parras, Tom
 * @version 8.0.1.20260102_0846-patch
 * @status
 */
public class REST_Services implements TalendJob {
	static {
		System.setProperty("TalendJob.log", "REST_Services.log");
	}

	private static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
			.getLogger(REST_Services.class);

	static {
		System.setProperty("talend.component.record.nullable.check", "true");
		String javaUtilLoggingConfigFile = System.getProperty("java.util.logging.config.file");
		if (javaUtilLoggingConfigFile == null) {
			setupDefaultJavaUtilLogging();
		}
	}

	/**
	 * This class replaces the default {@code System.err} stream used by Java Util
	 * Logging (JUL). You can use your own configuration through the
	 * {@code java.util.logging.config.file} system property, enabling you to
	 * specify an external logging configuration file for tailored logging setup.
	 */
	public static class StandardConsoleHandler extends java.util.logging.StreamHandler {
		public StandardConsoleHandler() {
			// Set System.out as default log output stream
			super(System.out, new java.util.logging.SimpleFormatter());
		}

		/**
		 * Publish a {@code LogRecord}. The logging request was made initially to a
		 * {@code Logger} object, which initialized the {@code LogRecord} and forwarded
		 * it here.
		 *
		 * @param record description of the log event. A null record is silently ignored
		 *               and is not published
		 */
		@Override
		public void publish(java.util.logging.LogRecord record) {
			super.publish(record);
			flush();
		}

		/**
		 * Override {@code StreamHandler.close} to do a flush but not to close the
		 * output stream. That is, we do <b>not</b> close {@code System.out}.
		 */
		@Override
		public void close() {
			flush();
		}
	}

	protected static void setupDefaultJavaUtilLogging() {
		java.util.logging.LogManager logManager = java.util.logging.LogManager.getLogManager();

		// Get the root logger
		java.util.logging.Logger rootLogger = logManager.getLogger("");

		// Remove existing handlers to set standard console handler only
		java.util.logging.Handler[] handlers = rootLogger.getHandlers();
		for (java.util.logging.Handler handler : handlers) {
			rootLogger.removeHandler(handler);
		}

		rootLogger.addHandler(new StandardConsoleHandler());
		rootLogger.setLevel(java.util.logging.Level.INFO);
	}

	protected static boolean isCBPClientPresent() {
		boolean isCBPClientPresent = false;
		try {
			Class.forName("org.talend.metrics.CBPClient");
			isCBPClientPresent = true;
		} catch (java.lang.ClassNotFoundException e) {
		}
		return isCBPClientPresent;
	}

	protected static void logIgnoredError(String message, Throwable cause) {
		log.error(message, cause);

	}

	public final Object obj = new Object();

	// for transmiting parameters purpose
	private Object valueObject = null;

	public Object getValueObject() {
		return this.valueObject;
	}

	public void setValueObject(Object valueObject) {
		this.valueObject = valueObject;
	}

	private final static String defaultCharset = java.nio.charset.Charset.defaultCharset().name();

	private final static String utf8Charset = "UTF-8";

	public static String taskExecutionId = null;

	public static String jobExecutionId = java.util.UUID.randomUUID().toString();;

	private final static boolean isCBPClientPresent = isCBPClientPresent();

	public static final java.util.List<Thread> threadList = java.util.Collections
			.synchronizedList(new java.util.ArrayList<>());

	// contains type for every context property
	public class PropertiesWithType extends java.util.Properties {
		private static final long serialVersionUID = 1L;
		private java.util.Map<String, String> propertyTypes = new java.util.HashMap<>();

		public PropertiesWithType(java.util.Properties properties) {
			super(properties);
		}

		public PropertiesWithType() {
			super();
		}

		public void setContextType(String key, String type) {
			propertyTypes.put(key, type);
		}

		public String getContextType(String key) {
			return propertyTypes.get(key);
		}
	}

	// create and load default properties
	private java.util.Properties defaultProps = new java.util.Properties();

	// create application properties with default
	public class ContextProperties extends PropertiesWithType {

		private static final long serialVersionUID = 1L;

		public ContextProperties(java.util.Properties properties) {
			super(properties);
		}

		public ContextProperties() {
			super();
		}

		public void synchronizeContext() {

		}

		// if the stored or passed value is "<TALEND_NULL>" string, it mean null
		public String getStringValue(String key) {
			String origin_value = this.getProperty(key);
			if (NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY.equals(origin_value)) {
				return null;
			}
			return origin_value;
		}

	}

	protected ContextProperties context = new ContextProperties(); // will be instanciated by MS.

	public ContextProperties getContext() {
		return this.context;
	}

	protected java.util.Map<String, String> defaultProperties = new java.util.HashMap<String, String>();
	protected java.util.Map<String, String> additionalProperties = new java.util.HashMap<String, String>();

	public java.util.Map<String, String> getDefaultProperties() {
		return this.defaultProperties;
	}

	public java.util.Map<String, String> getAdditionalProperties() {
		return this.additionalProperties;
	}

	private final String jobVersion = "0.1";
	private final String jobName = "REST_Services";
	private final String projectName = "MBTC_POC";
	public Integer errorCode = null;
	private String currentComponent = "";
	public static boolean isStandaloneMS = Boolean.valueOf("false");

	private void s(final String component) {
		try {
			org.talend.metrics.DataReadTracker.setCurrentComponent(jobName, component);
		} catch (Exception | NoClassDefFoundError e) {
			// ignore
		}
	}

	private void mdc(final String subJobName, final String subJobPidPrefix) {
		mdcInfo.forEach(org.slf4j.MDC::put);
		org.slf4j.MDC.put("_subJobName", subJobName);
		org.slf4j.MDC.put("_subJobPid", subJobPidPrefix + subJobPidCounter.getAndIncrement());
	}

	private void sh(final String componentId) {
		ok_Hash.put(componentId, false);
		start_Hash.put(componentId, System.currentTimeMillis());
	}

	{
		s("none");
	}

	private String cLabel = null;

	private final java.util.Map<String, Object> globalMap = java.util.Collections
			.synchronizedMap(new java.util.HashMap<String, Object>());

	private final java.util.Map<String, Long> start_Hash = new java.util.HashMap<String, Long>();
	private final java.util.Map<String, Long> end_Hash = new java.util.HashMap<String, Long>();
	private final java.util.Map<String, Boolean> ok_Hash = new java.util.HashMap<String, Boolean>();
	public final java.util.List<String[]> globalBuffer = new java.util.ArrayList<String[]>();

	private class DaemonThreadFactory implements java.util.concurrent.ThreadFactory {
		java.util.concurrent.ThreadFactory factory = java.util.concurrent.Executors.defaultThreadFactory();

		public java.lang.Thread newThread(java.lang.Runnable r) {
			java.lang.Thread t = factory.newThread(r);
			t.setDaemon(true);
			return t;
		}
	}

	private final java.util.concurrent.ExecutorService es = java.util.concurrent.Executors.newFixedThreadPool(2,
			new DaemonThreadFactory());
	{
		java.lang.Runtime.getRuntime().addShutdownHook(new java.lang.Thread() {
			public void run() {
				es.shutdown();
				try {
					if (!es.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
						es.shutdownNow();
						if (!es.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {

						}
					}
				} catch (java.lang.InterruptedException ie) {
					es.shutdownNow();
				} catch (java.lang.Exception e) {

				}
			}
		});
	}
	private final JobStructureCatcherUtils talendJobLog = new JobStructureCatcherUtils(jobName,
			"_k3w0kOt_EfCo6J9Ef-vlPw", "0.1");
	private org.talend.job.audit.JobAuditLogger auditLogger_talendJobLog = null;

	private RunStat runStat = new RunStat(talendJobLog, System.getProperty("audit.interval"));

	// OSGi DataSource
	private final static String KEY_DB_DATASOURCES = "KEY_DB_DATASOURCES";

	private final static String KEY_DB_DATASOURCES_RAW = "KEY_DB_DATASOURCES_RAW";

	public void setDataSources(java.util.Map<String, javax.sql.DataSource> dataSources) {
		java.util.Map<String, routines.system.TalendDataSource> talendDataSources = new java.util.HashMap<String, routines.system.TalendDataSource>();
		for (java.util.Map.Entry<String, javax.sql.DataSource> dataSourceEntry : dataSources.entrySet()) {
			talendDataSources.put(dataSourceEntry.getKey(),
					new routines.system.TalendDataSource(dataSourceEntry.getValue()));
		}
		globalMap.put(KEY_DB_DATASOURCES, talendDataSources);
		globalMap.put(KEY_DB_DATASOURCES_RAW, new java.util.HashMap<String, javax.sql.DataSource>(dataSources));
	}

	public void setDataSourceReferences(List serviceReferences) throws Exception {

		java.util.Map<String, routines.system.TalendDataSource> talendDataSources = new java.util.HashMap<String, routines.system.TalendDataSource>();
		java.util.Map<String, javax.sql.DataSource> dataSources = new java.util.HashMap<String, javax.sql.DataSource>();

		for (java.util.Map.Entry<String, javax.sql.DataSource> entry : BundleUtils
				.getServices(serviceReferences, javax.sql.DataSource.class).entrySet()) {
			dataSources.put(entry.getKey(), entry.getValue());
			talendDataSources.put(entry.getKey(), new routines.system.TalendDataSource(entry.getValue()));
		}

		globalMap.put(KEY_DB_DATASOURCES, talendDataSources);
		globalMap.put(KEY_DB_DATASOURCES_RAW, new java.util.HashMap<String, javax.sql.DataSource>(dataSources));
	}

	private final java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
	private final java.io.PrintStream errorMessagePS = new java.io.PrintStream(new java.io.BufferedOutputStream(baos));

	public String getExceptionStackTrace() {
		if ("failure".equals(this.getStatus())) {
			errorMessagePS.flush();
			return baos.toString();
		}
		return null;
	}

	private Exception exception;

	public Exception getException() {
		if ("failure".equals(this.getStatus())) {
			return this.exception;
		}
		return null;
	}

	private class TalendException extends Exception {

		private static final long serialVersionUID = 1L;

		private java.util.Map<String, Object> globalMap = null;
		private Exception e = null;

		private String currentComponent = null;
		private String cLabel = null;

		private String virtualComponentName = null;

		public void setVirtualComponentName(String virtualComponentName) {
			this.virtualComponentName = virtualComponentName;
		}

		private TalendException(Exception e, String errorComponent, final java.util.Map<String, Object> globalMap) {
			this.currentComponent = errorComponent;
			this.globalMap = globalMap;
			this.e = e;
		}

		private TalendException(Exception e, String errorComponent, String errorComponentLabel,
				final java.util.Map<String, Object> globalMap) {
			this(e, errorComponent, globalMap);
			this.cLabel = errorComponentLabel;
		}

		public Exception getException() {
			return this.e;
		}

		public String getCurrentComponent() {
			return this.currentComponent;
		}

		public String getExceptionCauseMessage(Exception e) {
			Throwable cause = e;
			String message = null;
			int i = 10;
			while (null != cause && 0 < i--) {
				message = cause.getMessage();
				if (null == message) {
					cause = cause.getCause();
				} else {
					break;
				}
			}
			if (null == message) {
				message = e.getClass().getName();
			}
			return message;
		}

		@Override
		public void printStackTrace() {
			if (!(e instanceof TalendException || e instanceof TDieException)) {
				if (virtualComponentName != null && currentComponent.indexOf(virtualComponentName + "_") == 0) {
					globalMap.put(virtualComponentName + "_ERROR_MESSAGE", getExceptionCauseMessage(e));
				}
				globalMap.put(currentComponent + "_ERROR_MESSAGE", getExceptionCauseMessage(e));
				System.err.println("Exception in component " + currentComponent + " (" + jobName + ")");
			}
			if (!(e instanceof TDieException)) {
				if (e instanceof TalendException) {
					e.printStackTrace();
				} else {
					e.printStackTrace();
					e.printStackTrace(errorMessagePS);
				}
			}
			if (!(e instanceof TalendException)) {
				REST_Services.this.exception = e;
			}
			if (!(e instanceof TalendException)) {
				try {
					for (java.lang.reflect.Method m : this.getClass().getEnclosingClass().getMethods()) {
						if (m.getName().compareTo(currentComponent + "_error") == 0) {
							m.invoke(REST_Services.this, new Object[] { e, currentComponent, globalMap });
							break;
						}
					}

					if (!(e instanceof TDieException)) {
						if (enableLogStash) {
							talendJobLog.addJobExceptionMessage(currentComponent, cLabel, null, e);
							talendJobLogProcess(globalMap);
						}
					}
				} catch (Exception e) {
					this.e.printStackTrace();
				}
			}
		}
	}

	public void tFlowToIterate_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRESTRequest_1_Loop_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDBInput_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRESTRequest_1_Loop_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tRESTResponse_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRESTRequest_1_Loop_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tExtractJSONFields_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRESTRequest_1_Loop_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tLogRow_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRESTRequest_1_Loop_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDBOutput_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRESTRequest_1_Loop_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tRESTResponse_2_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRESTRequest_1_Loop_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tRESTResponse_3_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRESTRequest_1_Loop_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tMap_1_error(Exception exception, String errorComponent, final java.util.Map<String, Object> globalMap)
			throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRESTRequest_1_Loop_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDBOutput_2_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRESTRequest_1_Loop_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tRESTResponse_4_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRESTRequest_1_Loop_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tRESTRequest_1_Loop_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		tRESTRequest_1_In_error(exception, errorComponent, globalMap);

	}

	public void tRESTRequest_1_In_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRESTRequest_1_Loop_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tHMap_1_THMAP_OUT_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		tHMap_1_THMAP_IN_error(exception, errorComponent, globalMap);

	}

	public void tHMap_1_THMAP_IN_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRESTRequest_1_Loop_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tWriteJSONField_1_Out_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		tWriteJSONField_1_In_error(exception, errorComponent, globalMap);

	}

	public void tWriteJSONField_1_In_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRESTRequest_1_Loop_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tWriteJSONField_2_Out_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		tWriteJSONField_2_In_error(exception, errorComponent, globalMap);

	}

	public void tWriteJSONField_2_In_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRESTRequest_1_Loop_onSubJobError(exception, errorComponent, globalMap);
	}

	public void talendJobLog_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		talendJobLog_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tRESTRequest_1_Loop_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tWriteJSONField_1_In_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tWriteJSONField_2_In_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void talendJobLog_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	private boolean runInTalendEsbRuntimeContainer = false;

	public void setRunInTalendEsbRuntimeContainer(boolean flag) {
		runInTalendEsbRuntimeContainer = flag;
	}

	protected String restEndpoint;

	public void setRestEndpoint(String restEndpoint) {
		this.restEndpoint = restEndpoint;
	}

	public String getRestEndpoint() {
		return "/services";
	}

	private boolean runInDaemonMode = true;

	public void setRunInDaemonMode(boolean flag) {
		runInDaemonMode = flag;
	}

	private boolean restTalendJobAlreadyStarted = false;

	/**
	 * REST provider implementation
	 */
	@jakarta.ws.rs.Path("/")

	public static class RestServiceProviderImpl4TalendJob {

		@jakarta.ws.rs.core.Context
		private org.apache.cxf.jaxrs.ext.MessageContext messageContext;
		private final String setCookieHeader = "Set-Cookie";

		private final REST_Services job;

		public RestServiceProviderImpl4TalendJob() {
			this.job = new REST_Services();
		}

		public RestServiceProviderImpl4TalendJob(REST_Services job) {
			this.job = job;
		}

		private void populateRequestWithJobContext(java.util.Map<String, Object> requestGlobalMap, REST_Services job) {
			// pass job DataSources
			java.util.Map<String, routines.system.TalendDataSource> talendDataSources = (java.util.Map<String, routines.system.TalendDataSource>) job.globalMap
					.get(KEY_DB_DATASOURCES);
			if (null != talendDataSources) {
				java.util.Map<String, routines.system.TalendDataSource> restDataSources = new java.util.HashMap<String, routines.system.TalendDataSource>();
				for (java.util.Map.Entry<String, routines.system.TalendDataSource> talendDataSourceEntry : talendDataSources
						.entrySet()) {
					restDataSources.put(talendDataSourceEntry.getKey(),
							new routines.system.TalendDataSource(talendDataSourceEntry.getValue().getRawDataSource()));
				}
				requestGlobalMap.put(KEY_DB_DATASOURCES, restDataSources);
			}

			if (null != job.globalMap.get(KEY_DB_DATASOURCES_RAW)) {
				requestGlobalMap.put(KEY_DB_DATASOURCES_RAW, job.globalMap.get(KEY_DB_DATASOURCES_RAW));
			}

			// pass job shared connections
			requestGlobalMap.putAll(job.getSharedConnections4REST());

			// pass job concurrent map
			requestGlobalMap.put("concurrentHashMap", job.globalMap.get("concurrentHashMap"));

			requestGlobalMap.putAll(job.globalMap);
		}

		private void closePassedDataSourceConnections(java.util.Map<String, Object> requestGlobalMap) {
			// close connections in passed job DataSources
			try {
				java.util.Map<String, routines.system.TalendDataSource> restDataSources = (java.util.Map<String, routines.system.TalendDataSource>) requestGlobalMap
						.get(KEY_DB_DATASOURCES);
				if (null != restDataSources) {
					for (routines.system.TalendDataSource restDataSource : restDataSources.values()) {
						restDataSource.close();
					}
				}
			} catch (Throwable e) {
				e.printStackTrace(System.err);
			}
		}

		private jakarta.ws.rs.core.Response processRequest(java.util.Map<String, Object> request) {

			final java.util.Map<String, Object> globalMap = java.util.Collections
					.synchronizedMap(new java.util.HashMap<String, Object>());

			try {
				// add CBP code for OSGI Executions
				String taskExecutionId = REST_Services.taskExecutionId;
				String jobExecutionId = REST_Services.jobExecutionId;

				if (taskExecutionId != null && !taskExecutionId.isEmpty()) {
					try {
						org.talend.metrics.DataReadTracker.setExecutionId(taskExecutionId, jobExecutionId, true);
						org.talend.metrics.DataReadTracker.incrementEventCounter();
					} catch (Exception | NoClassDefFoundError e) {
						// ignore
					}
				}

				globalMap.put("restRequest", request);

				populateRequestWithJobContext(globalMap, job);

				job.tRESTRequest_1_LoopProcess(globalMap);

				java.util.Map<String, Object> response = (java.util.Map<String, Object>) globalMap.get("restResponse");

				Object responseBody = null;
				Integer status = null;
				java.util.Map<String, String> headers = null;
				if (null != response) {
					Object dropJsonRootProp = response.get("drop.json.root.element");
					Boolean dropJsonRoot = (null == dropJsonRootProp) ? false : (Boolean) dropJsonRootProp;
					messageContext.put("drop.json.root.element", dropJsonRoot.toString());

					responseBody = response.get("BODY");
					status = (Integer) response.get("STATUS");
					headers = (java.util.Map<String, String>) response.get("HEADERS");
				} else {
					responseBody = request.get("ERROR");
				}

				if (null == status) {
					status = (request.containsKey("STATUS")) ? (Integer) request.get("STATUS") : 404;
				}

				jakarta.ws.rs.core.Response.ResponseBuilder responseBuilder = jakarta.ws.rs.core.Response.status(status)
						.entity(responseBody);
				if (headers != null) {
					for (java.util.Map.Entry<String, String> header : headers.entrySet()) {
						if (header.getKey().equalsIgnoreCase(setCookieHeader)) {
							String cookies = header.getValue().trim();
							String cookiesList[] = cookies.split(";");

							for (String cookie : cookiesList) {
								String cookieTokens[] = cookie.trim().split("=");

								if (cookieTokens.length == 2) {

									String cookieName = cookieTokens[0].trim();
									String cookieValue = cookieTokens[1].trim();

									if (cookieName.length() > 0 && cookieValue.length() > 0) {
										jakarta.ws.rs.core.NewCookie newCookie = new jakarta.ws.rs.core.NewCookie(
												cookieName, cookieValue);
										responseBuilder.cookie(newCookie);
									}
								}
							}
						} else {
							responseBuilder.header(header.getKey(), header.getValue());
						}
					}
				}

				return responseBuilder.build();

			} catch (Throwable ex) {
				ex.printStackTrace();
				throw new jakarta.ws.rs.WebApplicationException(ex, 500);
			} finally {
				// close DB connections
				closePassedDataSourceConnections(globalMap);

				try {
					org.talend.metrics.DataReadTracker.reset();
				} catch (Exception | NoClassDefFoundError e) {
					// ignore
				}
			}
		}

		private jakarta.ws.rs.core.Response processStreamingResponseRequest(
				final java.util.Map<String, Object> request) {

			jakarta.ws.rs.core.StreamingOutput streamingOutput = new jakarta.ws.rs.core.StreamingOutput() {
				public void write(java.io.OutputStream output) {

					final java.util.Map<String, Object> globalMap = java.util.Collections
							.synchronizedMap(new java.util.HashMap<String, Object>());

					try {
						globalMap.put("restResponseStream", output);

						globalMap.put("restRequest", request);

						populateRequestWithJobContext(globalMap, job);

						job.tRESTRequest_1_LoopProcess(globalMap);

						if (globalMap.containsKey("restResponseWrappingClosure")) {
							output.write(((String) globalMap.get("restResponseWrappingClosure")).getBytes());
						}
					} catch (Throwable ex) {
						ex.printStackTrace();
						throw new jakarta.ws.rs.WebApplicationException(ex, 500);
					} finally {
						// close DB connections
						closePassedDataSourceConnections(globalMap);
					}
				}
			};

			return jakarta.ws.rs.core.Response.ok().entity(streamingOutput).build();
		}

		@jakarta.ws.rs.DELETE()

		@jakarta.ws.rs.Path("/customers/{id}")
		@jakarta.ws.rs.Produces({ "application/json" })
		public jakarta.ws.rs.core.Response Delete_a_customer(

				@jakarta.ws.rs.PathParam("id") String id

		) {
			List<String> requiredParameterWithNullValueList = new java.util.ArrayList<String>();
			if (null == id) {
				requiredParameterWithNullValueList.add("id");
			}
			if (requiredParameterWithNullValueList.size() > 0) {
				return handleWrongRequest(messageContext, 400,
						"400 Bad Request \n" + requiredParameterWithNullValueList.toString()
								+ " in tRESTRequest_1:Delete_a_customer "
								+ (requiredParameterWithNullValueList.size() == 1 ? "is required!" : "are required!"));
			}
			java.util.Map<String, Object> request_tRESTRequest_1 = new java.util.HashMap<String, Object>();
			request_tRESTRequest_1.put("VERB", "DELETE");
			request_tRESTRequest_1.put("OPERATION", "Delete_a_customer");
			request_tRESTRequest_1.put("PATTERN", "/customers/{id}");

			populateRequestInfo(request_tRESTRequest_1, messageContext);

			java.util.Map<String, Object> parameters_tRESTRequest_1 = new java.util.HashMap<String, Object>();

			parameters_tRESTRequest_1.put("PATH:id:id_String", id);

			request_tRESTRequest_1.put("PARAMS", parameters_tRESTRequest_1);

			return processRequest(request_tRESTRequest_1);
		}

		@jakarta.ws.rs.POST()

		@jakarta.ws.rs.Path("/customers")
		@jakarta.ws.rs.Consumes({ "application/json" })
		@jakarta.ws.rs.Produces({ "application/json" })
		public jakarta.ws.rs.core.Response Create_a_customer(

		) {
			List<String> requiredParameterWithNullValueList = new java.util.ArrayList<String>();
			if (requiredParameterWithNullValueList.size() > 0) {
				return handleWrongRequest(messageContext, 400,
						"400 Bad Request \n" + requiredParameterWithNullValueList.toString()
								+ " in tRESTRequest_1:Create_a_customer "
								+ (requiredParameterWithNullValueList.size() == 1 ? "is required!" : "are required!"));
			}
			java.util.Map<String, Object> request_tRESTRequest_1 = new java.util.HashMap<String, Object>();
			request_tRESTRequest_1.put("VERB", "POST");
			request_tRESTRequest_1.put("OPERATION", "Create_a_customer");
			request_tRESTRequest_1.put("PATTERN", "/customers");

			populateRequestInfo(request_tRESTRequest_1, messageContext);

			java.util.Map<String, Object> parameters_tRESTRequest_1 = new java.util.HashMap<String, Object>();

			request_tRESTRequest_1.put("PARAMS", parameters_tRESTRequest_1);

			return processRequest(request_tRESTRequest_1);
		}

		@jakarta.ws.rs.GET()

		@jakarta.ws.rs.Path("/customers")
		@jakarta.ws.rs.Produces({ "application/json" })
		public jakarta.ws.rs.core.Response Get_the_list_of_customers(

		) {
			List<String> requiredParameterWithNullValueList = new java.util.ArrayList<String>();
			if (requiredParameterWithNullValueList.size() > 0) {
				return handleWrongRequest(messageContext, 400,
						"400 Bad Request \n" + requiredParameterWithNullValueList.toString()
								+ " in tRESTRequest_1:Get_the_list_of_customers "
								+ (requiredParameterWithNullValueList.size() == 1 ? "is required!" : "are required!"));
			}
			java.util.Map<String, Object> request_tRESTRequest_1 = new java.util.HashMap<String, Object>();
			request_tRESTRequest_1.put("VERB", "GET");
			request_tRESTRequest_1.put("OPERATION", "Get_the_list_of_customers");
			request_tRESTRequest_1.put("PATTERN", "/customers");

			populateRequestInfo(request_tRESTRequest_1, messageContext);

			java.util.Map<String, Object> parameters_tRESTRequest_1 = new java.util.HashMap<String, Object>();

			request_tRESTRequest_1.put("PARAMS", parameters_tRESTRequest_1);

			return processRequest(request_tRESTRequest_1);
		}

		public jakarta.ws.rs.core.Response handleWrongRequest(org.apache.cxf.jaxrs.ext.MessageContext context,
				int status, String error) {

			// System.out.println("wrong call [uri: " + context.getUriInfo().getPath() + " ;
			// method: " + context.getRequest().getMethod() + " ; status: " + status + " ;
			// error: " + error + "]");

			java.util.Map<String, Object> wrongRequest = new java.util.HashMap<String, Object>();

			wrongRequest.put("ERROR", error);
			wrongRequest.put("STATUS", status);
			wrongRequest.put("VERB", context.getRequest().getMethod());
			wrongRequest.put("URI", context.getUriInfo().getPath());
			wrongRequest.put("URI_BASE", context.getUriInfo().getBaseUri().toString());
			wrongRequest.put("URI_ABSOLUTE", context.getUriInfo().getAbsolutePath().toString());
			wrongRequest.put("URI_REQUEST", context.getUriInfo().getRequestUri().toString());

			return processRequest(wrongRequest);
		}

		private void populateRequestInfo(java.util.Map<String, Object> request,
				org.apache.cxf.jaxrs.ext.MessageContext context) {
			final jakarta.ws.rs.core.UriInfo ui = context.getUriInfo();

			request.put("URI", ui.getPath());
			request.put("URI_BASE", ui.getBaseUri().toString());
			request.put("URI_ABSOLUTE", ui.getAbsolutePath().toString());
			request.put("URI_REQUEST", ui.getRequestUri().toString());

			request.put("ALL_HEADER_PARAMS", context.getHttpHeaders().getRequestHeaders());
			request.put("ALL_QUERY_PARAMS", ui.getQueryParameters());

			jakarta.ws.rs.core.SecurityContext securityContext = context.getSecurityContext();
			if (null != securityContext && null != securityContext.getUserPrincipal()) {
				request.put("PRINCIPAL_NAME", securityContext.getUserPrincipal().getName());
			}

			request.put("CorrelationID", context.get("CorrelationID"));

			request.put("MESSAGE_CONTEXT", context);
		}

		private void populateMultipartRequestInfo(java.util.Map<String, Object> request,
				org.apache.cxf.jaxrs.ext.MessageContext context, java.util.Map<String, Boolean> partNames) {
			java.util.Map<String, String> attachmentFilenames = new java.util.HashMap<String, String>();

			java.util.Map<String, java.util.Map<String, java.util.List<String>>> attachmentHeaders = new java.util.HashMap<String, java.util.Map<String, java.util.List<String>>>();

			for (java.util.Map.Entry<String, Boolean> p : partNames.entrySet()) {
				String partName = p.getKey();
				Boolean optional = p.getValue();
				org.apache.cxf.jaxrs.ext.multipart.Attachment attachment = getFirstMatchingPart(context, partName,
						optional);
				if (null != attachment) {
					attachmentHeaders.put(partName, attachment.getHeaders());
					if (null != attachment.getContentDisposition()) {
						String filename = attachment.getContentDisposition().getParameter("filename");
						if (null != filename) {
							attachmentFilenames.put(partName, filename);
						}
					}
				}
			}

			request.put("ATTACHMENT_HEADERS", attachmentHeaders);
			request.put("ATTACHMENT_FILENAMES", attachmentFilenames);
		}

		private static org.apache.cxf.jaxrs.ext.multipart.Attachment getFirstMatchingPart(
				org.apache.cxf.jaxrs.ext.MessageContext context, String partName, Boolean optional) {
			List<org.apache.cxf.jaxrs.ext.multipart.Attachment> attachments = org.apache.cxf.jaxrs.utils.multipart.AttachmentUtils
					.getAttachments(context);
			for (org.apache.cxf.jaxrs.ext.multipart.Attachment attachment : attachments) {
				if (partName.equals(attachment.getContentId())) {
					return attachment;
				}
				org.apache.cxf.jaxrs.ext.multipart.ContentDisposition cd = attachment.getContentDisposition();
				if (null != cd && partName.equals(cd.getParameter("name"))) {
					return attachment;
				}
			}
			if (optional) {
				return null;
			}
			// unexpected
			throw new jakarta.ws.rs.InternalServerErrorException();
		}
	}

	@jakarta.ws.rs.ext.Provider
	@jakarta.ws.rs.container.PreMatching
	@jakarta.annotation.Priority(1)
	public class RequestCounterFilter implements jakarta.ws.rs.container.ContainerRequestFilter {

		@Override
		public void filter(jakarta.ws.rs.container.ContainerRequestContext requestContext) throws java.io.IOException {
			try {
				org.talend.metrics.DataReadTracker.incrementEventCounter();
			} catch (Exception | NoClassDefFoundError e) {
				// ignore
			}
		}
	}

	public static class ExceptionMapper4TalendJobRestService
			extends org.apache.cxf.jaxrs.impl.WebApplicationExceptionMapper {

		@jakarta.ws.rs.core.Context
		private org.apache.cxf.jaxrs.ext.MessageContext messageContext;

		private RestServiceProviderImpl4TalendJob provider;

		public ExceptionMapper4TalendJobRestService(RestServiceProviderImpl4TalendJob provider) {
			this.provider = provider;
		}

		public jakarta.ws.rs.core.Response toResponse(jakarta.ws.rs.WebApplicationException ex) {
			String error = null;
			jakarta.ws.rs.core.Response response = ex.getResponse();
			if (null != response && null != response.getEntity()) {
				error = response.getEntity().toString();
			}
			response = super.toResponse(ex);
			if (null == error) {
				if (null != response && null != response.getEntity()) {
					error = response.getEntity().toString();
				} else {
					error = null == ex.getCause() ? ex.getMessage() : ex.getCause().getMessage();
				}
			}
			response = provider.handleWrongRequest(messageContext, response.getStatus(), error);

			java.util.List<jakarta.ws.rs.core.MediaType> accepts = messageContext.getHttpHeaders()
					.getAcceptableMediaTypes();
			jakarta.ws.rs.core.MediaType responseType = accepts.isEmpty() ? null : accepts.get(0);

			if (responseType == null
					|| !responseType.getSubtype().equals("xml") && !responseType.getSubtype().equals("json")) {
				responseType = jakarta.ws.rs.core.MediaType.APPLICATION_XML_TYPE;
			}

			jakarta.ws.rs.core.Response r = jakarta.ws.rs.core.Response.status(response.getStatus())
					.entity(response.getEntity()).type(responseType).build();

			if (response.getHeaders() != null) {
				r.getHeaders().putAll(response.getHeaders());
			}

			return r;
		}
	}

	protected String checkEndpointUrl(String url) {

		final String defaultEndpointUrl = "http://127.0.0.1:8090/";

		String endpointUrl = url;
		if (null == endpointUrl || endpointUrl.trim().isEmpty()) {
			endpointUrl = defaultEndpointUrl;
		} else if (!endpointUrl.contains("://")) { // relative
			if (endpointUrl.startsWith("/")) {
				endpointUrl = endpointUrl.substring(1);
			}
			endpointUrl = defaultEndpointUrl + endpointUrl;
		}

		// test for busy
		java.net.URI endpointURI = java.net.URI.create(endpointUrl);
		String host = endpointURI.getHost();
		try {
			if (java.net.InetAddress.getByName(host).isLoopbackAddress()) {
				int port = endpointURI.getPort();
				java.net.ServerSocket ss = null;
				try {
					ss = new java.net.ServerSocket(port);
				} catch (IOException e) {
					// rethrow exception
					throw new IllegalArgumentException(
							"Cannot start provider with uri: " + endpointUrl + ". Port " + port + " already in use.");
				} finally {
					if (ss != null) {
						try {
							ss.close();
						} catch (IOException e) {
							// ignore
						}
					}
				}
				try {
					// ok, let's doublecheck for silent listeners
					java.net.Socket cs = new java.net.Socket(host, port);
					// if succeed - somebody silently listening, fail!
					cs.close();
					// rethrow exception
					throw new IllegalArgumentException(
							"Cannot start provider with uri: " + endpointUrl + ". Port " + port + " already in use.");
				} catch (IOException e) {
					// ok, nobody listens, proceed
				}
			}
		} catch (java.net.UnknownHostException e) {
			// ignore
		}

		return endpointUrl;
	}

	private String evaluateURL(String url) {
		if (url != null && !url.trim().isEmpty() && url.contains("://")) {
			return url;
		}

		if (!url.startsWith("/")) {
			url = "/" + url;
		}

		String servletContextPath = System.getProperty("microservice.server.servlet.context-path");
		String protocol = System.getProperty("microservice.server.protocol");
		String host = System.getProperty("microservice.server.host");
		String port = System.getProperty("microservice.server.port");
		String address = String.format("%s://%s:%s", (null != protocol && !protocol.isEmpty()) ? protocol : "http",
				(null != host && !host.isEmpty()) ? host : "localhost",
				(null != port && !port.isEmpty()) ? port : "8090");

		if (null != servletContextPath && servletContextPath.endsWith("/") && null != url && url.startsWith("/")) {
			servletContextPath = servletContextPath.substring(0, servletContextPath.length() - 1);
		}

		return address + (null != servletContextPath ? servletContextPath : "") + (null != url ? url : "");
	}

	public static class StreamingDOM4JProvider extends org.apache.cxf.jaxrs.provider.dom4j.DOM4JProvider {

		public static final String SUPRESS_XML_DECLARATION = "supress.xml.declaration";

		private java.util.Map<String, Object> globalMap = null;

		public void setGlobalMap(java.util.Map<String, Object> globalMap) {
			this.globalMap = globalMap;
		}

		public void writeTo(org.dom4j.Document doc, Class<?> cls, java.lang.reflect.Type type,
				java.lang.annotation.Annotation[] anns, jakarta.ws.rs.core.MediaType mt,
				jakarta.ws.rs.core.MultivaluedMap<String, Object> headers, java.io.OutputStream os)
				throws java.io.IOException, jakarta.ws.rs.WebApplicationException {
			if (mt.getSubtype().contains("xml")) {
				org.dom4j.io.XMLWriter writer;
				org.apache.cxf.message.Message currentMessage = null;
				if (org.apache.cxf.jaxrs.utils.JAXRSUtils.getCurrentMessage() != null) {
					currentMessage = org.apache.cxf.jaxrs.utils.JAXRSUtils.getCurrentMessage();
				} else {
					currentMessage = (org.apache.cxf.message.Message) ((java.util.Map<String, Object>) globalMap
							.get("restRequest")).get("CURRENT_MESSAGE");
				}

				if (currentMessage != null && currentMessage.getExchange() != null
						&& currentMessage.getExchange().containsKey(SUPRESS_XML_DECLARATION)) {
					org.dom4j.io.OutputFormat format = new org.dom4j.io.OutputFormat();
					format.setSuppressDeclaration(true);
					writer = new org.dom4j.io.XMLWriter(os, format);
				} else {
					writer = new org.dom4j.io.XMLWriter(os);
				}
				writer.write(doc);
				writer.flush();
			} else {
				super.writeTo(doc, cls, type, anns, mt, headers, os);
			}
		}
	}

	Thread4RestServiceProviderEndpoint thread4RestServiceProviderEndpoint = null;

	class Thread4RestServiceProviderEndpoint extends Thread {

		private final String endpointUrl;

		private final REST_Services job;

		private org.apache.cxf.endpoint.Server server;

		private org.apache.cxf.jaxrs.JAXRSServerFactoryBean sf;

		public Thread4RestServiceProviderEndpoint(REST_Services job, String endpointUrl) {
			this.job = job;
			this.endpointUrl = endpointUrl;
			this.sf = new org.apache.cxf.jaxrs.JAXRSServerFactoryBean();
		}

		boolean inOSGi = routines.system.BundleUtils.inOSGi();

		public org.apache.cxf.endpoint.Server getServer() {
			return server;
		}

		public org.apache.cxf.jaxrs.JAXRSServerFactoryBean getJAXRSServerFactoryBean() {
			return sf;
		}

		public void run() {

			try {
				RestServiceProviderImpl4TalendJob provider = new RestServiceProviderImpl4TalendJob(job);

				if (sf.getProperties() == null) {
					sf.setProperties(new java.util.HashMap<String, Object>());
				}

				java.util.List<Object> providers = (java.util.List<Object>) sf.getProviders();
				providers.add(new RequestCounterFilter());
				providers.add(new ExceptionMapper4TalendJobRestService(provider));
				providers.add(new StreamingDOM4JProvider());

				// begin of JWT/Basic security
				boolean needCheckComponentSecurity = true;

				// component JWT logic, begin
				if (needCheckComponentSecurity) {

					// component JWT logic, end
				}
				// end of JWT/Basic security

				org.apache.cxf.jaxrs.provider.json.JSONProvider jsonProvider = new org.apache.cxf.jaxrs.provider.json.JSONProvider();
				jsonProvider.setIgnoreNamespaces(true);

				jsonProvider.setAttributesToElements(true);

				jsonProvider.setConvertTypesToStrings(false);

				providers.add(jsonProvider);

				if (needCheckComponentSecurity) {

				}

				sf.setProviders(providers);
				sf.setTransportId("http://cxf.apache.org/transports/http");
				sf.setResourceClasses(RestServiceProviderImpl4TalendJob.class);
				sf.setResourceProvider(RestServiceProviderImpl4TalendJob.class,
						new org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider(provider));

				sf.setAddress(endpointUrl);

				final java.util.List<org.apache.cxf.feature.Feature> features = sf.getFeatures() == null
						? new java.util.ArrayList<org.apache.cxf.feature.Feature>()
						: sf.getFeatures();

				boolean needExposeOpenApiFeature = true;
				if (needExposeOpenApiFeature) {
				} // needExposeOpenApiFeature end

				sf.setFeatures(features);

				server = sf.create();

				// System.out.println("REST service [endpoint: " + endpointUrl + "] published");
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		public void stopEndpoint() {
			if (null != server) {
				server.stop();
				server.destroy();
				// System.out.println("REST service [endpoint: " + endpointUrl + "]
				// unpublished");
			}
		}
	}

	public static class ContextBean {
		static String evaluate(String context, String contextExpression, String jobName)
				throws IOException, javax.script.ScriptException {
			String currentContext = context;
			String jobNameStripped = jobName.substring(jobName.lastIndexOf(".") + 1);

			boolean inOSGi = routines.system.BundleUtils.inOSGi();
			java.util.Dictionary<String, Object> jobProperties = null;
			if (inOSGi) {
				jobProperties = routines.system.BundleUtils.getJobProperties(jobNameStripped);

				if (jobProperties != null && null != jobProperties.get("context")) {
					currentContext = (String) jobProperties.get("context");
				}
			}

			boolean isExpression = contextExpression.contains("+") || contextExpression.contains("(");
			final String prefix = isExpression ? "\"" : "";
			java.util.Properties defaultProps = new java.util.Properties();
			java.io.InputStream inContext = REST_Services.class.getClassLoader()
					.getResourceAsStream("mbtc_poc/rest_services_0_1/contexts/" + currentContext + ".properties");
			if (inContext == null) {
				inContext = REST_Services.class.getClassLoader()
						.getResourceAsStream("config/contexts/" + currentContext + ".properties");
			}
			try {
				defaultProps.load(inContext);
				if (jobProperties != null) {
					java.util.Enumeration<String> keys = jobProperties.keys();
					while (keys.hasMoreElements()) {
						String propKey = keys.nextElement();
						if (defaultProps.containsKey(propKey)) {
							defaultProps.put(propKey, (String) jobProperties.get(propKey));
						}
					}
				}
			} finally {
				inContext.close();
			}
			java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("context.([\\w]+)");
			java.util.regex.Matcher matcher = pattern.matcher(contextExpression);

			while (matcher.find()) {
				contextExpression = contextExpression.replaceAll(matcher.group(0),
						prefix + defaultProps.getProperty(matcher.group(1)) + prefix);
			}
			if (contextExpression.startsWith("/services")) {
				contextExpression = contextExpression.replaceFirst("/services", "");
			}
			return isExpression ? evaluateContextExpression(contextExpression) : contextExpression;
		}

		public static String evaluateContextExpression(String expression) throws RuntimeException {
			delight.rhinosandox.RhinoSandbox sandbox = delight.rhinosandox.RhinoSandboxes.create();

			// Add some import for Java
			expression = expression.replaceAll("System.getProperty", "java.lang.System.getProperty");
			return sandbox.eval(null, expression).toString();
		}

		public static String getContext(String context, String contextName, String jobName) throws Exception {
			return contextName.contains("context.") ? evaluate(context, contextName, jobName) : contextName;
		}
	}

	public static class row7Struct implements routines.system.IPersistableRow<row7Struct> {
		final static byte[] commonByteArrayLock_MBTC_POC_REST_Services = new byte[0];
		static byte[] commonByteArray_MBTC_POC_REST_Services = new byte[0];
		protected static final int DEFAULT_HASHCODE = 1;
		protected static final int PRIME = 31;
		protected int hashCode = DEFAULT_HASHCODE;
		public boolean hashCodeDirty = true;

		public String loopKey;

		public String ID;

		public String getID() {
			return this.ID;
		}

		public Boolean IDIsNullable() {
			return false;
		}

		public Boolean IDIsKey() {
			return true;
		}

		public Integer IDLength() {
			return 255;
		}

		public Integer IDPrecision() {
			return 0;
		}

		public String IDDefault() {

			return null;

		}

		public String IDComment() {

			return "";

		}

		public String IDPattern() {

			return "";

		}

		public String IDOriginalDbColumnName() {

			return "ID";

		}

		public String NAME;

		public String getNAME() {
			return this.NAME;
		}

		public Boolean NAMEIsNullable() {
			return true;
		}

		public Boolean NAMEIsKey() {
			return false;
		}

		public Integer NAMELength() {
			return 255;
		}

		public Integer NAMEPrecision() {
			return 0;
		}

		public String NAMEDefault() {

			return null;

		}

		public String NAMEComment() {

			return "";

		}

		public String NAMEPattern() {

			return "";

		}

		public String NAMEOriginalDbColumnName() {

			return "NAME";

		}

		public String LAST_NAME;

		public String getLAST_NAME() {
			return this.LAST_NAME;
		}

		public Boolean LAST_NAMEIsNullable() {
			return true;
		}

		public Boolean LAST_NAMEIsKey() {
			return false;
		}

		public Integer LAST_NAMELength() {
			return 255;
		}

		public Integer LAST_NAMEPrecision() {
			return 0;
		}

		public String LAST_NAMEDefault() {

			return null;

		}

		public String LAST_NAMEComment() {

			return "";

		}

		public String LAST_NAMEPattern() {

			return "";

		}

		public String LAST_NAMEOriginalDbColumnName() {

			return "LAST_NAME";

		}

		public String EMAIL;

		public String getEMAIL() {
			return this.EMAIL;
		}

		public Boolean EMAILIsNullable() {
			return true;
		}

		public Boolean EMAILIsKey() {
			return false;
		}

		public Integer EMAILLength() {
			return 255;
		}

		public Integer EMAILPrecision() {
			return 0;
		}

		public String EMAILDefault() {

			return null;

		}

		public String EMAILComment() {

			return "";

		}

		public String EMAILPattern() {

			return "";

		}

		public String EMAILOriginalDbColumnName() {

			return "EMAIL";

		}

		public String JOB_TITLE;

		public String getJOB_TITLE() {
			return this.JOB_TITLE;
		}

		public Boolean JOB_TITLEIsNullable() {
			return true;
		}

		public Boolean JOB_TITLEIsKey() {
			return false;
		}

		public Integer JOB_TITLELength() {
			return 255;
		}

		public Integer JOB_TITLEPrecision() {
			return 0;
		}

		public String JOB_TITLEDefault() {

			return null;

		}

		public String JOB_TITLEComment() {

			return "";

		}

		public String JOB_TITLEPattern() {

			return "";

		}

		public String JOB_TITLEOriginalDbColumnName() {

			return "JOB_TITLE";

		}

		public String CREDITCARDNUMBER;

		public String getCREDITCARDNUMBER() {
			return this.CREDITCARDNUMBER;
		}

		public Boolean CREDITCARDNUMBERIsNullable() {
			return true;
		}

		public Boolean CREDITCARDNUMBERIsKey() {
			return false;
		}

		public Integer CREDITCARDNUMBERLength() {
			return 255;
		}

		public Integer CREDITCARDNUMBERPrecision() {
			return 0;
		}

		public String CREDITCARDNUMBERDefault() {

			return null;

		}

		public String CREDITCARDNUMBERComment() {

			return "";

		}

		public String CREDITCARDNUMBERPattern() {

			return "";

		}

		public String CREDITCARDNUMBEROriginalDbColumnName() {

			return "CREDITCARDNUMBER";

		}

		public String COMPANY;

		public String getCOMPANY() {
			return this.COMPANY;
		}

		public Boolean COMPANYIsNullable() {
			return true;
		}

		public Boolean COMPANYIsKey() {
			return false;
		}

		public Integer COMPANYLength() {
			return 255;
		}

		public Integer COMPANYPrecision() {
			return 0;
		}

		public String COMPANYDefault() {

			return null;

		}

		public String COMPANYComment() {

			return "";

		}

		public String COMPANYPattern() {

			return "";

		}

		public String COMPANYOriginalDbColumnName() {

			return "COMPANY";

		}

		public String CITY;

		public String getCITY() {
			return this.CITY;
		}

		public Boolean CITYIsNullable() {
			return true;
		}

		public Boolean CITYIsKey() {
			return false;
		}

		public Integer CITYLength() {
			return 255;
		}

		public Integer CITYPrecision() {
			return 0;
		}

		public String CITYDefault() {

			return null;

		}

		public String CITYComment() {

			return "";

		}

		public String CITYPattern() {

			return "";

		}

		public String CITYOriginalDbColumnName() {

			return "CITY";

		}

		public String STATE;

		public String getSTATE() {
			return this.STATE;
		}

		public Boolean STATEIsNullable() {
			return true;
		}

		public Boolean STATEIsKey() {
			return false;
		}

		public Integer STATELength() {
			return 255;
		}

		public Integer STATEPrecision() {
			return 0;
		}

		public String STATEDefault() {

			return null;

		}

		public String STATEComment() {

			return "";

		}

		public String STATEPattern() {

			return "";

		}

		public String STATEOriginalDbColumnName() {

			return "STATE";

		}

		@Override
		public int hashCode() {
			if (this.hashCodeDirty) {
				final int prime = PRIME;
				int result = DEFAULT_HASHCODE;

				result = prime * result + ((this.ID == null) ? 0 : this.ID.hashCode());

				this.hashCode = result;
				this.hashCodeDirty = false;
			}
			return this.hashCode;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final row7Struct other = (row7Struct) obj;

			if (this.ID == null) {
				if (other.ID != null)
					return false;

			} else if (!this.ID.equals(other.ID))

				return false;

			return true;
		}

		public void copyDataTo(row7Struct other) {

			other.ID = this.ID;
			other.NAME = this.NAME;
			other.LAST_NAME = this.LAST_NAME;
			other.EMAIL = this.EMAIL;
			other.JOB_TITLE = this.JOB_TITLE;
			other.CREDITCARDNUMBER = this.CREDITCARDNUMBER;
			other.COMPANY = this.COMPANY;
			other.CITY = this.CITY;
			other.STATE = this.STATE;

		}

		public void copyKeysDataTo(row7Struct other) {

			other.ID = this.ID;

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.ID = readString(dis);

					this.NAME = readString(dis);

					this.LAST_NAME = readString(dis);

					this.EMAIL = readString(dis);

					this.JOB_TITLE = readString(dis);

					this.CREDITCARDNUMBER = readString(dis);

					this.COMPANY = readString(dis);

					this.CITY = readString(dis);

					this.STATE = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.ID = readString(dis);

					this.NAME = readString(dis);

					this.LAST_NAME = readString(dis);

					this.EMAIL = readString(dis);

					this.JOB_TITLE = readString(dis);

					this.CREDITCARDNUMBER = readString(dis);

					this.COMPANY = readString(dis);

					this.CITY = readString(dis);

					this.STATE = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.ID, dos);

				// String

				writeString(this.NAME, dos);

				// String

				writeString(this.LAST_NAME, dos);

				// String

				writeString(this.EMAIL, dos);

				// String

				writeString(this.JOB_TITLE, dos);

				// String

				writeString(this.CREDITCARDNUMBER, dos);

				// String

				writeString(this.COMPANY, dos);

				// String

				writeString(this.CITY, dos);

				// String

				writeString(this.STATE, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// String

				writeString(this.ID, dos);

				// String

				writeString(this.NAME, dos);

				// String

				writeString(this.LAST_NAME, dos);

				// String

				writeString(this.EMAIL, dos);

				// String

				writeString(this.JOB_TITLE, dos);

				// String

				writeString(this.CREDITCARDNUMBER, dos);

				// String

				writeString(this.COMPANY, dos);

				// String

				writeString(this.CITY, dos);

				// String

				writeString(this.STATE, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("ID=" + ID);
			sb.append(",NAME=" + NAME);
			sb.append(",LAST_NAME=" + LAST_NAME);
			sb.append(",EMAIL=" + EMAIL);
			sb.append(",JOB_TITLE=" + JOB_TITLE);
			sb.append(",CREDITCARDNUMBER=" + CREDITCARDNUMBER);
			sb.append(",COMPANY=" + COMPANY);
			sb.append(",CITY=" + CITY);
			sb.append(",STATE=" + STATE);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (ID == null) {
				sb.append("<null>");
			} else {
				sb.append(ID);
			}

			sb.append("|");

			if (NAME == null) {
				sb.append("<null>");
			} else {
				sb.append(NAME);
			}

			sb.append("|");

			if (LAST_NAME == null) {
				sb.append("<null>");
			} else {
				sb.append(LAST_NAME);
			}

			sb.append("|");

			if (EMAIL == null) {
				sb.append("<null>");
			} else {
				sb.append(EMAIL);
			}

			sb.append("|");

			if (JOB_TITLE == null) {
				sb.append("<null>");
			} else {
				sb.append(JOB_TITLE);
			}

			sb.append("|");

			if (CREDITCARDNUMBER == null) {
				sb.append("<null>");
			} else {
				sb.append(CREDITCARDNUMBER);
			}

			sb.append("|");

			if (COMPANY == null) {
				sb.append("<null>");
			} else {
				sb.append(COMPANY);
			}

			sb.append("|");

			if (CITY == null) {
				sb.append("<null>");
			} else {
				sb.append(CITY);
			}

			sb.append("|");

			if (STATE == null) {
				sb.append("<null>");
			} else {
				sb.append(STATE);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row7Struct other) {

			int returnValue = -1;

			returnValue = checkNullsAndCompare(this.ID, other.ID);
			if (returnValue != 0) {
				return returnValue;
			}

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(), object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public static class ID_to_deleteStruct implements routines.system.IPersistableRow<ID_to_deleteStruct> {
		final static byte[] commonByteArrayLock_MBTC_POC_REST_Services = new byte[0];
		static byte[] commonByteArray_MBTC_POC_REST_Services = new byte[0];
		protected static final int DEFAULT_HASHCODE = 1;
		protected static final int PRIME = 31;
		protected int hashCode = DEFAULT_HASHCODE;
		public boolean hashCodeDirty = true;

		public String loopKey;

		public String ID;

		public String getID() {
			return this.ID;
		}

		public Boolean IDIsNullable() {
			return false;
		}

		public Boolean IDIsKey() {
			return true;
		}

		public Integer IDLength() {
			return 255;
		}

		public Integer IDPrecision() {
			return 0;
		}

		public String IDDefault() {

			return null;

		}

		public String IDComment() {

			return "";

		}

		public String IDPattern() {

			return "";

		}

		public String IDOriginalDbColumnName() {

			return "ID";

		}

		public String NAME;

		public String getNAME() {
			return this.NAME;
		}

		public Boolean NAMEIsNullable() {
			return true;
		}

		public Boolean NAMEIsKey() {
			return false;
		}

		public Integer NAMELength() {
			return 255;
		}

		public Integer NAMEPrecision() {
			return 0;
		}

		public String NAMEDefault() {

			return null;

		}

		public String NAMEComment() {

			return "";

		}

		public String NAMEPattern() {

			return "";

		}

		public String NAMEOriginalDbColumnName() {

			return "NAME";

		}

		public String LAST_NAME;

		public String getLAST_NAME() {
			return this.LAST_NAME;
		}

		public Boolean LAST_NAMEIsNullable() {
			return true;
		}

		public Boolean LAST_NAMEIsKey() {
			return false;
		}

		public Integer LAST_NAMELength() {
			return 255;
		}

		public Integer LAST_NAMEPrecision() {
			return 0;
		}

		public String LAST_NAMEDefault() {

			return null;

		}

		public String LAST_NAMEComment() {

			return "";

		}

		public String LAST_NAMEPattern() {

			return "";

		}

		public String LAST_NAMEOriginalDbColumnName() {

			return "LAST_NAME";

		}

		public String EMAIL;

		public String getEMAIL() {
			return this.EMAIL;
		}

		public Boolean EMAILIsNullable() {
			return true;
		}

		public Boolean EMAILIsKey() {
			return false;
		}

		public Integer EMAILLength() {
			return 255;
		}

		public Integer EMAILPrecision() {
			return 0;
		}

		public String EMAILDefault() {

			return null;

		}

		public String EMAILComment() {

			return "";

		}

		public String EMAILPattern() {

			return "";

		}

		public String EMAILOriginalDbColumnName() {

			return "EMAIL";

		}

		public String JOB_TITLE;

		public String getJOB_TITLE() {
			return this.JOB_TITLE;
		}

		public Boolean JOB_TITLEIsNullable() {
			return true;
		}

		public Boolean JOB_TITLEIsKey() {
			return false;
		}

		public Integer JOB_TITLELength() {
			return 255;
		}

		public Integer JOB_TITLEPrecision() {
			return 0;
		}

		public String JOB_TITLEDefault() {

			return null;

		}

		public String JOB_TITLEComment() {

			return "";

		}

		public String JOB_TITLEPattern() {

			return "";

		}

		public String JOB_TITLEOriginalDbColumnName() {

			return "JOB_TITLE";

		}

		public String CREDITCARDNUMBER;

		public String getCREDITCARDNUMBER() {
			return this.CREDITCARDNUMBER;
		}

		public Boolean CREDITCARDNUMBERIsNullable() {
			return true;
		}

		public Boolean CREDITCARDNUMBERIsKey() {
			return false;
		}

		public Integer CREDITCARDNUMBERLength() {
			return 255;
		}

		public Integer CREDITCARDNUMBERPrecision() {
			return 0;
		}

		public String CREDITCARDNUMBERDefault() {

			return null;

		}

		public String CREDITCARDNUMBERComment() {

			return "";

		}

		public String CREDITCARDNUMBERPattern() {

			return "";

		}

		public String CREDITCARDNUMBEROriginalDbColumnName() {

			return "CREDITCARDNUMBER";

		}

		public String COMPANY;

		public String getCOMPANY() {
			return this.COMPANY;
		}

		public Boolean COMPANYIsNullable() {
			return true;
		}

		public Boolean COMPANYIsKey() {
			return false;
		}

		public Integer COMPANYLength() {
			return 255;
		}

		public Integer COMPANYPrecision() {
			return 0;
		}

		public String COMPANYDefault() {

			return null;

		}

		public String COMPANYComment() {

			return "";

		}

		public String COMPANYPattern() {

			return "";

		}

		public String COMPANYOriginalDbColumnName() {

			return "COMPANY";

		}

		public String CITY;

		public String getCITY() {
			return this.CITY;
		}

		public Boolean CITYIsNullable() {
			return true;
		}

		public Boolean CITYIsKey() {
			return false;
		}

		public Integer CITYLength() {
			return 255;
		}

		public Integer CITYPrecision() {
			return 0;
		}

		public String CITYDefault() {

			return null;

		}

		public String CITYComment() {

			return "";

		}

		public String CITYPattern() {

			return "";

		}

		public String CITYOriginalDbColumnName() {

			return "CITY";

		}

		public String STATE;

		public String getSTATE() {
			return this.STATE;
		}

		public Boolean STATEIsNullable() {
			return true;
		}

		public Boolean STATEIsKey() {
			return false;
		}

		public Integer STATELength() {
			return 255;
		}

		public Integer STATEPrecision() {
			return 0;
		}

		public String STATEDefault() {

			return null;

		}

		public String STATEComment() {

			return "";

		}

		public String STATEPattern() {

			return "";

		}

		public String STATEOriginalDbColumnName() {

			return "STATE";

		}

		@Override
		public int hashCode() {
			if (this.hashCodeDirty) {
				final int prime = PRIME;
				int result = DEFAULT_HASHCODE;

				result = prime * result + ((this.ID == null) ? 0 : this.ID.hashCode());

				this.hashCode = result;
				this.hashCodeDirty = false;
			}
			return this.hashCode;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final ID_to_deleteStruct other = (ID_to_deleteStruct) obj;

			if (this.ID == null) {
				if (other.ID != null)
					return false;

			} else if (!this.ID.equals(other.ID))

				return false;

			return true;
		}

		public void copyDataTo(ID_to_deleteStruct other) {

			other.ID = this.ID;
			other.NAME = this.NAME;
			other.LAST_NAME = this.LAST_NAME;
			other.EMAIL = this.EMAIL;
			other.JOB_TITLE = this.JOB_TITLE;
			other.CREDITCARDNUMBER = this.CREDITCARDNUMBER;
			other.COMPANY = this.COMPANY;
			other.CITY = this.CITY;
			other.STATE = this.STATE;

		}

		public void copyKeysDataTo(ID_to_deleteStruct other) {

			other.ID = this.ID;

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.ID = readString(dis);

					this.NAME = readString(dis);

					this.LAST_NAME = readString(dis);

					this.EMAIL = readString(dis);

					this.JOB_TITLE = readString(dis);

					this.CREDITCARDNUMBER = readString(dis);

					this.COMPANY = readString(dis);

					this.CITY = readString(dis);

					this.STATE = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.ID = readString(dis);

					this.NAME = readString(dis);

					this.LAST_NAME = readString(dis);

					this.EMAIL = readString(dis);

					this.JOB_TITLE = readString(dis);

					this.CREDITCARDNUMBER = readString(dis);

					this.COMPANY = readString(dis);

					this.CITY = readString(dis);

					this.STATE = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.ID, dos);

				// String

				writeString(this.NAME, dos);

				// String

				writeString(this.LAST_NAME, dos);

				// String

				writeString(this.EMAIL, dos);

				// String

				writeString(this.JOB_TITLE, dos);

				// String

				writeString(this.CREDITCARDNUMBER, dos);

				// String

				writeString(this.COMPANY, dos);

				// String

				writeString(this.CITY, dos);

				// String

				writeString(this.STATE, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// String

				writeString(this.ID, dos);

				// String

				writeString(this.NAME, dos);

				// String

				writeString(this.LAST_NAME, dos);

				// String

				writeString(this.EMAIL, dos);

				// String

				writeString(this.JOB_TITLE, dos);

				// String

				writeString(this.CREDITCARDNUMBER, dos);

				// String

				writeString(this.COMPANY, dos);

				// String

				writeString(this.CITY, dos);

				// String

				writeString(this.STATE, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("ID=" + ID);
			sb.append(",NAME=" + NAME);
			sb.append(",LAST_NAME=" + LAST_NAME);
			sb.append(",EMAIL=" + EMAIL);
			sb.append(",JOB_TITLE=" + JOB_TITLE);
			sb.append(",CREDITCARDNUMBER=" + CREDITCARDNUMBER);
			sb.append(",COMPANY=" + COMPANY);
			sb.append(",CITY=" + CITY);
			sb.append(",STATE=" + STATE);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (ID == null) {
				sb.append("<null>");
			} else {
				sb.append(ID);
			}

			sb.append("|");

			if (NAME == null) {
				sb.append("<null>");
			} else {
				sb.append(NAME);
			}

			sb.append("|");

			if (LAST_NAME == null) {
				sb.append("<null>");
			} else {
				sb.append(LAST_NAME);
			}

			sb.append("|");

			if (EMAIL == null) {
				sb.append("<null>");
			} else {
				sb.append(EMAIL);
			}

			sb.append("|");

			if (JOB_TITLE == null) {
				sb.append("<null>");
			} else {
				sb.append(JOB_TITLE);
			}

			sb.append("|");

			if (CREDITCARDNUMBER == null) {
				sb.append("<null>");
			} else {
				sb.append(CREDITCARDNUMBER);
			}

			sb.append("|");

			if (COMPANY == null) {
				sb.append("<null>");
			} else {
				sb.append(COMPANY);
			}

			sb.append("|");

			if (CITY == null) {
				sb.append("<null>");
			} else {
				sb.append(CITY);
			}

			sb.append("|");

			if (STATE == null) {
				sb.append("<null>");
			} else {
				sb.append(STATE);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(ID_to_deleteStruct other) {

			int returnValue = -1;

			returnValue = checkNullsAndCompare(this.ID, other.ID);
			if (returnValue != 0) {
				return returnValue;
			}

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(), object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public static class row3Struct implements routines.system.IPersistableRow<row3Struct> {
		final static byte[] commonByteArrayLock_MBTC_POC_REST_Services = new byte[0];
		static byte[] commonByteArray_MBTC_POC_REST_Services = new byte[0];
		protected static final int DEFAULT_HASHCODE = 1;
		protected static final int PRIME = 31;
		protected int hashCode = DEFAULT_HASHCODE;
		public boolean hashCodeDirty = true;

		public String loopKey;

		public String ID;

		public String getID() {
			return this.ID;
		}

		public Boolean IDIsNullable() {
			return false;
		}

		public Boolean IDIsKey() {
			return true;
		}

		public Integer IDLength() {
			return 255;
		}

		public Integer IDPrecision() {
			return 0;
		}

		public String IDDefault() {

			return null;

		}

		public String IDComment() {

			return "";

		}

		public String IDPattern() {

			return "";

		}

		public String IDOriginalDbColumnName() {

			return "ID";

		}

		public String NAME;

		public String getNAME() {
			return this.NAME;
		}

		public Boolean NAMEIsNullable() {
			return true;
		}

		public Boolean NAMEIsKey() {
			return false;
		}

		public Integer NAMELength() {
			return 255;
		}

		public Integer NAMEPrecision() {
			return 0;
		}

		public String NAMEDefault() {

			return null;

		}

		public String NAMEComment() {

			return "";

		}

		public String NAMEPattern() {

			return "";

		}

		public String NAMEOriginalDbColumnName() {

			return "NAME";

		}

		public String LAST_NAME;

		public String getLAST_NAME() {
			return this.LAST_NAME;
		}

		public Boolean LAST_NAMEIsNullable() {
			return true;
		}

		public Boolean LAST_NAMEIsKey() {
			return false;
		}

		public Integer LAST_NAMELength() {
			return 255;
		}

		public Integer LAST_NAMEPrecision() {
			return 0;
		}

		public String LAST_NAMEDefault() {

			return null;

		}

		public String LAST_NAMEComment() {

			return "";

		}

		public String LAST_NAMEPattern() {

			return "";

		}

		public String LAST_NAMEOriginalDbColumnName() {

			return "LAST_NAME";

		}

		public String EMAIL;

		public String getEMAIL() {
			return this.EMAIL;
		}

		public Boolean EMAILIsNullable() {
			return true;
		}

		public Boolean EMAILIsKey() {
			return false;
		}

		public Integer EMAILLength() {
			return 255;
		}

		public Integer EMAILPrecision() {
			return 0;
		}

		public String EMAILDefault() {

			return null;

		}

		public String EMAILComment() {

			return "";

		}

		public String EMAILPattern() {

			return "";

		}

		public String EMAILOriginalDbColumnName() {

			return "EMAIL";

		}

		public String JOB_TITLE;

		public String getJOB_TITLE() {
			return this.JOB_TITLE;
		}

		public Boolean JOB_TITLEIsNullable() {
			return true;
		}

		public Boolean JOB_TITLEIsKey() {
			return false;
		}

		public Integer JOB_TITLELength() {
			return 255;
		}

		public Integer JOB_TITLEPrecision() {
			return 0;
		}

		public String JOB_TITLEDefault() {

			return null;

		}

		public String JOB_TITLEComment() {

			return "";

		}

		public String JOB_TITLEPattern() {

			return "";

		}

		public String JOB_TITLEOriginalDbColumnName() {

			return "JOB_TITLE";

		}

		public String CREDITCARDNUMBER;

		public String getCREDITCARDNUMBER() {
			return this.CREDITCARDNUMBER;
		}

		public Boolean CREDITCARDNUMBERIsNullable() {
			return true;
		}

		public Boolean CREDITCARDNUMBERIsKey() {
			return false;
		}

		public Integer CREDITCARDNUMBERLength() {
			return 255;
		}

		public Integer CREDITCARDNUMBERPrecision() {
			return 0;
		}

		public String CREDITCARDNUMBERDefault() {

			return null;

		}

		public String CREDITCARDNUMBERComment() {

			return "";

		}

		public String CREDITCARDNUMBERPattern() {

			return "";

		}

		public String CREDITCARDNUMBEROriginalDbColumnName() {

			return "CREDITCARDNUMBER";

		}

		public String COMPANY;

		public String getCOMPANY() {
			return this.COMPANY;
		}

		public Boolean COMPANYIsNullable() {
			return true;
		}

		public Boolean COMPANYIsKey() {
			return false;
		}

		public Integer COMPANYLength() {
			return 255;
		}

		public Integer COMPANYPrecision() {
			return 0;
		}

		public String COMPANYDefault() {

			return null;

		}

		public String COMPANYComment() {

			return "";

		}

		public String COMPANYPattern() {

			return "";

		}

		public String COMPANYOriginalDbColumnName() {

			return "COMPANY";

		}

		public String CITY;

		public String getCITY() {
			return this.CITY;
		}

		public Boolean CITYIsNullable() {
			return true;
		}

		public Boolean CITYIsKey() {
			return false;
		}

		public Integer CITYLength() {
			return 255;
		}

		public Integer CITYPrecision() {
			return 0;
		}

		public String CITYDefault() {

			return null;

		}

		public String CITYComment() {

			return "";

		}

		public String CITYPattern() {

			return "";

		}

		public String CITYOriginalDbColumnName() {

			return "CITY";

		}

		public String STATE;

		public String getSTATE() {
			return this.STATE;
		}

		public Boolean STATEIsNullable() {
			return true;
		}

		public Boolean STATEIsKey() {
			return false;
		}

		public Integer STATELength() {
			return 255;
		}

		public Integer STATEPrecision() {
			return 0;
		}

		public String STATEDefault() {

			return null;

		}

		public String STATEComment() {

			return "";

		}

		public String STATEPattern() {

			return "";

		}

		public String STATEOriginalDbColumnName() {

			return "STATE";

		}

		@Override
		public int hashCode() {
			if (this.hashCodeDirty) {
				final int prime = PRIME;
				int result = DEFAULT_HASHCODE;

				result = prime * result + ((this.ID == null) ? 0 : this.ID.hashCode());

				this.hashCode = result;
				this.hashCodeDirty = false;
			}
			return this.hashCode;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final row3Struct other = (row3Struct) obj;

			if (this.ID == null) {
				if (other.ID != null)
					return false;

			} else if (!this.ID.equals(other.ID))

				return false;

			return true;
		}

		public void copyDataTo(row3Struct other) {

			other.ID = this.ID;
			other.NAME = this.NAME;
			other.LAST_NAME = this.LAST_NAME;
			other.EMAIL = this.EMAIL;
			other.JOB_TITLE = this.JOB_TITLE;
			other.CREDITCARDNUMBER = this.CREDITCARDNUMBER;
			other.COMPANY = this.COMPANY;
			other.CITY = this.CITY;
			other.STATE = this.STATE;

		}

		public void copyKeysDataTo(row3Struct other) {

			other.ID = this.ID;

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.ID = readString(dis);

					this.NAME = readString(dis);

					this.LAST_NAME = readString(dis);

					this.EMAIL = readString(dis);

					this.JOB_TITLE = readString(dis);

					this.CREDITCARDNUMBER = readString(dis);

					this.COMPANY = readString(dis);

					this.CITY = readString(dis);

					this.STATE = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.ID = readString(dis);

					this.NAME = readString(dis);

					this.LAST_NAME = readString(dis);

					this.EMAIL = readString(dis);

					this.JOB_TITLE = readString(dis);

					this.CREDITCARDNUMBER = readString(dis);

					this.COMPANY = readString(dis);

					this.CITY = readString(dis);

					this.STATE = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.ID, dos);

				// String

				writeString(this.NAME, dos);

				// String

				writeString(this.LAST_NAME, dos);

				// String

				writeString(this.EMAIL, dos);

				// String

				writeString(this.JOB_TITLE, dos);

				// String

				writeString(this.CREDITCARDNUMBER, dos);

				// String

				writeString(this.COMPANY, dos);

				// String

				writeString(this.CITY, dos);

				// String

				writeString(this.STATE, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// String

				writeString(this.ID, dos);

				// String

				writeString(this.NAME, dos);

				// String

				writeString(this.LAST_NAME, dos);

				// String

				writeString(this.EMAIL, dos);

				// String

				writeString(this.JOB_TITLE, dos);

				// String

				writeString(this.CREDITCARDNUMBER, dos);

				// String

				writeString(this.COMPANY, dos);

				// String

				writeString(this.CITY, dos);

				// String

				writeString(this.STATE, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("ID=" + ID);
			sb.append(",NAME=" + NAME);
			sb.append(",LAST_NAME=" + LAST_NAME);
			sb.append(",EMAIL=" + EMAIL);
			sb.append(",JOB_TITLE=" + JOB_TITLE);
			sb.append(",CREDITCARDNUMBER=" + CREDITCARDNUMBER);
			sb.append(",COMPANY=" + COMPANY);
			sb.append(",CITY=" + CITY);
			sb.append(",STATE=" + STATE);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (ID == null) {
				sb.append("<null>");
			} else {
				sb.append(ID);
			}

			sb.append("|");

			if (NAME == null) {
				sb.append("<null>");
			} else {
				sb.append(NAME);
			}

			sb.append("|");

			if (LAST_NAME == null) {
				sb.append("<null>");
			} else {
				sb.append(LAST_NAME);
			}

			sb.append("|");

			if (EMAIL == null) {
				sb.append("<null>");
			} else {
				sb.append(EMAIL);
			}

			sb.append("|");

			if (JOB_TITLE == null) {
				sb.append("<null>");
			} else {
				sb.append(JOB_TITLE);
			}

			sb.append("|");

			if (CREDITCARDNUMBER == null) {
				sb.append("<null>");
			} else {
				sb.append(CREDITCARDNUMBER);
			}

			sb.append("|");

			if (COMPANY == null) {
				sb.append("<null>");
			} else {
				sb.append(COMPANY);
			}

			sb.append("|");

			if (CITY == null) {
				sb.append("<null>");
			} else {
				sb.append(CITY);
			}

			sb.append("|");

			if (STATE == null) {
				sb.append("<null>");
			} else {
				sb.append(STATE);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row3Struct other) {

			int returnValue = -1;

			returnValue = checkNullsAndCompare(this.ID, other.ID);
			if (returnValue != 0) {
				return returnValue;
			}

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(), object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public static class row5Struct implements routines.system.IPersistableRow<row5Struct> {
		final static byte[] commonByteArrayLock_MBTC_POC_REST_Services = new byte[0];
		static byte[] commonByteArray_MBTC_POC_REST_Services = new byte[0];
		protected static final int DEFAULT_HASHCODE = 1;
		protected static final int PRIME = 31;
		protected int hashCode = DEFAULT_HASHCODE;
		public boolean hashCodeDirty = true;

		public String loopKey;

		public String ID;

		public String getID() {
			return this.ID;
		}

		public Boolean IDIsNullable() {
			return false;
		}

		public Boolean IDIsKey() {
			return true;
		}

		public Integer IDLength() {
			return 255;
		}

		public Integer IDPrecision() {
			return 0;
		}

		public String IDDefault() {

			return null;

		}

		public String IDComment() {

			return "";

		}

		public String IDPattern() {

			return "";

		}

		public String IDOriginalDbColumnName() {

			return "ID";

		}

		public String NAME;

		public String getNAME() {
			return this.NAME;
		}

		public Boolean NAMEIsNullable() {
			return true;
		}

		public Boolean NAMEIsKey() {
			return false;
		}

		public Integer NAMELength() {
			return 255;
		}

		public Integer NAMEPrecision() {
			return 0;
		}

		public String NAMEDefault() {

			return null;

		}

		public String NAMEComment() {

			return "";

		}

		public String NAMEPattern() {

			return "";

		}

		public String NAMEOriginalDbColumnName() {

			return "NAME";

		}

		public String LAST_NAME;

		public String getLAST_NAME() {
			return this.LAST_NAME;
		}

		public Boolean LAST_NAMEIsNullable() {
			return true;
		}

		public Boolean LAST_NAMEIsKey() {
			return false;
		}

		public Integer LAST_NAMELength() {
			return 255;
		}

		public Integer LAST_NAMEPrecision() {
			return 0;
		}

		public String LAST_NAMEDefault() {

			return null;

		}

		public String LAST_NAMEComment() {

			return "";

		}

		public String LAST_NAMEPattern() {

			return "";

		}

		public String LAST_NAMEOriginalDbColumnName() {

			return "LAST_NAME";

		}

		public String EMAIL;

		public String getEMAIL() {
			return this.EMAIL;
		}

		public Boolean EMAILIsNullable() {
			return true;
		}

		public Boolean EMAILIsKey() {
			return false;
		}

		public Integer EMAILLength() {
			return 255;
		}

		public Integer EMAILPrecision() {
			return 0;
		}

		public String EMAILDefault() {

			return null;

		}

		public String EMAILComment() {

			return "";

		}

		public String EMAILPattern() {

			return "";

		}

		public String EMAILOriginalDbColumnName() {

			return "EMAIL";

		}

		public String JOB_TITLE;

		public String getJOB_TITLE() {
			return this.JOB_TITLE;
		}

		public Boolean JOB_TITLEIsNullable() {
			return true;
		}

		public Boolean JOB_TITLEIsKey() {
			return false;
		}

		public Integer JOB_TITLELength() {
			return 255;
		}

		public Integer JOB_TITLEPrecision() {
			return 0;
		}

		public String JOB_TITLEDefault() {

			return null;

		}

		public String JOB_TITLEComment() {

			return "";

		}

		public String JOB_TITLEPattern() {

			return "";

		}

		public String JOB_TITLEOriginalDbColumnName() {

			return "JOB_TITLE";

		}

		public String CREDITCARDNUMBER;

		public String getCREDITCARDNUMBER() {
			return this.CREDITCARDNUMBER;
		}

		public Boolean CREDITCARDNUMBERIsNullable() {
			return true;
		}

		public Boolean CREDITCARDNUMBERIsKey() {
			return false;
		}

		public Integer CREDITCARDNUMBERLength() {
			return 255;
		}

		public Integer CREDITCARDNUMBERPrecision() {
			return 0;
		}

		public String CREDITCARDNUMBERDefault() {

			return null;

		}

		public String CREDITCARDNUMBERComment() {

			return "";

		}

		public String CREDITCARDNUMBERPattern() {

			return "";

		}

		public String CREDITCARDNUMBEROriginalDbColumnName() {

			return "CREDITCARDNUMBER";

		}

		public String COMPANY;

		public String getCOMPANY() {
			return this.COMPANY;
		}

		public Boolean COMPANYIsNullable() {
			return true;
		}

		public Boolean COMPANYIsKey() {
			return false;
		}

		public Integer COMPANYLength() {
			return 255;
		}

		public Integer COMPANYPrecision() {
			return 0;
		}

		public String COMPANYDefault() {

			return null;

		}

		public String COMPANYComment() {

			return "";

		}

		public String COMPANYPattern() {

			return "";

		}

		public String COMPANYOriginalDbColumnName() {

			return "COMPANY";

		}

		public String CITY;

		public String getCITY() {
			return this.CITY;
		}

		public Boolean CITYIsNullable() {
			return true;
		}

		public Boolean CITYIsKey() {
			return false;
		}

		public Integer CITYLength() {
			return 255;
		}

		public Integer CITYPrecision() {
			return 0;
		}

		public String CITYDefault() {

			return null;

		}

		public String CITYComment() {

			return "";

		}

		public String CITYPattern() {

			return "";

		}

		public String CITYOriginalDbColumnName() {

			return "CITY";

		}

		public String STATE;

		public String getSTATE() {
			return this.STATE;
		}

		public Boolean STATEIsNullable() {
			return true;
		}

		public Boolean STATEIsKey() {
			return false;
		}

		public Integer STATELength() {
			return 255;
		}

		public Integer STATEPrecision() {
			return 0;
		}

		public String STATEDefault() {

			return null;

		}

		public String STATEComment() {

			return "";

		}

		public String STATEPattern() {

			return "";

		}

		public String STATEOriginalDbColumnName() {

			return "STATE";

		}

		public String errorCode;

		public String getErrorCode() {
			return this.errorCode;
		}

		public Boolean errorCodeIsNullable() {
			return true;
		}

		public Boolean errorCodeIsKey() {
			return false;
		}

		public Integer errorCodeLength() {
			return 255;
		}

		public Integer errorCodePrecision() {
			return 0;
		}

		public String errorCodeDefault() {

			return "";

		}

		public String errorCodeComment() {

			return null;

		}

		public String errorCodePattern() {

			return null;

		}

		public String errorCodeOriginalDbColumnName() {

			return "errorCode";

		}

		public String errorMessage;

		public String getErrorMessage() {
			return this.errorMessage;
		}

		public Boolean errorMessageIsNullable() {
			return true;
		}

		public Boolean errorMessageIsKey() {
			return false;
		}

		public Integer errorMessageLength() {
			return 255;
		}

		public Integer errorMessagePrecision() {
			return 0;
		}

		public String errorMessageDefault() {

			return "";

		}

		public String errorMessageComment() {

			return null;

		}

		public String errorMessagePattern() {

			return null;

		}

		public String errorMessageOriginalDbColumnName() {

			return "errorMessage";

		}

		@Override
		public int hashCode() {
			if (this.hashCodeDirty) {
				final int prime = PRIME;
				int result = DEFAULT_HASHCODE;

				result = prime * result + ((this.ID == null) ? 0 : this.ID.hashCode());

				this.hashCode = result;
				this.hashCodeDirty = false;
			}
			return this.hashCode;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final row5Struct other = (row5Struct) obj;

			if (this.ID == null) {
				if (other.ID != null)
					return false;

			} else if (!this.ID.equals(other.ID))

				return false;

			return true;
		}

		public void copyDataTo(row5Struct other) {

			other.ID = this.ID;
			other.NAME = this.NAME;
			other.LAST_NAME = this.LAST_NAME;
			other.EMAIL = this.EMAIL;
			other.JOB_TITLE = this.JOB_TITLE;
			other.CREDITCARDNUMBER = this.CREDITCARDNUMBER;
			other.COMPANY = this.COMPANY;
			other.CITY = this.CITY;
			other.STATE = this.STATE;
			other.errorCode = this.errorCode;
			other.errorMessage = this.errorMessage;

		}

		public void copyKeysDataTo(row5Struct other) {

			other.ID = this.ID;

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.ID = readString(dis);

					this.NAME = readString(dis);

					this.LAST_NAME = readString(dis);

					this.EMAIL = readString(dis);

					this.JOB_TITLE = readString(dis);

					this.CREDITCARDNUMBER = readString(dis);

					this.COMPANY = readString(dis);

					this.CITY = readString(dis);

					this.STATE = readString(dis);

					this.errorCode = readString(dis);

					this.errorMessage = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.ID = readString(dis);

					this.NAME = readString(dis);

					this.LAST_NAME = readString(dis);

					this.EMAIL = readString(dis);

					this.JOB_TITLE = readString(dis);

					this.CREDITCARDNUMBER = readString(dis);

					this.COMPANY = readString(dis);

					this.CITY = readString(dis);

					this.STATE = readString(dis);

					this.errorCode = readString(dis);

					this.errorMessage = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.ID, dos);

				// String

				writeString(this.NAME, dos);

				// String

				writeString(this.LAST_NAME, dos);

				// String

				writeString(this.EMAIL, dos);

				// String

				writeString(this.JOB_TITLE, dos);

				// String

				writeString(this.CREDITCARDNUMBER, dos);

				// String

				writeString(this.COMPANY, dos);

				// String

				writeString(this.CITY, dos);

				// String

				writeString(this.STATE, dos);

				// String

				writeString(this.errorCode, dos);

				// String

				writeString(this.errorMessage, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// String

				writeString(this.ID, dos);

				// String

				writeString(this.NAME, dos);

				// String

				writeString(this.LAST_NAME, dos);

				// String

				writeString(this.EMAIL, dos);

				// String

				writeString(this.JOB_TITLE, dos);

				// String

				writeString(this.CREDITCARDNUMBER, dos);

				// String

				writeString(this.COMPANY, dos);

				// String

				writeString(this.CITY, dos);

				// String

				writeString(this.STATE, dos);

				// String

				writeString(this.errorCode, dos);

				// String

				writeString(this.errorMessage, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("ID=" + ID);
			sb.append(",NAME=" + NAME);
			sb.append(",LAST_NAME=" + LAST_NAME);
			sb.append(",EMAIL=" + EMAIL);
			sb.append(",JOB_TITLE=" + JOB_TITLE);
			sb.append(",CREDITCARDNUMBER=" + CREDITCARDNUMBER);
			sb.append(",COMPANY=" + COMPANY);
			sb.append(",CITY=" + CITY);
			sb.append(",STATE=" + STATE);
			sb.append(",errorCode=" + errorCode);
			sb.append(",errorMessage=" + errorMessage);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (ID == null) {
				sb.append("<null>");
			} else {
				sb.append(ID);
			}

			sb.append("|");

			if (NAME == null) {
				sb.append("<null>");
			} else {
				sb.append(NAME);
			}

			sb.append("|");

			if (LAST_NAME == null) {
				sb.append("<null>");
			} else {
				sb.append(LAST_NAME);
			}

			sb.append("|");

			if (EMAIL == null) {
				sb.append("<null>");
			} else {
				sb.append(EMAIL);
			}

			sb.append("|");

			if (JOB_TITLE == null) {
				sb.append("<null>");
			} else {
				sb.append(JOB_TITLE);
			}

			sb.append("|");

			if (CREDITCARDNUMBER == null) {
				sb.append("<null>");
			} else {
				sb.append(CREDITCARDNUMBER);
			}

			sb.append("|");

			if (COMPANY == null) {
				sb.append("<null>");
			} else {
				sb.append(COMPANY);
			}

			sb.append("|");

			if (CITY == null) {
				sb.append("<null>");
			} else {
				sb.append(CITY);
			}

			sb.append("|");

			if (STATE == null) {
				sb.append("<null>");
			} else {
				sb.append(STATE);
			}

			sb.append("|");

			if (errorCode == null) {
				sb.append("<null>");
			} else {
				sb.append(errorCode);
			}

			sb.append("|");

			if (errorMessage == null) {
				sb.append("<null>");
			} else {
				sb.append(errorMessage);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row5Struct other) {

			int returnValue = -1;

			returnValue = checkNullsAndCompare(this.ID, other.ID);
			if (returnValue != 0) {
				return returnValue;
			}

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(), object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public static class row8Struct implements routines.system.IPersistableRow<row8Struct> {
		final static byte[] commonByteArrayLock_MBTC_POC_REST_Services = new byte[0];
		static byte[] commonByteArray_MBTC_POC_REST_Services = new byte[0];

		public String ID;

		public String getID() {
			return this.ID;
		}

		public Boolean IDIsNullable() {
			return true;
		}

		public Boolean IDIsKey() {
			return false;
		}

		public Integer IDLength() {
			return null;
		}

		public Integer IDPrecision() {
			return 0;
		}

		public String IDDefault() {

			return null;

		}

		public String IDComment() {

			return "";

		}

		public String IDPattern() {

			return "dd-MM-yyyy";

		}

		public String IDOriginalDbColumnName() {

			return "ID";

		}

		public String NAME;

		public String getNAME() {
			return this.NAME;
		}

		public Boolean NAMEIsNullable() {
			return true;
		}

		public Boolean NAMEIsKey() {
			return false;
		}

		public Integer NAMELength() {
			return 4;
		}

		public Integer NAMEPrecision() {
			return 0;
		}

		public String NAMEDefault() {

			return null;

		}

		public String NAMEComment() {

			return "";

		}

		public String NAMEPattern() {

			return "dd-MM-yyyy";

		}

		public String NAMEOriginalDbColumnName() {

			return "NAME";

		}

		public String LAST_NAME;

		public String getLAST_NAME() {
			return this.LAST_NAME;
		}

		public Boolean LAST_NAMEIsNullable() {
			return true;
		}

		public Boolean LAST_NAMEIsKey() {
			return false;
		}

		public Integer LAST_NAMELength() {
			return 5;
		}

		public Integer LAST_NAMEPrecision() {
			return 0;
		}

		public String LAST_NAMEDefault() {

			return null;

		}

		public String LAST_NAMEComment() {

			return "";

		}

		public String LAST_NAMEPattern() {

			return "dd-MM-yyyy";

		}

		public String LAST_NAMEOriginalDbColumnName() {

			return "LAST_NAME";

		}

		public String EMAIL;

		public String getEMAIL() {
			return this.EMAIL;
		}

		public Boolean EMAILIsNullable() {
			return true;
		}

		public Boolean EMAILIsKey() {
			return false;
		}

		public Integer EMAILLength() {
			return 21;
		}

		public Integer EMAILPrecision() {
			return 0;
		}

		public String EMAILDefault() {

			return null;

		}

		public String EMAILComment() {

			return "";

		}

		public String EMAILPattern() {

			return "dd-MM-yyyy";

		}

		public String EMAILOriginalDbColumnName() {

			return "EMAIL";

		}

		public String JOB_TITLE;

		public String getJOB_TITLE() {
			return this.JOB_TITLE;
		}

		public Boolean JOB_TITLEIsNullable() {
			return true;
		}

		public Boolean JOB_TITLEIsKey() {
			return false;
		}

		public Integer JOB_TITLELength() {
			return 19;
		}

		public Integer JOB_TITLEPrecision() {
			return 0;
		}

		public String JOB_TITLEDefault() {

			return null;

		}

		public String JOB_TITLEComment() {

			return "";

		}

		public String JOB_TITLEPattern() {

			return "dd-MM-yyyy";

		}

		public String JOB_TITLEOriginalDbColumnName() {

			return "JOB_TITLE";

		}

		public String CREDITCARDNUMBER;

		public String getCREDITCARDNUMBER() {
			return this.CREDITCARDNUMBER;
		}

		public Boolean CREDITCARDNUMBERIsNullable() {
			return true;
		}

		public Boolean CREDITCARDNUMBERIsKey() {
			return false;
		}

		public Integer CREDITCARDNUMBERLength() {
			return null;
		}

		public Integer CREDITCARDNUMBERPrecision() {
			return 0;
		}

		public String CREDITCARDNUMBERDefault() {

			return null;

		}

		public String CREDITCARDNUMBERComment() {

			return "";

		}

		public String CREDITCARDNUMBERPattern() {

			return "dd-MM-yyyy";

		}

		public String CREDITCARDNUMBEROriginalDbColumnName() {

			return "CREDITCARDNUMBER";

		}

		public String COMPANY;

		public String getCOMPANY() {
			return this.COMPANY;
		}

		public Boolean COMPANYIsNullable() {
			return true;
		}

		public Boolean COMPANYIsKey() {
			return false;
		}

		public Integer COMPANYLength() {
			return 7;
		}

		public Integer COMPANYPrecision() {
			return 0;
		}

		public String COMPANYDefault() {

			return null;

		}

		public String COMPANYComment() {

			return "";

		}

		public String COMPANYPattern() {

			return "dd-MM-yyyy";

		}

		public String COMPANYOriginalDbColumnName() {

			return "COMPANY";

		}

		public String CITY;

		public String getCITY() {
			return this.CITY;
		}

		public Boolean CITYIsNullable() {
			return true;
		}

		public Boolean CITYIsKey() {
			return false;
		}

		public Integer CITYLength() {
			return 13;
		}

		public Integer CITYPrecision() {
			return 0;
		}

		public String CITYDefault() {

			return null;

		}

		public String CITYComment() {

			return "";

		}

		public String CITYPattern() {

			return "dd-MM-yyyy";

		}

		public String CITYOriginalDbColumnName() {

			return "CITY";

		}

		public String STATE;

		public String getSTATE() {
			return this.STATE;
		}

		public Boolean STATEIsNullable() {
			return true;
		}

		public Boolean STATEIsKey() {
			return false;
		}

		public Integer STATELength() {
			return 255;
		}

		public Integer STATEPrecision() {
			return 0;
		}

		public String STATEDefault() {

			return null;

		}

		public String STATEComment() {

			return "";

		}

		public String STATEPattern() {

			return "dd-MM-yyyy";

		}

		public String STATEOriginalDbColumnName() {

			return "STATE";

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.ID = readString(dis);

					this.NAME = readString(dis);

					this.LAST_NAME = readString(dis);

					this.EMAIL = readString(dis);

					this.JOB_TITLE = readString(dis);

					this.CREDITCARDNUMBER = readString(dis);

					this.COMPANY = readString(dis);

					this.CITY = readString(dis);

					this.STATE = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.ID = readString(dis);

					this.NAME = readString(dis);

					this.LAST_NAME = readString(dis);

					this.EMAIL = readString(dis);

					this.JOB_TITLE = readString(dis);

					this.CREDITCARDNUMBER = readString(dis);

					this.COMPANY = readString(dis);

					this.CITY = readString(dis);

					this.STATE = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.ID, dos);

				// String

				writeString(this.NAME, dos);

				// String

				writeString(this.LAST_NAME, dos);

				// String

				writeString(this.EMAIL, dos);

				// String

				writeString(this.JOB_TITLE, dos);

				// String

				writeString(this.CREDITCARDNUMBER, dos);

				// String

				writeString(this.COMPANY, dos);

				// String

				writeString(this.CITY, dos);

				// String

				writeString(this.STATE, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// String

				writeString(this.ID, dos);

				// String

				writeString(this.NAME, dos);

				// String

				writeString(this.LAST_NAME, dos);

				// String

				writeString(this.EMAIL, dos);

				// String

				writeString(this.JOB_TITLE, dos);

				// String

				writeString(this.CREDITCARDNUMBER, dos);

				// String

				writeString(this.COMPANY, dos);

				// String

				writeString(this.CITY, dos);

				// String

				writeString(this.STATE, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("ID=" + ID);
			sb.append(",NAME=" + NAME);
			sb.append(",LAST_NAME=" + LAST_NAME);
			sb.append(",EMAIL=" + EMAIL);
			sb.append(",JOB_TITLE=" + JOB_TITLE);
			sb.append(",CREDITCARDNUMBER=" + CREDITCARDNUMBER);
			sb.append(",COMPANY=" + COMPANY);
			sb.append(",CITY=" + CITY);
			sb.append(",STATE=" + STATE);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (ID == null) {
				sb.append("<null>");
			} else {
				sb.append(ID);
			}

			sb.append("|");

			if (NAME == null) {
				sb.append("<null>");
			} else {
				sb.append(NAME);
			}

			sb.append("|");

			if (LAST_NAME == null) {
				sb.append("<null>");
			} else {
				sb.append(LAST_NAME);
			}

			sb.append("|");

			if (EMAIL == null) {
				sb.append("<null>");
			} else {
				sb.append(EMAIL);
			}

			sb.append("|");

			if (JOB_TITLE == null) {
				sb.append("<null>");
			} else {
				sb.append(JOB_TITLE);
			}

			sb.append("|");

			if (CREDITCARDNUMBER == null) {
				sb.append("<null>");
			} else {
				sb.append(CREDITCARDNUMBER);
			}

			sb.append("|");

			if (COMPANY == null) {
				sb.append("<null>");
			} else {
				sb.append(COMPANY);
			}

			sb.append("|");

			if (CITY == null) {
				sb.append("<null>");
			} else {
				sb.append(CITY);
			}

			sb.append("|");

			if (STATE == null) {
				sb.append("<null>");
			} else {
				sb.append(STATE);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row8Struct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(), object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public static class row2Struct implements routines.system.IPersistableRow<row2Struct> {
		final static byte[] commonByteArrayLock_MBTC_POC_REST_Services = new byte[0];
		static byte[] commonByteArray_MBTC_POC_REST_Services = new byte[0];

		public String ID;

		public String getID() {
			return this.ID;
		}

		public Boolean IDIsNullable() {
			return true;
		}

		public Boolean IDIsKey() {
			return false;
		}

		public Integer IDLength() {
			return null;
		}

		public Integer IDPrecision() {
			return 0;
		}

		public String IDDefault() {

			return null;

		}

		public String IDComment() {

			return "";

		}

		public String IDPattern() {

			return "dd-MM-yyyy";

		}

		public String IDOriginalDbColumnName() {

			return "ID";

		}

		public String NAME;

		public String getNAME() {
			return this.NAME;
		}

		public Boolean NAMEIsNullable() {
			return true;
		}

		public Boolean NAMEIsKey() {
			return false;
		}

		public Integer NAMELength() {
			return 4;
		}

		public Integer NAMEPrecision() {
			return 0;
		}

		public String NAMEDefault() {

			return null;

		}

		public String NAMEComment() {

			return "";

		}

		public String NAMEPattern() {

			return "dd-MM-yyyy";

		}

		public String NAMEOriginalDbColumnName() {

			return "NAME";

		}

		public String LAST_NAME;

		public String getLAST_NAME() {
			return this.LAST_NAME;
		}

		public Boolean LAST_NAMEIsNullable() {
			return true;
		}

		public Boolean LAST_NAMEIsKey() {
			return false;
		}

		public Integer LAST_NAMELength() {
			return 5;
		}

		public Integer LAST_NAMEPrecision() {
			return 0;
		}

		public String LAST_NAMEDefault() {

			return null;

		}

		public String LAST_NAMEComment() {

			return "";

		}

		public String LAST_NAMEPattern() {

			return "dd-MM-yyyy";

		}

		public String LAST_NAMEOriginalDbColumnName() {

			return "LAST_NAME";

		}

		public String EMAIL;

		public String getEMAIL() {
			return this.EMAIL;
		}

		public Boolean EMAILIsNullable() {
			return true;
		}

		public Boolean EMAILIsKey() {
			return false;
		}

		public Integer EMAILLength() {
			return 21;
		}

		public Integer EMAILPrecision() {
			return 0;
		}

		public String EMAILDefault() {

			return null;

		}

		public String EMAILComment() {

			return "";

		}

		public String EMAILPattern() {

			return "dd-MM-yyyy";

		}

		public String EMAILOriginalDbColumnName() {

			return "EMAIL";

		}

		public String JOB_TITLE;

		public String getJOB_TITLE() {
			return this.JOB_TITLE;
		}

		public Boolean JOB_TITLEIsNullable() {
			return true;
		}

		public Boolean JOB_TITLEIsKey() {
			return false;
		}

		public Integer JOB_TITLELength() {
			return 19;
		}

		public Integer JOB_TITLEPrecision() {
			return 0;
		}

		public String JOB_TITLEDefault() {

			return null;

		}

		public String JOB_TITLEComment() {

			return "";

		}

		public String JOB_TITLEPattern() {

			return "dd-MM-yyyy";

		}

		public String JOB_TITLEOriginalDbColumnName() {

			return "JOB_TITLE";

		}

		public String CREDITCARDNUMBER;

		public String getCREDITCARDNUMBER() {
			return this.CREDITCARDNUMBER;
		}

		public Boolean CREDITCARDNUMBERIsNullable() {
			return true;
		}

		public Boolean CREDITCARDNUMBERIsKey() {
			return false;
		}

		public Integer CREDITCARDNUMBERLength() {
			return null;
		}

		public Integer CREDITCARDNUMBERPrecision() {
			return 0;
		}

		public String CREDITCARDNUMBERDefault() {

			return null;

		}

		public String CREDITCARDNUMBERComment() {

			return "";

		}

		public String CREDITCARDNUMBERPattern() {

			return "dd-MM-yyyy";

		}

		public String CREDITCARDNUMBEROriginalDbColumnName() {

			return "CREDITCARDNUMBER";

		}

		public String COMPANY;

		public String getCOMPANY() {
			return this.COMPANY;
		}

		public Boolean COMPANYIsNullable() {
			return true;
		}

		public Boolean COMPANYIsKey() {
			return false;
		}

		public Integer COMPANYLength() {
			return 7;
		}

		public Integer COMPANYPrecision() {
			return 0;
		}

		public String COMPANYDefault() {

			return null;

		}

		public String COMPANYComment() {

			return "";

		}

		public String COMPANYPattern() {

			return "dd-MM-yyyy";

		}

		public String COMPANYOriginalDbColumnName() {

			return "COMPANY";

		}

		public String CITY;

		public String getCITY() {
			return this.CITY;
		}

		public Boolean CITYIsNullable() {
			return true;
		}

		public Boolean CITYIsKey() {
			return false;
		}

		public Integer CITYLength() {
			return 13;
		}

		public Integer CITYPrecision() {
			return 0;
		}

		public String CITYDefault() {

			return null;

		}

		public String CITYComment() {

			return "";

		}

		public String CITYPattern() {

			return "dd-MM-yyyy";

		}

		public String CITYOriginalDbColumnName() {

			return "CITY";

		}

		public String STATE;

		public String getSTATE() {
			return this.STATE;
		}

		public Boolean STATEIsNullable() {
			return true;
		}

		public Boolean STATEIsKey() {
			return false;
		}

		public Integer STATELength() {
			return 255;
		}

		public Integer STATEPrecision() {
			return 0;
		}

		public String STATEDefault() {

			return null;

		}

		public String STATEComment() {

			return "";

		}

		public String STATEPattern() {

			return "dd-MM-yyyy";

		}

		public String STATEOriginalDbColumnName() {

			return "STATE";

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.ID = readString(dis);

					this.NAME = readString(dis);

					this.LAST_NAME = readString(dis);

					this.EMAIL = readString(dis);

					this.JOB_TITLE = readString(dis);

					this.CREDITCARDNUMBER = readString(dis);

					this.COMPANY = readString(dis);

					this.CITY = readString(dis);

					this.STATE = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.ID = readString(dis);

					this.NAME = readString(dis);

					this.LAST_NAME = readString(dis);

					this.EMAIL = readString(dis);

					this.JOB_TITLE = readString(dis);

					this.CREDITCARDNUMBER = readString(dis);

					this.COMPANY = readString(dis);

					this.CITY = readString(dis);

					this.STATE = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.ID, dos);

				// String

				writeString(this.NAME, dos);

				// String

				writeString(this.LAST_NAME, dos);

				// String

				writeString(this.EMAIL, dos);

				// String

				writeString(this.JOB_TITLE, dos);

				// String

				writeString(this.CREDITCARDNUMBER, dos);

				// String

				writeString(this.COMPANY, dos);

				// String

				writeString(this.CITY, dos);

				// String

				writeString(this.STATE, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// String

				writeString(this.ID, dos);

				// String

				writeString(this.NAME, dos);

				// String

				writeString(this.LAST_NAME, dos);

				// String

				writeString(this.EMAIL, dos);

				// String

				writeString(this.JOB_TITLE, dos);

				// String

				writeString(this.CREDITCARDNUMBER, dos);

				// String

				writeString(this.COMPANY, dos);

				// String

				writeString(this.CITY, dos);

				// String

				writeString(this.STATE, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("ID=" + ID);
			sb.append(",NAME=" + NAME);
			sb.append(",LAST_NAME=" + LAST_NAME);
			sb.append(",EMAIL=" + EMAIL);
			sb.append(",JOB_TITLE=" + JOB_TITLE);
			sb.append(",CREDITCARDNUMBER=" + CREDITCARDNUMBER);
			sb.append(",COMPANY=" + COMPANY);
			sb.append(",CITY=" + CITY);
			sb.append(",STATE=" + STATE);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (ID == null) {
				sb.append("<null>");
			} else {
				sb.append(ID);
			}

			sb.append("|");

			if (NAME == null) {
				sb.append("<null>");
			} else {
				sb.append(NAME);
			}

			sb.append("|");

			if (LAST_NAME == null) {
				sb.append("<null>");
			} else {
				sb.append(LAST_NAME);
			}

			sb.append("|");

			if (EMAIL == null) {
				sb.append("<null>");
			} else {
				sb.append(EMAIL);
			}

			sb.append("|");

			if (JOB_TITLE == null) {
				sb.append("<null>");
			} else {
				sb.append(JOB_TITLE);
			}

			sb.append("|");

			if (CREDITCARDNUMBER == null) {
				sb.append("<null>");
			} else {
				sb.append(CREDITCARDNUMBER);
			}

			sb.append("|");

			if (COMPANY == null) {
				sb.append("<null>");
			} else {
				sb.append(COMPANY);
			}

			sb.append("|");

			if (CITY == null) {
				sb.append("<null>");
			} else {
				sb.append(CITY);
			}

			sb.append("|");

			if (STATE == null) {
				sb.append("<null>");
			} else {
				sb.append(STATE);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row2Struct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(), object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public static class sendCustomersStruct implements routines.system.IPersistableRow<sendCustomersStruct> {
		final static byte[] commonByteArrayLock_MBTC_POC_REST_Services = new byte[0];
		static byte[] commonByteArray_MBTC_POC_REST_Services = new byte[0];

		public String body;

		public String getBody() {
			return this.body;
		}

		public Boolean bodyIsNullable() {
			return true;
		}

		public Boolean bodyIsKey() {
			return false;
		}

		public Integer bodyLength() {
			return 0;
		}

		public Integer bodyPrecision() {
			return 0;
		}

		public String bodyDefault() {

			return "";

		}

		public String bodyComment() {

			return null;

		}

		public String bodyPattern() {

			return null;

		}

		public String bodyOriginalDbColumnName() {

			return "body";

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.body = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.body = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.body, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// String

				writeString(this.body, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("body=" + body);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (body == null) {
				sb.append("<null>");
			} else {
				sb.append(body);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(sendCustomersStruct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(), object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public static class row1Struct implements routines.system.IPersistableRow<row1Struct> {
		final static byte[] commonByteArrayLock_MBTC_POC_REST_Services = new byte[0];
		static byte[] commonByteArray_MBTC_POC_REST_Services = new byte[0];
		protected static final int DEFAULT_HASHCODE = 1;
		protected static final int PRIME = 31;
		protected int hashCode = DEFAULT_HASHCODE;
		public boolean hashCodeDirty = true;

		public String loopKey;

		public String ID;

		public String getID() {
			return this.ID;
		}

		public Boolean IDIsNullable() {
			return false;
		}

		public Boolean IDIsKey() {
			return true;
		}

		public Integer IDLength() {
			return 255;
		}

		public Integer IDPrecision() {
			return 0;
		}

		public String IDDefault() {

			return null;

		}

		public String IDComment() {

			return "";

		}

		public String IDPattern() {

			return "";

		}

		public String IDOriginalDbColumnName() {

			return "ID";

		}

		public String NAME;

		public String getNAME() {
			return this.NAME;
		}

		public Boolean NAMEIsNullable() {
			return true;
		}

		public Boolean NAMEIsKey() {
			return false;
		}

		public Integer NAMELength() {
			return 255;
		}

		public Integer NAMEPrecision() {
			return 0;
		}

		public String NAMEDefault() {

			return null;

		}

		public String NAMEComment() {

			return "";

		}

		public String NAMEPattern() {

			return "";

		}

		public String NAMEOriginalDbColumnName() {

			return "NAME";

		}

		public String LAST_NAME;

		public String getLAST_NAME() {
			return this.LAST_NAME;
		}

		public Boolean LAST_NAMEIsNullable() {
			return true;
		}

		public Boolean LAST_NAMEIsKey() {
			return false;
		}

		public Integer LAST_NAMELength() {
			return 255;
		}

		public Integer LAST_NAMEPrecision() {
			return 0;
		}

		public String LAST_NAMEDefault() {

			return null;

		}

		public String LAST_NAMEComment() {

			return "";

		}

		public String LAST_NAMEPattern() {

			return "";

		}

		public String LAST_NAMEOriginalDbColumnName() {

			return "LAST_NAME";

		}

		public String EMAIL;

		public String getEMAIL() {
			return this.EMAIL;
		}

		public Boolean EMAILIsNullable() {
			return true;
		}

		public Boolean EMAILIsKey() {
			return false;
		}

		public Integer EMAILLength() {
			return 255;
		}

		public Integer EMAILPrecision() {
			return 0;
		}

		public String EMAILDefault() {

			return null;

		}

		public String EMAILComment() {

			return "";

		}

		public String EMAILPattern() {

			return "";

		}

		public String EMAILOriginalDbColumnName() {

			return "EMAIL";

		}

		public String JOB_TITLE;

		public String getJOB_TITLE() {
			return this.JOB_TITLE;
		}

		public Boolean JOB_TITLEIsNullable() {
			return true;
		}

		public Boolean JOB_TITLEIsKey() {
			return false;
		}

		public Integer JOB_TITLELength() {
			return 255;
		}

		public Integer JOB_TITLEPrecision() {
			return 0;
		}

		public String JOB_TITLEDefault() {

			return null;

		}

		public String JOB_TITLEComment() {

			return "";

		}

		public String JOB_TITLEPattern() {

			return "";

		}

		public String JOB_TITLEOriginalDbColumnName() {

			return "JOB_TITLE";

		}

		public String CREDITCARDNUMBER;

		public String getCREDITCARDNUMBER() {
			return this.CREDITCARDNUMBER;
		}

		public Boolean CREDITCARDNUMBERIsNullable() {
			return true;
		}

		public Boolean CREDITCARDNUMBERIsKey() {
			return false;
		}

		public Integer CREDITCARDNUMBERLength() {
			return 255;
		}

		public Integer CREDITCARDNUMBERPrecision() {
			return 0;
		}

		public String CREDITCARDNUMBERDefault() {

			return null;

		}

		public String CREDITCARDNUMBERComment() {

			return "";

		}

		public String CREDITCARDNUMBERPattern() {

			return "";

		}

		public String CREDITCARDNUMBEROriginalDbColumnName() {

			return "CREDITCARDNUMBER";

		}

		public String COMPANY;

		public String getCOMPANY() {
			return this.COMPANY;
		}

		public Boolean COMPANYIsNullable() {
			return true;
		}

		public Boolean COMPANYIsKey() {
			return false;
		}

		public Integer COMPANYLength() {
			return 255;
		}

		public Integer COMPANYPrecision() {
			return 0;
		}

		public String COMPANYDefault() {

			return null;

		}

		public String COMPANYComment() {

			return "";

		}

		public String COMPANYPattern() {

			return "";

		}

		public String COMPANYOriginalDbColumnName() {

			return "COMPANY";

		}

		public String CITY;

		public String getCITY() {
			return this.CITY;
		}

		public Boolean CITYIsNullable() {
			return true;
		}

		public Boolean CITYIsKey() {
			return false;
		}

		public Integer CITYLength() {
			return 255;
		}

		public Integer CITYPrecision() {
			return 0;
		}

		public String CITYDefault() {

			return null;

		}

		public String CITYComment() {

			return "";

		}

		public String CITYPattern() {

			return "";

		}

		public String CITYOriginalDbColumnName() {

			return "CITY";

		}

		public String STATE;

		public String getSTATE() {
			return this.STATE;
		}

		public Boolean STATEIsNullable() {
			return true;
		}

		public Boolean STATEIsKey() {
			return false;
		}

		public Integer STATELength() {
			return 255;
		}

		public Integer STATEPrecision() {
			return 0;
		}

		public String STATEDefault() {

			return null;

		}

		public String STATEComment() {

			return "";

		}

		public String STATEPattern() {

			return "";

		}

		public String STATEOriginalDbColumnName() {

			return "STATE";

		}

		@Override
		public int hashCode() {
			if (this.hashCodeDirty) {
				final int prime = PRIME;
				int result = DEFAULT_HASHCODE;

				result = prime * result + ((this.ID == null) ? 0 : this.ID.hashCode());

				this.hashCode = result;
				this.hashCodeDirty = false;
			}
			return this.hashCode;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final row1Struct other = (row1Struct) obj;

			if (this.ID == null) {
				if (other.ID != null)
					return false;

			} else if (!this.ID.equals(other.ID))

				return false;

			return true;
		}

		public void copyDataTo(row1Struct other) {

			other.ID = this.ID;
			other.NAME = this.NAME;
			other.LAST_NAME = this.LAST_NAME;
			other.EMAIL = this.EMAIL;
			other.JOB_TITLE = this.JOB_TITLE;
			other.CREDITCARDNUMBER = this.CREDITCARDNUMBER;
			other.COMPANY = this.COMPANY;
			other.CITY = this.CITY;
			other.STATE = this.STATE;

		}

		public void copyKeysDataTo(row1Struct other) {

			other.ID = this.ID;

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.ID = readString(dis);

					this.NAME = readString(dis);

					this.LAST_NAME = readString(dis);

					this.EMAIL = readString(dis);

					this.JOB_TITLE = readString(dis);

					this.CREDITCARDNUMBER = readString(dis);

					this.COMPANY = readString(dis);

					this.CITY = readString(dis);

					this.STATE = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.ID = readString(dis);

					this.NAME = readString(dis);

					this.LAST_NAME = readString(dis);

					this.EMAIL = readString(dis);

					this.JOB_TITLE = readString(dis);

					this.CREDITCARDNUMBER = readString(dis);

					this.COMPANY = readString(dis);

					this.CITY = readString(dis);

					this.STATE = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.ID, dos);

				// String

				writeString(this.NAME, dos);

				// String

				writeString(this.LAST_NAME, dos);

				// String

				writeString(this.EMAIL, dos);

				// String

				writeString(this.JOB_TITLE, dos);

				// String

				writeString(this.CREDITCARDNUMBER, dos);

				// String

				writeString(this.COMPANY, dos);

				// String

				writeString(this.CITY, dos);

				// String

				writeString(this.STATE, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// String

				writeString(this.ID, dos);

				// String

				writeString(this.NAME, dos);

				// String

				writeString(this.LAST_NAME, dos);

				// String

				writeString(this.EMAIL, dos);

				// String

				writeString(this.JOB_TITLE, dos);

				// String

				writeString(this.CREDITCARDNUMBER, dos);

				// String

				writeString(this.COMPANY, dos);

				// String

				writeString(this.CITY, dos);

				// String

				writeString(this.STATE, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("ID=" + ID);
			sb.append(",NAME=" + NAME);
			sb.append(",LAST_NAME=" + LAST_NAME);
			sb.append(",EMAIL=" + EMAIL);
			sb.append(",JOB_TITLE=" + JOB_TITLE);
			sb.append(",CREDITCARDNUMBER=" + CREDITCARDNUMBER);
			sb.append(",COMPANY=" + COMPANY);
			sb.append(",CITY=" + CITY);
			sb.append(",STATE=" + STATE);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (ID == null) {
				sb.append("<null>");
			} else {
				sb.append(ID);
			}

			sb.append("|");

			if (NAME == null) {
				sb.append("<null>");
			} else {
				sb.append(NAME);
			}

			sb.append("|");

			if (LAST_NAME == null) {
				sb.append("<null>");
			} else {
				sb.append(LAST_NAME);
			}

			sb.append("|");

			if (EMAIL == null) {
				sb.append("<null>");
			} else {
				sb.append(EMAIL);
			}

			sb.append("|");

			if (JOB_TITLE == null) {
				sb.append("<null>");
			} else {
				sb.append(JOB_TITLE);
			}

			sb.append("|");

			if (CREDITCARDNUMBER == null) {
				sb.append("<null>");
			} else {
				sb.append(CREDITCARDNUMBER);
			}

			sb.append("|");

			if (COMPANY == null) {
				sb.append("<null>");
			} else {
				sb.append(COMPANY);
			}

			sb.append("|");

			if (CITY == null) {
				sb.append("<null>");
			} else {
				sb.append(CITY);
			}

			sb.append("|");

			if (STATE == null) {
				sb.append("<null>");
			} else {
				sb.append(STATE);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row1Struct other) {

			int returnValue = -1;

			returnValue = checkNullsAndCompare(this.ID, other.ID);
			if (returnValue != 0) {
				return returnValue;
			}

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(), object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public static class Get_the_list_of_customersStruct
			implements routines.system.IPersistableRow<Get_the_list_of_customersStruct> {
		final static byte[] commonByteArrayLock_MBTC_POC_REST_Services = new byte[0];
		static byte[] commonByteArray_MBTC_POC_REST_Services = new byte[0];

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

				}

				finally {
				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

				}

				finally {
				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

			}

			finally {
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

			}

			finally {
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(Get_the_list_of_customersStruct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(), object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public static class Create_a_customerStruct implements routines.system.IPersistableRow<Create_a_customerStruct> {
		final static byte[] commonByteArrayLock_MBTC_POC_REST_Services = new byte[0];
		static byte[] commonByteArray_MBTC_POC_REST_Services = new byte[0];

		public String ID;

		public String getID() {
			return this.ID;
		}

		public Boolean IDIsNullable() {
			return true;
		}

		public Boolean IDIsKey() {
			return false;
		}

		public Integer IDLength() {
			return null;
		}

		public Integer IDPrecision() {
			return 0;
		}

		public String IDDefault() {

			return null;

		}

		public String IDComment() {

			return "";

		}

		public String IDPattern() {

			return "dd-MM-yyyy";

		}

		public String IDOriginalDbColumnName() {

			return "ID";

		}

		public String NAME;

		public String getNAME() {
			return this.NAME;
		}

		public Boolean NAMEIsNullable() {
			return true;
		}

		public Boolean NAMEIsKey() {
			return false;
		}

		public Integer NAMELength() {
			return 4;
		}

		public Integer NAMEPrecision() {
			return 0;
		}

		public String NAMEDefault() {

			return null;

		}

		public String NAMEComment() {

			return "";

		}

		public String NAMEPattern() {

			return "dd-MM-yyyy";

		}

		public String NAMEOriginalDbColumnName() {

			return "NAME";

		}

		public String LAST_NAME;

		public String getLAST_NAME() {
			return this.LAST_NAME;
		}

		public Boolean LAST_NAMEIsNullable() {
			return true;
		}

		public Boolean LAST_NAMEIsKey() {
			return false;
		}

		public Integer LAST_NAMELength() {
			return 5;
		}

		public Integer LAST_NAMEPrecision() {
			return 0;
		}

		public String LAST_NAMEDefault() {

			return null;

		}

		public String LAST_NAMEComment() {

			return "";

		}

		public String LAST_NAMEPattern() {

			return "dd-MM-yyyy";

		}

		public String LAST_NAMEOriginalDbColumnName() {

			return "LAST_NAME";

		}

		public String EMAIL;

		public String getEMAIL() {
			return this.EMAIL;
		}

		public Boolean EMAILIsNullable() {
			return true;
		}

		public Boolean EMAILIsKey() {
			return false;
		}

		public Integer EMAILLength() {
			return 21;
		}

		public Integer EMAILPrecision() {
			return 0;
		}

		public String EMAILDefault() {

			return null;

		}

		public String EMAILComment() {

			return "";

		}

		public String EMAILPattern() {

			return "dd-MM-yyyy";

		}

		public String EMAILOriginalDbColumnName() {

			return "EMAIL";

		}

		public String JOB_TITLE;

		public String getJOB_TITLE() {
			return this.JOB_TITLE;
		}

		public Boolean JOB_TITLEIsNullable() {
			return true;
		}

		public Boolean JOB_TITLEIsKey() {
			return false;
		}

		public Integer JOB_TITLELength() {
			return 19;
		}

		public Integer JOB_TITLEPrecision() {
			return 0;
		}

		public String JOB_TITLEDefault() {

			return null;

		}

		public String JOB_TITLEComment() {

			return "";

		}

		public String JOB_TITLEPattern() {

			return "dd-MM-yyyy";

		}

		public String JOB_TITLEOriginalDbColumnName() {

			return "JOB_TITLE";

		}

		public String CREDITCARDNUMBER;

		public String getCREDITCARDNUMBER() {
			return this.CREDITCARDNUMBER;
		}

		public Boolean CREDITCARDNUMBERIsNullable() {
			return true;
		}

		public Boolean CREDITCARDNUMBERIsKey() {
			return false;
		}

		public Integer CREDITCARDNUMBERLength() {
			return null;
		}

		public Integer CREDITCARDNUMBERPrecision() {
			return 0;
		}

		public String CREDITCARDNUMBERDefault() {

			return null;

		}

		public String CREDITCARDNUMBERComment() {

			return "";

		}

		public String CREDITCARDNUMBERPattern() {

			return "dd-MM-yyyy";

		}

		public String CREDITCARDNUMBEROriginalDbColumnName() {

			return "CREDITCARDNUMBER";

		}

		public String COMPANY;

		public String getCOMPANY() {
			return this.COMPANY;
		}

		public Boolean COMPANYIsNullable() {
			return true;
		}

		public Boolean COMPANYIsKey() {
			return false;
		}

		public Integer COMPANYLength() {
			return 7;
		}

		public Integer COMPANYPrecision() {
			return 0;
		}

		public String COMPANYDefault() {

			return null;

		}

		public String COMPANYComment() {

			return "";

		}

		public String COMPANYPattern() {

			return "dd-MM-yyyy";

		}

		public String COMPANYOriginalDbColumnName() {

			return "COMPANY";

		}

		public String CITY;

		public String getCITY() {
			return this.CITY;
		}

		public Boolean CITYIsNullable() {
			return true;
		}

		public Boolean CITYIsKey() {
			return false;
		}

		public Integer CITYLength() {
			return 13;
		}

		public Integer CITYPrecision() {
			return 0;
		}

		public String CITYDefault() {

			return null;

		}

		public String CITYComment() {

			return "";

		}

		public String CITYPattern() {

			return "dd-MM-yyyy";

		}

		public String CITYOriginalDbColumnName() {

			return "CITY";

		}

		public String STATE;

		public String getSTATE() {
			return this.STATE;
		}

		public Boolean STATEIsNullable() {
			return true;
		}

		public Boolean STATEIsKey() {
			return false;
		}

		public Integer STATELength() {
			return 255;
		}

		public Integer STATEPrecision() {
			return 0;
		}

		public String STATEDefault() {

			return null;

		}

		public String STATEComment() {

			return "";

		}

		public String STATEPattern() {

			return "dd-MM-yyyy";

		}

		public String STATEOriginalDbColumnName() {

			return "STATE";

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.ID = readString(dis);

					this.NAME = readString(dis);

					this.LAST_NAME = readString(dis);

					this.EMAIL = readString(dis);

					this.JOB_TITLE = readString(dis);

					this.CREDITCARDNUMBER = readString(dis);

					this.COMPANY = readString(dis);

					this.CITY = readString(dis);

					this.STATE = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.ID = readString(dis);

					this.NAME = readString(dis);

					this.LAST_NAME = readString(dis);

					this.EMAIL = readString(dis);

					this.JOB_TITLE = readString(dis);

					this.CREDITCARDNUMBER = readString(dis);

					this.COMPANY = readString(dis);

					this.CITY = readString(dis);

					this.STATE = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.ID, dos);

				// String

				writeString(this.NAME, dos);

				// String

				writeString(this.LAST_NAME, dos);

				// String

				writeString(this.EMAIL, dos);

				// String

				writeString(this.JOB_TITLE, dos);

				// String

				writeString(this.CREDITCARDNUMBER, dos);

				// String

				writeString(this.COMPANY, dos);

				// String

				writeString(this.CITY, dos);

				// String

				writeString(this.STATE, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// String

				writeString(this.ID, dos);

				// String

				writeString(this.NAME, dos);

				// String

				writeString(this.LAST_NAME, dos);

				// String

				writeString(this.EMAIL, dos);

				// String

				writeString(this.JOB_TITLE, dos);

				// String

				writeString(this.CREDITCARDNUMBER, dos);

				// String

				writeString(this.COMPANY, dos);

				// String

				writeString(this.CITY, dos);

				// String

				writeString(this.STATE, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("ID=" + ID);
			sb.append(",NAME=" + NAME);
			sb.append(",LAST_NAME=" + LAST_NAME);
			sb.append(",EMAIL=" + EMAIL);
			sb.append(",JOB_TITLE=" + JOB_TITLE);
			sb.append(",CREDITCARDNUMBER=" + CREDITCARDNUMBER);
			sb.append(",COMPANY=" + COMPANY);
			sb.append(",CITY=" + CITY);
			sb.append(",STATE=" + STATE);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (ID == null) {
				sb.append("<null>");
			} else {
				sb.append(ID);
			}

			sb.append("|");

			if (NAME == null) {
				sb.append("<null>");
			} else {
				sb.append(NAME);
			}

			sb.append("|");

			if (LAST_NAME == null) {
				sb.append("<null>");
			} else {
				sb.append(LAST_NAME);
			}

			sb.append("|");

			if (EMAIL == null) {
				sb.append("<null>");
			} else {
				sb.append(EMAIL);
			}

			sb.append("|");

			if (JOB_TITLE == null) {
				sb.append("<null>");
			} else {
				sb.append(JOB_TITLE);
			}

			sb.append("|");

			if (CREDITCARDNUMBER == null) {
				sb.append("<null>");
			} else {
				sb.append(CREDITCARDNUMBER);
			}

			sb.append("|");

			if (COMPANY == null) {
				sb.append("<null>");
			} else {
				sb.append(COMPANY);
			}

			sb.append("|");

			if (CITY == null) {
				sb.append("<null>");
			} else {
				sb.append(CITY);
			}

			sb.append("|");

			if (STATE == null) {
				sb.append("<null>");
			} else {
				sb.append(STATE);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(Create_a_customerStruct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(), object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public static class Delete_a_customerStruct implements routines.system.IPersistableRow<Delete_a_customerStruct> {
		final static byte[] commonByteArrayLock_MBTC_POC_REST_Services = new byte[0];
		static byte[] commonByteArray_MBTC_POC_REST_Services = new byte[0];

		public String id;

		public String getId() {
			return this.id;
		}

		public Boolean idIsNullable() {
			return false;
		}

		public Boolean idIsKey() {
			return false;
		}

		public Integer idLength() {
			return null;
		}

		public Integer idPrecision() {
			return null;
		}

		public String idDefault() {

			return null;

		}

		public String idComment() {

			return "path(id)";

		}

		public String idPattern() {

			return null;

		}

		public String idOriginalDbColumnName() {

			return null;

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.id = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.id = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.id, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// String

				writeString(this.id, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("id=" + id);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (id == null) {
				sb.append("<null>");
			} else {
				sb.append(id);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(Delete_a_customerStruct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(), object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public void tRESTRequest_1_LoopProcess(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tRESTRequest_1_Loop_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tRESTRequest_1_Loop", "WccS48_");

		String currentVirtualComponent = null;

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				Get_the_list_of_customersStruct Get_the_list_of_customers = new Get_the_list_of_customersStruct();
				row1Struct row1 = new row1Struct();
				sendCustomersStruct sendCustomers = new sendCustomersStruct();
				Create_a_customerStruct Create_a_customer = new Create_a_customerStruct();
				row2Struct row2 = new row2Struct();
				row2Struct row8 = row2;
				row3Struct row3 = new row3Struct();
				row5Struct row5 = new row5Struct();
				Delete_a_customerStruct Delete_a_customer = new Delete_a_customerStruct();
				ID_to_deleteStruct ID_to_delete = new ID_to_deleteStruct();
				row7Struct row7 = new row7Struct();

				/**
				 * [tRESTRequest_1_Loop begin ] start
				 */

				int NB_ITERATE_tRESTRequest_1_In = 0; // for statistics

				sh("tRESTRequest_1_Loop");

				currentVirtualComponent = "tRESTRequest_1";

				s(currentComponent = "tRESTRequest_1_Loop");

				int tos_count_tRESTRequest_1_Loop = 0;

				if (enableLogStash) {
					talendJobLog.addCM("tRESTRequest_1_Loop", "Customers_API_Loop", "tRESTRequestLoop");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				runStat.updateStatAndLog(execStat, enableLogStash, iterateId, 0, 0, "Get_the_list_of_customers",
						"Create_a_customer", "row2", "row8", "row3", "row5", "Delete_a_customer", "ID_to_delete",
						"row7");

				Delete_a_customer = null;

				Create_a_customer = null;

				Get_the_list_of_customers = null;

				int nb_line_tRESTRequest_1 = 0;

				try {

					java.util.Map<String, Object> requestMessage_tRESTRequest_1 = (java.util.Map<String, Object>) globalMap
							.get("restRequest");

					restEndpoint = getRestEndpoint();

					if (null == requestMessage_tRESTRequest_1) {

						if (restTalendJobAlreadyStarted) {
							throw new RuntimeException("request is not provided");
						} else {
							if (!runInTalendEsbRuntimeContainer && null == thread4RestServiceProviderEndpoint) {
								String endpointUrl_tRESTRequest_1 = checkEndpointUrl(restEndpoint);
								// *** external thread for endpoint initialization
								thread4RestServiceProviderEndpoint = new Thread4RestServiceProviderEndpoint(this,
										endpointUrl_tRESTRequest_1);
								thread4RestServiceProviderEndpoint.start();
								// *** external thread for endpoint initialization
							}

							restTalendJobAlreadyStarted = true;

							if (runInDaemonMode) {
								Thread.currentThread();
								try {
									while (true) {
										Thread.sleep(60000);
									}
								} catch (InterruptedException e_tRESTRequest_1) {
									// e_tRESTRequest_1.printStackTrace();
									// throw new TalendException(e_tRESTRequest_1, "wholeJob", globalMap);
								}
							}
						}
						return;
					}

					requestMessage_tRESTRequest_1.put("CURRENT_MESSAGE",
							org.apache.cxf.jaxrs.utils.JAXRSUtils.getCurrentMessage());

					Object ctx_tRESTRequest_1 = requestMessage_tRESTRequest_1.get("MESSAGE_CONTEXT");
					if (ctx_tRESTRequest_1 != null
							&& ctx_tRESTRequest_1 instanceof org.apache.cxf.jaxrs.impl.tl.ThreadLocalMessageContext) {
						requestMessage_tRESTRequest_1.put("MESSAGE_CONTEXT",
								((org.apache.cxf.jaxrs.impl.tl.ThreadLocalMessageContext) ctx_tRESTRequest_1).get());
					}

					/**
					 * [tRESTRequest_1_Loop begin ] stop
					 */

					/**
					 * [tRESTRequest_1_Loop main ] start
					 */

					currentVirtualComponent = "tRESTRequest_1";

					s(currentComponent = "tRESTRequest_1_Loop");

					resourceMap.put("inIterateVComp", true);

					tos_count_tRESTRequest_1_Loop++;

					/**
					 * [tRESTRequest_1_Loop main ] stop
					 */

					/**
					 * [tRESTRequest_1_Loop process_data_begin ] start
					 */

					currentVirtualComponent = "tRESTRequest_1";

					s(currentComponent = "tRESTRequest_1_Loop");

					/**
					 * [tRESTRequest_1_Loop process_data_begin ] stop
					 */

					NB_ITERATE_tRESTRequest_1_In++;

					if (execStat) {
						runStat.updateStatOnConnection("Iterate", 1, "exec" + NB_ITERATE_tRESTRequest_1_In);
						// Thread.sleep(1000);
					}

					/**
					 * [tFlowToIterate_1 begin ] start
					 */

					int NB_ITERATE_tDBInput_1 = 0; // for statistics

					sh("tFlowToIterate_1");

					s(currentComponent = "tFlowToIterate_1");

					runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0,
							"Get_the_list_of_customers");

					int tos_count_tFlowToIterate_1 = 0;

					if (log.isDebugEnabled())
						log.debug("tFlowToIterate_1 - " + ("Start to work."));
					if (log.isDebugEnabled()) {
						class BytesLimit65535_tFlowToIterate_1 {
							public void limitLog4jByte() throws Exception {
								StringBuilder log4jParamters_tFlowToIterate_1 = new StringBuilder();
								log4jParamters_tFlowToIterate_1.append("Parameters:");
								log4jParamters_tFlowToIterate_1.append("DEFAULT_MAP" + " = " + "true");
								log4jParamters_tFlowToIterate_1.append(" | ");
								if (log.isDebugEnabled())
									log.debug("tFlowToIterate_1 - " + (log4jParamters_tFlowToIterate_1));
							}
						}
						new BytesLimit65535_tFlowToIterate_1().limitLog4jByte();
					}
					if (enableLogStash) {
						talendJobLog.addCM("tFlowToIterate_1", "tFlowToIterate_1", "tFlowToIterate");
						talendJobLogProcess(globalMap);
						s(currentComponent);
					}

					int nb_line_tFlowToIterate_1 = 0;
					int counter_tFlowToIterate_1 = 0;

					/**
					 * [tFlowToIterate_1 begin ] stop
					 */

					/**
					 * [tWriteJSONField_1_Out begin ] start
					 */

					sh("tWriteJSONField_1_Out");

					currentVirtualComponent = "tWriteJSONField_1";

					s(currentComponent = "tWriteJSONField_1_Out");

					runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row3");

					int tos_count_tWriteJSONField_1_Out = 0;

					if (log.isDebugEnabled())
						log.debug("tWriteJSONField_1_Out - " + ("Start to work."));
					if (log.isDebugEnabled()) {
						class BytesLimit65535_tWriteJSONField_1_Out {
							public void limitLog4jByte() throws Exception {
								StringBuilder log4jParamters_tWriteJSONField_1_Out = new StringBuilder();
								log4jParamters_tWriteJSONField_1_Out.append("Parameters:");
								log4jParamters_tWriteJSONField_1_Out.append("GROUPBYS" + " = " + "[]");
								log4jParamters_tWriteJSONField_1_Out.append(" | ");
								log4jParamters_tWriteJSONField_1_Out.append("REMOVE_HEADER" + " = " + "false");
								log4jParamters_tWriteJSONField_1_Out.append(" | ");
								log4jParamters_tWriteJSONField_1_Out.append("CREATE" + " = " + "true");
								log4jParamters_tWriteJSONField_1_Out.append(" | ");
								log4jParamters_tWriteJSONField_1_Out.append("CREATE_EMPTY_ELEMENT" + " = " + "true");
								log4jParamters_tWriteJSONField_1_Out.append(" | ");
								log4jParamters_tWriteJSONField_1_Out.append("EXPAND_EMPTY_ELM" + " = " + "false");
								log4jParamters_tWriteJSONField_1_Out.append(" | ");
								log4jParamters_tWriteJSONField_1_Out.append("ALLOW_EMPTY_STRINGS" + " = " + "false");
								log4jParamters_tWriteJSONField_1_Out.append(" | ");
								log4jParamters_tWriteJSONField_1_Out.append("OUTPUT_AS_XSD" + " = " + "false");
								log4jParamters_tWriteJSONField_1_Out.append(" | ");
								log4jParamters_tWriteJSONField_1_Out.append("ADVANCED_SEPARATOR" + " = " + "false");
								log4jParamters_tWriteJSONField_1_Out.append(" | ");
								log4jParamters_tWriteJSONField_1_Out.append("COMPACT_FORMAT" + " = " + "true");
								log4jParamters_tWriteJSONField_1_Out.append(" | ");
								log4jParamters_tWriteJSONField_1_Out.append("GENERATION_MODE" + " = " + "Dom4j");
								log4jParamters_tWriteJSONField_1_Out.append(" | ");
								log4jParamters_tWriteJSONField_1_Out.append("ENCODING" + " = " + "\"ISO-8859-15\"");
								log4jParamters_tWriteJSONField_1_Out.append(" | ");
								log4jParamters_tWriteJSONField_1_Out
										.append("DESTINATION" + " = " + "tWriteJSONField_1");
								log4jParamters_tWriteJSONField_1_Out.append(" | ");
								if (log.isDebugEnabled())
									log.debug("tWriteJSONField_1_Out - " + (log4jParamters_tWriteJSONField_1_Out));
							}
						}
						new BytesLimit65535_tWriteJSONField_1_Out().limitLog4jByte();
					}
					if (enableLogStash) {
						talendJobLog.addCM("tWriteJSONField_1_Out", "tWriteJSONField_1_Out", "tWriteXMLFieldOut");
						talendJobLogProcess(globalMap);
						s(currentComponent);
					}

//tWriteXMLFieldOut_begin
					int nb_line_tWriteJSONField_1_Out = 0;
					boolean needRoot_tWriteJSONField_1_Out = true;

					String strCompCache_tWriteJSONField_1_Out = null;

					java.util.Queue<row4Struct> listGroupby_tWriteJSONField_1_Out = new java.util.concurrent.ConcurrentLinkedQueue<row4Struct>();

					class ThreadXMLField_tWriteJSONField_1_Out implements java.lang.Runnable {

						java.util.Queue<row4Struct> queue;

						java.util.List<java.util.Map<String, String>> flows;
						java.lang.Exception lastException;
						java.lang.Error lastError;
						String currentComponent;

						ThreadXMLField_tWriteJSONField_1_Out(java.util.Queue q) {
							this.queue = q;
							globalMap.put("queue_tWriteJSONField_1_In", queue);
							lastException = null;
						}

						ThreadXMLField_tWriteJSONField_1_Out(java.util.Queue q,
								java.util.List<java.util.Map<String, String>> l) {
							this.queue = q;
							this.flows = l;
							lastException = null;
							globalMap.put("queue_tWriteJSONField_1_In", queue);
							globalMap.put("flows_tWriteJSONField_1_In", flows);
						}

						public java.lang.Exception getLastException() {
							return this.lastException;
						}

						public java.lang.Error getLastError() {
							return this.lastError;
						}

						public String getCurrentComponent() {
							return this.currentComponent;
						}

						@Override
						public void run() {
							try {
								tWriteJSONField_1_InProcess(globalMap);
							} catch (TalendException te) {
								globalMap.put("tWriteJSONField_1_Out_ERROR_MESSAGE", te.getMessage());
								this.lastException = te.getException();
								this.currentComponent = te.getCurrentComponent();
							} catch (java.lang.Error error) {
								this.lastError = error;
							}
						}
					}

					ThreadXMLField_tWriteJSONField_1_Out txf_tWriteJSONField_1_Out = new ThreadXMLField_tWriteJSONField_1_Out(
							listGroupby_tWriteJSONField_1_Out);

					java.util.concurrent.Future<?> future_tWriteJSONField_1_Out = es.submit(txf_tWriteJSONField_1_Out);
					globalMap.put("wrtXMLFieldIn_tWriteJSONField_1_Out", future_tWriteJSONField_1_Out);

					java.util.List<java.util.List<String>> groupbyList_tWriteJSONField_1_Out = new java.util.ArrayList<java.util.List<String>>();
					java.util.Map<String, String> valueMap_tWriteJSONField_1_Out = new java.util.HashMap<String, String>();
					java.util.Map<String, String> arraysValueMap_tWriteJSONField_1_Out = new java.util.HashMap<String, String>();

					class NestXMLTool_tWriteJSONField_1_Out {
						public void parseAndAdd(org.dom4j.Element nestRoot, String value) {
							try {
								org.dom4j.Document doc4Str = org.dom4j.DocumentHelper
										.parseText("<root>" + value + "</root>");
								nestRoot.setContent(doc4Str.getRootElement().content());
							} catch (java.lang.Exception e) {
								globalMap.put("tWriteJSONField_1_Out_ERROR_MESSAGE", e.getMessage());
								e.printStackTrace();
								nestRoot.setText(value);
							}
						}

						public void setText(org.dom4j.Element element, String value) {
							if (value.startsWith("<![CDATA[") && value.endsWith("]]>")) {
								String text = value.substring(9, value.length() - 3);
								element.addCDATA(text);
							} else {
								element.setText(value);
							}
						}

						public void replaceDefaultNameSpace(org.dom4j.Element nestRoot) {
							if (nestRoot != null) {
								for (org.dom4j.Element tmp : (java.util.List<org.dom4j.Element>) nestRoot.elements()) {
									if (("").equals(tmp.getQName().getNamespace().getURI())
											&& ("").equals(tmp.getQName().getNamespace().getPrefix())) {
										tmp.setQName(org.dom4j.DocumentHelper.createQName(tmp.getName(),
												nestRoot.getQName().getNamespace()));
									}
									replaceDefaultNameSpace(tmp);
								}
							}
						}

						public void removeEmptyElement(org.dom4j.Element root) {
							if (root != null) {
								for (org.dom4j.Element tmp : (java.util.List<org.dom4j.Element>) root.elements()) {
									removeEmptyElement(tmp);
								}

								boolean noSignificantDataAnnotationsExist = root.attributes().isEmpty();
								if (root.content().isEmpty() && noSignificantDataAnnotationsExist
										&& root.declaredNamespaces().isEmpty()) {
									if (root.getParent() != null) {
										root.getParent().remove(root);
									}
								}
							}
						}

						public String objectToString(Object value) {
							if (value.getClass().isArray()) {
								StringBuilder sb = new StringBuilder();

								int length = java.lang.reflect.Array.getLength(value);
								for (int i = 0; i < length; i++) {
									Object obj = java.lang.reflect.Array.get(value, i);
									sb.append("<element>");
									sb.append(obj);
									sb.append("</element>");
								}
								return sb.toString();
							} else {
								return value.toString();
							}
						}
					}
					NestXMLTool_tWriteJSONField_1_Out nestXMLTool_tWriteJSONField_1_Out = new NestXMLTool_tWriteJSONField_1_Out();

					row3Struct rowStructOutput_tWriteJSONField_1_Out = new row3Struct();
// sort group root element for judgement of group
					java.util.List<org.dom4j.Element> groupElementList_tWriteJSONField_1_Out = new java.util.ArrayList<org.dom4j.Element>();
					org.dom4j.Element root4Group_tWriteJSONField_1_Out = null;
					org.dom4j.Document doc_tWriteJSONField_1_Out = org.dom4j.DocumentHelper.createDocument();
					org.dom4j.io.OutputFormat format_tWriteJSONField_1_Out = org.dom4j.io.OutputFormat
							.createCompactFormat();
					format_tWriteJSONField_1_Out.setNewLineAfterDeclaration(false);
					format_tWriteJSONField_1_Out.setTrimText(false);
					format_tWriteJSONField_1_Out.setEncoding("ISO-8859-15");
					int[] orders_tWriteJSONField_1_Out = new int[1];

					/**
					 * [tWriteJSONField_1_Out begin ] stop
					 */

					/**
					 * [tWriteJSONField_2_Out begin ] start
					 */

					sh("tWriteJSONField_2_Out");

					currentVirtualComponent = "tWriteJSONField_2";

					s(currentComponent = "tWriteJSONField_2_Out");

					runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row5");

					int tos_count_tWriteJSONField_2_Out = 0;

					if (log.isDebugEnabled())
						log.debug("tWriteJSONField_2_Out - " + ("Start to work."));
					if (log.isDebugEnabled()) {
						class BytesLimit65535_tWriteJSONField_2_Out {
							public void limitLog4jByte() throws Exception {
								StringBuilder log4jParamters_tWriteJSONField_2_Out = new StringBuilder();
								log4jParamters_tWriteJSONField_2_Out.append("Parameters:");
								log4jParamters_tWriteJSONField_2_Out.append("GROUPBYS" + " = " + "[]");
								log4jParamters_tWriteJSONField_2_Out.append(" | ");
								log4jParamters_tWriteJSONField_2_Out.append("REMOVE_HEADER" + " = " + "false");
								log4jParamters_tWriteJSONField_2_Out.append(" | ");
								log4jParamters_tWriteJSONField_2_Out.append("CREATE" + " = " + "true");
								log4jParamters_tWriteJSONField_2_Out.append(" | ");
								log4jParamters_tWriteJSONField_2_Out.append("CREATE_EMPTY_ELEMENT" + " = " + "true");
								log4jParamters_tWriteJSONField_2_Out.append(" | ");
								log4jParamters_tWriteJSONField_2_Out.append("EXPAND_EMPTY_ELM" + " = " + "false");
								log4jParamters_tWriteJSONField_2_Out.append(" | ");
								log4jParamters_tWriteJSONField_2_Out.append("ALLOW_EMPTY_STRINGS" + " = " + "false");
								log4jParamters_tWriteJSONField_2_Out.append(" | ");
								log4jParamters_tWriteJSONField_2_Out.append("OUTPUT_AS_XSD" + " = " + "false");
								log4jParamters_tWriteJSONField_2_Out.append(" | ");
								log4jParamters_tWriteJSONField_2_Out.append("ADVANCED_SEPARATOR" + " = " + "false");
								log4jParamters_tWriteJSONField_2_Out.append(" | ");
								log4jParamters_tWriteJSONField_2_Out.append("COMPACT_FORMAT" + " = " + "true");
								log4jParamters_tWriteJSONField_2_Out.append(" | ");
								log4jParamters_tWriteJSONField_2_Out.append("GENERATION_MODE" + " = " + "Dom4j");
								log4jParamters_tWriteJSONField_2_Out.append(" | ");
								log4jParamters_tWriteJSONField_2_Out.append("ENCODING" + " = " + "\"ISO-8859-15\"");
								log4jParamters_tWriteJSONField_2_Out.append(" | ");
								log4jParamters_tWriteJSONField_2_Out
										.append("DESTINATION" + " = " + "tWriteJSONField_2");
								log4jParamters_tWriteJSONField_2_Out.append(" | ");
								if (log.isDebugEnabled())
									log.debug("tWriteJSONField_2_Out - " + (log4jParamters_tWriteJSONField_2_Out));
							}
						}
						new BytesLimit65535_tWriteJSONField_2_Out().limitLog4jByte();
					}
					if (enableLogStash) {
						talendJobLog.addCM("tWriteJSONField_2_Out", "tWriteJSONField_2_Out", "tWriteXMLFieldOut");
						talendJobLogProcess(globalMap);
						s(currentComponent);
					}

//tWriteXMLFieldOut_begin
					int nb_line_tWriteJSONField_2_Out = 0;
					boolean needRoot_tWriteJSONField_2_Out = true;

					String strCompCache_tWriteJSONField_2_Out = null;

					java.util.Queue<row6Struct> listGroupby_tWriteJSONField_2_Out = new java.util.concurrent.ConcurrentLinkedQueue<row6Struct>();

					class ThreadXMLField_tWriteJSONField_2_Out implements java.lang.Runnable {

						java.util.Queue<row6Struct> queue;

						java.util.List<java.util.Map<String, String>> flows;
						java.lang.Exception lastException;
						java.lang.Error lastError;
						String currentComponent;

						ThreadXMLField_tWriteJSONField_2_Out(java.util.Queue q) {
							this.queue = q;
							globalMap.put("queue_tWriteJSONField_2_In", queue);
							lastException = null;
						}

						ThreadXMLField_tWriteJSONField_2_Out(java.util.Queue q,
								java.util.List<java.util.Map<String, String>> l) {
							this.queue = q;
							this.flows = l;
							lastException = null;
							globalMap.put("queue_tWriteJSONField_2_In", queue);
							globalMap.put("flows_tWriteJSONField_2_In", flows);
						}

						public java.lang.Exception getLastException() {
							return this.lastException;
						}

						public java.lang.Error getLastError() {
							return this.lastError;
						}

						public String getCurrentComponent() {
							return this.currentComponent;
						}

						@Override
						public void run() {
							try {
								tWriteJSONField_2_InProcess(globalMap);
							} catch (TalendException te) {
								globalMap.put("tWriteJSONField_2_Out_ERROR_MESSAGE", te.getMessage());
								this.lastException = te.getException();
								this.currentComponent = te.getCurrentComponent();
							} catch (java.lang.Error error) {
								this.lastError = error;
							}
						}
					}

					ThreadXMLField_tWriteJSONField_2_Out txf_tWriteJSONField_2_Out = new ThreadXMLField_tWriteJSONField_2_Out(
							listGroupby_tWriteJSONField_2_Out);

					java.util.concurrent.Future<?> future_tWriteJSONField_2_Out = es.submit(txf_tWriteJSONField_2_Out);
					globalMap.put("wrtXMLFieldIn_tWriteJSONField_2_Out", future_tWriteJSONField_2_Out);

					java.util.List<java.util.List<String>> groupbyList_tWriteJSONField_2_Out = new java.util.ArrayList<java.util.List<String>>();
					java.util.Map<String, String> valueMap_tWriteJSONField_2_Out = new java.util.HashMap<String, String>();
					java.util.Map<String, String> arraysValueMap_tWriteJSONField_2_Out = new java.util.HashMap<String, String>();

					class NestXMLTool_tWriteJSONField_2_Out {
						public void parseAndAdd(org.dom4j.Element nestRoot, String value) {
							try {
								org.dom4j.Document doc4Str = org.dom4j.DocumentHelper
										.parseText("<root>" + value + "</root>");
								nestRoot.setContent(doc4Str.getRootElement().content());
							} catch (java.lang.Exception e) {
								globalMap.put("tWriteJSONField_2_Out_ERROR_MESSAGE", e.getMessage());
								e.printStackTrace();
								nestRoot.setText(value);
							}
						}

						public void setText(org.dom4j.Element element, String value) {
							if (value.startsWith("<![CDATA[") && value.endsWith("]]>")) {
								String text = value.substring(9, value.length() - 3);
								element.addCDATA(text);
							} else {
								element.setText(value);
							}
						}

						public void replaceDefaultNameSpace(org.dom4j.Element nestRoot) {
							if (nestRoot != null) {
								for (org.dom4j.Element tmp : (java.util.List<org.dom4j.Element>) nestRoot.elements()) {
									if (("").equals(tmp.getQName().getNamespace().getURI())
											&& ("").equals(tmp.getQName().getNamespace().getPrefix())) {
										tmp.setQName(org.dom4j.DocumentHelper.createQName(tmp.getName(),
												nestRoot.getQName().getNamespace()));
									}
									replaceDefaultNameSpace(tmp);
								}
							}
						}

						public void removeEmptyElement(org.dom4j.Element root) {
							if (root != null) {
								for (org.dom4j.Element tmp : (java.util.List<org.dom4j.Element>) root.elements()) {
									removeEmptyElement(tmp);
								}

								boolean noSignificantDataAnnotationsExist = root.attributes().isEmpty();
								if (root.content().isEmpty() && noSignificantDataAnnotationsExist
										&& root.declaredNamespaces().isEmpty()) {
									if (root.getParent() != null) {
										root.getParent().remove(root);
									}
								}
							}
						}

						public String objectToString(Object value) {
							if (value.getClass().isArray()) {
								StringBuilder sb = new StringBuilder();

								int length = java.lang.reflect.Array.getLength(value);
								for (int i = 0; i < length; i++) {
									Object obj = java.lang.reflect.Array.get(value, i);
									sb.append("<element>");
									sb.append(obj);
									sb.append("</element>");
								}
								return sb.toString();
							} else {
								return value.toString();
							}
						}
					}
					NestXMLTool_tWriteJSONField_2_Out nestXMLTool_tWriteJSONField_2_Out = new NestXMLTool_tWriteJSONField_2_Out();

					row5Struct rowStructOutput_tWriteJSONField_2_Out = new row5Struct();
// sort group root element for judgement of group
					java.util.List<org.dom4j.Element> groupElementList_tWriteJSONField_2_Out = new java.util.ArrayList<org.dom4j.Element>();
					org.dom4j.Element root4Group_tWriteJSONField_2_Out = null;
					org.dom4j.Document doc_tWriteJSONField_2_Out = org.dom4j.DocumentHelper.createDocument();
					org.dom4j.io.OutputFormat format_tWriteJSONField_2_Out = org.dom4j.io.OutputFormat
							.createCompactFormat();
					format_tWriteJSONField_2_Out.setNewLineAfterDeclaration(false);
					format_tWriteJSONField_2_Out.setTrimText(false);
					format_tWriteJSONField_2_Out.setEncoding("ISO-8859-15");
					int[] orders_tWriteJSONField_2_Out = new int[1];

					/**
					 * [tWriteJSONField_2_Out begin ] stop
					 */

					/**
					 * [tDBOutput_1 begin ] start
					 */

					sh("tDBOutput_1");

					s(currentComponent = "tDBOutput_1");

					cLabel = "\"customers\"";

					runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row8");

					int tos_count_tDBOutput_1 = 0;

					if (log.isDebugEnabled())
						log.debug("tDBOutput_1 - " + ("Start to work."));
					if (log.isDebugEnabled()) {
						class BytesLimit65535_tDBOutput_1 {
							public void limitLog4jByte() throws Exception {
								StringBuilder log4jParamters_tDBOutput_1 = new StringBuilder();
								log4jParamters_tDBOutput_1.append("Parameters:");
								log4jParamters_tDBOutput_1.append("DB_VERSION" + " = " + "MYSQL_8");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("USE_EXISTING_CONNECTION" + " = " + "false");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("HOST" + " = " + "\"localhost\"");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("PORT" + " = " + "\"3306\"");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("DBNAME" + " = " + "\"training\"");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("USER" + " = " + "\"root\"");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("PASS" + " = " + String.valueOf(
										"enc:routine.encryption.key.v1:ovw8Q3kxVuaNJcL5Bk0NSFjIwMW6FcFejpG1JYB8qjhW")
										.substring(0, 4) + "...");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("TABLE" + " = " + "\"customers\"");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("TABLE_ACTION" + " = " + "NONE");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("DATA_ACTION" + " = " + "INSERT");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("SPECIFY_DATASOURCE_ALIAS" + " = " + "false");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("DIE_ON_ERROR" + " = " + "false");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("PROPERTIES" + " = "
										+ "\"noDatetimeStringSync=true&enabledTLSProtocols=TLSv1.2,TLSv1.1,TLSv1\"");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("USE_BATCH_SIZE" + " = " + "false");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("COMMIT_EVERY" + " = " + "10000");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("ADD_COLS" + " = " + "[]");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("USE_FIELD_OPTIONS" + " = " + "false");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("USE_HINT_OPTIONS" + " = " + "false");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("ENABLE_DEBUG_MODE" + " = " + "false");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("ON_DUPLICATE_KEY_UPDATE" + " = " + "false");
								log4jParamters_tDBOutput_1.append(" | ");
								log4jParamters_tDBOutput_1.append("UNIFIED_COMPONENTS" + " = " + "tMysqlOutput");
								log4jParamters_tDBOutput_1.append(" | ");
								if (log.isDebugEnabled())
									log.debug("tDBOutput_1 - " + (log4jParamters_tDBOutput_1));
							}
						}
						new BytesLimit65535_tDBOutput_1().limitLog4jByte();
					}
					if (enableLogStash) {
						talendJobLog.addCM("tDBOutput_1", "\"customers\"", "tMysqlOutput");
						talendJobLogProcess(globalMap);
						s(currentComponent);
					}

					int nb_line_tDBOutput_1 = 0;
					int nb_line_update_tDBOutput_1 = 0;
					int nb_line_inserted_tDBOutput_1 = 0;
					int nb_line_deleted_tDBOutput_1 = 0;
					int nb_line_rejected_tDBOutput_1 = 0;

					int deletedCount_tDBOutput_1 = 0;
					int updatedCount_tDBOutput_1 = 0;
					int insertedCount_tDBOutput_1 = 0;
					int rowsToCommitCount_tDBOutput_1 = 0;
					int rejectedCount_tDBOutput_1 = 0;

					String tableName_tDBOutput_1 = "customers";
					boolean whetherReject_tDBOutput_1 = false;

					java.util.Calendar calendar_tDBOutput_1 = java.util.Calendar.getInstance();
					calendar_tDBOutput_1.set(1, 0, 1, 0, 0, 0);
					long year1_tDBOutput_1 = calendar_tDBOutput_1.getTime().getTime();
					calendar_tDBOutput_1.set(10000, 0, 1, 0, 0, 0);
					long year10000_tDBOutput_1 = calendar_tDBOutput_1.getTime().getTime();
					long date_tDBOutput_1;

					java.sql.Connection conn_tDBOutput_1 = null;

					String properties_tDBOutput_1 = "noDatetimeStringSync=true&enabledTLSProtocols=TLSv1.2,TLSv1.1,TLSv1";
					if (properties_tDBOutput_1 == null || properties_tDBOutput_1.trim().length() == 0) {
						properties_tDBOutput_1 = "rewriteBatchedStatements=true&allowLoadLocalInfile=true";
					} else {
						if (!properties_tDBOutput_1.contains("rewriteBatchedStatements=")) {
							properties_tDBOutput_1 += "&rewriteBatchedStatements=true";
						}

						if (!properties_tDBOutput_1.contains("allowLoadLocalInfile=")) {
							properties_tDBOutput_1 += "&allowLoadLocalInfile=true";
						}
					}

					String url_tDBOutput_1 = "jdbc:mysql://" + "localhost" + ":" + "3306" + "/" + "training" + "?"
							+ properties_tDBOutput_1;

					String driverClass_tDBOutput_1 = "com.mysql.cj.jdbc.Driver";

					if (log.isDebugEnabled())
						log.debug("tDBOutput_1 - " + ("Driver ClassName: ") + (driverClass_tDBOutput_1) + ("."));
					String dbUser_tDBOutput_1 = "root";

					final String decryptedPassword_tDBOutput_1 = java.util.Optional
							.ofNullable(routines.system.PasswordEncryptUtil.decryptPassword(
									"enc:routine.encryption.key.v1:eynNyjchI8tHwtyPi6uz+fOPbhLBfNIAub47rqeEXW7x"))
							.orElse("");

					String dbPwd_tDBOutput_1 = decryptedPassword_tDBOutput_1;
					java.lang.Class.forName(driverClass_tDBOutput_1);

					if (log.isDebugEnabled())
						log.debug("tDBOutput_1 - " + ("Connection attempts to '") + (url_tDBOutput_1)
								+ ("' with the username '") + (dbUser_tDBOutput_1) + ("'."));
					conn_tDBOutput_1 = java.sql.DriverManager.getConnection(url_tDBOutput_1, dbUser_tDBOutput_1,
							dbPwd_tDBOutput_1);

					if (log.isDebugEnabled())
						log.debug("tDBOutput_1 - " + ("Connection to '") + (url_tDBOutput_1) + ("' has succeeded."));

					resourceMap.put("conn_tDBOutput_1", conn_tDBOutput_1);

					conn_tDBOutput_1.setAutoCommit(false);
					int commitEvery_tDBOutput_1 = 10000;
					int commitCounter_tDBOutput_1 = 0;

					if (log.isDebugEnabled())
						log.debug("tDBOutput_1 - " + ("Connection is set auto commit to '")
								+ (conn_tDBOutput_1.getAutoCommit()) + ("'."));

					int count_tDBOutput_1 = 0;

					String insert_tDBOutput_1 = "INSERT INTO `" + "customers"
							+ "` (`ID`,`NAME`,`LAST_NAME`,`EMAIL`,`JOB_TITLE`,`CREDITCARDNUMBER`,`COMPANY`,`CITY`,`STATE`) VALUES (?,?,?,?,?,?,?,?,?)";

					java.sql.PreparedStatement pstmt_tDBOutput_1 = conn_tDBOutput_1
							.prepareStatement(insert_tDBOutput_1);
					resourceMap.put("pstmt_tDBOutput_1", pstmt_tDBOutput_1);

					/**
					 * [tDBOutput_1 begin ] stop
					 */

					/**
					 * [tLogRow_1 begin ] start
					 */

					sh("tLogRow_1");

					s(currentComponent = "tLogRow_1");

					runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row2");

					int tos_count_tLogRow_1 = 0;

					if (log.isDebugEnabled())
						log.debug("tLogRow_1 - " + ("Start to work."));
					if (log.isDebugEnabled()) {
						class BytesLimit65535_tLogRow_1 {
							public void limitLog4jByte() throws Exception {
								StringBuilder log4jParamters_tLogRow_1 = new StringBuilder();
								log4jParamters_tLogRow_1.append("Parameters:");
								log4jParamters_tLogRow_1.append("BASIC_MODE" + " = " + "true");
								log4jParamters_tLogRow_1.append(" | ");
								log4jParamters_tLogRow_1.append("TABLE_PRINT" + " = " + "false");
								log4jParamters_tLogRow_1.append(" | ");
								log4jParamters_tLogRow_1.append("VERTICAL" + " = " + "false");
								log4jParamters_tLogRow_1.append(" | ");
								log4jParamters_tLogRow_1.append("FIELDSEPARATOR" + " = " + "\"|\"");
								log4jParamters_tLogRow_1.append(" | ");
								log4jParamters_tLogRow_1.append("PRINT_HEADER" + " = " + "false");
								log4jParamters_tLogRow_1.append(" | ");
								log4jParamters_tLogRow_1.append("PRINT_UNIQUE_NAME" + " = " + "false");
								log4jParamters_tLogRow_1.append(" | ");
								log4jParamters_tLogRow_1.append("PRINT_COLNAMES" + " = " + "false");
								log4jParamters_tLogRow_1.append(" | ");
								log4jParamters_tLogRow_1.append("USE_FIXED_LENGTH" + " = " + "false");
								log4jParamters_tLogRow_1.append(" | ");
								log4jParamters_tLogRow_1.append("PRINT_CONTENT_WITH_LOG4J" + " = " + "true");
								log4jParamters_tLogRow_1.append(" | ");
								if (log.isDebugEnabled())
									log.debug("tLogRow_1 - " + (log4jParamters_tLogRow_1));
							}
						}
						new BytesLimit65535_tLogRow_1().limitLog4jByte();
					}
					if (enableLogStash) {
						talendJobLog.addCM("tLogRow_1", "tLogRow_1", "tLogRow");
						talendJobLogProcess(globalMap);
						s(currentComponent);
					}

					///////////////////////

					final String OUTPUT_FIELD_SEPARATOR_tLogRow_1 = "|";
					java.io.PrintStream consoleOut_tLogRow_1 = null;

					StringBuilder strBuffer_tLogRow_1 = null;
					int nb_line_tLogRow_1 = 0;
///////////////////////    			

					/**
					 * [tLogRow_1 begin ] stop
					 */

					/**
					 * [tExtractJSONFields_1 begin ] start
					 */

					sh("tExtractJSONFields_1");

					s(currentComponent = "tExtractJSONFields_1");

					cLabel = "customer";

					runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0,
							"Create_a_customer");

					int tos_count_tExtractJSONFields_1 = 0;

					if (log.isDebugEnabled())
						log.debug("tExtractJSONFields_1 - " + ("Start to work."));
					if (log.isDebugEnabled()) {
						class BytesLimit65535_tExtractJSONFields_1 {
							public void limitLog4jByte() throws Exception {
								StringBuilder log4jParamters_tExtractJSONFields_1 = new StringBuilder();
								log4jParamters_tExtractJSONFields_1.append("Parameters:");
								log4jParamters_tExtractJSONFields_1.append("READ_BY" + " = " + "JSONPATH");
								log4jParamters_tExtractJSONFields_1.append(" | ");
								log4jParamters_tExtractJSONFields_1.append("JSON_PATH_VERSION" + " = " + "2_1_0");
								log4jParamters_tExtractJSONFields_1.append(" | ");
								log4jParamters_tExtractJSONFields_1.append("JSONFIELD" + " = " + "ID");
								log4jParamters_tExtractJSONFields_1.append(" | ");
								log4jParamters_tExtractJSONFields_1.append("JSON_LOOP_QUERY" + " = " + "\"$\"");
								log4jParamters_tExtractJSONFields_1.append(" | ");
								log4jParamters_tExtractJSONFields_1.append("MAPPING_4_JSONPATH" + " = " + "[{QUERY="
										+ ("\"ID\"") + ", SCHEMA_COLUMN=" + ("ID") + "}, {QUERY=" + ("\"NAME\"")
										+ ", SCHEMA_COLUMN=" + ("NAME") + "}, {QUERY=" + ("\"LAST_NAME\"")
										+ ", SCHEMA_COLUMN=" + ("LAST_NAME") + "}, {QUERY=" + ("\"EMAIL\"")
										+ ", SCHEMA_COLUMN=" + ("EMAIL") + "}, {QUERY=" + ("\"JOB_TITLE\"")
										+ ", SCHEMA_COLUMN=" + ("JOB_TITLE") + "}, {QUERY=" + ("\"CREDITCARDNUMBER\"")
										+ ", SCHEMA_COLUMN=" + ("CREDITCARDNUMBER") + "}, {QUERY=" + ("\"COMPANY\"")
										+ ", SCHEMA_COLUMN=" + ("COMPANY") + "}, {QUERY=" + ("\"CITY\"")
										+ ", SCHEMA_COLUMN=" + ("CITY") + "}, {QUERY=" + ("\"STATE\"")
										+ ", SCHEMA_COLUMN=" + ("STATE") + "}]");
								log4jParamters_tExtractJSONFields_1.append(" | ");
								log4jParamters_tExtractJSONFields_1.append("DIE_ON_ERROR" + " = " + "false");
								log4jParamters_tExtractJSONFields_1.append(" | ");
								log4jParamters_tExtractJSONFields_1.append("USE_LOOP_AS_ROOT" + " = " + "false");
								log4jParamters_tExtractJSONFields_1.append(" | ");
								if (log.isDebugEnabled())
									log.debug("tExtractJSONFields_1 - " + (log4jParamters_tExtractJSONFields_1));
							}
						}
						new BytesLimit65535_tExtractJSONFields_1().limitLog4jByte();
					}
					if (enableLogStash) {
						talendJobLog.addCM("tExtractJSONFields_1", "customer", "tExtractJSONFields");
						talendJobLogProcess(globalMap);
						s(currentComponent);
					}

					int nb_line_tExtractJSONFields_1 = 0;
					String jsonStr_tExtractJSONFields_1 = "";

					class JsonPathCache_tExtractJSONFields_1 {
						final java.util.Map<String, com.jayway.jsonpath.JsonPath> jsonPathString2compiledJsonPath = new java.util.HashMap<String, com.jayway.jsonpath.JsonPath>();

						public com.jayway.jsonpath.JsonPath getCompiledJsonPath(String jsonPath) {
							if (jsonPathString2compiledJsonPath.containsKey(jsonPath)) {
								return jsonPathString2compiledJsonPath.get(jsonPath);
							} else {
								com.jayway.jsonpath.JsonPath compiledLoopPath = com.jayway.jsonpath.JsonPath
										.compile(jsonPath);
								jsonPathString2compiledJsonPath.put(jsonPath, compiledLoopPath);
								return compiledLoopPath;
							}
						}
					}

					JsonPathCache_tExtractJSONFields_1 jsonPathCache_tExtractJSONFields_1 = new JsonPathCache_tExtractJSONFields_1();

					/**
					 * [tExtractJSONFields_1 begin ] stop
					 */

					/**
					 * [tRESTResponse_4 begin ] start
					 */

					sh("tRESTResponse_4");

					s(currentComponent = "tRESTResponse_4");

					runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row7");

					int tos_count_tRESTResponse_4 = 0;

					if (enableLogStash) {
						talendJobLog.addCM("tRESTResponse_4", "tRESTResponse_4", "tRESTResponse");
						talendJobLogProcess(globalMap);
						s(currentComponent);
					}

					/**
					 * [tRESTResponse_4 begin ] stop
					 */

					/**
					 * [tDBOutput_2 begin ] start
					 */

					sh("tDBOutput_2");

					s(currentComponent = "tDBOutput_2");

					cLabel = "\"customers\"";

					runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "ID_to_delete");

					int tos_count_tDBOutput_2 = 0;

					if (log.isDebugEnabled())
						log.debug("tDBOutput_2 - " + ("Start to work."));
					if (log.isDebugEnabled()) {
						class BytesLimit65535_tDBOutput_2 {
							public void limitLog4jByte() throws Exception {
								StringBuilder log4jParamters_tDBOutput_2 = new StringBuilder();
								log4jParamters_tDBOutput_2.append("Parameters:");
								log4jParamters_tDBOutput_2.append("DB_VERSION" + " = " + "MYSQL_8");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("USE_EXISTING_CONNECTION" + " = " + "false");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("HOST" + " = " + "\"localhost\"");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("PORT" + " = " + "\"3306\"");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("DBNAME" + " = " + "\"training\"");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("USER" + " = " + "\"root\"");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("PASS" + " = " + String.valueOf(
										"enc:routine.encryption.key.v1:1ZXuJW/8uDMNB/lnBpccHtepmsO/uO25iClrQyiiZ5cH")
										.substring(0, 4) + "...");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("TABLE" + " = " + "\"customers\"");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("TABLE_ACTION" + " = " + "NONE");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("DATA_ACTION" + " = " + "DELETE");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("SPECIFY_DATASOURCE_ALIAS" + " = " + "false");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("DIE_ON_ERROR" + " = " + "false");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("PROPERTIES" + " = "
										+ "\"noDatetimeStringSync=true&enabledTLSProtocols=TLSv1.2,TLSv1.1,TLSv1\"");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("USE_BATCH_SIZE" + " = " + "false");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("COMMIT_EVERY" + " = " + "10000");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("ADD_COLS" + " = " + "[]");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("USE_FIELD_OPTIONS" + " = " + "false");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("USE_HINT_OPTIONS" + " = " + "false");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("ENABLE_DEBUG_MODE" + " = " + "false");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("SUPPORT_NULL_WHERE" + " = " + "false");
								log4jParamters_tDBOutput_2.append(" | ");
								log4jParamters_tDBOutput_2.append("UNIFIED_COMPONENTS" + " = " + "tMysqlOutput");
								log4jParamters_tDBOutput_2.append(" | ");
								if (log.isDebugEnabled())
									log.debug("tDBOutput_2 - " + (log4jParamters_tDBOutput_2));
							}
						}
						new BytesLimit65535_tDBOutput_2().limitLog4jByte();
					}
					if (enableLogStash) {
						talendJobLog.addCM("tDBOutput_2", "\"customers\"", "tMysqlOutput");
						talendJobLogProcess(globalMap);
						s(currentComponent);
					}

					int deleteKeyCount_tDBOutput_2 = 1;
					if (deleteKeyCount_tDBOutput_2 < 1) {
						throw new RuntimeException("For delete, Schema must have a key");
					}

					int nb_line_tDBOutput_2 = 0;
					int nb_line_update_tDBOutput_2 = 0;
					int nb_line_inserted_tDBOutput_2 = 0;
					int nb_line_deleted_tDBOutput_2 = 0;
					int nb_line_rejected_tDBOutput_2 = 0;

					int deletedCount_tDBOutput_2 = 0;
					int updatedCount_tDBOutput_2 = 0;
					int insertedCount_tDBOutput_2 = 0;
					int rowsToCommitCount_tDBOutput_2 = 0;
					int rejectedCount_tDBOutput_2 = 0;

					String tableName_tDBOutput_2 = "customers";
					boolean whetherReject_tDBOutput_2 = false;

					java.util.Calendar calendar_tDBOutput_2 = java.util.Calendar.getInstance();
					calendar_tDBOutput_2.set(1, 0, 1, 0, 0, 0);
					long year1_tDBOutput_2 = calendar_tDBOutput_2.getTime().getTime();
					calendar_tDBOutput_2.set(10000, 0, 1, 0, 0, 0);
					long year10000_tDBOutput_2 = calendar_tDBOutput_2.getTime().getTime();
					long date_tDBOutput_2;

					java.sql.Connection conn_tDBOutput_2 = null;

					String properties_tDBOutput_2 = "noDatetimeStringSync=true&enabledTLSProtocols=TLSv1.2,TLSv1.1,TLSv1";
					if (properties_tDBOutput_2 == null || properties_tDBOutput_2.trim().length() == 0) {
						properties_tDBOutput_2 = "rewriteBatchedStatements=true&allowLoadLocalInfile=true";
					} else {
						if (!properties_tDBOutput_2.contains("rewriteBatchedStatements=")) {
							properties_tDBOutput_2 += "&rewriteBatchedStatements=true";
						}

						if (!properties_tDBOutput_2.contains("allowLoadLocalInfile=")) {
							properties_tDBOutput_2 += "&allowLoadLocalInfile=true";
						}
					}

					String url_tDBOutput_2 = "jdbc:mysql://" + "localhost" + ":" + "3306" + "/" + "training" + "?"
							+ properties_tDBOutput_2;

					String driverClass_tDBOutput_2 = "com.mysql.cj.jdbc.Driver";

					if (log.isDebugEnabled())
						log.debug("tDBOutput_2 - " + ("Driver ClassName: ") + (driverClass_tDBOutput_2) + ("."));
					String dbUser_tDBOutput_2 = "root";

					final String decryptedPassword_tDBOutput_2 = java.util.Optional
							.ofNullable(routines.system.PasswordEncryptUtil.decryptPassword(
									"enc:routine.encryption.key.v1:gxymRU46IfKtLMh94s3DaG2VpT7T2uHl51ysaRE2r2jc"))
							.orElse("");

					String dbPwd_tDBOutput_2 = decryptedPassword_tDBOutput_2;
					java.lang.Class.forName(driverClass_tDBOutput_2);

					if (log.isDebugEnabled())
						log.debug("tDBOutput_2 - " + ("Connection attempts to '") + (url_tDBOutput_2)
								+ ("' with the username '") + (dbUser_tDBOutput_2) + ("'."));
					conn_tDBOutput_2 = java.sql.DriverManager.getConnection(url_tDBOutput_2, dbUser_tDBOutput_2,
							dbPwd_tDBOutput_2);

					if (log.isDebugEnabled())
						log.debug("tDBOutput_2 - " + ("Connection to '") + (url_tDBOutput_2) + ("' has succeeded."));

					resourceMap.put("conn_tDBOutput_2", conn_tDBOutput_2);

					conn_tDBOutput_2.setAutoCommit(false);
					int commitEvery_tDBOutput_2 = 10000;
					int commitCounter_tDBOutput_2 = 0;

					if (log.isDebugEnabled())
						log.debug("tDBOutput_2 - " + ("Connection is set auto commit to '")
								+ (conn_tDBOutput_2.getAutoCommit()) + ("'."));

					int count_tDBOutput_2 = 0;

					String delete_tDBOutput_2 = "DELETE FROM `" + "customers" + "` WHERE `ID` = ?";

					java.sql.PreparedStatement pstmt_tDBOutput_2 = conn_tDBOutput_2
							.prepareStatement(delete_tDBOutput_2);
					resourceMap.put("pstmt_tDBOutput_2", pstmt_tDBOutput_2);

					/**
					 * [tDBOutput_2 begin ] stop
					 */

					/**
					 * [tMap_1 begin ] start
					 */

					sh("tMap_1");

					s(currentComponent = "tMap_1");

					runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0,
							"Delete_a_customer");

					int tos_count_tMap_1 = 0;

					if (log.isDebugEnabled())
						log.debug("tMap_1 - " + ("Start to work."));
					if (log.isDebugEnabled()) {
						class BytesLimit65535_tMap_1 {
							public void limitLog4jByte() throws Exception {
								StringBuilder log4jParamters_tMap_1 = new StringBuilder();
								log4jParamters_tMap_1.append("Parameters:");
								log4jParamters_tMap_1.append("LINK_STYLE" + " = " + "AUTO");
								log4jParamters_tMap_1.append(" | ");
								log4jParamters_tMap_1.append("TEMPORARY_DATA_DIRECTORY" + " = " + "");
								log4jParamters_tMap_1.append(" | ");
								log4jParamters_tMap_1.append("ROWS_BUFFER_SIZE" + " = " + "2000000");
								log4jParamters_tMap_1.append(" | ");
								log4jParamters_tMap_1.append("CHANGE_HASH_AND_EQUALS_FOR_BIGDECIMAL" + " = " + "true");
								log4jParamters_tMap_1.append(" | ");
								if (log.isDebugEnabled())
									log.debug("tMap_1 - " + (log4jParamters_tMap_1));
							}
						}
						new BytesLimit65535_tMap_1().limitLog4jByte();
					}
					if (enableLogStash) {
						talendJobLog.addCM("tMap_1", "tMap_1", "tMap");
						talendJobLogProcess(globalMap);
						s(currentComponent);
					}

// ###############################
// # Lookup's keys initialization
					int count_Delete_a_customer_tMap_1 = 0;

// ###############################        

// ###############################
// # Vars initialization
					class Var__tMap_1__Struct {
					}
					Var__tMap_1__Struct Var__tMap_1 = new Var__tMap_1__Struct();
// ###############################

// ###############################
// # Outputs initialization
					int count_ID_to_delete_tMap_1 = 0;

					ID_to_deleteStruct ID_to_delete_tmp = new ID_to_deleteStruct();
// ###############################

					/**
					 * [tMap_1 begin ] stop
					 */

					/**
					 * [tRESTRequest_1_In begin ] start
					 */

					sh("tRESTRequest_1_In");

					currentVirtualComponent = "tRESTRequest_1";

					s(currentComponent = "tRESTRequest_1_In");

					int tos_count_tRESTRequest_1_In = 0;

					if (enableLogStash) {
						talendJobLog.addCM("tRESTRequest_1_In", "Customers_API_In", "tRESTRequestIn");
						talendJobLogProcess(globalMap);
						s(currentComponent);
					}

					resourceMap.remove("inIterateVComp");

					/**
					 * [tRESTRequest_1_In begin ] stop
					 */

					/**
					 * [tRESTRequest_1_In main ] start
					 */

					currentVirtualComponent = "tRESTRequest_1";

					s(currentComponent = "tRESTRequest_1_In");

					if (requestMessage_tRESTRequest_1.containsKey("ERROR")) { // wrong request received
						Delete_a_customer = null;
						Create_a_customer = null;
						Get_the_list_of_customers = null;
					} else { // non-error (not wrong request)

						String matchedUriPattern_tRESTRequest_1 = (String) requestMessage_tRESTRequest_1.get("PATTERN");
						String matchedFlow_tRESTRequest_1 = (String) requestMessage_tRESTRequest_1.get("OPERATION");

						java.util.Map<String, Object> params_tRESTRequest_1 = (java.util.Map<String, Object>) requestMessage_tRESTRequest_1
								.get("PARAMS");
						if (matchedFlow_tRESTRequest_1.equals("Delete_a_customer")) {
							Delete_a_customer = new Delete_a_customerStruct();
							if (params_tRESTRequest_1.containsKey("PATH:id:id_String")) {

								Delete_a_customer.id = (String) params_tRESTRequest_1.get("PATH:id:id_String");

							}
						} else { // non matched flow
							Delete_a_customer = null;
						}

						if (matchedFlow_tRESTRequest_1.equals("Create_a_customer")) {
							Create_a_customer = new Create_a_customerStruct();
							if (params_tRESTRequest_1.containsKey("PATH:ID:id_String")) {

								Create_a_customer.ID = (String) params_tRESTRequest_1.get("PATH:ID:id_String");

							}
							if (params_tRESTRequest_1.containsKey("PATH:NAME:id_String")) {

								Create_a_customer.NAME = (String) params_tRESTRequest_1.get("PATH:NAME:id_String");

							}
							if (params_tRESTRequest_1.containsKey("PATH:LAST_NAME:id_String")) {

								Create_a_customer.LAST_NAME = (String) params_tRESTRequest_1
										.get("PATH:LAST_NAME:id_String");

							}
							if (params_tRESTRequest_1.containsKey("PATH:EMAIL:id_String")) {

								Create_a_customer.EMAIL = (String) params_tRESTRequest_1.get("PATH:EMAIL:id_String");

							}
							if (params_tRESTRequest_1.containsKey("PATH:JOB_TITLE:id_String")) {

								Create_a_customer.JOB_TITLE = (String) params_tRESTRequest_1
										.get("PATH:JOB_TITLE:id_String");

							}
							if (params_tRESTRequest_1.containsKey("PATH:CREDITCARDNUMBER:id_String")) {

								Create_a_customer.CREDITCARDNUMBER = (String) params_tRESTRequest_1
										.get("PATH:CREDITCARDNUMBER:id_String");

							}
							if (params_tRESTRequest_1.containsKey("PATH:COMPANY:id_String")) {

								Create_a_customer.COMPANY = (String) params_tRESTRequest_1
										.get("PATH:COMPANY:id_String");

							}
							if (params_tRESTRequest_1.containsKey("PATH:CITY:id_String")) {

								Create_a_customer.CITY = (String) params_tRESTRequest_1.get("PATH:CITY:id_String");

							}
							if (params_tRESTRequest_1.containsKey("PATH:STATE:id_String")) {

								Create_a_customer.STATE = (String) params_tRESTRequest_1.get("PATH:STATE:id_String");

							}
						} else { // non matched flow
							Create_a_customer = null;
						}

						if (matchedFlow_tRESTRequest_1.equals("Get_the_list_of_customers")) {
							Get_the_list_of_customers = new Get_the_list_of_customersStruct();
						} else { // non matched flow
							Get_the_list_of_customers = null;
						}

					}

					globalMap.put("tRESTRequest_1_URI", (String) requestMessage_tRESTRequest_1.get("URI"));
					globalMap.put("tRESTRequest_1_URI_BASE", (String) requestMessage_tRESTRequest_1.get("URI_BASE"));
					globalMap.put("tRESTRequest_1_URI_ABSOLUTE",
							(String) requestMessage_tRESTRequest_1.get("URI_ABSOLUTE"));
					globalMap.put("tRESTRequest_1_URI_REQUEST",
							(String) requestMessage_tRESTRequest_1.get("URI_REQUEST"));
					globalMap.put("tRESTRequest_1_HTTP_METHOD", (String) requestMessage_tRESTRequest_1.get("VERB"));

					globalMap.put("tRESTRequest_1_ATTACHMENT_HEADERS",
							requestMessage_tRESTRequest_1.get("ATTACHMENT_HEADERS"));
					globalMap.put("tRESTRequest_1_ATTACHMENT_FILENAMES",
							requestMessage_tRESTRequest_1.get("ATTACHMENT_FILENAMES"));

					globalMap.put("tRESTRequest_1_PRINCIPAL_NAME",
							(String) requestMessage_tRESTRequest_1.get("PRINCIPAL_NAME"));
					globalMap.put("tRESTRequest_1_CORRELATION_ID",
							(String) requestMessage_tRESTRequest_1.get("CorrelationID"));

					tos_count_tRESTRequest_1_In++;

					/**
					 * [tRESTRequest_1_In main ] stop
					 */

					/**
					 * [tRESTRequest_1_In process_data_begin ] start
					 */

					currentVirtualComponent = "tRESTRequest_1";

					s(currentComponent = "tRESTRequest_1_In");

					/**
					 * [tRESTRequest_1_In process_data_begin ] stop
					 */

// Start of branch "Get_the_list_of_customers"
					if (Get_the_list_of_customers != null) {

						/**
						 * [tFlowToIterate_1 main ] start
						 */

						s(currentComponent = "tFlowToIterate_1");

						if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

								, "Get_the_list_of_customers", "tRESTRequest_1_In", "Customers_API_In",
								"tRESTRequestIn", "tFlowToIterate_1", "tFlowToIterate_1", "tFlowToIterate"

						)) {
							talendJobLogProcess(globalMap);
						}

						if (log.isTraceEnabled()) {
							log.trace("Get_the_list_of_customers - " + (Get_the_list_of_customers == null ? ""
									: Get_the_list_of_customers.toLogString()));
						}

						nb_line_tFlowToIterate_1++;
						counter_tFlowToIterate_1++;
						if (log.isDebugEnabled())
							log.debug("tFlowToIterate_1 - " + ("Current iteration is: ") + (counter_tFlowToIterate_1)
									+ ("."));
						globalMap.put("tFlowToIterate_1_CURRENT_ITERATION", counter_tFlowToIterate_1);

						tos_count_tFlowToIterate_1++;

						/**
						 * [tFlowToIterate_1 main ] stop
						 */

						/**
						 * [tFlowToIterate_1 process_data_begin ] start
						 */

						s(currentComponent = "tFlowToIterate_1");

						if (Get_the_list_of_customers != null) {

						}

						/**
						 * [tFlowToIterate_1 process_data_begin ] stop
						 */

						NB_ITERATE_tDBInput_1++;

						if (execStat) {
							runStat.updateStatOnConnection("OnRowsEnd", 3, 0);
						}

						if (execStat) {
							runStat.updateStatOnConnection("row1", 3, 0);
						}

						if (execStat) {
							runStat.updateStatOnConnection("sendCustomers", 3, 0);
						}

						if (execStat) {
							runStat.updateStatOnConnection("iterate1", 1, "exec" + NB_ITERATE_tDBInput_1);
							// Thread.sleep(1000);
						}

						/**
						 * [tHMap_1_THMAP_OUT begin ] start
						 */

						sh("tHMap_1_THMAP_OUT");

						currentVirtualComponent = "tHMap_1";

						s(currentComponent = "tHMap_1_THMAP_OUT");

						runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row1");

						int tos_count_tHMap_1_THMAP_OUT = 0;

						if (enableLogStash) {
							talendJobLog.addCM("tHMap_1_THMAP_OUT", "tHMap_1_THMAP_OUT", "tHMapOut");
							talendJobLogProcess(globalMap);
							s(currentComponent);
						}

						// THMAPOUT_BEGIN thMap: tHMap_1_THMAP_OUT
						int nb_line_tHMap_1 = 0;
						java.util.List<java.util.Map<String, Object>> list_tHMap_1 = new java.util.ArrayList<>();
						globalMap.put("tHMap_1_source_struct_name", "row1");
						globalMap.put("tHMap_1_target_struct_name", "Customers");

						/**
						 * [tHMap_1_THMAP_OUT begin ] stop
						 */

						/**
						 * [tDBInput_1 begin ] start
						 */

						sh("tDBInput_1");

						s(currentComponent = "tDBInput_1");

						cLabel = "\"customers\"";

						int tos_count_tDBInput_1 = 0;

						if (log.isDebugEnabled())
							log.debug("tDBInput_1 - " + ("Start to work."));
						if (log.isDebugEnabled()) {
							class BytesLimit65535_tDBInput_1 {
								public void limitLog4jByte() throws Exception {
									StringBuilder log4jParamters_tDBInput_1 = new StringBuilder();
									log4jParamters_tDBInput_1.append("Parameters:");
									log4jParamters_tDBInput_1.append("DB_VERSION" + " = " + "MYSQL_8");
									log4jParamters_tDBInput_1.append(" | ");
									log4jParamters_tDBInput_1.append("USE_EXISTING_CONNECTION" + " = " + "false");
									log4jParamters_tDBInput_1.append(" | ");
									log4jParamters_tDBInput_1.append("HOST" + " = " + "\"localhost\"");
									log4jParamters_tDBInput_1.append(" | ");
									log4jParamters_tDBInput_1.append("PORT" + " = " + "\"3306\"");
									log4jParamters_tDBInput_1.append(" | ");
									log4jParamters_tDBInput_1.append("DBNAME" + " = " + "\"training\"");
									log4jParamters_tDBInput_1.append(" | ");
									log4jParamters_tDBInput_1.append("USER" + " = " + "\"root\"");
									log4jParamters_tDBInput_1.append(" | ");
									log4jParamters_tDBInput_1.append("PASS" + " = " + String.valueOf(
											"enc:routine.encryption.key.v1:h+ic848sFzgLACrWRCOvJkL5bk/IeEwlzK9WzMbC2NiB")
											.substring(0, 4) + "...");
									log4jParamters_tDBInput_1.append(" | ");
									log4jParamters_tDBInput_1.append("TABLE" + " = " + "\"customers\"");
									log4jParamters_tDBInput_1.append(" | ");
									log4jParamters_tDBInput_1.append("QUERYSTORE" + " = " + "\"\"");
									log4jParamters_tDBInput_1.append(" | ");
									log4jParamters_tDBInput_1.append("QUERY" + " = "
											+ "\"SELECT    `customers`.`ID`,    `customers`.`NAME`,    `customers`.`LAST_NAME`,    `customers`.`EMAIL`,    `customers`.`JOB_TITLE`,    `customers`.`CREDITCARDNUMBER`,    `customers`.`COMPANY`,    `customers`.`CITY`,    `customers`.`STATE`  FROM `customers`\"");
									log4jParamters_tDBInput_1.append(" | ");
									log4jParamters_tDBInput_1.append("SPECIFY_DATASOURCE_ALIAS" + " = " + "false");
									log4jParamters_tDBInput_1.append(" | ");
									log4jParamters_tDBInput_1.append("PROPERTIES" + " = "
											+ "\"noDatetimeStringSync=true&enabledTLSProtocols=TLSv1.2,TLSv1.1,TLSv1\"");
									log4jParamters_tDBInput_1.append(" | ");
									log4jParamters_tDBInput_1.append("ENABLE_STREAM" + " = " + "false");
									log4jParamters_tDBInput_1.append(" | ");
									log4jParamters_tDBInput_1.append("TRIM_ALL_COLUMN" + " = " + "false");
									log4jParamters_tDBInput_1.append(" | ");
									log4jParamters_tDBInput_1.append("TRIM_COLUMN" + " = " + "[{TRIM=" + ("false")
											+ ", SCHEMA_COLUMN=" + ("ID") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN="
											+ ("NAME") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN=" + ("LAST_NAME")
											+ "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN=" + ("EMAIL") + "}, {TRIM="
											+ ("false") + ", SCHEMA_COLUMN=" + ("JOB_TITLE") + "}, {TRIM=" + ("false")
											+ ", SCHEMA_COLUMN=" + ("CREDITCARDNUMBER") + "}, {TRIM=" + ("false")
											+ ", SCHEMA_COLUMN=" + ("COMPANY") + "}, {TRIM=" + ("false")
											+ ", SCHEMA_COLUMN=" + ("CITY") + "}, {TRIM=" + ("false")
											+ ", SCHEMA_COLUMN=" + ("STATE") + "}]");
									log4jParamters_tDBInput_1.append(" | ");
									log4jParamters_tDBInput_1.append("UNIFIED_COMPONENTS" + " = " + "tMysqlInput");
									log4jParamters_tDBInput_1.append(" | ");
									if (log.isDebugEnabled())
										log.debug("tDBInput_1 - " + (log4jParamters_tDBInput_1));
								}
							}
							new BytesLimit65535_tDBInput_1().limitLog4jByte();
						}
						if (enableLogStash) {
							talendJobLog.addCM("tDBInput_1", "\"customers\"", "tMysqlInput");
							talendJobLogProcess(globalMap);
							s(currentComponent);
						}

						java.util.Calendar calendar_tDBInput_1 = java.util.Calendar.getInstance();
						calendar_tDBInput_1.set(0, 0, 0, 0, 0, 0);
						java.util.Date year0_tDBInput_1 = calendar_tDBInput_1.getTime();
						int nb_line_tDBInput_1 = 0;
						java.sql.Connection conn_tDBInput_1 = null;
						String driverClass_tDBInput_1 = "com.mysql.cj.jdbc.Driver";
						java.lang.Class jdbcclazz_tDBInput_1 = java.lang.Class.forName(driverClass_tDBInput_1);
						String dbUser_tDBInput_1 = "root";

						final String decryptedPassword_tDBInput_1 = java.util.Optional
								.ofNullable(routines.system.PasswordEncryptUtil.decryptPassword(
										"enc:routine.encryption.key.v1:VXD83ghrhcSZ0owMVU0Ua8mRktm3SCXcR5BRas/KrObi"))
								.orElse("");

						String dbPwd_tDBInput_1 = decryptedPassword_tDBInput_1;

						String properties_tDBInput_1 = "noDatetimeStringSync=true&enabledTLSProtocols=TLSv1.2,TLSv1.1,TLSv1";
						if (properties_tDBInput_1 == null || properties_tDBInput_1.trim().length() == 0) {
							properties_tDBInput_1 = "";
						}
						String url_tDBInput_1 = "jdbc:mysql://" + "localhost" + ":" + "3306" + "/" + "training" + "?"
								+ properties_tDBInput_1;

						log.debug("tDBInput_1 - Driver ClassName: " + driverClass_tDBInput_1 + ".");

						log.debug("tDBInput_1 - Connection attempt to '"
								+ url_tDBInput_1.replaceAll("(?<=trustStorePassword=)[^;]*", "********")
								+ "' with the username '" + dbUser_tDBInput_1 + "'.");

						conn_tDBInput_1 = java.sql.DriverManager.getConnection(url_tDBInput_1, dbUser_tDBInput_1,
								dbPwd_tDBInput_1);
						log.debug("tDBInput_1 - Connection to '"
								+ url_tDBInput_1.replaceAll("(?<=trustStorePassword=)[^;]*", "********")
								+ "' has succeeded.");

						java.sql.Statement stmt_tDBInput_1 = conn_tDBInput_1.createStatement();

						String dbquery_tDBInput_1 = new StringBuilder().append(
								"SELECT \n  `customers`.`ID`, \n  `customers`.`NAME`, \n  `customers`.`LAST_NAME`, \n  `customers`.`EMAIL`, \n  `customers`.`"
										+ "JOB_TITLE`, \n  `customers`.`CREDITCARDNUMBER`, \n  `customers`.`COMPANY`, \n  `customers`.`CITY`, \n  `customers`.`STATE`\n "
										+ "FROM `customers`")
								.toString();

						log.debug("tDBInput_1 - Executing the query: '" + dbquery_tDBInput_1 + "'.");

						globalMap.put("tDBInput_1_QUERY", dbquery_tDBInput_1);

						java.sql.ResultSet rs_tDBInput_1 = null;

						try {
							rs_tDBInput_1 = stmt_tDBInput_1.executeQuery(dbquery_tDBInput_1);
							java.sql.ResultSetMetaData rsmd_tDBInput_1 = rs_tDBInput_1.getMetaData();
							int colQtyInRs_tDBInput_1 = rsmd_tDBInput_1.getColumnCount();

							String tmpContent_tDBInput_1 = null;

							log.debug("tDBInput_1 - Retrieving records from the database.");

							while (rs_tDBInput_1.next()) {
								nb_line_tDBInput_1++;

								if (colQtyInRs_tDBInput_1 < 1) {
									row1.ID = null;
								} else {

									row1.ID = routines.system.JDBCUtil.getString(rs_tDBInput_1, 1, false);
								}
								if (colQtyInRs_tDBInput_1 < 2) {
									row1.NAME = null;
								} else {

									row1.NAME = routines.system.JDBCUtil.getString(rs_tDBInput_1, 2, false);
								}
								if (colQtyInRs_tDBInput_1 < 3) {
									row1.LAST_NAME = null;
								} else {

									row1.LAST_NAME = routines.system.JDBCUtil.getString(rs_tDBInput_1, 3, false);
								}
								if (colQtyInRs_tDBInput_1 < 4) {
									row1.EMAIL = null;
								} else {

									row1.EMAIL = routines.system.JDBCUtil.getString(rs_tDBInput_1, 4, false);
								}
								if (colQtyInRs_tDBInput_1 < 5) {
									row1.JOB_TITLE = null;
								} else {

									row1.JOB_TITLE = routines.system.JDBCUtil.getString(rs_tDBInput_1, 5, false);
								}
								if (colQtyInRs_tDBInput_1 < 6) {
									row1.CREDITCARDNUMBER = null;
								} else {

									row1.CREDITCARDNUMBER = routines.system.JDBCUtil.getString(rs_tDBInput_1, 6, false);
								}
								if (colQtyInRs_tDBInput_1 < 7) {
									row1.COMPANY = null;
								} else {

									row1.COMPANY = routines.system.JDBCUtil.getString(rs_tDBInput_1, 7, false);
								}
								if (colQtyInRs_tDBInput_1 < 8) {
									row1.CITY = null;
								} else {

									row1.CITY = routines.system.JDBCUtil.getString(rs_tDBInput_1, 8, false);
								}
								if (colQtyInRs_tDBInput_1 < 9) {
									row1.STATE = null;
								} else {

									row1.STATE = routines.system.JDBCUtil.getString(rs_tDBInput_1, 9, false);
								}

								log.debug("tDBInput_1 - Retrieving the record " + nb_line_tDBInput_1 + ".");

								/**
								 * [tDBInput_1 begin ] stop
								 */

								/**
								 * [tDBInput_1 main ] start
								 */

								s(currentComponent = "tDBInput_1");

								cLabel = "\"customers\"";

								tos_count_tDBInput_1++;

								/**
								 * [tDBInput_1 main ] stop
								 */

								/**
								 * [tDBInput_1 process_data_begin ] start
								 */

								s(currentComponent = "tDBInput_1");

								cLabel = "\"customers\"";

								/**
								 * [tDBInput_1 process_data_begin ] stop
								 */

								/**
								 * [tHMap_1_THMAP_OUT main ] start
								 */

								currentVirtualComponent = "tHMap_1";

								s(currentComponent = "tHMap_1_THMAP_OUT");

								if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

										, "row1", "tDBInput_1", "\"customers\"", "tMysqlInput", "tHMap_1_THMAP_OUT",
										"tHMap_1_THMAP_OUT", "tHMapOut"

								)) {
									talendJobLogProcess(globalMap);
								}

								if (log.isTraceEnabled()) {
									log.trace("row1 - " + (row1 == null ? "" : row1.toLogString()));
								}

								// THMAPOUT_MAIN thMap: tHMap_1_THMAP_OUT
								// Main job's main incomingConn: row1
								java.util.List<java.util.Map<String, Object>> list_row1_tHMap_1 = list_tHMap_1;
								java.util.Map<String, Object> map_row1_tHMap_1 = new java.util.HashMap<>();
								Class<?> connectionClass_row1_tHMap_1 = row1.getClass();
								for (java.lang.reflect.Field f_row1_tHMap_1 : connectionClass_row1_tHMap_1
										.getFields()) {
									map_row1_tHMap_1.put(f_row1_tHMap_1.getName(), f_row1_tHMap_1.get(row1));
								}
								list_row1_tHMap_1.add(map_row1_tHMap_1);
								nb_line_tHMap_1++;

								tos_count_tHMap_1_THMAP_OUT++;

								/**
								 * [tHMap_1_THMAP_OUT main ] stop
								 */

								/**
								 * [tHMap_1_THMAP_OUT process_data_begin ] start
								 */

								currentVirtualComponent = "tHMap_1";

								s(currentComponent = "tHMap_1_THMAP_OUT");

								/**
								 * [tHMap_1_THMAP_OUT process_data_begin ] stop
								 */

								/**
								 * [tHMap_1_THMAP_OUT process_data_end ] start
								 */

								currentVirtualComponent = "tHMap_1";

								s(currentComponent = "tHMap_1_THMAP_OUT");

								/**
								 * [tHMap_1_THMAP_OUT process_data_end ] stop
								 */

								/**
								 * [tDBInput_1 process_data_end ] start
								 */

								s(currentComponent = "tDBInput_1");

								cLabel = "\"customers\"";

								/**
								 * [tDBInput_1 process_data_end ] stop
								 */

								/**
								 * [tDBInput_1 end ] start
								 */

								s(currentComponent = "tDBInput_1");

								cLabel = "\"customers\"";

							}
						} finally {
							if (rs_tDBInput_1 != null) {
								rs_tDBInput_1.close();
							}
							if (stmt_tDBInput_1 != null) {
								stmt_tDBInput_1.close();
							}
							if (conn_tDBInput_1 != null && !conn_tDBInput_1.isClosed()) {

								log.debug("tDBInput_1 - Closing the connection to the database.");

								conn_tDBInput_1.close();

								if ("com.mysql.cj.jdbc.Driver".equals((String) globalMap.get("driverClass_"))
										&& routines.system.BundleUtils.inOSGi()) {
									Class.forName("com.mysql.cj.jdbc.AbandonedConnectionCleanupThread")
											.getMethod("checkedShutdown").invoke(null, (Object[]) null);
								}

								log.debug("tDBInput_1 - Connection to the database closed.");

							}

						}
						globalMap.put("tDBInput_1_NB_LINE", nb_line_tDBInput_1);
						log.debug("tDBInput_1 - Retrieved records count: " + nb_line_tDBInput_1 + " .");

						if (log.isDebugEnabled())
							log.debug("tDBInput_1 - " + ("Done."));

						ok_Hash.put("tDBInput_1", true);
						end_Hash.put("tDBInput_1", System.currentTimeMillis());

						/**
						 * [tDBInput_1 end ] stop
						 */

						/**
						 * [tHMap_1_THMAP_OUT end ] start
						 */

						currentVirtualComponent = "tHMap_1";

						s(currentComponent = "tHMap_1_THMAP_OUT");

						if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row1", 2, 0,
								"tDBInput_1", "\"customers\"", "tMysqlInput", "tHMap_1_THMAP_OUT", "tHMap_1_THMAP_OUT",
								"tHMapOut", "output")) {
							talendJobLogProcess(globalMap);
						}

						ok_Hash.put("tHMap_1_THMAP_OUT", true);
						end_Hash.put("tHMap_1_THMAP_OUT", System.currentTimeMillis());

						/**
						 * [tHMap_1_THMAP_OUT end ] stop
						 */

						/**
						 * [tRESTResponse_1 begin ] start
						 */

						sh("tRESTResponse_1");

						s(currentComponent = "tRESTResponse_1");

						runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0,
								"sendCustomers");

						int tos_count_tRESTResponse_1 = 0;

						if (enableLogStash) {
							talendJobLog.addCM("tRESTResponse_1", "tRESTResponse_1", "tRESTResponse");
							talendJobLogProcess(globalMap);
							s(currentComponent);
						}

						/**
						 * [tRESTResponse_1 begin ] stop
						 */

						/**
						 * [tHMap_1_THMAP_IN begin ] start
						 */

						sh("tHMap_1_THMAP_IN");

						currentVirtualComponent = "tHMap_1";

						s(currentComponent = "tHMap_1_THMAP_IN");

						int tos_count_tHMap_1_THMAP_IN = 0;

						if (enableLogStash) {
							talendJobLog.addCM("tHMap_1_THMAP_IN", "tHMap_1_THMAP_IN", "tHMapIn");
							talendJobLogProcess(globalMap);
							s(currentComponent);
						}

						// THMAPIN_BEGIN tHMap: tHMap_1_THMAP_IN
						boolean inOsgi_tHMap_1 = routines.system.BundleUtils.inOSGi();

						org.talend.transform.runtime.common.MapExecutor mapExec_tHMap_1 = null;
						if (inOsgi_tHMap_1) {
							String executorId_tHMap_1 = Thread.currentThread().getId() + "_mapExecutor";
							java.util.concurrent.ConcurrentHashMap<Object, Object> concurrentHashMap_tHMap_1 = (java.util.concurrent.ConcurrentHashMap<Object, Object>) REST_Services.this.globalMap
									.get("concurrentHashMap");
							java.util.Map<Object, String> executorCache_tHMap_1 = (java.util.Map<Object, String>) concurrentHashMap_tHMap_1
									.computeIfAbsent("hmapExecutorCache", $_tHMap_1 -> java.util.Collections
											.synchronizedMap(new java.util.WeakHashMap<Object, String>()));
							mapExec_tHMap_1 = executorCache_tHMap_1.entrySet().stream() //
									.filter($_tHMap_1 -> executorId_tHMap_1.equals($_tHMap_1.getValue())) //
									.map($_tHMap_1 -> (org.talend.transform.runtime.common.MapExecutor) $_tHMap_1
											.getKey()) //
									.findFirst() //
									.orElse(org.talend.transform.runtime.common.MapExecutorFactory.forStandard()
											.createNew());
						} else {
							mapExec_tHMap_1 = org.talend.transform.runtime.common.MapExecutorFactory.forStandard();
						}
						mapExec_tHMap_1
								.setExceptionThreshold(org.talend.transform.runtime.common.SeverityLevel.HIGHEST);

						java.util.Map<String, Object> ecProps_tHMap_1 = new java.util.HashMap<String, Object>();
						context.synchronizeContext();

						org.talend.transform.runtime.common.MapLocation mapLocation_tHMap_1 = new org.talend.transform.runtime.common.MapLocation(
								"MBTC_POC", "Jobs/REST_Services/tHMap_1", "0_1");
						mapExec_tHMap_1.setUp("tHMap_1", mapLocation_tHMap_1);
						org.talend.transform.runtime.common.MapExecutionCommand mapExecCommand_tHMap_1 = mapExec_tHMap_1
								.createMapExecutionCommand(mapLocation_tHMap_1);

						java.util.Enumeration<?> propertyNames_tHMap_1 = context.propertyNames();
						while (propertyNames_tHMap_1.hasMoreElements()) {
							String key_tHMap_1 = (String) propertyNames_tHMap_1.nextElement();
							Object value_tHMap_1 = (Object) context.get(key_tHMap_1);
							ecProps_tHMap_1.put("context." + key_tHMap_1, value_tHMap_1);
						}
						ecProps_tHMap_1.put("org.talend.transform.externalmap", globalMap);
						mapExecCommand_tHMap_1.setExecutionProperties(ecProps_tHMap_1);
						java.util.Map<String, javax.xml.transform.Source> sources_tHMap_1 = new java.util.LinkedHashMap<>();
						// Setting one source only
						mapExecCommand_tHMap_1.setObjectSource(list_tHMap_1);

						javax.xml.transform.Result output_tHMap_1 = null;
						// Result is of single column string
						javax.xml.transform.stream.StreamResult result_tHMap_1_sendCustomers = new javax.xml.transform.stream.StreamResult();
						java.io.StringWriter sw_tHMap_1_sendCustomers = new java.io.StringWriter();
						((javax.xml.transform.stream.StreamResult) result_tHMap_1_sendCustomers)
								.setWriter(sw_tHMap_1_sendCustomers);
						// Set the single result
						output_tHMap_1 = result_tHMap_1_sendCustomers;
						mapExecCommand_tHMap_1.setResult(output_tHMap_1);
						((java.util.concurrent.ConcurrentHashMap) globalMap.get("concurrentHashMap"))
								.put(Thread.currentThread().getId() + "_tHMap_1_" + "outputResult", output_tHMap_1);

						org.talend.transform.runtime.common.MapExecutionStatus results_tHMap_1 = mapExecCommand_tHMap_1
								.execute();
						mapExecCommand_tHMap_1.freeExecutionResources();
						globalMap.put("tHMap_1_" + "EXECUTION_STATUS", results_tHMap_1);
						globalMap.put("tHMap_1_" + "EXECUTION_SEVERITY",
								results_tHMap_1.getHighestSeverityLevel().getNumValue());
						if (results_tHMap_1.getHighestSeverityLevel().isGreaterOrEqualsTo(64)) {
							throw new TalendException(new java.lang.Exception(String.valueOf(results_tHMap_1)),
									currentComponent, globalMap);
						}

						/**
						 * [tHMap_1_THMAP_IN begin ] stop
						 */

						/**
						 * [tHMap_1_THMAP_IN main ] start
						 */

						currentVirtualComponent = "tHMap_1";

						s(currentComponent = "tHMap_1_THMAP_IN");

						// THMAPIN_MAIN tHMap: tHMap_1
						java.util.List<java.lang.Object> rows_tHMap_1 = new java.util.ArrayList<>();
						boolean emitEmptyPayload_tHMap_1 = true; // tHMap Emit Empty payload option
						java.util.concurrent.ConcurrentHashMap<Object, Object> concurrentHashMap_getResults_tHMap_1 = (java.util.concurrent.ConcurrentHashMap) globalMap
								.get("concurrentHashMap");
						// Result for single col connection: sendCustomers
						javax.xml.transform.Result execResult_tHMap_1_sendCustomers = (javax.xml.transform.Result) concurrentHashMap_getResults_tHMap_1
								.get(Thread.currentThread().getId() + "_tHMap_1_" + "outputResult");
						// Output as string for single column connection
						sendCustomers = null; // Initialize as null
						javax.xml.transform.stream.StreamResult streamResult_tHMap_1_sendCustomers = (javax.xml.transform.stream.StreamResult) execResult_tHMap_1_sendCustomers;
						java.io.StringWriter swOut_tHMap_1_sendCustomers = (java.io.StringWriter) streamResult_tHMap_1_sendCustomers
								.getWriter();
						java.lang.String swOutStr_tHMap_1_sendCustomers = swOut_tHMap_1_sendCustomers == null ? null
								: swOut_tHMap_1_sendCustomers.toString();
						if (emitEmptyPayload_tHMap_1 || (nb_line_tHMap_1 > 0 && swOutStr_tHMap_1_sendCustomers != null
								&& !swOutStr_tHMap_1_sendCustomers.isEmpty())) {
							sendCustomers = new sendCustomersStruct();
							sendCustomers.body = swOutStr_tHMap_1_sendCustomers;
							rows_tHMap_1.add(sendCustomers);
						}

						tos_count_tHMap_1_THMAP_IN++;

						/**
						 * [tHMap_1_THMAP_IN main ] stop
						 */

						/**
						 * [tHMap_1_THMAP_IN process_data_begin ] start
						 */

						currentVirtualComponent = "tHMap_1";

						s(currentComponent = "tHMap_1_THMAP_IN");

						/**
						 * [tHMap_1_THMAP_IN process_data_begin ] stop
						 */

// Start of branch "sendCustomers"
						if (sendCustomers != null) {

							/**
							 * [tRESTResponse_1 main ] start
							 */

							s(currentComponent = "tRESTResponse_1");

							if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

									, "sendCustomers", "tHMap_1_THMAP_IN", "tHMap_1_THMAP_IN", "tHMapIn",
									"tRESTResponse_1", "tRESTResponse_1", "tRESTResponse"

							)) {
								talendJobLogProcess(globalMap);
							}

							if (log.isTraceEnabled()) {
								log.trace("sendCustomers - "
										+ (sendCustomers == null ? "" : sendCustomers.toLogString()));
							}

							java.io.OutputStream outputStream_tRESTResponse_1 = (java.io.OutputStream) globalMap
									.get("restResponseStream");
							boolean responseAlreadySent_tRESTResponse_1 = globalMap.containsKey("restResponse");

							if (null == outputStream_tRESTResponse_1 && responseAlreadySent_tRESTResponse_1) {
								throw new RuntimeException("Rest response already sent.");
							} else if (!globalMap.containsKey("restRequest")) {
								throw new RuntimeException("Not received rest request yet.");
							} else {
								Integer restProviderStatusCode_tRESTResponse_1 = 200;

								Object restProviderResponse_tRESTResponse_1 = null;
								restProviderResponse_tRESTResponse_1 = sendCustomers.body;

								java.util.Map<String, String> restProviderResponseHeaders_tRESTResponse_1 = new java.util.TreeMap<String, String>(
										String.CASE_INSENSITIVE_ORDER);
								java.lang.StringBuilder restProviderResponseHeader_cookies_tRESTResponse_1 = new java.lang.StringBuilder();
								final String setCookieHeader = "Set-Cookie";

								if (restProviderResponseHeader_cookies_tRESTResponse_1.length() > 0) {
									restProviderResponseHeaders_tRESTResponse_1.put(setCookieHeader,
											restProviderResponseHeader_cookies_tRESTResponse_1.toString());
								}

								java.util.Map<String, Object> restRequest_tRESTResponse_1 = (java.util.Map<String, Object>) globalMap
										.get("restRequest");
								org.apache.cxf.jaxrs.ext.MessageContext messageContext_tRESTResponse_1 = (org.apache.cxf.jaxrs.ext.MessageContext) restRequest_tRESTResponse_1
										.get("MESSAGE_CONTEXT");

								if (null == outputStream_tRESTResponse_1) {
									java.util.Map<String, Object> restResponse_tRESTResponse_1 = new java.util.HashMap<String, Object>();
									restResponse_tRESTResponse_1.put("BODY", restProviderResponse_tRESTResponse_1);
									restResponse_tRESTResponse_1.put("STATUS", restProviderStatusCode_tRESTResponse_1);
									restResponse_tRESTResponse_1.put("HEADERS",
											restProviderResponseHeaders_tRESTResponse_1);
									restResponse_tRESTResponse_1.put("drop.json.root.element", Boolean.valueOf(false));
									globalMap.put("restResponse", restResponse_tRESTResponse_1);

								} else {

									jakarta.ws.rs.core.MediaType responseMediaType_tRESTResponse_1 = null;
									if (!responseAlreadySent_tRESTResponse_1) {
										org.apache.cxf.message.Message currentMessage = null;
										if (org.apache.cxf.jaxrs.utils.JAXRSUtils.getCurrentMessage() != null) {
											currentMessage = org.apache.cxf.jaxrs.utils.JAXRSUtils.getCurrentMessage();
										} else {
											currentMessage = ((org.apache.cxf.message.Message) restRequest_tRESTResponse_1
													.get("CURRENT_MESSAGE"));
										}

										if (currentMessage != null && currentMessage.getExchange() != null) {
											currentMessage.getExchange()
													.put(StreamingDOM4JProvider.SUPRESS_XML_DECLARATION, true);
										}

										messageContext_tRESTResponse_1.put(org.apache.cxf.message.Message.RESPONSE_CODE,
												restProviderStatusCode_tRESTResponse_1);
										jakarta.ws.rs.core.MultivaluedMap<String, String> headersMultivaluedMap_tRESTResponse_1 = new org.apache.cxf.jaxrs.impl.MetadataMap<String, String>();
										for (java.util.Map.Entry<String, String> multivaluedHeader : restProviderResponseHeaders_tRESTResponse_1
												.entrySet()) {
											headersMultivaluedMap_tRESTResponse_1.putSingle(multivaluedHeader.getKey(),
													multivaluedHeader.getValue());
										}
										messageContext_tRESTResponse_1.put(
												org.apache.cxf.message.Message.PROTOCOL_HEADERS,
												headersMultivaluedMap_tRESTResponse_1);

										String responseContentType_tRESTResponse_1 = null;

										if (currentMessage != null && currentMessage.getExchange() != null) {
											responseContentType_tRESTResponse_1 = (String) currentMessage.getExchange()
													.get(org.apache.cxf.message.Message.CONTENT_TYPE);
										}

										if (null == responseContentType_tRESTResponse_1) {
											// this should not be needed, just in case. set it to the first value in the
											// sorted list returned from HttpHeaders
											responseMediaType_tRESTResponse_1 = messageContext_tRESTResponse_1
													.getHttpHeaders().getAcceptableMediaTypes().get(0);
										} else {
											responseMediaType_tRESTResponse_1 = org.apache.cxf.jaxrs.utils.JAXRSUtils
													.toMediaType(responseContentType_tRESTResponse_1);
										}
										globalMap.put("restResponseMediaType", responseMediaType_tRESTResponse_1);

										String responseMediaSubType_tRESTResponse_1 = responseMediaType_tRESTResponse_1
												.getSubtype();
										if (responseMediaSubType_tRESTResponse_1.equals("xml")
												|| responseMediaSubType_tRESTResponse_1.endsWith("+xml")) {
											outputStream_tRESTResponse_1.write("<wrapper>".getBytes());
											globalMap.put("restResponseWrappingClosure", "</wrapper>");
										}
										if (responseMediaSubType_tRESTResponse_1.equals("json")
												|| responseMediaSubType_tRESTResponse_1.endsWith("+json")) {
											outputStream_tRESTResponse_1.write("[".getBytes());
											globalMap.put("restResponseWrappingClosure", "]");
										}

										globalMap.put("restResponse", true);
									} else {
										responseMediaType_tRESTResponse_1 = (jakarta.ws.rs.core.MediaType) globalMap
												.get("restResponseMediaType");
									}

									if (null != restProviderResponse_tRESTResponse_1) {
										String responseMediaSubType_tRESTResponse_1 = responseMediaType_tRESTResponse_1
												.getSubtype();
										if (responseMediaSubType_tRESTResponse_1.equals("json")
												|| responseMediaSubType_tRESTResponse_1.endsWith("+json")) {
											if (globalMap.containsKey("restResponseJsonStarted")) {
												outputStream_tRESTResponse_1.write(",".getBytes());
											} else {
												globalMap.put("restResponseJsonStarted", true);
											}
										}

										Class<? extends Object> responseBodyClass_tRESTResponse_1 = restProviderResponse_tRESTResponse_1
												.getClass();
										jakarta.ws.rs.ext.Providers messageBodyProviders_tRESTResponse_1 = messageContext_tRESTResponse_1
												.getProviders();
										jakarta.ws.rs.ext.MessageBodyWriter messageBodyWriter_tRESTResponse_1 = messageBodyProviders_tRESTResponse_1
												.getMessageBodyWriter(responseBodyClass_tRESTResponse_1,
														responseBodyClass_tRESTResponse_1, null,
														responseMediaType_tRESTResponse_1);

										if (messageBodyWriter_tRESTResponse_1 instanceof StreamingDOM4JProvider) {
											((StreamingDOM4JProvider) messageBodyWriter_tRESTResponse_1)
													.setGlobalMap(globalMap);
										}

										messageBodyWriter_tRESTResponse_1.writeTo(restProviderResponse_tRESTResponse_1,
												responseBodyClass_tRESTResponse_1, responseBodyClass_tRESTResponse_1,
												new java.lang.annotation.Annotation[] {},
												responseMediaType_tRESTResponse_1, null, outputStream_tRESTResponse_1);
									}
									// initial variant
									// outputStream_tRESTResponse_1.write(String.valueOf(restProviderResponse_tRESTResponse_1).getBytes());
									outputStream_tRESTResponse_1.flush();
								}
							}

							tos_count_tRESTResponse_1++;

							/**
							 * [tRESTResponse_1 main ] stop
							 */

							/**
							 * [tRESTResponse_1 process_data_begin ] start
							 */

							s(currentComponent = "tRESTResponse_1");

							/**
							 * [tRESTResponse_1 process_data_begin ] stop
							 */

							/**
							 * [tRESTResponse_1 process_data_end ] start
							 */

							s(currentComponent = "tRESTResponse_1");

							/**
							 * [tRESTResponse_1 process_data_end ] stop
							 */

						} // End of branch "sendCustomers"

						/**
						 * [tHMap_1_THMAP_IN process_data_end ] start
						 */

						currentVirtualComponent = "tHMap_1";

						s(currentComponent = "tHMap_1_THMAP_IN");

						/**
						 * [tHMap_1_THMAP_IN process_data_end ] stop
						 */

						/**
						 * [tHMap_1_THMAP_IN end ] start
						 */

						currentVirtualComponent = "tHMap_1";

						s(currentComponent = "tHMap_1_THMAP_IN");

						// THMAPIN_END thMap: tHMap_1_THMAP_IN
						globalMap.put("tHMap_1_NB_LINE", nb_line_tHMap_1);

						ok_Hash.put("tHMap_1_THMAP_IN", true);
						end_Hash.put("tHMap_1_THMAP_IN", System.currentTimeMillis());

						/**
						 * [tHMap_1_THMAP_IN end ] stop
						 */

						/**
						 * [tRESTResponse_1 end ] start
						 */

						s(currentComponent = "tRESTResponse_1");

						if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "sendCustomers",
								2, 0, "tHMap_1_THMAP_IN", "tHMap_1_THMAP_IN", "tHMapIn", "tRESTResponse_1",
								"tRESTResponse_1", "tRESTResponse", "output")) {
							talendJobLogProcess(globalMap);
						}

						ok_Hash.put("tRESTResponse_1", true);
						end_Hash.put("tRESTResponse_1", System.currentTimeMillis());

						/**
						 * [tRESTResponse_1 end ] stop
						 */

						if (execStat) {
							runStat.updateStatOnConnection("iterate1", 2, "exec" + NB_ITERATE_tDBInput_1);
						}

						/**
						 * [tFlowToIterate_1 process_data_end ] start
						 */

						s(currentComponent = "tFlowToIterate_1");

						if (Get_the_list_of_customers != null) {

						}

						/**
						 * [tFlowToIterate_1 process_data_end ] stop
						 */

					} // End of branch "Get_the_list_of_customers"

// Start of branch "Create_a_customer"
					if (Create_a_customer != null) {

						/**
						 * [tExtractJSONFields_1 main ] start
						 */

						s(currentComponent = "tExtractJSONFields_1");

						cLabel = "customer";

						if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

								, "Create_a_customer", "tRESTRequest_1_In", "Customers_API_In", "tRESTRequestIn",
								"tExtractJSONFields_1", "customer", "tExtractJSONFields"

						)) {
							talendJobLogProcess(globalMap);
						}

						if (log.isTraceEnabled()) {
							log.trace("Create_a_customer - "
									+ (Create_a_customer == null ? "" : Create_a_customer.toLogString()));
						}

						if (Create_a_customer.ID != null) {// C_01
							jsonStr_tExtractJSONFields_1 = Create_a_customer.ID.toString();

							row2 = null;

							String loopPath_tExtractJSONFields_1 = "$";
							java.util.List<Object> resultset_tExtractJSONFields_1 = new java.util.ArrayList<Object>();

							boolean isStructError_tExtractJSONFields_1 = true;
							com.jayway.jsonpath.ReadContext document_tExtractJSONFields_1 = null;
							try {
								document_tExtractJSONFields_1 = com.jayway.jsonpath.JsonPath
										.parse(jsonStr_tExtractJSONFields_1);
								com.jayway.jsonpath.JsonPath compiledLoopPath_tExtractJSONFields_1 = jsonPathCache_tExtractJSONFields_1
										.getCompiledJsonPath(loopPath_tExtractJSONFields_1);
								Object result_tExtractJSONFields_1 = document_tExtractJSONFields_1
										.read(compiledLoopPath_tExtractJSONFields_1, net.minidev.json.JSONObject.class);
								if (result_tExtractJSONFields_1 instanceof net.minidev.json.JSONArray) {
									resultset_tExtractJSONFields_1 = (net.minidev.json.JSONArray) result_tExtractJSONFields_1;
								} else {
									resultset_tExtractJSONFields_1.add(result_tExtractJSONFields_1);
								}

								isStructError_tExtractJSONFields_1 = false;
							} catch (java.lang.Exception ex_tExtractJSONFields_1) {
								globalMap.put("tExtractJSONFields_1_ERROR_MESSAGE",
										ex_tExtractJSONFields_1.getMessage());
								log.error("tExtractJSONFields_1 - " + ex_tExtractJSONFields_1.getMessage());
								System.err.println(ex_tExtractJSONFields_1.getMessage());
							}

							String jsonPath_tExtractJSONFields_1 = null;
							com.jayway.jsonpath.JsonPath compiledJsonPath_tExtractJSONFields_1 = null;

							Object value_tExtractJSONFields_1 = null;

							Object root_tExtractJSONFields_1 = null;
							for (int i_tExtractJSONFields_1 = 0; isStructError_tExtractJSONFields_1
									|| (i_tExtractJSONFields_1 < resultset_tExtractJSONFields_1
											.size()); i_tExtractJSONFields_1++) {
								if (!isStructError_tExtractJSONFields_1) {
									Object row_tExtractJSONFields_1 = resultset_tExtractJSONFields_1
											.get(i_tExtractJSONFields_1);
									row2 = null;
									row2 = new row2Struct();
									nb_line_tExtractJSONFields_1++;
									try {
										jsonPath_tExtractJSONFields_1 = "ID";
										compiledJsonPath_tExtractJSONFields_1 = jsonPathCache_tExtractJSONFields_1
												.getCompiledJsonPath(jsonPath_tExtractJSONFields_1);

										try {

											if (jsonPath_tExtractJSONFields_1.startsWith("$")) {
												if (root_tExtractJSONFields_1 == null) {
													root_tExtractJSONFields_1 = document_tExtractJSONFields_1
															.read(jsonPathCache_tExtractJSONFields_1
																	.getCompiledJsonPath("$"));
												}
												value_tExtractJSONFields_1 = compiledJsonPath_tExtractJSONFields_1
														.read(root_tExtractJSONFields_1);
											} else {
												value_tExtractJSONFields_1 = compiledJsonPath_tExtractJSONFields_1
														.read(row_tExtractJSONFields_1);
											}
											row2.ID = value_tExtractJSONFields_1 == null ?

													null

													: value_tExtractJSONFields_1.toString();
										} catch (com.jayway.jsonpath.PathNotFoundException e_tExtractJSONFields_1) {
											globalMap.put("tExtractJSONFields_1_ERROR_MESSAGE",
													e_tExtractJSONFields_1.getMessage());
											row2.ID =

													null

											;
										}
										jsonPath_tExtractJSONFields_1 = "NAME";
										compiledJsonPath_tExtractJSONFields_1 = jsonPathCache_tExtractJSONFields_1
												.getCompiledJsonPath(jsonPath_tExtractJSONFields_1);

										try {

											if (jsonPath_tExtractJSONFields_1.startsWith("$")) {
												if (root_tExtractJSONFields_1 == null) {
													root_tExtractJSONFields_1 = document_tExtractJSONFields_1
															.read(jsonPathCache_tExtractJSONFields_1
																	.getCompiledJsonPath("$"));
												}
												value_tExtractJSONFields_1 = compiledJsonPath_tExtractJSONFields_1
														.read(root_tExtractJSONFields_1);
											} else {
												value_tExtractJSONFields_1 = compiledJsonPath_tExtractJSONFields_1
														.read(row_tExtractJSONFields_1);
											}
											row2.NAME = value_tExtractJSONFields_1 == null ?

													null

													: value_tExtractJSONFields_1.toString();
										} catch (com.jayway.jsonpath.PathNotFoundException e_tExtractJSONFields_1) {
											globalMap.put("tExtractJSONFields_1_ERROR_MESSAGE",
													e_tExtractJSONFields_1.getMessage());
											row2.NAME =

													null

											;
										}
										jsonPath_tExtractJSONFields_1 = "LAST_NAME";
										compiledJsonPath_tExtractJSONFields_1 = jsonPathCache_tExtractJSONFields_1
												.getCompiledJsonPath(jsonPath_tExtractJSONFields_1);

										try {

											if (jsonPath_tExtractJSONFields_1.startsWith("$")) {
												if (root_tExtractJSONFields_1 == null) {
													root_tExtractJSONFields_1 = document_tExtractJSONFields_1
															.read(jsonPathCache_tExtractJSONFields_1
																	.getCompiledJsonPath("$"));
												}
												value_tExtractJSONFields_1 = compiledJsonPath_tExtractJSONFields_1
														.read(root_tExtractJSONFields_1);
											} else {
												value_tExtractJSONFields_1 = compiledJsonPath_tExtractJSONFields_1
														.read(row_tExtractJSONFields_1);
											}
											row2.LAST_NAME = value_tExtractJSONFields_1 == null ?

													null

													: value_tExtractJSONFields_1.toString();
										} catch (com.jayway.jsonpath.PathNotFoundException e_tExtractJSONFields_1) {
											globalMap.put("tExtractJSONFields_1_ERROR_MESSAGE",
													e_tExtractJSONFields_1.getMessage());
											row2.LAST_NAME =

													null

											;
										}
										jsonPath_tExtractJSONFields_1 = "EMAIL";
										compiledJsonPath_tExtractJSONFields_1 = jsonPathCache_tExtractJSONFields_1
												.getCompiledJsonPath(jsonPath_tExtractJSONFields_1);

										try {

											if (jsonPath_tExtractJSONFields_1.startsWith("$")) {
												if (root_tExtractJSONFields_1 == null) {
													root_tExtractJSONFields_1 = document_tExtractJSONFields_1
															.read(jsonPathCache_tExtractJSONFields_1
																	.getCompiledJsonPath("$"));
												}
												value_tExtractJSONFields_1 = compiledJsonPath_tExtractJSONFields_1
														.read(root_tExtractJSONFields_1);
											} else {
												value_tExtractJSONFields_1 = compiledJsonPath_tExtractJSONFields_1
														.read(row_tExtractJSONFields_1);
											}
											row2.EMAIL = value_tExtractJSONFields_1 == null ?

													null

													: value_tExtractJSONFields_1.toString();
										} catch (com.jayway.jsonpath.PathNotFoundException e_tExtractJSONFields_1) {
											globalMap.put("tExtractJSONFields_1_ERROR_MESSAGE",
													e_tExtractJSONFields_1.getMessage());
											row2.EMAIL =

													null

											;
										}
										jsonPath_tExtractJSONFields_1 = "JOB_TITLE";
										compiledJsonPath_tExtractJSONFields_1 = jsonPathCache_tExtractJSONFields_1
												.getCompiledJsonPath(jsonPath_tExtractJSONFields_1);

										try {

											if (jsonPath_tExtractJSONFields_1.startsWith("$")) {
												if (root_tExtractJSONFields_1 == null) {
													root_tExtractJSONFields_1 = document_tExtractJSONFields_1
															.read(jsonPathCache_tExtractJSONFields_1
																	.getCompiledJsonPath("$"));
												}
												value_tExtractJSONFields_1 = compiledJsonPath_tExtractJSONFields_1
														.read(root_tExtractJSONFields_1);
											} else {
												value_tExtractJSONFields_1 = compiledJsonPath_tExtractJSONFields_1
														.read(row_tExtractJSONFields_1);
											}
											row2.JOB_TITLE = value_tExtractJSONFields_1 == null ?

													null

													: value_tExtractJSONFields_1.toString();
										} catch (com.jayway.jsonpath.PathNotFoundException e_tExtractJSONFields_1) {
											globalMap.put("tExtractJSONFields_1_ERROR_MESSAGE",
													e_tExtractJSONFields_1.getMessage());
											row2.JOB_TITLE =

													null

											;
										}
										jsonPath_tExtractJSONFields_1 = "CREDITCARDNUMBER";
										compiledJsonPath_tExtractJSONFields_1 = jsonPathCache_tExtractJSONFields_1
												.getCompiledJsonPath(jsonPath_tExtractJSONFields_1);

										try {

											if (jsonPath_tExtractJSONFields_1.startsWith("$")) {
												if (root_tExtractJSONFields_1 == null) {
													root_tExtractJSONFields_1 = document_tExtractJSONFields_1
															.read(jsonPathCache_tExtractJSONFields_1
																	.getCompiledJsonPath("$"));
												}
												value_tExtractJSONFields_1 = compiledJsonPath_tExtractJSONFields_1
														.read(root_tExtractJSONFields_1);
											} else {
												value_tExtractJSONFields_1 = compiledJsonPath_tExtractJSONFields_1
														.read(row_tExtractJSONFields_1);
											}
											row2.CREDITCARDNUMBER = value_tExtractJSONFields_1 == null ?

													null

													: value_tExtractJSONFields_1.toString();
										} catch (com.jayway.jsonpath.PathNotFoundException e_tExtractJSONFields_1) {
											globalMap.put("tExtractJSONFields_1_ERROR_MESSAGE",
													e_tExtractJSONFields_1.getMessage());
											row2.CREDITCARDNUMBER =

													null

											;
										}
										jsonPath_tExtractJSONFields_1 = "COMPANY";
										compiledJsonPath_tExtractJSONFields_1 = jsonPathCache_tExtractJSONFields_1
												.getCompiledJsonPath(jsonPath_tExtractJSONFields_1);

										try {

											if (jsonPath_tExtractJSONFields_1.startsWith("$")) {
												if (root_tExtractJSONFields_1 == null) {
													root_tExtractJSONFields_1 = document_tExtractJSONFields_1
															.read(jsonPathCache_tExtractJSONFields_1
																	.getCompiledJsonPath("$"));
												}
												value_tExtractJSONFields_1 = compiledJsonPath_tExtractJSONFields_1
														.read(root_tExtractJSONFields_1);
											} else {
												value_tExtractJSONFields_1 = compiledJsonPath_tExtractJSONFields_1
														.read(row_tExtractJSONFields_1);
											}
											row2.COMPANY = value_tExtractJSONFields_1 == null ?

													null

													: value_tExtractJSONFields_1.toString();
										} catch (com.jayway.jsonpath.PathNotFoundException e_tExtractJSONFields_1) {
											globalMap.put("tExtractJSONFields_1_ERROR_MESSAGE",
													e_tExtractJSONFields_1.getMessage());
											row2.COMPANY =

													null

											;
										}
										jsonPath_tExtractJSONFields_1 = "CITY";
										compiledJsonPath_tExtractJSONFields_1 = jsonPathCache_tExtractJSONFields_1
												.getCompiledJsonPath(jsonPath_tExtractJSONFields_1);

										try {

											if (jsonPath_tExtractJSONFields_1.startsWith("$")) {
												if (root_tExtractJSONFields_1 == null) {
													root_tExtractJSONFields_1 = document_tExtractJSONFields_1
															.read(jsonPathCache_tExtractJSONFields_1
																	.getCompiledJsonPath("$"));
												}
												value_tExtractJSONFields_1 = compiledJsonPath_tExtractJSONFields_1
														.read(root_tExtractJSONFields_1);
											} else {
												value_tExtractJSONFields_1 = compiledJsonPath_tExtractJSONFields_1
														.read(row_tExtractJSONFields_1);
											}
											row2.CITY = value_tExtractJSONFields_1 == null ?

													null

													: value_tExtractJSONFields_1.toString();
										} catch (com.jayway.jsonpath.PathNotFoundException e_tExtractJSONFields_1) {
											globalMap.put("tExtractJSONFields_1_ERROR_MESSAGE",
													e_tExtractJSONFields_1.getMessage());
											row2.CITY =

													null

											;
										}
										jsonPath_tExtractJSONFields_1 = "STATE";
										compiledJsonPath_tExtractJSONFields_1 = jsonPathCache_tExtractJSONFields_1
												.getCompiledJsonPath(jsonPath_tExtractJSONFields_1);

										try {

											if (jsonPath_tExtractJSONFields_1.startsWith("$")) {
												if (root_tExtractJSONFields_1 == null) {
													root_tExtractJSONFields_1 = document_tExtractJSONFields_1
															.read(jsonPathCache_tExtractJSONFields_1
																	.getCompiledJsonPath("$"));
												}
												value_tExtractJSONFields_1 = compiledJsonPath_tExtractJSONFields_1
														.read(root_tExtractJSONFields_1);
											} else {
												value_tExtractJSONFields_1 = compiledJsonPath_tExtractJSONFields_1
														.read(row_tExtractJSONFields_1);
											}
											row2.STATE = value_tExtractJSONFields_1 == null ?

													null

													: value_tExtractJSONFields_1.toString();
										} catch (com.jayway.jsonpath.PathNotFoundException e_tExtractJSONFields_1) {
											globalMap.put("tExtractJSONFields_1_ERROR_MESSAGE",
													e_tExtractJSONFields_1.getMessage());
											row2.STATE =

													null

											;
										}
									} catch (java.lang.Exception ex_tExtractJSONFields_1) {
										globalMap.put("tExtractJSONFields_1_ERROR_MESSAGE",
												ex_tExtractJSONFields_1.getMessage());
										log.error("tExtractJSONFields_1 - " + ex_tExtractJSONFields_1.getMessage());
										System.err.println(ex_tExtractJSONFields_1.getMessage());
										row2 = null;
									}

								}

								isStructError_tExtractJSONFields_1 = false;

								log.debug("tExtractJSONFields_1 - Extracting the record " + nb_line_tExtractJSONFields_1
										+ ".");
//}

								tos_count_tExtractJSONFields_1++;

								/**
								 * [tExtractJSONFields_1 main ] stop
								 */

								/**
								 * [tExtractJSONFields_1 process_data_begin ] start
								 */

								s(currentComponent = "tExtractJSONFields_1");

								if (Create_a_customer != null) {

									cLabel = "customer";

								}

								/**
								 * [tExtractJSONFields_1 process_data_begin ] stop
								 */

// Start of branch "row2"
								if (row2 != null) {

									/**
									 * [tLogRow_1 main ] start
									 */

									s(currentComponent = "tLogRow_1");

									if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

											, "row2", "tExtractJSONFields_1", "customer", "tExtractJSONFields",
											"tLogRow_1", "tLogRow_1", "tLogRow"

									)) {
										talendJobLogProcess(globalMap);
									}

									if (log.isTraceEnabled()) {
										log.trace("row2 - " + (row2 == null ? "" : row2.toLogString()));
									}

///////////////////////		

									strBuffer_tLogRow_1 = new StringBuilder();

									if (row2.ID != null) { //

										strBuffer_tLogRow_1.append(String.valueOf(row2.ID));

									} //

									strBuffer_tLogRow_1.append("|");

									if (row2.NAME != null) { //

										strBuffer_tLogRow_1.append(String.valueOf(row2.NAME));

									} //

									strBuffer_tLogRow_1.append("|");

									if (row2.LAST_NAME != null) { //

										strBuffer_tLogRow_1.append(String.valueOf(row2.LAST_NAME));

									} //

									strBuffer_tLogRow_1.append("|");

									if (row2.EMAIL != null) { //

										strBuffer_tLogRow_1.append(String.valueOf(row2.EMAIL));

									} //

									strBuffer_tLogRow_1.append("|");

									if (row2.JOB_TITLE != null) { //

										strBuffer_tLogRow_1.append(String.valueOf(row2.JOB_TITLE));

									} //

									strBuffer_tLogRow_1.append("|");

									if (row2.CREDITCARDNUMBER != null) { //

										strBuffer_tLogRow_1.append(String.valueOf(row2.CREDITCARDNUMBER));

									} //

									strBuffer_tLogRow_1.append("|");

									if (row2.COMPANY != null) { //

										strBuffer_tLogRow_1.append(String.valueOf(row2.COMPANY));

									} //

									strBuffer_tLogRow_1.append("|");

									if (row2.CITY != null) { //

										strBuffer_tLogRow_1.append(String.valueOf(row2.CITY));

									} //

									strBuffer_tLogRow_1.append("|");

									if (row2.STATE != null) { //

										strBuffer_tLogRow_1.append(String.valueOf(row2.STATE));

									} //

									if (globalMap.get("tLogRow_CONSOLE") != null) {
										consoleOut_tLogRow_1 = (java.io.PrintStream) globalMap.get("tLogRow_CONSOLE");
									} else {
										consoleOut_tLogRow_1 = new java.io.PrintStream(
												new java.io.BufferedOutputStream(System.out));
										globalMap.put("tLogRow_CONSOLE", consoleOut_tLogRow_1);
									}
									log.info("tLogRow_1 - Content of row " + (nb_line_tLogRow_1 + 1) + ": "
											+ strBuffer_tLogRow_1.toString());
									consoleOut_tLogRow_1.println(strBuffer_tLogRow_1.toString());
									consoleOut_tLogRow_1.flush();
									nb_line_tLogRow_1++;
//////

//////                    

///////////////////////    			

									row8 = row2;

									tos_count_tLogRow_1++;

									/**
									 * [tLogRow_1 main ] stop
									 */

									/**
									 * [tLogRow_1 process_data_begin ] start
									 */

									s(currentComponent = "tLogRow_1");

									/**
									 * [tLogRow_1 process_data_begin ] stop
									 */

									/**
									 * [tDBOutput_1 main ] start
									 */

									s(currentComponent = "tDBOutput_1");

									cLabel = "\"customers\"";

									if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

											, "row8", "tLogRow_1", "tLogRow_1", "tLogRow", "tDBOutput_1",
											"\"customers\"", "tMysqlOutput"

									)) {
										talendJobLogProcess(globalMap);
									}

									if (log.isTraceEnabled()) {
										log.trace("row8 - " + (row8 == null ? "" : row8.toLogString()));
									}

									row3 = null;
									row5 = null;
									whetherReject_tDBOutput_1 = false;
									if (row8.ID == null) {
										pstmt_tDBOutput_1.setNull(1, java.sql.Types.VARCHAR);
									} else {
										pstmt_tDBOutput_1.setString(1, row8.ID);
									}

									if (row8.NAME == null) {
										pstmt_tDBOutput_1.setNull(2, java.sql.Types.VARCHAR);
									} else {
										pstmt_tDBOutput_1.setString(2, row8.NAME);
									}

									if (row8.LAST_NAME == null) {
										pstmt_tDBOutput_1.setNull(3, java.sql.Types.VARCHAR);
									} else {
										pstmt_tDBOutput_1.setString(3, row8.LAST_NAME);
									}

									if (row8.EMAIL == null) {
										pstmt_tDBOutput_1.setNull(4, java.sql.Types.VARCHAR);
									} else {
										pstmt_tDBOutput_1.setString(4, row8.EMAIL);
									}

									if (row8.JOB_TITLE == null) {
										pstmt_tDBOutput_1.setNull(5, java.sql.Types.VARCHAR);
									} else {
										pstmt_tDBOutput_1.setString(5, row8.JOB_TITLE);
									}

									if (row8.CREDITCARDNUMBER == null) {
										pstmt_tDBOutput_1.setNull(6, java.sql.Types.VARCHAR);
									} else {
										pstmt_tDBOutput_1.setString(6, row8.CREDITCARDNUMBER);
									}

									if (row8.COMPANY == null) {
										pstmt_tDBOutput_1.setNull(7, java.sql.Types.VARCHAR);
									} else {
										pstmt_tDBOutput_1.setString(7, row8.COMPANY);
									}

									if (row8.CITY == null) {
										pstmt_tDBOutput_1.setNull(8, java.sql.Types.VARCHAR);
									} else {
										pstmt_tDBOutput_1.setString(8, row8.CITY);
									}

									if (row8.STATE == null) {
										pstmt_tDBOutput_1.setNull(9, java.sql.Types.VARCHAR);
									} else {
										pstmt_tDBOutput_1.setString(9, row8.STATE);
									}

									try {
										nb_line_tDBOutput_1++;
										int processedCount_tDBOutput_1 = pstmt_tDBOutput_1.executeUpdate();
										insertedCount_tDBOutput_1 += processedCount_tDBOutput_1;
										rowsToCommitCount_tDBOutput_1 += processedCount_tDBOutput_1;
										if (log.isDebugEnabled())
											log.debug("tDBOutput_1 - " + ("Inserting") + (" the record ")
													+ (nb_line_tDBOutput_1) + ("."));
									} catch (java.lang.Exception e) {
										globalMap.put("tDBOutput_1_ERROR_MESSAGE", e.getMessage());
										whetherReject_tDBOutput_1 = true;
										row5 = new row5Struct();
										row5.ID = row8.ID;
										row5.NAME = row8.NAME;
										row5.LAST_NAME = row8.LAST_NAME;
										row5.EMAIL = row8.EMAIL;
										row5.JOB_TITLE = row8.JOB_TITLE;
										row5.CREDITCARDNUMBER = row8.CREDITCARDNUMBER;
										row5.COMPANY = row8.COMPANY;
										row5.CITY = row8.CITY;
										row5.STATE = row8.STATE;
										rejectedCount_tDBOutput_1 = rejectedCount_tDBOutput_1 + 1;
										row5.errorCode = ((java.sql.SQLException) e).getSQLState();
										row5.errorMessage = e.getMessage() + " - Line: " + tos_count_tDBOutput_1;
									}
									if (!whetherReject_tDBOutput_1) {
										row3 = new row3Struct();
										row3.ID = row8.ID;
										row3.NAME = row8.NAME;
										row3.LAST_NAME = row8.LAST_NAME;
										row3.EMAIL = row8.EMAIL;
										row3.JOB_TITLE = row8.JOB_TITLE;
										row3.CREDITCARDNUMBER = row8.CREDITCARDNUMBER;
										row3.COMPANY = row8.COMPANY;
										row3.CITY = row8.CITY;
										row3.STATE = row8.STATE;
									}
									commitCounter_tDBOutput_1++;

									if (commitEvery_tDBOutput_1 <= commitCounter_tDBOutput_1) {

										if (rowsToCommitCount_tDBOutput_1 != 0) {
											if (log.isDebugEnabled())
												log.debug("tDBOutput_1 - " + ("Connection starting to commit ")
														+ (rowsToCommitCount_tDBOutput_1) + (" record(s)."));
										}
										conn_tDBOutput_1.commit();
										if (rowsToCommitCount_tDBOutput_1 != 0) {
											if (log.isDebugEnabled())
												log.debug("tDBOutput_1 - " + ("Connection commit has succeeded."));
											rowsToCommitCount_tDBOutput_1 = 0;
										}
										commitCounter_tDBOutput_1 = 0;
									}

									tos_count_tDBOutput_1++;

									/**
									 * [tDBOutput_1 main ] stop
									 */

									/**
									 * [tDBOutput_1 process_data_begin ] start
									 */

									s(currentComponent = "tDBOutput_1");

									cLabel = "\"customers\"";

									/**
									 * [tDBOutput_1 process_data_begin ] stop
									 */

// Start of branch "row3"
									if (row3 != null) {

										/**
										 * [tWriteJSONField_1_Out main ] start
										 */

										currentVirtualComponent = "tWriteJSONField_1";

										s(currentComponent = "tWriteJSONField_1_Out");

										if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

												, "row3", "tDBOutput_1", "\"customers\"", "tMysqlOutput",
												"tWriteJSONField_1_Out", "tWriteJSONField_1_Out", "tWriteXMLFieldOut"

										)) {
											talendJobLogProcess(globalMap);
										}

										if (log.isTraceEnabled()) {
											log.trace("row3 - " + (row3 == null ? "" : row3.toLogString()));
										}

										if (txf_tWriteJSONField_1_Out.getLastException() != null) {
											currentComponent = txf_tWriteJSONField_1_Out.getCurrentComponent();
											throw txf_tWriteJSONField_1_Out.getLastException();
										}

										if (txf_tWriteJSONField_1_Out.getLastError() != null) {
											throw txf_tWriteJSONField_1_Out.getLastError();
										}
										nb_line_tWriteJSONField_1_Out++;
										log.debug("tWriteJSONField_1_Out - Processing the record "
												+ nb_line_tWriteJSONField_1_Out + ".");

										class ToStringHelper_tWriteJSONField_1_Out {
											public String toString(final Object value) {
												return value != null ? value.toString() : null;
											}
										}
										final ToStringHelper_tWriteJSONField_1_Out helper_tWriteJSONField_1_Out = new ToStringHelper_tWriteJSONField_1_Out();

										valueMap_tWriteJSONField_1_Out.clear();
										arraysValueMap_tWriteJSONField_1_Out.clear();
										valueMap_tWriteJSONField_1_Out.put("ID",
												helper_tWriteJSONField_1_Out.toString((row3.ID.toString())));
										arraysValueMap_tWriteJSONField_1_Out.put("ID",
												helper_tWriteJSONField_1_Out.toString((row3.ID.toString())));
										valueMap_tWriteJSONField_1_Out.put("NAME", helper_tWriteJSONField_1_Out
												.toString((row3.NAME != null ? row3.NAME.toString() : null)));
										arraysValueMap_tWriteJSONField_1_Out.put("NAME", helper_tWriteJSONField_1_Out
												.toString((row3.NAME != null ? row3.NAME.toString() : null)));
										valueMap_tWriteJSONField_1_Out.put("LAST_NAME", helper_tWriteJSONField_1_Out
												.toString((row3.LAST_NAME != null ? row3.LAST_NAME.toString() : null)));
										arraysValueMap_tWriteJSONField_1_Out.put("LAST_NAME",
												helper_tWriteJSONField_1_Out.toString(
														(row3.LAST_NAME != null ? row3.LAST_NAME.toString() : null)));
										valueMap_tWriteJSONField_1_Out.put("EMAIL", helper_tWriteJSONField_1_Out
												.toString((row3.EMAIL != null ? row3.EMAIL.toString() : null)));
										arraysValueMap_tWriteJSONField_1_Out.put("EMAIL", helper_tWriteJSONField_1_Out
												.toString((row3.EMAIL != null ? row3.EMAIL.toString() : null)));
										valueMap_tWriteJSONField_1_Out.put("JOB_TITLE", helper_tWriteJSONField_1_Out
												.toString((row3.JOB_TITLE != null ? row3.JOB_TITLE.toString() : null)));
										arraysValueMap_tWriteJSONField_1_Out.put("JOB_TITLE",
												helper_tWriteJSONField_1_Out.toString(
														(row3.JOB_TITLE != null ? row3.JOB_TITLE.toString() : null)));
										valueMap_tWriteJSONField_1_Out.put("CREDITCARDNUMBER",
												helper_tWriteJSONField_1_Out.toString((row3.CREDITCARDNUMBER != null
														? row3.CREDITCARDNUMBER.toString()
														: null)));
										arraysValueMap_tWriteJSONField_1_Out.put("CREDITCARDNUMBER",
												helper_tWriteJSONField_1_Out.toString((row3.CREDITCARDNUMBER != null
														? row3.CREDITCARDNUMBER.toString()
														: null)));
										valueMap_tWriteJSONField_1_Out.put("COMPANY", helper_tWriteJSONField_1_Out
												.toString((row3.COMPANY != null ? row3.COMPANY.toString() : null)));
										arraysValueMap_tWriteJSONField_1_Out.put("COMPANY", helper_tWriteJSONField_1_Out
												.toString((row3.COMPANY != null ? row3.COMPANY.toString() : null)));
										valueMap_tWriteJSONField_1_Out.put("CITY", helper_tWriteJSONField_1_Out
												.toString((row3.CITY != null ? row3.CITY.toString() : null)));
										arraysValueMap_tWriteJSONField_1_Out.put("CITY", helper_tWriteJSONField_1_Out
												.toString((row3.CITY != null ? row3.CITY.toString() : null)));
										valueMap_tWriteJSONField_1_Out.put("STATE", helper_tWriteJSONField_1_Out
												.toString((row3.STATE != null ? row3.STATE.toString() : null)));
										arraysValueMap_tWriteJSONField_1_Out.put("STATE", helper_tWriteJSONField_1_Out
												.toString((row3.STATE != null ? row3.STATE.toString() : null)));
										String strTemp_tWriteJSONField_1_Out = "";
										if (strCompCache_tWriteJSONField_1_Out == null) {
											strCompCache_tWriteJSONField_1_Out = strTemp_tWriteJSONField_1_Out;

										} else {
											nestXMLTool_tWriteJSONField_1_Out.replaceDefaultNameSpace(
													doc_tWriteJSONField_1_Out.getRootElement());
											java.io.StringWriter strWriter_tWriteJSONField_1_Out = new java.io.StringWriter();
											org.dom4j.io.XMLWriter output_tWriteJSONField_1_Out = new org.dom4j.io.XMLWriter(
													strWriter_tWriteJSONField_1_Out, format_tWriteJSONField_1_Out);
											output_tWriteJSONField_1_Out.write(doc_tWriteJSONField_1_Out);
											output_tWriteJSONField_1_Out.close();

											row4Struct row_tWriteJSONField_1_Out = new row4Struct();

											row_tWriteJSONField_1_Out.body = strWriter_tWriteJSONField_1_Out.toString();
											listGroupby_tWriteJSONField_1_Out.add(row_tWriteJSONField_1_Out);

											doc_tWriteJSONField_1_Out.clearContent();
											needRoot_tWriteJSONField_1_Out = true;
											for (int i_tWriteJSONField_1_Out = 0; i_tWriteJSONField_1_Out < orders_tWriteJSONField_1_Out.length; i_tWriteJSONField_1_Out++) {
												orders_tWriteJSONField_1_Out[i_tWriteJSONField_1_Out] = 0;
											}

											if (groupbyList_tWriteJSONField_1_Out != null
													&& groupbyList_tWriteJSONField_1_Out.size() >= 0) {
												groupbyList_tWriteJSONField_1_Out.clear();
											}
											strCompCache_tWriteJSONField_1_Out = strTemp_tWriteJSONField_1_Out;
										}

										org.dom4j.Element subTreeRootParent_tWriteJSONField_1_Out = null;

										// build root xml tree
										if (needRoot_tWriteJSONField_1_Out) {
											needRoot_tWriteJSONField_1_Out = false;
											org.dom4j.Element root_tWriteJSONField_1_Out = doc_tWriteJSONField_1_Out
													.addElement("body");
											subTreeRootParent_tWriteJSONField_1_Out = root_tWriteJSONField_1_Out;
											org.dom4j.Element root_0_tWriteJSONField_1_Out = root_tWriteJSONField_1_Out
													.addElement("NAME");
											if (valueMap_tWriteJSONField_1_Out.get("NAME") != null) {
												nestXMLTool_tWriteJSONField_1_Out.setText(root_0_tWriteJSONField_1_Out,
														valueMap_tWriteJSONField_1_Out.get("NAME"));
											}
											org.dom4j.Element root_1_tWriteJSONField_1_Out = root_tWriteJSONField_1_Out
													.addElement("LAST_NAME");
											if (valueMap_tWriteJSONField_1_Out.get("LAST_NAME") != null) {
												nestXMLTool_tWriteJSONField_1_Out.setText(root_1_tWriteJSONField_1_Out,
														valueMap_tWriteJSONField_1_Out.get("LAST_NAME"));
											}
											org.dom4j.Element root_2_tWriteJSONField_1_Out = root_tWriteJSONField_1_Out
													.addElement("EMAIL");
											if (valueMap_tWriteJSONField_1_Out.get("EMAIL") != null) {
												nestXMLTool_tWriteJSONField_1_Out.setText(root_2_tWriteJSONField_1_Out,
														valueMap_tWriteJSONField_1_Out.get("EMAIL"));
											}
											org.dom4j.Element root_3_tWriteJSONField_1_Out = root_tWriteJSONField_1_Out
													.addElement("JOB_TITLE");
											if (valueMap_tWriteJSONField_1_Out.get("JOB_TITLE") != null) {
												nestXMLTool_tWriteJSONField_1_Out.setText(root_3_tWriteJSONField_1_Out,
														valueMap_tWriteJSONField_1_Out.get("JOB_TITLE"));
											}
											org.dom4j.Element root_4_tWriteJSONField_1_Out = root_tWriteJSONField_1_Out
													.addElement("CREDITCARDNUMBER");
											if (valueMap_tWriteJSONField_1_Out.get("CREDITCARDNUMBER") != null) {
												nestXMLTool_tWriteJSONField_1_Out.setText(root_4_tWriteJSONField_1_Out,
														valueMap_tWriteJSONField_1_Out.get("CREDITCARDNUMBER"));
											}
											org.dom4j.Element root_5_tWriteJSONField_1_Out = root_tWriteJSONField_1_Out
													.addElement("COMPANY");
											if (valueMap_tWriteJSONField_1_Out.get("COMPANY") != null) {
												nestXMLTool_tWriteJSONField_1_Out.setText(root_5_tWriteJSONField_1_Out,
														valueMap_tWriteJSONField_1_Out.get("COMPANY"));
											}
											org.dom4j.Element root_6_tWriteJSONField_1_Out = root_tWriteJSONField_1_Out
													.addElement("CITY");
											if (valueMap_tWriteJSONField_1_Out.get("CITY") != null) {
												nestXMLTool_tWriteJSONField_1_Out.setText(root_6_tWriteJSONField_1_Out,
														valueMap_tWriteJSONField_1_Out.get("CITY"));
											}
											org.dom4j.Element root_7_tWriteJSONField_1_Out = root_tWriteJSONField_1_Out
													.addElement("STATE");
											if (valueMap_tWriteJSONField_1_Out.get("STATE") != null) {
												nestXMLTool_tWriteJSONField_1_Out.setText(root_7_tWriteJSONField_1_Out,
														valueMap_tWriteJSONField_1_Out.get("STATE"));
											}
											root4Group_tWriteJSONField_1_Out = subTreeRootParent_tWriteJSONField_1_Out;
										} else {
											subTreeRootParent_tWriteJSONField_1_Out = root4Group_tWriteJSONField_1_Out;
										}
										// build group xml tree
										// build loop xml tree
										org.dom4j.Element loop_tWriteJSONField_1_Out = org.dom4j.DocumentHelper
												.createElement("ID");
										if (orders_tWriteJSONField_1_Out[0] == 0) {
											orders_tWriteJSONField_1_Out[0] = 0;
										}
										if (1 < orders_tWriteJSONField_1_Out.length) {
											orders_tWriteJSONField_1_Out[1] = 0;
										}
										subTreeRootParent_tWriteJSONField_1_Out.elements()
												.add(orders_tWriteJSONField_1_Out[0]++, loop_tWriteJSONField_1_Out);
										if (valueMap_tWriteJSONField_1_Out.get("ID") != null) {
											nestXMLTool_tWriteJSONField_1_Out.setText(loop_tWriteJSONField_1_Out,
													valueMap_tWriteJSONField_1_Out.get("ID"));
										}

										tos_count_tWriteJSONField_1_Out++;

										/**
										 * [tWriteJSONField_1_Out main ] stop
										 */

										/**
										 * [tWriteJSONField_1_Out process_data_begin ] start
										 */

										currentVirtualComponent = "tWriteJSONField_1";

										s(currentComponent = "tWriteJSONField_1_Out");

										/**
										 * [tWriteJSONField_1_Out process_data_begin ] stop
										 */

										/**
										 * [tWriteJSONField_1_Out process_data_end ] start
										 */

										currentVirtualComponent = "tWriteJSONField_1";

										s(currentComponent = "tWriteJSONField_1_Out");

										/**
										 * [tWriteJSONField_1_Out process_data_end ] stop
										 */

									} // End of branch "row3"

// Start of branch "row5"
									if (row5 != null) {

										/**
										 * [tWriteJSONField_2_Out main ] start
										 */

										currentVirtualComponent = "tWriteJSONField_2";

										s(currentComponent = "tWriteJSONField_2_Out");

										if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

												, "row5", "tDBOutput_1", "\"customers\"", "tMysqlOutput",
												"tWriteJSONField_2_Out", "tWriteJSONField_2_Out", "tWriteXMLFieldOut"

										)) {
											talendJobLogProcess(globalMap);
										}

										if (log.isTraceEnabled()) {
											log.trace("row5 - " + (row5 == null ? "" : row5.toLogString()));
										}

										if (txf_tWriteJSONField_2_Out.getLastException() != null) {
											currentComponent = txf_tWriteJSONField_2_Out.getCurrentComponent();
											throw txf_tWriteJSONField_2_Out.getLastException();
										}

										if (txf_tWriteJSONField_2_Out.getLastError() != null) {
											throw txf_tWriteJSONField_2_Out.getLastError();
										}
										nb_line_tWriteJSONField_2_Out++;
										log.debug("tWriteJSONField_2_Out - Processing the record "
												+ nb_line_tWriteJSONField_2_Out + ".");

										class ToStringHelper_tWriteJSONField_2_Out {
											public String toString(final Object value) {
												return value != null ? value.toString() : null;
											}
										}
										final ToStringHelper_tWriteJSONField_2_Out helper_tWriteJSONField_2_Out = new ToStringHelper_tWriteJSONField_2_Out();

										valueMap_tWriteJSONField_2_Out.clear();
										arraysValueMap_tWriteJSONField_2_Out.clear();
										valueMap_tWriteJSONField_2_Out.put("ID",
												helper_tWriteJSONField_2_Out.toString((row5.ID.toString())));
										arraysValueMap_tWriteJSONField_2_Out.put("ID",
												helper_tWriteJSONField_2_Out.toString((row5.ID.toString())));
										valueMap_tWriteJSONField_2_Out.put("NAME", helper_tWriteJSONField_2_Out
												.toString((row5.NAME != null ? row5.NAME.toString() : null)));
										arraysValueMap_tWriteJSONField_2_Out.put("NAME", helper_tWriteJSONField_2_Out
												.toString((row5.NAME != null ? row5.NAME.toString() : null)));
										valueMap_tWriteJSONField_2_Out.put("LAST_NAME", helper_tWriteJSONField_2_Out
												.toString((row5.LAST_NAME != null ? row5.LAST_NAME.toString() : null)));
										arraysValueMap_tWriteJSONField_2_Out.put("LAST_NAME",
												helper_tWriteJSONField_2_Out.toString(
														(row5.LAST_NAME != null ? row5.LAST_NAME.toString() : null)));
										valueMap_tWriteJSONField_2_Out.put("EMAIL", helper_tWriteJSONField_2_Out
												.toString((row5.EMAIL != null ? row5.EMAIL.toString() : null)));
										arraysValueMap_tWriteJSONField_2_Out.put("EMAIL", helper_tWriteJSONField_2_Out
												.toString((row5.EMAIL != null ? row5.EMAIL.toString() : null)));
										valueMap_tWriteJSONField_2_Out.put("JOB_TITLE", helper_tWriteJSONField_2_Out
												.toString((row5.JOB_TITLE != null ? row5.JOB_TITLE.toString() : null)));
										arraysValueMap_tWriteJSONField_2_Out.put("JOB_TITLE",
												helper_tWriteJSONField_2_Out.toString(
														(row5.JOB_TITLE != null ? row5.JOB_TITLE.toString() : null)));
										valueMap_tWriteJSONField_2_Out.put("CREDITCARDNUMBER",
												helper_tWriteJSONField_2_Out.toString((row5.CREDITCARDNUMBER != null
														? row5.CREDITCARDNUMBER.toString()
														: null)));
										arraysValueMap_tWriteJSONField_2_Out.put("CREDITCARDNUMBER",
												helper_tWriteJSONField_2_Out.toString((row5.CREDITCARDNUMBER != null
														? row5.CREDITCARDNUMBER.toString()
														: null)));
										valueMap_tWriteJSONField_2_Out.put("COMPANY", helper_tWriteJSONField_2_Out
												.toString((row5.COMPANY != null ? row5.COMPANY.toString() : null)));
										arraysValueMap_tWriteJSONField_2_Out.put("COMPANY", helper_tWriteJSONField_2_Out
												.toString((row5.COMPANY != null ? row5.COMPANY.toString() : null)));
										valueMap_tWriteJSONField_2_Out.put("CITY", helper_tWriteJSONField_2_Out
												.toString((row5.CITY != null ? row5.CITY.toString() : null)));
										arraysValueMap_tWriteJSONField_2_Out.put("CITY", helper_tWriteJSONField_2_Out
												.toString((row5.CITY != null ? row5.CITY.toString() : null)));
										valueMap_tWriteJSONField_2_Out.put("STATE", helper_tWriteJSONField_2_Out
												.toString((row5.STATE != null ? row5.STATE.toString() : null)));
										arraysValueMap_tWriteJSONField_2_Out.put("STATE", helper_tWriteJSONField_2_Out
												.toString((row5.STATE != null ? row5.STATE.toString() : null)));
										valueMap_tWriteJSONField_2_Out.put("errorCode", helper_tWriteJSONField_2_Out
												.toString((row5.errorCode != null ? row5.errorCode.toString() : null)));
										arraysValueMap_tWriteJSONField_2_Out.put("errorCode",
												helper_tWriteJSONField_2_Out.toString(
														(row5.errorCode != null ? row5.errorCode.toString() : null)));
										valueMap_tWriteJSONField_2_Out.put("errorMessage",
												helper_tWriteJSONField_2_Out.toString(
														(row5.errorMessage != null ? row5.errorMessage.toString()
																: null)));
										arraysValueMap_tWriteJSONField_2_Out.put("errorMessage",
												helper_tWriteJSONField_2_Out.toString(
														(row5.errorMessage != null ? row5.errorMessage.toString()
																: null)));
										String strTemp_tWriteJSONField_2_Out = "";
										if (strCompCache_tWriteJSONField_2_Out == null) {
											strCompCache_tWriteJSONField_2_Out = strTemp_tWriteJSONField_2_Out;

										} else {
											nestXMLTool_tWriteJSONField_2_Out.replaceDefaultNameSpace(
													doc_tWriteJSONField_2_Out.getRootElement());
											java.io.StringWriter strWriter_tWriteJSONField_2_Out = new java.io.StringWriter();
											org.dom4j.io.XMLWriter output_tWriteJSONField_2_Out = new org.dom4j.io.XMLWriter(
													strWriter_tWriteJSONField_2_Out, format_tWriteJSONField_2_Out);
											output_tWriteJSONField_2_Out.write(doc_tWriteJSONField_2_Out);
											output_tWriteJSONField_2_Out.close();

											row6Struct row_tWriteJSONField_2_Out = new row6Struct();

											row_tWriteJSONField_2_Out.body = strWriter_tWriteJSONField_2_Out.toString();
											listGroupby_tWriteJSONField_2_Out.add(row_tWriteJSONField_2_Out);

											doc_tWriteJSONField_2_Out.clearContent();
											needRoot_tWriteJSONField_2_Out = true;
											for (int i_tWriteJSONField_2_Out = 0; i_tWriteJSONField_2_Out < orders_tWriteJSONField_2_Out.length; i_tWriteJSONField_2_Out++) {
												orders_tWriteJSONField_2_Out[i_tWriteJSONField_2_Out] = 0;
											}

											if (groupbyList_tWriteJSONField_2_Out != null
													&& groupbyList_tWriteJSONField_2_Out.size() >= 0) {
												groupbyList_tWriteJSONField_2_Out.clear();
											}
											strCompCache_tWriteJSONField_2_Out = strTemp_tWriteJSONField_2_Out;
										}

										org.dom4j.Element subTreeRootParent_tWriteJSONField_2_Out = null;

										// build root xml tree
										if (needRoot_tWriteJSONField_2_Out) {
											needRoot_tWriteJSONField_2_Out = false;
											org.dom4j.Element root_tWriteJSONField_2_Out = doc_tWriteJSONField_2_Out
													.addElement("body");
											subTreeRootParent_tWriteJSONField_2_Out = root_tWriteJSONField_2_Out;
											org.dom4j.Element root_0_tWriteJSONField_2_Out = root_tWriteJSONField_2_Out
													.addElement("errorCode");
											if (valueMap_tWriteJSONField_2_Out.get("errorCode") != null) {
												nestXMLTool_tWriteJSONField_2_Out.setText(root_0_tWriteJSONField_2_Out,
														valueMap_tWriteJSONField_2_Out.get("errorCode"));
											}
											org.dom4j.Element root_1_tWriteJSONField_2_Out = root_tWriteJSONField_2_Out
													.addElement("errorMessage");
											if (valueMap_tWriteJSONField_2_Out.get("errorMessage") != null) {
												nestXMLTool_tWriteJSONField_2_Out.setText(root_1_tWriteJSONField_2_Out,
														valueMap_tWriteJSONField_2_Out.get("errorMessage"));
											}
											root4Group_tWriteJSONField_2_Out = subTreeRootParent_tWriteJSONField_2_Out;
										} else {
											subTreeRootParent_tWriteJSONField_2_Out = root4Group_tWriteJSONField_2_Out;
										}
										// build group xml tree
										// build loop xml tree
										org.dom4j.Element loop_tWriteJSONField_2_Out = org.dom4j.DocumentHelper
												.createElement("ID");
										if (orders_tWriteJSONField_2_Out[0] == 0) {
											orders_tWriteJSONField_2_Out[0] = 0;
										}
										if (1 < orders_tWriteJSONField_2_Out.length) {
											orders_tWriteJSONField_2_Out[1] = 0;
										}
										subTreeRootParent_tWriteJSONField_2_Out.elements()
												.add(orders_tWriteJSONField_2_Out[0]++, loop_tWriteJSONField_2_Out);
										if (valueMap_tWriteJSONField_2_Out.get("ID") != null) {
											nestXMLTool_tWriteJSONField_2_Out.setText(loop_tWriteJSONField_2_Out,
													valueMap_tWriteJSONField_2_Out.get("ID"));
										}

										tos_count_tWriteJSONField_2_Out++;

										/**
										 * [tWriteJSONField_2_Out main ] stop
										 */

										/**
										 * [tWriteJSONField_2_Out process_data_begin ] start
										 */

										currentVirtualComponent = "tWriteJSONField_2";

										s(currentComponent = "tWriteJSONField_2_Out");

										/**
										 * [tWriteJSONField_2_Out process_data_begin ] stop
										 */

										/**
										 * [tWriteJSONField_2_Out process_data_end ] start
										 */

										currentVirtualComponent = "tWriteJSONField_2";

										s(currentComponent = "tWriteJSONField_2_Out");

										/**
										 * [tWriteJSONField_2_Out process_data_end ] stop
										 */

									} // End of branch "row5"

									/**
									 * [tDBOutput_1 process_data_end ] start
									 */

									s(currentComponent = "tDBOutput_1");

									cLabel = "\"customers\"";

									/**
									 * [tDBOutput_1 process_data_end ] stop
									 */

									/**
									 * [tLogRow_1 process_data_end ] start
									 */

									s(currentComponent = "tLogRow_1");

									/**
									 * [tLogRow_1 process_data_end ] stop
									 */

								} // End of branch "row2"

								// end for
							}

						} // C_01

						/**
						 * [tExtractJSONFields_1 process_data_end ] start
						 */

						s(currentComponent = "tExtractJSONFields_1");

						if (Create_a_customer != null) {

							cLabel = "customer";

						}

						/**
						 * [tExtractJSONFields_1 process_data_end ] stop
						 */

					} // End of branch "Create_a_customer"

// Start of branch "Delete_a_customer"
					if (Delete_a_customer != null) {

						/**
						 * [tMap_1 main ] start
						 */

						s(currentComponent = "tMap_1");

						if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

								, "Delete_a_customer", "tRESTRequest_1_In", "Customers_API_In", "tRESTRequestIn",
								"tMap_1", "tMap_1", "tMap"

						)) {
							talendJobLogProcess(globalMap);
						}

						if (log.isTraceEnabled()) {
							log.trace("Delete_a_customer - "
									+ (Delete_a_customer == null ? "" : Delete_a_customer.toLogString()));
						}

						boolean hasCasePrimitiveKeyWithNull_tMap_1 = false;

						// ###############################
						// # Input tables (lookups)

						boolean rejectedInnerJoin_tMap_1 = false;
						boolean mainRowRejected_tMap_1 = false;
						// ###############################
						{ // start of Var scope

							// ###############################
							// # Vars tables

							Var__tMap_1__Struct Var = Var__tMap_1;// ###############################
							// ###############################
							// # Output tables

							ID_to_delete = null;

// # Output table : 'ID_to_delete'
							count_ID_to_delete_tMap_1++;

							ID_to_delete_tmp.ID = Delete_a_customer.id;
							ID_to_delete_tmp.NAME = null;
							ID_to_delete_tmp.LAST_NAME = null;
							ID_to_delete_tmp.EMAIL = null;
							ID_to_delete_tmp.JOB_TITLE = null;
							ID_to_delete_tmp.CREDITCARDNUMBER = null;
							ID_to_delete_tmp.COMPANY = null;
							ID_to_delete_tmp.CITY = null;
							ID_to_delete_tmp.STATE = null;
							ID_to_delete = ID_to_delete_tmp;
							log.debug("tMap_1 - Outputting the record " + count_ID_to_delete_tMap_1
									+ " of the output table 'ID_to_delete'.");

// ###############################

						} // end of Var scope

						rejectedInnerJoin_tMap_1 = false;

						tos_count_tMap_1++;

						/**
						 * [tMap_1 main ] stop
						 */

						/**
						 * [tMap_1 process_data_begin ] start
						 */

						s(currentComponent = "tMap_1");

						if (Delete_a_customer != null) {

						}

						/**
						 * [tMap_1 process_data_begin ] stop
						 */

// Start of branch "ID_to_delete"
						if (ID_to_delete != null) {

							/**
							 * [tDBOutput_2 main ] start
							 */

							s(currentComponent = "tDBOutput_2");

							cLabel = "\"customers\"";

							if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

									, "ID_to_delete", "tMap_1", "tMap_1", "tMap", "tDBOutput_2", "\"customers\"",
									"tMysqlOutput"

							)) {
								talendJobLogProcess(globalMap);
							}

							if (log.isTraceEnabled()) {
								log.trace("ID_to_delete - " + (ID_to_delete == null ? "" : ID_to_delete.toLogString()));
							}

							row7 = null;
							whetherReject_tDBOutput_2 = false;
							if (ID_to_delete.ID == null) {
								pstmt_tDBOutput_2.setNull(1, java.sql.Types.VARCHAR);
							} else {
								pstmt_tDBOutput_2.setString(1, ID_to_delete.ID);
							}

							try {
								int processedCount_tDBOutput_2 = pstmt_tDBOutput_2.executeUpdate();
								deletedCount_tDBOutput_2 += processedCount_tDBOutput_2;
								rowsToCommitCount_tDBOutput_2 += processedCount_tDBOutput_2;
								nb_line_tDBOutput_2++;
								if (log.isDebugEnabled())
									log.debug("tDBOutput_2 - " + ("Deleting") + (" the record ") + (nb_line_tDBOutput_2)
											+ ("."));
							} catch (java.lang.Exception e) {
								globalMap.put("tDBOutput_2_ERROR_MESSAGE", e.getMessage());
								whetherReject_tDBOutput_2 = true;
								nb_line_tDBOutput_2++;
								log.error("tDBOutput_2 - " + (e.getMessage()));
								System.err.print(e.getMessage());
							}
							if (!whetherReject_tDBOutput_2) {
								row7 = new row7Struct();
								row7.ID = ID_to_delete.ID;
								row7.NAME = ID_to_delete.NAME;
								row7.LAST_NAME = ID_to_delete.LAST_NAME;
								row7.EMAIL = ID_to_delete.EMAIL;
								row7.JOB_TITLE = ID_to_delete.JOB_TITLE;
								row7.CREDITCARDNUMBER = ID_to_delete.CREDITCARDNUMBER;
								row7.COMPANY = ID_to_delete.COMPANY;
								row7.CITY = ID_to_delete.CITY;
								row7.STATE = ID_to_delete.STATE;
							}
							commitCounter_tDBOutput_2++;

							if (commitEvery_tDBOutput_2 <= commitCounter_tDBOutput_2) {

								if (rowsToCommitCount_tDBOutput_2 != 0) {
									if (log.isDebugEnabled())
										log.debug("tDBOutput_2 - " + ("Connection starting to commit ")
												+ (rowsToCommitCount_tDBOutput_2) + (" record(s)."));
								}
								conn_tDBOutput_2.commit();
								if (rowsToCommitCount_tDBOutput_2 != 0) {
									if (log.isDebugEnabled())
										log.debug("tDBOutput_2 - " + ("Connection commit has succeeded."));
									rowsToCommitCount_tDBOutput_2 = 0;
								}
								commitCounter_tDBOutput_2 = 0;
							}

							tos_count_tDBOutput_2++;

							/**
							 * [tDBOutput_2 main ] stop
							 */

							/**
							 * [tDBOutput_2 process_data_begin ] start
							 */

							s(currentComponent = "tDBOutput_2");

							cLabel = "\"customers\"";

							/**
							 * [tDBOutput_2 process_data_begin ] stop
							 */

// Start of branch "row7"
							if (row7 != null) {

								/**
								 * [tRESTResponse_4 main ] start
								 */

								s(currentComponent = "tRESTResponse_4");

								if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

										, "row7", "tDBOutput_2", "\"customers\"", "tMysqlOutput", "tRESTResponse_4",
										"tRESTResponse_4", "tRESTResponse"

								)) {
									talendJobLogProcess(globalMap);
								}

								if (log.isTraceEnabled()) {
									log.trace("row7 - " + (row7 == null ? "" : row7.toLogString()));
								}

								java.io.OutputStream outputStream_tRESTResponse_4 = (java.io.OutputStream) globalMap
										.get("restResponseStream");
								boolean responseAlreadySent_tRESTResponse_4 = globalMap.containsKey("restResponse");

								if (null == outputStream_tRESTResponse_4 && responseAlreadySent_tRESTResponse_4) {
									throw new RuntimeException("Rest response already sent.");
								} else if (!globalMap.containsKey("restRequest")) {
									throw new RuntimeException("Not received rest request yet.");
								} else {
									Integer restProviderStatusCode_tRESTResponse_4 = 200;

									Object restProviderResponse_tRESTResponse_4 = null;

									java.util.Map<String, String> restProviderResponseHeaders_tRESTResponse_4 = new java.util.TreeMap<String, String>(
											String.CASE_INSENSITIVE_ORDER);
									java.lang.StringBuilder restProviderResponseHeader_cookies_tRESTResponse_4 = new java.lang.StringBuilder();
									final String setCookieHeader = "Set-Cookie";

									if (restProviderResponseHeader_cookies_tRESTResponse_4.length() > 0) {
										restProviderResponseHeaders_tRESTResponse_4.put(setCookieHeader,
												restProviderResponseHeader_cookies_tRESTResponse_4.toString());
									}

									java.util.Map<String, Object> restRequest_tRESTResponse_4 = (java.util.Map<String, Object>) globalMap
											.get("restRequest");
									org.apache.cxf.jaxrs.ext.MessageContext messageContext_tRESTResponse_4 = (org.apache.cxf.jaxrs.ext.MessageContext) restRequest_tRESTResponse_4
											.get("MESSAGE_CONTEXT");

									if (null == outputStream_tRESTResponse_4) {
										java.util.Map<String, Object> restResponse_tRESTResponse_4 = new java.util.HashMap<String, Object>();
										restResponse_tRESTResponse_4.put("BODY", restProviderResponse_tRESTResponse_4);
										restResponse_tRESTResponse_4.put("STATUS",
												restProviderStatusCode_tRESTResponse_4);
										restResponse_tRESTResponse_4.put("HEADERS",
												restProviderResponseHeaders_tRESTResponse_4);
										restResponse_tRESTResponse_4.put("drop.json.root.element",
												Boolean.valueOf(false));
										globalMap.put("restResponse", restResponse_tRESTResponse_4);

									} else {

										jakarta.ws.rs.core.MediaType responseMediaType_tRESTResponse_4 = null;
										if (!responseAlreadySent_tRESTResponse_4) {
											org.apache.cxf.message.Message currentMessage = null;
											if (org.apache.cxf.jaxrs.utils.JAXRSUtils.getCurrentMessage() != null) {
												currentMessage = org.apache.cxf.jaxrs.utils.JAXRSUtils
														.getCurrentMessage();
											} else {
												currentMessage = ((org.apache.cxf.message.Message) restRequest_tRESTResponse_4
														.get("CURRENT_MESSAGE"));
											}

											if (currentMessage != null && currentMessage.getExchange() != null) {
												currentMessage.getExchange()
														.put(StreamingDOM4JProvider.SUPRESS_XML_DECLARATION, true);
											}

											messageContext_tRESTResponse_4.put(
													org.apache.cxf.message.Message.RESPONSE_CODE,
													restProviderStatusCode_tRESTResponse_4);
											jakarta.ws.rs.core.MultivaluedMap<String, String> headersMultivaluedMap_tRESTResponse_4 = new org.apache.cxf.jaxrs.impl.MetadataMap<String, String>();
											for (java.util.Map.Entry<String, String> multivaluedHeader : restProviderResponseHeaders_tRESTResponse_4
													.entrySet()) {
												headersMultivaluedMap_tRESTResponse_4.putSingle(
														multivaluedHeader.getKey(), multivaluedHeader.getValue());
											}
											messageContext_tRESTResponse_4.put(
													org.apache.cxf.message.Message.PROTOCOL_HEADERS,
													headersMultivaluedMap_tRESTResponse_4);

											String responseContentType_tRESTResponse_4 = null;

											if (currentMessage != null && currentMessage.getExchange() != null) {
												responseContentType_tRESTResponse_4 = (String) currentMessage
														.getExchange().get(org.apache.cxf.message.Message.CONTENT_TYPE);
											}

											if (null == responseContentType_tRESTResponse_4) {
												// this should not be needed, just in case. set it to the first value in
												// the sorted list returned from HttpHeaders
												responseMediaType_tRESTResponse_4 = messageContext_tRESTResponse_4
														.getHttpHeaders().getAcceptableMediaTypes().get(0);
											} else {
												responseMediaType_tRESTResponse_4 = org.apache.cxf.jaxrs.utils.JAXRSUtils
														.toMediaType(responseContentType_tRESTResponse_4);
											}
											globalMap.put("restResponseMediaType", responseMediaType_tRESTResponse_4);

											String responseMediaSubType_tRESTResponse_4 = responseMediaType_tRESTResponse_4
													.getSubtype();
											if (responseMediaSubType_tRESTResponse_4.equals("xml")
													|| responseMediaSubType_tRESTResponse_4.endsWith("+xml")) {
												outputStream_tRESTResponse_4.write("<wrapper>".getBytes());
												globalMap.put("restResponseWrappingClosure", "</wrapper>");
											}
											if (responseMediaSubType_tRESTResponse_4.equals("json")
													|| responseMediaSubType_tRESTResponse_4.endsWith("+json")) {
												outputStream_tRESTResponse_4.write("[".getBytes());
												globalMap.put("restResponseWrappingClosure", "]");
											}

											globalMap.put("restResponse", true);
										} else {
											responseMediaType_tRESTResponse_4 = (jakarta.ws.rs.core.MediaType) globalMap
													.get("restResponseMediaType");
										}

										if (null != restProviderResponse_tRESTResponse_4) {
											String responseMediaSubType_tRESTResponse_4 = responseMediaType_tRESTResponse_4
													.getSubtype();
											if (responseMediaSubType_tRESTResponse_4.equals("json")
													|| responseMediaSubType_tRESTResponse_4.endsWith("+json")) {
												if (globalMap.containsKey("restResponseJsonStarted")) {
													outputStream_tRESTResponse_4.write(",".getBytes());
												} else {
													globalMap.put("restResponseJsonStarted", true);
												}
											}

											Class<? extends Object> responseBodyClass_tRESTResponse_4 = restProviderResponse_tRESTResponse_4
													.getClass();
											jakarta.ws.rs.ext.Providers messageBodyProviders_tRESTResponse_4 = messageContext_tRESTResponse_4
													.getProviders();
											jakarta.ws.rs.ext.MessageBodyWriter messageBodyWriter_tRESTResponse_4 = messageBodyProviders_tRESTResponse_4
													.getMessageBodyWriter(responseBodyClass_tRESTResponse_4,
															responseBodyClass_tRESTResponse_4, null,
															responseMediaType_tRESTResponse_4);

											if (messageBodyWriter_tRESTResponse_4 instanceof StreamingDOM4JProvider) {
												((StreamingDOM4JProvider) messageBodyWriter_tRESTResponse_4)
														.setGlobalMap(globalMap);
											}

											messageBodyWriter_tRESTResponse_4.writeTo(
													restProviderResponse_tRESTResponse_4,
													responseBodyClass_tRESTResponse_4,
													responseBodyClass_tRESTResponse_4,
													new java.lang.annotation.Annotation[] {},
													responseMediaType_tRESTResponse_4, null,
													outputStream_tRESTResponse_4);
										}
										// initial variant
										// outputStream_tRESTResponse_4.write(String.valueOf(restProviderResponse_tRESTResponse_4).getBytes());
										outputStream_tRESTResponse_4.flush();
									}
								}

								tos_count_tRESTResponse_4++;

								/**
								 * [tRESTResponse_4 main ] stop
								 */

								/**
								 * [tRESTResponse_4 process_data_begin ] start
								 */

								s(currentComponent = "tRESTResponse_4");

								/**
								 * [tRESTResponse_4 process_data_begin ] stop
								 */

								/**
								 * [tRESTResponse_4 process_data_end ] start
								 */

								s(currentComponent = "tRESTResponse_4");

								/**
								 * [tRESTResponse_4 process_data_end ] stop
								 */

							} // End of branch "row7"

							/**
							 * [tDBOutput_2 process_data_end ] start
							 */

							s(currentComponent = "tDBOutput_2");

							cLabel = "\"customers\"";

							/**
							 * [tDBOutput_2 process_data_end ] stop
							 */

						} // End of branch "ID_to_delete"

						/**
						 * [tMap_1 process_data_end ] start
						 */

						s(currentComponent = "tMap_1");

						if (Delete_a_customer != null) {

						}

						/**
						 * [tMap_1 process_data_end ] stop
						 */

					} // End of branch "Delete_a_customer"

					/**
					 * [tRESTRequest_1_In process_data_end ] start
					 */

					currentVirtualComponent = "tRESTRequest_1";

					s(currentComponent = "tRESTRequest_1_In");

					/**
					 * [tRESTRequest_1_In process_data_end ] stop
					 */

					/**
					 * [tRESTRequest_1_In end ] start
					 */

					currentVirtualComponent = "tRESTRequest_1";

					s(currentComponent = "tRESTRequest_1_In");

					resourceMap.put("inIterateVComp", true);

					ok_Hash.put("tRESTRequest_1_In", true);
					end_Hash.put("tRESTRequest_1_In", System.currentTimeMillis());

					/**
					 * [tRESTRequest_1_In end ] stop
					 */

					/**
					 * [tFlowToIterate_1 end ] start
					 */

					s(currentComponent = "tFlowToIterate_1");

					if (Get_the_list_of_customers != null) {

						globalMap.put("tFlowToIterate_1_NB_LINE", nb_line_tFlowToIterate_1);
						if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId,
								"Get_the_list_of_customers", 2, 0, "tRESTRequest_1_In", "Customers_API_In",
								"tRESTRequestIn", "tFlowToIterate_1", "tFlowToIterate_1", "tFlowToIterate", "output")) {
							talendJobLogProcess(globalMap);
						}

						if (log.isDebugEnabled())
							log.debug("tFlowToIterate_1 - " + ("Done."));

						ok_Hash.put("tFlowToIterate_1", true);
						end_Hash.put("tFlowToIterate_1", System.currentTimeMillis());

					}

					/**
					 * [tFlowToIterate_1 end ] stop
					 */

					/**
					 * [tExtractJSONFields_1 end ] start
					 */

					s(currentComponent = "tExtractJSONFields_1");

					if (Create_a_customer != null) {

						cLabel = "customer";

						globalMap.put("tExtractJSONFields_1_NB_LINE", nb_line_tExtractJSONFields_1);
						log.debug("tExtractJSONFields_1 - Extracted records count: " + nb_line_tExtractJSONFields_1
								+ " .");

						if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId,
								"Create_a_customer", 2, 0, "tRESTRequest_1_In", "Customers_API_In", "tRESTRequestIn",
								"tExtractJSONFields_1", "customer", "tExtractJSONFields", "output")) {
							talendJobLogProcess(globalMap);
						}

						if (log.isDebugEnabled())
							log.debug("tExtractJSONFields_1 - " + ("Done."));

						ok_Hash.put("tExtractJSONFields_1", true);
						end_Hash.put("tExtractJSONFields_1", System.currentTimeMillis());

					}

					/**
					 * [tExtractJSONFields_1 end ] stop
					 */

					/**
					 * [tLogRow_1 end ] start
					 */

					s(currentComponent = "tLogRow_1");

//////
//////
					globalMap.put("tLogRow_1_NB_LINE", nb_line_tLogRow_1);
					if (log.isInfoEnabled())
						log.info("tLogRow_1 - " + ("Printed row count: ") + (nb_line_tLogRow_1) + ("."));

///////////////////////    			

					if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row2", 2, 0,
							"tExtractJSONFields_1", "customer", "tExtractJSONFields", "tLogRow_1", "tLogRow_1",
							"tLogRow", "output")) {
						talendJobLogProcess(globalMap);
					}

					if (log.isDebugEnabled())
						log.debug("tLogRow_1 - " + ("Done."));

					ok_Hash.put("tLogRow_1", true);
					end_Hash.put("tLogRow_1", System.currentTimeMillis());

					/**
					 * [tLogRow_1 end ] stop
					 */

					/**
					 * [tDBOutput_1 end ] start
					 */

					s(currentComponent = "tDBOutput_1");

					cLabel = "\"customers\"";

					if (pstmt_tDBOutput_1 != null) {

						pstmt_tDBOutput_1.close();
						resourceMap.remove("pstmt_tDBOutput_1");

					}

					resourceMap.put("statementClosed_tDBOutput_1", true);

					if (commitCounter_tDBOutput_1 > 0 && rowsToCommitCount_tDBOutput_1 != 0) {

						if (log.isDebugEnabled())
							log.debug("tDBOutput_1 - " + ("Connection starting to commit ")
									+ (rowsToCommitCount_tDBOutput_1) + (" record(s)."));
					}
					conn_tDBOutput_1.commit();
					if (commitCounter_tDBOutput_1 > 0 && rowsToCommitCount_tDBOutput_1 != 0) {

						if (log.isDebugEnabled())
							log.debug("tDBOutput_1 - " + ("Connection commit has succeeded."));
						rowsToCommitCount_tDBOutput_1 = 0;
					}
					commitCounter_tDBOutput_1 = 0;

					if (log.isDebugEnabled())
						log.debug("tDBOutput_1 - " + ("Closing the connection to the database."));
					conn_tDBOutput_1.close();

					if (log.isDebugEnabled())
						log.debug("tDBOutput_1 - " + ("Connection to the database has closed."));
					resourceMap.put("finish_tDBOutput_1", true);

					nb_line_deleted_tDBOutput_1 = nb_line_deleted_tDBOutput_1 + deletedCount_tDBOutput_1;
					nb_line_update_tDBOutput_1 = nb_line_update_tDBOutput_1 + updatedCount_tDBOutput_1;
					nb_line_inserted_tDBOutput_1 = nb_line_inserted_tDBOutput_1 + insertedCount_tDBOutput_1;
					nb_line_rejected_tDBOutput_1 = nb_line_rejected_tDBOutput_1 + rejectedCount_tDBOutput_1;

					globalMap.put("tDBOutput_1_NB_LINE", nb_line_tDBOutput_1);
					globalMap.put("tDBOutput_1_NB_LINE_UPDATED", nb_line_update_tDBOutput_1);
					globalMap.put("tDBOutput_1_NB_LINE_INSERTED", nb_line_inserted_tDBOutput_1);
					globalMap.put("tDBOutput_1_NB_LINE_DELETED", nb_line_deleted_tDBOutput_1);
					globalMap.put("tDBOutput_1_NB_LINE_REJECTED", nb_line_rejected_tDBOutput_1);

					if (log.isDebugEnabled())
						log.debug("tDBOutput_1 - " + ("Has ") + ("inserted") + (" ") + (nb_line_inserted_tDBOutput_1)
								+ (" record(s)."));
					if (log.isDebugEnabled())
						log.debug("tDBOutput_1 - " + ("Has ") + ("rejected") + (" ") + (nb_line_rejected_tDBOutput_1)
								+ (" record(s)."));

					if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row8", 2, 0,
							"tLogRow_1", "tLogRow_1", "tLogRow", "tDBOutput_1", "\"customers\"", "tMysqlOutput",
							"output")) {
						talendJobLogProcess(globalMap);
					}

					if (log.isDebugEnabled())
						log.debug("tDBOutput_1 - " + ("Done."));

					ok_Hash.put("tDBOutput_1", true);
					end_Hash.put("tDBOutput_1", System.currentTimeMillis());

					/**
					 * [tDBOutput_1 end ] stop
					 */

					/**
					 * [tWriteJSONField_1_Out end ] start
					 */

					currentVirtualComponent = "tWriteJSONField_1";

					s(currentComponent = "tWriteJSONField_1_Out");

					if (nb_line_tWriteJSONField_1_Out > 0) {
						nestXMLTool_tWriteJSONField_1_Out
								.replaceDefaultNameSpace(doc_tWriteJSONField_1_Out.getRootElement());
						java.io.StringWriter strWriter_tWriteJSONField_1_Out = new java.io.StringWriter();
						org.dom4j.io.XMLWriter output_tWriteJSONField_1_Out = new org.dom4j.io.XMLWriter(
								strWriter_tWriteJSONField_1_Out, format_tWriteJSONField_1_Out);
						output_tWriteJSONField_1_Out.write(doc_tWriteJSONField_1_Out);
						output_tWriteJSONField_1_Out.close();
						row4Struct row_tWriteJSONField_1_Out = new row4Struct();

						row_tWriteJSONField_1_Out.body = strWriter_tWriteJSONField_1_Out.toString();
						listGroupby_tWriteJSONField_1_Out.add(row_tWriteJSONField_1_Out);

					}
					globalMap.put("tWriteJSONField_1_Out_NB_LINE", nb_line_tWriteJSONField_1_Out);
					globalMap.put("tWriteJSONField_1_In_FINISH" + (listGroupby_tWriteJSONField_1_Out == null ? ""
							: listGroupby_tWriteJSONField_1_Out.hashCode()), "true");

					future_tWriteJSONField_1_Out.get();

					if (txf_tWriteJSONField_1_Out.getLastException() != null) {
						currentComponent = txf_tWriteJSONField_1_Out.getCurrentComponent();
						throw txf_tWriteJSONField_1_Out.getLastException();
					}

					if (txf_tWriteJSONField_1_Out.getLastError() != null) {
						throw txf_tWriteJSONField_1_Out.getLastError();
					}

					resourceMap.put("finish_tWriteJSONField_1_Out", true);
					if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row3", 2, 0,
							"tDBOutput_1", "\"customers\"", "tMysqlOutput", "tWriteJSONField_1_Out",
							"tWriteJSONField_1_Out", "tWriteXMLFieldOut", "output")) {
						talendJobLogProcess(globalMap);
					}

					if (log.isDebugEnabled())
						log.debug("tWriteJSONField_1_Out - " + ("Done."));

					ok_Hash.put("tWriteJSONField_1_Out", true);
					end_Hash.put("tWriteJSONField_1_Out", System.currentTimeMillis());

					if (execStat) {
						runStat.updateStatOnConnection("OnComponentOk", 0, "ok");
					}

					/**
					 * [tWriteJSONField_1_Out end ] stop
					 */

					/**
					 * [tWriteJSONField_2_Out end ] start
					 */

					currentVirtualComponent = "tWriteJSONField_2";

					s(currentComponent = "tWriteJSONField_2_Out");

					if (nb_line_tWriteJSONField_2_Out > 0) {
						nestXMLTool_tWriteJSONField_2_Out
								.replaceDefaultNameSpace(doc_tWriteJSONField_2_Out.getRootElement());
						java.io.StringWriter strWriter_tWriteJSONField_2_Out = new java.io.StringWriter();
						org.dom4j.io.XMLWriter output_tWriteJSONField_2_Out = new org.dom4j.io.XMLWriter(
								strWriter_tWriteJSONField_2_Out, format_tWriteJSONField_2_Out);
						output_tWriteJSONField_2_Out.write(doc_tWriteJSONField_2_Out);
						output_tWriteJSONField_2_Out.close();
						row6Struct row_tWriteJSONField_2_Out = new row6Struct();

						row_tWriteJSONField_2_Out.body = strWriter_tWriteJSONField_2_Out.toString();
						listGroupby_tWriteJSONField_2_Out.add(row_tWriteJSONField_2_Out);

					}
					globalMap.put("tWriteJSONField_2_Out_NB_LINE", nb_line_tWriteJSONField_2_Out);
					globalMap.put("tWriteJSONField_2_In_FINISH" + (listGroupby_tWriteJSONField_2_Out == null ? ""
							: listGroupby_tWriteJSONField_2_Out.hashCode()), "true");

					future_tWriteJSONField_2_Out.get();

					if (txf_tWriteJSONField_2_Out.getLastException() != null) {
						currentComponent = txf_tWriteJSONField_2_Out.getCurrentComponent();
						throw txf_tWriteJSONField_2_Out.getLastException();
					}

					if (txf_tWriteJSONField_2_Out.getLastError() != null) {
						throw txf_tWriteJSONField_2_Out.getLastError();
					}

					resourceMap.put("finish_tWriteJSONField_2_Out", true);
					if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row5", 2, 0,
							"tDBOutput_1", "\"customers\"", "tMysqlOutput", "tWriteJSONField_2_Out",
							"tWriteJSONField_2_Out", "tWriteXMLFieldOut", "reject")) {
						talendJobLogProcess(globalMap);
					}

					if (log.isDebugEnabled())
						log.debug("tWriteJSONField_2_Out - " + ("Done."));

					ok_Hash.put("tWriteJSONField_2_Out", true);
					end_Hash.put("tWriteJSONField_2_Out", System.currentTimeMillis());

					if (execStat) {
						runStat.updateStatOnConnection("OnComponentOk", 0, "ok");
					}

					/**
					 * [tWriteJSONField_2_Out end ] stop
					 */

					/**
					 * [tMap_1 end ] start
					 */

					s(currentComponent = "tMap_1");

					if (Delete_a_customer != null) {

// ###############################
// # Lookup hashes releasing
// ###############################      
						log.debug("tMap_1 - Written records count in the table 'ID_to_delete': "
								+ count_ID_to_delete_tMap_1 + ".");

						if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId,
								"Delete_a_customer", 2, 0, "tRESTRequest_1_In", "Customers_API_In", "tRESTRequestIn",
								"tMap_1", "tMap_1", "tMap", "output")) {
							talendJobLogProcess(globalMap);
						}

						if (log.isDebugEnabled())
							log.debug("tMap_1 - " + ("Done."));

						ok_Hash.put("tMap_1", true);
						end_Hash.put("tMap_1", System.currentTimeMillis());

					}

					/**
					 * [tMap_1 end ] stop
					 */

					/**
					 * [tDBOutput_2 end ] start
					 */

					s(currentComponent = "tDBOutput_2");

					cLabel = "\"customers\"";

					if (pstmt_tDBOutput_2 != null) {

						pstmt_tDBOutput_2.close();
						resourceMap.remove("pstmt_tDBOutput_2");

					}

					resourceMap.put("statementClosed_tDBOutput_2", true);

					if (commitCounter_tDBOutput_2 > 0 && rowsToCommitCount_tDBOutput_2 != 0) {

						if (log.isDebugEnabled())
							log.debug("tDBOutput_2 - " + ("Connection starting to commit ")
									+ (rowsToCommitCount_tDBOutput_2) + (" record(s)."));
					}
					conn_tDBOutput_2.commit();
					if (commitCounter_tDBOutput_2 > 0 && rowsToCommitCount_tDBOutput_2 != 0) {

						if (log.isDebugEnabled())
							log.debug("tDBOutput_2 - " + ("Connection commit has succeeded."));
						rowsToCommitCount_tDBOutput_2 = 0;
					}
					commitCounter_tDBOutput_2 = 0;

					if (log.isDebugEnabled())
						log.debug("tDBOutput_2 - " + ("Closing the connection to the database."));
					conn_tDBOutput_2.close();

					if (log.isDebugEnabled())
						log.debug("tDBOutput_2 - " + ("Connection to the database has closed."));
					resourceMap.put("finish_tDBOutput_2", true);

					nb_line_deleted_tDBOutput_2 = nb_line_deleted_tDBOutput_2 + deletedCount_tDBOutput_2;
					nb_line_update_tDBOutput_2 = nb_line_update_tDBOutput_2 + updatedCount_tDBOutput_2;
					nb_line_inserted_tDBOutput_2 = nb_line_inserted_tDBOutput_2 + insertedCount_tDBOutput_2;
					nb_line_rejected_tDBOutput_2 = nb_line_rejected_tDBOutput_2 + rejectedCount_tDBOutput_2;

					globalMap.put("tDBOutput_2_NB_LINE", nb_line_tDBOutput_2);
					globalMap.put("tDBOutput_2_NB_LINE_UPDATED", nb_line_update_tDBOutput_2);
					globalMap.put("tDBOutput_2_NB_LINE_INSERTED", nb_line_inserted_tDBOutput_2);
					globalMap.put("tDBOutput_2_NB_LINE_DELETED", nb_line_deleted_tDBOutput_2);
					globalMap.put("tDBOutput_2_NB_LINE_REJECTED", nb_line_rejected_tDBOutput_2);

					if (log.isDebugEnabled())
						log.debug("tDBOutput_2 - " + ("Has ") + ("deleted") + (" ") + (nb_line_deleted_tDBOutput_2)
								+ (" record(s)."));

					if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "ID_to_delete", 2, 0,
							"tMap_1", "tMap_1", "tMap", "tDBOutput_2", "\"customers\"", "tMysqlOutput", "output")) {
						talendJobLogProcess(globalMap);
					}

					if (log.isDebugEnabled())
						log.debug("tDBOutput_2 - " + ("Done."));

					ok_Hash.put("tDBOutput_2", true);
					end_Hash.put("tDBOutput_2", System.currentTimeMillis());

					/**
					 * [tDBOutput_2 end ] stop
					 */

					/**
					 * [tRESTResponse_4 end ] start
					 */

					s(currentComponent = "tRESTResponse_4");

					if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row7", 2, 0,
							"tDBOutput_2", "\"customers\"", "tMysqlOutput", "tRESTResponse_4", "tRESTResponse_4",
							"tRESTResponse", "output")) {
						talendJobLogProcess(globalMap);
					}

					ok_Hash.put("tRESTResponse_4", true);
					end_Hash.put("tRESTResponse_4", System.currentTimeMillis());

					/**
					 * [tRESTResponse_4 end ] stop
					 */

					if (execStat) {
						runStat.updateStatOnConnection("Iterate", 2, "exec" + NB_ITERATE_tRESTRequest_1_In);
					}

					/**
					 * [tRESTRequest_1_Loop process_data_end ] start
					 */

					currentVirtualComponent = "tRESTRequest_1";

					s(currentComponent = "tRESTRequest_1_Loop");

					/**
					 * [tRESTRequest_1_Loop process_data_end ] stop
					 */

					/**
					 * [tRESTRequest_1_Loop end ] start
					 */

					currentVirtualComponent = "tRESTRequest_1";

					s(currentComponent = "tRESTRequest_1_Loop");

					resourceMap.remove("inIterateVComp");

				} catch (Throwable e_tRESTRequest_1) {
					if (e_tRESTRequest_1 instanceof Exception) {
						new TalendException((Exception) e_tRESTRequest_1, currentComponent, globalMap)
								.printStackTrace();
					} else {
						new TalendException(new RuntimeException(e_tRESTRequest_1), currentComponent, globalMap)
								.printStackTrace();
					}
					if (!globalMap.containsKey("restResponse")) {
						java.util.Map<String, Object> restFault_tRESTRequest_1 = new java.util.HashMap<String, Object>();
						restFault_tRESTRequest_1.put("STATUS", 500);
						restFault_tRESTRequest_1.put("BODY", e_tRESTRequest_1.getMessage());
						globalMap.put("restResponse", restFault_tRESTRequest_1);
					}
					return;
				}
				nb_line_tRESTRequest_1++;
				globalMap.put("tRESTRequest_1_NB_LINE", nb_line_tRESTRequest_1);

				runStat.updateStatAndLog(execStat, enableLogStash, iterateId, 2, 0, "Get_the_list_of_customers",
						"Create_a_customer", "row2", "row8", "row3", "row5", "Delete_a_customer", "ID_to_delete",
						"row7");

				ok_Hash.put("tRESTRequest_1_Loop", true);
				end_Hash.put("tRESTRequest_1_Loop", System.currentTimeMillis());

				/**
				 * [tRESTRequest_1_Loop end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			te.setVirtualComponentName(currentVirtualComponent);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tRESTRequest_1_Loop finally ] start
				 */

				currentVirtualComponent = "tRESTRequest_1";

				s(currentComponent = "tRESTRequest_1_Loop");

				/**
				 * [tRESTRequest_1_Loop finally ] stop
				 */

				/**
				 * [tRESTRequest_1_In finally ] start
				 */

				currentVirtualComponent = "tRESTRequest_1";

				s(currentComponent = "tRESTRequest_1_In");

				/**
				 * [tRESTRequest_1_In finally ] stop
				 */

				/**
				 * [tFlowToIterate_1 finally ] start
				 */

				s(currentComponent = "tFlowToIterate_1");

				/**
				 * [tFlowToIterate_1 finally ] stop
				 */

				/**
				 * [tDBInput_1 finally ] start
				 */

				s(currentComponent = "tDBInput_1");

				cLabel = "\"customers\"";

				/**
				 * [tDBInput_1 finally ] stop
				 */

				/**
				 * [tHMap_1_THMAP_OUT finally ] start
				 */

				currentVirtualComponent = "tHMap_1";

				s(currentComponent = "tHMap_1_THMAP_OUT");

				/**
				 * [tHMap_1_THMAP_OUT finally ] stop
				 */

				/**
				 * [tHMap_1_THMAP_IN finally ] start
				 */

				currentVirtualComponent = "tHMap_1";

				s(currentComponent = "tHMap_1_THMAP_IN");

				// THMAP_IN_FINALLY Cleanup tHMap_1
				java.util.concurrent.ConcurrentHashMap<Object, Object> concurrentHashMap_cleanup_tHMap_1 = (java.util.concurrent.ConcurrentHashMap) globalMap
						.get("concurrentHashMap");
				concurrentHashMap_cleanup_tHMap_1.remove(Thread.currentThread().getId() + "_tHMap_1_outputResult");

				/**
				 * [tHMap_1_THMAP_IN finally ] stop
				 */

				/**
				 * [tRESTResponse_1 finally ] start
				 */

				s(currentComponent = "tRESTResponse_1");

				/**
				 * [tRESTResponse_1 finally ] stop
				 */

				/**
				 * [tExtractJSONFields_1 finally ] start
				 */

				s(currentComponent = "tExtractJSONFields_1");

				cLabel = "customer";

				/**
				 * [tExtractJSONFields_1 finally ] stop
				 */

				/**
				 * [tLogRow_1 finally ] start
				 */

				s(currentComponent = "tLogRow_1");

				/**
				 * [tLogRow_1 finally ] stop
				 */

				/**
				 * [tDBOutput_1 finally ] start
				 */

				s(currentComponent = "tDBOutput_1");

				cLabel = "\"customers\"";

				try {
					if (resourceMap.get("statementClosed_tDBOutput_1") == null) {
						java.sql.PreparedStatement pstmtToClose_tDBOutput_1 = null;
						if ((pstmtToClose_tDBOutput_1 = (java.sql.PreparedStatement) resourceMap
								.remove("pstmt_tDBOutput_1")) != null) {
							pstmtToClose_tDBOutput_1.close();
						}
					}
				} finally {
					if (resourceMap.get("finish_tDBOutput_1") == null) {
						java.sql.Connection ctn_tDBOutput_1 = null;
						if ((ctn_tDBOutput_1 = (java.sql.Connection) resourceMap.get("conn_tDBOutput_1")) != null) {
							try {
								if (log.isDebugEnabled())
									log.debug("tDBOutput_1 - " + ("Closing the connection to the database."));
								ctn_tDBOutput_1.close();
								if (log.isDebugEnabled())
									log.debug("tDBOutput_1 - " + ("Connection to the database has closed."));
							} catch (java.sql.SQLException sqlEx_tDBOutput_1) {
								String errorMessage_tDBOutput_1 = "failed to close the connection in tDBOutput_1 :"
										+ sqlEx_tDBOutput_1.getMessage();
								log.error("tDBOutput_1 - " + (errorMessage_tDBOutput_1));
								System.err.println(errorMessage_tDBOutput_1);
							}
						}
					}
				}

				/**
				 * [tDBOutput_1 finally ] stop
				 */

				/**
				 * [tWriteJSONField_1_Out finally ] start
				 */

				currentVirtualComponent = "tWriteJSONField_1";

				s(currentComponent = "tWriteJSONField_1_Out");

				java.util.Queue listGroupby_tWriteJSONField_1_Out = (java.util.Queue) globalMap
						.get("queue_tWriteJSONField_1_In");
				if (resourceMap.get("finish_tWriteJSONField_1_Out") == null) {
					globalMap.put("tWriteJSONField_1_In_FINISH_WITH_EXCEPTION"
							+ (listGroupby_tWriteJSONField_1_Out == null ? ""
									: listGroupby_tWriteJSONField_1_Out.hashCode()),
							"true");
				}

				if (listGroupby_tWriteJSONField_1_Out != null) {
					globalMap.put("tWriteJSONField_1_In_FINISH" + (listGroupby_tWriteJSONField_1_Out == null ? ""
							: listGroupby_tWriteJSONField_1_Out.hashCode()), "true");
				}
				// workaround for 37349 - in case of normal execution it will pass normally
				// in case it fails and handle by catch - it will wait for child thread finish
				java.util.concurrent.Future<?> future_tWriteJSONField_1_Out = (java.util.concurrent.Future) globalMap
						.get("wrtXMLFieldIn_tWriteJSONField_1_Out");
				if (future_tWriteJSONField_1_Out != null) {
					future_tWriteJSONField_1_Out.get();
				}

				/**
				 * [tWriteJSONField_1_Out finally ] stop
				 */

				/**
				 * [tWriteJSONField_2_Out finally ] start
				 */

				currentVirtualComponent = "tWriteJSONField_2";

				s(currentComponent = "tWriteJSONField_2_Out");

				java.util.Queue listGroupby_tWriteJSONField_2_Out = (java.util.Queue) globalMap
						.get("queue_tWriteJSONField_2_In");
				if (resourceMap.get("finish_tWriteJSONField_2_Out") == null) {
					globalMap.put("tWriteJSONField_2_In_FINISH_WITH_EXCEPTION"
							+ (listGroupby_tWriteJSONField_2_Out == null ? ""
									: listGroupby_tWriteJSONField_2_Out.hashCode()),
							"true");
				}

				if (listGroupby_tWriteJSONField_2_Out != null) {
					globalMap.put("tWriteJSONField_2_In_FINISH" + (listGroupby_tWriteJSONField_2_Out == null ? ""
							: listGroupby_tWriteJSONField_2_Out.hashCode()), "true");
				}
				// workaround for 37349 - in case of normal execution it will pass normally
				// in case it fails and handle by catch - it will wait for child thread finish
				java.util.concurrent.Future<?> future_tWriteJSONField_2_Out = (java.util.concurrent.Future) globalMap
						.get("wrtXMLFieldIn_tWriteJSONField_2_Out");
				if (future_tWriteJSONField_2_Out != null) {
					future_tWriteJSONField_2_Out.get();
				}

				/**
				 * [tWriteJSONField_2_Out finally ] stop
				 */

				/**
				 * [tMap_1 finally ] start
				 */

				s(currentComponent = "tMap_1");

				/**
				 * [tMap_1 finally ] stop
				 */

				/**
				 * [tDBOutput_2 finally ] start
				 */

				s(currentComponent = "tDBOutput_2");

				cLabel = "\"customers\"";

				try {
					if (resourceMap.get("statementClosed_tDBOutput_2") == null) {
						java.sql.PreparedStatement pstmtToClose_tDBOutput_2 = null;
						if ((pstmtToClose_tDBOutput_2 = (java.sql.PreparedStatement) resourceMap
								.remove("pstmt_tDBOutput_2")) != null) {
							pstmtToClose_tDBOutput_2.close();
						}
					}
				} finally {
					if (resourceMap.get("finish_tDBOutput_2") == null) {
						java.sql.Connection ctn_tDBOutput_2 = null;
						if ((ctn_tDBOutput_2 = (java.sql.Connection) resourceMap.get("conn_tDBOutput_2")) != null) {
							try {
								if (log.isDebugEnabled())
									log.debug("tDBOutput_2 - " + ("Closing the connection to the database."));
								ctn_tDBOutput_2.close();
								if (log.isDebugEnabled())
									log.debug("tDBOutput_2 - " + ("Connection to the database has closed."));
							} catch (java.sql.SQLException sqlEx_tDBOutput_2) {
								String errorMessage_tDBOutput_2 = "failed to close the connection in tDBOutput_2 :"
										+ sqlEx_tDBOutput_2.getMessage();
								log.error("tDBOutput_2 - " + (errorMessage_tDBOutput_2));
								System.err.println(errorMessage_tDBOutput_2);
							}
						}
					}
				}

				/**
				 * [tDBOutput_2 finally ] stop
				 */

				/**
				 * [tRESTResponse_4 finally ] start
				 */

				s(currentComponent = "tRESTResponse_4");

				/**
				 * [tRESTResponse_4 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tRESTRequest_1_Loop_SUBPROCESS_STATE", 1);
	}

	public static class row4Struct implements routines.system.IPersistableRow<row4Struct> {
		final static byte[] commonByteArrayLock_MBTC_POC_REST_Services = new byte[0];
		static byte[] commonByteArray_MBTC_POC_REST_Services = new byte[0];

		public String body;

		public String getBody() {
			return this.body;
		}

		public Boolean bodyIsNullable() {
			return true;
		}

		public Boolean bodyIsKey() {
			return false;
		}

		public Integer bodyLength() {
			return 0;
		}

		public Integer bodyPrecision() {
			return 0;
		}

		public String bodyDefault() {

			return null;

		}

		public String bodyComment() {

			return null;

		}

		public String bodyPattern() {

			return null;

		}

		public String bodyOriginalDbColumnName() {

			return "body";

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.body = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.body = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.body, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// String

				writeString(this.body, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("body=" + body);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (body == null) {
				sb.append("<null>");
			} else {
				sb.append(body);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row4Struct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(), object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public void tWriteJSONField_1_InProcess(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tWriteJSONField_1_In_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tWriteJSONField_1_In", "QyrlSS_");

		String currentVirtualComponent = null;

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				row4Struct row4 = new row4Struct();

				/**
				 * [tRESTResponse_2 begin ] start
				 */

				sh("tRESTResponse_2");

				s(currentComponent = "tRESTResponse_2");

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row4");

				int tos_count_tRESTResponse_2 = 0;

				if (enableLogStash) {
					talendJobLog.addCM("tRESTResponse_2", "tRESTResponse_2", "tRESTResponse");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				/**
				 * [tRESTResponse_2 begin ] stop
				 */

				/**
				 * [tWriteJSONField_1_In begin ] start
				 */

				sh("tWriteJSONField_1_In");

				currentVirtualComponent = "tWriteJSONField_1";

				s(currentComponent = "tWriteJSONField_1_In");

				int tos_count_tWriteJSONField_1_In = 0;

				if (log.isDebugEnabled())
					log.debug("tWriteJSONField_1_In - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tWriteJSONField_1_In {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tWriteJSONField_1_In = new StringBuilder();
							log4jParamters_tWriteJSONField_1_In.append("Parameters:");
							log4jParamters_tWriteJSONField_1_In.append("JSONFIELD" + " = " + "body");
							log4jParamters_tWriteJSONField_1_In.append(" | ");
							log4jParamters_tWriteJSONField_1_In.append("DESTINATION" + " = " + "tWriteJSONField_1");
							log4jParamters_tWriteJSONField_1_In.append(" | ");
							log4jParamters_tWriteJSONField_1_In.append("REMOVE_ROOT" + " = " + "false");
							log4jParamters_tWriteJSONField_1_In.append(" | ");
							log4jParamters_tWriteJSONField_1_In.append("GROUPBYS" + " = " + "[]");
							log4jParamters_tWriteJSONField_1_In.append(" | ");
							log4jParamters_tWriteJSONField_1_In.append("QUOTE_ALL_VALUES" + " = " + "false");
							log4jParamters_tWriteJSONField_1_In.append(" | ");
							log4jParamters_tWriteJSONField_1_In.append("ALLOW_EMPTY_STRINGS" + " = " + "false");
							log4jParamters_tWriteJSONField_1_In.append(" | ");
							log4jParamters_tWriteJSONField_1_In.append("USE_SCIENTIFIC_NOTATION" + " = " + "false");
							log4jParamters_tWriteJSONField_1_In.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tWriteJSONField_1_In - " + (log4jParamters_tWriteJSONField_1_In));
						}
					}
					new BytesLimit65535_tWriteJSONField_1_In().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tWriteJSONField_1_In", "tWriteJSONField_1_In", "tWriteJSONFieldIn");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				int nb_line_tWriteJSONField_1_In = 0;
				org.kordamp.json.xml.XMLSerializer xmlSerializer_tWriteJSONField_1_In = new org.kordamp.json.xml.XMLSerializer();
				xmlSerializer_tWriteJSONField_1_In.clearNamespaces();
				xmlSerializer_tWriteJSONField_1_In.setSkipNamespaces(true);
				xmlSerializer_tWriteJSONField_1_In.setForceTopLevelObject(true);
				xmlSerializer_tWriteJSONField_1_In.setUseEmptyStrings(false);
				xmlSerializer_tWriteJSONField_1_In.setUseScientificNotation(false);

				java.util.Queue<row4Struct> queue_tWriteJSONField_1_In = (java.util.Queue<row4Struct>) globalMap
						.get("queue_tWriteJSONField_1_In");

				String readFinishMarkWithPipeId_tWriteJSONField_1_In = "tWriteJSONField_1_In_FINISH"
						+ (queue_tWriteJSONField_1_In == null ? "" : queue_tWriteJSONField_1_In.hashCode());
				String str_tWriteJSONField_1_In = null;

				while (!globalMap.containsKey(readFinishMarkWithPipeId_tWriteJSONField_1_In)
						|| !queue_tWriteJSONField_1_In.isEmpty()) {
					if (!queue_tWriteJSONField_1_In.isEmpty()) {

						/**
						 * [tWriteJSONField_1_In begin ] stop
						 */

						/**
						 * [tWriteJSONField_1_In main ] start
						 */

						currentVirtualComponent = "tWriteJSONField_1";

						s(currentComponent = "tWriteJSONField_1_In");

						row4Struct result_tWriteJSONField_1_In = queue_tWriteJSONField_1_In.poll();
						str_tWriteJSONField_1_In = result_tWriteJSONField_1_In.body;
						// Convert XML to JSON
						org.kordamp.json.JsonStandard jsonStandard_tWriteJSONField_1_In = org.kordamp.json.JsonStandard.LEGACY;
						xmlSerializer_tWriteJSONField_1_In.setJsonStandard(jsonStandard_tWriteJSONField_1_In);
						org.kordamp.json.JSON json_tWriteJSONField_1_In = xmlSerializer_tWriteJSONField_1_In
								.read(str_tWriteJSONField_1_In);
						row4.body = org.kordamp.json.util.JSONUtils.jsonToStandardizedString(json_tWriteJSONField_1_In,
								jsonStandard_tWriteJSONField_1_In);

						nb_line_tWriteJSONField_1_In++;

						tos_count_tWriteJSONField_1_In++;

						/**
						 * [tWriteJSONField_1_In main ] stop
						 */

						/**
						 * [tWriteJSONField_1_In process_data_begin ] start
						 */

						currentVirtualComponent = "tWriteJSONField_1";

						s(currentComponent = "tWriteJSONField_1_In");

						/**
						 * [tWriteJSONField_1_In process_data_begin ] stop
						 */

						/**
						 * [tRESTResponse_2 main ] start
						 */

						s(currentComponent = "tRESTResponse_2");

						if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

								, "row4", "tWriteJSONField_1_In", "tWriteJSONField_1_In", "tWriteJSONFieldIn",
								"tRESTResponse_2", "tRESTResponse_2", "tRESTResponse"

						)) {
							talendJobLogProcess(globalMap);
						}

						if (log.isTraceEnabled()) {
							log.trace("row4 - " + (row4 == null ? "" : row4.toLogString()));
						}

						java.io.OutputStream outputStream_tRESTResponse_2 = (java.io.OutputStream) globalMap
								.get("restResponseStream");
						boolean responseAlreadySent_tRESTResponse_2 = globalMap.containsKey("restResponse");

						if (null == outputStream_tRESTResponse_2 && responseAlreadySent_tRESTResponse_2) {
							throw new RuntimeException("Rest response already sent.");
						} else if (!globalMap.containsKey("restRequest")) {
							throw new RuntimeException("Not received rest request yet.");
						} else {
							Integer restProviderStatusCode_tRESTResponse_2 = 201;

							Object restProviderResponse_tRESTResponse_2 = null;
							restProviderResponse_tRESTResponse_2 = row4.body;

							java.util.Map<String, String> restProviderResponseHeaders_tRESTResponse_2 = new java.util.TreeMap<String, String>(
									String.CASE_INSENSITIVE_ORDER);
							java.lang.StringBuilder restProviderResponseHeader_cookies_tRESTResponse_2 = new java.lang.StringBuilder();
							final String setCookieHeader = "Set-Cookie";

							if (restProviderResponseHeader_cookies_tRESTResponse_2.length() > 0) {
								restProviderResponseHeaders_tRESTResponse_2.put(setCookieHeader,
										restProviderResponseHeader_cookies_tRESTResponse_2.toString());
							}

							java.util.Map<String, Object> restRequest_tRESTResponse_2 = (java.util.Map<String, Object>) globalMap
									.get("restRequest");
							org.apache.cxf.jaxrs.ext.MessageContext messageContext_tRESTResponse_2 = (org.apache.cxf.jaxrs.ext.MessageContext) restRequest_tRESTResponse_2
									.get("MESSAGE_CONTEXT");

							if (null == outputStream_tRESTResponse_2) {
								java.util.Map<String, Object> restResponse_tRESTResponse_2 = new java.util.HashMap<String, Object>();
								restResponse_tRESTResponse_2.put("BODY", restProviderResponse_tRESTResponse_2);
								restResponse_tRESTResponse_2.put("STATUS", restProviderStatusCode_tRESTResponse_2);
								restResponse_tRESTResponse_2.put("HEADERS",
										restProviderResponseHeaders_tRESTResponse_2);
								restResponse_tRESTResponse_2.put("drop.json.root.element", Boolean.valueOf(false));
								globalMap.put("restResponse", restResponse_tRESTResponse_2);

							} else {

								jakarta.ws.rs.core.MediaType responseMediaType_tRESTResponse_2 = null;
								if (!responseAlreadySent_tRESTResponse_2) {
									org.apache.cxf.message.Message currentMessage = null;
									if (org.apache.cxf.jaxrs.utils.JAXRSUtils.getCurrentMessage() != null) {
										currentMessage = org.apache.cxf.jaxrs.utils.JAXRSUtils.getCurrentMessage();
									} else {
										currentMessage = ((org.apache.cxf.message.Message) restRequest_tRESTResponse_2
												.get("CURRENT_MESSAGE"));
									}

									if (currentMessage != null && currentMessage.getExchange() != null) {
										currentMessage.getExchange().put(StreamingDOM4JProvider.SUPRESS_XML_DECLARATION,
												true);
									}

									messageContext_tRESTResponse_2.put(org.apache.cxf.message.Message.RESPONSE_CODE,
											restProviderStatusCode_tRESTResponse_2);
									jakarta.ws.rs.core.MultivaluedMap<String, String> headersMultivaluedMap_tRESTResponse_2 = new org.apache.cxf.jaxrs.impl.MetadataMap<String, String>();
									for (java.util.Map.Entry<String, String> multivaluedHeader : restProviderResponseHeaders_tRESTResponse_2
											.entrySet()) {
										headersMultivaluedMap_tRESTResponse_2.putSingle(multivaluedHeader.getKey(),
												multivaluedHeader.getValue());
									}
									messageContext_tRESTResponse_2.put(org.apache.cxf.message.Message.PROTOCOL_HEADERS,
											headersMultivaluedMap_tRESTResponse_2);

									String responseContentType_tRESTResponse_2 = null;

									if (currentMessage != null && currentMessage.getExchange() != null) {
										responseContentType_tRESTResponse_2 = (String) currentMessage.getExchange()
												.get(org.apache.cxf.message.Message.CONTENT_TYPE);
									}

									if (null == responseContentType_tRESTResponse_2) {
										// this should not be needed, just in case. set it to the first value in the
										// sorted list returned from HttpHeaders
										responseMediaType_tRESTResponse_2 = messageContext_tRESTResponse_2
												.getHttpHeaders().getAcceptableMediaTypes().get(0);
									} else {
										responseMediaType_tRESTResponse_2 = org.apache.cxf.jaxrs.utils.JAXRSUtils
												.toMediaType(responseContentType_tRESTResponse_2);
									}
									globalMap.put("restResponseMediaType", responseMediaType_tRESTResponse_2);

									String responseMediaSubType_tRESTResponse_2 = responseMediaType_tRESTResponse_2
											.getSubtype();
									if (responseMediaSubType_tRESTResponse_2.equals("xml")
											|| responseMediaSubType_tRESTResponse_2.endsWith("+xml")) {
										outputStream_tRESTResponse_2.write("<wrapper>".getBytes());
										globalMap.put("restResponseWrappingClosure", "</wrapper>");
									}
									if (responseMediaSubType_tRESTResponse_2.equals("json")
											|| responseMediaSubType_tRESTResponse_2.endsWith("+json")) {
										outputStream_tRESTResponse_2.write("[".getBytes());
										globalMap.put("restResponseWrappingClosure", "]");
									}

									globalMap.put("restResponse", true);
								} else {
									responseMediaType_tRESTResponse_2 = (jakarta.ws.rs.core.MediaType) globalMap
											.get("restResponseMediaType");
								}

								if (null != restProviderResponse_tRESTResponse_2) {
									String responseMediaSubType_tRESTResponse_2 = responseMediaType_tRESTResponse_2
											.getSubtype();
									if (responseMediaSubType_tRESTResponse_2.equals("json")
											|| responseMediaSubType_tRESTResponse_2.endsWith("+json")) {
										if (globalMap.containsKey("restResponseJsonStarted")) {
											outputStream_tRESTResponse_2.write(",".getBytes());
										} else {
											globalMap.put("restResponseJsonStarted", true);
										}
									}

									Class<? extends Object> responseBodyClass_tRESTResponse_2 = restProviderResponse_tRESTResponse_2
											.getClass();
									jakarta.ws.rs.ext.Providers messageBodyProviders_tRESTResponse_2 = messageContext_tRESTResponse_2
											.getProviders();
									jakarta.ws.rs.ext.MessageBodyWriter messageBodyWriter_tRESTResponse_2 = messageBodyProviders_tRESTResponse_2
											.getMessageBodyWriter(responseBodyClass_tRESTResponse_2,
													responseBodyClass_tRESTResponse_2, null,
													responseMediaType_tRESTResponse_2);

									if (messageBodyWriter_tRESTResponse_2 instanceof StreamingDOM4JProvider) {
										((StreamingDOM4JProvider) messageBodyWriter_tRESTResponse_2)
												.setGlobalMap(globalMap);
									}

									messageBodyWriter_tRESTResponse_2.writeTo(restProviderResponse_tRESTResponse_2,
											responseBodyClass_tRESTResponse_2, responseBodyClass_tRESTResponse_2,
											new java.lang.annotation.Annotation[] {}, responseMediaType_tRESTResponse_2,
											null, outputStream_tRESTResponse_2);
								}
								// initial variant
								// outputStream_tRESTResponse_2.write(String.valueOf(restProviderResponse_tRESTResponse_2).getBytes());
								outputStream_tRESTResponse_2.flush();
							}
						}

						tos_count_tRESTResponse_2++;

						/**
						 * [tRESTResponse_2 main ] stop
						 */

						/**
						 * [tRESTResponse_2 process_data_begin ] start
						 */

						s(currentComponent = "tRESTResponse_2");

						/**
						 * [tRESTResponse_2 process_data_begin ] stop
						 */

						/**
						 * [tRESTResponse_2 process_data_end ] start
						 */

						s(currentComponent = "tRESTResponse_2");

						/**
						 * [tRESTResponse_2 process_data_end ] stop
						 */

						/**
						 * [tWriteJSONField_1_In process_data_end ] start
						 */

						currentVirtualComponent = "tWriteJSONField_1";

						s(currentComponent = "tWriteJSONField_1_In");

						/**
						 * [tWriteJSONField_1_In process_data_end ] stop
						 */

						/**
						 * [tWriteJSONField_1_In end ] start
						 */

						currentVirtualComponent = "tWriteJSONField_1";

						s(currentComponent = "tWriteJSONField_1_In");

					}
				}

				String readFinishWithExceptionMarkWithPipeId_tWriteJSONField_1_In = "tWriteJSONField_1_In_FINISH_WITH_EXCEPTION"
						+ (queue_tWriteJSONField_1_In == null ? "" : queue_tWriteJSONField_1_In.hashCode());
				if (globalMap.containsKey(readFinishWithExceptionMarkWithPipeId_tWriteJSONField_1_In)) {
					if (!(globalMap instanceof java.util.concurrent.ConcurrentHashMap)) {
						globalMap.put(readFinishWithExceptionMarkWithPipeId_tWriteJSONField_1_In, null);// syn
					}
					globalMap.remove(readFinishWithExceptionMarkWithPipeId_tWriteJSONField_1_In);
					return;
				}
				globalMap.remove("queue_tWriteJSONField_1_In");

				if (!(globalMap instanceof java.util.concurrent.ConcurrentHashMap)) {
					globalMap.put(readFinishMarkWithPipeId_tWriteJSONField_1_In, null);// syn
				}
				globalMap.remove(readFinishMarkWithPipeId_tWriteJSONField_1_In);

				globalMap.put("tWriteJSONField_1_NB_LINE", nb_line_tWriteJSONField_1_In);
				log.debug("tWriteJSONField_1_In - Processed records count: " + nb_line_tWriteJSONField_1_In + " .");

				if (log.isDebugEnabled())
					log.debug("tWriteJSONField_1_In - " + ("Done."));

				ok_Hash.put("tWriteJSONField_1_In", true);
				end_Hash.put("tWriteJSONField_1_In", System.currentTimeMillis());

				/**
				 * [tWriteJSONField_1_In end ] stop
				 */

				/**
				 * [tRESTResponse_2 end ] start
				 */

				s(currentComponent = "tRESTResponse_2");

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row4", 2, 0,
						"tWriteJSONField_1_In", "tWriteJSONField_1_In", "tWriteJSONFieldIn", "tRESTResponse_2",
						"tRESTResponse_2", "tRESTResponse", "output")) {
					talendJobLogProcess(globalMap);
				}

				ok_Hash.put("tRESTResponse_2", true);
				end_Hash.put("tRESTResponse_2", System.currentTimeMillis());

				/**
				 * [tRESTResponse_2 end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			te.setVirtualComponentName(currentVirtualComponent);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tWriteJSONField_1_In finally ] start
				 */

				currentVirtualComponent = "tWriteJSONField_1";

				s(currentComponent = "tWriteJSONField_1_In");

				/**
				 * [tWriteJSONField_1_In finally ] stop
				 */

				/**
				 * [tRESTResponse_2 finally ] start
				 */

				s(currentComponent = "tRESTResponse_2");

				/**
				 * [tRESTResponse_2 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tWriteJSONField_1_In_SUBPROCESS_STATE", 1);
	}

	public static class row6Struct implements routines.system.IPersistableRow<row6Struct> {
		final static byte[] commonByteArrayLock_MBTC_POC_REST_Services = new byte[0];
		static byte[] commonByteArray_MBTC_POC_REST_Services = new byte[0];

		public String body;

		public String getBody() {
			return this.body;
		}

		public Boolean bodyIsNullable() {
			return true;
		}

		public Boolean bodyIsKey() {
			return false;
		}

		public Integer bodyLength() {
			return 0;
		}

		public Integer bodyPrecision() {
			return 0;
		}

		public String bodyDefault() {

			return "";

		}

		public String bodyComment() {

			return null;

		}

		public String bodyPattern() {

			return null;

		}

		public String bodyOriginalDbColumnName() {

			return "body";

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_MBTC_POC_REST_Services.length) {
					if (length < 1024 && commonByteArray_MBTC_POC_REST_Services.length == 0) {
						commonByteArray_MBTC_POC_REST_Services = new byte[1024];
					} else {
						commonByteArray_MBTC_POC_REST_Services = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_MBTC_POC_REST_Services, 0, length);
				strReturn = new String(commonByteArray_MBTC_POC_REST_Services, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.body = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_MBTC_POC_REST_Services) {

				try {

					int length = 0;

					this.body = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.body, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// String

				writeString(this.body, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("body=" + body);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (body == null) {
				sb.append("<null>");
			} else {
				sb.append(body);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row6Struct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(), object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public void tWriteJSONField_2_InProcess(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tWriteJSONField_2_In_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tWriteJSONField_2_In", "pJ1ixu_");

		String currentVirtualComponent = null;

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				row6Struct row6 = new row6Struct();

				/**
				 * [tRESTResponse_3 begin ] start
				 */

				sh("tRESTResponse_3");

				s(currentComponent = "tRESTResponse_3");

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row6");

				int tos_count_tRESTResponse_3 = 0;

				if (enableLogStash) {
					talendJobLog.addCM("tRESTResponse_3", "tRESTResponse_3", "tRESTResponse");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				/**
				 * [tRESTResponse_3 begin ] stop
				 */

				/**
				 * [tWriteJSONField_2_In begin ] start
				 */

				sh("tWriteJSONField_2_In");

				currentVirtualComponent = "tWriteJSONField_2";

				s(currentComponent = "tWriteJSONField_2_In");

				int tos_count_tWriteJSONField_2_In = 0;

				if (log.isDebugEnabled())
					log.debug("tWriteJSONField_2_In - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tWriteJSONField_2_In {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tWriteJSONField_2_In = new StringBuilder();
							log4jParamters_tWriteJSONField_2_In.append("Parameters:");
							log4jParamters_tWriteJSONField_2_In.append("JSONFIELD" + " = " + "body");
							log4jParamters_tWriteJSONField_2_In.append(" | ");
							log4jParamters_tWriteJSONField_2_In.append("DESTINATION" + " = " + "tWriteJSONField_2");
							log4jParamters_tWriteJSONField_2_In.append(" | ");
							log4jParamters_tWriteJSONField_2_In.append("REMOVE_ROOT" + " = " + "false");
							log4jParamters_tWriteJSONField_2_In.append(" | ");
							log4jParamters_tWriteJSONField_2_In.append("GROUPBYS" + " = " + "[]");
							log4jParamters_tWriteJSONField_2_In.append(" | ");
							log4jParamters_tWriteJSONField_2_In.append("QUOTE_ALL_VALUES" + " = " + "false");
							log4jParamters_tWriteJSONField_2_In.append(" | ");
							log4jParamters_tWriteJSONField_2_In.append("ALLOW_EMPTY_STRINGS" + " = " + "false");
							log4jParamters_tWriteJSONField_2_In.append(" | ");
							log4jParamters_tWriteJSONField_2_In.append("USE_SCIENTIFIC_NOTATION" + " = " + "false");
							log4jParamters_tWriteJSONField_2_In.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tWriteJSONField_2_In - " + (log4jParamters_tWriteJSONField_2_In));
						}
					}
					new BytesLimit65535_tWriteJSONField_2_In().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tWriteJSONField_2_In", "tWriteJSONField_2_In", "tWriteJSONFieldIn");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				int nb_line_tWriteJSONField_2_In = 0;
				org.kordamp.json.xml.XMLSerializer xmlSerializer_tWriteJSONField_2_In = new org.kordamp.json.xml.XMLSerializer();
				xmlSerializer_tWriteJSONField_2_In.clearNamespaces();
				xmlSerializer_tWriteJSONField_2_In.setSkipNamespaces(true);
				xmlSerializer_tWriteJSONField_2_In.setForceTopLevelObject(true);
				xmlSerializer_tWriteJSONField_2_In.setUseEmptyStrings(false);
				xmlSerializer_tWriteJSONField_2_In.setUseScientificNotation(false);

				java.util.Queue<row6Struct> queue_tWriteJSONField_2_In = (java.util.Queue<row6Struct>) globalMap
						.get("queue_tWriteJSONField_2_In");

				String readFinishMarkWithPipeId_tWriteJSONField_2_In = "tWriteJSONField_2_In_FINISH"
						+ (queue_tWriteJSONField_2_In == null ? "" : queue_tWriteJSONField_2_In.hashCode());
				String str_tWriteJSONField_2_In = null;

				while (!globalMap.containsKey(readFinishMarkWithPipeId_tWriteJSONField_2_In)
						|| !queue_tWriteJSONField_2_In.isEmpty()) {
					if (!queue_tWriteJSONField_2_In.isEmpty()) {

						/**
						 * [tWriteJSONField_2_In begin ] stop
						 */

						/**
						 * [tWriteJSONField_2_In main ] start
						 */

						currentVirtualComponent = "tWriteJSONField_2";

						s(currentComponent = "tWriteJSONField_2_In");

						row6Struct result_tWriteJSONField_2_In = queue_tWriteJSONField_2_In.poll();
						str_tWriteJSONField_2_In = result_tWriteJSONField_2_In.body;
						// Convert XML to JSON
						org.kordamp.json.JsonStandard jsonStandard_tWriteJSONField_2_In = org.kordamp.json.JsonStandard.LEGACY;
						xmlSerializer_tWriteJSONField_2_In.setJsonStandard(jsonStandard_tWriteJSONField_2_In);
						org.kordamp.json.JSON json_tWriteJSONField_2_In = xmlSerializer_tWriteJSONField_2_In
								.read(str_tWriteJSONField_2_In);
						row6.body = org.kordamp.json.util.JSONUtils.jsonToStandardizedString(json_tWriteJSONField_2_In,
								jsonStandard_tWriteJSONField_2_In);

						nb_line_tWriteJSONField_2_In++;

						tos_count_tWriteJSONField_2_In++;

						/**
						 * [tWriteJSONField_2_In main ] stop
						 */

						/**
						 * [tWriteJSONField_2_In process_data_begin ] start
						 */

						currentVirtualComponent = "tWriteJSONField_2";

						s(currentComponent = "tWriteJSONField_2_In");

						/**
						 * [tWriteJSONField_2_In process_data_begin ] stop
						 */

						/**
						 * [tRESTResponse_3 main ] start
						 */

						s(currentComponent = "tRESTResponse_3");

						if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

								, "row6", "tWriteJSONField_2_In", "tWriteJSONField_2_In", "tWriteJSONFieldIn",
								"tRESTResponse_3", "tRESTResponse_3", "tRESTResponse"

						)) {
							talendJobLogProcess(globalMap);
						}

						if (log.isTraceEnabled()) {
							log.trace("row6 - " + (row6 == null ? "" : row6.toLogString()));
						}

						java.io.OutputStream outputStream_tRESTResponse_3 = (java.io.OutputStream) globalMap
								.get("restResponseStream");
						boolean responseAlreadySent_tRESTResponse_3 = globalMap.containsKey("restResponse");

						if (null == outputStream_tRESTResponse_3 && responseAlreadySent_tRESTResponse_3) {
							throw new RuntimeException("Rest response already sent.");
						} else if (!globalMap.containsKey("restRequest")) {
							throw new RuntimeException("Not received rest request yet.");
						} else {
							Integer restProviderStatusCode_tRESTResponse_3 = 409;

							Object restProviderResponse_tRESTResponse_3 = null;
							restProviderResponse_tRESTResponse_3 = row6.body;

							java.util.Map<String, String> restProviderResponseHeaders_tRESTResponse_3 = new java.util.TreeMap<String, String>(
									String.CASE_INSENSITIVE_ORDER);
							java.lang.StringBuilder restProviderResponseHeader_cookies_tRESTResponse_3 = new java.lang.StringBuilder();
							final String setCookieHeader = "Set-Cookie";

							if (restProviderResponseHeader_cookies_tRESTResponse_3.length() > 0) {
								restProviderResponseHeaders_tRESTResponse_3.put(setCookieHeader,
										restProviderResponseHeader_cookies_tRESTResponse_3.toString());
							}

							java.util.Map<String, Object> restRequest_tRESTResponse_3 = (java.util.Map<String, Object>) globalMap
									.get("restRequest");
							org.apache.cxf.jaxrs.ext.MessageContext messageContext_tRESTResponse_3 = (org.apache.cxf.jaxrs.ext.MessageContext) restRequest_tRESTResponse_3
									.get("MESSAGE_CONTEXT");

							if (null == outputStream_tRESTResponse_3) {
								java.util.Map<String, Object> restResponse_tRESTResponse_3 = new java.util.HashMap<String, Object>();
								restResponse_tRESTResponse_3.put("BODY", restProviderResponse_tRESTResponse_3);
								restResponse_tRESTResponse_3.put("STATUS", restProviderStatusCode_tRESTResponse_3);
								restResponse_tRESTResponse_3.put("HEADERS",
										restProviderResponseHeaders_tRESTResponse_3);
								restResponse_tRESTResponse_3.put("drop.json.root.element", Boolean.valueOf(false));
								globalMap.put("restResponse", restResponse_tRESTResponse_3);

							} else {

								jakarta.ws.rs.core.MediaType responseMediaType_tRESTResponse_3 = null;
								if (!responseAlreadySent_tRESTResponse_3) {
									org.apache.cxf.message.Message currentMessage = null;
									if (org.apache.cxf.jaxrs.utils.JAXRSUtils.getCurrentMessage() != null) {
										currentMessage = org.apache.cxf.jaxrs.utils.JAXRSUtils.getCurrentMessage();
									} else {
										currentMessage = ((org.apache.cxf.message.Message) restRequest_tRESTResponse_3
												.get("CURRENT_MESSAGE"));
									}

									if (currentMessage != null && currentMessage.getExchange() != null) {
										currentMessage.getExchange().put(StreamingDOM4JProvider.SUPRESS_XML_DECLARATION,
												true);
									}

									messageContext_tRESTResponse_3.put(org.apache.cxf.message.Message.RESPONSE_CODE,
											restProviderStatusCode_tRESTResponse_3);
									jakarta.ws.rs.core.MultivaluedMap<String, String> headersMultivaluedMap_tRESTResponse_3 = new org.apache.cxf.jaxrs.impl.MetadataMap<String, String>();
									for (java.util.Map.Entry<String, String> multivaluedHeader : restProviderResponseHeaders_tRESTResponse_3
											.entrySet()) {
										headersMultivaluedMap_tRESTResponse_3.putSingle(multivaluedHeader.getKey(),
												multivaluedHeader.getValue());
									}
									messageContext_tRESTResponse_3.put(org.apache.cxf.message.Message.PROTOCOL_HEADERS,
											headersMultivaluedMap_tRESTResponse_3);

									String responseContentType_tRESTResponse_3 = null;

									if (currentMessage != null && currentMessage.getExchange() != null) {
										responseContentType_tRESTResponse_3 = (String) currentMessage.getExchange()
												.get(org.apache.cxf.message.Message.CONTENT_TYPE);
									}

									if (null == responseContentType_tRESTResponse_3) {
										// this should not be needed, just in case. set it to the first value in the
										// sorted list returned from HttpHeaders
										responseMediaType_tRESTResponse_3 = messageContext_tRESTResponse_3
												.getHttpHeaders().getAcceptableMediaTypes().get(0);
									} else {
										responseMediaType_tRESTResponse_3 = org.apache.cxf.jaxrs.utils.JAXRSUtils
												.toMediaType(responseContentType_tRESTResponse_3);
									}
									globalMap.put("restResponseMediaType", responseMediaType_tRESTResponse_3);

									String responseMediaSubType_tRESTResponse_3 = responseMediaType_tRESTResponse_3
											.getSubtype();
									if (responseMediaSubType_tRESTResponse_3.equals("xml")
											|| responseMediaSubType_tRESTResponse_3.endsWith("+xml")) {
										outputStream_tRESTResponse_3.write("<wrapper>".getBytes());
										globalMap.put("restResponseWrappingClosure", "</wrapper>");
									}
									if (responseMediaSubType_tRESTResponse_3.equals("json")
											|| responseMediaSubType_tRESTResponse_3.endsWith("+json")) {
										outputStream_tRESTResponse_3.write("[".getBytes());
										globalMap.put("restResponseWrappingClosure", "]");
									}

									globalMap.put("restResponse", true);
								} else {
									responseMediaType_tRESTResponse_3 = (jakarta.ws.rs.core.MediaType) globalMap
											.get("restResponseMediaType");
								}

								if (null != restProviderResponse_tRESTResponse_3) {
									String responseMediaSubType_tRESTResponse_3 = responseMediaType_tRESTResponse_3
											.getSubtype();
									if (responseMediaSubType_tRESTResponse_3.equals("json")
											|| responseMediaSubType_tRESTResponse_3.endsWith("+json")) {
										if (globalMap.containsKey("restResponseJsonStarted")) {
											outputStream_tRESTResponse_3.write(",".getBytes());
										} else {
											globalMap.put("restResponseJsonStarted", true);
										}
									}

									Class<? extends Object> responseBodyClass_tRESTResponse_3 = restProviderResponse_tRESTResponse_3
											.getClass();
									jakarta.ws.rs.ext.Providers messageBodyProviders_tRESTResponse_3 = messageContext_tRESTResponse_3
											.getProviders();
									jakarta.ws.rs.ext.MessageBodyWriter messageBodyWriter_tRESTResponse_3 = messageBodyProviders_tRESTResponse_3
											.getMessageBodyWriter(responseBodyClass_tRESTResponse_3,
													responseBodyClass_tRESTResponse_3, null,
													responseMediaType_tRESTResponse_3);

									if (messageBodyWriter_tRESTResponse_3 instanceof StreamingDOM4JProvider) {
										((StreamingDOM4JProvider) messageBodyWriter_tRESTResponse_3)
												.setGlobalMap(globalMap);
									}

									messageBodyWriter_tRESTResponse_3.writeTo(restProviderResponse_tRESTResponse_3,
											responseBodyClass_tRESTResponse_3, responseBodyClass_tRESTResponse_3,
											new java.lang.annotation.Annotation[] {}, responseMediaType_tRESTResponse_3,
											null, outputStream_tRESTResponse_3);
								}
								// initial variant
								// outputStream_tRESTResponse_3.write(String.valueOf(restProviderResponse_tRESTResponse_3).getBytes());
								outputStream_tRESTResponse_3.flush();
							}
						}

						tos_count_tRESTResponse_3++;

						/**
						 * [tRESTResponse_3 main ] stop
						 */

						/**
						 * [tRESTResponse_3 process_data_begin ] start
						 */

						s(currentComponent = "tRESTResponse_3");

						/**
						 * [tRESTResponse_3 process_data_begin ] stop
						 */

						/**
						 * [tRESTResponse_3 process_data_end ] start
						 */

						s(currentComponent = "tRESTResponse_3");

						/**
						 * [tRESTResponse_3 process_data_end ] stop
						 */

						/**
						 * [tWriteJSONField_2_In process_data_end ] start
						 */

						currentVirtualComponent = "tWriteJSONField_2";

						s(currentComponent = "tWriteJSONField_2_In");

						/**
						 * [tWriteJSONField_2_In process_data_end ] stop
						 */

						/**
						 * [tWriteJSONField_2_In end ] start
						 */

						currentVirtualComponent = "tWriteJSONField_2";

						s(currentComponent = "tWriteJSONField_2_In");

					}
				}

				String readFinishWithExceptionMarkWithPipeId_tWriteJSONField_2_In = "tWriteJSONField_2_In_FINISH_WITH_EXCEPTION"
						+ (queue_tWriteJSONField_2_In == null ? "" : queue_tWriteJSONField_2_In.hashCode());
				if (globalMap.containsKey(readFinishWithExceptionMarkWithPipeId_tWriteJSONField_2_In)) {
					if (!(globalMap instanceof java.util.concurrent.ConcurrentHashMap)) {
						globalMap.put(readFinishWithExceptionMarkWithPipeId_tWriteJSONField_2_In, null);// syn
					}
					globalMap.remove(readFinishWithExceptionMarkWithPipeId_tWriteJSONField_2_In);
					return;
				}
				globalMap.remove("queue_tWriteJSONField_2_In");

				if (!(globalMap instanceof java.util.concurrent.ConcurrentHashMap)) {
					globalMap.put(readFinishMarkWithPipeId_tWriteJSONField_2_In, null);// syn
				}
				globalMap.remove(readFinishMarkWithPipeId_tWriteJSONField_2_In);

				globalMap.put("tWriteJSONField_2_NB_LINE", nb_line_tWriteJSONField_2_In);
				log.debug("tWriteJSONField_2_In - Processed records count: " + nb_line_tWriteJSONField_2_In + " .");

				if (log.isDebugEnabled())
					log.debug("tWriteJSONField_2_In - " + ("Done."));

				ok_Hash.put("tWriteJSONField_2_In", true);
				end_Hash.put("tWriteJSONField_2_In", System.currentTimeMillis());

				/**
				 * [tWriteJSONField_2_In end ] stop
				 */

				/**
				 * [tRESTResponse_3 end ] start
				 */

				s(currentComponent = "tRESTResponse_3");

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row6", 2, 0,
						"tWriteJSONField_2_In", "tWriteJSONField_2_In", "tWriteJSONFieldIn", "tRESTResponse_3",
						"tRESTResponse_3", "tRESTResponse", "output")) {
					talendJobLogProcess(globalMap);
				}

				ok_Hash.put("tRESTResponse_3", true);
				end_Hash.put("tRESTResponse_3", System.currentTimeMillis());

				/**
				 * [tRESTResponse_3 end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			te.setVirtualComponentName(currentVirtualComponent);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tWriteJSONField_2_In finally ] start
				 */

				currentVirtualComponent = "tWriteJSONField_2";

				s(currentComponent = "tWriteJSONField_2_In");

				/**
				 * [tWriteJSONField_2_In finally ] stop
				 */

				/**
				 * [tRESTResponse_3 finally ] start
				 */

				s(currentComponent = "tRESTResponse_3");

				/**
				 * [tRESTResponse_3 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tWriteJSONField_2_In_SUBPROCESS_STATE", 1);
	}

	public void talendJobLogProcess(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("talendJobLog_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [talendJobLog begin ] start
				 */

				sh("talendJobLog");

				s(currentComponent = "talendJobLog");

				int tos_count_talendJobLog = 0;

				for (JobStructureCatcherUtils.JobStructureCatcherMessage jcm : talendJobLog.getMessages()) {
					org.talend.job.audit.JobContextBuilder builder_talendJobLog = org.talend.job.audit.JobContextBuilder
							.create().jobName(jcm.job_name).jobId(jcm.job_id).jobVersion(jcm.job_version)
							.custom("process_id", jcm.pid).custom("thread_id", jcm.tid).custom("pid", pid)
							.custom("father_pid", fatherPid).custom("root_pid", rootPid);
					org.talend.logging.audit.Context log_context_talendJobLog = null;

					if (jcm.log_type == JobStructureCatcherUtils.LogType.PERFORMANCE) {
						long timeMS = jcm.end_time - jcm.start_time;
						String duration = String.valueOf(timeMS);

						log_context_talendJobLog = builder_talendJobLog.sourceId(jcm.sourceId)
								.sourceLabel(jcm.sourceLabel).sourceConnectorType(jcm.sourceComponentName)
								.targetId(jcm.targetId).targetLabel(jcm.targetLabel)
								.targetConnectorType(jcm.targetComponentName).connectionName(jcm.current_connector)
								.rows(jcm.row_count).duration(duration).build();
						auditLogger_talendJobLog.flowExecution(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.JOBSTART) {
						log_context_talendJobLog = builder_talendJobLog.timestamp(jcm.moment).build();
						auditLogger_talendJobLog.jobstart(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.JOBEND) {
						long timeMS = jcm.end_time - jcm.start_time;
						String duration = String.valueOf(timeMS);

						log_context_talendJobLog = builder_talendJobLog.timestamp(jcm.moment).duration(duration)
								.status(jcm.status).build();
						auditLogger_talendJobLog.jobstop(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.RUNCOMPONENT) {
						log_context_talendJobLog = builder_talendJobLog.timestamp(jcm.moment)
								.connectorType(jcm.component_name).connectorId(jcm.component_id)
								.connectorLabel(jcm.component_label).build();
						auditLogger_talendJobLog.runcomponent(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.FLOWINPUT) {// log current component
																							// input line
						long timeMS = jcm.end_time - jcm.start_time;
						String duration = String.valueOf(timeMS);

						log_context_talendJobLog = builder_talendJobLog.connectorType(jcm.component_name)
								.connectorId(jcm.component_id).connectorLabel(jcm.component_label)
								.connectionName(jcm.current_connector).connectionType(jcm.current_connector_type)
								.rows(jcm.total_row_number).duration(duration).build();
						auditLogger_talendJobLog.flowInput(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.FLOWOUTPUT) {// log current component
																								// output/reject line
						long timeMS = jcm.end_time - jcm.start_time;
						String duration = String.valueOf(timeMS);

						log_context_talendJobLog = builder_talendJobLog.connectorType(jcm.component_name)
								.connectorId(jcm.component_id).connectorLabel(jcm.component_label)
								.connectionName(jcm.current_connector).connectionType(jcm.current_connector_type)
								.rows(jcm.total_row_number).duration(duration).build();
						auditLogger_talendJobLog.flowOutput(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.JOBERROR) {
						java.lang.Exception e_talendJobLog = jcm.exception;
						if (e_talendJobLog != null) {
							try (java.io.StringWriter sw_talendJobLog = new java.io.StringWriter();
									java.io.PrintWriter pw_talendJobLog = new java.io.PrintWriter(sw_talendJobLog)) {
								e_talendJobLog.printStackTrace(pw_talendJobLog);
								builder_talendJobLog.custom("stacktrace", sw_talendJobLog.getBuffer().substring(0,
										java.lang.Math.min(sw_talendJobLog.getBuffer().length(), 512)));
							}
						}

						if (jcm.extra_info != null) {
							builder_talendJobLog.connectorId(jcm.component_id).custom("extra_info", jcm.extra_info);
						}

						log_context_talendJobLog = builder_talendJobLog
								.connectorType(jcm.component_id.substring(0, jcm.component_id.lastIndexOf('_')))
								.connectorId(jcm.component_id)
								.connectorLabel(jcm.component_label == null ? jcm.component_id : jcm.component_label)
								.build();

						auditLogger_talendJobLog.exception(log_context_talendJobLog);
					}

				}

				/**
				 * [talendJobLog begin ] stop
				 */

				/**
				 * [talendJobLog main ] start
				 */

				s(currentComponent = "talendJobLog");

				tos_count_talendJobLog++;

				/**
				 * [talendJobLog main ] stop
				 */

				/**
				 * [talendJobLog process_data_begin ] start
				 */

				s(currentComponent = "talendJobLog");

				/**
				 * [talendJobLog process_data_begin ] stop
				 */

				/**
				 * [talendJobLog process_data_end ] start
				 */

				s(currentComponent = "talendJobLog");

				/**
				 * [talendJobLog process_data_end ] stop
				 */

				/**
				 * [talendJobLog end ] start
				 */

				s(currentComponent = "talendJobLog");

				ok_Hash.put("talendJobLog", true);
				end_Hash.put("talendJobLog", System.currentTimeMillis());

				/**
				 * [talendJobLog end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException) && !(e instanceof TDieException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [talendJobLog finally ] start
				 */

				s(currentComponent = "talendJobLog");

				/**
				 * [talendJobLog finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("talendJobLog_SUBPROCESS_STATE", 1);
	}

	public String resuming_logs_dir_path = null;
	public String resuming_checkpoint_path = null;
	public String parent_part_launcher = null;
	private String resumeEntryMethodName = null;
	private boolean globalResumeTicket = false;

	public boolean watch = false;
	// portStats is null, it means don't execute the statistics
	public Integer portStats = null;
	public int portTraces = 4334;
	public String clientHost;
	public String defaultClientHost = "localhost";
	public String contextStr = "Default";
	public boolean isDefaultContext = true;
	public String pid = "0";
	public String rootPid = null;
	public String fatherPid = null;
	public String fatherNode = null;
	public long startTime = 0;
	public boolean isChildJob = false;
	public String log4jLevel = "";

	private boolean enableLogStash;
	private boolean enableLineage;

	private boolean execStat = true;

	private ThreadLocal<java.util.Map<String, String>> threadLocal = new ThreadLocal<java.util.Map<String, String>>() {
		protected java.util.Map<String, String> initialValue() {
			java.util.Map<String, String> threadRunResultMap = new java.util.HashMap<String, String>();
			threadRunResultMap.put("errorCode", null);
			threadRunResultMap.put("status", "");
			return threadRunResultMap;
		};
	};

	protected PropertiesWithType context_param = new PropertiesWithType();
	public java.util.Map<String, Object> parentContextMap = new java.util.HashMap<String, Object>();

	public String status = "";

	private final static java.util.Properties jobInfo = new java.util.Properties();
	private final static java.util.Map<String, String> mdcInfo = new java.util.HashMap<>();
	private final static java.util.concurrent.atomic.AtomicLong subJobPidCounter = new java.util.concurrent.atomic.AtomicLong();

	public static void main(String[] args) {
		final REST_Services REST_ServicesClass = new REST_Services();

		int exitCode = REST_ServicesClass.runJobInTOS(args);
		if (exitCode == 0) {
			log.info("TalendJob: 'REST_Services' - Done.");
		}

		System.exit(exitCode);
	}

	private void getjobInfo() {
		final String TEMPLATE_PATH = "src/main/templates/jobInfo_template.properties";
		final String BUILD_PATH = "../jobInfo.properties";
		final String path = this.getClass().getResource("").getPath();
		if (path.lastIndexOf("target") > 0) {
			final java.io.File templateFile = new java.io.File(
					path.substring(0, path.lastIndexOf("target")).concat(TEMPLATE_PATH));
			if (templateFile.exists()) {
				readJobInfo(templateFile);
				return;
			}
		}
		readJobInfo(new java.io.File(BUILD_PATH));
	}

	private void readJobInfo(java.io.File jobInfoFile) {

		if (jobInfoFile.exists()) {
			try (java.io.InputStream is = new java.io.FileInputStream(jobInfoFile)) {
				jobInfo.load(is);
			} catch (IOException e) {

				log.debug("Read jobInfo.properties file fail: " + e.getMessage());

			}
		}
		log.info(String.format("Project name: %s\tJob name: %s\tGIT Commit ID: %s\tTalend Version: %s", projectName,
				jobName, jobInfo.getProperty("gitCommitId"), "8.0.1.20260102_0846-patch"));

	}

	public String[][] runJob(String[] args) {

		int exitCode = runJobInTOS(args);
		String[][] bufferValue = new String[][] { { Integer.toString(exitCode) } };

		return bufferValue;
	}

	public boolean hastBufferOutputComponent() {
		boolean hastBufferOutput = false;

		return hastBufferOutput;
	}

	public int runJobInTOS(String[] args) {
		// reset status
		status = "";

		String lastStr = "";
		for (String arg : args) {
			if (arg.equalsIgnoreCase("--context_param")) {
				lastStr = arg;
			} else if (lastStr.equals("")) {
				evalParam(arg);
			} else {
				evalParam(lastStr + " " + arg);
				lastStr = "";
			}
		}

		final boolean enableCBP = false;
		boolean inOSGi = routines.system.BundleUtils.inOSGi();

		enableLogStash = "true".equalsIgnoreCase(System.getProperty("audit.enabled"));

		if (!"".equals(log4jLevel)) {

			if ("trace".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.TRACE);
			} else if ("debug".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.DEBUG);
			} else if ("info".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.INFO);
			} else if ("warn".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.WARN);
			} else if ("error".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.ERROR);
			} else if ("fatal".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.FATAL);
			} else if ("off".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.OFF);
			}
			org.apache.logging.log4j.core.config.Configurator
					.setLevel(org.apache.logging.log4j.LogManager.getRootLogger().getName(), log.getLevel());

		}

		getjobInfo();
		log.info("TalendJob: 'REST_Services' - Start.");

		java.util.Set<Object> jobInfoKeys = jobInfo.keySet();
		for (Object jobInfoKey : jobInfoKeys) {
			org.slf4j.MDC.put("_" + jobInfoKey.toString(), jobInfo.get(jobInfoKey).toString());
		}
		org.slf4j.MDC.put("_pid", pid);
		org.slf4j.MDC.put("_rootPid", rootPid);
		org.slf4j.MDC.put("_fatherPid", fatherPid);
		org.slf4j.MDC.put("_projectName", projectName);
		org.slf4j.MDC.put("_startTimestamp", java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC)
				.format(java.time.format.DateTimeFormatter.ISO_INSTANT));
		org.slf4j.MDC.put("_jobRepositoryId", "_k3w0kOt_EfCo6J9Ef-vlPw");
		org.slf4j.MDC.put("_compiledAtTimestamp", "2026-01-07T06:23:42.601955100Z");

		java.lang.management.RuntimeMXBean mx = java.lang.management.ManagementFactory.getRuntimeMXBean();
		String[] mxNameTable = mx.getName().split("@"); //$NON-NLS-1$
		if (mxNameTable.length == 2) {
			org.slf4j.MDC.put("_systemPid", mxNameTable[0]);
		} else {
			org.slf4j.MDC.put("_systemPid", String.valueOf(java.lang.Thread.currentThread().getId()));
		}

		if (enableLogStash) {
			java.util.Properties properties_talendJobLog = new java.util.Properties();
			properties_talendJobLog.setProperty("root.logger", "audit");
			properties_talendJobLog.setProperty("encoding", "UTF-8");
			properties_talendJobLog.setProperty("application.name", "Talend Studio");
			properties_talendJobLog.setProperty("service.name", "Talend Studio Job");
			properties_talendJobLog.setProperty("instance.name", "Talend Studio Job Instance");
			properties_talendJobLog.setProperty("propagate.appender.exceptions", "none");
			properties_talendJobLog.setProperty("log.appender", "file");
			properties_talendJobLog.setProperty("appender.file.path", "audit.json");
			properties_talendJobLog.setProperty("appender.file.maxsize", "52428800");
			properties_talendJobLog.setProperty("appender.file.maxbackup", "20");
			properties_talendJobLog.setProperty("host", "false");

			System.getProperties().stringPropertyNames().stream().filter(it -> it.startsWith("audit.logger."))
					.forEach(key -> properties_talendJobLog.setProperty(key.substring("audit.logger.".length()),
							System.getProperty(key)));

			org.apache.logging.log4j.core.config.Configurator
					.setLevel(properties_talendJobLog.getProperty("root.logger"), org.apache.logging.log4j.Level.DEBUG);

			auditLogger_talendJobLog = org.talend.job.audit.JobEventAuditLoggerFactory
					.createJobAuditLogger(properties_talendJobLog);
		}

		if (clientHost == null) {
			clientHost = defaultClientHost;
		}

		if (pid == null || "0".equals(pid)) {
			pid = TalendString.getAsciiRandomString(6);
		}

		org.slf4j.MDC.put("_pid", pid);

		if (rootPid == null) {
			rootPid = pid;
		}

		org.slf4j.MDC.put("_rootPid", rootPid);

		if (fatherPid == null) {
			fatherPid = pid;
		} else {
			isChildJob = true;
		}
		org.slf4j.MDC.put("_fatherPid", fatherPid);

		if (portStats != null) {
			// portStats = -1; //for testing
			if (portStats < 0 || portStats > 65535) {
				// issue:10869, the portStats is invalid, so this client socket can't open
				System.err.println("The statistics socket port " + portStats + " is invalid.");
				execStat = false;
			}
		} else {
			execStat = false;
		}

		try {
			java.util.Dictionary<String, Object> jobProperties = null;
			if (inOSGi) {
				jobProperties = routines.system.BundleUtils.getJobProperties(jobName);

				if (jobProperties != null && jobProperties.get("context") != null) {
					contextStr = (String) jobProperties.get("context");
				}

				if (jobProperties != null && jobProperties.get("taskExecutionId") != null) {
					taskExecutionId = (String) jobProperties.get("taskExecutionId");
				}

				// extract ids from parent route
				if (null == taskExecutionId || taskExecutionId.isEmpty()) {
					for (String arg : args) {
						if (arg.startsWith("--context_param")
								&& (arg.contains("taskExecutionId") || arg.contains("jobExecutionId"))) {

							String keyValue = arg.replace("--context_param", "");
							String[] parts = keyValue.split("=");
							String[] cleanParts = java.util.Arrays.stream(parts).filter(s -> !s.isEmpty())
									.toArray(String[]::new);
							if (cleanParts.length == 2) {
								String key = cleanParts[0];
								String value = cleanParts[1];
								if ("taskExecutionId".equals(key.trim()) && null != value) {
									taskExecutionId = value.trim();
								} else if ("jobExecutionId".equals(key.trim()) && null != value) {
									jobExecutionId = value.trim();
								}
							}
						}
					}
				}
			}

			// first load default key-value pairs from application.properties
			if (isStandaloneMS) {
				context.putAll(this.getDefaultProperties());
			}
			// call job/subjob with an existing context, like: --context=production. if
			// without this parameter, there will use the default context instead.
			java.io.InputStream inContext = REST_Services.class.getClassLoader()
					.getResourceAsStream("mbtc_poc/rest_services_0_1/contexts/" + contextStr + ".properties");
			if (inContext == null) {
				inContext = REST_Services.class.getClassLoader()
						.getResourceAsStream("config/contexts/" + contextStr + ".properties");
			}
			if (inContext != null) {
				try {
					// defaultProps is in order to keep the original context value
					if (context != null && context.isEmpty()) {
						defaultProps.load(inContext);
						if (inOSGi && jobProperties != null) {
							java.util.Enumeration<String> keys = jobProperties.keys();
							while (keys.hasMoreElements()) {
								String propKey = keys.nextElement();
								if (defaultProps.containsKey(propKey)) {
									defaultProps.put(propKey, (String) jobProperties.get(propKey));
								}
							}
						}
						context = new ContextProperties(defaultProps);
					}
					if (isStandaloneMS) {
						// override context key-value pairs if provided using --context=contextName
						defaultProps.load(inContext);
						context.putAll(defaultProps);
					}
				} finally {
					inContext.close();
				}
			} else if (!isDefaultContext) {
				// print info and job continue to run, for case: context_param is not empty.
				System.err.println("Could not find the context " + contextStr);
			}
			// override key-value pairs if provided via --config.location=file1.file2 OR
			// --config.additional-location=file1,file2
			if (isStandaloneMS) {
				context.putAll(this.getAdditionalProperties());
			}

			// override key-value pairs if provide via command line like
			// --key1=value1,--key2=value2
			if (!context_param.isEmpty()) {
				context.putAll(context_param);
				// set types for params from parentJobs
				for (Object key : context_param.keySet()) {
					String context_key = key.toString();
					String context_type = context_param.getContextType(context_key);
					context.setContextType(context_key, context_type);

				}
			}
			class ContextProcessing {
				private void processContext_0() {
				}

				public void processAllContext() {
					processContext_0();
				}
			}

			new ContextProcessing().processAllContext();
		} catch (java.io.IOException ie) {
			System.err.println("Could not load context " + contextStr);
			ie.printStackTrace();
		}

		// get context value from parent directly
		if (parentContextMap != null && !parentContextMap.isEmpty()) {
		}

		// Resume: init the resumeUtil
		resumeEntryMethodName = ResumeUtil.getResumeEntryMethodName(resuming_checkpoint_path);
		resumeUtil = new ResumeUtil(resuming_logs_dir_path, isChildJob, rootPid);
		resumeUtil.initCommonInfo(pid, rootPid, fatherPid, projectName, jobName, contextStr, jobVersion);

		List<String> parametersToEncrypt = new java.util.ArrayList<String>();
		// Resume: jobStart
		resumeUtil.addLog("JOB_STARTED", "JOB:" + jobName, parent_part_launcher, Thread.currentThread().getId() + "",
				"", "", "", "", ResumeUtil.convertToJsonText(context, ContextProperties.class, parametersToEncrypt));

		org.slf4j.MDC.put("_context", contextStr);
		log.info("TalendJob: 'REST_Services' - Started.");
		java.util.Optional.ofNullable(org.slf4j.MDC.getCopyOfContextMap()).ifPresent(mdcInfo::putAll);

		if (execStat) {
			try {
				runStat.openSocket(!isChildJob);
				runStat.setAllPID(rootPid, fatherPid, pid, jobName);
				runStat.startThreadStat(clientHost, portStats);
				runStat.updateStatOnJob(RunStat.JOBSTART, fatherNode);
			} catch (java.io.IOException ioException) {
				ioException.printStackTrace();
			}
		}

		java.util.concurrent.ConcurrentHashMap<Object, Object> concurrentHashMap = new java.util.concurrent.ConcurrentHashMap<Object, Object>();
		globalMap.put("concurrentHashMap", concurrentHashMap);

		long startUsedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long endUsedMemory = 0;
		long end = 0;

		startTime = System.currentTimeMillis();

		this.globalResumeTicket = true;// to run tPreJob

		if (enableLogStash) {
			talendJobLog.addJobStartMessage();
			try {
				talendJobLogProcess(globalMap);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}

		this.globalResumeTicket = false;// to run others jobs

		try {
			errorCode = null;
			tRESTRequest_1_LoopProcess(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (TalendException e_tRESTRequest_1_Loop) {
			globalMap.put("tRESTRequest_1_Loop_SUBPROCESS_STATE", -1);

			e_tRESTRequest_1_Loop.printStackTrace();

		}

		this.globalResumeTicket = true;// to run tPostJob

		end = System.currentTimeMillis();

		if (watch) {
			System.out.println((end - startTime) + " milliseconds");
		}

		endUsedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		if (false) {
			System.out
					.println((endUsedMemory - startUsedMemory) + " bytes memory increase when running : REST_Services");
		}
		if (enableLogStash) {
			talendJobLog.addJobEndMessage(startTime, end, status);
			try {
				talendJobLogProcess(globalMap);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}

		if (execStat) {
			runStat.updateStatOnJob(RunStat.JOBEND, fatherNode);
			runStat.stopThreadStat();
		}

		int returnCode = 0;

		if (errorCode == null) {
			returnCode = status != null && status.equals("failure") ? 1 : 0;
		} else {
			returnCode = errorCode.intValue();
		}
		resumeUtil.addLog("JOB_ENDED", "JOB:" + jobName, parent_part_launcher, Thread.currentThread().getId() + "", "",
				"" + returnCode, "", "", "");
		resumeUtil.flush();

		org.slf4j.MDC.remove("_subJobName");
		org.slf4j.MDC.remove("_subJobPid");
		org.slf4j.MDC.remove("_systemPid");
		log.info("TalendJob: 'REST_Services' - Finished - status: " + status + " returnCode: " + returnCode);

		return returnCode;

	}

	// only for OSGi env
	public void destroy() {
		// add CBP code for OSGI Executions
		if (null != taskExecutionId && !taskExecutionId.isEmpty()) {
			try {
				org.talend.metrics.DataReadTracker.setExecutionId(taskExecutionId, jobExecutionId, false);
				org.talend.metrics.DataReadTracker.sealCounter();
				org.talend.metrics.DataReadTracker.reset();
			} catch (Exception | NoClassDefFoundError e) {
				// ignore
			}
		}

		// check for orphan threads if still alive after undeploy
		synchronized (threadList) {
			java.util.Iterator<Thread> it = threadList.iterator();
			while (it.hasNext()) {
				Thread thread = it.next();
				if (thread != null && thread.isAlive()) {
					System.err.println(
							"Initiating thread cleanup prior to bundle undeployment. This is a precautionary step to ensure no no memory leaks.");
					System.err.println("Forcefully interrupting thread with ID = " + thread.getId()
							+ ". This may result in expected errors due to abrupt termination. Please verify if the thread was performing critical operations.");
					thread.interrupt();
				}
				if (thread == null || !thread.isAlive()) {
					it.remove();
				}
			}
		}
		// end of destroy()
	}

	private java.util.Map<String, Object> getSharedConnections4REST() {
		java.util.Map<String, Object> connections = new java.util.HashMap<String, Object>();

		return connections;
	}

	private void evalParam(String arg) {
		if (arg.startsWith("--resuming_logs_dir_path")) {
			resuming_logs_dir_path = arg.substring(25);
		} else if (arg.startsWith("--resuming_checkpoint_path")) {
			resuming_checkpoint_path = arg.substring(27);
		} else if (arg.startsWith("--parent_part_launcher")) {
			parent_part_launcher = arg.substring(23);
		} else if (arg.startsWith("--watch")) {
			watch = true;
		} else if (arg.startsWith("--stat_port=")) {
			String portStatsStr = arg.substring(12);
			if (portStatsStr != null && !portStatsStr.equals("null")) {
				portStats = Integer.parseInt(portStatsStr);
			}
		} else if (arg.startsWith("--trace_port=")) {
			portTraces = Integer.parseInt(arg.substring(13));
		} else if (arg.startsWith("--client_host=")) {
			clientHost = arg.substring(14);
		} else if (arg.startsWith("--context=")) {
			contextStr = arg.substring(10);
			isDefaultContext = false;
		} else if (arg.startsWith("--father_pid=")) {
			fatherPid = arg.substring(13);
		} else if (arg.startsWith("--root_pid=")) {
			rootPid = arg.substring(11);
		} else if (arg.startsWith("--father_node=")) {
			fatherNode = arg.substring(14);
		} else if (arg.startsWith("--pid=")) {
			pid = arg.substring(6);
		} else if (arg.startsWith("--context_type")) {
			String keyValue = arg.substring(15);
			int index = -1;
			if (keyValue != null && (index = keyValue.indexOf('=')) > -1) {
				if (fatherPid == null) {
					context_param.setContextType(keyValue.substring(0, index),
							replaceEscapeChars(keyValue.substring(index + 1)));
				} else { // the subjob won't escape the especial chars
					context_param.setContextType(keyValue.substring(0, index), keyValue.substring(index + 1));
				}

			}

		} else if (arg.startsWith("--context_param")) {
			String keyValue = arg.substring(16);
			int index = -1;
			if (keyValue != null && (index = keyValue.indexOf('=')) > -1) {
				if (fatherPid == null) {
					context_param.put(keyValue.substring(0, index), replaceEscapeChars(keyValue.substring(index + 1)));
				} else { // the subjob won't escape the especial chars
					context_param.put(keyValue.substring(0, index), keyValue.substring(index + 1));
				}
			}
		} else if (arg.startsWith("--context_file")) {
			String keyValue = arg.substring(15);
			String filePath = new String(java.util.Base64.getDecoder().decode(keyValue));
			java.nio.file.Path contextFile = java.nio.file.Paths.get(filePath);
			try (java.io.BufferedReader reader = java.nio.file.Files.newBufferedReader(contextFile)) {
				String line;
				while ((line = reader.readLine()) != null) {
					int index = -1;
					if ((index = line.indexOf('=')) > -1) {
						if (line.startsWith("--context_param")) {
							if ("id_Password".equals(context_param.getContextType(line.substring(16, index)))) {
								context_param.put(line.substring(16, index),
										routines.system.PasswordEncryptUtil.decryptPassword(line.substring(index + 1)));
							} else {
								context_param.put(line.substring(16, index), line.substring(index + 1));
							}
						} else {// --context_type
							context_param.setContextType(line.substring(15, index), line.substring(index + 1));
						}
					}
				}
			} catch (java.io.IOException e) {
				System.err.println("Could not load the context file: " + filePath);
				e.printStackTrace();
			}
		} else if (arg.startsWith("--log4jLevel=")) {
			log4jLevel = arg.substring(13);
		} else if (arg.startsWith("--audit.enabled") && arg.contains("=")) {// for trunjob call
			final int equal = arg.indexOf('=');
			final String key = arg.substring("--".length(), equal);
			System.setProperty(key, arg.substring(equal + 1));
		}
	}

	private static final String NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY = "<TALEND_NULL>";

	private final String[][] escapeChars = { { "\\\\", "\\" }, { "\\n", "\n" }, { "\\'", "\'" }, { "\\r", "\r" },
			{ "\\f", "\f" }, { "\\b", "\b" }, { "\\t", "\t" } };

	private String replaceEscapeChars(String keyValue) {

		if (keyValue == null || ("").equals(keyValue.trim())) {
			return keyValue;
		}

		StringBuilder result = new StringBuilder();
		int currIndex = 0;
		while (currIndex < keyValue.length()) {
			int index = -1;
			// judege if the left string includes escape chars
			for (String[] strArray : escapeChars) {
				index = keyValue.indexOf(strArray[0], currIndex);
				if (index >= 0) {

					result.append(keyValue.substring(currIndex, index + strArray[0].length()).replace(strArray[0],
							strArray[1]));
					currIndex = index + strArray[0].length();
					break;
				}
			}
			// if the left string doesn't include escape chars, append the left into the
			// result
			if (index < 0) {
				result.append(keyValue.substring(currIndex));
				currIndex = currIndex + keyValue.length();
			}
		}

		return result.toString();
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public String getStatus() {
		return status;
	}

	ResumeUtil resumeUtil = null;
}
/************************************************************************************************
 * 448693 characters generated by Talend Cloud Data Fabric on the 7 January 2026
 * at 2:23:42 pm PST
 ************************************************************************************************/